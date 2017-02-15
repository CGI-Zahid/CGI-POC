package com.ChildWelfareAutomationSuite.factory;

import com.ChildWelfareAutomationSuite.dataProvider.ConfigDataProvider;
import com.ChildWelfareAutomationSuite.dataProvider.ExcelDataProvider;

public class DataProviderFactory {

	public static ConfigDataProvider getConfig() {
		ConfigDataProvider config = new ConfigDataProvider();
		return config;
	}

	public static ExcelDataProvider getExcel() {
		ExcelDataProvider excel = new ExcelDataProvider();
		return excel;
	}

	
	
}
