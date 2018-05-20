package com.automation.AutomationPractice.BasePage;


import java.io.File;
import java.io.FileInputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.Random;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;

import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;


public class BasePage extends Reports {
	
	public static final String configPath="./config.properties";
		
	public static String getValue(String key) throws Exception
	{		
		File file=new File(configPath);
		FileInputStream fis=new FileInputStream(file);
		Properties prop=new Properties();
		prop.load(fis);
		return prop.getProperty(key);
	}
	
	
	public static void browserLaunch(String browser,String url)
	{		
		if(browser.equalsIgnoreCase("CHROME"))
		{
			System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir")+ "//Drivers//chromedriver.exe");
			driver=new ChromeDriver();
			logger.log(Status.INFO, "Firefox has been launched");
		}
		else if(browser.equalsIgnoreCase("FIREFOX"))
		{
			System.setProperty("webdriver.gecko.driver", System.getProperty("user.dir")+ "//Drivers//geckodriver.exe");
			driver=new FirefoxDriver();
		}
		else if(browser.equalsIgnoreCase("IE"))
		{
			System.setProperty("webdriver.ie.driver", System.getProperty("user.dir")+ "//Drivers//IEDriverServer.exe");
			driver=new FirefoxDriver();
		}
		
		driver.manage().window().maximize();
		driver.manage().deleteAllCookies();
		driver.get(url);	
	}
	
	
	public static int randomNumber()
	{
		Random r=new Random();
		int random = r.nextInt(9999);
		return random;
	}
	
	
	public static int myRandomNo(int x)
	{		
		double b = Math.random()*x;
		System.out.println(b);
		int c=(int)b;
		return c;
	}
	
	
	public void elementVisible(int time,WebElement element)
	{
		WebDriverWait wait=new WebDriverWait(driver, time);
		wait.until(ExpectedConditions.visibilityOf(element));
	}
	
	
	public void waitForElement(int timeOutInSeconds, WebElement element,String condition) 
	{
			
		WebDriverWait wait = new WebDriverWait(driver, timeOutInSeconds);
		
		if(condition=="visible")
		{ 
		   wait.until(ExpectedConditions.visibilityOf(element));
		}
		else if(condition=="clikable")
		{
		    wait.until(ExpectedConditions.elementToBeClickable(element));
		}
	}
		
	public static boolean WaitForJStoLoad() {
		boolean jsLoad = false;
		try {
			for (int i = 0; i < 180; i++) {
				JavascriptExecutor executor = (JavascriptExecutor) driver;
				jsLoad = (Boolean) executor.executeScript("return jQuery.active == 0");
				Thread.sleep(1000);
				if (jsLoad)
					break;
			}
			if (!jsLoad) {
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsLoad;
	}
	
	
	public void selectOption(WebElement element,String option)
	{
		Select sel=new Select(element);
		sel.selectByVisibleText(option);
	}
	
	
	public static void MouseOver(WebElement element)
	{
		Actions actObj=new Actions(driver);
		actObj.moveToElement(element).build().perform();
	}
	
	
	public void switchToFrame(WebElement homePageIframe)
	{
		driver.switchTo().frame(homePageIframe);
	}

	
	public void switchToDefaultContent()
	{
		driver.switchTo().defaultContent();
	}
	
	public static void domClick(WebElement element){
	
		JavascriptExecutor executor = (JavascriptExecutor)driver;
		executor.executeScript("arguments[0].click();", element); 
	}
	
	@BeforeClass
	@Parameters("browser")
	public void startProcess(String browser) throws Exception {
		logger = report.createTest("Launch Application");
		browserLaunch(browser, getValue("baseprodurl"));
	}
	
	@AfterMethod
	public void tearDown(ITestResult result) {

		try {
			if(result.getStatus() == ITestResult.SUCCESS){
				
				logger.log(Status.PASS, "Test Method : "+result.getName()+ "is Passed");
				
			}else if (result.getStatus() == ITestResult.FAILURE) {

				logger.fail(MarkupHelper.createLabel(result.getName() + " Test Method Failed", ExtentColor.RED));
				logger.fail(result.getThrowable());
				DateFormat df = new SimpleDateFormat("dd-MMMM-yyyy");
				Date dateobj = new Date();
				String date = df.format(dateobj);
				DateFormat df1 = new SimpleDateFormat("hh-mm-ss");
				Date dateobj1 = new Date();
				String time = df1.format(dateobj1);
				// result.getName() will return name of test case so that
				// screenshot name will be same as test case name
				String screenlocation = System.getProperty("user.dir") + "/Screenshots/" + result.getName() + "_"
						+ date + "_" + time + ".png";	
				TakesScreenshot screenshot = (TakesScreenshot) driver;
				File src = screenshot.getScreenshotAs(OutputType.FILE);				
				FileUtils.copyFile(src, new File(screenlocation));
				logger.fail("Failed Test Method Screen is :  "+result.getName()+ logger.addScreenCaptureFromPath(screenlocation));
			} else if (result.getStatus() == ITestResult.SKIP) {

				logger.log(Status.SKIP, "Test Method : " + result.getName()+ "is Skipped");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@AfterClass
	public void endProcess() {
		driver.close();
	}
	
	

}
