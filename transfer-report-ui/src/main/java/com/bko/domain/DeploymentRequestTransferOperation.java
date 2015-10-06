package com.bko.domain;

public class DeploymentRequestTransferOperation {
	/*
	 * This POJO is used to save the table YFD07 columns
	 *  select reflot, stpall, ordopn, refmai, numstp, numtft, typtft, typopn, swiman, 
	 *  vernum, swimlt, swichk, bypass, nomgrp, idtent, trim(ittcmd) 
     *   from yfd07 where reflot = '00000065';

	 */
	private String reflot;
	private String stpall;
	private String ordopn;
	private String refmai;
	private String numstp;
	private String typtft;
	private String typopn;
	private String swiman;
	private String vernum; //platform
	private String swimlt;
	private String swichk;
	private String bypass;
	private String nomgrp;
	private String idtent;
	private String ittcmd;
	
	public String getReflot() {
		return reflot;
	}
	public void setReflot(String reflot) {
		this.reflot = reflot;
	}
	public String getStpall() {
		return stpall;
	}
	public void setStpall(String stpall) {
		this.stpall = stpall;
	}
	public String getOrdopn() {
		return ordopn;
	}
	public void setOrdopn(String ordopn) {
		this.ordopn = ordopn;
	}
	public String getRefmai() {
		return refmai;
	}
	public void setRefmai(String refmai) {
		this.refmai = refmai;
	}
	public String getNumstp() {
		return numstp;
	}
	public void setNumstp(String numstp) {
		this.numstp = numstp;
	}
	public String getTyptft() {
		return typtft;
	}
	public void setTyptft(String typtft) {
		this.typtft = typtft;
	}
	public String getTypopn() {
		return typopn;
	}
	public void setTypopn(String typopn) {
		this.typopn = typopn;
	}
	public String getSwiman() {
		return swiman;
	}
	public void setSwiman(String swiman) {
		this.swiman = swiman;
	}
	public String getVernum() {
		return vernum;
	}
	public void setVernum(String vernum) {
		this.vernum = vernum;
	}
	public String getSwimlt() {
		return swimlt;
	}
	public void setSwimlt(String swimlt) {
		this.swimlt = swimlt;
	}
	public String getSwichk() {
		return swichk;
	}
	public void setSwichk(String swichk) {
		this.swichk = swichk;
	}
	public String getBypass() {
		return bypass;
	}
	public void setBypass(String bypass) {
		this.bypass = bypass;
	}
	public String getNomgrp() {
		return nomgrp;
	}
	public void setNomgrp(String nomgrp) {
		this.nomgrp = nomgrp;
	}
	public String getIdtent() {
		return idtent;
	}
	public void setIdtent(String idtent) {
		this.idtent = idtent;
	}
	public String getIttcmd() {
		return ittcmd;
	}
	public void setIttcmd(String ittcmd) {
		this.ittcmd = ittcmd;
	}

}
