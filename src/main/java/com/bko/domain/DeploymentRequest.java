package com.bko.domain;

import java.util.List;

<<<<<<< HEAD
import com.bko.viewresolver.util.SynergyObject;

//import com.bko.SynergyObject;

=======
>>>>>>> 705648a88b089efef9ae9a6b68c1902379dcc7c5
public class DeploymentRequest {

	private String drName;
	private List<Patch> patchList;
	private List<DeploymentRequestTransferOperation> deploymentRequestTransferOperation;
<<<<<<< HEAD
	
	private List<PatchMember> memberList;
	
	private List<SynergyObject> objectList;
	
	public List<PatchMember> getMemberList() {
		return memberList;
	}
	public void setMemberList(List<PatchMember> memberList) {
		this.memberList = memberList;
	}
=======
>>>>>>> 705648a88b089efef9ae9a6b68c1902379dcc7c5
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
	
<<<<<<< HEAD

	private String hostName;
	private String hostLogin;
	private String hostPassword;
	
	
	public String getHostName() {
		return hostName;
	}
	public void setHostName(String hostName) {
		this.hostName = hostName;
	}
	public String getHostLogin() {
		return hostLogin;
	}
	public void setHostLogin(String hostLogin) {
		this.hostLogin = hostLogin;
	}
	public String getHostPassword() {
		return hostPassword;
	}
	public void setHostPassword(String hostPassword) {
		this.hostPassword = hostPassword;
	}
	
	
=======
>>>>>>> 705648a88b089efef9ae9a6b68c1902379dcc7c5
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
<<<<<<< HEAD
	public List<SynergyObject> getObjectList() {
		return objectList;
	}
	public void setObjectList(List<SynergyObject> objectList) {
		this.objectList = objectList;
	}
=======
>>>>>>> 705648a88b089efef9ae9a6b68c1902379dcc7c5


}
