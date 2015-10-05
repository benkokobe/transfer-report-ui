package com.bko.domain;

import java.util.List;

public class DeploymentRequest {

	private String drName;
	private List<Patch> patchList;
	private List<DeploymentRequestTransferOperation> deploymentRequestTransferOperation;
	public List<DeploymentRequestTransferOperation> getDeploymentRequestTransferOperation() {
		return deploymentRequestTransferOperation;
	}
	public void setDeploymentRequestTransferOperation(List<DeploymentRequestTransferOperation> deploymentRequestTransferOperation) {
		this.deploymentRequestTransferOperation = deploymentRequestTransferOperation;
	}
	private int numberOfPatches;
	private int numberOfTransferOperations;
	private int numberOfManualTransferOperations;
	private int numberOfSubjects;
	
	private String envDst;
	private String envSrc;
	private String synopsis;
	
	public String getEnvDst() {
		return envDst;
	}
	public void setEnvDst(String envDst) {
		this.envDst = envDst;
	}
	public String getEnvSrc() {
		return envSrc;
	}
	public void setEnvSrc(String envSrc) {
		this.envSrc = envSrc;
	}
	public String getSynopsis() {
		return synopsis;
	}
	public void setSynopsis(String synopsis) {
		this.synopsis = synopsis;
	}
	public int getNumberOfSubjects() {
		return numberOfSubjects;
	}
	public void setNumberOfSubjects(int numberOfSubjects) {
		this.numberOfSubjects = numberOfSubjects;
	}
	private List<PatchMember> patchMembersList;
	
	public List<PatchMember> getPatchMembersList() {
		return patchMembersList;
	}
	public void setPatchMembersList(List<PatchMember> patchMembersList) {
		this.patchMembersList = patchMembersList;
	}
	public String getDrName() {
		return drName;
	}
	public void setDrName(String drName) {
		this.drName = drName;
	}
	public List<Patch> getPatchList() {
		return patchList;
	}
	public void setPatchList(List<Patch> patchList) {
		this.patchList = patchList;
	}
	public int getNumberOfPatches() {
		return numberOfPatches;
	}
	public void setNumberOfPatches(int numberOfPatches) {
		this.numberOfPatches = numberOfPatches;
	}
	public int getNumberOfTransferOperations() {
		return numberOfTransferOperations;
	}
	public void setNumberOfTransferOperations(int numberOfTransferOperations) {
		this.numberOfTransferOperations = numberOfTransferOperations;
	}
	public int getNumberOfManualTransferOperations() {
		return numberOfManualTransferOperations;
	}
	public void setNumberOfManualTransferOperations(
			int numberOfManualTransferOperations) {
		this.numberOfManualTransferOperations = numberOfManualTransferOperations;
	}


}
