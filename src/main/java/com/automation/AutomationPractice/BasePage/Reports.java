package com.automation.AutomationPractice.BasePage;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;


public class Reports {
	public static ExtentHtmlReporter htmlreport;
	public static ExtentReports report;
	public static ExtentTest logger;
	public static WebDriver driver = null;
	protected String timeStamp;
	String extentReportPath;

	@BeforeSuite
	public void startReport(){
		
		timeStamp = new SimpleDateFormat("dd-MMM-yy  hh.mm.ss aa").format(Calendar.getInstance().getTime());
		extentReportPath = System.getProperty("user.dir") +"//extentReports//"+timeStamp+".html";
		htmlreport = new ExtentHtmlReporter(extentReportPath);
		htmlreport.loadXMLConfig(new File(System.getProperty("user.dir")+"\\extent-config.xml"));
		report = new ExtentReports();
		report.setSystemInfo("Operating System", "Window_10");
		report.attachReporter(htmlreport);
	}
	
	

	@AfterSuite
	public void endReport(){
		report.flush();
	}
	
	
}
