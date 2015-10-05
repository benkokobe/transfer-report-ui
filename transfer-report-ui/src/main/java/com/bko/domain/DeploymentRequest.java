package com.bko.domain;

import java.util.List;

public class DeploymentRequest {

	private String drName;
	private List<Patch> patchList;
	
	private int numberOfPatches;
	private int numberOfTransferOperations;
	private int numberOfManualTransferOperations;
	
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
