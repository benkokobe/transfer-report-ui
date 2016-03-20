package com.bko.domain;



import org.springframework.web.multipart.MultipartFile;


public class User {
	private String drName;
	private String reflot;
	private String conflictFile;
	private MultipartFile fileToFormat;
	
	private String fileToFormatName;
	
	public String getConflictFile() {
		return conflictFile;
	}
	public void setConflictFile(String conflictFile) {
		this.conflictFile = conflictFile;
	}
	public String getDrName() {
		return drName;
	}
	public void setDrName(String drName) {
		this.drName = drName;
	}
	public String getReflot() {
		return reflot;
	}
	public void setReflot(String reflot) {
		this.reflot = reflot;
	}
	public MultipartFile getFileToFormat() {
		return fileToFormat;
	}
	public void setFileToFormat(MultipartFile fileToFormat) {
		this.fileToFormat = fileToFormat;
	}
	public String getFileToFormatName() {
		return fileToFormatName;
	}
	public void setFileToFormatName(String fileToFormatName) {
		this.fileToFormatName = fileToFormatName;
	}
	
}
