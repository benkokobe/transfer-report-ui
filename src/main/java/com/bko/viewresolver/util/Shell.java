package com.bko.viewresolver.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.bko.domain.Patch;
import com.jcabi.ssh.*;
import com.jcabi.ssh.SSH;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

public class Shell {
	public enum QueryField {
		//%task, %name, %version, %type, %instance, %task_synopsis, %release
		TASK(0), NAME(1), VERSION(2), TYPE(3), INSTANCE(4), TASK_SYNOPSIS(5), RELEASE(6);

		private int field;

		QueryField(int field) {
			this.field = field;
		}
		public int result(){
			return field;
		}

	}
	private Properties config;
	private Session    session;
	private Channel channel;
	private ChannelExec channel_exec;
	
	private List<SynergyObject>  synergyObjects;
	private List<Patch>    patchList;
	
	public void intialize_and_connect (String host, String login, String password) throws JSchException{
		
		if (password.length() == 0){
				intialize_and_connect(host,login);
				return;
		}
		JSch js = new JSch();
		session = js.getSession(login, host, 22);
		session.setPassword(password);
	    
		//session.setPassword("Thaler?123");
	    config = new Properties();
	    config.put("StrictHostKeyChecking", "no");
	    session.setConfig(config);
	    session.connect();
	}
	public void intialize_and_connect (String host, String login) throws JSchException{
		JSch js = new JSch();
		//js.setKnownHosts("C:\\Users\\bkokobe\\.ssh\\known_hosts");
		//js.addIdentity("C:\\thalerng\\config\\bko_priv_rsa.ppk", "bko");
		session = js.getSession(login, host, 22);
		//session.setPassword(password);
	    
		//session.setPassword("Thaler?123");
	    config = new Properties();
	    config.put("StrictHostKeyChecking", "no");
	    
	    session.setConfig("PreferredAuthentications", "publickey,keyboard-interactive,password");
	    //C:\Users\bkokobe\Documents\RIC\ssh_keys\SSH-2-RSA-4096
	    
	    
	    session.setConfig("StrictHostKeyChecking", "no");
	    session.connect();
	}
	/*
	 * 
	 */
	public void intialize_and_connect (String host, String login, String key, String pass) throws JSchException{
		JSch js = new JSch();
		//js.setKnownHosts("C:\\Users\\bkokobe\\.ssh\\known_hosts");
		js.addIdentity(key, pass);
		session = js.getSession(login, host, 22);
	    
		//session.setPassword("Thaler?123");
	    config = new Properties();
	    config.put("StrictHostKeyChecking", "no");
	    
	    //session.setConfig("PreferredAuthentications", "publickey,keyboard-interactive,password");
	    //C:\Users\bkokobe\Documents\RIC\ssh_keys\SSH-2-RSA-4096
	    
	    
	    session.setConfig("StrictHostKeyChecking", "no");
	    session.setConfig("PreferredAuthentications", "publickey,keyboard-interactive,password");
	    session.connect();
	}
	public List<Patch>  execute_query_patch_list(String drName) throws JSchException, IOException{
		
        patchList = new ArrayList<Patch>();
		
		channel = session.openChannel("exec");
		channel_exec = (ChannelExec) channel;
		String profile = "source $HOME/.profile;";
		String query = "ccm query \"problem_type='patch' and is_associated_patch_of(dr_name = '"
				+ drName
				+ "'"
				+ ")\" "
	    		+ "                      -u -f \"%reference|%group|%code|%crstatus|%evolution_type|%problem_synopsis";
		
		String complete_command = profile + query;
		channel_exec.setCommand(complete_command);
		channel_exec.setErrStream(System.err);
		
		channel_exec.connect();

	    BufferedReader reader = new BufferedReader(new InputStreamReader(channel_exec.getInputStream()));
	    String line;
	    int j =0;
	    //SynergyObject synergyObject;
	    Patch patch;
	    while ((line = reader.readLine()) != null) {
	    	
	    	patch  = new Patch();
	    	String[] tokens = line.split("\\|");

	    	System.out.print("Patch:" + tokens[0]);
	    	System.out.print("Group:" + tokens[1]);
	    	System.out.println("Subject:" + tokens[2]);
	    	patch.setPatchId(tokens[0]);
	    	patch.setNomGrp(tokens[1]);
	    	patch.setSujPat(tokens[2]);
	    	patch.setStatus(tokens[3]);
	    	patch.setTypEvl(tokens[4]);
	    	patch.setSynopsis(tokens[5]);
	    	patchList.add(patch);
	    }
	    for (Patch p : patchList){
	    	System.out.println("Object: " + p.getPatchId());
	    }

	    channel_exec.disconnect();
	    //session.disconnect();

	    System.out.println("Exit code: " + channel_exec.getExitStatus());
		return this.patchList;	
		
		
	}
	public Session getSession() {
		return session;
	}
	public void setSession(Session session) {
		this.session = session;
	}
	public Channel getChannel() {
		return channel;
	}
	public void setChannel(Channel channel) {
		this.channel = channel;
	}
	public List<SynergyObject> execute_command(String command) throws JSchException, IOException{
		
		synergyObjects = new ArrayList<SynergyObject>();
		
		channel = session.openChannel("exec");
		channel_exec = (ChannelExec) channel;
		String profile = "source $HOME/.profile;";
		
		String complete_command = profile + command;
		channel_exec.setCommand(complete_command);
		channel_exec.setErrStream(System.err);
		
		channel_exec.connect();

	    BufferedReader reader = new BufferedReader(new InputStreamReader(channel_exec.getInputStream()));
	    String line;
	    int j =0;
	    SynergyObject synergyObject;
	    
	    while ((line = reader.readLine()) != null) {
	    	//System.out.println("line:" + line);
	    	String[] tokens = line.split("\\|");
	    	//int i = QueryField.TASK;
	    	//%task, %name, %version, %type, %instance, %task_synopsis, %release
	    	System.out.print("TASK:" + tokens[QueryField.TASK.result()].trim());
	    	System.out.print("-- NAME:" + tokens[QueryField.NAME.result()].trim());
	    	System.out.println("-- VERSION:" + tokens[QueryField.VERSION.result()].trim());
	    	
	    	synergyObject = new SynergyObject();
	    	synergyObject.setTask(tokens[QueryField.TASK.result()].trim());
	    	synergyObject.setObject(tokens[QueryField.NAME.result()].trim());
	    	synergyObject.setType(tokens[QueryField.TYPE.result()].trim());
	    	synergyObject.setVersion(tokens[QueryField.VERSION.result()].trim());
	    	synergyObject.setInstance(tokens[QueryField.INSTANCE.result()].trim());
	    	
	    	this.synergyObjects.add(synergyObject);    	
	    	
	    	
	    }
	    for (SynergyObject obj : synergyObjects){
	    	System.out.println("Object: " + obj.getObject());
	    }

	    channel_exec.disconnect();
	    //session.disconnect();

	    System.out.println("Exit code: " + channel_exec.getExitStatus());
		return this.synergyObjects;	
		
		
	}

	//public static void main(String[] arg) throws IOException, JSchException {
	public void main(String[] arg) throws IOException, JSchException {
		
		

		//http://stackoverflow.com/questions/2405885/any-good-jsch-examples
		//https://nikunjp.wordpress.com/2011/07/30/remote-ssh-using-jsch-with-expect4j/
		
		/*
		 JSch js = new JSch();
	    Session s = js.getSession("cwfbm", "vmpsasynprd", 22);
	    s.setPassword("Thaler?123");
	    Properties config = new Properties();
	    config.put("StrictHostKeyChecking", "no");
	    s.setConfig(config);
	    s.connect();

	    Channel c = s.openChannel("exec");
	    ChannelExec ce = (ChannelExec) c;

	    //ce.setCommand("source $HOME/.profile;ccm query \"is_associated_cv_of(is_associated_task_of(reference='Z01113'))\" -u ");
	    
	    String profile = "source $HOME/.profile;";
	    //String query = "ccm query \"is_associated_cv_of(is_associated_task_of(reference='Z01113'))";
	    String query = "ccm query \"is_associated_cv_of(is_associated_task_of(is_associated_patch_of(dr_name = 'PACK-PRG-0051'))) \" "
	    		+ "                      -u -f \"%task, %name, %version, %type, %instance, %task_synopsis, %release";
	    
	    String cmd = profile + query;
	    ce.setCommand(cmd);
	    ce.setErrStream(System.err);

	    ce.connect();

	    BufferedReader reader = new BufferedReader(new InputStreamReader(ce.getInputStream()));
	    String line;
	    int j =0;
	    while ((line = reader.readLine()) != null) {
	    	//System.out.println("line:" + line);
	    	String[] tokens = line.split(",");
	    	//int i = QueryField.TASK;
	    	//%task, %name, %version, %type, %instance, %task_synopsis, %release
	    	System.out.print("TASK:" + tokens[QueryField.TASK.result()].trim());
	    	System.out.print("NAME:" + tokens[QueryField.NAME.result()].trim());
	    	System.out.println("TYPE:" + tokens[QueryField.TYPE.result()].trim());
	    }

	    ce.disconnect();
	    s.disconnect();

	    System.out.println("Exit code: " + ce.getExitStatus());
	    */
		String query = "ccm query \"is_associated_cv_of(is_associated_task_of(is_associated_patch_of(dr_name = 'PACK-PRG-0037'))) \" "
	    		+ "                      -u -f \"%task|%name|%version|%type|%instance|%task_synopsis|%release";
		Shell shell = new Shell();
		String key = "C:\\thalerng\\config\\bko_priv_rsa.ppk";
		String pass = "bko";
		shell.intialize_and_connect("scm.com.saas.i2s", "bkokobe", key, pass);
		//shell.execute_command(query);
		String drName = "PACK-PRG-0037";
		shell.execute_query_patch_list(drName);
		
		
	}
}
