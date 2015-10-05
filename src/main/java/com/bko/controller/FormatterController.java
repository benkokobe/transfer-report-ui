package com.bko.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import scala.annotation.meta.getter;

import com.bko.domain.User;
import com.bko.service.PatchService;
import com.bko.service.PatchTaskService;
import com.bko.validators.FileValidator;
import com.bko.viewresolver.ConflictReport;
import com.bko.viewresolver.FormatterExcel;

@Controller
@RequestMapping(value = "/formatter")
public class FormatterController {

	@Value("${env.name}")
	private String env_name;
	private final Logger logger = Logger.getLogger(FormatterController.class);

	@Autowired
	private PatchTaskService patchTaskservice;
	@Autowired
	private PatchService patchService;

	private FormatterExcel formatterExcel;

	@Value("${pif.filter}")
	private String pifActivatedFilterFile;

	@Value("${pet.filter}")
	private String petActivatedFilterFile;

	@Value("${formatter.dir}")
	private String saveDirectory;

	@Autowired
	FileValidator validator;

	@InitBinder
	private void initBinder(WebDataBinder binder) {
		binder.setValidator(validator);
	}

	// private String saveDirectory = "C:/Test/Upload/";

	@RequestMapping(method = RequestMethod.GET)
	public String provideUploadInfo(Model model) {
		logger.info("generated names!");
		User usersLocal = new User();
		model.addAttribute("user", usersLocal);
		model.addAttribute("env_name", env_name);
		model.addAttribute("saveDirectory", this.saveDirectory);
		return "formatter";
	}

	@RequestMapping(method = RequestMethod.POST)
	public String submitForm(@ModelAttribute("user") @Validated User user,
			BindingResult result, RedirectAttributes redirctAttributes)
			throws IOException

	{
		if (result.hasErrors()) {
			logger.info("Error with input");
			return "formatter";
		}
		String fileToFormatName = user.getFileToFormat().getOriginalFilename();

		MultipartFile fileToFormat = user.getFileToFormat();

		String fileToCopy = this.saveDirectory + fileToFormatName;

		try {
			byte[] bytes = fileToFormat.getBytes();
			BufferedOutputStream stream = new BufferedOutputStream(
					new FileOutputStream(new File(fileToCopy)));
			stream.write(bytes);
			stream.close();
		} catch (Exception e) {
			return "You failed to upload " + fileToFormatName + " => "
					+ e.getMessage();
		}

		user.setFileToFormatName(fileToFormatName);

		this.formatterExcel = new FormatterExcel(fileToFormat,
				this.saveDirectory);
		this.formatterExcel.setPatchTaskservice(patchTaskservice);
		this.formatterExcel.setPatchService(patchService);

		this.formatterExcel.setPetActivatedFilterFile(petActivatedFilterFile);
		this.formatterExcel.setPifActivatedFilterFile(pifActivatedFilterFile);
		this.formatterExcel.readFile(user.getFileToFormat()
				.getOriginalFilename());
		this.formatterExcel.generateFormattedFile();

		redirctAttributes.addFlashAttribute("flashkind", "success");
		redirctAttributes.addFlashAttribute("flashMessage",
				"Success: The file is generated in the directory: "
						+ this.saveDirectory + fileToFormatName);

		return "redirect:/formatter";
		// return "formatterResult";
	}

}
