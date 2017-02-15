package com.ChildWelfareAutomationSuite.Utilities;

import java.io.File;
import java.io.IOException;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import org.testng.Reporter;



public class GenericFunctions {

	public static String CaptureScreenshot(WebDriver driver, String Screenshotname) {
		TakesScreenshot ts = (TakesScreenshot) driver;
		File src = ts.getScreenshotAs(OutputType.FILE);
		System.out.println("Source Path" + src);
		String destination = "../Screenshots/" + Screenshotname + ".PNG";

		try {
			FileUtils.copyFile(src, new File("./Screenshots/" + Screenshotname + ".PNG"));
		} catch (IOException e) {
			System.out.println("Failed to take Screenshot" + e.getMessage());

		}
		return destination;
	}
	
	
	
		
	}

