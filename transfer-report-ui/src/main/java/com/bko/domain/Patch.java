package com.bko.domain;

import java.util.List;

public class Patch {
	public String getPatchId() {
		return patchId;
	}
	public void setPatchId(String patchId) {
		this.patchId = patchId;
	}
	private String patchId;
	private String nomGrp;
	private String sujPat;
	private String verPat;
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
