package com.bko.viewresolver;

import java.io.FileOutputStream;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.document.AbstractExcelView;

import com.bko.domain.DeploymentRequest;
import com.bko.domain.DeploymentRequestTransferOperation;
import com.bko.domain.Patch;
import com.bko.domain.PatchMember;
import com.bko.domain.TransferOperation;
import com.bko.service.DeploymentRequestService;
import com.bko.service.PatchService;

@Component
@Deprecated
public class ExcelGenerator extends AbstractExcelView {

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

	private String generatedFileName;
	private FileOutputStream generatedFile;
	
	private DeploymentRequest deploymentRequest;

	private String deploymentRequestName;

	private List<Patch> patchList;
	
	private String drName;

	private static final Logger log = LoggerFactory.getLogger(ExcelGenerator.class);

	// private ArrayList<TransferOperationRule> transferOperationRuleList;

	// private ArrayList<String> touchyMembersList;

	class TransferOperationRule {
		String category;
		String complement;
	}

	private HSSFWorkbook workbook;

	private Sheet sheet1;
	private Sheet sheet2;
	private Sheet sheet3;
	private Sheet sheet4, sheet5, sheet6;

	Cell columnPatchListDRname, columnPatchListPatchId,
			columnPatchListGroupName, columnPatchListSubject,
			columnMembersListPatchReference, columnMembersListMemberType,
			columnMembersListActionType, columnMembersListMemberName,
			columnMembersListTouchyMember, columnStepAll, cell_32, cell_33,
			cell_34, cell_35, columnCategory, columnComplement, cell_36,cell_37, cell_38, 
			     cell_39, cell_391, cell_392,cell_393,cell_394,cell_395, 
			cell_41,cell_42, cell_43, cell_44, cell_51, cell_52, cell_61, cell_62,
			cell_63;

	Row rowPatchList, rowPatchMembersList, rowTransferOperationList, row4,
			row5;

	@Override
	protected void buildExcelDocument(Map model, HSSFWorkbook wb,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {


		this.sheet1 = wb.createSheet("Patch List");
		this.sheet2 = wb.createSheet("Patch members list");
		this.sheet3 = wb.createSheet("TFT operations");
		this.sheet4 = wb.createSheet("YPD23 operations");
		// this.sheet5 = wb.createSheet("Missing Members");
		// this.sheet6 = wb.createSheet("Missing YE");

		// this.loadTransferOpRule(getCorrectPath("transfer_op_rule.txt"));
		// this.loadTouchyMembers(getCorrectPath("touchy_members.txt"));

		// List<Patch> patchList = (List<Patch>)model.get("deploymentRequest");
		// deploymentRequest
		this.deploymentRequest = (DeploymentRequest) model.get("deploymentRequest");

		this.patchList = this.deploymentRequest.getPatchList();

		this.deploymentRequest.setPatchList(patchList);
		
		this.drName = this.deploymentRequest.getDrName();
		
		this.generatedFileName = this.drName + "-1.xls";
		this.generatedFile = new FileOutputStream(generatedFileName);

		log.info("buildExcelDocument() :Name of the DR    :" + this.deploymentRequest.getDrName());
		log.info("buildExcelDocument() :Size of patch list:" + this.deploymentRequest.getPatchList().size());

		generateList(wb);
		log.info("To generate file:"+ generatedFileName);
		wb.write(generatedFile);
		
		this.generatedFile.flush();
		this.generatedFile.close();
		

	}
	
	public void formatHeader(HSSFWorkbook wb) {

		// Style
		// HSSFWorkbook wblocal;
		HSSFCellStyle myStyle = wb.createCellStyle();

		myStyle.setFillForegroundColor(HSSFColor.BLUE.index);
		myStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		// myStyle.setFillForegroundColor(HSSFColor.WHITE.index);
		Font font = wb.createFont();
		font.setColor(HSSFColor.WHITE.index);
		myStyle.setFont(font);

	}

	public void generateList(HSSFWorkbook wb) {
		log.info("generateDocument() :Size of patch list:" + patchList.size());
		int rowCounter = 0;
		// int indexMembers = 1;

		sheet1.createFreezePane(0, 1);

		HSSFCellStyle myStyle = wb.createCellStyle();

		myStyle.setFillForegroundColor(HSSFColor.BLUE.index);
		myStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		// myStyle.setFillForegroundColor(HSSFColor.WHITE.index);
		Font font = wb.createFont();
		font.setColor(HSSFColor.WHITE.index);
		myStyle.setFont(font);


		rowPatchList = sheet1.createRow((short) rowCounter);
		// creation of columns
		columnPatchListDRname = rowPatchList.createCell((short) 0);
		columnPatchListPatchId = rowPatchList.createCell((short) 1);
		columnPatchListGroupName = rowPatchList.createCell((short) 2);
		columnPatchListSubject = rowPatchList.createCell((short) 3);
		
		columnPatchListDRname.setCellStyle(myStyle);
		columnPatchListPatchId.setCellStyle(myStyle);
		columnPatchListGroupName.setCellStyle(myStyle);
		columnPatchListSubject.setCellStyle(myStyle);
		
		columnPatchListDRname.setCellValue("DR name");
		columnPatchListPatchId.setCellValue("Patch Reference");
		columnPatchListGroupName.setCellValue("Group Name");
		columnPatchListSubject.setCellValue("Subject");
		
		rowCounter++;

		for (Patch patch : this.patchList) {

			List<Patch> patchCompleteList = (List<Patch>) this.patchService.getPatchDescription(patch.getPatchId());

			// TODO find a better way of finding patch description
			Patch patchDescription = patchCompleteList.get(0);

			log.info("generateList patch  ref     :" + patchDescription.getPatchId());
			log.info("generateList patch  GROUP   :" + patchDescription.getNomGrp());
			log.info("generateList patch  SUBJECT :" + patchDescription.getSujPat());

			rowPatchList = sheet1.createRow((short) rowCounter);

			// creation of columns
			columnPatchListDRname = rowPatchList.createCell((short) 0);
			columnPatchListPatchId = rowPatchList.createCell((short) 1);
			columnPatchListGroupName = rowPatchList.createCell((short) 2);
			columnPatchListSubject = rowPatchList.createCell((short) 3);

			columnPatchListDRname.setCellValue(this.deploymentRequest.getDrName());
			columnPatchListPatchId.setCellValue(patch.getPatchId());
			columnPatchListGroupName.setCellValue(patchDescription.getNomGrp());
			columnPatchListSubject.setCellValue(patchDescription.getSujPat());

			rowCounter++;

			log.debug("Rows written: " + rowCounter);
		}
		generateDRMembers(wb);
		generateTransferOperation(wb);
		// generateTransferOperation();
		// generateYpd23("0000000E");
		// generateYpd23(this.reflot);
		// generateYAmissingList(this.reflot);
	}

	public void generateDRMembers(HSSFWorkbook wb) {

		log.info("generateList DR members for    :"
				+ this.deploymentRequest.getDrName());

		List<PatchMember> patchMembersList = this.deploymentRequestService.getDRMembers(this.deploymentRequest.getDrName());

		// Style
		// HSSFWorkbook wblocal;
		HSSFCellStyle myStyle = wb.createCellStyle();

		myStyle.setFillForegroundColor(HSSFColor.BLUE.index);
		myStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		// myStyle.setFillForegroundColor(HSSFColor.WHITE.index);
		Font font = wb.createFont();
		font.setColor(HSSFColor.WHITE.index);
		myStyle.setFont(font);

		rowPatchMembersList = sheet2.createRow((short) 0);

		sheet2.createFreezePane(0, 1);
		// category and complement
		columnMembersListPatchReference = rowPatchMembersList.createCell((short) 0);
		columnMembersListMemberType = rowPatchMembersList.createCell((short) 1);
		columnMembersListActionType = rowPatchMembersList.createCell((short) 2);
		columnMembersListMemberName = rowPatchMembersList.createCell((short) 3);
		columnMembersListTouchyMember = rowPatchMembersList.createCell((short) 4);

		columnMembersListPatchReference.setCellStyle(myStyle);
		columnMembersListMemberType.setCellStyle(myStyle);
		columnMembersListActionType.setCellStyle(myStyle);
		columnMembersListMemberName.setCellStyle(myStyle);
		columnMembersListTouchyMember.setCellStyle(myStyle);

		columnMembersListPatchReference.setCellValue("REFPAT");
		columnMembersListMemberType.setCellValue("TYPMBR");
		columnMembersListActionType.setCellValue("TYPACT");
		columnMembersListMemberName.setCellValue("NOMMBR");
		columnMembersListTouchyMember.setCellValue("TOUCHY");

		int indexMembers = 1; // first row for the titles
		for (PatchMember patchMember : patchMembersList) {

			log.info("patchMember    :" + patchMember.getPatchMember());

			rowPatchMembersList = sheet2.createRow((short) indexMembers);
			columnMembersListPatchReference = rowPatchMembersList.createCell((short) 0);
			columnMembersListMemberType = rowPatchMembersList.createCell((short) 1);
			columnMembersListActionType = rowPatchMembersList.createCell((short) 2);
			columnMembersListMemberName = rowPatchMembersList.createCell((short) 3);
			// cell_25 = row2.createCell((short) 4);

			columnMembersListPatchReference.setCellValue(patchMember.getPatchId());
			columnMembersListMemberType.setCellValue(patchMember.getMemberType());
			columnMembersListActionType.setCellValue(patchMember.getTypAct());
			columnMembersListMemberName.setCellValue(patchMember.getPatchMember());

			indexMembers++;
		}

	}

	public void generateTransferOperation(HSSFWorkbook wb) {

		List<DeploymentRequestTransferOperation> transferOperationList = this.deploymentRequestService.getTransferOperation(this.deploymentRequest.getDrName());

		HSSFCellStyle myStyle = wb.createCellStyle();

		myStyle.setFillForegroundColor(HSSFColor.BLUE.index);
		myStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		// myStyle.setFillForegroundColor(HSSFColor.WHITE.index);
		Font font = wb.createFont();
		font.setColor(HSSFColor.WHITE.index);
		myStyle.setFont(font);

		rowTransferOperationList = sheet3.createRow((short) 0);
		// category and complement
		// TODO: refactor column names
		sheet3.createFreezePane(0, 1);
		columnCategory = rowTransferOperationList.createCell((short) 0);
		columnComplement = rowTransferOperationList.createCell((short) 1);

		columnStepAll = rowTransferOperationList.createCell((short) 2);
		cell_32 = rowTransferOperationList.createCell((short) 3);
		cell_33 = rowTransferOperationList.createCell((short) 4);
		cell_34 = rowTransferOperationList.createCell((short) 5);
		cell_35 = rowTransferOperationList.createCell((short) 6);
		cell_36 = rowTransferOperationList.createCell((short) 7);
		cell_37 = rowTransferOperationList.createCell((short) 8);
		cell_38 = rowTransferOperationList.createCell((short) 9);
		cell_39 = rowTransferOperationList.createCell((short) 10);
		
		cell_391 = rowTransferOperationList.createCell((short)11);
		cell_392 = rowTransferOperationList.createCell((short) 12);
		cell_393 = rowTransferOperationList.createCell((short) 13);
		cell_394 = rowTransferOperationList.createCell((short) 14);
		cell_395 = rowTransferOperationList.createCell((short) 15);

		columnCategory.setCellStyle(myStyle);
		columnComplement.setCellStyle(myStyle);
		columnStepAll.setCellStyle(myStyle);
		cell_32.setCellStyle(myStyle);

		cell_33.setCellStyle(myStyle);
		cell_34.setCellStyle(myStyle);
		cell_35.setCellStyle(myStyle);
		cell_36.setCellStyle(myStyle);
		cell_37.setCellStyle(myStyle);
		cell_38.setCellStyle(myStyle);
		cell_39.setCellStyle(myStyle);
		cell_391.setCellStyle(myStyle);
		cell_392.setCellStyle(myStyle);
		cell_393.setCellStyle(myStyle);
		cell_394.setCellStyle(myStyle);
		cell_395.setCellStyle(myStyle);

		columnCategory.setCellValue("Category");
		columnComplement.setCellValue("Complement");
		columnStepAll.setCellValue("REFLOT");
		cell_32.setCellValue("STPALL");
		cell_33.setCellValue("ORDOPN");
		cell_34.setCellValue("REFMAI");
	
		cell_35.setCellValue("NUMSTP");
		cell_36.setCellValue("TYPTFT");
		cell_37.setCellValue("SWIMAN");
		cell_38.setCellValue("VERNUM");
		cell_39.setCellValue("SWIMLT");
		cell_391.setCellValue("SWICHK");
		cell_392.setCellValue("BYPASS");
		cell_393.setCellValue("NOMGRP");
		cell_394.setCellValue("IDTENT");
		cell_395.setCellValue("ITTCMD");

		int rowsInTransferOpTab = 1;
		for (DeploymentRequestTransferOperation transferOperation : transferOperationList) {
			rowTransferOperationList = sheet3.createRow((short) rowsInTransferOpTab);
			columnCategory = rowTransferOperationList.createCell((short) 0);
			columnComplement = rowTransferOperationList.createCell((short) 1);

			columnStepAll = rowTransferOperationList.createCell((short) 2);
			cell_32 = rowTransferOperationList.createCell((short) 3);
			cell_33 = rowTransferOperationList.createCell((short) 4);
			cell_34 = rowTransferOperationList.createCell((short) 5);
			cell_35 = rowTransferOperationList.createCell((short) 6);
			cell_36 = rowTransferOperationList.createCell((short) 7);
			cell_37 = rowTransferOperationList.createCell((short) 8);
			cell_38 = rowTransferOperationList.createCell((short) 9);
			cell_39 = rowTransferOperationList.createCell((short) 10);
			cell_391 = rowTransferOperationList.createCell((short) 11);
			cell_392 = rowTransferOperationList.createCell((short) 12);
			cell_393 = rowTransferOperationList.createCell((short) 13);
			cell_394 = rowTransferOperationList.createCell((short) 14);
			cell_395 = rowTransferOperationList.createCell((short) 15);

			columnStepAll.setCellValue(transferOperation.getReflot());
			cell_32.setCellValue(transferOperation.getStpall());
			cell_33.setCellValue(transferOperation.getOrdopn());
			cell_34.setCellValue(transferOperation.getRefmai());
			cell_35.setCellValue(transferOperation.getNumstp());
			cell_36.setCellValue(transferOperation.getTyptft());
			cell_37.setCellValue(transferOperation.getSwiman());
			cell_38.setCellValue(transferOperation.getVernum());
			cell_39.setCellValue(transferOperation.getSwimlt());
			cell_391.setCellValue(transferOperation.getSwichk());
			cell_392.setCellValue(transferOperation.getBypass());//swibypass
			cell_393.setCellValue(transferOperation.getNomgrp());
			cell_394.setCellValue(transferOperation.getIdtent());
			cell_395.setCellValue(transferOperation.getIttcmd());

			rowsInTransferOpTab++;
		}

	}

	public String getCorrectPath(String file_name) {
		String f_path = System.getProperty("user.home")
		// + System.getProperty("file.seperator")
				+ "/" + file_name;
		return f_path;
	}

}
