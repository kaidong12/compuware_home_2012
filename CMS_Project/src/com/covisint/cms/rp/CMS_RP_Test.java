package com.covisint.cms.rp;

//import java.sql.Driver;
//import java.text.DateFormat;
//import java.text.SimpleDateFormat;
//import java.util.Date;
import java.util.Iterator;
import java.util.Map;

import main.java.com.compuware.gdo.framework.core.Browser;
import main.java.com.compuware.gdo.framework.core.Locators;
import main.java.com.compuware.gdo.framework.core.TestData;
//import main.java.com.compuware.gdo.framework.core.enums.BrowserType;
//import main.java.com.compuware.gdo.framework.core.Locators.LocatorType;
//import main.java.com.compuware.gdo.framework.core.exceptions.FrameworkException;
import main.java.com.compuware.gdo.framework.utilities.TestLink.importer;
import main.java.com.compuware.gdo.framework.utilities.logging.Log;
import main.java.com.compuware.gdo.framework.utilities.logging.LogData;

//import org.openqa.selenium.By;
//import org.openqa.selenium.Alert;
//import org.openqa.selenium.WebDriver;
//import org.apache.commons.lang.StringUtils;
//import org.openqa.selenium.remote.RemoteWebElement;

import org.testng.ITestContext;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

//import WrappedElement.Grid;
//import WrappedElement.ControlPanelMenu;
//import WrappedElement.TopMenuBar;
import WrappedElement.DataDriver;
import WrappedElement.Page;

//import email.Mailinator;
import com.covisint.cms.*;
import com.covisint.cms.generic.CMS_GENERIC_Lib;

import org.apache.commons.lang.StringUtils;

/**
 * 
 * TestCases : <br>
 * 1. CMS-6: Site _ Add Default Blank Site <br>
 * 2. CMS-98: Site _ Add default Site Template <br>
 * 3. CMS-101: Site Page _ Add default Page Template <br>
 * 4. CMS-72: Site _ Edit Details of Site <br>
 * 
 */

public class CMS_RP_Test extends CMSTestSuite {

	private CMS_RP_Lib			aut			= null;
	private DataDriver			sqliteData	= null;
	protected static LogData	tlLogData	= null;

	public CMS_RP_Test() {
	}

	// ================================== START: DATA PROVIDER SECTION ==================================

	@DataProvider(name = "Component_Unit_Test")
	public Iterator<Object[]> getDataProvidertest_Component_Unit_Test() throws Exception {
		Iterator<Object[]> res = null;
		try {
			res = testData.get(aut.concatTestCaseName("testCMS_1"));
		} catch (Exception e) {
			log.startTestExecution("testCMS_1");
			log.exception(e);
			log.endTestExecution();
			// Re-throw exception...
			throw e;
		}
		return res;
	}

	// ================================== END: DATA PROVIDER SECTION ==================================

	@BeforeClass(alwaysRun = true)
	public void beforeClass(ITestContext context) throws Exception {
		try {
			caseId = "BeforeClass(Remote Publishing)";
			log.startTestExecution(caseId);
			tlLogData = new LogData(log);

			// Retrieve the context and use it to start the browser object with the desired browser engine.
			this.context = context;

			// Instantiate Browser object needed by the test.
			log.comment("Instantiate Browser object needed by the test...");
			browser = new Browser(log, context);
			// browser = new Browser(log, context, BrowserType.FIREFOX);
			// browser = new Browser(log,context,BrowserType.INTERNET_EXPLORER);
			// browser = new Browser(log, context,BrowserType.CHROME);

			// Get the xml tags from the current context xml and the common testsuite xml.
			parameters = browser.getParameters();

			// Instantiate locator objects needed by the test methods
			locators = new Locators(parameters.get("CMS_RP_Locators"));

			// Instantiate testdata objects needed by the test methods
			testData = new TestData(parameters.get("CMS_RP_TestData"));

			// Instantiate the project framework.
			log.comment("Instantiate the project framework...");
			aut = new CMS_RP_Lib(log, browser);

			// Pass Common_Locators and Common_TestData to CMSWebApp
			aut.setCommonLocators(parameters.get("Common_Locators"));
			aut.setCommonData(parameters.get("Common_TestData"));

			// Change the default wait timeouts
			log.comment("Change the default wait timeouts");
			aut.setNumSecondsToWaitForPageToLoad(Integer.valueOf(parameters.get("NUMBER_OF_SECONDS_TO_WAIT_FOR_PAGE_TO_LOAD")));
			aut.setNumSecondsToWaitForElementPresent(Integer.valueOf(parameters.get("NUMBER_OF_SECONDS_TO_WAIT_FOR_ELEMENT_PRESENT")));
			aut.getBrowser().getSelenium().windowMaximize();

			// Open sqlite database connection
			System.setProperty("jdbc.drivers", parameters.get("jdbc.drivers"));
			sqliteData = new DataDriver(log, parameters.get("sqlite.connectionstr"));
			log.comment("Open sqlite database connection");
			sqliteData.openConnection();

			// Precondition
			setupPrecondition();

		} catch (Exception e) {

			log.exception(e);
			log.comment("Method Name", "No Locator", "Script did not complete successfully.", Log.FAIL, Log.MAJOR_ISSUE);
			// Re-throw exception...
			throw e;
		} finally {
			log.endTestExecution();
		}

	}

	// ================================== START: TEST CASE SECTION ==================================

	@Test(description = "Unit Test Entry", dataProvider = "Component_Unit_Test")
	public void Component_Unit_Test(Object[] testDataItem) throws Exception {
		try {
			caseId = "Component_Unit_Test";
			log.startTestExecution(caseId);

		} catch (Exception e) {
			log.exception(e);
			log.comment("Method Name", "No Locator", "Script did not complete successfully.", Log.FAIL, Log.MAJOR_ISSUE);
			throw e;
		} finally {
			log.endTestExecution();
		}
	}

	@Test(description = "CMS-81:Remote Publishing_Remote Live now via Site Pages")
	public void testCMS_81_Publish_via_Site_Pages() throws Exception {
		try {
			caseId = "CMS-81";
			log.startTestExecution(caseId);

			Map<String, String> pagedata = sqliteData.getDataProvider(aut.concatTestCaseName("testCMS_81"), DataDriver.datatable.page);
			aut.Add_Page_via_Control_Panel(pagedata);

			Map<String, String> publishdata = sqliteData.getDataProvider(aut.concatTestCaseName("testCMS_81"), DataDriver.datatable.publish);
			aut.Publish_to_Remote_Live_Now_via_Site_Pages(publishdata);

			aut.Verify_Published_Data(publishdata, sqliteData);

		} catch (Exception e) {
			log.exception(e);
			log.comment("Test Case ID", "No Locator", "Script did not complete successfully.", Log.FAIL, Log.MAJOR_ISSUE);
			throw e;
		} finally {
			log.endTestExecution();
		}
	}

	@Test(description = "CMS-86:Remote Publishing_Remote Live now via Staging menu")
	public void testCMS_86_Publish_via_Staging_Menu() throws Exception {
		try {
			caseId = "CMS-86";
			log.startTestExecution(caseId);

			Map<String, String> pagedata = sqliteData.getDataProvider(aut.concatTestCaseName("testCMS_86"), DataDriver.datatable.page);
			aut.Add_Page_via_Control_Panel(pagedata);

			Map<String, String> publishdata = sqliteData.getDataProvider(aut.concatTestCaseName("testCMS_86"), DataDriver.datatable.publish);
			aut.Publish_to_Remote_Live_Now_via_Staging_Menu(publishdata);

			aut.Verify_Published_Data(publishdata, sqliteData);

		} catch (Exception e) {
			log.exception(e);
			log.comment("Test Case ID", "No Locator", "Script did not complete successfully.", Log.FAIL, Log.MAJOR_ISSUE);
			throw e;
		} finally {
			log.endTestExecution();
		}
	}

	// @Test(description = "CMS-142:Remote Publishing_Pages_Change Link", dependsOnMethods = { "testCMS_81_Publish_via_Site_Pages",
	// "testCMS_86_Publish_via_Staging_Menu" })
	@Test(description = "CMS-142:Remote Publishing_Pages_Change Link")
	public void testCMS_142_Publish_Pages_Change_Link() throws Exception {
		try {
			caseId = "CMS-142";
			log.startTestExecution(caseId);

			Map<String, String> publishdata = sqliteData.getDataProvider(aut.concatTestCaseName("testCMS_142"), DataDriver.datatable.publish);
			aut.Publish_to_Remote_Live_Now_via_Staging_Menu(publishdata);

			aut.Verify_Published_Data(publishdata, sqliteData);

		} catch (Exception e) {
			log.exception(e);
			log.comment("Test Case ID", "No Locator", "Script did not complete successfully.", Log.FAIL, Log.MAJOR_ISSUE);
			throw e;
		} finally {
			log.endTestExecution();
		}
	}

	@Test(description = "CMS-93:Remote Publishing_Pages_Deleting Missing Page_LiveSiteWithOwnPages")
	public void testCMS_93_Publish_Pages_Delete_Missing_Pages() throws Exception {
		try {
			caseId = "CMS-93";
			log.startTestExecution(caseId);

			Map<String, String> pagedata = sqliteData.getDataProvider(aut.concatTestCaseName("testCMS_93"), DataDriver.datatable.page);
			aut.Add_Page_via_Control_Panel(pagedata);

			Map<String, String> publishdata = sqliteData.getDataProvider(aut.concatTestCaseName("testCMS_93"), DataDriver.datatable.publish);
			aut.Publish_to_Remote_Live_Now_via_Staging_Menu(publishdata);

			aut.Verify_Published_Data(publishdata, sqliteData);

		} catch (Exception e) {
			log.exception(e);
			log.comment("Test Case ID", "No Locator", "Script did not complete successfully.", Log.FAIL, Log.MAJOR_ISSUE);
			throw e;
		} finally {
			log.endTestExecution();
		}
	}

	@Test(description = "CMS-112:Remote Publishing_publish web content")
	public void testCMS_112_Publish_Web_Content() throws Exception {
		try {
			caseId = "CMS-112";
			log.startTestExecution(caseId);

			Map<String, String> webcontentdata = sqliteData.getDataProvider(aut.concatTestCaseName("testCMS_112"), DataDriver.datatable.webcontent);
			aut.Add_Web_Content_via_Control_Panel(webcontentdata);

			Map<String, String> publishdata = sqliteData.getDataProvider(aut.concatTestCaseName("testCMS_112"), DataDriver.datatable.publish);
			aut.Publish_to_Remote_Live_Now_via_Staging_Menu(publishdata);

			Map<String, String> webcontent_publish_data = sqliteData.getDataProvider(aut.concatTestCaseName("testCMS_112"),
					DataDriver.datatable.publish);
			aut.Verify_Published_Data(webcontent_publish_data, sqliteData);

		} catch (Exception e) {
			log.exception(e);
			log.comment("Test Case ID", "No Locator", "Script did not complete successfully.", Log.FAIL, Log.MAJOR_ISSUE);
			throw e;
		} finally {
			log.endTestExecution();
		}
	}

	@Test(description = "CMS-141:Remote Publishing_permission set on one page")
	public void testCMS_141_Publish_Pages_Permission_Check_One_page() throws Exception {
		try {
			caseId = "CMS-141";
			log.startTestExecution(caseId);

			Map<String, String> pagedata = sqliteData.getDataProvider(aut.concatTestCaseName("testCMS_141"), DataDriver.datatable.page);
			aut.Add_Page_via_Control_Panel(pagedata);
			aut.Set_Permissions_for_Site_Page_via_Control_Panel(pagedata);

			Map<String, String> publishdata = sqliteData.getDataProvider(aut.concatTestCaseName("testCMS_141"), DataDriver.datatable.publish);
			aut.Publish_to_Remote_Live_Now_via_Staging_Menu(publishdata);

			Map<String, String> page_publish_data = sqliteData.getDataProvider(aut.concatTestCaseName("testCMS_141"), DataDriver.datatable.page,
					DataDriver.datatable.publish);
			aut.Verify_Published_Data(page_publish_data, sqliteData);

		} catch (Exception e) {
			log.exception(e);
			log.comment("Test Case ID", "No Locator", "Script did not complete successfully.", Log.FAIL, Log.MAJOR_ISSUE);
			throw e;
		} finally {
			log.endTestExecution();
		}
	}

	@Test(description = "CMS-146:Remote Publishing_permission set on multiple pages")
	public void testCMS_146_Publish_Pages_Permission_Check_Multiple_pages() throws Exception {
		try {
			caseId = "CMS-146";
			log.startTestExecution(caseId);

			// Map<String, String> pagedata1 = sqliteData.getDataProvider(aut.concatTestCaseName("testCMS_146_1"), DataDriver.datatable.page);
			// aut.Add_Page_via_Control_Panel(pagedata1);
			// aut.Set_Permissions_for_Site_Page_via_Control_Panel(pagedata1);
			//
			// Map<String, String> pagedata2 = sqliteData.getDataProvider(aut.concatTestCaseName("testCMS_146_2"), DataDriver.datatable.page);
			// aut.Add_Page_via_Control_Panel(pagedata2);
			// aut.Set_Permissions_for_Site_Page_via_Control_Panel(pagedata2);
			//
			// Map<String, String> publishdata = sqliteData.getDataProvider(aut.concatTestCaseName("testCMS_146"), DataDriver.datatable.publish);
			// aut.Publish_to_Remote_Live_Now_via_Staging_Menu(publishdata);

			Map<String, String> publish_data = sqliteData.getDataProvider(aut.concatTestCaseName("testCMS_146"), DataDriver.datatable.publish);
			aut.Verify_Published_Data(publish_data, sqliteData);

		} catch (Exception e) {
			log.exception(e);
			log.comment("Test Case ID", "No Locator", "Script did not complete successfully.", Log.FAIL, Log.MAJOR_ISSUE);
			throw e;
		} finally {
			log.endTestExecution();
		}
	}

	// ================================== END: TEST CASE SECTION ==================================

	// -------------------------------- START PUBLIC METHOD SECTION ------------------------------

	public void setupPrecondition() throws Exception {
		try {
			log.comment("**********Setup Precondition Data**********");

			aut.loginCorePortal(CMSWebApp.UserRole.portal_admin);

			// Map<String, String> precondition_site_staging = sqliteData.getDataProvider(aut.concatTestCaseName("testRP_staging"),
			// DataDriver.datatable.site);
			// Map<String, String> precondition_site_live = sqliteData.getDataProvider(aut.concatTestCaseName("testRP_live"),
			// DataDriver.datatable.site);
			// if (precondition_site_staging.get("Deleted").equalsIgnoreCase("Yes")) {
			// aut.Add_Default_Blank_Site_and_Update_Database(precondition_site_staging, sqliteData);
			// }
			// if (precondition_site_live.get("Deleted").equalsIgnoreCase("Yes")) {
			// aut.Add_Default_Blank_Site_and_Update_Database(precondition_site_live, sqliteData);
			// }
			//
			// Map<String, String> site_staging_edit_data = sqliteData.getDataProvider(aut.concatTestCaseName("testRP_staging"),
			// DataDriver.datatable.site);
			// aut.Edit_Site_Staging_Remote_live(site_staging_edit_data);

			log.comment("**********Setup Precondition Data Complete Successfully**********");

		} catch (Exception e) {
			log.exception(e);
			log.comment("Test Case ID", "No Locator", "Script did not complete successfully.", Log.FAIL, Log.MAJOR_ISSUE);
			throw e;
		}
	}

	public void cleanupData() throws Exception {
		try {
			log.comment("**********Cleanup Precondition Data**********");

			Map<String, String> precondition_site_staging = sqliteData.getDataProvider(aut.concatTestCaseName("testRP_staging"),
					DataDriver.datatable.site);
			Map<String, String> precondition_site_live = sqliteData.getDataProvider(aut.concatTestCaseName("testRP_live"), DataDriver.datatable.site);
			if (precondition_site_staging.get("Deleted").equalsIgnoreCase("No")) {
				aut.Delete_Site_and_Update_Database(precondition_site_staging, sqliteData);
			}
			if (precondition_site_live.get("Deleted").equalsIgnoreCase("No")) {
				aut.Delete_Site_and_Update_Database(precondition_site_live, sqliteData);
			}

			log.comment("**********Cleanup Precondition Data Complete Successfully**********");

		} catch (Exception e) {
			log.exception(e);
			log.comment("Test Case ID", "No Locator", "Script did not complete successfully.", Log.FAIL, Log.MAJOR_ISSUE);
			throw e;
		}
	}

	// -------------------------------- END PUBLIC METHOD SECTION --------------------------------

	@AfterClass(alwaysRun = true)
	public void afterClass() throws Exception {

		try {

			log.startTestExecution("AfterClass(Remote Publishing)");

			// Clean Data
			// cleanupData();

			log.comment("Close sqlite database connection");
			sqliteData.closeConnection();

			log.comment("Close Browser!!");
			aut.getBrowser().getWebDriver().quit();

			log.comment("Done WITH TESTING!!");
			long time = System.currentTimeMillis();
			if ((parameters.get("ExportResult")).equals("YES")) {
				System.out.println("Export TestResults to TestLink...");
				log.comment("Export TestResults to TestLink...");
				tlLogData.GenerateMetrics(log.getLogFilePath(), "AnySuitName");
				String[] args1 = { "-devKey", parameters.get("devKey"), "-apiUrl", parameters.get("apiUrl"), "-projectName",
						parameters.get("projectName"), "-testplanName", parameters.get("testplanName"), "-buildName", parameters.get("buildName"),
						"-platformName", parameters.get("platformName") };
				vpnConnect("vpndisconnect");
				aut.waitFor(aut.getNumSecondsToWaitForPageToLoad());
				importer.testLinkImporter(args1, tlLogData.getTestCaseResults());
				log.comment("Export Done");
				System.out.println(System.currentTimeMillis() - time);
				Thread.sleep(50000);
				vpnConnect("vpnconnect");

			} else {
				log.comment("Export TestResults to TestLink was not enabled");
			}

		} catch (Exception e) {
			log.exception(e);
			log.comment("Test Case ID", "No Locator", "Script did not complete successfully.", Log.FAIL, Log.MAJOR_ISSUE);
		} finally {
			log.endTestExecution();
		}
	}
}
