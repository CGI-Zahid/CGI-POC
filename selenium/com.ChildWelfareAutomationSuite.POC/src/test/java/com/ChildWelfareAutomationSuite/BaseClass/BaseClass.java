package com.ChildWelfareAutomationSuite.BaseClass;

import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import com.ChildWelfareAutomationSuite.Utilities.GenericFunctions;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;
import com.ChildWelfareAutomationSuite.factory.BrowserFactory;
import com.ChildWelfareAutomationSuite.factory.DataProviderFactory;

public abstract class BaseClass {
	public static WebDriver driver;
	public static ExtentReports report;
	public static ExtentTest logger;
	public static String className;

	@BeforeMethod
	public void setUp() {
		// Launch the specific Browser type
		driver = BrowserFactory.getBrowser("firefox");
		driver.get(DataProviderFactory.getConfig().getApplicationUrl());
		BrowserFactory.impliciteWait(25);

	}

	@BeforeClass
	// Create Run time HTML Report
	public void CreateHtmlReport() {
		className = this.getClass().getSimpleName();
		report = new ExtentReports("./Reports/" + className + ".html", true);

	}

	@AfterClass
	// Publish the HTML report
	public void PublishHtmlReport() {
		report.endTest(logger);
		report.flush();

	}

	@AfterMethod
	// Take the Screenshot for the failures and exit the Testcase & close Browser
	public void tearDown(ITestResult result) {
		try {
			if (result.getStatus() == ITestResult.FAILURE) {
				String path = GenericFunctions.CaptureScreenshot(driver, result.getName());
				System.out.println("Path of Screenshot" + path);
				logger.log(LogStatus.FAIL, logger.addScreenCapture(path));
			}

			BrowserFactory.closeBrowser(driver);

		} catch (Exception e) {
			System.out.println("\nLog Message::@AfterMethod: Exception caught" + e.getMessage());

		}
	}
}