package com.bko.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.bko.domain.DeploymentRequest;
import com.bko.service.DeploymentRequestService;
import com.bko.service.PatchService;
import com.bko.viewresolver.util.ReleaseManager;
import com.bko.viewresolver.util.SynergyShell;
import com.jcraft.jsch.JSchException;

public class BaseController2 {

	private static final Logger logger = LoggerFactory.getLogger(BaseController2.class);

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

	protected SynergyShell shell;

	protected ReleaseManager releaseManager;

	public void initialize_release() throws JSchException {

		this.releaseManager = new ReleaseManager();
		this.shell = new SynergyShell();

		if (this.host_password.length() != 0)
			this.shell.intialize_and_connect(host_name, host_login, host_password);
		else
			this.shell.intialize_and_connect(host_name, host_login, ssh_key_file, ssh_key_pass);
	}

}
