package com.bko.controller;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.bko.domain.User;
import com.bko.service.PatchService;
import com.bko.service.PatchTaskService;
import com.bko.viewresolver.ConflictReport;

@Controller
@RequestMapping(value="/conflict")
public class ConflictReportController {
	private final Logger logger = Logger.getLogger(ConflictReportController.class);
	
	@Autowired
	private PatchTaskService patchTaskservice;
	@Autowired
	private PatchService patchService;
	
	private ConflictReport conflictReport;
	
	@Value("${env.name}")
	private String env_name;
	
	@Value("${pif.filter}")
	private String pifActivatedFilterFile;
	
	@Value("${pet.filter}")
	private String petActivatedFilterFile;
	
	@RequestMapping(method=RequestMethod.GET)
	public String home(Model model) {
		logger.info("conflict page!");
		//generateName();
		logger.info("generated names!");
		User usersLocal = new User();
		model.addAttribute("user", usersLocal);
		model.addAttribute("env_name", env_name );
		return "conflicts";
	}
	
	@RequestMapping(method = RequestMethod.POST)
	public String submitForm(@ModelAttribute("user") User user) throws IOException{
		//String dr = "PACK-TF0-0014";//this is over-written by properties file
		String conflictFile = user.getConflictFile();
		this.conflictReport = new ConflictReport(conflictFile);
		this.conflictReport.setPatchTaskservice(patchTaskservice);
		this.conflictReport.setPatchService(patchService);
		
		this.conflictReport.setPetActivatedFilterFile(petActivatedFilterFile);
		this.conflictReport.setPifActivatedFilterFile(pifActivatedFilterFile);
		
		logger.info("petActivatedFilterFile: " + petActivatedFilterFile);
		
		
		
		
	
		//this.conflictReport.setReportName(user.getConflictFile());
		logger.info("ConflictFile from session:" + user.getConflictFile());
		logger.info("ConflictFile from session:" + conflictReport.getReportName());
		this.conflictReport.readFile(conflictFile);
		this.conflictReport.generateConflictReport();

		return "resultConflicts";
		
	}

}
