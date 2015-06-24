package com.bko.controller;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

/**
 * Handles requests for the application home page.
 */
@Controller
@RequestMapping(value = "/")
public class GenerationController {
	
	private static final Logger logger = LoggerFactory.getLogger(GenerationController.class);
	
	@Autowired
	private DeploymentRequestService deploymentRequestService;
    @Autowired
	private PatchService patchService;
    
    
    
    private DeploymentRequest deploymentRequest;
    
	@RequestMapping(method = RequestMethod.GET)
	public String home(Locale locale, Model model) {
		logger.info("Welcome home! The client locale is {}.", locale);
		
		Date date = new Date();
		DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, locale);
		
		String formattedDate = dateFormat.format(date);
		this.deploymentRequest = new DeploymentRequest();
		//this.deploymentRequest.setDrName(deploymentRequestName);
		model.addAttribute("deploymentRequest", this.deploymentRequest);
		model.addAttribute("serverTime", formattedDate );
		
		return "start";
	}
	@RequestMapping(method = RequestMethod.POST)
    public String submitForm(@ModelAttribute("deploymentRequest")
    DeploymentRequest deploymentRequest, BindingResult result, Model model) {
		
		String deploymentRequestName = deploymentRequest.getDrName();
		//this.deploymentRequest = new DeploymentRequest();
		
		deploymentRequest.setPatchList(deploymentRequestService.getPatchList(deploymentRequestName));
		deploymentRequest.setNumberOfPatches(deploymentRequestService.getNumberOfPatches(deploymentRequestName));
		deploymentRequest.setNumberOfTransferOperations(deploymentRequestService.getnumberOfTransferOperations(deploymentRequestName));
		
		logger.info("getNumberOfPatches: " + deploymentRequest.getNumberOfPatches());
		
		return "DRdetails";
	}
	//@RequestMapping(value="generate*", method = RequestMethod.GET)
	@RequestMapping(value= "/generate.xls", method = RequestMethod.GET)
	public String submitForm(@RequestParam(required = true, value = "drName") String deploymentRequestName, 
			Model model) {
		
		this.deploymentRequest = new DeploymentRequest();
		this.deploymentRequest.setDrName(deploymentRequestName);
		this.deploymentRequest.setPatchList(deploymentRequestService.getPatchList(deploymentRequestName));
		this.deploymentRequest.setNumberOfPatches(deploymentRequestService.getNumberOfPatches(deploymentRequestName));
		this.deploymentRequest.setNumberOfTransferOperations(deploymentRequestService.getnumberOfTransferOperations(deploymentRequestName));

		logger.info("To generate an excel document:" + deploymentRequestName);
		logger.info("Dr nbr of patches:" + deploymentRequest.getNumberOfPatches());
		
		model.addAttribute("deploymentRequest",this.deploymentRequest);
		return "DRSummary";
		
	}
	
}
