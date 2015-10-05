package com.bko.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.accept.ContentNegotiationManager;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.ContentNegotiatingViewResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import com.bko.viewresolver.ExcelViewResolver;


@Configuration
//@EnableWebMvc
//@ComponentScan(basePackages = "com.bko.controller")
public class WebConfig extends WebMvcConfigurerAdapter{
	
	/*
	 * Configure ContentNegotiationManager
	 */
	@Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }
    
	
	/*
     * Configure ContentNegotiatingViewResolver
     */
    @Bean
    public ViewResolver contentNegotiatingViewResolver(ContentNegotiationManager manager) {
        ContentNegotiatingViewResolver resolver = new ContentNegotiatingViewResolver();
        resolver.setContentNegotiationManager(manager);
 
        // Define all possible view resolvers
        List<ViewResolver> resolvers = new ArrayList<ViewResolver>();
 
        //resolvers.add(jsonViewResolver());
        resolvers.add(jspViewResolver());//jsp ------
        //resolvers.add(pdfViewResolver());
        //resolvers.add(excelViewResolver());
         
        resolver.setViewResolvers(resolvers);
        //resolver.setOrder(2);
        return resolver;
    }
    @Bean
	public ViewResolver jspViewResolver() {
		InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
		//viewResolver.setViewClass(JstlView.class);
		viewResolver.setPrefix("/WEB-INF/jsp/");
		viewResolver.setSuffix(".jsp");
		return viewResolver;
	}
	@Bean
    public ViewResolver excelViewResolver() {
		ExcelViewResolver myExcelViewer = new ExcelViewResolver();
        return myExcelViewer;
    }	


}
