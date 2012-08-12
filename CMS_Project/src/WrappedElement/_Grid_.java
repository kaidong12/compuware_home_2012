package WrappedElement;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.covisint.cms.generic.CMS_GENERIC_Lib;

public class _Grid_ {

	public int sectionHeaderIndex = 0;
	public int columnHeaderIndex = 1;
	public int firstRowIndex = 2;
	public CMS_GENERIC_Lib aut;

	public WebDriver driver;

	public String XPath;

	public List<WebElement> columnHeaders = null;
	public List<WebElement> dataRowsInCurrentPage = null;
	public ArrayList<String> columnHeadersNames = new ArrayList();

	public _Grid_(CMS_GENERIC_Lib aut, String gridXpath) {
		this.aut = aut;
		driver = aut.getBrowser().getWebDriver();
		this.XPath = gridXpath;
	}
	
	public static _Grid_ getGridBySectionName(CMS_GENERIC_Lib aut, String sectionName){
		String xpath = "//table[tbody/tr/td[@class='sectionHeader' and starts-with(.,'"+sectionName+"')]]";
		return new _Grid_(aut,xpath);
	}

	public _Grid_(CMS_GENERIC_Lib aut, String gridXpath, int columnHeaderIndex,
			int firstRowIndex) {
		this.aut = aut;
		driver = aut.getBrowser().getWebDriver();
		this.XPath = gridXpath;
		this.columnHeaderIndex = columnHeaderIndex;
		this.firstRowIndex = firstRowIndex;
	}

	public void readColumnHeaders() {
		if (columnHeaders == null) {
			int index = columnHeaderIndex + 1;
			String tdXPath = this.XPath + "/tbody/tr[" + index + "]/td";
			System.out.println(tdXPath);
			columnHeaders = driver.findElements(By.xpath(tdXPath));
			System.out.println("readColumnHeaders ok");
		}

	}

	public int getColumnHeaderCount() {
		readColumnHeaders();
		return columnHeaders.size();
	}

	public void readColumnName() {
		readColumnHeaders();
		if (columnHeadersNames.size()==0) {			
			for (WebElement e : columnHeaders) {
				String text= e.getText();
				columnHeadersNames.add(text);
				System.out.println(text);				
			}
			System.out.println("readColumnName ok");
		}
		
	}

	public boolean columnHeaderExists(String columnHeaderName) {
		readColumnName();
		for (String s : columnHeadersNames) {
			if (s.equals(columnHeaderName)) {
				return true;
			}
		}
		return false;
	}

	public void assertColumnHeaderExists(String columnHeaderName)
			throws Exception {

		try {
			org.testng.Assert.assertTrue(columnHeaderExists(columnHeaderName),
					columnHeaderName + " does not exist");
		} catch (AssertionError ae) {
			throw new Exception(ae.getMessage());
		}

	}
	
	public boolean columnHeadersMatch(String [] expectedColumnHeaders){
		readColumnName();
		if(expectedColumnHeaders.length!=columnHeadersNames.size()){
			
			System.out.println("column headers count does not match");
			return false;
		}
			
		
		for(int i =0 ;i<columnHeadersNames.size() ; i++){
			if(!ArrayUtils.contains(expectedColumnHeaders,columnHeadersNames.get(i))){
				
				return false;
			}
		}
		
		return true;
	}
	
	public List<WebElement> getDataRowsInCurrentPage(){
		String dataRowXPath = this.XPath + "//tbody/tr[td[starts-with(@class,'tableDataUnderscore')]]";
		
		System.out.println(dataRowXPath);
		dataRowsInCurrentPage = driver.findElements(By.xpath(dataRowXPath));
		return dataRowsInCurrentPage;
	}
	
	public int getDataRowsCount(){
		return getDataRowsInCurrentPage().size();
	}
	
	public WebElement findDataRow(int columnIndex,String cellText){
		List<WebElement> dataRows = getDataRowsInCurrentPage();
		for(int i = 0; i<dataRows.size();i++){
			WebElement row = dataRows.get(i);
			
//			String cellXPath = "//td[starts-with(@class,'tableDataUnderscore')]["+(columnIndex+1)+"]";
			System.out.println(row.getText());
			WebElement cell = row.findElements(By.tagName("td")).get(columnIndex);
			
			String actualCellText = cell.getText();
			System.out.println("actualCellText="+actualCellText);
			if(actualCellText.equals(cellText)){
				return row;
			}
		}
		return null;
	}
	
	public boolean nextPage(){
		int waittime= aut.getWaitTimeout();
		aut.setWaitTimeout(1);
		
		String dataRowXPath = this.XPath + "//td[@class='tableDataAlt']/a[@href='javascript:doNextPage()']";
		
		try{
			driver.findElement(By.xpath(dataRowXPath)).click();	
			aut.waitForPageToLoad();
			return true;
		}catch(Exception e){
			return false;
		}finally{
			aut.setWaitTimeout(waittime);
		}
		
	}
	
	public boolean PreviousPage(){
		int waittime= aut.getWaitTimeout();
		aut.setWaitTimeout(1);
		
		String dataRowXPath = this.XPath + "//td[@class='tableDataAlt']/a[@href='javascript:doPreviousPage();']";
		
		try{
			driver.findElement(By.xpath(dataRowXPath)).click();	
			aut.waitForPageToLoad();
			return true;
		}catch(Exception e){
			return false;
		}finally{
			aut.setWaitTimeout(waittime);
		}
		
	}
	
	
	public WebElement findDataRowInAllPages(int columnIndex,String cellText){
		WebElement  row = findDataRow(columnIndex,cellText);		
		if(row==null){
			while(nextPage()){
				row = findDataRow(columnIndex,cellText);
				if(row!=null){
					return row;
				}
			}			
			return null;
		}else{
			return row;
		}
	}
	

}
