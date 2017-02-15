package com.ChildWelfareAutomationSuite.factory;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.Reporter;



// Initializing Browser Class
public class BrowserFactory {

	static WebDriver driver;

	public static WebDriver getBrowser(String browserName) {

		// Invoke Firefox Browser
		if (browserName.equalsIgnoreCase("firefox")) {

			// System.setProperty("webdriver.gecko.driver",DataProviderFactory.getConfig().getFirefoxPath());
			driver = new FirefoxDriver();

		}

		// Invoke Chrome Browser
		else if (browserName.equalsIgnoreCase("chrome")) {

			//System.setProperty("webdriver.chrome.driver", DataProviderFactory.getConfig().getChromePath());
			driver = new ChromeDriver();
		}

		// Invoke IE Browser
		else if (browserName.equalsIgnoreCase("ie")) {

			//System.setProperty("webdriver.ie.driver", DataProviderFactory.getConfig().getIEPath());
			driver = new InternetExplorerDriver();
		}

		// Maximize the Window and wait for until the page load form 10 seconds
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
	
		return driver;
	}

	// Close the Browser
	public static void closeBrowser(WebDriver ldriver) {
		ldriver.quit();
	}
	
	
	public static void  isTextPresent(String textval,String reportmesg)
	{
		if (driver.getPageSource().contains(textval) == true) {
			Assert.assertTrue(true, reportmesg);
		} else {
			Assert.assertTrue(false, reportmesg);
		}
		
	}
	
	public static void  isTextNotPresent(String textval,String reportmesg)
	{
		if (driver.getPageSource().contains(textval) == true) {
			Assert.assertFalse(false, reportmesg);
		} else {
			Assert.assertFalse(true, reportmesg);
		}
		
	}
	
	public static void impliciteWait(int timeInsec) {
		Reporter.log("waiting for page to load...");
		try {
			driver.manage().timeouts().implicitlyWait(timeInsec, TimeUnit.SECONDS);
			Reporter.log("Page is loaded");
		} catch (Throwable error) {
			Reporter.log("Timeout for Page Load Request to complete after " + timeInsec + " seconds");
			Assert.assertTrue(false, "Timeout for page load request after " + timeInsec + " second");
		}
	}
	
	public static void switchToTab() {
		  //Switching between tabs using CTRL + tab keys.
		  driver.findElement(By.cssSelector("body")).sendKeys(Keys.CONTROL +"\t");
		  //Switch to current selected tab's content.
		  driver.switchTo().defaultContent(); 
		  
		 }
	
	public static void openNewTab()

	{
		 driver.findElement(By.cssSelector("body")).sendKeys(Keys.CONTROL +"t");
		 driver.get(DataProviderFactory.getConfig().getApplicationUrl());
		 //Call switchToTab() method to switch to 1st tab
		  switchToTab(); 

	}
	
	public static String waitFor()   {
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			return "Failed - unable to load the page";
		}
		return "Pass";
	}
}
