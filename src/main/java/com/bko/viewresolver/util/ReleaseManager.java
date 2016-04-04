package com.bko.viewresolver.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ImportResource;

import com.bko.domain.DeploymentRequest;
import com.bko.domain.PatchMember;
import com.bko.service.DeploymentRequestService;
import com.cw.scm.member.description.DescriptionManager;
import com.cw.scm.member.description.XmlMemberDescription;
import com.cw.scm.member.description.table.TableDescription;
import com.jcraft.jsch.JSchException;

//@SpringBootApplication
//@ImportResource({"classpath*:root-context.xml"})
public class ReleaseManager {
	
	private static final Logger log = LoggerFactory.getLogger(ReleaseManager.class);
	
	private String releaseName;
	private List<DeploymentRequest> linkedDeploymentRequest;
	private boolean hasLinkedDr;
	
	private int numberOfLinkedDr;
	
	public int getNumberOfLinkedDr() {
		return numberOfLinkedDr;
	}
	public void setNumberOfLinkedDr() {
		if (hasLinkedDr)
		   this.numberOfLinkedDr = linkedDeploymentRequest.size();
		 else
			 this.numberOfLinkedDr = 0; 
	}


	private DeploymentRequest singleDeploymentRequest;
	
	public DeploymentRequest getSingleDeploymentRequest() {
		return singleDeploymentRequest;
	}


	private String sourceEnvironment;
	public String getSourceEnvironment() {
		return sourceEnvironment;
	}

	public String getDestinationEnvironment() {
		return destinationEnvironment;
	}


	private String destinationEnvironment;
	private String synopsisOfRelease;
	
	
	
	
	private SynergyShell synergyShell;
	
	
	public boolean hasLinkedDr() {
		return hasLinkedDr;
	}
	
	@Autowired
	public void setDeploymentRequestService(DeploymentRequestService deploymentRequestService) {
		this.deploymentRequestService = deploymentRequestService;
	}

	
	DeploymentRequestService deploymentRequestService;
	
	private DeploymentRequestSynergy deploymentRequestSynergy;
	//private Object deploymentRequest;
	//private Object deploymentRequestMemberTypes;
	private HashMap<String, List<String>> types = new HashMap<String, List<String>>();
	
//	public void fillMemberTypes(DeploymentRequest deploymentRequest) {
//
//		String fileName;
//		File member_file;
//		List<PatchMember> patchMemberList = deploymentRequest.getMemberList();
//		XmlMemberDescription memberDbdesc;
//
//		List<TableDescription> tableDescritption;
//
//		// for (String type : this.types){
//		String type = "";
//		for (PatchMember pMember : patchMemberList) {
//			type = pMember.getMemberType();
//			fileName = "memberdescription/";
//			System.out.println("Type: " + type);
//			fileName = fileName + type + ".xml";
//			member_file = new File(fileName);
//			memberDbdesc = DescriptionManager.getInstance().getDescriptionFromXml(type, member_file);
//
//			List<String> tables = new ArrayList<String>();
//			if (memberDbdesc != null) {
//				tableDescritption = memberDbdesc.getTableDescriptions();
//				for (TableDescription tableDesc : tableDescritption) {
//					System.out.println("Table: " + tableDesc.name);
//					tables.add(tableDesc.name);
//					this.types.put(type, tables);
//				}
//			}
//		}
//	}
	
	public SynergyShell getSynergyShell() {
		return synergyShell;
	}
	public void setSynergyShell(SynergyShell synergyShell) {
		this.synergyShell = synergyShell;
	}
	public void setLinkedDeploymentRequest(List<DeploymentRequest> linkedDeploymentRequest) {
		this.linkedDeploymentRequest = linkedDeploymentRequest;
	}
	
	
	public String getReleaseName() {
		return releaseName;
	}
	public List<DeploymentRequest> getLinkedDeploymentRequest() {
		return linkedDeploymentRequest;
	}
	public void initialize(String drName, SynergyShell shell, DeploymentRequestService deploymentRequestService) throws JSchException, IOException{
		this.synergyShell = shell;
		this.releaseName = drName;
		setLinkedDeploymentRequest(drName);
		setDeploymentRequestService(deploymentRequestService);
		fillReleaseContent();
		fillReleaseMetaData();
		setNumberOfLinkedDr();
	}
	
	public void setReleaseName(String releaseName) {
		this.releaseName = releaseName;
	}

	public void fillDeploymentRequestContent(DeploymentRequest deploymentRequest) throws JSchException, IOException{
		DeploymentRequestSynergy deploymentRequestSynergy = new DeploymentRequestSynergy();
		DeploymentRequestMemberTypes deploymentRequestMemberTypes = new DeploymentRequestMemberTypes();
		
		deploymentRequestSynergy.setShell(this.synergyShell);
		deploymentRequestSynergy.setDeploymentRequest(deploymentRequest);
		deploymentRequestSynergy.setPatchList();
		deploymentRequestSynergy.setObjectsLinkedToDR(deploymentRequest.getDrName());
		deploymentRequestSynergy.setDeploymentRequestInfo(deploymentRequest);
		
		deploymentRequest.setMemberList(this.deploymentRequestService.getDRMembers(deploymentRequest.getDrName()));
		deploymentRequest.setDeploymentRequestTransferOperation(
				                this.deploymentRequestService.getTransferOperation(deploymentRequest.getDrName()));
		
		deploymentRequest.setNumberOfTransferOperations(
				this.deploymentRequestService.getnumberOfTransferOperations(deploymentRequest.getDrName()));
		
		deploymentRequest.setNumberOfManualTransferOperations(
				this.deploymentRequestService.getNumberOfManualTransferOperations(deploymentRequest.getDrName()));
		
		deploymentRequest.setNumberOfSubjects(
				this.deploymentRequestService.getNumberOfSubjects(deploymentRequest.getDrName()));
		
		deploymentRequest.setNumberOfPatches(deploymentRequest.getPatchList().size());//from Synergy
		
		deploymentRequestMemberTypes.setDeploymentRequest(deploymentRequest);
		deploymentRequestMemberTypes.generatePatchMemberTypes();
		
		deploymentRequest.setTypes(deploymentRequestMemberTypes.getTypes());
		
		
		
	}
	public void fillReleaseMetaData() throws JSchException, IOException{
		DeploymentRequest dr = new DeploymentRequest();
		dr.setDrName(releaseName);
		
		this.synergyShell.setDeploymentRequestInfo(dr);
		this.destinationEnvironment = dr.getEnvDst();
		this.sourceEnvironment      = dr.getEnvSrc();
		this.synopsisOfRelease      = dr.getSynopsis();
	}
	
	public void fillReleaseContent() throws JSchException, IOException{
		
		
		
		if (this.hasLinkedDr){
			for(DeploymentRequest deploymentRequest : this.linkedDeploymentRequest){
				String drName = deploymentRequest.getDrName();
				deploymentRequestSynergy = new DeploymentRequestSynergy();
				deploymentRequestSynergy.setDeploymentRequest(deploymentRequest);
				fillDeploymentRequestContent(deploymentRequest);
				log.info("********************************");
				
				//fill up DR
			}
			
		}else{
			//fill up the singleDeploymentRequest
			this.singleDeploymentRequest = new DeploymentRequest();
			this.singleDeploymentRequest.setDrName(this.releaseName);
			fillDeploymentRequestContent(this.singleDeploymentRequest);
			
		}
	}
	public void showDeploymentRequestContets(){
		if (this.hasLinkedDr){
			for ( DeploymentRequest dr : this.linkedDeploymentRequest){
				log.info("dr name:" + dr.getDrName());
				log.info("dr. nbr. tft:" + dr.getNumberOfTransferOperations());
				log.info("dt:type tables:" + dr.getTypes());
			}
		}else {
			log.info("DR name     :" + this.singleDeploymentRequest.getDrName());
			log.info("dr. nbr. tft:" + this.singleDeploymentRequest.getNumberOfTransferOperations());
			log.info("dt:type tables:" + this.singleDeploymentRequest.getTypes().get("01"));
		}
	}
	public String getSynopsisOfRelease() {
		return this.synopsisOfRelease;
	}
	
	public void setLinkedDeploymentRequest(String drName) throws JSchException, IOException {
		setReleaseName(drName);
		
		//check if the DR has linked DR's
		if (this.synergyShell.hasLinkedDRs(drName)){
			this.linkedDeploymentRequest = this.synergyShell.getLinkedDeploymentRequests(drName);
			this.hasLinkedDr = true;
		} else {
			//this.synergyShell.setLinkedDR(drName);
			this.hasLinkedDr = false;
			
			
		}
		
	}
	public void closeSession(){
		this.synergyShell.getSession().disconnect();
	}
//	public static void main(String[] arg) throws IOException, JSchException {
//		
//		ApplicationContext ctx = SpringApplication.run(ReleaseManager.class, arg);
//		
//		SynergyShell shell = new SynergyShell();
//		String key = "C:\\thalerng\\config\\bko_priv_rsa.ppk";
//		String pass = "bko";
//		shell.intialize_and_connect("scm.com.saas.i2s", "bkokobe", key, pass);
//		//shell.execute_command(query);
//		//String drName = "PACK-ACP-0011";
//		String drName = "PACK-PRG-0018";
//		//shell.hasLinkedDRs(drName);
//		//DeploymentRequestService deploymentRequestService = new Dep  
//		
//		DeploymentRequestService deploymentRequestService = (DeploymentRequestService) ctx.getBean("deploymentRequestService");
//		
//		ReleaseManager releaseManager = new ReleaseManager();
//		releaseManager.initialize(drName, shell, deploymentRequestService);
//		//releaseManager.setLinkedDeploymentRequest(drName);
//		
//		//releaseManager.setDeploymentRequestService(deploymentRequestService);
//		//releaseManager.fillReleaseContent();
//		releaseManager.showDeploymentRequestContets();
//		
//	}


	


}
