package com.bko.viewresolver;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.context.ApplicationContext;
import org.springframework.web.multipart.MultipartFile;

import com.bko.domain.Patch;
import com.bko.domain.PatchTask;
import com.bko.domain.ReportLine;
import com.bko.persistence.PatchTaskDao;
import com.bko.service.PatchService;
import com.bko.service.PatchTaskService;

public class ConflictReport {

	final static Logger log = Logger.getLogger(ConflictReport.class.getName());

	private PatchTaskService patchTaskservice;
	
	private PatchService patchService;

	public void setPatchTaskservice(PatchTaskService patchTaskservice) {
		this.patchTaskservice = patchTaskservice;
	}

	static final String LOG_PROPERTIES_FILE = "log4j.properties";
	static final String TRANSFER_PROPERTY_FILE = "transfer.properties";

	private Properties props;
	private String reportName;

	private HSSFWorkbook wb;
	private Sheet sheet1;
	Cell cell_11, cell_12, cell_13, cell_14, cell_15, cell_16, cell_17, 
	cell_18,cell_19, cell_20, cell_21; // TODO improve
																// this !! (to
																// put extra
																// column for
																// extra change
																// patches)
	Row row;

	private String generatedFileName;
	private FileOutputStream generatedFile;

	private List<ReportLine> reportLines;
	//private ReportLine conflict;
	private int STATE;

	// private ApplicationContext context;
	private PatchTaskDao patchTask;

	private HashMap<Integer, List<String>> patchLineMap = new HashMap<Integer, List<String>>();

	private List<String> alineInCsvFile;
	
	private List<Patch> patchList;
	
	MultipartFile fileToFormat;
	
	
	public MultipartFile getFileToFormat() {
		return fileToFormat;
	}

	public void setFileToFormat(MultipartFile fileToFormat) {
		this.fileToFormat = fileToFormat;
	}

	private String pifActivatedFilterFile;
	private String petActivatedFilterFile;
	
	HashMap<String, String>petFilterMap;
	HashMap<String, String>pifFilterMap;
	
	public ConflictReport(MultipartFile fileToFormat) throws FileNotFoundException {
		this.setFileToFormat(fileToFormat);
		init();
	}

	public ConflictReport(String fileName) throws FileNotFoundException {
		this.setReportName(fileName);
		init();
	}

	public String getPifActivatedFilterFile() {
		return pifActivatedFilterFile;
	}

	public void setPifActivatedFilterFile(String pifActivatedFilterFile) {
		this.pifActivatedFilterFile = pifActivatedFilterFile;
	}

	public String getPetActivatedFilterFile() {
		return petActivatedFilterFile;
	}

	public void setPetActivatedFilterFile(String petActivatedFilterFile) {
		this.petActivatedFilterFile = petActivatedFilterFile;
	}

	/**
	 * initialize reportName, excel workbook, create the first sheet create an
	 * instance of the conflictList
	 *
	 * @throws FileNotFoundException
	 */
	public String getCorrectPath(String file_name) {
		String f_path = System.getProperty("user.home")
		// + System.getProperty("file.seperator")
				+ "/" + file_name;
		return f_path;
	}

	public void init() throws FileNotFoundException {
		// initializeLogger();
		// loadProperties();
		this.wb = new HSSFWorkbook();
		this.sheet1 = this.wb.createSheet("PCP PET synchro report");

		this.generatedFileName = getCorrectPath(this.reportName + "-formatted.xls");
		log.info("generatedFileName: " + this.generatedFileName);
		
		
		log.info("pifActivatedFilterFile: " + this.pifActivatedFilterFile);
		log.info("petActivatedFilterFile: " + this.petActivatedFilterFile);
		

		// this.generatedFileName = this.generatedFileName + "-1.xls";
		this.generatedFile = new FileOutputStream(generatedFileName);
		this.reportLines = new ArrayList<ReportLine>();
		
		this.petFilterMap =  new HashMap<>();
		this.pifFilterMap =  new HashMap<>();

		// this.context = new ClassPathXmlApplicationContext("beans.xml");
		// this.patchTask = (PatchTaskDao) context.getBean("patchTask");
	}
	
	public void readFilterFile(String fileName, HashMap<String, String>filterMap) {
		
		log.info("readFilterFiler:" + fileName);
		try {
			Scanner scanner = new Scanner(new File(getCorrectPath(fileName)));
			scanner.useDelimiter(System.getProperty("line.separator"));
			log.info("readFilterFiler:" + fileName);
			while (scanner.hasNext()) {
				filterMap.put(scanner.next(), "ACTIVATED");
				//parseLine(scanner.next());
			}
			
		}catch (FileNotFoundException e) {
			// e.printStackTrace("Error file:" + e);
			log.error("Error reading  file: " + e.getMessage());
		}
	}

	public void readFile(String fileName) {
		
		try {
			Scanner scanner = new Scanner(new File(getCorrectPath(fileName)));
			scanner.useDelimiter(System.getProperty("line.separator"));
			log.info("line.separator:" + System.getProperty("line.separator"));
			while (scanner.hasNext()) {
				parseLine(scanner.next());
			}
			// add the last conflict into the list
			log.info("Adding the last conflict into the list");
			//this.reportLines.add(this.conflict);

			scanner.close();
		} catch (FileNotFoundException e) {
			// e.printStackTrace("Error file:" + e);
			log.error("Error reading transfer file: " + e.getMessage());
		}
	}

	private void parseLine(String line) {
		
		if (line.length() == 0) {
			log.info("Empty line");
			return;
		}
		//log.info("line:" + line);
		if (line.contains("Patch ID")) {
			log.info("First line");
			return;
		}
		

		ReportLine reportLine = new ReportLine();
		
		String[] tokens = line.split(";");
		

		if (tokens.length < 2){
			log.info("tokens size: " +tokens.length);
			log.info("line: " + line);
			return;
		}
		/*
		log.info("tokens[1]:" + tokens[1]);
		String refpat = tokens[1]; 
		List<Patch> patch = this.patchService.getPatchDescription(refpat);
		log.info("refpat: refpat:" + patch.get(0).getPatchId());
		log.info("refpat: refpat:" + patch.get(0).getPatchId());
		*/
		
		reportLine.setCrId(tokens[0]);
		reportLine.setPatchReference(tokens[1]);
		reportLine.setStatus(tokens[2]);
		reportLine.setGroup(tokens[3]);
		reportLine.setSubject(tokens[4]);
		reportLine.setEnvironment(tokens[5]);
		reportLine.setProjectCode(tokens[9]);
		reportLine.setMrNumber(tokens[10]);
		
		this.reportLines.add(reportLine);
		
		return;

	}

	private Properties getPropertiesFromClasspath(String propFileName)
			throws IOException {
		// loading xmlProfileGen.properties from the classpath
		Properties props = new Properties();
		InputStream inputStream = this.getClass().getClassLoader()
				.getResourceAsStream(propFileName);

		if (inputStream == null) {
			throw new FileNotFoundException("property file '" + propFileName
					+ "' not found in the classpath");
		}

		props.load(inputStream);

		return props;
	}

	public void createTitle() {

	}

	public void generateConflictReport() throws IOException {
		
		readFilterFile(this.pifActivatedFilterFile, this.pifFilterMap);
		readFilterFile(this.petActivatedFilterFile, this.petFilterMap);
		
		int j = 1;
		// Write the tile of the columns
		this.row = sheet1.createRow((short) 0);
		cell_11 = row.createCell((short) 0);
		cell_12 = row.createCell((short) 1);
		cell_13 = row.createCell((short) 2);
		cell_14 = row.createCell((short) 3);
		cell_15 = row.createCell((short) 4);
		cell_16 = row.createCell((short) 5);
		cell_17 = row.createCell((short) 6);
		cell_18 = row.createCell((short) 7);
		cell_19 = row.createCell((short) 8);
		cell_20 = row.createCell((short) 9);
		cell_21 = row.createCell((short) 10);

		// Style
		// HSSFWorkbook wblocal;
		HSSFCellStyle myStyle = this.wb.createCellStyle();

		myStyle.setFillForegroundColor(HSSFColor.BLUE.index);
		myStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		// myStyle.setFillForegroundColor(HSSFColor.WHITE.index);
		Font font = wb.createFont();
		font.setColor(HSSFColor.WHITE.index);
		myStyle.setFont(font);

		cell_11.setCellStyle(myStyle);
		cell_12.setCellStyle(myStyle);
		cell_13.setCellStyle(myStyle);
		cell_14.setCellStyle(myStyle);
		cell_15.setCellStyle(myStyle);
		cell_16.setCellStyle(myStyle);
		cell_17.setCellStyle(myStyle);
		cell_18.setCellStyle(myStyle);
		cell_19.setCellStyle(myStyle);
		cell_20.setCellStyle(myStyle);
		cell_21.setCellStyle(myStyle);

		cell_11.setCellValue("Patch ID");
		cell_12.setCellValue("Reference");
		cell_13.setCellValue("Status");
		cell_14.setCellValue("Group");
		//added
		cell_15.setCellValue("Subject");
		cell_16.setCellValue("Environment");
		cell_17.setCellValue("Synopsis");
		cell_18.setCellValue("Project Code");
		cell_19.setCellValue("Mr number");
		cell_20.setCellValue("PET activated");
		cell_21.setCellValue("PIF activated");
		

		log.info("The formatted file name: " + this.generatedFileName);
		
		for (ReportLine reportLine : this.reportLines) {
			this.row = sheet1.createRow((short) j);
			cell_11 = row.createCell((short) 0);
			cell_12 = row.createCell((short) 1);
			cell_13 = row.createCell((short) 2);
			cell_14 = row.createCell((short) 3);
			cell_15 = row.createCell((short) 4);
			cell_16 = row.createCell((short) 5);
			cell_17 = row.createCell((short) 6);
			cell_18 = row.createCell((short) 7);
			cell_19 = row.createCell((short) 8);
			cell_20 = row.createCell((short) 9);
			cell_21 = row.createCell((short) 10);
			

			cell_11.setCellValue(reportLine.getCrId());
			cell_12.setCellValue(reportLine.getPatchReference());
			cell_13.setCellValue(reportLine.getStatus());
			cell_14.setCellValue(reportLine.getGroup());
			
			cell_15.setCellValue(reportLine.getSubject());
			cell_16.setCellValue(reportLine.getEnvironment());
			
			
			log.info("refpat: refpat:" + reportLine.getPatchReference());
			List<Patch> patch = this.patchService.getPatchDescription(reportLine.getPatchReference());
			
			
			if (patch != null && patch.isEmpty() != true && patch.get(0) != null)
			   cell_17.setCellValue(patch.get(0).getSynopsis());
			else
				cell_17.setCellValue("Patch not found");
			cell_18.setCellValue(reportLine.getProjectCode());
			cell_19.setCellValue(reportLine.getMrNumber());
			//check if group is in filter file
			
			String group = reportLine.getGroup();
			log.info("Checking group: " + group);
			if (this.petFilterMap.get(group) != null){
				cell_20.setCellValue("PET-ACTIVATED");
				log.info("Group: " + group + "in PET" + this.petFilterMap.get(group));
			}else{
				cell_20.setCellValue("PET-NOT-ACTIVATED");
				log.info("Group: " + group + "not in PET" + this.petFilterMap.get(group));
			}
			
			
			if (this.pifFilterMap.get(group) != null){
				cell_21.setCellValue("PIF-ACTIVATED");
				log.info("Group: " + group + "in PIF" + this.pifFilterMap.get(group));
			}else{
				cell_21.setCellValue("PIF-NOT-ACTIVATED");
				log.info("Group: " + group + "not in PIF" + this.pifFilterMap.get(group));
			}
			
			
			j++;
		}
		wb.write(generatedFile);
	}

	public String getReportName() {
		return reportName;
	}

	public void setReportName(String reportName) {
		log.info("setting reportName: " + reportName);
		this.reportName = reportName;
	}

	public PatchService getPatchService() {
		return patchService;
	}

	public void setPatchService(PatchService patchService) {
		this.patchService = patchService;
	}
	
	

}