package com.covisint.cms.rp;

//import java.text.DateFormat;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//import java.util.Iterator;
//import java.util.List;
//import java.util.Map;
//import java.util.concurrent.TimeUnit;

//import org.apache.commons.lang.StringUtils;
//import org.openqa.selenium.Alert;
//import org.openqa.selenium.By;
//import org.openqa.selenium.JavascriptExecutor;
//import org.openqa.selenium.WebDriver;
//import org.openqa.selenium.WebElement;
//import org.openqa.selenium.interactions.Actions;
//import org.openqa.selenium.internal.seleniumemulation.JavascriptLibrary;
//import org.openqa.selenium.remote.RemoteWebElement;
//import org.testng.ITestContext;

import java.util.ArrayList;
import java.util.Map;

import org.openqa.selenium.By;

import main.java.com.compuware.gdo.framework.core.Browser;
//import main.java.com.compuware.gdo.framework.core.Locators;
//import main.java.com.compuware.gdo.framework.core.TestData;
//import main.java.com.compuware.gdo.framework.core.Locators.LocatorType;
//import main.java.com.compuware.gdo.framework.core.enums.BrowserType;
//import main.java.com.compuware.gdo.framework.core.exceptions.FrameworkException;
import main.java.com.compuware.gdo.framework.utilities.logging.Log;

//import WrappedElement.Page;
//import com.covisint.cms.*;
import com.covisint.cms.generic.CMS_GENERIC_Lib;

import WrappedElement.DataDriver;
import WrappedElement.Page;
import WrappedElement.PublishToRemoteLiveDialog;

public class CMS_RP_Lib extends CMS_GENERIC_Lib {

	protected PublishToRemoteLiveDialog	dialog	= null;

	public CMS_RP_Lib(Log log, Browser browser) {
		super(log, browser);
		this.dialog = new PublishToRemoteLiveDialog(this, log);
	}

	// ---------------------------------------------------
	// --- START PUBLIC METHOD SECTION

	public void Publish_to_Remote_Live_Now_via_Staging_Menu(Map<String, String> testdata) throws Exception {

		try {
			page.goToPage(testdata.get("StagingSite"));
			page.openDialogPublishNow();

			Publish_to_Remote_Live_Now(testdata);
			// Verify_Published_Data(testdata);
		} catch (Exception e) {
			log.exception(e);
			log.comment("Method Name", "No Locator", "Script did not complete successfully.", Log.FAIL, Log.MAJOR_ISSUE);
			throw e;
		} finally {
			page.goBackToViewPage();
		}
	}

	public void Publish_to_Remote_Live_Now_via_Site_Pages(Map<String, String> testdata) throws Exception {

		try {

			page.goToPage("Control Panel");
			page.setCurrentSite(testdata.get("StagingSite"));
			page.openPage(Page.Menu.content, "Site Pages");
			page.openDialogPublishNow();

			Publish_to_Remote_Live_Now(testdata);

		} catch (Exception e) {
			log.exception(e);
			log.comment("Method Name", "No Locator", "Script did not complete successfully.", Log.FAIL, Log.MAJOR_ISSUE);
			throw e;
		} finally {
			page.goBackToViewPage();
		}
	}

	public void Verify_Published_Data(Map<String, String> testdata, DataDriver sqliteData) throws Exception {

		try {
			driver.navigate().refresh();
			page.goToPage(testdata.get("LiveSite"));
			// page.goToPage("Control Panel");
			// page.setCurrentSite(testdata.get("LiveSite"));
			// page.goBackToViewPage();

			if (!(testdata.get("Pages") == null)) {
				Verify_Published_Pages(testdata.get("Pages"), sqliteData);
			}
			if (!(testdata.get("DocOptions") == null)) {
				Verify_Published_Doc(testdata.get("DocOptions"));
			}
			if (!(testdata.get("ContentOptions") == null)) {
				Verify_Published_Web_Content(testdata.get("TestCaseName"), sqliteData);
			}
			if (!(testdata.get("ContentDisplaysOptions") == null)) {
				Verify_Published_Display(testdata.get("ContentDisplaysOptions"));
			}
			if (!(testdata.get("OtherOptions") == null)) {
				// if (!(testdata.get("Pages") == null) && testdata.get("OtherOptions").contains("Permissions")) {
				// Verify_Page_Permissions(testdata.get("PageName"), testdata.get("PagePermissions"));
				// }
			}

		} catch (Exception e) {
			log.exception(e);
			log.comment("Method Name", "No Locator", "Script did not complete successfully.", Log.FAIL, Log.MAJOR_ISSUE);
			throw e;
		} finally {
			page.goBackToViewPage();
		}

	}

	private void Verify_Published_Pages(String pages, DataDriver sqliteData) throws Exception {

		try {
			log.comment("**********Remote Publishing_Verify Published Pages**********");
			page.goToPage("Control Panel");
			page.openPage(Page.Menu.content, "Site Pages");

			String[] page = pages.split("#");
			for (int i = 0; i < page.length; i++) {
				if (page[i].endsWith("*")) {
					verifyElementNotExist(getDynamicLocatorType("CMS.ControlPanel.SitePages.text_PageName", page[i].replace("*", "")));
				} else if (page[i].endsWith("$")) {
					verifyElementNotExist(getDynamicLocatorType("CMS.ControlPanel.SitePages.text_PageName", page[i].replace("$", "")));
				} else {
					verifyElementExist(getDynamicLocatorType("CMS.ControlPanel.SitePages.text_PageName", page[i]));
					Map<String, String> pagedata = sqliteData.getDataProvider(page[i], DataDriver.datatable.page);
					if (!(pagedata.get("PagePermissions") == null)) {
						Verify_Page_Permissions(page[i], pagedata.get("PagePermissions"));
					}
				}
			}

		} catch (Exception e) {
			log.exception(e);
			log.comment("Method Name", "No Locator", "Script did not complete successfully.", Log.FAIL, Log.MAJOR_ISSUE);
			throw e;
		} finally {
			page.goBackToViewPage();
		}
	}

	private void Verify_Page_Permissions(String sitepage, String pagepermissions) throws Exception {

		try {
			log.comment("**********Verify Page Permissions for page: " + sitepage + " **********");

			// page.goToPage("Control Panel");
			// page.openPage(Page.Menu.content, "Site Pages");
			// verifyElementExist(getDynamicLocatorType("CMS.ControlPanel.SitePages.text_PageName", sitepage));

			click(getDynamicLocatorType("CMS.ControlPanel.SitePages.text_PageName", sitepage));
			waitFor(1);
			waitForPageToLoad();
			click(commonLocators.getLocator("CMS.ControlPanel.SitePages.button_Permissions"));
			waitFor(3);
			verifyElementExist(commonLocators.getLocator("CMS.ControlPanel.PermissionsDialog.text_Title"));
			String winhndl = getBrowser().getWebDriver().getWindowHandle();
			String framename = driver.findElement(By.xpath("//iframe[contains(@name,'_permissions')]")).getAttribute("name");
			driver.switchTo().frame(framename);
			driver.switchTo().activeElement();

			verify_Permissions_for_Site_Page(pagepermissions);

			driver.switchTo().window(winhndl);
			captureScreenShot();
			click(commonLocators.getLocator("CMS.ControlPanel.PermissionsDialog.button_Close"));
			waitFor(1);
			waitForPageToLoad();

		} catch (Exception e) {
			log.exception(e);
			log.comment("Method Name", "No Locator", "Script did not complete successfully.", Log.FAIL, Log.MAJOR_ISSUE);
			throw e;
		}
	}

	private void Verify_Web_Content_Permissions(String webcontent, String pagepermissions) throws Exception {

		try {
			log.comment("**********Verify Web Content Permissions for Web Content: " + webcontent + " **********");

			// // page.goToPage("Control Panel");
			// // page.openPage(Page.Menu.content, "Site Pages");
			// // verifyElementExist(getDynamicLocatorType("CMS.ControlPanel.SitePages.text_PageName", sitepage));
			//
			// click(getDynamicLocatorType("CMS.ControlPanel.SitePages.text_PageName", webcontent));
			// waitFor(1);
			// waitForPageToLoad();
			// click(commonLocators.getLocator("CMS.ControlPanel.SitePages.button_Permissions"));
			// waitFor(3);
			// verifyElementExist(commonLocators.getLocator("CMS.ControlPanel.PermissionsDialog.text_Title"));
			// String winhndl = getBrowser().getWebDriver().getWindowHandle();
			// String framename = driver.findElement(By.xpath("//iframe[contains(@name,'_permissions')]")).getAttribute("name");
			// driver.switchTo().frame(framename);
			// driver.switchTo().activeElement();
			//
			// verify_Permissions_for_Site_Page(pagepermissions);
			//
			// driver.switchTo().window(winhndl);
			// captureScreenShot();
			// click(commonLocators.getLocator("CMS.ControlPanel.PermissionsDialog.button_Close"));
			// waitFor(1);
			// waitForPageToLoad();

		} catch (Exception e) {
			log.exception(e);
			log.comment("Method Name", "No Locator", "Script did not complete successfully.", Log.FAIL, Log.MAJOR_ISSUE);
			throw e;
		}
	}

	private void Verify_Published_Web_Content(String testCaseName, DataDriver sqliteData) throws Exception {

		try {

			log.comment("**********Remote Publishing_Verify Published Web Contents**********");
			page.goToPage("Control Panel");
			page.openPage(Page.Menu.content, "Web Content");

			ArrayList<String> testCaseNames = sqliteData.getColummnData("TestCaseName", DataDriver.datatable.webcontent);
			for (String casename : testCaseNames) {
				if (casename.contains(testCaseName)) {
					Map<String, String> contentdata = sqliteData.getDataProvider(casename, DataDriver.datatable.webcontent);
					String contentName = contentdata.get("WebContentName");
					// String siteName = webcontentdata.get("Site");
					verifyElementExist(getDynamicLocatorType("CMS.ControlPanel.datatable.link-cell_name", contentName));
					if (!(contentdata.get("WebContentPermissions") == null)) {
						Verify_Web_Content_Permissions(contentName, contentdata.get("WebContentPermissions"));
					}
				}
			}

		} catch (Exception e) {
			log.exception(e);
			log.comment("Method Name", "No Locator", "Script did not complete successfully.", Log.FAIL, Log.MAJOR_ISSUE);
			throw e;
		} finally {
			page.goBackToViewPage();
		}
	}

	private void Verify_Published_Doc(String docOptions) throws Exception {

		try {
			// TODO Verify_Published_Doc
			page.goToPage("Control Panel");
			page.openPage(Page.Menu.content, "Documents and Media");

		} catch (Exception e) {
			log.exception(e);
			log.comment("Method Name", "No Locator", "Script did not complete successfully.", Log.FAIL, Log.MAJOR_ISSUE);
			throw e;
		} finally {
			page.goBackToViewPage();
		}
	}

	private void Verify_Published_Display(String contentDisplayOptions) throws Exception {

		try {
			// TODO Verify_Published_Doc
			page.goToPage("Control Panel");
			page.openPage(Page.Menu.content, "Site Pages");

		} catch (Exception e) {
			log.exception(e);
			log.comment("Method Name", "No Locator", "Script did not complete successfully.", Log.FAIL, Log.MAJOR_ISSUE);
			throw e;
		} finally {
			page.goBackToViewPage();
		}
	}

	private void Verify_Other_Item(String otherOptions) throws Exception {

		try {
			// TODO Verify_Published_Doc
			page.goToPage("Control Panel");
			page.openPage(Page.Menu.content, "Site Pages");

			page.openPage(Page.Menu.content, "Categories");

			page.openPage(Page.Menu.content, "Web Content");

		} catch (Exception e) {
			log.exception(e);
			log.comment("Method Name", "No Locator", "Script did not complete successfully.", Log.FAIL, Log.MAJOR_ISSUE);
			throw e;
		} finally {
			page.goBackToViewPage();
		}
	}

	public void Publish_to_Remote_Live_Now(Map<String, String> testdata) throws Exception {

		try {

			log.comment("**********Remote Publishing_Publish to Remote Live Now**********");

			dialog.openAllPanels();
			dialog.selectPages(testdata.get("Pages"));
			dialog.checkPageOptions(testdata.get("PageOption"));

			dialog.checkRangeOption(testdata.get("RangeOption"));
			dialog.checkWebContentOptions(testdata.get("ContentOptions"));

			dialog.checkOtherOptions(testdata.get("OtherOptions"));

			dialog.publish();

		} catch (Exception e) {
			log.exception(e);
			log.comment("Method Name", "No Locator", "Script did not complete successfully.", Log.FAIL, Log.MAJOR_ISSUE);
			throw e;
		} finally {
			dialog.close();
		}
	}
	// --- END PUBLIC METHOD SECTION
	// ---------------------------------------------------

	// ---------------------------------------------------

	// ---------------------------------------------------

}
