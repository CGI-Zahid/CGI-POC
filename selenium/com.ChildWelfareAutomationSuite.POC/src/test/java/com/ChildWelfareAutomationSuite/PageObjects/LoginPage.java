package com.ChildWelfareAutomationSuite.PageObjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.ChildWelfareAutomationSuite.factory.BrowserFactory;

public class LoginPage {

	WebDriver driver;

	public LoginPage(WebDriver ldriver) {
		this.driver = ldriver;
	}

	// Login Page Objects Properties-Object Repository
	@FindBy(name = "username")
	WebElement Username;
	@FindBy(name = "password")
	WebElement password;
	@FindBy(xpath = "//*[contains(text(), 'Log in')]")
	WebElement LoginButton;
	@FindBy(xpath = "//*[contains(text(),'Error Status')]")
	WebElement ErrorMesg;

	// Enter Login Details
	public void myAdtLoginEnterDetails(String username, String Password) {
		Username.sendKeys(username);
		password.sendKeys(Password);
		LoginButton.click();
		
	}

	// Verify Error message
	public String VerifyErrorMesg()

	{
		String errorMesg = ErrorMesg.getText();
		return errorMesg;

	}
	
	public boolean VerifyPasswordEncrpt()
	{
		WebElement input = driver.findElement(By.name("password"));
		boolean isEncrypted = input.getAttribute("type").equals("password");
		return isEncrypted;
	}

	// Get Application URL
	public String getApplicationTittle() {
		return driver.getTitle();
	}

}