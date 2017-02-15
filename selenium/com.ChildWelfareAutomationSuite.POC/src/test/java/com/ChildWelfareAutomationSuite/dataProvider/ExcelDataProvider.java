package com.ChildWelfareAutomationSuite.dataProvider;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

//Create the Object of Excel File
public class ExcelDataProvider

{

	XSSFWorkbook wb;
	XSSFSheet ws;
	XSSFRow xrow;
	XSSFCell xcol;
public ExcelDataProvider() {
	File src = new File("./ApplicationTestData/AppData.xlsx");

	try {
		FileInputStream fis = new FileInputStream(src);
		wb = new XSSFWorkbook(fis);

	} catch (Exception e) {
		// Capture the Error message of Excel File Loading
		System.out.println("Exception" + e.getMessage());
	}
}

// Get the Cell Value with passing Sheet Index
public String getDatawithSheetIndex(int sheetIndex, int row, int column) {
	String data = wb.getSheetAt(sheetIndex).getRow(row).getCell(column).getStringCellValue();

	return data;
}

// Get the Numeric Cell Value
public String getNumericDatawithSheetIndex(int sheetIndex, int row, int column) {
	DataFormatter formatter = new DataFormatter();
	Cell cell = wb.getSheetAt(sheetIndex).getRow(row).getCell(column);
	String Numdata = formatter.formatCellValue(cell);
	return Numdata;
}

// Get the Sheet Name
public int getColCount(String SheetName) throws InvalidFormatException, IOException

{
	ws = wb.getSheet(SheetName);
	int iColcount = ws.getRow(0).getLastCellNum();
	return iColcount;
}

// Get the Row Count
public int getrowCount(int SheetIndex)

{
	int rCount = ws.getLastRowNum() - 1;
	return rCount;
}



public   ExcelDataProvider getSheetName(String sheetName) {
	wb.getSheet(sheetName);
	return this;
}

public XSSFCell getCell(String sheetName,int irow, int icol) {

	XSSFCell sCellval=ws.getRow(0).getCell(icol);
	// TODO Auto-generated method stub
	return sCellval;
	
}

}