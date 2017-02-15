package com.ChildWelfareAutomationSuite.Testcases;

import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.ChildWelfareAutomationSuite.BaseClass.BaseClass;
import com.ChildWelfareAutomationSuite.PageObjects.LoginPage;
import com.ChildWelfareAutomationSuite.Utilities.GenericFunctions;
import com.ChildWelfareAutomationSuite.factory.BrowserFactory;
import com.ChildWelfareAutomationSuite.factory.DataProviderFactory;
import com.relevantcodes.extentreports.LogStatus;

public class TC01_LoginFlow extends BaseClass {
	@Test(description="Verify login success message with valid user")

	public void TC01_AllowLogin_1() {

		logger = report.startTest("TC01_VerifyLandingPageWithValidCredentials");
		logger.log(LogStatus.INFO, "Application is up and running");
		LoginPage login = PageFactory.initElements(driver, LoginPage.class);
		//login.myAdtLoginEnterDetails(DataProviderFactory.getExcel().getDatawithSheetIndex(0, 1, 0),DataProviderFactory.getExcel().getDatawithSheetIndex(0, 1, 1));
		login.myAdtLoginEnterDetails("sravan.neppalli@cgi.com","ddff444");
		BrowserFactory.waitFor();
		logger.log(LogStatus.INFO, "Valid Login Details Entered");
		BrowserFactory.isTextPresent("Success Status","Sucess Message is displayed");
		logger.log(LogStatus.PASS, "Sucess Message is displayed");
		logger.log(LogStatus.INFO, logger.addScreenCapture(GenericFunctions.CaptureScreenshot(driver, "LoginSuccessMessagewithValidCredentials")));
		
	}
}