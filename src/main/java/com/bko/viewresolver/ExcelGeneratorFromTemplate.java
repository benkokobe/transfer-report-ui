package com.bko.viewresolver;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.document.AbstractExcelView;



import com.bko.domain.DeploymentRequest;
import com.bko.domain.DeploymentRequestTransferOperation;
import com.bko.domain.Patch;
import com.bko.domain.PatchMember;
import com.bko.service.DeploymentRequestService;
import com.bko.service.PatchService;
import com.bko.viewresolver.util.SynergyShell;
import com.bko.viewresolver.util.SynergyObject;
import com.jcraft.jsch.JSchException;


@Component
@Deprecated
public class ExcelGeneratorFromTemplate extends AbstractPOIExcelView {
	
	private static final Logger log = LoggerFactory.getLogger(ExcelGeneratorFromTemplate.class);
	private PatchService patchService;

	@Autowired
	public void setPatchService(PatchService patchService) {
		this.patchService = patchService;
	}

	private DeploymentRequestService deploymentRequestService;

	@Autowired
	public void setDeploymentRequestService(DeploymentRequestService deploymentRequestService) {
		this.deploymentRequestService = deploymentRequestService;
	}
	
	private DeploymentRequest deploymentRequest;
	private List<Patch> patchList;
	
	private String drName;
	private String generatedFileName;
	
	private FileInputStream inputTemplateFile;
	private FileOutputStream generatedFile;
	
	
	private Row rowPatchList, rowPatchMembersList, rowTransferOperationList, rowObjectList, row4,row5;
	
	private SynergyShell shell;
	
	private Sheet patchListSheetTab;
	private Cell columnPatchListDRname;
	private Cell columnPatchListPatchId;
	private Cell columnPatchListStatus;
	private Cell columnPatchListTypEvl;
	private Cell columnPatchListGroupName;
	private Cell columnPatchListSubject;
	private Cell columnPatchListSynopsis;
	private XSSFSheet drMembersSheetTab;
	private Cell columnMembersListPatchReference;
	private Cell columnMembersListMemberType;
	private Cell columnMembersListActionType;
	private Cell columnMembersListMemberName;
	private XSSFSheet transferOperationsSheetTab;
	private XSSFSheet summaryTab;
	
	private XSSFSheet objectListTab;
	private Cell columnObjectListTask;
	private Cell columnObjectListObject;
	private Cell columnObjectListType;
	private Cell columnObjectListVersion;
	private Cell columnObjectListInstance;
	
	
	private Cell cell_31;
	private Cell cell_32;
	private Cell cell_33;
	private Cell cell_34;
	private Cell cell_35;
	private Cell cell_36;
	private Cell cell_37;
	private Cell cell_38;
	private Cell cell_39;
	private Cell cell_391;
	private Cell cell_392;
	private Cell cell_393;
	private Cell cell_394;
	private Cell cell_395;
	private Cell cell_396;
	private Cell cell_351;
	
	

	@Override
	protected void buildExcelDocument(Map<String, Object> model,
			Workbook wb, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		
		this.deploymentRequest = (DeploymentRequest) model.get("deploymentRequest");
		this.shell             = (SynergyShell) model.get("shell");
		
		this.patchList = this.deploymentRequest.getPatchList();

		this.deploymentRequest.setPatchList(patchList);
		
		this.drName = this.deploymentRequest.getDrName();
		
		//this.generatedFileName = this.drName + "-formatted.xls";
		this.generatedFileName = "DR-REPORT/" + this.drName + ".xlsm";
		
		//String inputFileName = "TEMPLATES/REPORT-TEMPLATE.xlsm";
		//String inputFileName = "TEMPLATES/REPORT-TEMPLATE-JGH.xlsm";
		//String inputFileName = "TEMPLATES/REPORT-TEMPLATE_GAL.xlsm";
		String inputFileName = "TEMPLATES/REPORT-TEMPLATE-1.xlsm";
		
		this.inputTemplateFile = new FileInputStream(new File(inputFileName));
		
		
		this.generatedFile = new FileOutputStream(generatedFileName);
		
		//OPCPackage.open("resources/template_with_macro.xlsm")
		//wb = new XSSFWorkbook(this.inputTemplateFile);
		//http://stackoverflow.com/questions/18350178/write-to-xlsm-excel-2007-using-apache-poi
		wb = new XSSFWorkbook(OPCPackage.open(this.inputTemplateFile));
		
		fillSummaryTab((XSSFWorkbook)wb);
		generatePtchList((XSSFWorkbook) wb);
		generateDRMembers((XSSFWorkbook) wb);
		generateTransferOp((XSSFWorkbook) wb);
		generateObjectList((XSSFWorkbook) wb);
		//generateList(wb);
		
		wb.write(generatedFile);
		
		this.inputTemplateFile.close();
		//this.generatedFile.close();
		
		String argument = "attachment; filename=" + this.drName + ".xlsm";
		
		//response.setHeader("Content-disposition", "attachment; filename=test.xlsm");
		response.setHeader("Content-disposition", argument);
		//application/vnd.ms-excel.sheet.macroEnabled.12
		//response.setHeader("Content-disposition","application/vnd.ms-excel.sheet.macroEnabled.12");
		//response.setHeader("Content-disposition", "attachment");
		wb.write( response.getOutputStream() );
		
		this.generatedFile.flush();
		this.generatedFile.close();
		
		
		
	}
	public void fillSummaryTab(XSSFWorkbook wb){
		
		
		
		this.summaryTab = wb.getSheetAt(0);
		for (int i=0;i<20;i++){
			System.out.println("row:"+ i + ":" +this.summaryTab.getRow(1).getCell(i));
		}
		log.info("Tab name   :" + this.summaryTab.getSheetName());
		log.info("Dr name    : " + this.summaryTab.getRow(8).getCell(2));
		log.info("Synopsis   : " + this.summaryTab.getRow(1).getCell(2));
		log.info("From       : " + this.summaryTab.getRow(1).getCell(5));
		log.info("To         : " + this.summaryTab.getRow(1).getCell(7));
		
		
		
		this.deploymentRequestService.getSynopsis(this.drName);
		
		
		this.summaryTab.getRow(8).getCell(2).setCellValue(this.deploymentRequest.getDrName());
		this.summaryTab.getRow(1).getCell(2).setCellValue(this.deploymentRequestService.getSynopsis(this.drName));
		this.summaryTab.getRow(1).getCell(5).setCellValue(this.deploymentRequestService.getEnvSrc(this.drName));
		this.summaryTab.getRow(1).getCell(7).setCellValue(this.deploymentRequestService.getEnvDst(this.drName));
		
		log.info("Dr name         : " + this.deploymentRequest.getDrName());
		log.info("Dest. name         : " + this.deploymentRequestService.getEnvDst(this.drName));
		
		for ( int i=0; i<20; i++){
			System.out.println("row:" + i + ":" + this.summaryTab.getRow(i).getCell(2));
		}
	}
	/*
	 * REFLOT	STPALL	ORDOPN	REFMAI	NUMSTP	NUMTFT	TYPTFT	TYPOPN	SWIMAN	VERNUM	SWIMLT	SWICHK	BYPASS	NOMGRP	IDTENT ITTCMD
	 */
	public void generateTransferOp(XSSFWorkbook wb){
		List<DeploymentRequestTransferOperation> transferOperationList = this.deploymentRequestService.getTransferOperation(this.deploymentRequest.getDrName());
		
		this.transferOperationsSheetTab = wb.getSheetAt(5);
		//rowTransferOperationList = transferOperationsSheetTab.createRow((short) 0);
		
		int rowsInTransferOpTab = 1;
		for (DeploymentRequestTransferOperation transferOperation : transferOperationList) {
			rowTransferOperationList = transferOperationsSheetTab.createRow((short) rowsInTransferOpTab);
		
			cell_31 = rowTransferOperationList.createCell((short) 0);
			cell_32 = rowTransferOperationList.createCell((short) 1);
			cell_33 = rowTransferOperationList.createCell((short) 2);
			cell_34 = rowTransferOperationList.createCell((short) 3);
			cell_35 = rowTransferOperationList.createCell((short) 4);
			cell_351 = rowTransferOperationList.createCell((short) 5);//NUMTFT
			cell_36 = rowTransferOperationList.createCell((short) 6);
			cell_37 = rowTransferOperationList.createCell((short) 7);
			cell_38 = rowTransferOperationList.createCell((short) 8);
			cell_39 = rowTransferOperationList.createCell((short) 9);
			cell_391 = rowTransferOperationList.createCell((short) 10);
			cell_392 = rowTransferOperationList.createCell((short) 11);
			cell_393 = rowTransferOperationList.createCell((short) 12);
			cell_394 = rowTransferOperationList.createCell((short) 13);
			cell_395 = rowTransferOperationList.createCell((short) 14);
			cell_396 = rowTransferOperationList.createCell((short) 15);

			cell_31.setCellValue(transferOperation.getReflot());
			cell_32.setCellValue(transferOperation.getStpall());
			cell_33.setCellValue(transferOperation.getOrdopn());
			cell_34.setCellValue(transferOperation.getRefmai());
			cell_35.setCellValue(transferOperation.getNumstp());
			cell_351.setCellValue("NUMTFT-not-added");
			cell_36.setCellValue(transferOperation.getTyptft());
			cell_37.setCellValue(transferOperation.getTypopn());
			cell_38.setCellValue(transferOperation.getSwiman());
			cell_39.setCellValue(transferOperation.getVernum());
			cell_391.setCellValue(transferOperation.getSwimlt());
			cell_392.setCellValue(transferOperation.getSwichk());
			cell_393.setCellValue(transferOperation.getBypass());//swibypass
			cell_394.setCellValue(transferOperation.getNomgrp());
			cell_395.setCellValue(transferOperation.getIdtent());
			cell_396.setCellValue(transferOperation.getIttcmd());
			rowsInTransferOpTab++;
		}
	}
	/*
	 * REFPAT	NOMMBR	TYPMBR	TYPACT	TYPEVL
	 */
	public void generateDRMembers(XSSFWorkbook wb) {
		int rowCounter = 1;
		
		log.info("generateList DR members for    :" 	+ this.deploymentRequest.getDrName());
		List<PatchMember> patchMembersList = this.deploymentRequestService.getDRMembers(this.deploymentRequest.getDrName());
		
		this.drMembersSheetTab = wb.getSheetAt(4);
		log.info("Tab name:" + drMembersSheetTab.getSheetName());
		
		for (PatchMember patchMember : patchMembersList) {
			rowPatchMembersList = this.drMembersSheetTab.createRow((short) rowCounter);
			
			columnMembersListPatchReference = rowPatchMembersList.createCell((short) 0);
			columnMembersListMemberName =     rowPatchMembersList.createCell((short) 1);
			columnMembersListMemberType =     rowPatchMembersList.createCell((short) 2);
			columnMembersListActionType =     rowPatchMembersList.createCell((short) 3);
			
			
			columnMembersListPatchReference.setCellValue(patchMember.getPatchId());
			columnMembersListMemberName.setCellValue(patchMember.getPatchMember());
			columnMembersListMemberType.setCellValue(patchMember.getMemberType());
			columnMembersListActionType.setCellValue(patchMember.getTypAct());
			rowCounter++;
			
			
		}
		
		
	}
	/**
	 * This tab will contain the list of objects related to the DR
	 * @param wb
	 * @throws JSchException
	 * @throws IOException
	 */
	public void generateObjectList(XSSFWorkbook wb) throws JSchException, IOException{
		//TODO
		int rowCounter = 1;
		
		this.objectListTab = wb.getSheetAt(6);
		log.info("Tab name:" + objectListTab.getSheetName());
		
		List<SynergyObject>  synergyObjects;
		synergyObjects = this.shell.getObjectsLinkedToDR(this.deploymentRequest.getDrName());
		
		for (SynergyObject synObject:  synergyObjects){
			System.out.println("****Task: " + synObject.getTask());
			System.out.println("***Object: "+ synObject.getObject());
			rowObjectList = this.objectListTab.createRow((short) rowCounter);
			
			columnObjectListTask      = rowObjectList.createCell((short) 0);
			columnObjectListObject    = rowObjectList.createCell((short) 1);
			columnObjectListType      = rowObjectList.createCell((short) 2);
			columnObjectListVersion   = rowObjectList.createCell((short) 3);
			columnObjectListInstance  = rowObjectList.createCell((short) 4);
			
			columnObjectListObject.setCellValue(synObject.getObject());
			columnObjectListTask.setCellValue(synObject.getTask());
			columnObjectListType.setCellValue(synObject.getType());
			columnObjectListVersion.setCellValue(synObject.getVersion());
			columnObjectListInstance.setCellValue(synObject.getInstance());
			
			rowCounter++;
			
		}
	    
	}
	/**
	 * This tab/excel sheet will contain the following columns
	 * REFPAT	STAPAT	NOMGRP	TYPEVL	SUBJECT	SYNOPSIS
	 * @param wb
	 */
	public void generatePtchList(XSSFWorkbook wb){

		log.info("generateDocument() :Size of patch list:" + patchList.size());
		int rowCounter = 1;
		
		for (Patch patch : this.patchList) {
			List<Patch> patchCompleteList = (List<Patch>) this.patchService.getPatchDescription(patch.getPatchId());
			
			
			this.patchListSheetTab = wb.getSheetAt(3);
			log.info("Tab name:" + patchListSheetTab.getSheetName());
			
			// TODO find a better way of finding patch description
			Patch patchDescription = new Patch();
			if (patchCompleteList.isEmpty()) {
				patchDescription.setPatchId("NOT FOUND in Inventory table!");	
				patchDescription.setNomGrp("NOT FOUND in Inventory table!");
			}else{				
			   patchDescription = patchCompleteList.get(0);
			}
			
			
			//columnPatchListDRname = rowPatchList.createCell((short) 0);
			
			//columnPatchListPatchId = rowPatchList.createCell((short) 0);
			rowPatchList = patchListSheetTab.createRow(rowCounter);
			
			this.columnPatchListPatchId = rowPatchList.createCell(0); //column that corresponds to refpat in patch list tab
			this.columnPatchListStatus =  rowPatchList.createCell(1);
			this.columnPatchListGroupName =  rowPatchList.createCell(2);
			this.columnPatchListTypEvl =  rowPatchList.createCell(3);
			this.columnPatchListSubject =  rowPatchList.createCell(4);
			this.columnPatchListSynopsis =  rowPatchList.createCell(5);
			
			
			this.columnPatchListPatchId.setCellValue(patch.getPatchId());
			this.columnPatchListStatus.setCellValue("STAPAT-to-be-added");
			this.columnPatchListGroupName.setCellValue(patchDescription.getNomGrp());
			this.columnPatchListTypEvl.setCellValue(patchDescription.getTypEvl());
			this.columnPatchListSubject.setCellValue(patchDescription.getSujPat());
			this.columnPatchListSynopsis.setCellValue(patchDescription.getSynopsis());
			
			
			rowCounter++;

			log.debug("Rows written: " + rowCounter);			
			
		}
		
	}
	public void generateList(XSSFWorkbook wb) {
		log.info("generateDocument() :Size of patch list:" + patchList.size());
		int rowCounter = 1;
		
		for (Patch patch : this.patchList) {
			List<Patch> patchCompleteList = (List<Patch>) this.patchService.getPatchDescription(patch.getPatchId());
			
			
			this.patchListSheetTab = wb.getSheetAt(3);
			System.out.println("Tab name:" + patchListSheetTab.getSheetName());
			
			//this.columnPatchListPatchId = this.patchListSheetTab.getRow(rowCounter).getCell(1);
			
		
			// TODO find a better way of finding patch description
			Patch patchDescription = patchCompleteList.get(0);
			//this.rowPatchList = patchListSheetTab.createRow((short) rowCounter);
			
			// creation of columns
			//columnPatchListDRname = rowPatchList.createCell((short) 0);
			
			//columnPatchListPatchId = rowPatchList.createCell((short) 0);
			rowPatchList = patchListSheetTab.createRow((short) rowCounter);
			columnPatchListPatchId = rowPatchList.createCell((short) 0); //column that corresponds to refpat in patch list tab
			this.columnPatchListPatchId.setCellValue(patch.getPatchId());

			
			//columnPatchListPatchId = rowPatchList.createCell((short) 1);
			//columnPatchListGroupName = rowPatchList.createCell((short) 2);
			//columnPatchListSubject = rowPatchList.createCell((short) 3);

			//columnPatchListDRname.setCellValue(this.deploymentRequest.getDrName());
			columnPatchListPatchId.setCellValue(patch.getPatchId());
			//columnPatchListGroupName.setCellValue(patchDescription.getNomGrp());
			//columnPatchListSubject.setCellValue(patchDescription.getSujPat());

			rowCounter++;

			log.debug("Rows written: " + rowCounter);			
			
		}
		
	}

}
