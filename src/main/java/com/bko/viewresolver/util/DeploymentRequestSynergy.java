package com.bko.viewresolver.util;

import java.io.IOException;
import java.util.List;

import com.bko.domain.DeploymentRequest;
import com.bko.domain.Patch;
import com.jcraft.jsch.JSchException;

public class DeploymentRequestSynergy {

	private DeploymentRequest deploymentRequest;

	private SynergyShell synergyShell;
	List<SynergyObject> synergyObjects;
	List<Patch> patchList;

	public void setObjectsLinkedToDR(String drName) throws JSchException, IOException {
		synergyObjects = this.synergyShell.getObjectsLinkedToDR(drName);
		this.deploymentRequest.setObjectList(synergyObjects);
	}

	public SynergyShell getShell() {
		return synergyShell;
	}

	public void setShell(SynergyShell shell) {
		this.synergyShell = shell;
	}

	public DeploymentRequest getDeploymentRequest() {
		return deploymentRequest;
	}

	public void setDeploymentRequest(DeploymentRequest deploymentRequest) {
		this.deploymentRequest = deploymentRequest;
	}

	public void getPatchList() throws JSchException, IOException {

		patchList = this.synergyShell.execute_query_patch_list(this.deploymentRequest.getDrName());
		this.deploymentRequest.setPatchList(patchList);

	}

	public void setDeploymentRequestInfo(DeploymentRequest deploymentRequest) throws JSchException, IOException {
		this.synergyShell.setDeploymentRequestInfo(deploymentRequest);

	}
}
