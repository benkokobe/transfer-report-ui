package com.bko.viewresolver;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bko.domain.DeploymentRequest;
import com.bko.service.DeploymentRequestService;
import com.bko.viewresolver.util.SynergyShell;

@Component
public class ExcelGeneratorFromTemplate2 extends AbstractPOIExcelView{
	
	private BaseExcelGeneratorFromTemplate excelGenerator;
	
	
	private DeploymentRequest deploymentRequest;

	private DeploymentRequestService deploymentRequestService;
	@Autowired
	public void setDeploymentRequestService(DeploymentRequestService deploymentRequestService) {
		this.deploymentRequestService = deploymentRequestService;
	}

	
	private SynergyShell shell;

	@Override
	protected void buildExcelDocument(Map<String, Object> model,
			Workbook wb, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		
		this.deploymentRequest = (DeploymentRequest) model.get("deploymentRequest");
		this.shell             = (SynergyShell) model.get("shell");
		
		
		String drName = this.deploymentRequest.getDrName();
		
		
		String generatedFileName = "DR-REPORT/" + drName + ".xlsm";
		
		String inputFileName = "TEMPLATES/REPORT-TEMPLATE-1.xlsm";
		
		FileInputStream inputTemplateFile = new FileInputStream(new File(inputFileName));
		
		
		FileOutputStream generatedFile = new FileOutputStream(generatedFileName);
		
		/**
		 * Ability to read and write on Excel doc which contains macros
		 * http://stackoverflow.com/questions/18350178/write-to-xlsm-excel-2007-using-apache-poi
		 */
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
		
		wb.write(generatedFile);
		
		inputTemplateFile.close();
		
		String argument = "attachment; filename=" + drName + ".xlsm";
		
		response.setHeader("Content-disposition", argument);
		
		/**
		 * application/vnd.ms-excel.sheet.macroEnabled.12
		   response.setHeader("Content-disposition","application/vnd.ms-excel.sheet.macroEnabled.12");
     	   response.setHeader("Content-disposition", "attachment");
		*/
		
		wb.write( response.getOutputStream() );
		
		generatedFile.flush();
		generatedFile.close();
		this.shell.getSession().disconnect();
		
	}

}
