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
import com.bko.viewresolver.util.ReleaseManager;
import com.bko.viewresolver.util.DeploymentRequestMemberTypes;
import com.bko.viewresolver.util.DeploymentRequestSynergy;
import com.bko.viewresolver.util.SynergyObject;
import com.bko.viewresolver.util.SynergyShell;
import com.jcraft.jsch.JSchException;

@Component
public class BaseReleaseExcelGenerator {
	private static final Logger log = LoggerFactory.getLogger(BaseReleaseExcelGenerator.class);

	protected FileInputStream inputTemplateFile;
	protected FileOutputStream generatedFile;

	protected Row rowPatchList, rowPatchMembersList, rowTransferOperationList, rowObjectList, row4, row5,
			rowTypeTabesList;

	// tabs
	protected XSSFSheet summaryTab;
	// protected Sheet patchListSheetTab;
	protected XSSFSheet patchListSheetTab;
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

	protected Cell columnMembersListDrName;
	protected Cell columnMembersListPatchReference;
	protected Cell columnMembersListMemberType;
	protected Cell columnMembersListActionType;
	protected Cell columnMembersListMemberName;

	protected Cell columnObjectListDrName;
	protected Cell columnObjectListTask;
	protected Cell columnObjectListObject;
	protected Cell columnObjectListType;
	protected Cell columnObjectListVersion;
	protected Cell columnObjectListInstance;

	/*
	 * REFLOT STPALL ORDOPN REFMAI NUMSTP NUMTFT TYPTFT TYPOPN SWIMAN VERNUM
	 * SWIMLT SWICHK BYPASS NOMGRP IDTENT ITTCMD
	 */
	protected Cell columnTransferOpDrNhame;
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

	protected Cell columnMemberTypesDrName;
	protected Cell columnMemberTypesType;
	protected Cell columnMemberTypesTable;

	// private DeploymentRequestSynergy deploymentRequestSynergy;
	private DeploymentRequestMemberTypes deploymentRequestMemberTypes;

	// private DeploymentRequestService deploymentRequestService;
	private DeploymentRequest deploymentRequest;

	private ReleaseManager releaseManager;

	public DeploymentRequest getDeploymentRequest() {
		return deploymentRequest;
	}

	public void setDeploymentRequest(DeploymentRequest deploymentRequest) {
		this.deploymentRequest = deploymentRequest;
	}

	public void initialize_release(String drName, SynergyShell shell, DeploymentRequestService deploymentRequestService,
			ReleaseManager releaseManager) throws JSchException, IOException {

		this.releaseManager = releaseManager;

	}

	/**
	 * This will generate the tab -- member types impacted tables
	 * 
	 * @param wb
	 */
	public void fillMemberTypesTab(XSSFWorkbook wb) {
		this.memberTablesTab = wb.getSheetAt(7);
		int rowsInTypesTabes = 1;

		Map<String, List<String>> hashTypeTables;// = this.deploymentRequestMemberTypes.getTypes();
		// this.columnMemberTypes
		
		if (this.releaseManager.hasLinkedDr()) {
			for (DeploymentRequest dr : releaseManager.getLinkedDeploymentRequest()) {
				hashTypeTables = dr.getTypes();
				for (Entry<String, List<String>> entry : hashTypeTables.entrySet()) {
					log.info("Key : " + entry.getKey() + " Value : " + entry.getValue());
					for (String table : entry.getValue()) {
						rowTypeTabesList = memberTablesTab.createRow((short) rowsInTypesTabes);
						this.columnMemberTypesDrName = rowTypeTabesList.createCell((short) 0);
						this.columnMemberTypesType = rowTypeTabesList.createCell((short) 1);
						this.columnMemberTypesTable = rowTypeTabesList.createCell((short) 2);

						this.columnMemberTypesDrName.setCellValue(dr.getDrName());
						this.columnMemberTypesType.setCellValue(entry.getKey());
						this.columnMemberTypesTable.setCellValue(table);
						rowsInTypesTabes++;
					}
				}
			}
		}else{
			DeploymentRequest dr = releaseManager.getSingleDeploymentRequest();
			hashTypeTables = dr.getTypes();
			for (Entry<String, List<String>> entry : hashTypeTables.entrySet()) {
				log.info("Key : " + entry.getKey() + " Value : " + entry.getValue());
				for (String table : entry.getValue()) {
					rowTypeTabesList = memberTablesTab.createRow((short) rowsInTypesTabes);
					this.columnMemberTypesDrName = rowTypeTabesList.createCell((short) 0);
					this.columnMemberTypesType = rowTypeTabesList.createCell((short) 1);
					this.columnMemberTypesTable = rowTypeTabesList.createCell((short) 2);

					this.columnMemberTypesDrName.setCellValue(dr.getDrName());
					this.columnMemberTypesType.setCellValue(entry.getKey());
					this.columnMemberTypesTable.setCellValue(table);
					rowsInTypesTabes++;
				}
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
	public void fillTransferOperationsTab(XSSFWorkbook wb) {

		List<DeploymentRequestTransferOperation> transferOperationList;//= this.deploymentRequest.getDeploymentRequestTransferOperation();
		
		int rowsInTransferOpTab = 1;
		this.transferOperationsSheetTab = wb.getSheetAt(5);
		
		if (this.releaseManager.hasLinkedDr()) {
			for (DeploymentRequest dr : releaseManager.getLinkedDeploymentRequest()) {
				
				transferOperationList = dr.getDeploymentRequestTransferOperation();
				for (DeploymentRequestTransferOperation transferOperation : transferOperationList) {
					rowTransferOperationList = transferOperationsSheetTab.createRow((short) rowsInTransferOpTab);

				    columnTransferOpDrNhame = rowTransferOperationList.createCell((short) 0);
					columnTransferOpReflot = rowTransferOperationList.createCell((short) 1);
					columnTransferOpStpall = rowTransferOperationList.createCell((short) 2);
					columnTransferOpOrdOpn = rowTransferOperationList.createCell((short) 3);
					columnTransferOpRefmai = rowTransferOperationList.createCell((short) 4);
					columnTransferOpNumStp = rowTransferOperationList.createCell((short) 5);
					columnTransferOpNumTft = rowTransferOperationList.createCell((short) 6);// NUMTFT
					columnTransferOpTypTft = rowTransferOperationList.createCell((short) 7);
					columnTransferOpTypOpn = rowTransferOperationList.createCell((short) 8);
					columnTransferOpSwiMan = rowTransferOperationList.createCell((short) 9);
					columnTransferOpVerNum = rowTransferOperationList.createCell((short) 10);
					columnTransferOpSwiMlt = rowTransferOperationList.createCell((short) 11);
					columnTransferOpSwiChk = rowTransferOperationList.createCell((short) 12);
					columnTransferOpBypass = rowTransferOperationList.createCell((short) 13);
					columnTransferOpNomGrp = rowTransferOperationList.createCell((short) 14);
					columnTransferOpIdtent = rowTransferOperationList.createCell((short) 15);
					columnTransferOpIttCmd = rowTransferOperationList.createCell((short) 16);

					columnTransferOpDrNhame.setCellValue(dr.getDrName());
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
		}else{
			DeploymentRequest dr = releaseManager.getSingleDeploymentRequest();
			transferOperationList = dr.getDeploymentRequestTransferOperation();
			for (DeploymentRequestTransferOperation transferOperation : transferOperationList) {
				rowTransferOperationList = transferOperationsSheetTab.createRow((short) rowsInTransferOpTab);

				columnTransferOpDrNhame = rowTransferOperationList.createCell((short) 0);
				columnTransferOpReflot = rowTransferOperationList.createCell((short) 1);
				columnTransferOpStpall = rowTransferOperationList.createCell((short) 2);
				columnTransferOpOrdOpn = rowTransferOperationList.createCell((short) 3);
				columnTransferOpRefmai = rowTransferOperationList.createCell((short) 4);
				columnTransferOpNumStp = rowTransferOperationList.createCell((short) 5);
				columnTransferOpNumTft = rowTransferOperationList.createCell((short) 6);// NUMTFT
				columnTransferOpTypTft = rowTransferOperationList.createCell((short) 7);
				columnTransferOpTypOpn = rowTransferOperationList.createCell((short) 8);
				columnTransferOpSwiMan = rowTransferOperationList.createCell((short) 9);
				columnTransferOpVerNum = rowTransferOperationList.createCell((short) 10);
				columnTransferOpSwiMlt = rowTransferOperationList.createCell((short) 11);
				columnTransferOpSwiChk = rowTransferOperationList.createCell((short) 12);
				columnTransferOpBypass = rowTransferOperationList.createCell((short) 13);
				columnTransferOpNomGrp = rowTransferOperationList.createCell((short) 14);
				columnTransferOpIdtent = rowTransferOperationList.createCell((short) 15);
				columnTransferOpIttCmd = rowTransferOperationList.createCell((short) 16);

				columnTransferOpDrNhame.setCellValue(dr.getDrName());
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

		this.summaryTab.getRow(8).getCell(2).setCellValue(this.releaseManager.getReleaseName());
		this.summaryTab.getRow(1).getCell(2).setCellValue(this.releaseManager.getSynopsisOfRelease());
		this.summaryTab.getRow(1).getCell(5).setCellValue(this.releaseManager.getSourceEnvironment());
		this.summaryTab.getRow(1).getCell(7).setCellValue(this.releaseManager.getDestinationEnvironment());

		log.info("Release name         : " + this.releaseManager.getReleaseName());
		log.info("Dest. name           : " + this.releaseManager.getDestinationEnvironment());

		for (int i = 0; i < 20; i++) {
			log.info("row:" + i + ":" + this.summaryTab.getRow(i).getCell(2));
		}
	}

	/**
	 * This will generate the excel tab DB member list REFPAT NOMMBR TYPMBR
	 * TYPACT TYPEVL
	 * 
	 * @param wb
	 */
	public void fillDBmembersTab(XSSFWorkbook wb) {
		int rowCounter = 1;

		log.info("generateList DR members for    :" + this.releaseManager.getReleaseName());
		// List<PatchMember> patchMembersList =
		// this.deploymentRequest.getMemberList();

		List<PatchMember> patchMembersList;

		if (this.releaseManager.hasLinkedDr()) {
			for (DeploymentRequest dr : releaseManager.getLinkedDeploymentRequest()) {

				patchMembersList = dr.getMemberList();
				this.drMembersSheetTab = wb.getSheetAt(4);

				for (PatchMember patchMember : patchMembersList) {

					rowPatchMembersList = this.drMembersSheetTab.createRow((short) rowCounter);

					columnMembersListDrName = rowPatchMembersList.createCell((short) 0);       
					columnMembersListPatchReference = rowPatchMembersList.createCell((short) 1);
					columnMembersListMemberName = rowPatchMembersList.createCell((short) 2);
					columnMembersListMemberType = rowPatchMembersList.createCell((short) 3);
					columnMembersListActionType = rowPatchMembersList.createCell((short) 4);

					columnMembersListDrName.setCellValue(dr.getDrName());
					columnMembersListPatchReference.setCellValue(patchMember.getPatchId());
					columnMembersListMemberName.setCellValue(patchMember.getPatchMember());
					columnMembersListMemberType.setCellValue(patchMember.getMemberType());
					columnMembersListActionType.setCellValue(patchMember.getTypAct());
					rowCounter++;
				}
			}
		} else {
			DeploymentRequest dr = releaseManager.getSingleDeploymentRequest();
			patchMembersList = dr.getMemberList();
			this.drMembersSheetTab = wb.getSheetAt(4);

			for (PatchMember patchMember : patchMembersList) {

				rowPatchMembersList = this.drMembersSheetTab.createRow((short) rowCounter);

				columnMembersListDrName = rowPatchMembersList.createCell((short) 0);       
				columnMembersListPatchReference = rowPatchMembersList.createCell((short) 1);
				columnMembersListMemberName = rowPatchMembersList.createCell((short) 2);
				columnMembersListMemberType = rowPatchMembersList.createCell((short) 3);
				columnMembersListActionType = rowPatchMembersList.createCell((short) 4);

				columnMembersListDrName.setCellValue(dr.getDrName());
				columnMembersListPatchReference.setCellValue(patchMember.getPatchId());
				columnMembersListMemberName.setCellValue(patchMember.getPatchMember());
				columnMembersListMemberType.setCellValue(patchMember.getMemberType());
				columnMembersListActionType.setCellValue(patchMember.getTypAct());
				rowCounter++;
			}

		}

	}

	/**
	 * THis will generate the tab Synergy Object list
	 * 
	 * @param wb
	 * @throws JSchException
	 * @throws IOException
	 */
	public void fillSynergyObjectsTab(XSSFWorkbook wb) throws JSchException, IOException {
		// TODO
		int rowCounter = 1;

		this.objectListTab = wb.getSheetAt(6);

		List<SynergyObject> synergyObjects;
		log.info("Tab name:" + objectListTab.getSheetName());

		
		if (this.releaseManager.hasLinkedDr()) {
			for (DeploymentRequest dr : releaseManager.getLinkedDeploymentRequest()) {
				synergyObjects = dr.getObjectList();
				for (SynergyObject synObject : synergyObjects) {
					log.info("****Task: " + synObject.getTask());
					log.info("***Object: " + synObject.getObject());
					rowObjectList = this.objectListTab.createRow((short) rowCounter);

					columnObjectListDrName = rowObjectList.createCell((short) 0);
					columnObjectListTask = rowObjectList.createCell((short) 1);
					columnObjectListObject = rowObjectList.createCell((short) 2);
					columnObjectListType = rowObjectList.createCell((short) 3);
					columnObjectListVersion = rowObjectList.createCell((short) 4);
					columnObjectListInstance = rowObjectList.createCell((short) 5);

					columnObjectListDrName.setCellValue(dr.getDrName());
					columnObjectListObject.setCellValue(synObject.getObject());
					columnObjectListTask.setCellValue(synObject.getTask());
					columnObjectListType.setCellValue(synObject.getType());
					columnObjectListVersion.setCellValue(synObject.getVersion());
					columnObjectListInstance.setCellValue(synObject.getInstance());

					rowCounter++;
				}
			}
			
		}else{
			DeploymentRequest dr = releaseManager.getSingleDeploymentRequest();
			synergyObjects = dr.getObjectList();
			for (SynergyObject synObject : synergyObjects) {
				log.info("****Task: " + synObject.getTask());
				log.info("***Object: " + synObject.getObject());
				rowObjectList = this.objectListTab.createRow((short) rowCounter);

				columnObjectListDrName = rowObjectList.createCell((short) 0);
				columnObjectListTask = rowObjectList.createCell((short) 1);
				columnObjectListObject = rowObjectList.createCell((short) 2);
				columnObjectListType = rowObjectList.createCell((short) 3);
				columnObjectListVersion = rowObjectList.createCell((short) 4);
				columnObjectListInstance = rowObjectList.createCell((short) 5);

				columnObjectListDrName.setCellValue(dr.getDrName());
				columnObjectListObject.setCellValue(synObject.getObject());
				columnObjectListTask.setCellValue(synObject.getTask());
				columnObjectListType.setCellValue(synObject.getType());
				columnObjectListVersion.setCellValue(synObject.getVersion());
				columnObjectListInstance.setCellValue(synObject.getInstance());

				rowCounter++;
			}
		}
	}

	/**
	 * This will generate the patch list tab REFPAT STAPAT NOMGRP TYPEVL SUBJECT
	 * SYNOPSIS
	 * 
	 * @param wb
	 */
	public void fillPatchListTab(XSSFWorkbook wb) {
		List<Patch> patchList;

		int rowCounter = 1;
		if (this.releaseManager.hasLinkedDr()) {
			for (DeploymentRequest dr : releaseManager.getLinkedDeploymentRequest()) {
				patchList = dr.getPatchList();

				log.info(
						"generateDocument() :Size of patch list[linked DR]:" + dr.getDrName() + ":" + patchList.size());

				for (Patch patch : patchList) {

					this.patchListSheetTab = wb.getSheetAt(3);
					log.info("Tab name:" + patchListSheetTab.getSheetName());

					rowPatchList = patchListSheetTab.createRow(rowCounter);

					this.columnPatchListDRname = rowPatchList.createCell(0);
					this.columnPatchListPatchId = rowPatchList.createCell(1);
					this.columnPatchListStatus = rowPatchList.createCell(2);
					this.columnPatchListGroupName = rowPatchList.createCell(3);
					this.columnPatchListTypEvl = rowPatchList.createCell(4);
					this.columnPatchListSubject = rowPatchList.createCell(5);
					this.columnPatchListSynopsis = rowPatchList.createCell(6);

					this.columnPatchListDRname.setCellValue(dr.getDrName());
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
		} else {
			DeploymentRequest dr = this.releaseManager.getSingleDeploymentRequest();
			patchList = dr.getPatchList();
			log.info("generateDocument() :Size of patch list[Single DR]:" + dr.getDrName() + ":" + patchList.size());
			for (Patch patch : patchList) {

				this.patchListSheetTab = wb.getSheetAt(3);
				log.info("Tab name:" + patchListSheetTab.getSheetName());

				rowPatchList = patchListSheetTab.createRow(rowCounter);

				this.columnPatchListDRname = rowPatchList.createCell(0);
				this.columnPatchListPatchId = rowPatchList.createCell(1);
				this.columnPatchListStatus = rowPatchList.createCell(2);
				this.columnPatchListGroupName = rowPatchList.createCell(3);
				this.columnPatchListTypEvl = rowPatchList.createCell(4);
				this.columnPatchListSubject = rowPatchList.createCell(5);
				this.columnPatchListSynopsis = rowPatchList.createCell(6);

				this.columnPatchListDRname.setCellValue(dr.getDrName());
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

}
