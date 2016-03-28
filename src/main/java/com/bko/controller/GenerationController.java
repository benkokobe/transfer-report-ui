package com.bko.controller;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.bko.domain.DeploymentRequest;
import com.bko.service.DeploymentRequestService;
import com.bko.service.PatchService;
import com.bko.viewresolver.util.Shell;
import com.jcraft.jsch.JSchException;

/**
 * Handles requests for the application home page.
 */
//@Controller
//@RequestMapping(value = "/")
public class GenerationController {
	
	private static final Logger logger = LoggerFactory.getLogger(GenerationController.class);
	
	@Autowired
	private DeploymentRequestService deploymentRequestService;
    @Autowired
	private PatchService patchService;
    
    @Value("${env.name}")
	private String env_name;
    
    @Value("${spring.datasource.url}")
    private String db_url;
    
    @Value("${env.name.host}")
    private String host_name;
    
    @Value("${env.name.login}")
    private String host_login;
    
    @Value("${env.name.password}")
    private String host_password;
    
    @Value("${ssh.key.file}")
    private String ssh_key_file;
    
    @Value("${ssh.key.pass}")
    private String ssh_key_pass;
    
    private Shell shell;
    
    private DeploymentRequest deploymentRequest;
    
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

		this.deploymentRequest.setHostName(host_name);
		this.deploymentRequest.setHostLogin(host_login);
		this.deploymentRequest.setHostPassword(host_password);
		
   	
    }
    
	@RequestMapping(method = RequestMethod.GET)
	public String home(Locale locale, Model model) throws JSchException {
		logger.info("Welcome home! The client locale is {}.", locale);
		
		Date date = new Date();
		DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, locale);
		
		String formattedDate = dateFormat.format(date);
		
		initialize_controller();
		//set_DR();
		
		//this.deploymentRequest.setDrName(deploymentRequestName);
		model.addAttribute("deploymentRequest", this.deploymentRequest);
		model.addAttribute("shell", this.shell);
		this.deploymentRequest = new DeploymentRequest();
		//this.deploymentRequest.setDrName(deploymentRequestName);
		model.addAttribute("deploymentRequest", this.deploymentRequest);
		logger.info("env_name:" + env_name);
		model.addAttribute("env_name", env_name );
		model.addAttribute("db_url", db_url );
		
		return "start";
	}
	@RequestMapping(method = RequestMethod.POST)
    public String submitForm(@ModelAttribute("deploymentRequest")
    DeploymentRequest deploymentRequest, BindingResult result, Model model) throws JSchException {
		
		
		set_DR(deploymentRequest);
		
		String deploymentRequestName = deploymentRequest.getDrName();
		//this.deploymentRequest = new DeploymentRequest();
		
		deploymentRequest.setPatchList(deploymentRequestService.getPatchList(deploymentRequestName));
		deploymentRequest.setSynopsis(deploymentRequestService.getSynopsis(deploymentRequestName));
		deploymentRequest.setNumberOfPatches(deploymentRequestService.getNumberOfPatches(deploymentRequestName));
		deploymentRequest.setNumberOfTransferOperations(deploymentRequestService.getnumberOfTransferOperations(deploymentRequestName));
		deploymentRequest.setNumberOfManualTransferOperations(deploymentRequestService.getNumberOfManualTransferOperations(deploymentRequestName));
		deploymentRequest.setNumberOfSubjects(deploymentRequestService.getNumberOfSubjects(deploymentRequestName));
		
		logger.info("getNumberOfPatches: " + deploymentRequest.getNumberOfPatches());
		
		model.addAttribute("env_name", env_name );
		model.addAttribute("db_url", db_url );
		
		
		return "DRdetails";
	}
	//@RequestMapping(value="generate*", method = RequestMethod.GET)
	@RequestMapping(value= "/*", method = RequestMethod.GET)
	public String submitForm(@RequestParam(required = true, value = "drName") String deploymentRequestName, 
			Model model) throws JSchException {
		
		
		set_DR(deploymentRequestName);
		
		
		this.deploymentRequest = new DeploymentRequest();
		this.deploymentRequest.setDrName(deploymentRequestName);
		this.deploymentRequest.setPatchList(deploymentRequestService.getPatchList(deploymentRequestName));
		this.deploymentRequest.setNumberOfPatches(deploymentRequestService.getNumberOfPatches(deploymentRequestName));
		this.deploymentRequest.setNumberOfTransferOperations(deploymentRequestService.getnumberOfTransferOperations(deploymentRequestName));

		logger.info("To generate an excel document:" + deploymentRequestName);
		logger.info("Dr nbr of patches:" + deploymentRequest.getNumberOfPatches());
		
		model.addAttribute("deploymentRequest",this.deploymentRequest);
		model.addAttribute("shell", this.shell);
		return "DRSummary";
		
	}
	
}
