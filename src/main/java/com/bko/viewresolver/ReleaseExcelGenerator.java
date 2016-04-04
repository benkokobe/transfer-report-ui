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

import com.bko.domain.DeploymentRequest;
import com.bko.service.DeploymentRequestService;
import com.bko.viewresolver.util.ReleaseManager;
import com.bko.viewresolver.util.SynergyShell;

public class ReleaseExcelGenerator extends AbstractPOIExcelView{
	
	private BaseReleaseExcelGenerator excelGenerator;
	
	
	//private DeploymentRequest deploymentRequest;
	private ReleaseManager releaseManager;

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

		
		this.releaseManager = (ReleaseManager) model.get("releaseManager");
		//this.shell             = (SynergyShell) model.get("shell");
		
		
		String releaseName = this.releaseManager.getReleaseName();
		
		
		String generatedFileName = "DR-REPORT/" + releaseName + ".xlsm";
		
		String inputFileName = "TEMPLATES/REPORT-TEMPLATE-RELEASE.xlsm";
		
		FileInputStream inputTemplateFile = new FileInputStream(new File(inputFileName));
		
		
		FileOutputStream generatedFile = new FileOutputStream(generatedFileName);
		
		/**
		 * Ability to read and write on Excel doc which contains macros
		 * http://stackoverflow.com/questions/18350178/write-to-xlsm-excel-2007-using-apache-poi
		 */
		wb = new XSSFWorkbook(OPCPackage.open(inputTemplateFile));
		
		this.excelGenerator = new BaseReleaseExcelGenerator();
		//this.excelGenerator.setDeploymentRequest(this.deploymentRequest);
		//this.excelGenerator.setDeploymentRequestService(this.deploymentRequestService);
		this.excelGenerator.initialize_release(releaseName, shell,deploymentRequestService, this.releaseManager);
		
		this.excelGenerator.fillSummaryTab((XSSFWorkbook)wb);
		this.excelGenerator.fillPatchListTab((XSSFWorkbook) wb);
		this.excelGenerator.fillDBmembersTab((XSSFWorkbook) wb);
	    this.excelGenerator.fillTransferOperationsTab((XSSFWorkbook) wb);
		this.excelGenerator.fillSynergyObjectsTab((XSSFWorkbook) wb);
		this.excelGenerator.fillMemberTypesTab((XSSFWorkbook)wb);
		
		wb.write(generatedFile);
		
		inputTemplateFile.close();
		
		String argument = "attachment; filename=" + releaseName + ".xlsm";
		
		response.setHeader("Content-disposition", argument);
		
		/**
		 * application/vnd.ms-excel.sheet.macroEnabled.12
		   response.setHeader("Content-disposition","application/vnd.ms-excel.sheet.macroEnabled.12");
     	   response.setHeader("Content-disposition", "attachment");
		*/
		
		wb.write( response.getOutputStream() );
		
		generatedFile.flush();
		generatedFile.close();
		//this.shell.getSession().disconnect();
		
	}
	

}
