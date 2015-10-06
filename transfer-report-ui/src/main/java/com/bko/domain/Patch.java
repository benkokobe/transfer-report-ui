package com.bko.domain;

import java.util.List;

public class Patch {

	private String patchId;
	private String nomGrp;
	private String sujPat;
	private String verPat;
	private String synopsis;
	private String typEvl;
	private String status;
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getTypEvl() {
		return typEvl;
	}
	public void setTypEvl(String typEvl) {
		this.typEvl = typEvl;
	}
	public String getPatchId() {
		return patchId;
	}
	public void setPatchId(String patchId) {
		this.patchId = patchId;
	}
	public String getSynopsis() {
		return synopsis;
	}
	public void setSynopsis(String synopsis) {
		this.synopsis = synopsis;
	}
	public String getNomGrp() {
		return nomGrp;
	}
	public void setNomGrp(String nomGrp) {
		this.nomGrp = nomGrp;
	}
	public String getSujPat() {
		return sujPat;
	}
	public void setSujPat(String sujPat) {
		this.sujPat = sujPat;
	}
	public String getVerPat() {
		return verPat;
	}
	public void setVerPat(String verPat) {
		this.verPat = verPat;
	}
	private List<TransferOperation> transferOperationsList;
	private List<PatchMember> patchMembersList;

}
