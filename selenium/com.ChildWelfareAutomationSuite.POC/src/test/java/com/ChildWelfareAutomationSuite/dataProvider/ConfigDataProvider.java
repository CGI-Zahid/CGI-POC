package com.ChildWelfareAutomationSuite.dataProvider;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

// Load & Read the Configuration File
public class ConfigDataProvider {
	Properties pro;

	public ConfigDataProvider() {
		File src = new File("./Configuration/config.properties");

		FileInputStream fis;
		try {
			fis = new FileInputStream(src);
			pro = new Properties();
			pro.load(fis);
		} catch (Exception e) {
			System.out.println("Exception is" + e.getMessage());
		}

	}

	// Get the IE Driver Path
	public String getIEPath() {
		String iepath = pro.getProperty("iepath");
		return iepath;
	}

	// Get the Firefox Path
	public String getFirefoxPath() {
		String firefoxpath = pro.getProperty("firefoxpath");
		return firefoxpath;
	}

	// Get the Chrome Path
	public String getChromePath() {
		String chromepath = pro.getProperty("chromepath");
		return chromepath;
	}

	// Get the Application URL
	public String getApplicationUrl() {
		String url = pro.getProperty("url");
		return url;
	}

}