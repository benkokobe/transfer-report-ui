package com.bko.controller;

import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value = "/about")
public class AboutController {
	private static final Logger logger = LoggerFactory.getLogger(AboutController.class);

	@Value("${env.name}")
	private String env_name;

	@RequestMapping(method = RequestMethod.GET)
	public String home(Locale locale, Model model) {

		model.addAttribute("env_name", env_name );

		return "about";
	}

}
