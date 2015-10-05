package com.bko.viewresolver;

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
import com.bko.domain.Patch;
import com.bko.domain.PatchMember;
import com.bko.domain.TransferOperation;
import com.bko.service.DeploymentRequestService;
import com.bko.service.PatchService;

@Component
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

	private DeploymentRequest deploymentRequest;

	private String deploymentRequestName;

	private List<Patch> patchList;

	private static final Logger log = LoggerFactory
			.getLogger(ExcelGenerator.class);

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
			cell_34, cell_35, columnCategory, columnComplement, cell_41,
			cell_42, cell_43, cell_44, cell_51, cell_52, cell_61, cell_62,
			cell_63;

	Row rowPatchList, rowPatchMembersList, rowTransferOperationList, row4,
			row5;

	@Override
	protected void buildExcelDocument(Map model, HSSFWorkbook wb,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		// this.workbook = new HSSFWorkbook();

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

		log.info("buildExcelDocument() :Name of the DR    :" + this.deploymentRequest.getDrName());
		log.info("buildExcelDocument() :Size of patch list:" + this.deploymentRequest.getPatchList().size());

		generateList(wb);

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

		List<TransferOperation> transferOperationList = this.deploymentRequestService.getTransferOperation(this.deploymentRequest.getDrName());

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
		columnCategory = rowTransferOperationList.createCell((short) 0);
		columnComplement = rowTransferOperationList.createCell((short) 1);

		columnStepAll = rowTransferOperationList.createCell((short) 2);
		cell_32 = rowTransferOperationList.createCell((short) 3);
		cell_33 = rowTransferOperationList.createCell((short) 4);
		cell_34 = rowTransferOperationList.createCell((short) 5);
		cell_35 = rowTransferOperationList.createCell((short) 6);

		columnCategory.setCellStyle(myStyle);
		columnComplement.setCellStyle(myStyle);
		columnStepAll.setCellStyle(myStyle);
		cell_32.setCellStyle(myStyle);

		cell_33.setCellStyle(myStyle);
		cell_34.setCellStyle(myStyle);
		cell_35.setCellStyle(myStyle);

		columnCategory.setCellValue("Category");
		columnComplement.setCellValue("Complement");
		columnStepAll.setCellValue("STPALL");
		cell_32.setCellValue("REFPAT");
		cell_33.setCellValue("SWICHK");
		cell_34.setCellValue("SWIMAN");
		cell_35.setCellValue("TFT - ITTCMD");

		int rowsInTransferOpTab = 1;
		for (TransferOperation transferOperation : transferOperationList) {
			rowTransferOperationList = sheet3
					.createRow((short) rowsInTransferOpTab);
			columnCategory = rowTransferOperationList.createCell((short) 0);
			columnComplement = rowTransferOperationList.createCell((short) 1);

			columnStepAll = rowTransferOperationList.createCell((short) 2);
			cell_32 = rowTransferOperationList.createCell((short) 3);
			cell_33 = rowTransferOperationList.createCell((short) 4);
			cell_34 = rowTransferOperationList.createCell((short) 5);
			cell_35 = rowTransferOperationList.createCell((short) 6);

			columnStepAll.setCellValue(transferOperation.getStpAll());
			cell_32.setCellValue(transferOperation.getPatchRef());
			cell_33.setCellValue(transferOperation.getSwiChk());
			cell_34.setCellValue(transferOperation.getSwiMan());
			cell_35.setCellValue(transferOperation.getIttCmd());

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
