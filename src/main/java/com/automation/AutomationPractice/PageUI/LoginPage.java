package com.automation.AutomationPractice.PageUI;

import static org.testng.Assert.assertTrue;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.Reporter;

import com.automation.AutomationPractice.BasePage.BasePage;
import com.aventstack.extentreports.Status;

public class LoginPage extends BasePage
{
	//Login Properties
	@FindBy(linkText="Sign in")WebElement signIn;
	@FindBy(id="email")WebElement email;
	@FindBy(id="passwd")WebElement password;
	@FindBy(id="SubmitLogin")WebElement submitLogin;
	@FindBy(xpath="//h1[text()='My account']")WebElement verifyAccount;
	@FindBy(xpath="//a[@title='Return to Home']")public WebElement homeLink;
	
	
	public LoginPage(WebDriver driver) 
	{
		PageFactory.initElements(driver, this);
	}

	public void customerLogin(String email,String password)
	{
			try {
				signIn.click();
			    logger.log(Status.INFO, "Clicked on SignIn");
			    			    
			    //Enter Email ID
			    this.email.sendKeys(email);
			    logger.log(Status.INFO,"Entered Email Address : " + email);
			    
			    //Enter Password
			    this.password.sendKeys(password);
			    logger.log(Status.INFO, "Entered Password  : " + password);
			    
			    //Click on Submit Login
			    submitLogin.click();
			    logger.log(Status.INFO, "Clicked on SubmitButton ");
			    
			    //Very the Account 
			    verifyLoginPage();
			   
			} catch (Exception e) {
				
				e.printStackTrace();
			}
	}
	
	public boolean verifyLoginPage()
	{
		String actualText = verifyAccount.getText();
		logger.log(Status.INFO, "Verified Account details : " + actualText);
		String expectedText="My account";
		assertTrue(actualText.equalsIgnoreCase(expectedText));
		return false;
	}
	
}


