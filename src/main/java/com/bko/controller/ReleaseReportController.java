package com.bko.controller;

import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.bko.domain.DeploymentRequest;
import com.jcraft.jsch.JSchException;

@Controller
@RequestMapping(value = "/")
public class ReleaseReportController extends BaseController {

	private static final Logger logger = LoggerFactory.getLogger(ReleaseReportController.class);


	@RequestMapping(method = RequestMethod.GET)
	public String home(Locale locale, Model model) throws JSchException {
		
		initialize_controller();
		model.addAttribute("deploymentRequest", this.deploymentRequest);
		model.addAttribute("env_name", env_name);
		
		logger.info("env_name:" + env_name);
		return "release-reporter2";
	}
	@RequestMapping(method = RequestMethod.POST)
    public String submitForm(@ModelAttribute("deploymentRequest")
    DeploymentRequest deploymentRequest, BindingResult result, Model model) throws JSchException {
		
		setDeploymentRequest(deploymentRequest);
		//for JSP
		model.addAttribute("env_name", env_name );
		
		logger.info("getNumberOfPatches: " + deploymentRequest.getNumberOfPatches());
		
		return "DRdetails";
	}
	@RequestMapping(value= "/*", method = RequestMethod.GET)
	public String submitForm(@RequestParam(required = true, value = "drName") String deploymentRequestName, 
				Model model) throws JSchException {
			
		    setDeploymentRequest(deploymentRequestName);
			//for Excel sheet
			model.addAttribute("deploymentRequest",this.deploymentRequest);
			model.addAttribute("shell", this.shell);
			
			logger.info("To generate an excel document:" + deploymentRequestName);
			logger.info("Dr nbr of patches:" + deploymentRequest.getNumberOfPatches());

			return "DRSummary";
			
		}

}
