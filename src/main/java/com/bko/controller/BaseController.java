package com.bko.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.bko.domain.DeploymentRequest;
import com.bko.service.DeploymentRequestService;
import com.bko.service.PatchService;
import com.bko.viewresolver.util.Shell;
import com.jcraft.jsch.JSchException;

public class BaseController {
	private static final Logger logger = LoggerFactory.getLogger(GenerationController.class);
	
	@Autowired
	protected DeploymentRequestService deploymentRequestService;
    @Autowired
    protected PatchService patchService;
    
    @Value("${env.name}")
    protected String env_name;
    
    @Value("${spring.datasource.url}")
    protected String db_url;
    
    @Value("${env.name.host}")
    protected String host_name;
    
    @Value("${env.name.login}")
    protected String host_login;
    
    @Value("${env.name.password}")
    protected String host_password;
    
    @Value("${ssh.key.file}")
    protected String ssh_key_file;
    
    @Value("${ssh.key.pass}")
    protected String ssh_key_pass;
    
    protected Shell shell;
    
    
    protected DeploymentRequest deploymentRequest;
    
    public void initialize_controller() throws JSchException{
    	
    	this.deploymentRequest = new DeploymentRequest();
    	this.shell = new Shell();
    	
    	
    	
    	this.deploymentRequest.setHostName(host_name);
		this.deploymentRequest.setHostLogin(host_login);
		this.deploymentRequest.setHostPassword(host_password);
    	
    	int length = this.host_password.length();//for debu purpose --> TODO REMOVE
		if (this.host_password.length() != 0)
			this.shell.intialize_and_connect(host_name, host_login, host_password);
		else
			this.shell.intialize_and_connect(host_name, host_login,ssh_key_file,ssh_key_pass );
		
		
    }
    
    public void set_DR(DeploymentRequest deploymentRequest){
    	
    	String deploymentRequestName = deploymentRequest.getDrName();
		
		deploymentRequest.setPatchList(deploymentRequestService.getPatchList(deploymentRequestName));
		deploymentRequest.setSynopsis(deploymentRequestService.getSynopsis(deploymentRequestName));
		deploymentRequest.setNumberOfPatches(deploymentRequestService.getNumberOfPatches(deploymentRequestName));
		deploymentRequest.setNumberOfTransferOperations(deploymentRequestService.getnumberOfTransferOperations(deploymentRequestName));
		deploymentRequest.setNumberOfManualTransferOperations(deploymentRequestService.getNumberOfManualTransferOperations(deploymentRequestName));
		deploymentRequest.setNumberOfSubjects(deploymentRequestService.getNumberOfSubjects(deploymentRequestName));
    	
    }
    public void set_DR(String deploymentRequestName){
    	
    	//this.deploymentRequest = new DeploymentRequest();
		this.deploymentRequest.setDrName(deploymentRequestName);
		this.deploymentRequest.setPatchList(deploymentRequestService.getPatchList(deploymentRequestName));
		this.deploymentRequest.setNumberOfPatches(deploymentRequestService.getNumberOfPatches(deploymentRequestName));
		this.deploymentRequest.setNumberOfTransferOperations(deploymentRequestService.getnumberOfTransferOperations(deploymentRequestName));

		this.deploymentRequest.setNumberOfManualTransferOperations(deploymentRequestService.getNumberOfManualTransferOperations(deploymentRequestName));
		this.deploymentRequest.setNumberOfSubjects(deploymentRequestService.getNumberOfSubjects(deploymentRequestName));
		
		this.deploymentRequest.setMemberList(deploymentRequestService.getDRMembers(deploymentRequestName));
		//this.deploymentRequest.setObjectList(deploymentRequestService.);
		

		this.deploymentRequest.setHostName(host_name);
		this.deploymentRequest.setHostLogin(host_login);
		this.deploymentRequest.setHostPassword(host_password);
		
   	
    }

}
