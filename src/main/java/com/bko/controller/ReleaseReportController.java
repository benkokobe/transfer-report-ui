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
import com.jcraft.jsch.JSchException;

@Controller
//@RequestMapping(value = "/release/")
@RequestMapping(value = "/")
public class ReleaseReportController extends BaseController {

	private static final Logger logger = LoggerFactory.getLogger(ReleaseReportController.class);


	@RequestMapping(method = RequestMethod.GET)
	public String home(Locale locale, Model model) throws JSchException {
		logger.info("This is release reporter 2! The client locale is {}.", locale);

		Date date = new Date();
		DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, locale);

		initialize_controller();

		model.addAttribute("deploymentRequest", this.deploymentRequest);
		model.addAttribute("shell", this.shell);
		logger.info("env_name:" + env_name);
		model.addAttribute("env_name", env_name);

		return "release-reporter2";
	}
	@RequestMapping(method = RequestMethod.POST)
    public String submitForm(@ModelAttribute("deploymentRequest")
    DeploymentRequest deploymentRequest, BindingResult result, Model model) throws JSchException {
		
		
		set_DR(deploymentRequest);
		
		logger.info("getNumberOfPatches: " + deploymentRequest.getNumberOfPatches());
		
		model.addAttribute("env_name", env_name );
		
		
		return "DRdetails";
	}
	//@RequestMapping(value="generate*", method = RequestMethod.GET)
	@RequestMapping(value= "/*", method = RequestMethod.GET)
	public String submitForm(@RequestParam(required = true, value = "drName") String deploymentRequestName, 
				Model model) throws JSchException {
			
			
			set_DR(deploymentRequestName);
			
			
			logger.info("To generate an excel document:" + deploymentRequestName);
			logger.info("Dr nbr of patches:" + deploymentRequest.getNumberOfPatches());
			
			model.addAttribute("deploymentRequest",this.deploymentRequest);
			model.addAttribute("shell", this.shell);
			return "DRSummary";
			
		}

}
