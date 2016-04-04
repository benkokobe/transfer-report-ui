package com.bko.viewresolver;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


import com.bko.domain.DeploymentRequest;
import com.bko.domain.DeploymentRequestTransferOperation;
import com.bko.domain.Patch;
import com.bko.domain.PatchMember;
import com.bko.service.DeploymentRequestService;
import com.bko.viewresolver.util.DeploymentRequestMemberTypes;
import com.bko.viewresolver.util.DeploymentRequestSynergy;
import com.bko.viewresolver.util.SynergyObject;
import com.bko.viewresolver.util.SynergyShell;
import com.jcraft.jsch.JSchException;

@Component
public class BaseExcelGeneratorFromTemplate {

	private static final Logger log = LoggerFactory.getLogger(BaseExcelGeneratorFromTemplate.class);

	protected FileInputStream inputTemplateFile;
	protected FileOutputStream generatedFile;

	protected Row rowPatchList, rowPatchMembersList, rowTransferOperationList, rowObjectList, row4, row5,
			rowTypeTabesList;

	// tabs
	protected XSSFSheet summaryTab;
	protected Sheet patchListSheetTab;
	protected XSSFSheet drMembersSheetTab;
	protected XSSFSheet transferOperationsSheetTab;
	protected XSSFSheet objectListTab;
	protected XSSFSheet memberTablesTab;

	// Columns for each tabs
	protected Cell columnPatchListDRname;
	protected Cell columnPatchListPatchId;
	protected Cell columnPatchListStatus;
	protected Cell columnPatchListTypEvl;
	protected Cell columnPatchListGroupName;
	protected Cell columnPatchListSubject;
	protected Cell columnPatchListSynopsis;

	protected Cell columnMembersListPatchReference;
	protected Cell columnMembersListMemberType;
	protected Cell columnMembersListActionType;
	protected Cell columnMembersListMemberName;

	protected Cell columnObjectListTask;
	protected Cell columnObjectListObject;
	protected Cell columnObjectListType;
	protected Cell columnObjectListVersion;
	protected Cell columnObjectListInstance;

	/*
	 * REFLOT STPALL ORDOPN REFMAI NUMSTP NUMTFT TYPTFT TYPOPN SWIMAN VERNUM
	 * SWIMLT SWICHK BYPASS NOMGRP IDTENT ITTCMD
	 */
	protected Cell columnTransferOpReflot;
	protected Cell columnTransferOpStpall;
	protected Cell columnTransferOpOrdOpn;
	protected Cell columnTransferOpRefmai;
	protected Cell columnTransferOpNumStp;
	protected Cell columnTransferOpNumTft;
	protected Cell columnTransferOpTypTft;
	protected Cell columnTransferOpTypOpn;
	protected Cell columnTransferOpSwiMan;
	protected Cell columnTransferOpVerNum;
	protected Cell columnTransferOpSwiMlt;
	protected Cell columnTransferOpSwiChk;
	protected Cell columnTransferOpBypass;
	protected Cell columnTransferOpNomGrp;
	protected Cell columnTransferOpIdtent;
	protected Cell columnTransferOpIttCmd;

	protected Cell columnMemberTypesType;
	protected Cell columnMemberTypesTable;

	private DeploymentRequestSynergy deploymentRequestSynergy;
	private DeploymentRequestMemberTypes deploymentRequestMemberTypes;

	private DeploymentRequestService deploymentRequestService;
	private DeploymentRequest deploymentRequest;

	public DeploymentRequest getDeploymentRequest() {
		return deploymentRequest;
	}

	public void setDeploymentRequest(DeploymentRequest deploymentRequest) {
		this.deploymentRequest = deploymentRequest;
	}

	public void initialize_DR(String drName, SynergyShell shell) throws JSchException, IOException {

		this.deploymentRequestSynergy = new DeploymentRequestSynergy();
		this.deploymentRequestMemberTypes = new DeploymentRequestMemberTypes();

		this.deploymentRequestSynergy.setDeploymentRequest(this.deploymentRequest);

		this.deploymentRequestSynergy.setShell(shell);
		this.deploymentRequestSynergy.setObjectsLinkedToDR(drName);
		this.deploymentRequestSynergy.setPatchList();
		this.deploymentRequestSynergy.setDeploymentRequestInfo(this.deploymentRequest);

		this.deploymentRequest
				.setDeploymentRequestTransferOperation(this.deploymentRequestService.getTransferOperation(drName));

		this.deploymentRequest.setPatchMembersList(this.deploymentRequestService.getDRMembers(drName));

		this.deploymentRequestMemberTypes.setDeploymentRequest(this.deploymentRequest);
		this.deploymentRequestMemberTypes.generatePatchMemberTypes();

	}

	public DeploymentRequestService getDeploymentRequestService() {
		return deploymentRequestService;
	}

	public void setDeploymentRequestService(DeploymentRequestService deploymentRequestService) {
		this.deploymentRequestService = deploymentRequestService;
	}

	/**
	 * This will generate the tab -- member types impacted tables
	 * 
	 * @param wb
	 */
	public void fillMemberTpes(XSSFWorkbook wb) {
		this.memberTablesTab = wb.getSheetAt(7);
		int rowsInTypesTabes = 1;

		Map<String, List<String>> hashTypeTables = this.deploymentRequestMemberTypes.getTypes();
		// this.columnMemberTypes
		for (Entry<String, List<String>> entry : hashTypeTables.entrySet()) {
			System.out.println("Key : " + entry.getKey() + " Value : " + entry.getValue());
			for (String table : entry.getValue()) {
				rowTypeTabesList = memberTablesTab.createRow((short) rowsInTypesTabes);
				this.columnMemberTypesType = rowTypeTabesList.createCell((short) 0);
				this.columnMemberTypesTable = rowTypeTabesList.createCell((short) 1);

				this.columnMemberTypesType.setCellValue(entry.getKey());
				this.columnMemberTypesTable.setCellValue(table);
				rowsInTypesTabes++;
			}
		}
	}

	/**
	 * generates the tab transfer operations list REFLOT STPALL ORDOPN REFMAI
	 * NUMSTP NUMTFT TYPTFT TYPOPN SWIMAN VERNUM SWIMLT SWICHK BYPASS NOMGRP
	 * IDTENT ITTCMD
	 * 
	 * @param wb
	 */
	public void generateTransferOp(XSSFWorkbook wb) {

		List<DeploymentRequestTransferOperation> transferOperationList = this.deploymentRequest
				.getDeploymentRequestTransferOperation();

		this.transferOperationsSheetTab = wb.getSheetAt(5);

		int rowsInTransferOpTab = 1;
		for (DeploymentRequestTransferOperation transferOperation : transferOperationList) {
			rowTransferOperationList = transferOperationsSheetTab.createRow((short) rowsInTransferOpTab);

			columnTransferOpReflot = rowTransferOperationList.createCell((short) 0);
			columnTransferOpStpall = rowTransferOperationList.createCell((short) 1);
			columnTransferOpOrdOpn = rowTransferOperationList.createCell((short) 2);
			columnTransferOpRefmai = rowTransferOperationList.createCell((short) 3);
			columnTransferOpNumStp = rowTransferOperationList.createCell((short) 4);
			columnTransferOpNumTft = rowTransferOperationList.createCell((short) 5);// NUMTFT
			columnTransferOpTypTft = rowTransferOperationList.createCell((short) 6);
			columnTransferOpTypOpn = rowTransferOperationList.createCell((short) 7);
			columnTransferOpSwiMan = rowTransferOperationList.createCell((short) 8);
			columnTransferOpVerNum = rowTransferOperationList.createCell((short) 9);
			columnTransferOpSwiMlt = rowTransferOperationList.createCell((short) 10);
			columnTransferOpSwiChk = rowTransferOperationList.createCell((short) 11);
			columnTransferOpBypass = rowTransferOperationList.createCell((short) 12);
			columnTransferOpNomGrp = rowTransferOperationList.createCell((short) 13);
			columnTransferOpIdtent = rowTransferOperationList.createCell((short) 14);
			columnTransferOpIttCmd = rowTransferOperationList.createCell((short) 15);

			columnTransferOpReflot.setCellValue(transferOperation.getReflot());
			columnTransferOpStpall.setCellValue(transferOperation.getStpall());
			columnTransferOpOrdOpn.setCellValue(transferOperation.getOrdopn());
			columnTransferOpRefmai.setCellValue(transferOperation.getRefmai());
			columnTransferOpNumStp.setCellValue(transferOperation.getNumstp());
			columnTransferOpNumTft.setCellValue("NUMTFT-not-added");
			columnTransferOpTypTft.setCellValue(transferOperation.getTyptft());
			columnTransferOpTypOpn.setCellValue(transferOperation.getTypopn());
			columnTransferOpSwiMan.setCellValue(transferOperation.getSwiman());
			columnTransferOpVerNum.setCellValue(transferOperation.getVernum());
			columnTransferOpSwiMlt.setCellValue(transferOperation.getSwimlt());
			columnTransferOpSwiChk.setCellValue(transferOperation.getSwichk());
			columnTransferOpBypass.setCellValue(transferOperation.getBypass());// swibypass
			columnTransferOpNomGrp.setCellValue(transferOperation.getNomgrp());
			columnTransferOpIdtent.setCellValue(transferOperation.getIdtent());
			columnTransferOpIttCmd.setCellValue(transferOperation.getIttcmd());
			rowsInTransferOpTab++;
		}
	}

	public void fillSummaryTab(XSSFWorkbook wb) {

		this.summaryTab = wb.getSheetAt(0);
		/*
		 * for (int i = 0; i < 20; i++) { System.out.println("row:" + i + ":" +
		 * this.summaryTab.getRow(1).getCell(i)); }
		 */
		log.info("Tab name   :" + this.summaryTab.getSheetName());
		log.info("Dr name    : " + this.summaryTab.getRow(8).getCell(2));
		log.info("Synopsis   : " + this.summaryTab.getRow(1).getCell(2));
		log.info("From       : " + this.summaryTab.getRow(1).getCell(5));
		log.info("To         : " + this.summaryTab.getRow(1).getCell(7));

		// this.deploymentRequestService.getSynopsis(this.drName);

		this.summaryTab.getRow(8).getCell(2).setCellValue(this.deploymentRequest.getDrName());
		this.summaryTab.getRow(1).getCell(2).setCellValue(this.deploymentRequest.getSynopsis());
		this.summaryTab.getRow(1).getCell(5).setCellValue(this.deploymentRequest.getEnvSrc());
		this.summaryTab.getRow(1).getCell(7).setCellValue(this.deploymentRequest.getEnvDst());

		log.info("Dr name         : " + this.deploymentRequest.getDrName());
		log.info("Dest. name         : " + this.deploymentRequest.getEnvDst());

		for (int i = 0; i < 20; i++) {
			System.out.println("row:" + i + ":" + this.summaryTab.getRow(i).getCell(2));
		}
	}

	/**
	 * This will generate the excel tab DB member list REFPAT NOMMBR TYPMBR
	 * TYPACT TYPEVL
	 * 
	 * @param wb
	 */
	public void generateDRMembers(XSSFWorkbook wb) {
		int rowCounter = 1;

		log.info("generateList DR members for    :" + this.deploymentRequest.getDrName());
		List<PatchMember> patchMembersList = this.deploymentRequest.getMemberList();

		this.drMembersSheetTab = wb.getSheetAt(4);
		log.info("Tab name:" + drMembersSheetTab.getSheetName());

		for (PatchMember patchMember : patchMembersList) {
			rowPatchMembersList = this.drMembersSheetTab.createRow((short) rowCounter);

			columnMembersListPatchReference = rowPatchMembersList.createCell((short) 0);
			columnMembersListMemberName = rowPatchMembersList.createCell((short) 1);
			columnMembersListMemberType = rowPatchMembersList.createCell((short) 2);
			columnMembersListActionType = rowPatchMembersList.createCell((short) 3);

			columnMembersListPatchReference.setCellValue(patchMember.getPatchId());
			columnMembersListMemberName.setCellValue(patchMember.getPatchMember());
			columnMembersListMemberType.setCellValue(patchMember.getMemberType());
			columnMembersListActionType.setCellValue(patchMember.getTypAct());
			rowCounter++;

		}
	}

	/**
	 * THis will generate the tab Synergy Object list
	 * 
	 * @param wb
	 * @throws JSchException
	 * @throws IOException
	 */
	public void generateObjectList(XSSFWorkbook wb) throws JSchException, IOException {
		// TODO
		int rowCounter = 1;

		this.objectListTab = wb.getSheetAt(6);

		List<SynergyObject> synergyObjects;
		log.info("Tab name:" + objectListTab.getSheetName());

		synergyObjects = this.deploymentRequest.getObjectList();

		for (SynergyObject synObject : synergyObjects) {
			System.out.println("****Task: " + synObject.getTask());
			System.out.println("***Object: " + synObject.getObject());
			rowObjectList = this.objectListTab.createRow((short) rowCounter);

			columnObjectListTask = rowObjectList.createCell((short) 0);
			columnObjectListObject = rowObjectList.createCell((short) 1);
			columnObjectListType = rowObjectList.createCell((short) 2);
			columnObjectListVersion = rowObjectList.createCell((short) 3);
			columnObjectListInstance = rowObjectList.createCell((short) 4);

			columnObjectListObject.setCellValue(synObject.getObject());
			columnObjectListTask.setCellValue(synObject.getTask());
			columnObjectListType.setCellValue(synObject.getType());
			columnObjectListVersion.setCellValue(synObject.getVersion());
			columnObjectListInstance.setCellValue(synObject.getInstance());

			rowCounter++;
		}
	}

	/**
	 * This will generate the patch list tab REFPAT STAPAT NOMGRP TYPEVL SUBJECT
	 * SYNOPSIS
	 * 
	 * @param wb
	 */
	public void generatePtchList(XSSFWorkbook wb) {
		List<Patch> patchList;

		patchList = deploymentRequest.getPatchList();

		log.info("generateDocument() :Size of patch list:" + patchList.size());
		int rowCounter = 1;

		for (Patch patch : patchList) {

			this.patchListSheetTab = wb.getSheetAt(3);
			log.info("Tab name:" + patchListSheetTab.getSheetName());

			rowPatchList = patchListSheetTab.createRow(rowCounter);

			this.columnPatchListPatchId = rowPatchList.createCell(0);
			this.columnPatchListStatus = rowPatchList.createCell(1);
			this.columnPatchListGroupName = rowPatchList.createCell(2);
			this.columnPatchListTypEvl = rowPatchList.createCell(3);
			this.columnPatchListSubject = rowPatchList.createCell(4);
			this.columnPatchListSynopsis = rowPatchList.createCell(5);

			this.columnPatchListPatchId.setCellValue(patch.getPatchId());
			this.columnPatchListStatus.setCellValue(patch.getStatus());
			this.columnPatchListGroupName.setCellValue(patch.getNomGrp());
			this.columnPatchListTypEvl.setCellValue(patch.getTypEvl());
			this.columnPatchListSubject.setCellValue(patch.getSujPat());
			this.columnPatchListSynopsis.setCellValue(patch.getSynopsis());

			rowCounter++;

			log.debug("Rows written: " + rowCounter);
		}
	}
}
