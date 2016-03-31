package com.bko.viewresolver.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.bko.domain.DeploymentRequest;
import com.bko.domain.Patch;
import com.jcabi.ssh.*;
import com.jcabi.ssh.SSH;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

public class SynergyShell {
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
	/**
	 * This method gives the a DR's meta data such as source, destination synopsis of a DR
	 * @param deploymentRequest
	 * @throws JSchException
	 * @throws IOException
	 */
	public void setDeploymentRequestInfo(DeploymentRequest deploymentRequest) throws JSchException, IOException{
		
		String drName = deploymentRequest.getDrName();
		String query = "ccm query \"problem_type='dr' and dr_name = '"
				+ drName 
				+ "'"
				+ "\" "
				+ "                      -u -f \"%source_environment|%destination_environment|%problem_synopsis";
		
		
		sessionConnectAndExecCommand(query);

	    BufferedReader reader = new BufferedReader(new InputStreamReader(channel_exec.getInputStream()));
	    String line;
	    
	    while ((line = reader.readLine()) != null) {
	    	
	    	String[] tokens = line.split("\\|");

	    	System.out.print("Source Env:" + tokens[0]);
	    	System.out.print("Destination Env:" + tokens[1]);
	    	System.out.println("Synopsis:" + tokens[2]);
	    	deploymentRequest.setEnvSrc(tokens[0]);
	    	deploymentRequest.setEnvDst(tokens[1]);
	    	deploymentRequest.setSynopsis(tokens[2]);

	    }

	    //channel_exec.disconnect();

	    System.out.println("Exit code: " + channel_exec.getExitStatus());
		
	}
	/**
	 * This method returns list of patches linked to a given DR drName
	 * @param drName
	 * @return
	 * @throws JSchException
	 * @throws IOException
	 */
	public List<Patch>  execute_query_patch_list(String drName) throws JSchException, IOException{
		
        patchList = new ArrayList<Patch>();
		
		String query = "ccm query \"problem_type='patch' and is_associated_patch_of(dr_name = '"
				+ drName
				+ "'"
				+ ")\" "
	    		+ "                      -u -f \"%reference|%group|%code|%crstatus|%evolution_type|%problem_synopsis";
		
		
		sessionConnectAndExecCommand(query);

	    BufferedReader reader = new BufferedReader(new InputStreamReader(channel_exec.getInputStream()));
	    String line;
	    int j =0;
	    //SynergyObject synergyObject;
	    Patch patch;
	    while ((line = reader.readLine()) != null) {
	    	
	    	patch  = new Patch();
	    	String[] tokens = line.split("\\|");

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
	public void sessionConnectAndExecCommand(String query) throws JSchException{
		channel = session.openChannel("exec");
		channel_exec = (ChannelExec) channel;
		String profile = ". $HOME/.profile;";
		String complete_command = profile + query;
		channel_exec.setCommand(complete_command);
		channel_exec.setErrStream(System.err);
		channel_exec.connect();
		
	}
	public List<SynergyObject> getObjectsLinkedToDR(String drName) throws JSchException, IOException{
		
		String query = "ccm query \"is_associated_cv_of(is_associated_task_of(is_associated_patch_of(dr_name = '"
				+ drName
				+ "'"
				+ "))) \" "
	    		+ "                      -u -f \"%task|%name|%version|%type|%instance|%task_synopsis|%release";
		
		synergyObjects = new ArrayList<SynergyObject>();

		sessionConnectAndExecCommand(query);

	    BufferedReader reader = new BufferedReader(new InputStreamReader(channel_exec.getInputStream()));
	    String line;
	    int j =0;
	    SynergyObject synergyObject;
	    
	    while ((line = reader.readLine()) != null) {
	    	String[] tokens = line.split("\\|");
	    	
	    	//%task, %name, %version, %type, %instance, %task_synopsis, %release
    	
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
		SynergyShell shell = new SynergyShell();
		String key = "C:\\thalerng\\config\\bko_priv_rsa.ppk";
		String pass = "bko";
		shell.intialize_and_connect("scm.com.saas.i2s", "bkokobe", key, pass);
		//shell.execute_command(query);
		String drName = "PACK-PRG-0037";
		shell.execute_query_patch_list(drName);
		
		
	}
}
