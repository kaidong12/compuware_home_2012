package com.covisint.cms.generic;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.internal.seleniumemulation.JavascriptLibrary;
import org.openqa.selenium.remote.RemoteWebElement;
import org.testng.ITestContext;

import main.java.com.compuware.gdo.framework.core.Browser;
import main.java.com.compuware.gdo.framework.core.Locators;
import main.java.com.compuware.gdo.framework.core.TestData;
import main.java.com.compuware.gdo.framework.core.Locators.LocatorType;
import main.java.com.compuware.gdo.framework.core.enums.BrowserType;
import main.java.com.compuware.gdo.framework.core.exceptions.FrameworkException;
import main.java.com.compuware.gdo.framework.utilities.logging.Log;

import WrappedElement.DataDriver;
import WrappedElement.Page;
import com.covisint.cms.*;

public class CMS_GENERIC_Lib extends CMSWebApp {

	protected int					_waitTimeoutSeconds	= 30;
	protected int					_waitTimeoutSecondsForMail;
	protected JavascriptExecutor	jse					= null;
	protected TestData				genericTestData		= null;
	protected Object[]				testDataItem		= null;
	protected Page					page				= null;

	public CMS_GENERIC_Lib(Log log, Browser browser, TestData testData) {
		super(log, browser);
		this.genericTestData = testData;
		this.page = new Page(this, log);
	}

	public CMS_GENERIC_Lib(Log log, Browser browser) {
		super(log, browser);
		this.page = new Page(this, log);
	}

	// --- START PUBLIC METHOD SECTION
	// ---------------------------------------------------

	public String getRandomString(int length) {

		String str = "";
		for (int i = 0; i < length; i++) {
			str = str + (char) (Math.random() * 26 + 'A');
		}
		return str;
	}

	public String getRandomNumber(int length) {
		return Integer.toString((int) (Math.random() * Math.pow(10, length)));
	}

	// public Iterator<Object[]> getDataProvidertestData(String rowName) throws Exception {
	// Iterator<Object[]> res = null;
	// try {
	// // cmsTestData = new TestData(parameters.get("CMS_GENERIC_TestData"));
	// // res = cmsTestData.get(parameters.get("ENVIRONMENT") + rowName);
	// res = cmsTestData.get(concatTestCaseName(rowName));
	// } catch (Exception e) {
	// // log.startTestExecution("cmsTestData"+"rowName");
	// log.exception(e);
	// log.endTestExecution();
	// // Re-throw exception...
	// throw e;
	// }
	// return res;
	// }

	public String getTestCaseData(String row, String column) throws Exception {
		if (getDataProvider(genericTestData, concatTestCaseName(row)).hasNext())
			testDataItem = getDataProvider(genericTestData, concatTestCaseName(row)).next();
		return genericTestData.getTestDataValue((Object[]) testDataItem[0], column);
	}

	/**
	 * @author Terry Sun
	 * @description Get comm locators for test cases.
	 * @param commLoc
	 * @return locators
	 * @date 2011-12-21
	 */
	public LocatorType getCommLocatorsForTestCase(String commLoc) throws Exception {
		// CMS_Comm_Locators = new Locators(parameters.get("Common_Locators"));
		// return CMS_Comm_Locators.getLocator(commLoc);
		return commonLocators.getLocator(commLoc);
	}

	public void mouseOver(Locators.LocatorType locatorType) {
		mouseOver(locatorType.getValue());
	}

	public void mouseEmulator(LocatorType locatorType, String mouseEvent, String callingMethodName) {
		WebElement webElement = this.getWebElement(locatorType, callingMethodName);
		fireJavaScriptMouseEvent(webElement, mouseEvent, callingMethodName);
	}

	/**
	 * @author Lance Yan
	 * @description mouseover
	 * @param elementXpath
	 * @return void
	 * @date 2012-06-21
	 */
	public void forceMouseOver(Locators.LocatorType locatorType, String callingMethodName) {
		// WebElement elementRegister = driver.findElement(By.xpath(elementXpath));
		WebElement elementRegister = this.getWebElement(locatorType, callingMethodName);
		Actions builder = new Actions(driver);
		Actions hoverOverElement = builder.moveToElement(elementRegister);
		hoverOverElement.build().perform();
		// builder.moveByOffset(1, 1).build().perform();
	}

	/**
	 * @author Lance Yan
	 * @description mouseover
	 * @param elementXpath
	 * @return void
	 * @date 2012-07-27
	 */
	public void forceClick(Locators.LocatorType locatorType, String callingMethodName) {
		// WebElement elementRegister = driver.findElement(By.xpath(elementXpath));
		WebElement elementRegister = this.getWebElement(locatorType, callingMethodName);
		Actions builder = new Actions(driver);
		Actions clickElement = builder.moveToElement(elementRegister).click();
		clickElement.build().perform();
	}

	/**
	 * @author Lance Yan
	 * @description fire JavaScript Mouse Event
	 * @param webElement
	 * @param mouseEvent
	 *            -->mouseover, mouseup, mousemove, mouseout, etc.
	 * @param callingMethodName
	 * @return void
	 * @date 2012-06-27
	 */
	protected void fireJavaScriptMouseEvent(WebElement webElement, String mouseEvent, String callingMethodName) {
		if (webElement == null) {
			throw new FrameworkException(callingMethodName, "webElement", "MAKES REFERENCE TO A NULL POINTER", Log.ERROR, Log.SCRIPT_ISSUE);
		}
		if (mouseEvent == null) {
			throw new FrameworkException(callingMethodName, "mouseEvent", "MAKES REFERENCE TO A NULL POINTER", Log.ERROR, Log.SCRIPT_ISSUE);
		}
		if (mouseEvent.isEmpty()) {
			throw new FrameworkException(callingMethodName, "mouseEvent", "MAKES REFERENCE TO AN EMPTY STRING", Log.ERROR, Log.SCRIPT_ISSUE);
		}
		StringBuilder code = new StringBuilder();
		code.append("var fireOnThis = arguments[0];");
		code.append("var evObj = document.createEvent('MouseEvents');");
		code.append("evObj.initEvent( '%s', true, true );");
		code.append("fireOnThis.dispatchEvent(evObj);");
		code.trimToSize();
		jse = (JavascriptExecutor) driver;
		jse.executeScript(String.format(code.toString(), mouseEvent), webElement);

		// WebDriver _driver = driver;
		// JavascriptLibrary _js = new JavascriptLibrary();
		// _js.executeScript(_driver, String.format(code.toString(), mouseEvent), webElement);

	}

	public void mouseOver(String xpath) {

		WebDriver _driver = driver;
		RemoteWebElement _webElement = (RemoteWebElement) _driver.findElement(By.xpath(xpath));

		JavascriptLibrary _js = new JavascriptLibrary();

		_js.callEmbeddedSelenium(_driver, "triggerMouseEventAt", _webElement, "mouseover");
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		// _js.callEmbeddedSelenium(_driver, "triggerMouseEventAt", _webElement,
		// "mousemove");

	}

	public void setWaitTimeout(int timeoutSeconds) {
		_waitTimeoutSeconds = timeoutSeconds;
		driver.manage().timeouts().implicitlyWait(timeoutSeconds, TimeUnit.SECONDS);
	}

	public int getWaitTimeout() {
		return this._waitTimeoutSeconds;
	}

	public void sendEnterKeyToElement(String xpath) {
		RemoteWebElement element = (RemoteWebElement) driver.findElement(By.xpath(xpath));
		element.sendKeys("\n");
	}

	public String getSelectedOptionText(String xpath) {
		// WebDriver driver = this.getBrowser().getWebDriver();
		List<WebElement> options = driver.findElement(By.xpath(xpath)).findElements(By.tagName("option"));

		for (WebElement option : options) {
			if (option.isSelected()) {
				return option.getText();
			}
		}
		return null;
	}

	public void clickLinkByText(String linkText) {
		driver.findElement(By.linkText(linkText)).click();
	}

	public Browser reStartBrowser(Log log, BrowserType browsertype, ITestContext context, Browser browser) throws Exception {
		browser.quit();
		// driver.quit();
		browser = new Browser(log, context, browsertype);
		setBrowser(browser);
		return browser;
	}

	public void getWindow(String windowsName) {
		for (String handle : driver.getWindowHandles()) {
			String wintitle = driver.switchTo().window(handle).getTitle();
			if (wintitle.contains(windowsName)) {
				driver.switchTo().window(handle);
				break;
			}
		}
	}

	// --- START PUBLIC METHOD SECTION
	// ---------------------------------------------------

	public String Add_Default_Blank_Site(Object[] testDataItem) throws Exception {
		try {

			log.comment("**********Site_Add A Default Blank Site**********");

			page.goToPage("Control Panel");
			page.openPage(Page.Menu.portal, "Sites");

			waitForPageToLoad();
			log.comment("Verify Sites page opened.");
			verifyElementExist(getDynamicLocatorType("CMS.ControlPanel.SitePage.pagetitle", "Sites"));
			click(commonLocators.getLocator("CMS.ControlPanel.SitePage.menu-button_Add"));
			click(commonLocators.getLocator("CMS.ControlPanel.AddSite.menu-link_BlankSite"));
			waitForPageToLoad();

			log.comment("Verify New Site page opened.");
			verifyElementExist(getDynamicLocatorType("CMS.ControlPanel.SitePage.sectiontitle", "New Site"));
			setText(commonLocators.getLocator("CMS.ControlPanel.SitePage.textbox_NewSite"),
					genericTestData.getTestDataValue(testDataItem, "SiteName"));
			setText(commonLocators.getLocator("CMS.ControlPanel.SitePage.textarea_Description"),
					genericTestData.getTestDataValue(testDataItem, "SiteDescription"));
			click(commonLocators.getLocator("CMS.ControlPanel.RightSideMenu.menu-button_Save"));
			waitForPageToLoad();

			log.comment("Check Success messages on New Site page");
			verifyElementExist(commonLocators.getLocator("CMS.CorePortal.Messages.success"));
			verifyElementExist(getDynamicLocatorType("CMS.ControlPanel.SitePage.sectiontitle",
					genericTestData.getTestDataValue(testDataItem, "SiteName")));
			verifyElementExist(getDynamicLocatorType("CMS.ControlPanel.LeftSideMenu.panel-title_SiteName",
					genericTestData.getTestDataValue(testDataItem, "SiteName")));

			// Update Site ID to database
			String siteIDstr = getElementText(commonLocators.getLocator("CMS.ControlPanel.SitePage.text_SiteID"), false);
			// String siteID = StringUtils.substringBetween(siteIDstr, "Site ID ", "\n");
			String siteID = StringUtils.substringAfter(siteIDstr, "Site ID ");
			log.comment("New Site ID is: " + siteID);
			// System.out.println("\n\n\n\n\n\n" + siteID + "\n\n\n\n\n\n");

			log.comment("Check new Site displays on Sites table");
			click(commonLocators.getLocator("CMS.ControlPanel.SitePage.sectiontitle_Back"));
			waitForPageToLoad();
			click(commonLocators.getLocator("CMS.ControlPanel.datatable.header-name"));
			waitForPageToLoad();
			if (isElementPresent(commonLocators.getLocator("CMS.ControlPanel.datatable.header-name_asc"))) {
				click(commonLocators.getLocator("CMS.ControlPanel.datatable.header-name_asc"));
				waitForPageToLoad();
			}
			verifyElementExist(getDynamicLocatorType("CMS.ControlPanel.datatable.link-cell_name",
					genericTestData.getTestDataValue(testDataItem, "SiteName")));

			// TODO datatable(Grid)
			// Grid siteGrid = Grid.getGrid(aut, "Sites");
			// log.verifyStep(siteGrid.columnHeadersMatch(new String[] { "",
			// "ID", "Title", "Status", "Modified Date", "Display Date",
			// "Author", "" }),
			// "verify grid headers", "pass", "");
			// siteGrid.findDataRowInAllPages(1,
			// "USER_AUTO, USER_FOR_VIEW").findElement(By.tagName("A")).click();
			// waitForPageToLoad();
			return siteID;
		} catch (Exception e) {
			log.exception(e);
			log.comment("Method Name", "No Locator", "Script did not complete successfully.", Log.FAIL, Log.MAJOR_ISSUE);
			throw e;
		} finally {
			page.goBackToViewPage();
		}
	}

	public void Add_Default_Blank_Site_and_Update_Database(Map<String, String> testdata, DataDriver sqliteData) throws Exception {
		try {
			String siteID = Add_Default_Blank_Site(testdata);
			sqliteData.updateSiteID(testdata.get("SiteName"), siteID);
			sqliteData.updateAddedData(DataDriver.datatable.site, "TestCaseName", testdata.get("TestCaseName"));
		} catch (Exception e) {
			log.exception(e);
			log.comment("Method Name", "No Locator", "Script did not complete successfully.", Log.FAIL, Log.MAJOR_ISSUE);
			throw e;
		} finally {
			page.goBackToViewPage();
		}
	}

	protected String Add_Default_Blank_Site(Map<String, String> testdata) throws Exception {
		try {

			log.comment("**********Site_Add A Default Blank Site**********");

			page.goToPage("Control Panel");
			page.openPage(Page.Menu.portal, "Sites");

			// waitForPageToLoad();
			// log.comment("Verify Sites page opened.");
			// verifyElementExist(getDynamicLocatorType("CMS.ControlPanel.SitePage.pagetitle", "Sites"));
			click(commonLocators.getLocator("CMS.ControlPanel.SitePage.menu-button_Add"));
			click(commonLocators.getLocator("CMS.ControlPanel.AddSite.menu-link_BlankSite"));
			waitForPageToLoad();

			log.comment("Verify New Site page opened.");
			verifyElementExist(getDynamicLocatorType("CMS.ControlPanel.SitePage.sectiontitle", "New Site"));
			setText(commonLocators.getLocator("CMS.ControlPanel.SitePage.textbox_NewSite"), testdata.get("SiteName"));
			setText(commonLocators.getLocator("CMS.ControlPanel.SitePage.textarea_Description"), testdata.get("SiteDescription"));

			click(commonLocators.getLocator("CMS.ControlPanel.RightSideMenu.menu-button_Save"));
			waitForPageToLoad();

			log.comment("Check Success messages on New Site page");
			page.verifySuccessMessage();
			verifyElementExist(getDynamicLocatorType("CMS.ControlPanel.SitePage.sectiontitle", testdata.get("SiteName")));
			verifyElementExist(getDynamicLocatorType("CMS.ControlPanel.LeftSideMenu.panel-title_SiteName", testdata.get("SiteName")));

			// Update Site ID to database
			String siteIDstr = getElementText(commonLocators.getLocator("CMS.ControlPanel.SitePage.text_SiteID"), false);
			// String siteID = StringUtils.substringBetween(siteIDstr, "Site ID ", "\n");
			String siteID = StringUtils.substringAfter(siteIDstr, "Site ID ");
			log.comment("New Site ID is: " + siteID);
			// System.out.println("\n\n\n\n\n\n" + siteID + "\n\n\n\n\n\n");

			log.comment("Check new Site displays on Sites table");
			click(commonLocators.getLocator("CMS.ControlPanel.SitePage.sectiontitle_Back"));
			waitForPageToLoad();
			click(commonLocators.getLocator("CMS.ControlPanel.datatable.header-name"));
			waitForPageToLoad();
			if (isElementPresent(commonLocators.getLocator("CMS.ControlPanel.datatable.header-name_asc"))) {
				click(commonLocators.getLocator("CMS.ControlPanel.datatable.header-name_asc"));
				waitForPageToLoad();
			}
			verifyElementExist(getDynamicLocatorType("CMS.ControlPanel.datatable.link-cell_name", testdata.get("SiteName")));
			return siteID;

		} catch (Exception e) {
			log.exception(e);
			log.comment("Method Name", "No Locator", "Script did not complete successfully.", Log.FAIL, Log.MAJOR_ISSUE);
			throw e;
		}
	}

	public void Delete_Site_and_Update_Database(Map<String, String> testdata, DataDriver sqliteData) throws Exception {
		try {
			boolean deleted = Delete_Site(testdata);
			if (deleted) {
				sqliteData.updateDeletedData(DataDriver.datatable.site, "TestCaseName", testdata.get("TestCaseName"));
			}
		} catch (Exception e) {
			log.exception(e);
			log.comment("Method Name", "No Locator", "Script did not complete successfully.", Log.FAIL, Log.MAJOR_ISSUE);
			throw e;
		} finally {
			page.goBackToViewPage();
		}
	}

	protected boolean Delete_Site(Map<String, String> testdata) throws Exception {
		boolean deleted = false;
		try {

			log.comment("**********Site_Delete A Site via Control Panel**********");

			page.goToPage("Control Panel");
			page.openPage(Page.Menu.portal, "Sites");

			click(commonLocators.getLocator("CMS.ControlPanel.datatable.header-name"));
			waitForPageToLoad();
			if (isElementPresent(commonLocators.getLocator("CMS.ControlPanel.datatable.header-name_asc"))) {
				click(commonLocators.getLocator("CMS.ControlPanel.datatable.header-name_asc"));
				waitForPageToLoad();
			}

			verifyElementExist(getDynamicLocatorType("CMS.ControlPanel.datatable.link-cell_name", testdata.get("SiteName")));
			if (isElementPresent(getDynamicLocatorType("CMS.ControlPanel.datatable.link-cell_name", testdata.get("SiteName")))) {
				click(getDynamicLocatorType("CMS.ControlPanel.SitePage.menu-button_Actions", testdata.get("SiteName")));
				click(commonLocators.getLocator("CMS.ControlPanel.SitePage.menu-item_Delete"));
				waitFor(1);
				acceptAlert();

				log.comment("Check if Site page has been deleted.");
				verifyElementNotExist(getDynamicLocatorType("CMS.ControlPanel.datatable.link-cell_name", testdata.get("SiteName")));
				if (!isElementPresent(getDynamicLocatorType("CMS.ControlPanel.datatable.link-cell_name", testdata.get("SiteName")))) {
					deleted = true;
				}

			}
			return deleted;

		} catch (Exception e) {
			log.exception(e);
			log.comment("Method Name", "No Locator", "Script did not complete successfully.", Log.FAIL, Log.MAJOR_ISSUE);
			throw e;
		}

	}

	public void Add_Default_Site_Template(Object[] testDataItem) throws Exception {
		try {

			log.comment("**********Site_Add A default Site Template**********");

			page.goToPage("Control Panel");
			page.openPage(Page.Menu.portal, "Site Templates");
			page.verifyPageTitle("Site Templates");

			click(commonLocators.getLocator("CMS.ControlPanel.SiteTemplate.link_Add"));
			waitForPageToLoad();

			page.verifysectionTitle("New Site Template");

			setText(commonLocators.getLocator("CMS.ControlPanel.SiteTemplate.textbox_NewTemplate"),
					genericTestData.getTestDataValue(testDataItem, "SiteTemplateName"));
			setText(commonLocators.getLocator("CMS.ControlPanel.SiteTemplate.textarea_Description"),
					genericTestData.getTestDataValue(testDataItem, "SiteTemplateDescription"));
			click(commonLocators.getLocator("CMS.ControlPanel.SiteTemplate.button_Save"));
			waitForPageToLoad();

			// Check messages on New Templates page
			page.verifySuccessMessage();

			// Check messages on Sites table
			waitForPageToLoad();
			if (isElementPresent(commonLocators.getLocator("CMS.ControlPanel.datatable.header-name_asc"))) {
				click(commonLocators.getLocator("CMS.ControlPanel.datatable.header-name_asc"));
				waitForPageToLoad();
			}
			verifyElementExist(getDynamicLocatorType("CMS.ControlPanel.datatable.link-cell_name",
					genericTestData.getTestDataValue(testDataItem, "SiteTemplateName")));

		} catch (Exception e) {
			log.exception(e);
			log.comment("Method Name", "No Locator", "Script did not complete successfully.", Log.FAIL, Log.MAJOR_ISSUE);
			throw e;
		} finally {
			page.goBackToViewPage();
		}
	}

	public void Add_Default_Page_Template(String testCaseName) throws Exception {
		try {

			log.comment("**********Site Page_Add A default Page Template**********");

			page.goToPage("Control Panel");
			page.openPage(Page.Menu.portal, "Page Templates");

			page.verifyPageTitle("Page Templates");
			click(commonLocators.getLocator("CMS.ControlPanel.PageTemplate.link_Add"));
			waitForPageToLoad();
			page.verifysectionTitle("New Page Template");

			setText(commonLocators.getLocator("CMS.ControlPanel.PageTemplate.textbox_NewTemplate"), getTestCaseData(testCaseName, "PageTemplateName"));
			setText(commonLocators.getLocator("CMS.ControlPanel.SiteTemplate.textarea_Description"), getTestCaseData(testCaseName, "Remarks"));
			click(commonLocators.getLocator("CMS.ControlPanel.PageTemplate.button_Save"));
			waitForPageToLoad();

			// Check messages on New Templates page
			page.verifySuccessMessage();

			// Check messages on Sites table
			// page.goBack();
			waitForPageToLoad();
			if (isElementPresent(commonLocators.getLocator("CMS.ControlPanel.datatable.header-name_asc"))) {
				click(commonLocators.getLocator("CMS.ControlPanel.datatable.header-name_asc"));
				waitForPageToLoad();
			}
			verifyElementExist(getDynamicLocatorType("CMS.ControlPanel.datatable.link-cell_name", getTestCaseData(testCaseName, "PageTemplateName")));

		} catch (Exception e) {
			log.exception(e);
			log.comment("Method Name", "No Locator", "Script did not complete successfully.", Log.FAIL, Log.MAJOR_ISSUE);
			throw e;
		} finally {
			page.goBackToViewPage();
		}
	}

	public void Edit_Details_of_Site(String testCaseName) throws Exception {
		try {

			log.comment("**********Site_Edit Details of Site**********");

			page.goToPage("Control Panel");
			page.openPage(Page.Menu.portal, "Sites");

			waitForPageToLoad();
			click(commonLocators.getLocator("CMS.ControlPanel.datatable.header-name"));
			waitForPageToLoad();
			if (isElementPresent(commonLocators.getLocator("CMS.ControlPanel.datatable.header-name_asc"))) {
				click(commonLocators.getLocator("CMS.ControlPanel.datatable.header-name_asc"));
				waitForPageToLoad();
			}
			verifyElementExist(getDynamicLocatorType("CMS.ControlPanel.datatable.link-cell_name", getTestCaseData(testCaseName, "SiteName")));
			click(getDynamicLocatorType("CMS.ControlPanel.datatable.link-cell_name", getTestCaseData(testCaseName, "SiteName")));
			waitForPageToLoad();
			page.verifyPageTitle("Site Settings");
			page.verifysectionTitle(getTestCaseData(testCaseName, "SiteName"));
			page.openFrame("Details");

			setText(commonLocators.getLocator("CMS.ControlPanel.SitePage.textbox_NewSite"), getTestCaseData(testCaseName, "NewSiteName"));
			setText(commonLocators.getLocator("CMS.ControlPanel.SiteTemplate.textarea_Description"), getTestCaseData(testCaseName, "Remarks"));
			page.clickButton("Save");
			waitForPageToLoad();
			page.verifySuccessMessage();
			page.goBack();
			verifyElementExist(getDynamicLocatorType("CMS.ControlPanel.datatable.link-cell_name", getTestCaseData(testCaseName, "NewSiteName")));

		} catch (Exception e) {
			log.exception(e);
			log.comment("Method Name", "No Locator", "Script did not complete successfully.", Log.FAIL, Log.MAJOR_ISSUE);
			throw e;
		} finally {
			page.goBackToViewPage();
		}
	}

	public void Edit_Site_Staging_Remote_live(String testCaseName) throws Exception {
		try {

			log.comment("**********Site_Edit [Staging --> Remote live] of Site**********");

			page.goToPage("Control Panel");
			page.openPage(Page.Menu.portal, "Sites");

			waitForPageToLoad();
			click(commonLocators.getLocator("CMS.ControlPanel.datatable.header-name"));
			waitForPageToLoad();
			if (isElementPresent(commonLocators.getLocator("CMS.ControlPanel.datatable.header-name_asc"))) {
				click(commonLocators.getLocator("CMS.ControlPanel.datatable.header-name_asc"));
				waitForPageToLoad();
			}
			verifyElementExist(getDynamicLocatorType("CMS.ControlPanel.datatable.link-cell_name", getTestCaseData(testCaseName, "SiteName")));
			click(getDynamicLocatorType("CMS.ControlPanel.datatable.link-cell_name", getTestCaseData(testCaseName, "SiteName")));
			waitForPageToLoad();
			page.verifyPageTitle("Site Settings");
			page.verifysectionTitle(getTestCaseData(testCaseName, "SiteName"));

			page.openFrame("Details");
			setText(commonLocators.getLocator("CMS.ControlPanel.SitePage.textbox_NewSite"), getTestCaseData(testCaseName, "NewSiteName"));

			page.openFrame("Staging");
			click(commonLocators.getLocator("CMS.ControlPanel.SitePage.radio-button_RemoteLive"));
			setText(commonLocators.getLocator("CMS.ControlPanel.SitePage.textbox_RemoteIP"), getTestCaseData(testCaseName, "RemoteIP"));
			setText(commonLocators.getLocator("CMS.ControlPanel.SitePage.textbox_Port"), getTestCaseData(testCaseName, "RemotePort"));
			setText(commonLocators.getLocator("CMS.ControlPanel.SitePage.textbox_SiteID"), getTestCaseData(testCaseName, "RemoteSiteID"));

			page.clickButton("Save");
			waitForPageToLoad();
			page.verifySuccessMessage();
			page.goBack();
			verifyElementExist(getDynamicLocatorType("CMS.ControlPanel.datatable.link-cell_name", getTestCaseData(testCaseName, "NewSiteName")));

		} catch (Exception e) {
			log.exception(e);
			log.comment("Method Name", "No Locator", "Script did not complete successfully.", Log.FAIL, Log.MAJOR_ISSUE);
			throw e;
		} finally {
			page.goBackToViewPage();
		}
	}

	public void Edit_Site_Staging_Remote_live(Map<String, String> testdata) throws Exception {
		try {

			log.comment("**********Site_Edit [Staging --> Remote live] of Site**********");

			page.goToPage("Control Panel");
			page.openPage(Page.Menu.portal, "Sites");

			waitForPageToLoad();
			click(commonLocators.getLocator("CMS.ControlPanel.datatable.header-name"));
			waitForPageToLoad();
			if (isElementPresent(commonLocators.getLocator("CMS.ControlPanel.datatable.header-name_asc"))) {
				click(commonLocators.getLocator("CMS.ControlPanel.datatable.header-name_asc"));
				waitForPageToLoad();
			}
			verifyElementExist(getDynamicLocatorType("CMS.ControlPanel.datatable.link-cell_name", testdata.get("SiteName")));
			click(getDynamicLocatorType("CMS.ControlPanel.datatable.link-cell_name", testdata.get("SiteName")));
			waitForPageToLoad();
			page.verifyPageTitle("Site Settings");
			page.verifysectionTitle(testdata.get("SiteName"));

			page.openFrame("Staging");
			click(commonLocators.getLocator("CMS.ControlPanel.SitePage.radio-button_RemoteLive"));
			setText(commonLocators.getLocator("CMS.ControlPanel.SitePage.textbox_RemoteIP"), testdata.get("RemoteIP"));
			setText(commonLocators.getLocator("CMS.ControlPanel.SitePage.textbox_Port"), testdata.get("RemotePort"));
			setText(commonLocators.getLocator("CMS.ControlPanel.SitePage.textbox_SiteID"), testdata.get("RemoteSiteID"));

			page.clickButton("Save");
			waitForPageToLoad();
			page.verifySuccessMessage();
			page.goBack();
			verifyElementExist(getDynamicLocatorType("CMS.ControlPanel.datatable.link-cell_name", testdata.get("SiteName")));

		} catch (Exception e) {
			log.exception(e);
			log.comment("Method Name", "No Locator", "Script did not complete successfully.", Log.FAIL, Log.MAJOR_ISSUE);
			throw e;
		} finally {
			page.goBackToViewPage();
		}
	}

	public void Add_Page_via_Menu_Bar(String testCaseName) throws Exception {
		try {

			log.comment("**********Site Page_Add Blog/Content Display/Wiki page via Menu Bar**********");

			page.goToPage(getTestCaseData(testCaseName, "SiteName"));
			page.addPortlet("Page");
			setText(commonLocators.getLocator("CMS.ViewPage.NavigationBar.textbox_PageName"), getTestCaseData(testCaseName, "PageName"));
			click(getDynamicLocatorType("CMS.ViewPage.NavigationBar.radio-button_template", getTestCaseData(testCaseName, "Template")));
			click(commonLocators.getLocator("CMS.ViewPage.NavigationBar.button_save"));
			waitFor(2);
			page.verifyPageName(getTestCaseData(testCaseName, "PageName"));
			// verifyElementExist(getDynamicLocatorType("CMS.ViewPage.NavigationBar.text_PageName",
			// getTestCaseData(testCaseName, "PageName")));

		} catch (Exception e) {
			log.exception(e);
			log.comment("Method Name", "No Locator", "Script did not complete successfully.", Log.FAIL, Log.MAJOR_ISSUE);
			throw e;
		}
	}

	public void Add_Blank_Page_via_Menu_Bar(String testCaseName) throws Exception {
		try {

			log.comment("**********Site Page_Add Blank page via Menu Bar**********");

			page.goToPage(getTestCaseData(testCaseName, "SiteName"));
			page.addPortlet("Page");
			setText(commonLocators.getLocator("CMS.ViewPage.NavigationBar.textbox_PageName"), getTestCaseData(testCaseName, "PageName"));
			click(commonLocators.getLocator("CMS.ViewPage.NavigationBar.button_save"));
			waitFor(2);
			page.verifyPageName(getTestCaseData(testCaseName, "PageName"));
			// verifyElementExist(getDynamicLocatorType("CMS.ViewPage.NavigationBar.text_PageName",
			// getTestCaseData(testCaseName, "PageName")));

		} catch (Exception e) {
			log.exception(e);
			log.comment("Method Name", "No Locator", "Script did not complete successfully.", Log.FAIL, Log.MAJOR_ISSUE);
			throw e;
		}
	}

	public void Add_Page_via_Control_Panel(String testCaseName) throws Exception {
		try {

			log.comment("**********Site Page_Add Blog page/Content Display Page/Wiki Page via Control Panel**********");

			page.goToPage("Control Panel");
			page.setCurrentSite(getTestCaseData(testCaseName, "SiteName"));
			page.openPage(Page.Menu.content, "Site Pages");
			// page.verifyPageTitle("Site Pages");
			// page.verifysectionTitle(getTestCaseData(testCaseName, "SiteName"));

			click(commonLocators.getLocator("CMS.ControlPanel.SitePages.button_AddPage"));
			waitForPageToLoad();
			// verifyElementExist(commonLocators.getLocator("CMS.ControlPanel.AddPageDialog.title_AddPage"));
			verifyElementExist(getDynamicLocatorType("CMS.ControlPanel.AddPageDialog.title_AddPage", "Add Page"));
			setText(commonLocators.getLocator("CMS.ControlPanel.AddPageDialog.textbox_PageName"), getTestCaseData(testCaseName, "PageName"));
			if (!(getTestCaseData(testCaseName, "Template") == null)) {
				select(commonLocators.getLocator("CMS.ControlPanel.AddPageDialog.select_Template"), getTestCaseData(testCaseName, "Template"));
			} else if (!(getTestCaseData(testCaseName, "Type") == null)) {
				select(commonLocators.getLocator("CMS.ControlPanel.AddPageDialog.select_Type"), getTestCaseData(testCaseName, "Type"));
			}
			click(commonLocators.getLocator("CMS.ControlPanel.AddPageDialog.button_Add"));
			waitForPageToLoad();
			page.verifySuccessMessage();
			verifyElementExist(getDynamicLocatorType("CMS.ControlPanel.SitePages.text_PageName", getTestCaseData(testCaseName, "PageName")));

		} catch (Exception e) {
			log.exception(e);
			log.comment("Method Name", "No Locator", "Script did not complete successfully.", Log.FAIL, Log.MAJOR_ISSUE);
			throw e;
		} finally {
			page.goBackToViewPage();
		}
	}

	public void Add_Page_via_Control_Panel(Map<String, String> testdata) throws Exception {
		try {

			log.comment("**********Site Page_Add Blog page/Content Display Page/Wiki Page via Control Panel**********");

			page.goToPage("Control Panel");
			page.setCurrentSite(testdata.get("Site"));
			page.openPage(Page.Menu.content, "Site Pages");
			// page.verifyPageTitle("Site Pages");
			// page.verifysectionTitle(testdata.get("Site"));

			click(commonLocators.getLocator("CMS.ControlPanel.SitePages.button_AddPage"));
			waitForPageToLoad();
			// verifyElementExist(commonLocators.getLocator("CMS.ControlPanel.AddPageDialog.title_AddPage"));
			verifyElementExist(getDynamicLocatorType("CMS.ControlPanel.AddPageDialog.title_AddPage", "Add Page"));
			setText(commonLocators.getLocator("CMS.ControlPanel.AddPageDialog.textbox_PageName"), testdata.get("PageName"));
			if (!(testdata.get("Template") == null)) {
				select(commonLocators.getLocator("CMS.ControlPanel.AddPageDialog.select_Template"), testdata.get("Template"));
			} else if (!(testdata.get("Type") == null)) {
				select(commonLocators.getLocator("CMS.ControlPanel.AddPageDialog.select_Type"), testdata.get("Type"));
			}
			click(commonLocators.getLocator("CMS.ControlPanel.AddPageDialog.button_Add"));
			waitForPageToLoad();
			page.verifySuccessMessage();
			verifyElementExist(getDynamicLocatorType("CMS.ControlPanel.SitePages.text_PageName", testdata.get("PageName")));

		} catch (Exception e) {
			log.exception(e);
			log.comment("Method Name", "No Locator", "Script did not complete successfully.", Log.FAIL, Log.MAJOR_ISSUE);
			throw e;
		} finally {
			page.goBackToViewPage();
		}
	}

	public void Add_Folder(Map<String, String> testdata) throws Exception {
		try {

			log.comment("**********Documents and Media_Folder_Add folder**********");

			page.goToPage("Control Panel");
			page.setCurrentSite(testdata.get("Site"));
			page.openPage(Page.Menu.content, "Documents and Media");
			page.verifyPageTitle("Documents and Media");
			gotoFolder(testdata.get("ParentFolder"));

			if ("Home".equals(testdata.get("ParentFolder"))) {
				click(commonLocators.getLocator("CMS.ControlPanel.DocAndLib.button_Add"));
				click(commonLocators.getLocator("CMS.DocAndLib.AddMenu.menuitem_Folder"));

			} else {
				// String[] parentFolder = testdata.get("Folder").split("#");
				// for (int i = 0; i < parentFolder.length; i++) {
				// click(getDynamicLocatorType("CMS.ControlPanel.datatable.link-cell_title", parentFolder[i]));
				// waitFor(2);
				// waitForPageToLoad();
				// }
				click(commonLocators.getLocator("CMS.ControlPanel.DocAndLib.button_Add"));
				click(commonLocators.getLocator("CMS.DocAndLib.AddMenu.menuitem_Subfolder"));

			}

			waitForPageToLoad();
			page.verifysectionTitle("New Folder");
			setText(commonLocators.getLocator("CMS.ControlPanel.DocAndLib.textbox_FolderName"), testdata.get("FolderName"));
			setText(commonLocators.getLocator("CMS.ControlPanel.DocAndLib.textarea_Description"), testdata.get("FolderDescription"));
			click(commonLocators.getLocator("CMS.ControlPanel.DocAndLib.button_Save"));

			page.verifySuccessMessage();
			verifyElementExist(getDynamicLocatorType("CMS.ControlPanel.datatable.span-cell_title", testdata.get("FolderName")));

		} catch (Exception e) {
			log.exception(e);
			log.comment("Method Name", "No Locator", "Script did not complete successfully.", Log.FAIL, Log.MAJOR_ISSUE);
			throw e;
		} finally {
			page.goBackToViewPage();
		}
	}

	public void Add_Basic_Document_via_Control_Panel(Map<String, String> testdata) throws Exception {
		try {

			log.comment("**********Documents and Media_Add a basic document in a folder via Control Pannel**********");

			page.goToPage("Control Panel");
			page.setCurrentSite(testdata.get("Site"));
			page.openPage(Page.Menu.content, "Documents and Media");
			page.verifyPageTitle("Documents and Media");
			// click(commonLocators.getLocator("CMS.ControlPanel.DocAndLib.button_ListView"));
			// waitForPageToLoad();
			gotoFolder(testdata.get("Folder"));

			click(commonLocators.getLocator("CMS.ControlPanel.DocAndLib.button_Add"));
			click(commonLocators.getLocator("CMS.DocAndLib.AddMenu.menuitem_BasicDocument"));
			waitForPageToLoad();
			waitFor(1);
			page.verifysectionTitle("New Document");

			// forceClick(commonLocators.getLocator("CMS.ControlPanel.DocAndLib.textbox_DocumentFile"));
			// setText(commonLocators.getLocator("CMS.ControlPanel.DocAndLib.textbox_DocumentFile"), getTestDataPath(testdata.get("FileName")));
			// click(commonLocators.getLocator("CMS.ControlPanel.DocAndLib.textbox_DocumentFile"));
			// page.mouseOverHelper(commonLocators.getLocator("CMS.ControlPanel.DocAndLib.textbox_DocumentFile").getValue());
			// doubleClick(commonLocators.getLocator("CMS.ControlPanel.DocAndLib.textbox_DocumentFile"));
			// page.UploadFile("firefox", getTestDataPath(testdata.get("FileName")));
			getBrowser().getSelenium().type(commonLocators.getLocator("CMS.ControlPanel.DocAndLib.textbox_DocumentFile").getValue(),
					getTestDataPath(testdata.get("FileName")));

			setText(commonLocators.getLocator("CMS.ControlPanel.DocAndLib.textbox_DocumentTitle"), testdata.get("BasicDocName"));
			setText(commonLocators.getLocator("CMS.ControlPanel.DocAndLib.textarea_Description"), testdata.get("DocDescription"));
			click(commonLocators.getLocator("CMS.ControlPanel.DocAndLib.button_Publish"));
			waitFor(5);

			page.verifySuccessMessage();
			verifyElementExist(getDynamicLocatorType("CMS.ControlPanel.datatable.link-cell_title", testdata.get("BasicDocName")));

		} catch (Exception e) {
			log.exception(e);
			log.comment("Method Name", "No Locator", "Script did not complete successfully.", Log.FAIL, Log.MAJOR_ISSUE);
			throw e;
		} finally {
			page.goBackToViewPage();
		}
	}

	public void Check_Out_Document(Map<String, String> testdata) throws Exception {

		try {

			log.comment("**********Documents and Media_Check Out**********");

			page.goToPage("Control Panel");
			page.setCurrentSite(testdata.get("Site"));
			page.openPage(Page.Menu.content, "Documents and Media");
			// page.verifyPageTitle("Documents and Media");
			gotoFolder(testdata.get("Folder"));

			verifyElementExist(getDynamicLocatorType("CMS.ControlPanel.datatable.link-cell_title", testdata.get("BasicDocName")));
			click(getDynamicLocatorType("CMS.ControlPanel.datatable.link-cell_title", testdata.get("BasicDocName")));
			waitForPageToLoad();

			page.verifysectionTitle(testdata.get("BasicDocName"));
			click(commonLocators.getLocator("CMS.ControlPanel.DocAndLib.button_Checkout"));
			waitFor(3);
			waitForPageToLoad();

			page.verifySuccessMessage();
			page.verifyCheckOutMessage();
			verifyElementNotExist(commonLocators.getLocator("CMS.ControlPanel.DocAndLib.button_Checkout"));
			verifyElementExist(commonLocators.getLocator("CMS.ControlPanel.DocAndLib.button_Checkin"));
			verifyElementExist(commonLocators.getLocator("CMS.ControlPanel.DocAndLib.button_CancelCheckout"));
			captureScreenShot();

			page.goBack();
			verifyElementExist(getDynamicLocatorType("CMS.ControlPanel.DocAndLib.text_Draft", testdata.get("BasicDocName")));
			captureScreenShot();

		} catch (Exception e) {
			log.exception(e);
			log.comment("Method Name", "No Locator", "Script did not complete successfully.", Log.FAIL, Log.MAJOR_ISSUE);
			throw e;
		} finally {
			page.goBackToViewPage();
		}
	}

	public void Cancel_Check_Out_Document(Map<String, String> testdata) throws Exception {

		try {

			log.comment("**********Documents and Media_Cancel Check Out**********");

			page.goToPage("Control Panel");
			page.setCurrentSite(testdata.get("Site"));
			page.openPage(Page.Menu.content, "Documents and Media");
			// page.verifyPageTitle("Documents and Media");
			gotoFolder(testdata.get("Folder"));

			verifyElementExist(getDynamicLocatorType("CMS.ControlPanel.datatable.link-cell_title", testdata.get("BasicDocName")));
			click(getDynamicLocatorType("CMS.ControlPanel.datatable.link-cell_title", testdata.get("BasicDocName")));
			waitForPageToLoad();

			page.verifysectionTitle(testdata.get("BasicDocName"));
			click(commonLocators.getLocator("CMS.ControlPanel.DocAndLib.button_CancelCheckout"));
			waitFor(3);
			waitForPageToLoad();

			page.verifySuccessMessage();
			verifyElementExist(commonLocators.getLocator("CMS.ControlPanel.DocAndLib.button_Checkout"));
			verifyElementNotExist(commonLocators.getLocator("CMS.ControlPanel.DocAndLib.button_Checkin"));
			verifyElementNotExist(commonLocators.getLocator("CMS.ControlPanel.DocAndLib.button_CancelCheckout"));
			captureScreenShot();

			page.goBack();
			verifyElementNotExist(getDynamicLocatorType("CMS.ControlPanel.DocAndLib.text_Draft", testdata.get("BasicDocName")));
			captureScreenShot();

		} catch (Exception e) {
			log.exception(e);
			log.comment("Method Name", "No Locator", "Script did not complete successfully.", Log.FAIL, Log.MAJOR_ISSUE);
			throw e;
		} finally {
			page.goBackToViewPage();
		}
	}

	public void Check_In_Document(Map<String, String> testdata) throws Exception {

		try {

			log.comment("**********Documents and Media_Check In**********");

			page.goToPage("Control Panel");
			page.setCurrentSite(testdata.get("Site"));
			page.openPage(Page.Menu.content, "Documents and Media");
			// page.verifyPageTitle("Documents and Media");
			gotoFolder(testdata.get("Folder"));

			verifyElementExist(getDynamicLocatorType("CMS.ControlPanel.datatable.link-cell_title", testdata.get("BasicDocName")));
			click(getDynamicLocatorType("CMS.ControlPanel.datatable.link-cell_title", testdata.get("BasicDocName")));
			waitForPageToLoad();

			page.verifysectionTitle(testdata.get("BasicDocName"));
			click(commonLocators.getLocator("CMS.ControlPanel.DocAndLib.button_Checkin"));
			waitFor(3);
			waitForPageToLoad();

			page.verifySuccessMessage();
			verifyElementExist(commonLocators.getLocator("CMS.ControlPanel.DocAndLib.button_Checkout"));
			verifyElementNotExist(commonLocators.getLocator("CMS.ControlPanel.DocAndLib.button_Checkin"));
			verifyElementNotExist(commonLocators.getLocator("CMS.ControlPanel.DocAndLib.button_CancelCheckout"));
			captureScreenShot();

			page.goBack();
			verifyElementNotExist(getDynamicLocatorType("CMS.ControlPanel.DocAndLib.text_Draft", testdata.get("BasicDocName")));
			captureScreenShot();

		} catch (Exception e) {
			log.exception(e);
			log.comment("Method Name", "No Locator", "Script did not complete successfully.", Log.FAIL, Log.MAJOR_ISSUE);
			throw e;
		} finally {
			page.goBackToViewPage();
		}
	}

	public boolean Delte_Document(Map<String, String> testdata) throws Exception {
		boolean deleted = false;

		try {

			log.comment("**********Documents and Media_Delete Document**********");

			page.goToPage("Control Panel");
			page.setCurrentSite(testdata.get("Site"));
			page.openPage(Page.Menu.content, "Documents and Media");
			page.verifyPageTitle("Documents and Media");
			gotoFolder(testdata.get("Folder"));

			verifyElementExist(getDynamicLocatorType("CMS.ControlPanel.datatable.link-cell_title", testdata.get("BasicDocName")));
			click(getDynamicLocatorType("CMS.ControlPanel.datatable.link-cell_title", testdata.get("BasicDocName")));
			waitForPageToLoad();

			page.verifysectionTitle(testdata.get("BasicDocName"));
			click(commonLocators.getLocator("CMS.ControlPanel.DocAndLib.button_Delete"));
			waitFor(2);

			acceptAlert();
			waitFor(3);
			waitForPageToLoad();

			page.verifySuccessMessage();
			verifyElementNotExist(getDynamicLocatorType("CMS.ControlPanel.datatable.link-cell_title", testdata.get("BasicDocName")));
			deleted = !isElementExistHelper(getDynamicLocatorType("CMS.ControlPanel.datatable.link-cell_title", testdata.get("BasicDocName"))
					.getValue());
			return deleted;
		} catch (Exception e) {
			log.exception(e);
			log.comment("Method Name", "No Locator", "Script did not complete successfully.", Log.FAIL, Log.MAJOR_ISSUE);
			throw e;
		} finally {
			page.goBackToViewPage();
		}
	}

	public void Add_Web_Content_via_Control_Panel(Map<String, String> testdata) throws Exception {
		try {

			log.comment("**********Web Content_Add web content via Control Pannel**********");

			page.goToPage("Control Panel");
			page.setCurrentSite(testdata.get("Site"));
			page.openPage(Page.Menu.content, "Web Content");
			page.verifyPageTitle("Web Content");

			click(commonLocators.getLocator("CMS.ControlPanel.WebContent.tab-label_WebContent"));
			// click(commonLocators.getLocator("CMS.ControlPanel.WebContent.button_Add"));
			click(commonLocators.getLocator("CMS.ControlPanel.WebContent.menu-button_Add"));
			click(commonLocators.getLocator("CMS.ControlPanel.WebContent.menuitem_Basic"));
			waitFor(2);
			waitForPageToLoad();
			page.verifysectionTitle("New Web Content");
			setText(commonLocators.getLocator("CMS.ControlPanel.WebContent.textbox_Title"), testdata.get("WebContentName"));

			String winhndl = getBrowser().getWebDriver().getWindowHandle();
			click(commonLocators.getLocator("CMS.ControlPanel.WebContent.frame_Content"));
			String frameid = driver.findElement(By.xpath("//iframe[contains(@title,'Rich text editor')]")).getAttribute("id");
			driver.switchTo().frame(frameid);
			// driver.switchTo().activeElement();
			WebElement editable = driver.switchTo().activeElement();
			// editable.sendKeys(testdata.get("Content"));
			((JavascriptExecutor) driver).executeScript("document.body.innerHTML = '<p>" + testdata.get("Content") + "</p>'");
			waitFor(1);
			driver.switchTo().window(winhndl);
			click(commonLocators.getLocator("CMS.ControlPanel.WebContent.button_Source"));
			click(commonLocators.getLocator("CMS.ControlPanel.WebContent.button_Source"));

			if (!(testdata.get("Image") == null)) {
				Add_Image_to_Web_Content(testdata);
			}

			page.clickButton("Publish");
			waitFor(5);
			waitForPageToLoad();

			// String winhndl = getBrowser().getWebDriver().getWindowHandle();
			// String frameid = driver.findElement(By.xpath("//iframe[contains(@title,'Rich text editor')]")).getAttribute("id");
			// driver.switchTo().frame(frameid);
			// driver.switchTo().activeElement();
			// WebElement editable = getBrowser().getWebDriver().switchTo().activeElement();
			// ((JavascriptExecutor) driver).executeScript("document.body.innerHTML = '<p>sfdsfsa</p>'");
			// // ((JavascriptExecutor) driver).executeScript("document.write(\"Hello World!\")");
			// editable.sendKeys(testdata.get("WebContentName"));
			//
			// // setText(commonLocators.getLocator("CMS.ControlPanel.WebContent.textarea_Content"), testdata.get("TextContent"));
			// // setText(commonLocators.getLocator("CMS.ControlPanel.WebContent.frame_Content"), testdata.get("TextContent"));
			// // click(commonLocators.getLocator("CMS.ControlPanel.WebContent.frame_Content"));
			// // getBrowser().getSelenium().type(commonLocators.getLocator("CMS.ControlPanel.WebContent.body_Content").getValue(),
			// // testdata.get("WebContentName"));
			//
			// // getBrowser().getSelenium().type("//html/body/p", testdata.get("TextContent"));
			//
			// click(commonLocators.getLocator("CMS.ControlPanel.WebContent.button_Source"));
			// click(commonLocators.getLocator("CMS.ControlPanel.WebContent.textarea_Content"));
			// // getBrowser().getSelenium().runScript("CKeditorAPI.GetInstance('propJournal.content').SetHTML('hello, world')");
			// ((JavascriptExecutor) driver).executeScript("CKEDITOR.instances.editor1.insertText( 'hello xxxxx' );");
			// getBrowser().getSelenium().runScript("CKEDITOR.instances['aui_3_4_0_1_4398'].setData('<p>xxxxxxxxxx</p>');");
			// getBrowser().getSelenium().type(commonLocators.getLocator("CMS.ControlPanel.WebContent.textarea_Content").getValue(),
			// testdata.get("TextContent"));
			// driver.findElement(By.xpath(commonLocators.getLocator("CMS.ControlPanel.WebContent.textarea_Content").getValue())).sendKeys(
			// testdata.get("TextContent"));
			//
			// // CKEDITOR.tools.callFunction(49)
			//
			// setText(commonLocators.getLocator("CMS.ControlPanel.WebContent.textarea_Content"), testdata.get("TextContent"));
			//
			// page.clickButton("Publish");
			// waitFor(5);
			// waitForPageToLoad();
			// getBrowser().getWebDriver().switchTo().window(winhndl);

			page.verifySuccessMessage();
			verifyElementExist(getDynamicLocatorType("CMS.ControlPanel.datatable.link-cell_name", testdata.get("WebContentName")));
			verifyElementExist(getDynamicLocatorType("CMS.ControlPanel.datatable.link-cell_status", testdata.get("WebContentName"),
					testdata.get("WebContentName"), "Approved"));

		} catch (Exception e) {
			log.exception(e);
			log.comment("Method Name", "No Locator", "Script did not complete successfully.", Log.FAIL, Log.MAJOR_ISSUE);
			throw e;
		} finally {
			page.goBackToViewPage();
		}
	}

	public void Add_Image_to_Web_Content(Map<String, String> testdata) throws Exception {
		try {
			log.comment("**********Web Content_Add Image to web content**********");

			String winhndl = getBrowser().getWebDriver().getWindowHandle();
			click(commonLocators.getLocator("CMS.ControlPanel.WebContent.button_Image"));
			waitFor(3);
			click(commonLocators.getLocator("CMS.ControlPanel.WebContent.button_BrowseServer"));
			waitFor(5);
			waitForPageToLoad();
			getWindow("Resources Browser");
			// driver.switchTo().window("Resources Browser - Mozilla Firefox");
			driver.switchTo().frame("frmResourcesList");
			click(getDynamicLocatorType("CMS.ControlPanel.ResourcesBrowser.link_Folder", testdata.get("Site")));
			waitFor(3);
			String[] folders = testdata.get("Image").split("#");
			for (int i = 0; i < folders.length; i++) {
				click(getDynamicLocatorType("CMS.ControlPanel.ResourcesBrowser.link_Folder", folders[i]));
				waitFor(3);
				waitForPageToLoad();
			}
			driver.switchTo().window(winhndl);
			driver.switchTo().defaultContent();
			click(commonLocators.getLocator("CMS.ControlPanel.WebContent.button_OK"));
			waitFor(5);
			waitForPageToLoad();

		} catch (Exception e) {
			log.exception(e);
			log.comment("Method Name", "No Locator", "Script did not complete successfully.", Log.FAIL, Log.MAJOR_ISSUE);
			throw e;
		}
	}

	public void Add_Draft_Web_Content_via_Control_Panel(Map<String, String> testdata) throws Exception {
		try {

			log.comment("**********Web Content_Create a draft Content via Control Pannel**********");

			page.goToPage("Control Panel");
			page.setCurrentSite(testdata.get("Site"));
			page.openPage(Page.Menu.content, "Web Content");
			page.verifyPageTitle("Web Content");

			click(commonLocators.getLocator("CMS.ControlPanel.WebContent.tab-label_WebContent"));
			// click(commonLocators.getLocator("CMS.ControlPanel.WebContent.button_Add"));
			click(commonLocators.getLocator("CMS.ControlPanel.WebContent.menu-button_Add"));
			click(commonLocators.getLocator("CMS.ControlPanel.WebContent.menuitem_Basic"));
			waitFor(2);
			waitForPageToLoad();
			page.verifysectionTitle("New Web Content");
			setText(commonLocators.getLocator("CMS.ControlPanel.WebContent.textbox_Title"), testdata.get("WebContentName"));

			String winhndl = getBrowser().getWebDriver().getWindowHandle();
			click(commonLocators.getLocator("CMS.ControlPanel.WebContent.frame_Content"));
			String frameid = driver.findElement(By.xpath("//iframe[contains(@title,'Rich text editor')]")).getAttribute("id");
			driver.switchTo().frame(frameid);
			// driver.switchTo().activeElement();
			WebElement editable = driver.switchTo().activeElement();
			// editable.sendKeys(testdata.get("Content"));
			((JavascriptExecutor) driver).executeScript("document.body.innerHTML = '<p>" + testdata.get("Content") + "</p>'");
			waitFor(1);
			driver.switchTo().window(winhndl);
			click(commonLocators.getLocator("CMS.ControlPanel.WebContent.button_Source"));
			click(commonLocators.getLocator("CMS.ControlPanel.WebContent.button_Source"));

			if (!(testdata.get("Image") == null)) {
				Add_Image_to_Web_Content(testdata);
			}

			// setText(commonLocators.getLocator("CMS.ControlPanel.WebContent.textarea_Content"), testdata.get("TextContent"));
			// setText(commonLocators.getLocator("CMS.ControlPanel.WebContent.frame_Content"), testdata.get("TextContent"));
			// click(commonLocators.getLocator("CMS.ControlPanel.WebContent.frame_Content"));
			// getBrowser().getSelenium().type(commonLocators.getLocator("CMS.ControlPanel.WebContent.body_Content").getValue(),
			// testdata.get("WebContentName"));

			// String winhndl = getBrowser().getWebDriver().getWindowHandle();
			// getBrowser().getWebDriver().switchTo().frame(0);
			// WebElement editable = getBrowser().getWebDriver().switchTo().activeElement();
			// ((JavascriptExecutor) driver).executeScript("document.body.innerHTML = '<br>'");
			// editable.sendKeys(testdata.get("WebContentName"));
			// getBrowser().getSelenium().type("//html/body", testdata.get("WebContentName"));

			// click(commonLocators.getLocator("CMS.ControlPanel.WebContent.button_Source"));
			// click(commonLocators.getLocator("CMS.ControlPanel.WebContent.textarea_Content"));
			//
			// // getBrowser().getSelenium().runScript("CKeditorAPI.GetInstance('propJournal.content').SetHTML('hello, world')");
			// ((JavascriptExecutor) driver).executeScript("CKEDITOR.instances.editor1.insertText( 'hello xxxxx' );");
			// getBrowser().getSelenium().runScript("CKEDITOR.instances['aui_3_4_0_1_4398'].setData('<p>xxxxxxxxxx</p>');");
			// getBrowser().getSelenium().type(commonLocators.getLocator("CMS.ControlPanel.WebContent.textarea_Content").getValue(),
			// testdata.get("TextContent"));
			// driver.findElement(By.xpath(commonLocators.getLocator("CMS.ControlPanel.WebContent.textarea_Content").getValue())).sendKeys(
			// testdata.get("TextContent"));
			//
			// setText(commonLocators.getLocator("CMS.ControlPanel.WebContent.textarea_Content"), testdata.get("TextContent"));
			// getBrowser().getWebDriver().switchTo().window(winhndl);

			page.clickButton("Save as Draft");
			waitFor(5);
			waitForPageToLoad();
			page.verifySuccessMessage();
			page.goBack();
			waitForPageToLoad();
			verifyElementExist(getDynamicLocatorType("CMS.ControlPanel.datatable.link-cell_name", testdata.get("WebContentName")));
			verifyElementExist(getDynamicLocatorType("CMS.ControlPanel.datatable.link-cell_status", testdata.get("WebContentName"),
					testdata.get("WebContentName"), "Draft"));

		} catch (Exception e) {
			log.exception(e);
			log.comment("Method Name", "No Locator", "Script did not complete successfully.", Log.FAIL, Log.MAJOR_ISSUE);
			throw e;
		} finally {
			page.goBackToViewPage();
		}
	}

	public void Add_Tag_via_Tags_Page(Map<String, String> testdata) throws Exception {
		try {

			log.comment("**********Tags_Add Tag via Tags Page**********");

			page.goToPage("Control Panel");
			page.setCurrentSite(testdata.get("Site"));
			page.openPage(Page.Menu.content, "Tags");

			click(commonLocators.getLocator("CMS.ControlPanel.Tags.button_AddTag"));
			waitForPageToLoad();

			verifyElementExist(commonLocators.getLocator("CMS.ControlPanel.AddTagDialog.title_AddTag"));
			setText(commonLocators.getLocator("CMS.ControlPanel.AddTagDialog.textbox_TagName"), testdata.get("TagName"));
			click(commonLocators.getLocator("CMS.ControlPanel.AddTagDialog.button_Save"));
			waitFor(1);
			waitForPageToLoad();
			page.verifySuccessMessage();
			verifyElementExist(getDynamicLocatorType("CMS.ControlPanel.Tags.text_TagName", testdata.get("TagName")));

		} catch (Exception e) {
			log.exception(e);
			log.comment("Method Name", "No Locator", "Script did not complete successfully.", Log.FAIL, Log.MAJOR_ISSUE);
			throw e;
		} finally {
			page.goBackToViewPage();
		}
	}

	public boolean Delete_Tag_via_Tags_Page(Map<String, String> testdata) throws Exception {
		boolean deleted = false;
		try {

			log.comment("**********Tags_Delete tag via Tags Page**********");

			page.goToPage("Control Panel");
			page.setCurrentSite(testdata.get("Site"));
			page.openPage(Page.Menu.content, "Tags");

			verifyElementExist(getDynamicLocatorType("CMS.ControlPanel.Tags.text_TagName", testdata.get("TagName")));
			click(getDynamicLocatorType("CMS.ControlPanel.Tags.text_TagName", testdata.get("TagName")));
			// setCheck(getDynamicLocatorType("CMS.ControlPanel.Tags.checkbox_Tag", testdata.get("TagName").toLowerCase()), true);
			click(getDynamicLocatorType("CMS.ControlPanel.Tags.checkbox_Tag", testdata.get("TagName")));
			waitFor(2);

			click(commonLocators.getLocator("CMS.ControlPanel.Tags.button_Actions"));
			waitFor(1);
			click(commonLocators.getLocator("CMS.ControlPanel.Tags.menuitem_Delete"));
			waitFor(2);
			captureScreenShot();

			acceptAlert();
			waitFor(3);
			captureScreenShot();

			page.verifySuccessMessage();
			verifyElementNotExist(getDynamicLocatorType("CMS.ControlPanel.Tags.text_TagName", testdata.get("TagName")));
			deleted = !isElementExistHelper(getDynamicLocatorType("CMS.ControlPanel.Tags.text_TagName", testdata.get("TagName")).getValue());
			return deleted;

		} catch (Exception e) {
			log.exception(e);
			log.comment("Method Name", "No Locator", "Script did not complete successfully.", Log.FAIL, Log.MAJOR_ISSUE);
			throw e;
		} finally {
			page.goBackToViewPage();
		}
	}

	public void Add_Vocabulary(Map<String, String> testdata) throws Exception {
		try {

			log.comment("**********Categories_Add Vocabulary**********");

			page.goToPage("Control Panel");
			page.setCurrentSite(testdata.get("Site"));
			page.openPage(Page.Menu.content, "Categories");
			page.verifyPageTitle("Categories");

			click(commonLocators.getLocator("CMS.ControlPanel.Categories.button_AddVocabulary"));
			waitForPageToLoad();

			verifyElementExist(commonLocators.getLocator("CMS.ControlPanel.AddVocabularyDialog.title_AddVocabulary"));
			setText(commonLocators.getLocator("CMS.ControlPanel.AddVocabularyDialog.textbox_VocabularyName"), testdata.get("VocabularyName"));
			setText(commonLocators.getLocator("CMS.ControlPanel.AddVocabularyDialog.textarea_VocDescription"), testdata.get("VocDescription"));
			click(commonLocators.getLocator("CMS.ControlPanel.AddCategoryDialog.button_Save"));
			waitFor(3);
			waitForPageToLoad();
			page.verifySuccessMessage();
			verifyElementExist(getDynamicLocatorType("CMS.ControlPanel.Categories.text_VocabularyName", testdata.get("VocabularyName")));

		} catch (Exception e) {
			log.exception(e);
			log.comment("Method Name", "No Locator", "Script did not complete successfully.", Log.FAIL, Log.MAJOR_ISSUE);
			throw e;
		} finally {
			page.goBackToViewPage();
		}
	}

	public void Add_Category(Map<String, String> testdata) throws Exception {
		try {

			log.comment("**********Categories_Add Category**********");

			page.goToPage("Control Panel");
			page.setCurrentSite(testdata.get("Site"));
			page.openPage(Page.Menu.content, "Categories");
			page.verifyPageTitle("Categories");

			if (isElementExistHelper(getDynamicLocatorType("CMS.ControlPanel.Categories.text_VocabularyName", testdata.get("Vocabulary")).getValue())) {
				click(getDynamicLocatorType("CMS.ControlPanel.Categories.text_VocabularyName", testdata.get("Vocabulary")));
			} else {
				Add_Vocabulary(testdata);
			}

			click(commonLocators.getLocator("CMS.ControlPanel.Categories.button_AddCategory"));
			waitForPageToLoad();

			verifyElementExist(commonLocators.getLocator("CMS.ControlPanel.AddCategoryDialog.title_AddCategory"));
			setText(commonLocators.getLocator("CMS.ControlPanel.AddCategoryDialog.textbox_CategoryName"), testdata.get("CategoryName"));
			setText(commonLocators.getLocator("CMS.ControlPanel.AddCategoryDialog.textarea_CatDescription"), testdata.get("CatDescription"));
			click(commonLocators.getLocator("CMS.ControlPanel.AddCategoryDialog.button_Save"));
			waitFor(3);
			waitForPageToLoad();
			page.verifySuccessMessage();
			verifyElementExist(getDynamicLocatorType("CMS.ControlPanel.Categories.text_CategoryName", testdata.get("CategoryName")));

		} catch (Exception e) {
			log.exception(e);
			log.comment("Method Name", "No Locator", "Script did not complete successfully.", Log.FAIL, Log.MAJOR_ISSUE);
			throw e;
		} finally {
			page.goBackToViewPage();
		}
	}

	public void Categorize_Document(Map<String, String> testdata) throws Exception {
		try {

			log.comment("**********Documents and Media_Categorize Document**********");

			page.goToPage("Control Panel");
			page.setCurrentSite(testdata.get("Site"));
			page.openPage(Page.Menu.content, "Documents and Media");
			page.verifyPageTitle("Documents and Media");
			gotoFolder(testdata.get("Folder"));

			verifyElementExist(getDynamicLocatorType("CMS.ControlPanel.datatable.link-cell_title", testdata.get("BasicDocName")));
			click(getDynamicLocatorType("CMS.ControlPanel.datatable.link-cell_title", testdata.get("BasicDocName")));
			waitForPageToLoad();

			page.verifysectionTitle(testdata.get("BasicDocName"));
			click(commonLocators.getLocator("CMS.ControlPanel.DocAndLib.button_Edit"));
			waitForPageToLoad();

			verifyElementExist(getDynamicLocatorType("CMS.ControlPanel.DocAndLib.text_Vocabulary", testdata.get("Vocabulary")));
			Select_Category(testdata.get("Vocabulary"), testdata.get("CategoryName"));
			// if (isElementPresent(getDynamicLocatorType("CMS.ControlPanel.DocAndLib.text_Vocabulary", testdata.get("Vocabulary")))) {
			// click(getDynamicLocatorType("CMS.ControlPanel.DocAndLib.button_Select", testdata.get("Vocabulary")));
			// waitFor(2);
			// waitForPageToLoad();
			// verifyElementExist(commonLocators.getLocator("CMS.ControlPanel.CategoriesDialog.title_Categories"));
			// click(getDynamicLocatorType("CMS.ControlPanel.CategoriesDialog.weblist_Category", testdata.get("Category")));
			// click(commonLocators.getLocator("CMS.ControlPanel.CategoriesDialog.button_Close"));
			// verifyElementExist(getDynamicLocatorType("CMS.ControlPanel.DocAndLib.text_Category", testdata.get("Category")));
			// click(commonLocators.getLocator("CMS.ControlPanel.DocAndLib.button_Publish"));
			// waitFor(3);
			// waitForPageToLoad();
			// page.verifySuccessMessage();
			// } else {
			// log.comment("Method Name", "No Locator", "Script did not complete successfully.", Log.FAIL, Log.MAJOR_ISSUE);
			// }

		} catch (Exception e) {
			log.exception(e);
			log.comment("Method Name", "No Locator", "Script did not complete successfully.", Log.FAIL, Log.MAJOR_ISSUE);
			throw e;
		} finally {
			page.goBackToViewPage();
		}
	}

	public void Categorize_Web_Content(Map<String, String> testdata) throws Exception {
		try {

			log.comment("**********Web Content_Categorize a Web Content**********");

			page.goToPage("Control Panel");
			page.setCurrentSite(testdata.get("Site"));
			page.openPage(Page.Menu.content, "Web Content");
			page.verifyPageTitle("Web Content");

			verifyElementExist(getDynamicLocatorType("CMS.ControlPanel.datatable.link-cell_name", testdata.get("WebContentName")));
			click(getDynamicLocatorType("CMS.ControlPanel.datatable.link-cell_name", testdata.get("WebContentName")));
			waitForPageToLoad();
			page.verifysectionTitle(testdata.get("WebContentName"));

			page.openFrame("Categorization");
			waitForPageToLoad();

			verifyElementExist(getDynamicLocatorType("CMS.ControlPanel.DocAndLib.text_Vocabulary", testdata.get("Vocabulary")));
			Select_Category(testdata.get("Vocabulary"), testdata.get("CategoryName"));

		} catch (Exception e) {
			log.exception(e);
			log.comment("Method Name", "No Locator", "Script did not complete successfully.", Log.FAIL, Log.MAJOR_ISSUE);
			throw e;
		} finally {
			page.goBackToViewPage();
		}
	}

	public void Select_Category(String vocabulary, String category) throws Exception {
		try {
			if (isElementPresent(getDynamicLocatorType("CMS.ControlPanel.DocAndLib.text_Vocabulary", vocabulary))) {
				click(getDynamicLocatorType("CMS.ControlPanel.DocAndLib.button_Select", vocabulary));
				waitFor(2);
				waitForPageToLoad();
				verifyElementExist(commonLocators.getLocator("CMS.ControlPanel.CategoriesDialog.title_Categories"));
				click(getDynamicLocatorType("CMS.ControlPanel.CategoriesDialog.weblist_Category", category));
				click(commonLocators.getLocator("CMS.ControlPanel.CategoriesDialog.button_Close"));
				verifyElementExist(getDynamicLocatorType("CMS.ControlPanel.DocAndLib.text_Category", category));
				click(commonLocators.getLocator("CMS.ControlPanel.DocAndLib.button_Publish"));
				waitFor(3);
				waitForPageToLoad();
				page.verifySuccessMessage();
			} else {
				log.comment("Method Name", "No Locator", "The Vocabulary \"" + vocabulary + "\" did not shows up.", Log.FAIL, Log.MAJOR_ISSUE);
			}
		} catch (Exception e) {
			log.exception(e);
			log.comment("Method Name", "No Locator", "Script did not complete successfully.", Log.FAIL, Log.MAJOR_ISSUE);
			throw e;
		} finally {
			page.goBackToViewPage();
		}
	}

	public void Edit_HTML_Title_of_Page(String testCaseName) throws Exception {
		try {

			log.comment("**********Site Page_Edit Details of page_HTML Title**********");

			page.goToPage("Control Panel");
			page.openPage(Page.Menu.content, "Site Pages");
			page.verifyPageTitle("Site Pages");

			page.setCurrentSite(getTestCaseData(testCaseName, "SiteName"));
			page.verifysectionTitle(getTestCaseData(testCaseName, "SiteName"));

			verifyElementExist(getDynamicLocatorType("CMS.ControlPanel.SitePages.text_PageName", getTestCaseData(testCaseName, "PageName")));
			click(getDynamicLocatorType("CMS.ControlPanel.SitePages.text_PageName", getTestCaseData(testCaseName, "PageName")));
			waitFor(3);
			waitForPageToLoad();

			page.openFrame("Details");
			setText(commonLocators.getLocator("CMS.ControlPanel.SitePages.textbox_HTMLTitle"), getTestCaseData(testCaseName, "TestDataX"));
			page.clickButton("Save");
			waitForPageToLoad();
			page.verifySuccessMessage();

		} catch (Exception e) {
			log.exception(e);
			log.comment("Method Name", "No Locator", "Script did not complete successfully.", Log.FAIL, Log.MAJOR_ISSUE);
			throw e;
		} finally {
			page.goBackToViewPage();
		}
	}

	public void Add_Child_Page(String testCaseName) throws Exception {
		try {

			log.comment("**********Site Page_Add Child page**********");

			page.goToPage("Control Panel");
			page.openPage(Page.Menu.content, "Site Pages");
			page.verifyPageTitle("Site Pages");

			page.setCurrentSite(getTestCaseData(testCaseName, "SiteName"));
			page.verifysectionTitle(getTestCaseData(testCaseName, "SiteName"));

			verifyElementExist(getDynamicLocatorType("CMS.ControlPanel.SitePages.text_PageName", getTestCaseData(testCaseName, "TestDataX")));
			click(getDynamicLocatorType("CMS.ControlPanel.SitePages.text_PageName", getTestCaseData(testCaseName, "TestDataX")));
			waitForPageToLoad();

			click(commonLocators.getLocator("CMS.ControlPanel.SitePages.button_AddChildPage"));
			waitForPageToLoad();
			// verifyElementExist(commonLocators.getLocator("CMS.ControlPanel.AddPageDialog.title_AddChildPage"));
			verifyElementExist(getDynamicLocatorType("CMS.ControlPanel.AddPageDialog.title_AddPage", "Add Child Page"));
			setText(commonLocators.getLocator("CMS.ControlPanel.AddPageDialog.textbox_PageName"), getTestCaseData(testCaseName, "PageName"));
			select(commonLocators.getLocator("CMS.ControlPanel.AddPageDialog.select_Type"), getTestCaseData(testCaseName, "Type"));
			click(commonLocators.getLocator("CMS.ControlPanel.AddPageDialog.button_Add"));
			waitForPageToLoad();
			page.verifySuccessMessage();
			click(getDynamicLocatorType("CMS.ControlPanel.SitePages.icon_treeNode", getTestCaseData(testCaseName, "TestDataX")));
			waitForPageToLoad();
			verifyElementExist(getDynamicLocatorType("CMS.ControlPanel.SitePages.text_ChildPageName", getTestCaseData(testCaseName, "PageName")));

		} catch (Exception e) {
			log.exception(e);
			log.comment("Method Name", "No Locator", "Script did not complete successfully.", Log.FAIL, Log.MAJOR_ISSUE);
			throw e;
		} finally {
			page.goBackToViewPage();
		}
	}

	public void Add_Private_Page(String testCaseName) throws Exception {
		try {

			log.comment("**********Site Page_Add Private page**********");

			page.goToPage("Control Panel");
			page.openPage(Page.Menu.content, "Site Pages");
			page.verifyPageTitle("Site Pages");

			page.setCurrentSite(getTestCaseData(testCaseName, "SiteName"));
			page.verifysectionTitle(getTestCaseData(testCaseName, "SiteName"));

			click(commonLocators.getLocator("CMS.ControlPanel.SitePages.tabtitle_Private"));
			waitForPageToLoad();

			click(commonLocators.getLocator("CMS.ControlPanel.SitePages.button_AddPage"));
			waitForPageToLoad();
			// verifyElementExist(commonLocators.getLocator("CMS.ControlPanel.AddPageDialog.title_AddPage"));
			verifyElementExist(getDynamicLocatorType("CMS.ControlPanel.AddPageDialog.title_AddPage", "Add Page"));

			setText(commonLocators.getLocator("CMS.ControlPanel.AddPageDialog.textbox_PageName"), getTestCaseData(testCaseName, "PageName"));
			click(commonLocators.getLocator("CMS.ControlPanel.AddPageDialog.button_Add"));
			waitForPageToLoad();
			page.verifySuccessMessage();
			verifyElementExist(getDynamicLocatorType("CMS.ControlPanel.SitePages.text_PageName", getTestCaseData(testCaseName, "PageName")));

		} catch (Exception e) {
			log.exception(e);
			log.comment("Method Name", "No Locator", "Script did not complete successfully.", Log.FAIL, Log.MAJOR_ISSUE);
			throw e;
		} finally {
			page.goBackToViewPage();
		}
	}

	public void Move_Page_Public_to_Private(String testCaseName) throws Exception {
		try {

			log.comment("**********Site Page_Move page(Move Public page to Private)**********");

			page.goToPage("Control Panel");
			page.openPage(Page.Menu.content, "Site Pages");
			page.verifyPageTitle("Site Pages");

			page.setCurrentSite(getTestCaseData(testCaseName, "SiteName"));
			page.verifysectionTitle(getTestCaseData(testCaseName, "SiteName"));

			click(commonLocators.getLocator("CMS.ControlPanel.SitePages.tabtitle_Move"));
			waitForPageToLoad();

			// move from public page to private page
			click(getDynamicLocatorType("CMS.ControlPanel.SitePages.select_publicpage", getTestCaseData(testCaseName, "PageName")));
			click(commonLocators.getLocator("CMS.ControlPanel.SitePages.button_rightArrow"));
			click(commonLocators.getLocator("CMS.ControlPanel.SitePages.button_Save"));
			waitForPageToLoad();
			page.verifySuccessMessage();
			verifyElementExist(getDynamicLocatorType("CMS.ControlPanel.SitePages.select_privatepage", getTestCaseData(testCaseName, "PageName")));
			verifyElementNotExist(getDynamicLocatorType("CMS.ControlPanel.SitePages.select_publicpage", getTestCaseData(testCaseName, "PageName")));

		} catch (Exception e) {
			log.exception(e);
			log.comment("Method Name", "No Locator", "Script did not complete successfully.", Log.FAIL, Log.MAJOR_ISSUE);
			throw e;
		} finally {
			page.goBackToViewPage();
		}
	}

	public void Move_Page_Private_to_Public(String testCaseName) throws Exception {
		try {

			log.comment("**********Site Page_Move page(Move Private page to Public)**********");

			page.goToPage("Control Panel");
			page.openPage(Page.Menu.content, "Site Pages");
			page.verifyPageTitle("Site Pages");

			page.setCurrentSite(getTestCaseData(testCaseName, "SiteName"));
			page.verifysectionTitle(getTestCaseData(testCaseName, "SiteName"));

			click(commonLocators.getLocator("CMS.ControlPanel.SitePages.tabtitle_Move"));
			waitForPageToLoad();

			// move from private page to public page
			click(getDynamicLocatorType("CMS.ControlPanel.SitePages.select_privatepage", getTestCaseData(testCaseName, "PageName")));
			click(commonLocators.getLocator("CMS.ControlPanel.SitePages.button_leftArrow"));
			click(commonLocators.getLocator("CMS.ControlPanel.SitePages.button_Save"));
			waitForPageToLoad();
			page.verifySuccessMessage();
			verifyElementExist(getDynamicLocatorType("CMS.ControlPanel.SitePages.select_publicpage", getTestCaseData(testCaseName, "PageName")));
			verifyElementNotExist(getDynamicLocatorType("CMS.ControlPanel.SitePages.select_privatepage", getTestCaseData(testCaseName, "PageName")));

		} catch (Exception e) {
			log.exception(e);
			log.comment("Method Name", "No Locator", "Script did not complete successfully.", Log.FAIL, Log.MAJOR_ISSUE);
			throw e;
		} finally {
			page.goBackToViewPage();
		}
	}

	public void Export_Pages(String testCaseName) throws Exception {
		try {

			log.comment("**********Site Page_Export pages**********");

			page.goToPage("Control Panel");
			page.openPage(Page.Menu.content, "Site Pages");
			page.verifyPageTitle("Site Pages");

			page.setCurrentSite(getTestCaseData(testCaseName, "SiteName"));
			page.verifysectionTitle(getTestCaseData(testCaseName, "SiteName"));

			click(commonLocators.getLocator("CMS.ControlPanel.SitePages.button_ExportPage"));
			waitForPageToLoad();
			verifyElementExist(commonLocators.getLocator("CMS.ControlPanel.ExportDialog.title_Export"));
			setText(commonLocators.getLocator("CMS.ControlPanel.ExportDialog.textbox_PackageName"), getTestCaseData(testCaseName, "PackageName"));
			click(commonLocators.getLocator("CMS.ControlPanel.ExportDialog.button_Export"));

			// TODO Export pages with autoit3 or UI Spy

			click(commonLocators.getLocator("CMS.ControlPanel.ExportDialog.button_Close"));

			// TODO Check result

		} catch (Exception e) {
			log.exception(e);
			log.comment("Method Name", "No Locator", "Script did not complete successfully.", Log.FAIL, Log.MAJOR_ISSUE);
			throw e;
		} finally {
			page.goBackToViewPage();
		}
	}

	public void Import_Pages(String testCaseName) throws Exception {
		try {

			log.comment("**********Site Page_Import pages**********");

			page.goToPage("Control Panel");
			page.openPage(Page.Menu.content, "Site Pages");
			page.verifyPageTitle("Site Pages");

			page.setCurrentSite(getTestCaseData(testCaseName, "SiteName"));
			page.verifysectionTitle(getTestCaseData(testCaseName, "SiteName"));

			click(commonLocators.getLocator("CMS.ControlPanel.SitePages.button_ImportPage"));
			waitForPageToLoad();
			verifyElementExist(commonLocators.getLocator("CMS.ControlPanel.ImportDialog.title_Import"));
			setText(commonLocators.getLocator("CMS.ControlPanel.ImportDialog.textbox_PackageName"), getTestCaseData(testCaseName, "PackageName"));
			click(commonLocators.getLocator("CMS.ControlPanel.ImportDialog.button_Import"));

			// TODO Import pages with autoit3 or UI Spy

			click(commonLocators.getLocator("CMS.ControlPanel.ExportDialog.button_Close"));

			// TODO Check result

		} catch (Exception e) {
			log.exception(e);
			log.comment("Method Name", "No Locator", "Script did not complete successfully.", Log.FAIL, Log.MAJOR_ISSUE);
			throw e;
		} finally {
			page.goBackToViewPage();
		}
	}

	public boolean Delete_Site_Page_via_Manage_Page(Map<String, String> testdata) throws Exception {
		boolean deleted = false;
		try {

			log.comment("**********Site Page_Delete page via Manage Page(Live)**********");

			page.goToPage(testdata.get("Site"));
			page.manageUtility("Page");
			waitFor(5);
			waitForPageToLoad();
			verifyElementExist(commonLocators.getLocator("CMS.ViewPage.ManagePageDialog.pagetitle_ManagePage"));
			String winhndl = getBrowser().getWebDriver().getWindowHandle();
			getBrowser().getWebDriver().switchTo().frame("manageContentDialog");
			verifyElementExist(getDynamicLocatorType("CMS.ViewPage.ManagePageDialog.text_PageName", testdata.get("PageName")));
			click(getDynamicLocatorType("CMS.ViewPage.ManagePageDialog.text_PageName", testdata.get("PageName")));
			waitFor(3);
			waitForPageToLoad();
			click(commonLocators.getLocator("CMS.ViewPage.ManagePageDialog.button_Delete"));
			waitFor(2);

			// page.click_Ok();
			acceptAlert();
			waitFor(3);
			waitForPageToLoad();
			// waitFor(getNumSecondsToWaitForElementPresent());

			page.verifySuccessMessage();
			verifyElementNotExist(getDynamicLocatorType("CMS.ViewPage.ManagePageDialog.text_PageName", testdata.get("PageName")));
			getBrowser().getWebDriver().switchTo().window(winhndl);
			click(commonLocators.getLocator("CMS.ViewPage.ManagePageDialog.button_Close"));
			waitForPageToLoad();
			verifyElementNotExist(getDynamicLocatorType("CMS.ViewPage.NavigationBar.text_PageName", testdata.get("PageName")));
			deleted = !isElementExistHelper(getDynamicLocatorType("CMS.ViewPage.NavigationBar.text_PageName", testdata.get("PageName")).getValue());
			return deleted;

		} catch (Exception e) {
			log.exception(e);
			log.comment("Method Name", "No Locator", "Script did not complete successfully.", Log.FAIL, Log.MAJOR_ISSUE);
			throw e;
		}
	}

	public boolean Delete_Site_Page_via_Manage_Site_Pages(Map<String, String> testdata) throws Exception {
		boolean deleted = false;
		try {

			log.comment("**********Site Page_Delete page via Manage Page(Live)**********");

			page.goToPage(testdata.get("Site"));
			page.manageUtility("Site Pages");
			waitFor(5);
			waitForPageToLoad();
			verifyElementExist(commonLocators.getLocator("CMS.ViewPage.ManagePageDialog.pagetitle_ManageSitePages"));
			String winhndl = getBrowser().getWebDriver().getWindowHandle();
			getBrowser().getWebDriver().switchTo().frame("manageContentDialog");
			verifyElementExist(getDynamicLocatorType("CMS.ViewPage.ManagePageDialog.text_PageName", testdata.get("PageName")));
			click(getDynamicLocatorType("CMS.ViewPage.ManagePageDialog.text_PageName", testdata.get("PageName")));
			waitFor(3);
			waitForPageToLoad();
			click(commonLocators.getLocator("CMS.ViewPage.ManagePageDialog.button_Delete"));
			waitFor(2);

			// page.click_Ok();
			acceptAlert();
			waitFor(3);
			waitForPageToLoad();
			// waitFor(getNumSecondsToWaitForElementPresent());

			page.verifySuccessMessage();
			verifyElementNotExist(getDynamicLocatorType("CMS.ViewPage.ManagePageDialog.text_PageName", testdata.get("PageName")));
			getBrowser().getWebDriver().switchTo().window(winhndl);
			click(commonLocators.getLocator("CMS.ViewPage.ManagePageDialog.button_Close"));
			waitForPageToLoad();
			verifyElementNotExist(getDynamicLocatorType("CMS.ViewPage.NavigationBar.text_PageName", testdata.get("PageName")));
			deleted = !isElementExistHelper(getDynamicLocatorType("CMS.ViewPage.NavigationBar.text_PageName", testdata.get("PageName")).getValue());
			return deleted;

		} catch (Exception e) {
			log.exception(e);
			log.comment("Method Name", "No Locator", "Script did not complete successfully.", Log.FAIL, Log.MAJOR_ISSUE);
			throw e;
		}
	}

	public boolean Delete_Site_Page_via_Site_Pages(Map<String, String> testdata) throws Exception {
		boolean deleted = false;
		try {

			log.comment("**********Site Page_Delete page via Site Pages(Live)**********");

			page.goToPage("Control Panel");
			page.openPage(Page.Menu.content, "Site Pages");
			// page.verifyPageTitle("Site Pages");

			page.setCurrentSite(testdata.get("Site"));
			page.verifysectionTitle(testdata.get("Site"));

			verifyElementExist(getDynamicLocatorType("CMS.ControlPanel.SitePages.text_PageName", testdata.get("PageName")));
			click(getDynamicLocatorType("CMS.ControlPanel.SitePages.text_PageName", testdata.get("PageName")));
			waitFor(1);
			waitForPageToLoad();
			click(commonLocators.getLocator("CMS.ControlPanel.SitePages.button_Delete"));
			waitFor(3);

			// page.click_Ok();
			acceptAlert();
			waitFor(2);

			verifyElementNotExist(getDynamicLocatorType("CMS.ControlPanel.SitePages.text_PageName", testdata.get("PageName")));
			deleted = !isElementExistHelper(getDynamicLocatorType("CMS.ControlPanel.SitePages.text_PageName", testdata.get("PageName")).getValue());

			return deleted;

		} catch (Exception e) {
			log.exception(e);
			log.comment("Method Name", "No Locator", "Script did not complete successfully.", Log.FAIL, Log.MAJOR_ISSUE);
			throw e;
		} finally {
			page.goBackToViewPage();
		}
	}

	public boolean Delete_Site_Page_via_Menu_Bar(String testCaseName) throws Exception {
		boolean deleted = false;
		try {

			log.comment("**********Site Page_Delete Site Page via Menu Bar**********");

			page.goToPage(getTestCaseData(testCaseName, "SiteName"));
			waitFor(2);
			// getBrowser().getSelenium().mouseMove(
			// getDynamicLocatorType("CMS.ViewPage.NavigationBar.tab_PageName", getTestCaseData(testCaseName, "PageName")).getValue());
			// getBrowser().getSelenium().mouseOver(
			// getDynamicLocatorType("CMS.ViewPage.NavigationBar.tab_PageName", getTestCaseData(testCaseName, "PageName")).getValue());
			// click(commonLocators.getLocator("CMS.ViewPage.NavigationBar.button_delete"));
			// waitFor(2);
			// // page.click_Ok();
			// acceptAlert();
			// waitFor(2);
			page.deletePageOnNavigationBar(getTestCaseData(testCaseName, "PageName"));
			verifyElementNotExist(getDynamicLocatorType("CMS.ViewPage.NavigationBar.text_PageName", getTestCaseData(testCaseName, "PageName")));
			deleted = !isElementExistHelper(getDynamicLocatorType("CMS.ViewPage.NavigationBar.text_PageName",
					getTestCaseData(testCaseName, "PageName")).getValue());
			return deleted;

		} catch (Exception e) {
			log.exception(e);
			log.comment("Method Name", "No Locator", "Script did not complete successfully.", Log.FAIL, Log.MAJOR_ISSUE);
			throw e;
		}
	}

	public void Set_Permissions_for_Site_Page_via_Control_Panel(Map<String, String> testdata) throws Exception {

		try {

			log.comment("**********Site Page_Edit Permissions of page**********");

			String permissions = testdata.get("PagePermissions");
			String[] rolesPermissions = permissions.split("#");

			page.goToPage("Control Panel");
			page.setCurrentSite(testdata.get("Site"));
			// page.verifysectionTitle(testdata.get("Site"));
			page.openPage(Page.Menu.content, "Site Pages");
			// page.verifyPageTitle("Site Pages");

			verifyElementExist(getDynamicLocatorType("CMS.ControlPanel.SitePages.text_PageName", testdata.get("PageName")));
			click(getDynamicLocatorType("CMS.ControlPanel.SitePages.text_PageName", testdata.get("PageName")));
			waitFor(1);
			waitForPageToLoad();
			click(commonLocators.getLocator("CMS.ControlPanel.SitePages.button_Permissions"));
			waitFor(3);
			verifyElementExist(commonLocators.getLocator("CMS.ControlPanel.PermissionsDialog.text_Title"));
			String winhndl = getBrowser().getWebDriver().getWindowHandle();
			// driver.switchTo().frame(0);
			// driver.switchTo().activeElement();
			String framename = driver.findElement(By.xpath("//iframe[contains(@name,'_permissions')]")).getAttribute("name");
			driver.switchTo().frame(framename);
			driver.switchTo().activeElement();

			for (String rolePermissionsStr : rolesPermissions) {
				if (rolePermissionsStr.contains(":")) {
					String[] rolePermissionsArray = rolePermissionsStr.split(":");
					String role = rolePermissionsArray[0];
					Page.UserActions[] enumRolePermissions = new Page.UserActions[rolePermissionsArray.length - 1];
					for (int i = 0; i < rolePermissionsArray.length - 1; i++) {
						enumRolePermissions[i] = Enum.valueOf(Page.UserActions.class, rolePermissionsArray[i + 1]);
					}
					// String[] RolePermissions = Arrays.copyOfRange(rolePermissionsArray, 1, rolePermissionsArray.length - 1);
					page.setOffAllPermissions(Enum.valueOf(CMSWebApp.UserRole.class, role));
					page.setPermissions(Enum.valueOf(CMSWebApp.UserRole.class, role), enumRolePermissions);

				} else {
					String role = rolePermissionsStr;
					page.setOffAllPermissions(Enum.valueOf(CMSWebApp.UserRole.class, role));
				}

			}

			click(commonLocators.getLocator("CMS.ControlPanel.PermissionsDialog.button_Save"));
			waitFor(3);
			waitForPageToLoad();
			page.verifySuccessMessage();
			driver.switchTo().window(winhndl);
			click(commonLocators.getLocator("CMS.ControlPanel.PermissionsDialog.button_Close"));

		} catch (Exception e) {
			log.exception(e);
			log.comment("Method Name", "No Locator", "Script did not complete successfully.", Log.FAIL, Log.MAJOR_ISSUE);
			throw e;
		} finally {
			page.goBackToViewPage();
		}

	}

	public void verify_Permissions_for_Site_Page_via_Control_Panel(Map<String, String> testdata) throws Exception {

		try {

			log.comment("**********Site Page_Verify Permissions of page**********");
			String permissions = testdata.get("PagePermissions");

			page.goToPage("Control Panel");
			page.setCurrentSite(testdata.get("Site"));
			// page.verifysectionTitle(testdata.get("Site"));
			page.openPage(Page.Menu.content, "Site Pages");
			// page.verifyPageTitle("Site Pages");

			verifyElementExist(getDynamicLocatorType("CMS.ControlPanel.SitePages.text_PageName", testdata.get("PageName")));
			click(getDynamicLocatorType("CMS.ControlPanel.SitePages.text_PageName", testdata.get("PageName")));
			waitFor(1);
			waitForPageToLoad();
			click(commonLocators.getLocator("CMS.ControlPanel.SitePages.button_Permissions"));
			waitFor(3);
			verifyElementExist(commonLocators.getLocator("CMS.ControlPanel.PermissionsDialog.text_Title"));
			String winhndl = getBrowser().getWebDriver().getWindowHandle();
			String framename = driver.findElement(By.xpath("//iframe[contains(@name,'_permissions')]")).getAttribute("name");
			driver.switchTo().frame(framename);
			driver.switchTo().activeElement();

			verify_Permissions_for_Site_Page(permissions);

			driver.switchTo().window(winhndl);
			click(commonLocators.getLocator("CMS.ControlPanel.PermissionsDialog.button_Close"));

		} catch (Exception e) {
			log.exception(e);
			log.comment("Method Name", "No Locator", "Script did not complete successfully.", Log.FAIL, Log.MAJOR_ISSUE);
			throw e;
		} finally {
			page.goBackToViewPage();
		}

	}

	protected void verify_Permissions_for_Site_Page(String permissions) throws Exception {

		try {

			log.comment("**********Verify Permissions for a specific page**********");
			String[] rolesPermissions = permissions.split("#");

			for (String rolePermissionsStr : rolesPermissions) {
				if (rolePermissionsStr.contains(":")) {
					String[] rolePermissionsArray = rolePermissionsStr.split(":");
					String role = rolePermissionsArray[0];
					Page.UserActions[] enumRolePermissions = new Page.UserActions[rolePermissionsArray.length - 1];
					for (int i = 0; i < rolePermissionsArray.length - 1; i++) {
						enumRolePermissions[i] = Enum.valueOf(Page.UserActions.class, rolePermissionsArray[i + 1]);
					}
					page.verifyPermissions(Enum.valueOf(CMSWebApp.UserRole.class, role), enumRolePermissions);

				} else {
					String role = rolePermissionsStr;
					page.verifyNoPermissions(Enum.valueOf(CMSWebApp.UserRole.class, role));
				}

			}

		} catch (Exception e) {
			log.exception(e);
			log.comment("Method Name", "No Locator", "Script did not complete successfully.", Log.FAIL, Log.MAJOR_ISSUE);
			throw e;
		}

	}

	public void Add_Search_Portlet(Map<String, String> testdata) throws Exception {

		try {

			log.comment("**********Portlet_Add Search Portlet**********");

			page.goToPage(testdata.get("Site"));
			page.viewPageViaNavBar(testdata.get("PageName"));
			page.addPortlet("More");
			String portlet = "Search";
			setText(commonLocators.getLocator("CMS.Welcome.TopMenuBar.textbox_PortletName"), portlet);
			waitFor(1);
			click(getDynamicLocatorType("CMS.Welcome.TopMenuBar.PortletItem_Add", portlet));
			waitFor(3);
			waitForPageToLoad();
			verifyElementExist(getDynamicLocatorType("CMS.ViewPage.Portlets.text_PortletTitle", portlet));

		} catch (Exception e) {
			log.exception(e);
			log.comment("Method Name", "No Locator", "Script did not complete successfully.", Log.FAIL, Log.MAJOR_ISSUE);
			throw e;
		}
	}

	// --------------------------Deprecated--------------------------Deprecated--------------------------

	/**
	 * @author terry
	 * @description setMailWaitTime
	 * @date 2012-02-01
	 */
	public void setMailWaitTime(int timeout) {
		_waitTimeoutSecondsForMail = timeout;
	}

	public void getMailWaitTime() {
		try {
			Thread.sleep(_waitTimeoutSecondsForMail * 1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public String searchMail(String mailAddress) {
		getBrowser().getWebDriver().get("www.mailinator.com");
		getMailWaitTime();
		getBrowser().getWebDriver().findElement(By.xpath("//input[@name='email']")).sendKeys(mailAddress);
		getBrowser().getWebDriver().findElement(By.xpath("//input[@class='buttonGo']")).click();
		boolean toTest = getBrowser().getWebDriver().findElement(By.xpath("//td/a[contains(text(),'CMSA PXCSM: Message Regarding Your Acco')]"))
				.isDisplayed();
		if (toTest)
			getBrowser().getWebDriver().findElement(By.xpath("//td/a[contains(text(),'CMSA PXCSM: Message Regarding Your Acco')]")).click();
		return getBrowser().getWebDriver().findElement(By.xpath("//div[@id='message']/p")).getText();

	}

	public void searchMailinator(String mailSearchkey) throws Exception {
		getBrowser().navigateTo("www.mailinator.com");
		getMailWaitTime();
		searchMail(getCommLocatorsForTestCase("CMS.SendMail.Mailinator.txt_checkBox"), mailSearchkey,
				getCommLocatorsForTestCase("CMS.SendMail.Mailinator.btn_buttonGo"));
		this.getMailWaitTime();
		viewMail(getCommLocatorsForTestCase("CMS.SendMail.Mailinator.link_inboxList"));
		click(getCommLocatorsForTestCase("CMS.SendMail.Mailinator.link_charset"));
		waitFor(getNumSecondsToWaitForElementPresent());
		clickLinkToRegister(getCommLocatorsForTestCase("CMS.SendMail.Mailinator.link_regLink"));
	}

	public void searchMail(Locators.LocatorType locator, String strInput, Locators.LocatorType goBtn) throws Exception {
		// getMailWaitTime();
		setText(locator, strInput);
		click(goBtn);
		waitForPageToLoad();
	}

	public void viewMail(Locators.LocatorType inboxList) throws Exception {
		Number nSearchCount = getXpathCount(inboxList);
		String xpath = "//table[@id ='inboxList']/tbody/tr[" + (nSearchCount.intValue() - 1) + "]/td[2]/a";
		browser.getSelenium().click(xpath);
		waitForPageToLoad();
	}

	/**
	 * @author Terry Sun
	 * @description Search invitation mail.Then navigate to register page for the user
	 * @param
	 * @return null
	 * @date 2011-12-21
	 */
	public void clickLinkToRegister(Locators.LocatorType regLink) {
		click(regLink);
		waitForPageToLoad();
	}

	public Number getXpathCount(Locators.LocatorType locatorType) throws Exception {
		assertElementExist(locatorType);
		Number n = 0;
		n = browser.getSelenium().getXpathCount(locatorType.getValue());
		return n;
	}

	public void assertElementExist(Locators.LocatorType locatorType) throws Exception {
		try {
			org.testng.Assert.assertTrue(isElementPresent(locatorType));
		} catch (AssertionError ae) {
			throw new Exception("assertElementExist, but " + locatorType.getKey() + "=" + locatorType.getValue() + " does not exist");
		}
	}

	/**
	 * @author Ryan
	 * @description return full name Register a user of the organization information.
	 * @param String
	 *            mailSearchkey,String line
	 * @return fullName
	 * @date 2012-03-11
	 */

	public String[] regUserInfo(String mailSearchkey, String line) throws Exception {
		String[] fullName = new String[2];
		DateFormat df = new SimpleDateFormat("ddhhmm");
		fullName[0] = getTestCaseData(line, "FirstName") + df.format(new Date());
		fullName[1] = getTestCaseData(line, "LastName") + df.format(new Date());

		setText(getCommLocatorsForTestCase("CMS.Registration.EnterUserInfo_FirstName"), fullName[0]);
		setText(getCommLocatorsForTestCase("CMS.Registration.EnterUserInfo_LastName"), fullName[1]);
		setText(getCommLocatorsForTestCase("CMS.Registration.EnterUserInfo_PhoneNum"), getTestCaseData(line, "PhoneNumber"));
		setText(getCommLocatorsForTestCase("CMS.Registration.EnterUserInfo_EmailAddress"), mailSearchkey);
		setText(getCommLocatorsForTestCase("CMS.Registration.EnterUserInfo_ReEntEmailAddress"), mailSearchkey);
		click(getCommLocatorsForTestCase("CMS.Registration.EnterUserInfo_ContinueRegBtn"));
		return fullName;
	}

	/**
	 * @author terry
	 * @description confirm invitation with admin user.
	 * @param confirmReqStr
	 *            :'ConfirmReqest','ConfrimReqQrg'
	 * @date 2011-12-27
	 */
	public void confirmInvitation(String confirmReqStr) throws Exception {

		// navigate to system.
		adminLoginCMS();

		// Get the latest request from the pending user list.
		WebElement userRegLink = null;
		String url = getTestCaseData(confirmReqStr, "URL");
		this.getBrowser().navigateTo(url);

		List<WebElement> linkLists = getElements(getCommLocatorsForTestCase("CMS_ViewRequest_AllLinks"));
		for (WebElement lastlink : linkLists) {
			userRegLink = lastlink;
		}
		userRegLink.click();

		// approve the pending user's request.
		click(getCommLocatorsForTestCase("CMS_ViewRequest_radio_Approve"));
		setText(getCommLocatorsForTestCase("CMS_ViewRequest_textarea_Reason"), "approve");
		click(getCommLocatorsForTestCase("CMS_ViewRequest_btn_Submit"));
		acceptAlert();
		acceptAlertIfFound();

		// logout system.
		click(getCommLocatorsForTestCase("CMS.SignOut.link_Logout"));
	}

	// login CMS with AdminLoginCMS
	public void adminLoginCMS() throws Exception {
		String url = getTestCaseData("common", "URL");
		this.getBrowser().navigateTo(url);
		setText(getCommLocatorsForTestCase("CMS.SignIn.txt_User"), getTestCaseData("common", "AdminUID"));
		setText(getCommLocatorsForTestCase("CMS.SignIn.txt_Password"), getTestCaseData("common", "AdminPassword"));
		click(getCommLocatorsForTestCase("CMS.SignIn.btn_SignOn"));
		waitForPageToLoad();
	}

	/**
	 * @author terry
	 * @description find all links
	 * @date 2011-12-19
	 */
	public List<WebElement> getElements(LocatorType locator) {
		return driver.findElements(By.xpath(locator.getValue()));
	}

	/**
	 * @author Terry Sun
	 * @description invite a user
	 * @param inviteUserStr
	 *            : 'InviteUser','InviteUserOfDiv'
	 * @return null
	 * @date 2011-12-21
	 */
	public String inviteUser(String inviteUserStr) throws Exception {

		// prepare a mail address.
		DateFormat df = new SimpleDateFormat("MMddyyyy_hh_mm");
		String mailaddress = "CCA_" + df.format(new Date()) + "@mailinator.com";

		adminLoginCMS();

		// navigate to invite user page.
		String url = getTestCaseData(inviteUserStr, "URL");
		getBrowser().navigateTo(url);

		// set invite user info including email.
		// select(getCommLocatorsForTestCase("CMS.InviteUsers.page_ParentPkgname"),"None");
		setText(getCommLocatorsForTestCase("CMS.InviteUsers.page_EmailAddress"), mailaddress);
		// click(getCommLocatorsForTestCase("CMS.InviteUsers.page_Next"));
		click(getCommLocatorsForTestCase("CMS.InviteUsers.page_SendInvitation"));
		click(getCommLocatorsForTestCase("CMS.SignOut.link_Logout"));
		return mailaddress;
	}

	/**
	 * @author Terry Sun
	 * @description Register a userid with passwd.
	 * @param
	 * @return null
	 * @date 2011-12-27
	 */
	public String[] regUserID() throws Exception {
		String[] userInfo = new String[3];
		DateFormat df = new SimpleDateFormat("MMddyyyyhhmm");
		userInfo[0] = getTestCaseData("RegisterUserID", "UserID") + df.format(new Date());
		userInfo[1] = getTestCaseData("RegisterUserID", "UserPassword");
		userInfo[2] = getTestCaseData("RegisterUserID", "UserPassword");

		setText(getCommLocatorsForTestCase("CMS.Registration.EnterUserInfo_UserID"), userInfo[0]);
		setText(getCommLocatorsForTestCase("CMS.Registration.EnterUserInfo_Passwd"), userInfo[1]);
		setText(getCommLocatorsForTestCase("CMS.Registration.EnterUserInfo_ReEntPasswd"), userInfo[2]);
		select(getCommLocatorsForTestCase("CMS.Registration.EnterUserInfo_ChallengeQ"), 1);
		setText(getCommLocatorsForTestCase("CMS.Registration.EnterUserInfo_ChallengeA"), "cat");
		click(getCommLocatorsForTestCase("CMS.Registration.EnterUserInfo_ContinueRegBtn"));
		try {
			click(getCommLocatorsForTestCase("CMS.Registration.EnterUserInfo_AgreementBtn"));
		} catch (Exception e) {
			log.comment("Be intend to pass the step,if it doesn't show up.");
		}
		setText(getCommLocatorsForTestCase("CMS.Registration.EnterUserInfo_RequestReason"), "apply");
		click(getCommLocatorsForTestCase("CMS.Registration.EnterUserInfo_SubmitBtn"));
		// click(getCommLocatorsForTestCase("CMS.SignOut.link_Logout"));
		return userInfo;
	}

	/**
	 * @author Terry Sun
	 * @description normal user login.
	 * @param String
	 *            userID, String password
	 * @return null
	 * @date 2011-12-21
	 */
	public void normalUserLogin(String userID, String password) throws Exception {
		String url = getTestCaseData("common", "URL");
		getBrowser().navigateTo(url);
		setText(getCommLocatorsForTestCase("CMS.SignIn.txt_User"), userID);
		setText(getCommLocatorsForTestCase("CMS.SignIn.txt_Password"), password);
		click(getCommLocatorsForTestCase("CMS.SignIn.btn_SignOn"));
		waitForPageToLoad();
	}

	/**
	 * @author Ryan
	 * @description get invite url in mail content
	 * @param String
	 *            mailAddress
	 * @return url
	 * @date 2012-03-06
	 */
	public String getURLFromMailContent(String mailAddress) throws Exception {
		getBrowser().getWebDriver().get("www.mailinator.com");
		getMailWaitTime();
		getBrowser().getWebDriver().findElement(By.xpath("//input[@name='email']")).sendKeys(mailAddress);
		getBrowser().getWebDriver().findElement(By.xpath("//input[@class='buttonGo']")).click();
		boolean toTest = getBrowser().getWebDriver().findElement(By.xpath("//td/a[contains(text(),'Invitation from CMSA')]")).isDisplayed();

		if (toTest) {
			getBrowser().getWebDriver().findElement(By.xpath("//td/a[contains(text(),'Invitation from CMSA')]")).click();
			waitForPageToLoad();

		}

		click(getCommLocatorsForTestCase("CMS.SendMail.Mailinator.link_charset"));

		String url = getBrowser().getWebDriver().findElement(By.xpath("//div[@id='message']/p")).findElement(By.tagName("a")).getAttribute("href");
		return url;
	}

	/**
	 * @author Terry Sun
	 * @description Register a user of the division information.
	 * @param
	 * @return null
	 * @date 2011-12-27
	 */

	public String regUserDivInfo(String mailSearchkey, String testDataLine) throws Exception {
		DateFormat df = new SimpleDateFormat("MMddyyyyhhmm");
		String company = getTestCaseData(testDataLine, "CompanyName") + "_" + df.format(new Date());
		click(getCommLocatorsForTestCase("CMS.InviteUsersDiv_Btn_AcceptRole"));
		setText(getCommLocatorsForTestCase("CMS.InviteUsersDiv_TextBox_CompanyName"), company);
		setText(getCommLocatorsForTestCase("CMS.InviteUsersDiv_TextBox_Address1"), getTestCaseData(testDataLine, "Address1"));
		setText(getCommLocatorsForTestCase("CMS.InviteUsersDiv_TextBox_City"), getTestCaseData(testDataLine, "City"));
		setText(getCommLocatorsForTestCase("CMS.InviteUsersDiv_TextBox_Sate"), getTestCaseData(testDataLine, "Sate"));
		setText(getCommLocatorsForTestCase("CMS.InviteUsersDiv_TextBox_Postal"), getTestCaseData(testDataLine, "Postal"));
		click(getCommLocatorsForTestCase("CMS.InviteUsersDiv_Btn_Submit"));
		setText(getCommLocatorsForTestCase("CMS.Registration.EnterUserInfo_FirstName"), getTestCaseData(testDataLine, "FirstName"));
		setText(getCommLocatorsForTestCase("CMS.Registration.EnterUserInfo_LastName"), getTestCaseData(testDataLine, "LastName"));
		setText(getCommLocatorsForTestCase("CMS.Registration.EnterUserInfo_PhoneNum"), getTestCaseData(testDataLine, "PhoneNumber"));
		setText(getCommLocatorsForTestCase("CMS.Registration.EnterUserInfo_EmailAddress"), mailSearchkey);
		setText(getCommLocatorsForTestCase("CMS.Registration.EnterUserInfo_ReEntEmailAddress"), mailSearchkey);
		click(getCommLocatorsForTestCase("CMS.Registration.EnterUserInfo_ContinueRegBtn"));

		return company;
	}

	// --- END PUBLIC METHOD SECTION
	// -----------------------------------------------------

	// ---------------------------------------------------

	// public void assertTrue(boolean actual) throws Exception {
	// try {
	// org.testng.Assert.assertTrue(actual, "Expected is ture, but actual is false");
	// } catch (AssertionError ae) {
	// throw new Exception(ae.getMessage());
	// }
	// }
	//
	// public void assertTrue(boolean actual, String failMessage) throws Exception {
	// try {
	// org.testng.Assert.assertTrue(actual);
	// } catch (AssertionError ae) {
	// throw new Exception(failMessage);
	// }
	// }
	//
	// public void assertEquals(String actual, String expected) throws Exception {
	// try {
	// org.testng.Assert.assertEquals(actual, expected);
	// } catch (AssertionError ae) {
	// throw new Exception(ae.getMessage());
	// }
	// }
	//
	// public void assertElementVisible(Locators.LocatorType locatorType) throws Exception {
	// try {
	// org.testng.Assert.assertTrue(this.isElementVisible(locatorType));
	// } catch (AssertionError ae) {
	// throw new Exception("assertElementVisible, but " + locatorType.getKey() + "=" + locatorType.getValue() + " is not visible");
	// }
	// }
	//
	// public void assertElementInVisible(Locators.LocatorType locatorType) throws Exception {
	// assertElementExist(locatorType);
	// try {
	// org.testng.Assert.assertFalse(this.isElementVisible(locatorType));
	// } catch (AssertionError ae) {
	// throw new Exception("assertElementVisible, but " + locatorType.getKey() + "=" + locatorType.getValue() + " is visible");
	// }
	// }
	//
	// public void assertTextEquals(String expected, String actual) throws Exception {
	// try {
	// org.testng.Assert.assertEquals(actual, expected);
	// } catch (AssertionError ae) {
	// throw new Exception("assertTextEquals failed, expected=" + expected + " ,but actual =" + actual);
	// }
	// }
	//
	// public void assertElementExist(Locators.LocatorType locatorType) throws Exception {
	// try {
	// org.testng.Assert.assertTrue(this.isElementPresent(locatorType));
	// } catch (AssertionError ae) {
	// throw new Exception("assertElementExist, but " + locatorType.getKey() + "=" + locatorType.getValue() + " does not exist");
	// }
	// }
	//
	// public void assertElementNotExist(Locators.LocatorType locatorType) throws Exception {
	// try {
	// org.testng.Assert.assertFalse(this.isElementPresent(locatorType));
	// } catch (AssertionError ae) {
	// throw new Exception("assertElementNotExist, but " + locatorType.getKey() + "=" + locatorType.getValue() + " does exist");
	// }
	// }

	// ---------------------------------------------------

}
