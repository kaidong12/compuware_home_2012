package com.covisint.cms.generic;

//import java.sql.Driver;
//import java.text.DateFormat;
//import java.text.SimpleDateFormat;
//import java.util.Date;
import java.util.HashMap;
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
//import org.testng.ITestContext;
//import org.openqa.selenium.Alert;
//import org.openqa.selenium.WebDriver;
//import org.apache.commons.lang.StringUtils;
//import org.openqa.selenium.remote.RemoteWebElement;

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
import org.apache.commons.lang.StringUtils;

/**
 * 
 * TestCases : <br>
 * 1. CMS-6: Site_Add Default Blank Site <br>
 * 2. CMS-98: Site_Add default Site Template <br>
 * 3. CMS-101: Site Page_Add default Page Template <br>
 * 4. CMS-72: Site_Edit Details of Site <br>
 * 
 */

public class CMS_GENERIC_Test extends CMSTestSuite {

	private CMS_GENERIC_Lib		aut			= null;
	// String[] userInfoOrg = new String[10];
	// String[] userInfoDiv = new String[10];
	// String[] tempUserInfo = new String[10];
	// String[] fullname = null;
	// String mailAddress = "";

	protected static LogData	tlLogData	= null;

	// private ControlPanelMenu controlpanel = null;
	// private TopMenuBar toolbar = null;
	// private Page page = null;
	// private final String dbconnectionstr = "jdbc:sqlite:TestData/CMS.sqlite";
	private DataDriver			sqliteData	= null;

	public CMS_GENERIC_Test() {

	}

	// ================================== START: DATA PROVIDER SECTION ==================================

	// @DataProvider(name = "dpcommon")
	// public Iterator<Object[]> getDataProviderCommon() throws Exception {
	// Iterator<Object[]> res = null;
	// try {
	// res = testData.get(environment + "common");
	// } catch (Exception e) {
	// log.startTestExecution("common");
	// log.exception(e);
	// log.endTestExecution();
	// // Re-throw exception...
	// throw e;
	// }
	// return res;
	// }

	@DataProvider(name = "dptestCMS_3")
	public Iterator<Object[]> getDataProvidertestCMS_3() throws Exception {
		Iterator<Object[]> res = null;
		try {
			res = testData.get(aut.concatTestCaseName("testCMS_3"));
		} catch (Exception e) {
			log.startTestExecution("testCMS_3");
			log.exception(e);
			log.endTestExecution();
			// Re-throw exception...
			throw e;
		}
		return res;
	}

	@DataProvider(name = "dptestCMS_5")
	public Iterator<Object[]> getDataProvidertestCMS_5() throws Exception {
		Iterator<Object[]> res = null;
		try {
			res = testData.get(aut.concatTestCaseName("testCMS_5"));
		} catch (Exception e) {
			log.startTestExecution("testCMS_5");
			log.exception(e);
			log.endTestExecution();
			// Re-throw exception...
			throw e;
		}
		return res;
	}

	@DataProvider(name = "dptestCMS_6")
	public Iterator<Object[]> getDataProvidertestCMS_6() throws Exception {
		Iterator<Object[]> res = null;
		try {
			res = testData.get(aut.concatTestCaseName("testCMS_6"));
		} catch (Exception e) {
			log.startTestExecution("testCMS_6");
			log.exception(e);
			log.endTestExecution();
			// Re-throw exception...
			throw e;
		}
		return res;
	}

	@DataProvider(name = "dptestCMS_98")
	public Iterator<Object[]> getDataProvidertestCMS_98() throws Exception {
		Iterator<Object[]> res = null;
		try {
			res = testData.get(aut.concatTestCaseName("testCMS_98"));
		} catch (Exception e) {
			log.startTestExecution("testCMS_98");
			log.exception(e);
			log.endTestExecution();
			// Re-throw exception...
			throw e;
		}
		return res;
	}

	@DataProvider(name = "dptestCMS_74")
	public Iterator<Object[]> getDataProvidertestCMS_74() throws Exception {
		Iterator<Object[]> res = null;
		try {
			res = testData.get(aut.concatTestCaseName("testCMS_74"));
		} catch (Exception e) {
			log.startTestExecution("testCMS_74");
			log.exception(e);
			log.endTestExecution();
			// Re-throw exception...
			throw e;
		}
		return res;
	}

	@DataProvider(name = "dptestCMS_72")
	public Iterator<Object[]> getDataProvidertestCMS_72() throws Exception {
		Iterator<Object[]> res = null;
		try {
			res = testData.get(aut.concatTestCaseName("testCMS_72"));
		} catch (Exception e) {
			log.startTestExecution("testCMS_72");
			log.exception(e);
			log.endTestExecution();
			// Re-throw exception...
			throw e;
		}
		return res;
	}

	// ================================== END: DATA PROVIDER SECTION ==================================

	@BeforeClass(alwaysRun = true)
	public void beforeClass() throws Exception {
		try {
			caseId = "BeforeClass(Generic Module)";
			log.startTestExecution(caseId);
			tlLogData = new LogData(log);

			// Instantiate Browser object needed by the test.
			log.comment("Instantiate Browser object needed by the test...");
			browser = new Browser(log, context);
			// browser = new Browser(log, context, BrowserType.FIREFOX);
			// browser = new Browser(log,context,BrowserType.INTERNET_EXPLORER);
			// browser = new Browser(log, context,BrowserType.CHROME);

			// Get the xml tags from the current context xml and the common testsuite xml.
			parameters = browser.getParameters();

			// Instantiate locator objects needed by the test methods
			locators = new Locators(parameters.get("CMS_GENERIC_Locators"));

			// Instantiate testdata objects needed by the test methods
			testData = new TestData(parameters.get("CMS_GENERIC_TestData"));
			// Common_testData = new TestData(parameters.get("Common_TestData"));

			// Instantiate the project framework.
			log.comment("Instantiate the project framework...");
			aut = new CMS_GENERIC_Lib(log, browser, testData);

			// Pass Common_Locators and Common_TestData to CMSWebApp
			aut.setCommonLocators(parameters.get("Common_Locators"));
			aut.setCommonData(parameters.get("Common_TestData"));

			// Change the default wait timeouts
			log.comment("Change the default wait timeouts");
			aut.setNumSecondsToWaitForPageToLoad(Integer.valueOf(parameters.get("NUMBER_OF_SECONDS_TO_WAIT_FOR_PAGE_TO_LOAD")));
			aut.setNumSecondsToWaitForElementPresent(Integer.valueOf(parameters.get("NUMBER_OF_SECONDS_TO_WAIT_FOR_ELEMENT_PRESENT")));
			aut.getBrowser().getSelenium().windowMaximize();
			aut.setMailWaitTime(Integer.valueOf(parameters.get("NUMBER_OF_SECONDS_TO_WAIT_For_Mail")));

			// toolbar = new TopMenuBar(aut);
			// controlpanel = new ControlPanelMenu(aut);
			// page = Page.getPage(aut);
			// page = new Page(aut, log);

			// Open sqlite database connection
			System.setProperty("jdbc.drivers", parameters.get("jdbc.drivers"));
			sqliteData = new DataDriver(log, parameters.get("sqlite.connectionstr"));
			log.comment("Open sqlite database connection");
			sqliteData.openConnection();

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

	// @Test(description = "CMS-3: Login_Administrator via Login page", dataProvider = "dptestCMS_3")
	// public void testCMS_3_Login_as_Administrator(Object[] testDataItem) throws Exception {
	// try {
	// caseId = "CMS-3";
	// log.startTestExecution(caseId);
	// loginAsAdmin(testDataItem);
	// } catch (Exception e) {
	// log.exception(e);
	// log.comment("Test Case ID", "No Locator", "Script did not complete successfully.", Log.FAIL, Log.MAJOR_ISSUE);
	// throw e;
	// } finally {
	// log.endTestExecution();
	// }
	// }
	//
	// @Test(description = "CMS-5: Login_Power User via Login page", dataProvider = "dptestCMS_5")
	// public void testCMS_5_Login_as_Power_User(Object[] testDataItem) throws Exception {
	// try {
	// caseId = "CMS-5";
	// log.startTestExecution(caseId);
	// loginAsUser(testDataItem);
	// } catch (Exception e) {
	// log.exception(e);
	// log.comment("Test Case ID", "No Locator", "Script did not complete successfully.", Log.FAIL, Log.MAJOR_ISSUE);
	// throw e;
	// } finally {
	// log.endTestExecution();
	// }
	// }

	@Test(description = "Unit Test Entry")
	public void Component_Unit_Test(Object[] testDataItem) throws Exception {
		try {
			caseId = "Component_Unit_Test";
			// log.startTestExecution(caseId);

			// String testResultDir = System.getProperty("user.dir") + parameters.get("testResultsDir");
			// Runtime.getRuntime().exec("cmd for /r " + testResultDir + " %%a in (*.html) do start iexplore.exe %%a");

			// try {
			// // cmd /c "for /r C:\ceshi\ %a in (*.exe) do start %a"
			// // Runtime.getRuntime().exec("IEXPLORE.EXE   ");
			// String testResultDir = System.getProperty("user.dir") + parameters.get("testResultsDir");
			// Runtime.getRuntime().exec("cmd /c for /r " + testResultDir + "%a in (*.html) do start file%a");
			// } catch (IOException e) {
			//
			// }

			String testFilePath = log.getLogFilePath();
			System.out.println(testFilePath);
			// Runtime.getRuntime().exec("cmd /c start filename" + testFilePath);

			aut.getDynamicLocatorType("CMS.UnitTest.getDynamicLocatorType1", "xxxxx");
			aut.getDynamicLocatorType("CMS.UnitTest.getDynamicLocatorType2", "xxxxx", "yyyyy");
			aut.getDynamicLocatorType("CMS.UnitTest.getDynamicLocatorType3", "xxxxx", "yyyyy", "zzzzz");
			aut.getDynamicLocatorType("CMS.UnitTest.getDynamicLocatorType1", "xxxxx", "yyyyy", "zzzzz");

			log.startTestExecution("unitTest18");
			log.comment("unitTest18");
			log.endTestExecution();

			log.startTestExecution("unitTest18");
			log.comment("unitTest18");
			log.endTestExecution();

			log.startTestExecution("unitTest18");
			log.comment("unitTest18");
			log.endTestExecution();

			log.startTestExecution("unitTest1818");
			log.comment("unitTest1818");
			log.endTestExecution();

			log.startTestExecution("unitTest18");
			log.comment("unitTest18");
			log.endTestExecution();

			log.startTestExecution("unitTest18");
			log.comment("unitTest18");
			log.endTestExecution();

			Map<String, String> map = new HashMap<String, String>();
			map.put("str1", "String1");
			map.put("str2", "String2");
			map.put("str3", "String4");
			map.put("str4", "String4");
			map.put("str1", "String5");

			for (Object o : map.keySet()) {
				System.out.println("key=" + o + " value=" + map.get(o));
			}

			log.startTestExecution(caseId);
			aut.getTestDataPath("basic_document.txt");

		} catch (Exception e) {
			log.exception(e);
			log.comment("Method Name", "No Locator", "Script did not complete successfully.", Log.FAIL, Log.MAJOR_ISSUE);
			throw e;
		} finally {
			log.endTestExecution();
		}
	}

	@Test(description = "CMS-1: Login Core Portal as Portal Administrator via SSO")
	public void testCMS_1_Login_as_Portal_Administrator() throws Exception {
		try {
			caseId = "CMS-1";
			log.startTestExecution(caseId);

			aut.loginCorePortal(CMSWebApp.UserRole.portal_admin);

		} catch (Exception e) {
			log.exception(e);
			log.comment("Test Case ID", "No Locator", "Script did not complete successfully.", Log.FAIL, Log.MAJOR_ISSUE);
			throw e;
		} finally {
			log.endTestExecution();
		}
	}

	@Test(description = "CMS-2: Login Core Portal as Site Administrator via SSO")
	public void testCMS_2_Login_as_Site_Administrator() throws Exception {
		try {
			caseId = "CMS-2";
			log.startTestExecution(caseId);

			aut.loginCorePortal(CMSWebApp.UserRole.site_admin);

		} catch (Exception e) {
			log.exception(e);
			log.comment("Test Case ID", "No Locator", "Script did not complete successfully.", Log.FAIL, Log.MAJOR_ISSUE);
			throw e;
		} finally {
			log.endTestExecution();
		}
	}

	@Test(description = "CMS-3: Login Core Portal as Site Owner via SSO")
	public void testCMS_3_Login_as_Site_Owner() throws Exception {
		try {
			caseId = "CMS-3";
			log.startTestExecution(caseId);

			aut.loginCorePortal(CMSWebApp.UserRole.site_owner);

		} catch (Exception e) {
			log.exception(e);
			log.comment("Test Case ID", "No Locator", "Script did not complete successfully.", Log.FAIL, Log.MAJOR_ISSUE);
			throw e;
		} finally {
			log.endTestExecution();
		}
	}

	@Test(description = "CMS-4: Login Core Portal as Site Member via SSO")
	public void testCMS_4_Login_as_Site_Member() throws Exception {
		try {
			caseId = "CMS-5";
			log.startTestExecution(caseId);

			aut.loginCorePortal(CMSWebApp.UserRole.site_member);

		} catch (Exception e) {
			log.exception(e);
			log.comment("Test Case ID", "No Locator", "Script did not complete successfully.", Log.FAIL, Log.MAJOR_ISSUE);
			throw e;
		} finally {
			log.endTestExecution();
		}
	}

	@Test(description = "CMS-5: Login Core Portal as Normal User via SSO")
	public void testCMS_5_Login_as_Normal_User() throws Exception {
		try {
			caseId = "CMS-5";
			log.startTestExecution(caseId);

			aut.loginCorePortal(CMSWebApp.UserRole.normal_user);

		} catch (Exception e) {
			log.exception(e);
			log.comment("Test Case ID", "No Locator", "Script did not complete successfully.", Log.FAIL, Log.MAJOR_ISSUE);
			throw e;
		} finally {
			log.endTestExecution();
		}
	}

	@Test(description = "CMS-6: Site_Add Default Blank Site", dataProvider = "dptestCMS_6")
	public void testCMS_6_Add_Default_Blank_Site(Object[] testDataItem) throws Exception {
		try {
			caseId = "CMS-6";
			log.startTestExecution(caseId);

			String siteID = aut.Add_Default_Blank_Site(testDataItem);
			sqliteData.updateSiteID(testData.getTestDataValue(testDataItem, "SiteName"), siteID);

			// // aut.mouseOver(CMS_GENERIC_Locators.getLocator("CMS.Welcome.TopMenuBar.menu-button_GoTo"));
			// // aut.click(CMS_GENERIC_Locators.getLocator("CMS.Welcome.GoToMenu.menu-item_ControlPanel"));
			// // aut.waitForPageToLoad();
			// // aut.click(CMS_GENERIC_Locators.getLocator("CMS.ControlPanel.LeftSideMenu.menu-link_Sites"));
			//
			// page.goToPage("Control Panel");
			// page.openPage("portal", "Sites");
			//
			// aut.waitForPageToLoad();
			// // log.verifyStep(aut.waitForElementPresent(CMS_GENERIC_Locators.getLocator("CMS.ControlPanel.SitePage.pagetitle_Sites")),
			// // "verify wait for element present success", "pass", "");
			// aut.verifyElementExist(aut.getDynamicLocatorType(locator, "CMS.ControlPanel.SitePage.pagetitle", "%COLUMNHEADER%", "Sites"));
			// aut.click(locator.getLocator("CMS.ControlPanel.SitePage.menu-button_Add"));
			// aut.click(locator.getLocator("CMS.ControlPanel.AddSite.menu-link_BlankSite"));
			// aut.waitForPageToLoad();
			// // log.verifyStep(aut.waitForElementPresent(CMS_GENERIC_Locators.getLocator("CMS.ControlPanel.SitePage.sectiontitle_NewSites")),
			// // "verify wait for element present success", "pass", "");
			// aut.verifyElementExist(aut.getDynamicLocatorType(locator, "CMS.ControlPanel.SitePage.sectiontitle", "%COLUMNHEADER%", "New Site"),
			// caseId);
			// aut.setText(locator.getLocator("CMS.ControlPanel.SitePage.textbox_NewSite"), testData.getTestDataValue(testDataItem, "SiteName"));
			// aut.setText(locator.getLocator("CMS.ControlPanel.SitePage.textarea_Description"),
			// testData.getTestDataValue(testDataItem, "SiteDescription"));
			// aut.click(locator.getLocator("CMS.ControlPanel.RightSideMenu.menu-button_Save"));
			// aut.waitForPageToLoad();
			//
			// // Check messages on New Site page
			// aut.verifyElementExist(locator.getLocator("CMS.CorePortal.Messages.success"));
			// aut.verifyElementExist(
			// aut.getDynamicLocatorType(locator, "CMS.ControlPanel.SitePage.sectiontitle", "%COLUMNHEADER%",
			// testData.getTestDataValue(testDataItem, "SiteName")));
			// aut.verifyElementExist(
			// aut.getDynamicLocatorType(locator, "CMS.ControlPanel.LeftSideMenu.panel-title_SiteName", "%COLUMNHEADER%",
			// testData.getTestDataValue(testDataItem, "SiteName")));
			//
			// // Update Site ID to database
			// String siteIDstr = aut.getElementText(locator.getLocator("CMS.ControlPanel.SitePage.text_SiteID"), false);
			// // String siteID = StringUtils.substringBetween(siteIDstr, "Site ID ", "\n");
			// String siteID = StringUtils.substringAfter(siteIDstr, "Site ID ");
			// System.out.println(siteID);
			// sqliteData.updateSiteID(testData.getTestDataValue(testDataItem, "SiteName"), siteID);
			//
			// // Check messages on Sites table
			// aut.click(locator.getLocator("CMS.ControlPanel.SitePage.sectiontitle_Back"));
			// aut.waitForPageToLoad();
			// aut.click(locator.getLocator("CMS.ControlPanel.datatable.header-name"));
			// aut.waitForPageToLoad();
			// if (aut.isElementPresent(locator.getLocator("CMS.ControlPanel.datatable.header-name_asc"))) {
			// aut.click(locator.getLocator("CMS.ControlPanel.datatable.header-name_asc"));
			// aut.waitForPageToLoad();
			// }
			// aut.verifyElementExist(
			// aut.getDynamicLocatorType(locator, "CMS.ControlPanel.datatable.link-cell_name", "%COLUMNHEADER%",
			// testData.getTestDataValue(testDataItem, "SiteName")));
			//
			// // TODO datatable(Grid)
			// // Grid siteGrid = Grid.getGrid(aut, "Sites");
			// // log.verifyStep(siteGrid.columnHeadersMatch(new String[] { "",
			// // "ID", "Title", "Status", "Modified Date", "Display Date",
			// // "Author", "" }),
			// // "verify grid headers", "pass", "");
			// // siteGrid.findDataRowInAllPages(1,
			// // "USER_AUTO, USER_FOR_VIEW").findElement(By.tagName("A")).click();
			// // aut.waitForPageToLoad();

		} catch (Exception e) {
			log.exception(e);
			log.comment("Test Case ID", "No Locator", "Script did not complete successfully.", Log.FAIL, Log.MAJOR_ISSUE);
			throw e;
		} finally {
			log.endTestExecution();
		}
	}

	@Test(description = "CMS-98: Site_Add default Site Template", dataProvider = "dptestCMS_98")
	public void testCMS_98_Add_Default_Site_Template(Object[] testDataItem) throws Exception {
		try {
			caseId = "CMS-98";
			log.startTestExecution(caseId);

			aut.Add_Default_Site_Template(testDataItem);

			// page.goToPage("Control Panel");
			// page.openPage("portal", "Site Templates");
			// page.verifyPageTitle("Site Templates");
			//
			// aut.click(locator.getLocator("CMS.ControlPanel.SiteTemplate.link_Add"));
			// aut.waitForPageToLoad();
			//
			// page.verifysectionTitle("New Site Template");
			//
			// aut.setText(locator.getLocator("CMS.ControlPanel.SiteTemplate.textbox_NewTemplate"),
			// testData.getTestDataValue(testDataItem, "SiteTemplateName"));
			// aut.setText(locator.getLocator("CMS.ControlPanel.SiteTemplate.textarea_Description"),
			// testData.getTestDataValue(testDataItem, "SiteTemplateDescription"));
			// aut.click(locator.getLocator("CMS.ControlPanel.SiteTemplate.button_Save"));
			// aut.waitForPageToLoad();
			//
			// // Check messages on New Templates page
			// page.verifySuccessMessage(caseId);
			//
			// // Check messages on Sites table
			// aut.waitForPageToLoad();
			// if (aut.isElementPresent(locator.getLocator("CMS.ControlPanel.datatable.header-name_asc"))) {
			// aut.click(locator.getLocator("CMS.ControlPanel.datatable.header-name_asc"));
			// aut.waitForPageToLoad();
			// }
			// aut.verifyElementExist(
			// aut.getDynamicLocatorType(locator, "CMS.ControlPanel.datatable.link-cell_name", "%COLUMNHEADER%",
			// testData.getTestDataValue(testDataItem, "SiteTemplateName")));

		} catch (Exception e) {
			log.exception(e);
			log.comment("Test Case ID", "No Locator", "Script did not complete successfully.", Log.FAIL, Log.MAJOR_ISSUE);
			throw e;
		} finally {
			log.endTestExecution();
		}
	}

	@Test(description = "CMS-101: Site Page_Add default Page Template")
	public void testCMS_101_Add_Default_Page_Template() throws Exception {
		try {
			caseId = "CMS-101";
			log.startTestExecution(caseId);

			aut.Add_Default_Page_Template("testCMS_101");

			//
			// page.goToPage("Control Panel");
			// page.openPage("portal", "Page Templates");
			//
			// page.verifyPageTitle("Page Templates");
			// aut.click(locator.getLocator("CMS.ControlPanel.PageTemplate.link_Add"));
			// aut.waitForPageToLoad();
			// page.verifysectionTitle("New Page Template");
			//
			// aut.setText(locator.getLocator("CMS.ControlPanel.PageTemplate.textbox_NewTemplate"),
			// aut.getTestCaseData("testCMS_101", "PageTemplateName"));
			// aut.setText(locator.getLocator("CMS.ControlPanel.SiteTemplate.textarea_Description"), aut.getTestCaseData("testCMS_101", "Remarks"));
			// aut.click(locator.getLocator("CMS.ControlPanel.PageTemplate.button_Save"));
			// aut.waitForPageToLoad();
			//
			// // Check messages on New Templates page
			// page.verifySuccessMessage(caseId);
			//
			// // Check messages on Sites table
			// // page.goBack();
			// aut.waitForPageToLoad();
			// if (aut.isElementPresent(locator.getLocator("CMS.ControlPanel.datatable.header-name_asc"))) {
			// aut.click(locator.getLocator("CMS.ControlPanel.datatable.header-name_asc"));
			// aut.waitForPageToLoad();
			// }
			// aut.verifyElementExist(
			// aut.getDynamicLocatorType(locator, "CMS.ControlPanel.datatable.link-cell_name", "%COLUMNHEADER%",
			// aut.getTestCaseData("testCMS_101", "PageTemplateName")));

		} catch (Exception e) {
			log.exception(e);
			log.comment("Test Case ID", "No Locator", "Script did not complete successfully.", Log.FAIL, Log.MAJOR_ISSUE);
			throw e;
		} finally {
			log.endTestExecution();
		}
	}

	@Test(description = "CMS-72: Site_Edit Details of Site", dataProvider = "dptestCMS_72")
	public void testCMS_72_Edit_Details_of_Site(Object[] testDataItem) throws Exception {
		try {
			caseId = "CMS-72";
			log.startTestExecution(caseId);

			String siteID = aut.Add_Default_Blank_Site(testDataItem);
			aut.Edit_Details_of_Site("testCMS_72");
			sqliteData.updateSiteID(testData.getTestDataValue(testDataItem, "NewSiteName"), siteID);

			// page.goToPage("Control Panel");
			// page.openPage("portal", "Sites");
			//
			// aut.waitForPageToLoad();
			// aut.click(locator.getLocator("CMS.ControlPanel.datatable.header-name"));
			// aut.waitForPageToLoad();
			// if (aut.isElementPresent(locator.getLocator("CMS.ControlPanel.datatable.header-name_asc"))) {
			// aut.click(locator.getLocator("CMS.ControlPanel.datatable.header-name_asc"));
			// aut.waitForPageToLoad();
			// }
			// aut.verifyElementExist(
			// aut.getDynamicLocatorType(locator, "CMS.ControlPanel.datatable.link-cell_name", "%COLUMNHEADER%",
			// aut.getTestCaseData("testCMS_72", "SiteName")));
			// aut.click(aut.getDynamicLocatorType(locator, "CMS.ControlPanel.datatable.link-cell_name", "%COLUMNHEADER%",
			// aut.getTestCaseData("testCMS_72", "SiteName")));
			// aut.waitForPageToLoad();
			// page.verifyPageTitle("Site Settings");
			// page.verifysectionTitle(aut.getTestCaseData("testCMS_72", "SiteName"));
			// page.openFrame("Details");
			//
			// aut.setText(locator.getLocator("CMS.ControlPanel.SitePage.textbox_NewSite"), aut.getTestCaseData("testCMS_72", "NewSiteName"));
			// aut.setText(locator.getLocator("CMS.ControlPanel.SiteTemplate.textarea_Description"), aut.getTestCaseData("testCMS_72", "Remarks"));
			// page.clickButton("Save");
			// aut.waitForPageToLoad();
			// page.verifySuccessMessage(caseId);
			// page.goBack();
			// aut.verifyElementExist(
			// aut.getDynamicLocatorType(locator, "CMS.ControlPanel.datatable.link-cell_name", "%COLUMNHEADER%",
			// aut.getTestCaseData("testCMS_72", "NewSiteName")));
		} catch (Exception e) {
			log.exception(e);
			log.comment("Test Case ID", "No Locator", "Script did not complete successfully.", Log.FAIL, Log.MAJOR_ISSUE);
			throw e;
		} finally {
			log.endTestExecution();
		}
	}

	@Test(description = "CMS-74: Site_Edit Staging of Site_Remote live", dataProvider = "dptestCMS_74")
	public void testCMS_74_Edit_Site_to_Remote_live(Object[] testDataItem) throws Exception {
		try {
			caseId = "CMS-74";
			log.startTestExecution(caseId);

			// TODO Three kinds of data provider(testng + getTestCaseData + sqlite)
			String siteID = aut.Add_Default_Blank_Site(testDataItem);
			aut.Edit_Details_of_Site("testCMS_74");
			sqliteData.updateSiteID(testData.getTestDataValue(testDataItem, "NewSiteName"), siteID);

			// aut.Edit_Site_Staging_Remote_live("testCMS_74");
			Map<String, String> sitedata = sqliteData.getDataProvider(aut.concatTestCaseName("testCMS_74"), DataDriver.datatable.site);
			aut.Edit_Site_Staging_Remote_live(sitedata);

			// page.goToPage("Control Panel");
			// page.openPage("portal", "Sites");
			//
			// aut.waitForPageToLoad();
			// aut.click(locator.getLocator("CMS.ControlPanel.datatable.header-name"));
			// aut.waitForPageToLoad();
			// if (aut.isElementPresent(locator.getLocator("CMS.ControlPanel.datatable.header-name_asc"))) {
			// aut.click(locator.getLocator("CMS.ControlPanel.datatable.header-name_asc"));
			// aut.waitForPageToLoad();
			// }
			// aut.verifyElementExist(
			// aut.getDynamicLocatorType(locator, "CMS.ControlPanel.datatable.link-cell_name", "%COLUMNHEADER%",
			// aut.getTestCaseData("testCMS_74", "SiteName")));
			// aut.click(aut.getDynamicLocatorType(locator, "CMS.ControlPanel.datatable.link-cell_name", "%COLUMNHEADER%",
			// aut.getTestCaseData("testCMS_74", "SiteName")));
			// aut.waitForPageToLoad();
			// page.verifyPageTitle("Site Settings");
			// page.verifysectionTitle(aut.getTestCaseData("testCMS_74", "SiteName"));
			//
			// page.openFrame("Details");
			// aut.setText(locator.getLocator("CMS.ControlPanel.SitePage.textbox_NewSite"), aut.getTestCaseData("testCMS_74", "NewSiteName"));
			//
			// page.openFrame("Staging");
			// aut.click(locator.getLocator("CMS.ControlPanel.SitePage.radio-button_RemoteLive"));
			// aut.setText(locator.getLocator("CMS.ControlPanel.SitePage.textbox_RemoteIP"), aut.getTestCaseData("testCMS_74", "RemoteIP"));
			// aut.setText(locator.getLocator("CMS.ControlPanel.SitePage.textbox_Port"), aut.getTestCaseData("testCMS_74", "RemotePort"));
			// aut.setText(locator.getLocator("CMS.ControlPanel.SitePage.textbox_SiteID"), aut.getTestCaseData("testCMS_74", "RemoteID"));
			//
			// page.clickButton("Save");
			// aut.waitForPageToLoad();
			// page.verifySuccessMessage(caseId);
			// page.goBack();
			// aut.verifyElementExist(
			// aut.getDynamicLocatorType(locator, "CMS.ControlPanel.datatable.link-cell_name", "%COLUMNHEADER%",
			// aut.getTestCaseData("testCMS_74", "NewSiteName")));
		} catch (Exception e) {
			log.exception(e);
			log.comment("Test Case ID", "No Locator", "Script did not complete successfully.", Log.FAIL, Log.MAJOR_ISSUE);
			throw e;
		} finally {
			log.endTestExecution();
		}
	}

	@Test(description = "CMS-7 to CMS-9: Site Page_Add Blog/Content Display/Wiki page via Menu Bar")
	public void testCMS_7_to_9_Add_Page_Menu_Bar() throws Exception {

		for (int i = 7; i <= 9; i++) {
			try {
				caseId = "CMS-" + i;
				log.startTestExecution(caseId);

				aut.Add_Page_via_Menu_Bar("testCMS_" + i);

				// // ==========Do mouseover on "Add" button==========
				//
				// // aut.getBrowser().getSelenium().mouseMove(CMS_GENERIC_Locators.getLocator("CMS.Welcome.TopMenuBar.menu-button_Add").getValue());
				// // aut.click(CMS_GENERIC_Locators.getLocator("CMS.Welcome.TopMenuBar.menu-button_Add"));
				// // aut.waitFor(5);
				// // aut.getBrowser().getSelenium().mouseMove(CMS_GENERIC_Locators.getLocator("CMS.Welcome.TopMenuBar.menu-button_GoTo").getValue());
				// // aut.click(CMS_GENERIC_Locators.getLocator("CMS.Welcome.TopMenuBar.menu-button_GoTo"));
				//
				// // aut.waitFor(5);
				// // aut.mouseEmulator(CMS_GENERIC_Locators.getLocator("CMS.Welcome.TopMenuBar.menu-button_Add"), "MouseOver");
				// // aut.waitFor(5);
				// // aut.mouseEmulator(CMS_GENERIC_Locators.getLocator("CMS.Welcome.TopMenuBar.menu-button_Add"), "mousemove");
				// // aut.mouseEmulator(CMS_GENERIC_Locators.getLocator("CMS.Welcome.TopMenuBar.menu-button_Add"), "click");
				//
				// // aut.waitFor(5);s
				// // aut.mouseEmulator(CMS_GENERIC_Locators.getLocator("CMS.Welcome.TopMenuBar.menu-button_Add"), "mouseover");
				// // aut.forceMouseOver(CMS_GENERIC_Locators.getLocator("CMS.Welcome.TopMenuBar.menu-button_Add").getValue());
				//
				// // aut.getBrowser().getSelenium().mouseOver(CMS_GENERIC_Locators.getLocator("CMS.Welcome.TopMenuBar.menu-button_Add").getValue());
				// // aut.mouseOver(CMS_GENERIC_Locators.getLocator("CMS.Welcome.TopMenuBar.menu-button_Add"));
				//
				// // ==========Click "Page"==========
				//
				// // aut.waitFor(2);
				// // aut.click(CMS_GENERIC_Locators.getLocator("CMS.Welcome.AddMenu.menu-item_Page"));
				// // aut.sendEnterKeyToElement("//li[contains(@class,'first add-page')]/a[contains(.,'Page')]");
				//
				// // if (aut.isElementPresent(CMS_GENERIC_Locators.getLocator("CMS.Welcome.AddMenu.menu-item_Page_click"))) {
				// // aut.click(CMS_GENERIC_Locators.getLocator("CMS.Welcome.AddMenu.menu-item_Page_click"));
				// // } else {
				// // aut.click(CMS_GENERIC_Locators.getLocator("CMS.Welcome.AddMenu.menu-item_Page"));
				// // aut.sendEnterKeyToElement("//div[@class='aui-menu-content']/ul/li[@class='first add-page']/a[contains(.,'Page')]");
				// // }
				//
				// // aut.waitFor(2);
				// // aut.waitForPageToLoad();
				//
				// for (int i = 7; i <= 9; i++) {
				// page.addPortlet("Page");
				// aut.setText(locator.getLocator("CMS.ViewPage.NavigationBar.textbox_PageName"), aut.getTestCaseData("testCMS_" + i, "PageName"));
				// aut.click(aut.getDynamicLocatorType(locator, "CMS.ViewPage.NavigationBar.radio-button_template", "%COLUMNHEADER%",
				// aut.getTestCaseData("testCMS_" + i, "Template")));
				// aut.click(locator.getLocator("CMS.ViewPage.NavigationBar.button_save"));
				// aut.waitForPageToLoad();
				// // page.verifyPageName(aut.getTestCaseData("testCMS_7", "PageName"));
				// aut.verifyElementExist(
				// aut.getDynamicLocatorType(locator, "CMS.ViewPage.NavigationBar.text_PageName", "%COLUMNHEADER%",
				// aut.getTestCaseData("testCMS_" + i, "PageName")));
				// }

			} catch (Exception e) {
				log.exception(e);
				log.comment("Test Case ID", "No Locator", "Script did not complete successfully.", Log.FAIL, Log.MAJOR_ISSUE);
				throw e;
			} finally {
				log.endTestExecution();
			}
		}
	}

	@Test(description = "CMS-94: Site Page_Add Blank page via Menu Bar")
	public void testCMS_94_Add_Blank_Page_Menu_Bar() throws Exception {
		try {
			caseId = "CMS-94";
			log.startTestExecution(caseId);

			aut.Add_Blank_Page_via_Menu_Bar("testCMS_94");

			// page.addPortlet("Page");
			// aut.setText(locator.getLocator("CMS.ViewPage.NavigationBar.textbox_PageName"), aut.getTestCaseData("testCMS_94", "PageName"));
			// aut.click(locator.getLocator("CMS.ViewPage.NavigationBar.button_save"));
			// aut.waitFor(2);
			// // page.verifyPageName(aut.getTestCaseData("testCMS_94", "PageName"));
			// aut.verifyElementExist(
			// aut.getDynamicLocatorType(locator, "CMS.ViewPage.NavigationBar.text_PageName", "%COLUMNHEADER%",
			// aut.getTestCaseData("testCMS_94", "PageName")));

		} catch (Exception e) {
			log.exception(e);
			log.comment("Test Case ID", "No Locator", "Script did not complete successfully.", Log.FAIL, Log.MAJOR_ISSUE);
			throw e;
		} finally {
			log.endTestExecution();
		}
	}

	@Test(description = "CMS-10: Site Page_Delete Site page via Menu Bar")
	public void testCMS_10_Delete_Site_Page_Menu_Bar() throws Exception {
		try {
			caseId = "CMS-10";
			log.startTestExecution(caseId);
			// TODO defered: testCMS_10_Delete_Site_Page_Menu_Bar
			boolean deletePage = aut.Delete_Site_Page_via_Menu_Bar("testCMS_9");
			if (deletePage) {
				sqliteData.updateDeletedData(DataDriver.datatable.page, "TestCaseName", aut.concatTestCaseName("testCMS_9"));
			}
			// // TODO mouse over issue
			// // aut.mouseOver(aut.getDynamicLocatorType(CMS_GENERIC_Locators, "CMS.ViewPage.NavigationBar.text_PageName", "%COLUMNHEADER%",
			// // aut.getTestCaseData("testCMS_10", "PageName")));
			// // aut.getBrowser()
			// // .getSelenium()
			// // .mouseOver(
			// // aut.getDynamicLocatorType(CMS_GENERIC_Locators, "CMS.ViewPage.NavigationBar.text_PageName", "%COLUMNHEADER%",
			// // aut.getTestCaseData("testCMS_10", "PageName")).getValue());
			// // aut.getBrowser()
			// // .getSelenium()
			// // .mouseMove(
			// // aut.getDynamicLocatorType(CMS_GENERIC_Locators, "CMS.ViewPage.NavigationBar.text_PageName", "%COLUMNHEADER%",
			// // aut.getTestCaseData("testCMS_10", "PageName")).getValue());
			//
			// aut.getBrowser()
			// .getSelenium()
			// .mouseMove(
			// aut.getDynamicLocatorType(locators, "CMS.ViewPage.NavigationBar.tab_PageName", "%COLUMNHEADER%",
			// aut.getTestCaseData("testCMS_10", "PageName")).getValue());
			//
			// aut.click(locators.getLocator("CMS.ViewPage.NavigationBar.button_delete"));
			// aut.waitForPageToLoad();
			// page.click_Ok();
			// aut.verifyElementNotExist(
			// aut.getDynamicLocatorType(locators, "CMS.ViewPage.NavigationBar.text_PageName", "%COLUMNHEADER%",
			// aut.getTestCaseData("testCMS_10", "PageName")));
			//
			// // TODO update data base and mouseover....

		} catch (Exception e) {
			log.exception(e);
			log.comment("Test Case ID", "No Locator", "Script did not complete successfully.", Log.FAIL, Log.MAJOR_ISSUE);
			throw e;
		} finally {
			log.endTestExecution();
		}
	}

	@Test(description = "CMS-11 to CMS-13: Site Page_Add Blog page/Content Display Page/Wiki Page via Control Panel")
	public void testCMS_11_to_13_Add_Page_Control_Panel() throws Exception {

		for (int i = 11; i <= 13; i++) {

			try {
				caseId = "CMS-" + i;
				log.startTestExecution(caseId);

				aut.Add_Page_via_Control_Panel("testCMS_" + i);

				// page.goToPage("Control Panel");
				// page.openPage("content", "Site Pages");
				// // toolbar.goTo("Control Panel");
				// // controlpanel.openItemPage("content", "Site Pages");
				//
				// // aut.waitForPageToLoad();
				// page.verifyPageTitle("Site Pages");
				// // controlpanel.selectSite(aut.getTestCaseData("testCMS_74",
				// // "NewSiteName"));
				// page.setCurrentSite(aut.getTestCaseData("testCMS_74", "NewSiteName"));
				// page.verifysectionTitle(aut.getTestCaseData("testCMS_74", "NewSiteName"));
				//
				// for (int i = 11; i <= 13; i++) {
				// aut.click(locator.getLocator("CMS.ControlPanel.SitePages.button_AddPage"));
				// aut.waitForPageToLoad();
				// aut.verifyElementExist(locator.getLocator("CMS.ControlPanel.AddPageDialog.title_AddPage"));
				// aut.setText(locator.getLocator("CMS.ControlPanel.AddPageDialog.textbox_PageName"), aut.getTestCaseData("testCMS_" + i,
				// "PageName"));
				// aut.select(locator.getLocator("CMS.ControlPanel.AddPageDialog.select_Template"), aut.getTestCaseData("testCMS_" + i, "Template"));
				// aut.click(locator.getLocator("CMS.ControlPanel.AddPageDialog.button_Add"));
				// aut.waitForPageToLoad();
				// page.verifySuccessMessage(caseId);
				// aut.verifyElementExist(
				// aut.getDynamicLocatorType(locator, "CMS.ControlPanel.SitePages.text_PageName", "%COLUMNHEADER%",
				// aut.getTestCaseData("testCMS_" + i, "PageName")));
				// }

			} catch (Exception e) {
				log.exception(e);
				log.comment("Test Case ID", "No Locator", "Script did not complete successfully.", Log.FAIL, Log.MAJOR_ISSUE);
				throw e;
			} finally {
				log.endTestExecution();
			}
		}
	}

	@Test(description = "CMS-14 to CMS-18: Site Page_Add Portlet page/Panel page/Embedded page/URL page/Link to Page page  via Control Panel")
	public void testCMS_14_to_18_Add_Page_Control_Panel() throws Exception {

		for (int i = 14; i <= 18; i++) {

			try {
				caseId = "CMS-" + i;
				log.startTestExecution(caseId);

				aut.Add_Page_via_Control_Panel("testCMS_" + i);

				// page.goToPage("Control Panel");
				// page.openPage("content", "Site Pages");
				// page.verifyPageTitle("Site Pages");
				//
				// page.setCurrentSite(aut.getTestCaseData("testCMS_74", "NewSiteName"));
				// page.verifysectionTitle(aut.getTestCaseData("testCMS_74", "NewSiteName"));
				//
				// for (int i = 14; i <= 18; i++) {
				// aut.click(locator.getLocator("CMS.ControlPanel.SitePages.button_AddPage"));
				// aut.waitForPageToLoad();
				// aut.verifyElementExist(locator.getLocator("CMS.ControlPanel.AddPageDialog.title_AddPage"));
				// aut.setText(locator.getLocator("CMS.ControlPanel.AddPageDialog.textbox_PageName"),
				// aut.getTestCaseData("testCMS_" + i, "PageName"));
				// aut.select(locator.getLocator("CMS.ControlPanel.AddPageDialog.select_Type"), aut.getTestCaseData("testCMS_" + i, "Type"));
				// aut.click(locator.getLocator("CMS.ControlPanel.AddPageDialog.button_Add"));
				// aut.waitForPageToLoad();
				// page.verifySuccessMessage(caseId);
				// aut.verifyElementExist(
				// aut.getDynamicLocatorType(locator, "CMS.ControlPanel.SitePages.text_PageName", "%COLUMNHEADER%",
				// aut.getTestCaseData("testCMS_" + i, "PageName")));
				// }

			} catch (Exception e) {
				log.exception(e);
				log.comment("Test Case ID", "No Locator", "Script did not complete successfully.", Log.FAIL, Log.MAJOR_ISSUE);
				throw e;
			} finally {
				log.endTestExecution();
			}

		}
	}

	@Test(description = "CMS-143: Site Page_Edit Details of page_HTML Title")
	public void testCMS_143_Edit_HTML_Title_of_Page() throws Exception {
		try {
			caseId = "CMS-143";
			log.startTestExecution(caseId);

			aut.Edit_HTML_Title_of_Page("testCMS_14");

			// page.goToPage("Control Panel");
			// page.openPage("content", "Site Pages");
			// page.verifyPageTitle("Site Pages");
			//
			// page.setCurrentSite(aut.getTestCaseData("testCMS_74", "NewSiteName"));
			// page.verifysectionTitle(aut.getTestCaseData("testCMS_74", "NewSiteName"));
			//
			// aut.verifyElementExist(
			// aut.getDynamicLocatorType(locator, "CMS.ControlPanel.SitePages.text_PageName", "%COLUMNHEADER%",
			// aut.getTestCaseData("testCMS_14", "PageName")));
			// aut.click(aut.getDynamicLocatorType(locator, "CMS.ControlPanel.SitePages.text_PageName", "%COLUMNHEADER%",
			// aut.getTestCaseData("testCMS_14", "PageName")));
			// aut.waitForPageToLoad();
			//
			// page.openFrame("Details");
			// aut.setText(locator.getLocator("CMS.ControlPanel.SitePages.textbox_HTMLTitle"), aut.getTestCaseData("testCMS_143", "PageName"));
			// page.clickButton("Save");
			// aut.waitForPageToLoad();
			// page.verifySuccessMessage(caseId);

		} catch (Exception e) {
			log.exception(e);
			log.comment("Test Case ID", "No Locator", "Script did not complete successfully.", Log.FAIL, Log.MAJOR_ISSUE);
			throw e;
		} finally {
			log.endTestExecution();
		}
	}

	@Test(description = "CMS-21: Site Page_Add Child page")
	public void testCMS_21_Add_Child_Page() throws Exception {
		try {
			caseId = "CMS-21";
			log.startTestExecution(caseId);

			aut.Add_Child_Page("testCMS_21");

			// page.goToPage("Control Panel");
			// page.openPage("content", "Site Pages");
			// page.verifyPageTitle("Site Pages");
			//
			// page.setCurrentSite(aut.getTestCaseData("testCMS_74", "NewSiteName"));
			// page.verifysectionTitle(aut.getTestCaseData("testCMS_74", "NewSiteName"));
			//
			// aut.verifyElementExist(
			// aut.getDynamicLocatorType(locator, "CMS.ControlPanel.SitePages.text_PageName", "%COLUMNHEADER%",
			// aut.getTestCaseData("testCMS_14", "PageName")));
			// aut.click(aut.getDynamicLocatorType(locator, "CMS.ControlPanel.SitePages.text_PageName", "%COLUMNHEADER%",
			// aut.getTestCaseData("testCMS_14", "PageName")));
			// aut.waitForPageToLoad();
			//
			// aut.click(locator.getLocator("CMS.ControlPanel.SitePages.button_AddChildPage"));
			// aut.waitForPageToLoad();
			// aut.verifyElementExist(locator.getLocator("CMS.ControlPanel.AddPageDialog.title_AddPage"));
			// aut.setText(locator.getLocator("CMS.ControlPanel.AddPageDialog.textbox_PageName"), aut.getTestCaseData("testCMS_21", "PageName"));
			// aut.select(locator.getLocator("CMS.ControlPanel.AddPageDialog.select_Type"), aut.getTestCaseData("testCMS_21", "Type"));
			// aut.click(locator.getLocator("CMS.ControlPanel.AddPageDialog.button_Add"));
			// aut.waitForPageToLoad();
			// page.verifySuccessMessage(caseId);
			// aut.click(aut.getDynamicLocatorType(locator, "CMS.ControlPanel.SitePages.icon_treeNode", "%COLUMNHEADER%",
			// aut.getTestCaseData("testCMS_14", "PageName")));
			// aut.waitForPageToLoad();
			// aut.verifyElementExist(
			// aut.getDynamicLocatorType(locator, "CMS.ControlPanel.SitePages.text_ChildPageName", "%COLUMNHEADER%",
			// aut.getTestCaseData("testCMS_21", "PageName")));

		} catch (Exception e) {
			log.exception(e);
			log.comment("Test Case ID", "No Locator", "Script did not complete successfully.", Log.FAIL, Log.MAJOR_ISSUE);
			throw e;
		} finally {
			log.endTestExecution();
		}
	}

	// TODO Structure enhancement
	@Test(description = "CMS-22: Site Page_Add Private page")
	public void testCMS_22_Add_Private_Page() throws Exception {
		try {
			caseId = "CMS-22";
			log.startTestExecution(caseId);

			aut.Add_Private_Page("testCMS_22");

			// page.goToPage("Control Panel");
			// page.openPage("content", "Site Pages");
			// page.verifyPageTitle("Site Pages");
			//
			// page.setCurrentSite(aut.getTestCaseData("testCMS_74", "NewSiteName"));
			// page.verifysectionTitle(aut.getTestCaseData("testCMS_74", "NewSiteName"));
			//
			// aut.click(locator.getLocator("CMS.ControlPanel.SitePages.tabtitle_Private"));
			// aut.waitForPageToLoad();
			//
			// aut.click(locator.getLocator("CMS.ControlPanel.SitePages.button_AddPage"));
			// aut.waitForPageToLoad();
			// aut.verifyElementExist(locator.getLocator("CMS.ControlPanel.AddPageDialog.title_AddPage"));
			// aut.setText(locator.getLocator("CMS.ControlPanel.AddPageDialog.textbox_PageName"), aut.getTestCaseData("testCMS_22", "PageName"));
			// aut.click(locator.getLocator("CMS.ControlPanel.AddPageDialog.button_Add"));
			// aut.waitForPageToLoad();
			// page.verifySuccessMessage(caseId);
			// aut.verifyElementExist(
			// aut.getDynamicLocatorType(locator, "CMS.ControlPanel.SitePages.text_PageName", "%COLUMNHEADER%",
			// aut.getTestCaseData("testCMS_22", "PageName")));

		} catch (Exception e) {
			log.exception(e);
			log.comment("Test Case ID", "No Locator", "Script did not complete successfully.", Log.FAIL, Log.MAJOR_ISSUE);
			throw e;
		} finally {
			log.endTestExecution();
		}
	}

	@Test(description = "CMS-23: Site Page_Move page")
	public void testCMS_23_Move_Page() throws Exception {
		try {
			caseId = "CMS-23";
			log.startTestExecution(caseId);

			aut.Move_Page_Public_to_Private("testCMS_23");
			aut.Move_Page_Private_to_Public("testCMS_23");

			// page.goToPage("Control Panel");
			// page.openPage("content", "Site Pages");
			// page.verifyPageTitle("Site Pages");
			//
			// page.setCurrentSite(aut.getTestCaseData("testCMS_23", "SiteName"));
			// page.verifysectionTitle(aut.getTestCaseData("testCMS_23", "SiteName"));
			//
			// aut.click(locators.getLocator("CMS.ControlPanel.SitePages.tabtitle_Move"));
			// aut.waitForPageToLoad();
			//
			// // move from public page to private page
			// aut.click(aut.getDynamicLocatorType(locators, "CMS.ControlPanel.SitePages.select_publicpage", "%COLUMNHEADER%",
			// aut.getTestCaseData("testCMS_23", "PageName")));
			// aut.click(locators.getLocator("CMS.ControlPanel.SitePages.button_rightArrow"));
			// aut.click(locators.getLocator("CMS.ControlPanel.SitePages.button_Save"));
			// aut.waitForPageToLoad();
			// page.verifySuccessMessage(caseId);
			// aut.verifyElementExist(
			// aut.getDynamicLocatorType(locators, "CMS.ControlPanel.SitePages.select_privatepage", "%COLUMNHEADER%",
			// aut.getTestCaseData("testCMS_23", "PageName")));
			// aut.verifyElementNotExist(
			// aut.getDynamicLocatorType(locators, "CMS.ControlPanel.SitePages.select_publicpage", "%COLUMNHEADER%",
			// aut.getTestCaseData("testCMS_23", "PageName")));
			//
			// // move from private page to public page
			// aut.click(aut.getDynamicLocatorType(locators, "CMS.ControlPanel.SitePages.select_privatepage", "%COLUMNHEADER%",
			// aut.getTestCaseData("testCMS_23", "PageName")));
			// aut.click(locators.getLocator("CMS.ControlPanel.SitePages.button_leftArrow"));
			// aut.click(locators.getLocator("CMS.ControlPanel.SitePages.button_Save"));
			// aut.waitForPageToLoad();
			// page.verifySuccessMessage(caseId);
			// aut.verifyElementExist(
			// aut.getDynamicLocatorType(locators, "CMS.ControlPanel.SitePages.select_publicpage", "%COLUMNHEADER%",
			// aut.getTestCaseData("testCMS_23", "PageName")));
			// aut.verifyElementNotExist(
			// aut.getDynamicLocatorType(locators, "CMS.ControlPanel.SitePages.select_privatepage", "%COLUMNHEADER%",
			// aut.getTestCaseData("testCMS_23", "PageName")));
		} catch (Exception e) {
			log.exception(e);
			log.comment("Test Case ID", "No Locator", "Script did not complete successfully.", Log.FAIL, Log.MAJOR_ISSUE);
			throw e;
		} finally {
			log.endTestExecution();
		}
	}

	@Test(description = "CMS-25: Site Page_Export pages")
	public void testCMS_25_Export_Pages() throws Exception {
		try {
			caseId = "CMS-25";
			log.startTestExecution(caseId);
			aut.Export_Pages("testCMS_25");

			// page.goToPage("Control Panel");
			// page.openPage("content", "Site Pages");
			// page.verifyPageTitle("Site Pages");
			//
			// page.setCurrentSite(aut.getTestCaseData("testCMS_74", "NewSiteName"));
			// page.verifysectionTitle(aut.getTestCaseData("testCMS_74", "NewSiteName"));
			//
			// aut.click(locator.getLocator("CMS.ControlPanel.SitePages.button_ExportPage"));
			// aut.waitForPageToLoad();
			// aut.verifyElementExist(locator.getLocator("CMS.ControlPanel.ExportDialog.title_Export"));
			// aut.setText(locator.getLocator("CMS.ControlPanel.ExportDialog.textbox_PackageName"), aut.getTestCaseData("testCMS_25", "PackageName"));
			// aut.click(locator.getLocator("CMS.ControlPanel.ExportDialog.button_Export"));
			//
			// // TODO Export pages with autoit3 or UI Spy
			//
			// aut.click(locator.getLocator("CMS.ControlPanel.ExportDialog.button_Close"));
			//
			// // TODO Check result

		} catch (Exception e) {
			log.exception(e);
			log.comment("Test Case ID", "No Locator", "Script did not complete successfully.", Log.FAIL, Log.MAJOR_ISSUE);
			throw e;
		} finally {
			log.endTestExecution();
		}
	}

	@Test(description = "CMS-26: Site Page_Import pages")
	public void testCMS_26_Import_Pages() throws Exception {
		try {
			caseId = "CMS-26";
			log.startTestExecution(caseId);
			aut.Import_Pages("testCMS_26");

			// page.goToPage("Control Panel");
			// page.openPage("content", "Site Pages");
			// page.verifyPageTitle("Site Pages");
			//
			// page.setCurrentSite(aut.getTestCaseData("testCMS_74", "NewSiteName"));
			// page.verifysectionTitle(aut.getTestCaseData("testCMS_74", "NewSiteName"));
			//
			// aut.click(locator.getLocator("CMS.ControlPanel.SitePages.button_ImportPage"));
			// aut.waitForPageToLoad();
			// aut.verifyElementExist(locator.getLocator("CMS.ControlPanel.ImportDialog.title_Import"));
			// aut.setText(locator.getLocator("CMS.ControlPanel.ImportDialog.textbox_PackageName"), aut.getTestCaseData("testCMS_26", "PackageName"));
			// aut.click(locator.getLocator("CMS.ControlPanel.ImportDialog.button_Import"));
			//
			// // TODO Import pages with autoit3 or UI Spy
			//
			// aut.click(locator.getLocator("CMS.ControlPanel.ExportDialog.button_Close"));
			//
			// // TODO Check result

		} catch (Exception e) {
			log.exception(e);
			log.comment("Test Case ID", "No Locator", "Script did not complete successfully.", Log.FAIL, Log.MAJOR_ISSUE);
			throw e;
		} finally {
			log.endTestExecution();
		}
	}

	@Test(description = "CMS-107: Site Page_Delete page via Site Pages(Live)")
	public void testCMS_107_Delete_Site_Page_via_Site_Pages_live() throws Exception {
		try {
			caseId = "CMS-107";
			log.startTestExecution(caseId);
			Map<String, String> sitepagedata = sqliteData.getDataProvider(aut.concatTestCaseName("testCMS_11"), DataDriver.datatable.page);
			boolean deletePage = aut.Delete_Site_Page_via_Site_Pages(sitepagedata);
			if (deletePage) {
				sqliteData.updateDeletedData(DataDriver.datatable.page, "TestCaseName", aut.concatTestCaseName("testCMS_11"));
			}
		} catch (Exception e) {
			log.exception(e);
			log.comment("Test Case ID", "No Locator", "Script did not complete successfully.", Log.FAIL, Log.MAJOR_ISSUE);
			throw e;
		} finally {
			log.endTestExecution();
		}
	}

	@Test(description = "CMS-108: Site Page_Delete page via Manage Page(Live)")
	public void testCMS_108_Delete_Site_Page_via_Manager_Page_live() throws Exception {
		try {
			caseId = "CMS-108";
			log.startTestExecution(caseId);
			Map<String, String> sitepagedata = sqliteData.getDataProvider(aut.concatTestCaseName("testCMS_13"), DataDriver.datatable.page);
			boolean deletePage = aut.Delete_Site_Page_via_Manage_Page(sitepagedata);
			if (deletePage) {
				sqliteData.updateDeletedData(DataDriver.datatable.page, "TestCaseName", aut.concatTestCaseName("testCMS_13"));
			}

		} catch (Exception e) {
			log.exception(e);
			log.comment("Test Case ID", "No Locator", "Script did not complete successfully.", Log.FAIL, Log.MAJOR_ISSUE);
			throw e;
		} finally {
			log.endTestExecution();
		}
	}

	@Test(description = "CMS-109:Site Page_Delete page via Manager Site Pages(Live)")
	public void testCMS_109_Delete_Site_Page_via_Manager_Site_Pages_live() throws Exception {
		try {
			caseId = "CMS-109";
			log.startTestExecution(caseId);
			Map<String, String> sitepagedata = sqliteData.getDataProvider(aut.concatTestCaseName("testCMS_12"), DataDriver.datatable.page);
			boolean deletePage = aut.Delete_Site_Page_via_Manage_Site_Pages(sitepagedata);
			if (deletePage) {
				sqliteData.updateDeletedData(DataDriver.datatable.page, "TestCaseName", aut.concatTestCaseName("testCMS_12"));
			}

		} catch (Exception e) {
			log.exception(e);
			log.comment("Test Case ID", "No Locator", "Script did not complete successfully.", Log.FAIL, Log.MAJOR_ISSUE);
			throw e;
		} finally {
			log.endTestExecution();
		}
	}

	@Test(description = "CMS-27: Site Page_Delete page via Site Pages(Staging)")
	public void testCMS_27_Delete_Site_Page_via_Site_Pages_Staging() throws Exception {
		try {
			caseId = "CMS-27";
			log.startTestExecution(caseId);
			Map<String, String> sitepagedata = sqliteData.getDataProvider(aut.concatTestCaseName("testCMS_7"), DataDriver.datatable.page);
			boolean deletePage = aut.Delete_Site_Page_via_Site_Pages(sitepagedata);
			if (deletePage) {
				sqliteData.updateDeletedData(DataDriver.datatable.page, "TestCaseName", aut.concatTestCaseName("testCMS_7"));
			}
		} catch (Exception e) {
			log.exception(e);
			log.comment("Test Case ID", "No Locator", "Script did not complete successfully.", Log.FAIL, Log.MAJOR_ISSUE);
			throw e;
		} finally {
			log.endTestExecution();
		}
	}

	@Test(description = "CMS-91:Site Page_Delete page via Manager Page(Staging)")
	public void testCMS_91_Delete_Site_Page_via_Manager_Page_Staging() throws Exception {
		try {
			caseId = "CMS-91";
			log.startTestExecution(caseId);
			Map<String, String> sitepagedata = sqliteData.getDataProvider(aut.concatTestCaseName("testCMS_8"), DataDriver.datatable.page);
			boolean deletePage = aut.Delete_Site_Page_via_Manage_Page(sitepagedata);
			if (deletePage) {
				sqliteData.updateDeletedData(DataDriver.datatable.page, "TestCaseName", aut.concatTestCaseName("testCMS_8"));
			}

		} catch (Exception e) {
			log.exception(e);
			log.comment("Test Case ID", "No Locator", "Script did not complete successfully.", Log.FAIL, Log.MAJOR_ISSUE);
			throw e;
		} finally {
			log.endTestExecution();
		}
	}

	@Test(description = "CMS-92:Site Page_Delete page via Manager Site Pages(Staging)")
	public void testCMS_92_Delete_Site_Page_via_Manager_Site_Pages_Staging() throws Exception {
		try {
			caseId = "CMS-92";
			log.startTestExecution(caseId);
			Map<String, String> sitepagedata = sqliteData.getDataProvider(aut.concatTestCaseName("testCMS_16"), DataDriver.datatable.page);
			boolean deletePage = aut.Delete_Site_Page_via_Manage_Site_Pages(sitepagedata);
			if (deletePage) {
				sqliteData.updateDeletedData(DataDriver.datatable.page, "TestCaseName", aut.concatTestCaseName("testCMS_16"));
			}

		} catch (Exception e) {
			log.exception(e);
			log.comment("Test Case ID", "No Locator", "Script did not complete successfully.", Log.FAIL, Log.MAJOR_ISSUE);
			throw e;
		} finally {
			log.endTestExecution();
		}
	}

	@Test(description = "CMS-87:Documents and Media_Folder_Add Folder")
	public void testCMS_87_Add_Folder() throws Exception {
		try {
			caseId = "CMS-87";
			log.startTestExecution(caseId);

			Map<String, String> folderdata = sqliteData.getDataProvider(aut.concatTestCaseName("testCMS_87"), DataDriver.datatable.folder);
			aut.Add_Folder(folderdata);

		} catch (Exception e) {
			log.exception(e);
			log.comment("Test Case ID", "No Locator", "Script did not complete successfully.", Log.FAIL, Log.MAJOR_ISSUE);
			throw e;
		} finally {
			log.endTestExecution();
		}
	}

	@Test(description = "CMS-87:Documents and Media_Folder_Add Subfolder")
	public void testCMS_47_Add_Subfolder_via_Menu_Bar() throws Exception {
		try {
			caseId = "CMS-47";
			log.startTestExecution(caseId);

			Map<String, String> folderdata = sqliteData.getDataProvider(aut.concatTestCaseName("testCMS_47"), DataDriver.datatable.folder);
			aut.Add_Folder(folderdata);

		} catch (Exception e) {
			log.exception(e);
			log.comment("Test Case ID", "No Locator", "Script did not complete successfully.", Log.FAIL, Log.MAJOR_ISSUE);
			throw e;
		} finally {
			log.endTestExecution();
		}
	}

	@Test(description = "CMS-56:Documents and Media_Add a basic document in a folder via Control Pannel")
	public void testCMS_56_Add_Basic_Document_via_Control_Panel() throws Exception {
		try {
			caseId = "CMS-56";
			log.startTestExecution(caseId);

			Map<String, String> basicdocdata = sqliteData.getDataProvider(aut.concatTestCaseName("testCMS_56"), DataDriver.datatable.basicdoc);
			aut.Add_Basic_Document_via_Control_Panel(basicdocdata);

		} catch (Exception e) {
			log.exception(e);
			log.comment("Test Case ID", "No Locator", "Script did not complete successfully.", Log.FAIL, Log.MAJOR_ISSUE);
			throw e;
		} finally {
			log.endTestExecution();
		}
	}

	@Test(description = "CMS-83:Categories_Add Vocabulary")
	public void testCMS_83_Add_Vocabulary() throws Exception {
		try {
			caseId = "CMS-83";
			log.startTestExecution(caseId);

			Map<String, String> categorydata = sqliteData.getDataProvider(aut.concatTestCaseName("testCMS_83"), DataDriver.datatable.vocabulary);
			aut.Add_Vocabulary(categorydata);

		} catch (Exception e) {
			log.exception(e);
			log.comment("Test Case ID", "No Locator", "Script did not complete successfully.", Log.FAIL, Log.MAJOR_ISSUE);
			throw e;
		} finally {
			log.endTestExecution();
		}
	}

	@Test(description = "CMS-80:Categories_Add Category")
	public void testCMS_80_Add_Category() throws Exception {
		try {
			caseId = "CMS-80";
			log.startTestExecution(caseId);

			Map<String, String> categorydata = sqliteData.getDataProvider(aut.concatTestCaseName("testCMS_80"), DataDriver.datatable.category);
			aut.Add_Category(categorydata);

		} catch (Exception e) {
			log.exception(e);
			log.comment("Test Case ID", "No Locator", "Script did not complete successfully.", Log.FAIL, Log.MAJOR_ISSUE);
			throw e;
		} finally {
			log.endTestExecution();
		}
	}

	@Test(description = "CMS-89:Documents and Media_Categorize Document")
	public void testCMS_89_Categorize_Document() throws Exception {
		try {
			caseId = "CMS-89";
			log.startTestExecution(caseId);

			Map<String, String> basicdocdata = sqliteData.getDataProvider(aut.concatTestCaseName("testCMS_56"), DataDriver.datatable.basicdoc);
			Map<String, String> categorydata = null;
			if (basicdocdata.get("Categories").contains("#")) {
				String[] Categories = basicdocdata.get("Categories").split("#");
				for (String category : Categories) {
					categorydata = sqliteData.getDataProvider(category, DataDriver.datatable.category);
					categorydata.putAll(basicdocdata);
					aut.Categorize_Document(categorydata);
				}
			} else {
				categorydata = sqliteData.getDataProvider(basicdocdata.get("Categories"), DataDriver.datatable.category);
				categorydata.putAll(basicdocdata);
				aut.Categorize_Document(categorydata);
			}

		} catch (Exception e) {
			log.exception(e);
			log.comment("Test Case ID", "No Locator", "Script did not complete successfully.", Log.FAIL, Log.MAJOR_ISSUE);
			throw e;
		} finally {
			log.endTestExecution();
		}
	}

	@Test(description = "CMS-62:Web Content_Add web content via Control Pannel")
	public void testCMS_62_Add_Web_Content_via_Control_Panel() throws Exception {
		try {
			caseId = "CMS-62";
			log.startTestExecution(caseId);

			Map<String, String> webcontentdata = sqliteData.getDataProvider(aut.concatTestCaseName("testCMS_62"), DataDriver.datatable.webcontent);
			aut.Add_Web_Content_via_Control_Panel(webcontentdata);

		} catch (Exception e) {
			log.exception(e);
			log.comment("Test Case ID", "No Locator", "Script did not complete successfully.", Log.FAIL, Log.MAJOR_ISSUE);
			throw e;
		} finally {
			log.endTestExecution();
		}
	}

	@Test(description = "CMS-144:Web Content_Create a draft Content")
	public void testCMS_144_Add_Draft_Web_Content_via_Control_Panel() throws Exception {
		try {
			caseId = "CMS-144";
			log.startTestExecution(caseId);

			Map<String, String> webcontentdata = sqliteData.getDataProvider(aut.concatTestCaseName("testCMS_144"), DataDriver.datatable.webcontent);
			aut.Add_Draft_Web_Content_via_Control_Panel(webcontentdata);

		} catch (Exception e) {
			log.exception(e);
			log.comment("Test Case ID", "No Locator", "Script did not complete successfully.", Log.FAIL, Log.MAJOR_ISSUE);
			throw e;
		} finally {
			log.endTestExecution();
		}
	}

	@Test(description = "CMS-88:Web Content_Categorize a Web Content")
	public void testCMS_88_Categorize_Web_Content() throws Exception {
		try {
			caseId = "CMS-88";
			log.startTestExecution(caseId);

			Map<String, String> webcontentdata = sqliteData.getDataProvider(aut.concatTestCaseName("testCMS_62"), DataDriver.datatable.webcontent);
			Map<String, String> categorydata = null;
			if (webcontentdata.get("Categories").contains("#")) {
				String[] Categories = webcontentdata.get("Categories").split("#");
				for (String category : Categories) {
					categorydata = sqliteData.getDataProvider(category, DataDriver.datatable.category);
					categorydata.putAll(webcontentdata);
					aut.Categorize_Web_Content(categorydata);
				}
			} else {
				categorydata = sqliteData.getDataProvider(webcontentdata.get("Categories"), DataDriver.datatable.category);
				categorydata.putAll(webcontentdata);
				aut.Categorize_Web_Content(categorydata);
			}

		} catch (Exception e) {
			log.exception(e);
			log.comment("Test Case ID", "No Locator", "Script did not complete successfully.", Log.FAIL, Log.MAJOR_ISSUE);
			throw e;
		} finally {
			log.endTestExecution();
		}
	}

	@Test(description = "CMS-82:Tags_Add tag via Tags page")
	public void testCMS_82_Add_Tag_via_Tags_Page() throws Exception {
		try {
			caseId = "CMS-82";
			log.startTestExecution(caseId);

			Map<String, String> tagdata = sqliteData.getDataProvider(aut.concatTestCaseName("testCMS_82"), DataDriver.datatable.tag);
			aut.Add_Tag_via_Tags_Page(tagdata);

		} catch (Exception e) {
			log.exception(e);
			log.comment("Test Case ID", "No Locator", "Script did not complete successfully.", Log.FAIL, Log.MAJOR_ISSUE);
			throw e;
		} finally {
			log.endTestExecution();
		}
	}

	@Test(description = "CMS-97:Documents and Media_Check Out")
	public void testCMS_97_Check_Out_Document() throws Exception {
		try {
			caseId = "CMS-97";
			log.startTestExecution(caseId);

			Map<String, String> basicdocdata = sqliteData.getDataProvider(aut.concatTestCaseName("testCMS_56"), DataDriver.datatable.basicdoc);
			aut.Check_Out_Document(basicdocdata);

		} catch (Exception e) {
			log.exception(e);
			log.comment("Test Case ID", "No Locator", "Script did not complete successfully.", Log.FAIL, Log.MAJOR_ISSUE);
			throw e;
		} finally {
			log.endTestExecution();
		}
	}

	@Test(description = "CMS-99:Documents and Media_Check In")
	public void testCMS_99_Check_In_Document() throws Exception {
		try {
			caseId = "CMS-99";
			log.startTestExecution(caseId);

			Map<String, String> basicdocdata = sqliteData.getDataProvider(aut.concatTestCaseName("testCMS_56"), DataDriver.datatable.basicdoc);
			aut.Check_In_Document(basicdocdata);

		} catch (Exception e) {
			log.exception(e);
			log.comment("Test Case ID", "No Locator", "Script did not complete successfully.", Log.FAIL, Log.MAJOR_ISSUE);
			throw e;
		} finally {
			log.endTestExecution();
		}
	}

	@Test(description = "CMS-102:Documents and Media_Cancel Checkout")
	public void testCMS_102_Cancel_Check_Out_Document() throws Exception {
		try {
			caseId = "CMS-102";
			log.startTestExecution(caseId);

			Map<String, String> basicdocdata = sqliteData.getDataProvider(aut.concatTestCaseName("testCMS_56"), DataDriver.datatable.basicdoc);
			aut.Check_Out_Document(basicdocdata);
			aut.Cancel_Check_Out_Document(basicdocdata);

		} catch (Exception e) {
			log.exception(e);
			log.comment("Test Case ID", "No Locator", "Script did not complete successfully.", Log.FAIL, Log.MAJOR_ISSUE);
			throw e;
		} finally {
			log.endTestExecution();
		}
	}

	@Test(description = "CMS-61:Documents and Media_Delete Document")
	public void testCMS_61_Delete_Document() throws Exception {
		try {
			caseId = "CMS-61";
			log.startTestExecution(caseId);

			Map<String, String> basicdocdata = sqliteData.getDataProvider(aut.concatTestCaseName("testCMS_61"), DataDriver.datatable.basicdoc);
			aut.Add_Basic_Document_via_Control_Panel(basicdocdata);
			boolean basicdocdeleted = aut.Delte_Document(basicdocdata);
			if (basicdocdeleted) {
				sqliteData.updateDeletedData(DataDriver.datatable.basicdoc, "TestCaseName", aut.concatTestCaseName("testCMS_61"));
			}

		} catch (Exception e) {
			log.exception(e);
			log.comment("Test Case ID", "No Locator", "Script did not complete successfully.", Log.FAIL, Log.MAJOR_ISSUE);
			throw e;
		} finally {
			log.endTestExecution();
		}
	}

	@Test(description = "CMS-96:Tags_Delete tag via Tags Page")
	public void testCMS_96_Delete_Tag_via_Tags_Page() throws Exception {
		try {
			caseId = "CMS-96";
			log.startTestExecution(caseId);

			Map<String, String> tagdata = sqliteData.getDataProvider(aut.concatTestCaseName("testCMS_96"), DataDriver.datatable.tag);
			aut.Add_Tag_via_Tags_Page(tagdata);

			boolean tagdeleted = aut.Delete_Tag_via_Tags_Page(tagdata);
			if (tagdeleted) {
				sqliteData.updateDeletedData(DataDriver.datatable.basicdoc, "TestCaseName", aut.concatTestCaseName("testCMS_96"));
			}

		} catch (Exception e) {
			log.exception(e);
			log.comment("Test Case ID", "No Locator", "Script did not complete successfully.", Log.FAIL, Log.MAJOR_ISSUE);
			throw e;
		} finally {
			log.endTestExecution();
		}
	}

	@Test(description = "CMS-339:Site_Delete Site")
	public void testCMS_339_Delete_Site() throws Exception {
		try {
			caseId = "CMS-339";
			log.startTestExecution(caseId);

			Map<String, String> sitedata = sqliteData.getDataProvider(aut.concatTestCaseName("testCMS_72"), DataDriver.datatable.site);
			aut.Delete_Site_and_Update_Database(sitedata, sqliteData);

		} catch (Exception e) {
			log.exception(e);
			log.comment("Test Case ID", "No Locator", "Script did not complete successfully.", Log.FAIL, Log.MAJOR_ISSUE);
			throw e;
		} finally {
			log.endTestExecution();
		}
	}

	@Test(description = "CMS-145:Portlet_Add Search Portlet")
	public void testCMS_145_Add_Search_Portlet() throws Exception {
		try {
			caseId = "CMS-145";
			log.startTestExecution(caseId);

			Map<String, String> pagedata = sqliteData.getDataProvider(aut.concatTestCaseName("testCMS_14"), DataDriver.datatable.page);
			aut.Add_Search_Portlet(pagedata);

		} catch (Exception e) {
			log.exception(e);
			log.comment("Test Case ID", "No Locator", "Script did not complete successfully.", Log.FAIL, Log.MAJOR_ISSUE);
			throw e;
		} finally {
			log.endTestExecution();
		}
	}

	@Test(description = "CMS-90:Web Content_Add a web content with Image in it")
	public void testCMS_90_Add_Web_Content_with_Image_via_Control_Panel() throws Exception {
		try {
			caseId = "CMS-90";
			log.startTestExecution(caseId);

			Map<String, String> folderdata = sqliteData.getDataProvider(aut.concatTestCaseName("testCMS_90"), DataDriver.datatable.folder);
			aut.Add_Folder(folderdata);

			Map<String, String> basicdocdata = sqliteData.getDataProvider(aut.concatTestCaseName("testCMS_90"), DataDriver.datatable.basicdoc);
			aut.Add_Basic_Document_via_Control_Panel(basicdocdata);

			Map<String, String> webcontentdata = sqliteData.getDataProvider(aut.concatTestCaseName("testCMS_90"), DataDriver.datatable.webcontent);
			aut.Add_Web_Content_via_Control_Panel(webcontentdata);

		} catch (Exception e) {
			log.exception(e);
			log.comment("Test Case ID", "No Locator", "Script did not complete successfully.", Log.FAIL, Log.MAJOR_ISSUE);
			throw e;
		} finally {
			log.endTestExecution();
		}
	}

	// TODO CMS-116:Documents and Media_Assign related assets
	// TODO CMS-66:Web Content_Delete web content
	// TODO CMS-79:Web Content_View history

	// TODO CMS-115:Web Content_Assign related assets


	// ================================== END: TEST CASE SECTION ==================================

	// -------------------------------- START PUBLIC METHOD SECTION ------------------------------
	// -------------------------------- END PUBLIC METHOD SECTION --------------------------------

	@AfterClass(alwaysRun = true)
	public void afterClass() throws Exception {

		try {

			log.startTestExecution("AfterClass(Generic Module)");
			log.comment("Close sqlite database connection");
			sqliteData.closeConnection();

			log.comment("Close Browser!!!");
			aut.getBrowser().getWebDriver().quit();

			log.comment("DONE WITH TESTING!!!");
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

	// --------------------------Deprecated--------------------------Deprecated--------------------------

	// public void assertEditORGMandatoryField(LocatorType locatorType) throws Exception {
	// String currentValue = aut.getElementText(locatorType, true);
	// aut.setText(locatorType, "");
	// aut.click(locator.getLocator("CCA.ORG.EditProfile.btn_Submit"));
	// // TODO look into --> alert.accept();
	// WebDriver driver = aut.getBrowser().getWebDriver();
	// Alert alert = driver.switchTo().alert();
	// String text = alert.getText();
	// alert.accept();
	// log.verifyStep(text.contains("The following fields are mandatory:"), "verify text contains", "pass", "");
	// aut.setText(locatorType, currentValue);
	// }

	// TODO look into --> aut.sendEnterKeyToElement
	// aut.sendEnterKeyToElement(locator.getLocator("CCA.MyOrg.Options.lnk_RequireReason").getValue());
	// aut.navigateBack();
	// verifyTextEquals(fname, aut.getElementText(locator.getLocator("CCA.MyProfile.EditProfile.txt_FirstName"), true));
	// aut.getRandomString(6);
	// aut.mouseOver(locator.getLocator("CCA.Admin.AdminOption.link_RolesandServices"));

	// TODO look into --> innerclass ORG
	public ORG registerRandomDivision() {
		String random_div_name = "DIV" + aut.getRandomString(3);
		String random_div_user_id = "UID" + aut.getRandomString(6);
		String password = "test1234";
		return new ORG(random_div_name, random_div_user_id, password);
	}

	/**
	 * org class
	 * 
	 * @author u122933
	 * 
	 */
	public class ORG {

		public String getOrgName() {
			return orgName;
		}

		public void setOrgName(String orgName) {
			this.orgName = orgName;
		}

		public String getAdminName() {
			return adminName;
		}

		public void setAdminName(String adminName) {
			this.adminName = adminName;
		}

		public String getAdminUID() {
			return adminUID;
		}

		public void setAdminUID(String adminUID) {
			this.adminUID = adminUID;
		}

		public String getAdminPassword() {
			return adminPassword;
		}

		public void setAdminPassword(String adminPassword) {
			this.adminPassword = adminPassword;
		}

		String	orgName;
		String	adminName;
		String	adminUID;
		String	adminPassword;

		public ORG(String orgName, String adminUID, String adminPassword) {
			this.orgName = orgName;
			this.adminUID = adminUID;
			this.adminPassword = adminPassword;
		}

	}

}
