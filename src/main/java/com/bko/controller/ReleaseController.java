package com.bko.controller;

import java.io.IOException;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.bko.viewresolver.util.ReleaseManager;
import com.jcraft.jsch.JSchException;

@Controller
@RequestMapping(value = "/")
public class ReleaseController extends BaseController2 {
	private static final Logger logger = LoggerFactory.getLogger(ReleaseController.class);
	
	@RequestMapping(method = RequestMethod.GET)
	public String home(Locale locale, Model model) throws JSchException {

		initialize_release();
		model.addAttribute("releaseManager", this.releaseManager);
		model.addAttribute("env_name", env_name);

		logger.info("env_name:" + env_name);
		return "release2";
	}

	@RequestMapping(method = RequestMethod.POST)
	public String submitForm(@ModelAttribute("releaseManager") ReleaseManager releaseManager,
			BindingResult result, Model model) throws JSchException, IOException {

		String drName= releaseManager.getReleaseName();
		releaseManager.initialize(drName, shell, deploymentRequestService);
		
		this.releaseManager = releaseManager;
		// for JSP
		model.addAttribute("env_name", env_name);

		logger.info("Release synopsis: " + releaseManager.getSynopsisOfRelease());
		this.releaseManager.closeSession();

		return "releaseDetails";
	}

	@RequestMapping(value = "/*", method = RequestMethod.GET)
	public String submitForm(@RequestParam(required = true, value = "releaseName") String releaseName, 
			Model model, RedirectAttributes redirctAttributes)
			throws JSchException, IOException {

		// for Excel sheet
		model.addAttribute("releaseManager", this.releaseManager);
		model.addAttribute("shell", this.shell);

		logger.info("To generate an excel document for release :" + releaseName);
		logger.info("Release synopsis                          :" + releaseManager.getSynopsisOfRelease());

		redirctAttributes.addFlashAttribute("flashkind", "success");
		redirctAttributes.addFlashAttribute("flashMessage",
				"Success: The file " + releaseName + ".xsl" + " is generated in your default download directory!");

		//return "redirect:/";
		return "releaseSummary";
		
	}

}
