package com.bko.viewresolver;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;

import com.bko.service.DeploymentRequestService;
import com.bko.service.PatchService;

@Component
public class ExcelViewResolver implements ViewResolver{
	
	@Autowired
	private DeploymentRequestService deploymentRequestService;
	@Autowired
	private PatchService patchService;
	
	@Override
	public View resolveViewName(String viewName, Locale locale) throws Exception {
		//ExcelView view = new ExcelView();
		System.out.println("Calling new ExcelGenerator");
		
		//ExcelGenerator view = new ExcelGenerator();
		//ExcelGeneratorFromTemplate view = new ExcelGeneratorFromTemplate();
		//ExcelGeneratorFromTemplate2 view = new ExcelGeneratorFromTemplate2();
		ReleaseExcelGenerator  view = new ReleaseExcelGenerator();
		//view.setPatchService(patchService);
		view.setDeploymentRequestService(deploymentRequestService);
		//System.out.println("ExcelViewResolver:" + deploymentRequestService.getNumberOfPatches("PACK-PND-0691"));
		return view;
      }
	
}