package com.bko.domain;

/*
 * This class is an encapsulation of the line in a generated CSV file by the advanced report
 * Patch ID;Reference;Status;Group;Subject;Environment;Analysis author;Development author;
 * Review author;Project code;MR number;CR status;TOTAL;Install PCP;TOTAL PCP;DR Name PCP;
 * DR Status PCP;Install PET;TOTAL PET;DR Name PET;DR Status PET
 */
public class ReportLine {
	String crId;
	String patchReference;
	String status;
	String group;
	String subject;
	String environment;
	String analyst;
	String developer;
	String reviewer;
	String projectCode;
	String mrNumber;
	String crStatus;
	
	String installSource;
	String installDestination;
	
	public String getInstallSource() {
		return installSource;
	}
	public void setInstallSource(String installSource) {
		this.installSource = installSource;
	}
	public String getInstallDestination() {
		return installDestination;
	}
	public void setInstallDestination(String installDestination) {
		this.installDestination = installDestination;
	}
	public String getCrId() {
		return crId;
	}
	public void setCrId(String crId) {
		this.crId = crId;
	}
	public String getPatchReference() {
		return patchReference;
	}
	public void setPatchReference(String patchReference) {
		this.patchReference = patchReference;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getGroup() {
		return group;
	}
	public void setGroup(String group) {
		this.group = group;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getEnvironment() {
		return environment;
	}
	public void setEnvironment(String environment) {
		this.environment = environment;
	}
	public String getAnalyst() {
		return analyst;
	}
	public void setAnalyst(String analyst) {
		this.analyst = analyst;
	}
	public String getDeveloper() {
		return developer;
	}
	public void setDeveloper(String developer) {
		this.developer = developer;
	}
	public String getReviewer() {
		return reviewer;
	}
	public void setReviewer(String reviewer) {
		this.reviewer = reviewer;
	}
	public String getProjectCode() {
		return projectCode;
	}
	public void setProjectCode(String projectCode) {
		this.projectCode = projectCode;
	}
	public String getMrNumber() {
		return mrNumber;
	}
	public void setMrNumber(String mrNumber) {
		this.mrNumber = mrNumber;
	}
	public String getCrStatus() {
		return crStatus;
	}
	public void setCrStatus(String crStatus) {
		this.crStatus = crStatus;
	}

    

}