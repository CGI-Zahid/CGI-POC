package com.ChildWelfareAutomationSuite.Testcases;

import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.ChildWelfareAutomationSuite.BaseClass.BaseClass;
import com.ChildWelfareAutomationSuite.PageObjects.LoginPage;
import com.ChildWelfareAutomationSuite.Utilities.GenericFunctions;
import com.ChildWelfareAutomationSuite.factory.BrowserFactory;
import com.relevantcodes.extentreports.LogStatus;

public class TC02_disAllowLogin extends BaseClass {
	@Test(description = "Verify login error message with nonregister user")

	public void TC02_AllowLogin_1() {

		logger = report.startTest("TC01_VerifyErrorMessgNonRegisterUser");
		logger.log(LogStatus.INFO, "Application is up and running");
		LoginPage login = PageFactory.initElements(driver, LoginPage.class);
		// login.myAdtLoginEnterDetails(DataProviderFactory.getExcel().getDatawithSheetIndex(0,
		// 1, 0),DataProviderFactory.getExcel().getDatawithSheetIndex(0, 1, 1));
		login.myAdtLoginEnterDetails("sravan1.neppalli@cgi.com", "ddff444");
		BrowserFactory.waitFor();
		logger.log(LogStatus.INFO, "nonregister Login Details Entered");
		BrowserFactory.isTextNotPresent("Error Status", "Error Message is displayed");
		logger.log(LogStatus.PASS, "Error Message is displayed");
		logger.log(LogStatus.INFO,logger.addScreenCapture(GenericFunctions.CaptureScreenshot(driver, "VerifyErrorMesgwithNonRegisterUser")));

	}
	
	//DisAllow Login with Blank Username
	@Test(description = "Enter Blank Username & Verify the Error Mesg")

	public void TC02_AllowLogin_2() {

		logger = report.startTest("TC02_LoginWithBlankUsername");
		logger.log(LogStatus.INFO, "Application is up and running");
		LoginPage login = PageFactory.initElements(driver, LoginPage.class);
		// login.myAdtLoginEnterDetails(DataProviderFactory.getExcel().getDatawithSheetIndex(0,
		// 1, 0),DataProviderFactory.getExcel().getDatawithSheetIndex(0, 1, 1));
		login.myAdtLoginEnterDetails("", "ddff444");
		BrowserFactory.waitFor();
		logger.log(LogStatus.INFO, "Username with Blank Value is Entered");
		BrowserFactory.isTextNotPresent("Error Status", "Error Message is displayed");
		logger.log(LogStatus.PASS, "Error Message is displayed");
		logger.log(LogStatus.INFO,logger.addScreenCapture(GenericFunctions.CaptureScreenshot(driver, "LoginErrorMesgwithBlanUser")));

	}
	
	@Test(description = "Enter Blank Password & Verify the Error Mesg")

	public void TC02_AllowLogin_3() {

		logger = report.startTest("TC03_LoginWithBlankPassword");
		logger.log(LogStatus.INFO, "Application is up and running");
		LoginPage login = PageFactory.initElements(driver, LoginPage.class);
		// login.myAdtLoginEnterDetails(DataProviderFactory.getExcel().getDatawithSheetIndex(0,
		// 1, 0),DataProviderFactory.getExcel().getDatawithSheetIndex(0, 1, 1));
		login.myAdtLoginEnterDetails("sravan.neppalli@cgi.com", "");
		BrowserFactory.waitFor();
		logger.log(LogStatus.INFO, "Password with Blank Value is Entered");
		BrowserFactory.isTextNotPresent("Error Status", "Error Message is displayed");
		logger.log(LogStatus.PASS, "Error Message is displayed");
		logger.log(LogStatus.INFO,logger.addScreenCapture(GenericFunctions.CaptureScreenshot(driver, "LoginErrorMesgwithBlanPassword")));

	}
	
	@Test(description = "Enter Blank Username password & Verify the Error Mesg")

	public void TC02_AllowLogin_4() {

		logger = report.startTest("TC04_LoginWithBlankUsername");
		logger.log(LogStatus.INFO, "Application is up and running");
		LoginPage login = PageFactory.initElements(driver, LoginPage.class);
		// login.myAdtLoginEnterDetails(DataProviderFactory.getExcel().getDatawithSheetIndex(0,
		// 1, 0),DataProviderFactory.getExcel().getDatawithSheetIndex(0, 1, 1));
		login.myAdtLoginEnterDetails("", "");
		BrowserFactory.waitFor();
		logger.log(LogStatus.INFO, "Username and password with Blank Values are Entered");
		BrowserFactory.isTextNotPresent("Error Status", "Error Message is displayed");
		logger.log(LogStatus.PASS, "Error Message is displayed");
		logger.log(LogStatus.INFO,logger.addScreenCapture(GenericFunctions.CaptureScreenshot(driver, "LoginErrorMesgwithBlankUsernamePassword")));

	}

	@Test(description = "Verify Password encrypted")

	public void TC02_AllowLogin_5() {

		logger = report.startTest("TC05_VerifyPasswordEncrypt");
		logger.log(LogStatus.INFO, "Application is up and running");
		LoginPage login = PageFactory.initElements(driver, LoginPage.class);
		// login.myAdtLoginEnterDetails(DataProviderFactory.getExcel().getDatawithSheetIndex(0,
		// 1, 0),DataProviderFactory.getExcel().getDatawithSheetIndex(0, 1, 1));
		login.myAdtLoginEnterDetails("", "ddff444");
		BrowserFactory.waitFor();
		logger.log(LogStatus.INFO, "Password Encrypt");
		Assert.assertTrue(login.VerifyPasswordEncrpt());
		logger.log(LogStatus.PASS, "Password Encryption is displayed");
		logger.log(LogStatus.INFO,logger.addScreenCapture(GenericFunctions.CaptureScreenshot(driver, "Password Encryption")));

	}
	
	
}
