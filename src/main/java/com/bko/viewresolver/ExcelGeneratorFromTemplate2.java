package com.bko.viewresolver;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bko.domain.DeploymentRequest;
import com.bko.domain.Patch;
import com.bko.service.DeploymentRequestService;
import com.bko.service.PatchService;
import com.bko.viewresolver.util.Shell;

@Component
public class ExcelGeneratorFromTemplate2 extends AbstractPOIExcelView{
	
	private BaseExcelGeneratorFromTemplate excelGenerator;
	
	
	private PatchService patchService;
	private DeploymentRequest deploymentRequest;
	private List<Patch> patchList;

	private DeploymentRequestService deploymentRequestService;
	@Autowired
	public void setDeploymentRequestService(DeploymentRequestService deploymentRequestService) {
		this.deploymentRequestService = deploymentRequestService;
	}
	@Autowired
	public void setPatchService(PatchService patchService) {
		this.patchService = patchService;
	}

	
	private Shell shell;

	@Override
	protected void buildExcelDocument(Map<String, Object> model,
			Workbook wb, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		
		this.deploymentRequest = (DeploymentRequest) model.get("deploymentRequest");
		this.shell             = (Shell) model.get("shell");
		
		//this.patchList = this.deploymentRequest.getPatchList();

		//this.deploymentRequest.setPatchList(patchList);
		
		String drName = this.deploymentRequest.getDrName();
		
		
		//this.generatedFileName = this.drName + "-formatted.xls";
		String generatedFileName = "DR-REPORT/" + drName + ".xlsm";
		
		//String inputFileName = "TEMPLATES/REPORT-TEMPLATE.xlsm";
		//String inputFileName = "TEMPLATES/REPORT-TEMPLATE-JGH.xlsm";
		//String inputFileName = "TEMPLATES/REPORT-TEMPLATE_GAL.xlsm";
		String inputFileName = "TEMPLATES/REPORT-TEMPLATE-1.xlsm";
		
		FileInputStream inputTemplateFile = new FileInputStream(new File(inputFileName));
		
		
		FileOutputStream generatedFile = new FileOutputStream(generatedFileName);
		
		//OPCPackage.open("resources/template_with_macro.xlsm")
		//wb = new XSSFWorkbook(this.inputTemplateFile);
		//http://stackoverflow.com/questions/18350178/write-to-xlsm-excel-2007-using-apache-poi
		wb = new XSSFWorkbook(OPCPackage.open(inputTemplateFile));
		
		this.excelGenerator = new BaseExcelGeneratorFromTemplate();
		this.excelGenerator.setDeploymentRequest(this.deploymentRequest);
		this.excelGenerator.setDeploymentRequestService(this.deploymentRequestService);
		this.excelGenerator.initialize_DR(drName, shell);
		
		this.excelGenerator.fillSummaryTab((XSSFWorkbook)wb);
		this.excelGenerator.generatePtchList((XSSFWorkbook) wb);
		this.excelGenerator.generateDRMembers((XSSFWorkbook) wb);
		this.excelGenerator.generateTransferOp((XSSFWorkbook) wb);
		this.excelGenerator.generateObjectList((XSSFWorkbook) wb);
		this.excelGenerator.fillMemberTpes((XSSFWorkbook)wb);
		//generateList(wb);
		
		wb.write(generatedFile);
		
		inputTemplateFile.close();
		//this.generatedFile.close();
		
		String argument = "attachment; filename=" + drName + ".xlsm";
		
		//response.setHeader("Content-disposition", "attachment; filename=test.xlsm");
		response.setHeader("Content-disposition", argument);
		//application/vnd.ms-excel.sheet.macroEnabled.12
		//response.setHeader("Content-disposition","application/vnd.ms-excel.sheet.macroEnabled.12");
		//response.setHeader("Content-disposition", "attachment");
		wb.write( response.getOutputStream() );
		
		generatedFile.flush();
		generatedFile.close();
		this.shell.getSession().disconnect();
		
		
		
	}

}
