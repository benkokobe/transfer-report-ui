package com.bko.viewresolver.util;

import java.io.IOException;
import java.util.List;


import com.bko.domain.DeploymentRequest;
import com.bko.domain.Patch;
import com.jcraft.jsch.JSchException;

public class DeploymentRequestSynergy {
	/* public DeploymentRequestSynergy(DeploymentRequest deploymentRequest) {
		super();
		this.deploymentRequest = deploymentRequest;
	}
	*/

	private DeploymentRequest deploymentRequest;
	

	private Shell shell;
	List<SynergyObject>  synergyObjects;
	List<Patch> patchList;
	
	
	//synergyObjects = this.shell.execute_command(query);
	
	public void getObjectsLinkedToDr() throws JSchException, IOException{
				
		String query = "ccm query \"is_associated_cv_of(is_associated_task_of(is_associated_patch_of(dr_name = '"
				+ this.deploymentRequest.getDrName()
				+ "'"
				+ "))) \" "
	    		+ "                      -u -f \"%task|%name|%version|%type|%instance|%task_synopsis|%release";

		synergyObjects = this.shell.execute_command(query);
		this.deploymentRequest.setObjectList(synergyObjects);
	}
	public Shell getShell() {
		return shell;
	}
	public void setShell(Shell shell) {
		this.shell = shell;
	}
	public DeploymentRequest getDeploymentRequest() {
		return deploymentRequest;
	}
	public void setDeploymentRequest(DeploymentRequest deploymentRequest) {
		this.deploymentRequest = deploymentRequest;
	}
	public void getPatchList() throws JSchException, IOException{
		
		patchList = this.shell.execute_query_patch_list(this.deploymentRequest.getDrName());
		this.deploymentRequest.setPatchList(patchList);
		
	}
}
