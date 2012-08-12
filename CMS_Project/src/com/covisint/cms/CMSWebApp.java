package com.covisint.cms;

import java.util.List;
import java.util.Map;
import java.util.Iterator;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import main.java.com.compuware.gdo.framework.core.Browser;
import main.java.com.compuware.gdo.framework.core.Locators;
import main.java.com.compuware.gdo.framework.core.TestData;
import main.java.com.compuware.gdo.framework.core.WebApp;
import main.java.com.compuware.gdo.framework.core.Locators.LocatorType;
import main.java.com.compuware.gdo.framework.core.exceptions.FrameworkException;
import main.java.com.compuware.gdo.framework.utilities.logging.Log;

/**
 * @author Francesco Ferrante Public class OnStarNGWWebApp allows user access to functionality specific to OnStar's NGW web application.
 */
public class CMSWebApp extends WebApp {

	protected WebDriver				driver			= null;
	protected Map<String, String>	parameters		= null;
	protected Locators				commonLocators	= null;
	protected TestData				commonData		= null;
	protected static String			caseId			= "";
	protected Object[]				commonDataItem	= null;
	protected final static String	NEW_LINE		= System.getProperty("line.separator");

	/**
	 * @author Francesco Ferrante
	 * @param testLogFileName
	 * @param browser
	 */
	public CMSWebApp(Log log, Browser browser) {
		super(log, browser);
		this.parameters = browser.getParameters();
		this.driver = browser.getWebDriver();
	}

	// ----------------------------------------------------- START PUBLIC METHOD SECTION ---------------------------------------------------

	public void acceptAlert() {
		if (isAlertPresent()) {
			driver.switchTo().alert().accept();
		}
	}

	public boolean isAlertPresent() {
		try {
			driver.switchTo().alert();
			return true;
		} catch (NoAlertPresentException Ex) {
			return false;
		}
	}

	public void acceptAlertIfFound() {
		try {
			Alert alert = driver.switchTo().alert();
			alert.accept();
		} catch (Exception e) {
			System.out.println("no alert found.");
		}
	}

	public Alert getAlert() {
		return driver.switchTo().alert();
	}

	public String getAlertText() {
		return driver.switchTo().alert().getText();
	}

	public void cancleAlert() {
		driver.switchTo().alert().dismiss();
	}

	public void navigateBack() {
		driver.navigate().back();
		this.waitForPageToLoad();
	}

	/**
	 * @param commonData
	 * 
	 *            Instantiate the Common TestData object to make the common test data available to the entire project.
	 */
	public void setCommonData(String commonData) {
		this.commonData = new TestData(commonData);
	}

	/**
	 * @param commonLocators
	 * 
	 *            Instantiate the Common locators object to make the locators available to the entire project.
	 */
	public void setCommonLocators(String commonLocators) {
		this.commonLocators = new Locators(commonLocators);
	}

	protected Iterator<Object[]> getDataProvider(TestData testdata, String rowName) throws Exception {
		Iterator<Object[]> res = null;
		try {
			// res = testdata.get(concatTestCaseName(rowName));
			res = testdata.get(rowName);
		} catch (Exception e) {
			log.exception(e);
			log.endTestExecution();
			throw e;
		}
		return res;
	}

	private String getCommonTestData(String row, String column) throws Exception {
		if (getDataProvider(commonData, row).hasNext())
			commonDataItem = getDataProvider(commonData, row).next();
		return commonData.getTestDataValue((Object[]) commonDataItem[0], column);
	}

	// /**
	// * @param caseId
	// * Set the caseId for logging.
	// */
	// protected void setCaseId(String caseId) {
	// CMSWebApp.caseId = caseId;
	// }

	/**
	 * @param testcasename
	 *            concat test case name with ENVIRONMENT (can set in "Common_TestSuite.xml").
	 */
	public String concatTestCaseName(String testcasename) {
		StringBuilder casename = new StringBuilder();
		if (testcasename.contains("#")) {
			String[] testcasenames = testcasename.split("#");
			for (int i = 0; i < testcasenames.length; i++) {
				casename.append(parameters.get("ENVIRONMENT"));
				casename.append(testcasenames[i]);
				casename.append("#");
			}
			casename.deleteCharAt(casename.length() - 1);
		} else {
			casename.append(parameters.get("ENVIRONMENT"));
			casename.append(testcasename);
		}
		return casename.toString();

	}

	/**
	 * @author u124699
	 * @description Get dynamic locator.
	 * @param <b>commonLocators is used for Locator retrieving by default<b>
	 * @param key
	 *            -->Locator key in Common_Locators.txt or testcase_Locators.txt<br>
	 * @param value
	 *            -->array of text value which used to replace the position hold %TEXT% in Locator value<br>
	 * @return LocatorType
	 * @date 2011-07-27
	 */
	public LocatorType getDynamicLocatorType(String key, String... value) {
		return getDynamicLocatorType(commonLocators, key, value);
	}

	/**
	 * @author u124699
	 * @description Get dynamic locator.
	 * @param locators
	 *            -->commonLocators or testcaseLocators<br>
	 * @param key
	 *            -->Locator key in Common_Locators.txt or testcase_Locators.txt<br>
	 * @param value
	 *            -->array of text value which used to replace the position hold %TEXT% in Locator value<br>
	 * @return LocatorType
	 * @date 2011-07-27
	 */
	public LocatorType getDynamicLocatorType(Locators locators, String key, String... value) {
		LocatorType locatorType = null;
		try {
			String locatorKey = locators.getLocator(key).getKey();
			String locatorValue = locators.getLocator(key).getValue();
			for (int i = 0; i < value.length; i++) {
				locatorValue = locatorValue.replaceFirst("%TEXT%", value[i]);
				log.comment("CMSWebApp-->getDynamicLocatorType:\t" + locatorValue);
			}
			locatorType = locators.getLocator(locatorKey, locatorValue);
		} catch (Exception e) {
			log.exception(e);
			log.comment("Method Name", "No Locator", "Script did not complete successfully.", Log.FAIL, Log.MAJOR_ISSUE);
		}
		return locatorType;
	}

	/**
	 * @author u124699
	 * @description Check if a specific element displays on current page
	 * @param elementXpath
	 *            --XPATH of the element
	 * @return void
	 * @date 2011-07-02
	 */
	public boolean isElementExistHelper(String elementXpath) {
		boolean isElementExist = false;
		List<WebElement> elements = driver.findElements(By.xpath(elementXpath));

		// for (WebElement e : elements) {
		// if (e.isDisplayed() == true) {
		// isElementExist = true;
		// break;
		// }
		// }
		// return isElementExist;

		System.out.println("Page--->aut.isElementExistHelper:\n\t" + elementXpath + "\n\t" + elements.size() + "\n\t" + elementXpath + "\n\n");
		if (!elements.isEmpty()) {
			isElementExist = true;
		}
		return isElementExist;

	}

	public void verifyElementExist(Locators.LocatorType locatorType) throws FrameworkException, Exception {
		log.verifyStep(isElementPresent(locatorType), "Verify element exist." + NEW_LINE + locatorType.getKey(), "Element exists. Element XPATH:"
				+ NEW_LINE + locatorType.getValue());
	}

	public void verifyElementNotExist(Locators.LocatorType locatorType) throws FrameworkException, Exception {
		log.verifyStep(!isElementPresent(locatorType), "Verify element does not exist." + NEW_LINE + locatorType.getKey(),
				"Element does not exist. Element XPATH:" + NEW_LINE + locatorType.getValue());
	}

	public void verifyElementVisible(Locators.LocatorType locatorType) throws FrameworkException, Exception {
		log.verifyStep(isElementVisible(locatorType), "Verify element visible." + NEW_LINE + locatorType.getKey(),
				"Element is visible. Element XPATH:" + NEW_LINE + locatorType.getValue());
	}

	public void verifyElementInvisible(Locators.LocatorType locatorType) throws FrameworkException, Exception {
		log.verifyStep(!isElementVisible(locatorType), "Verify element invisible." + NEW_LINE + locatorType.getKey(),
				"Element is invisible. Element XPATH:" + NEW_LINE + locatorType.getValue());
	}

	public void verifyTextEquals(String expectedValue, String actualValue) throws FrameworkException, Exception {
		log.verifyCompare(actualValue, expectedValue, "verifyTextEquals");
	}

	/**
	 * 
	 * @param fileName
	 * @return Concat filename with the full directory path.
	 */
	public String getTestDataPath(String fileName) {
		String filePath = "";
		try {
			if (fileName == null) {
				log.comment("File Name is null. Please confirm!!!");
				throw new Exception("File Name is null. Please confirm!!!");
			} else {
				filePath = System.getProperty("user.dir") + parameters.get("testDataDir") + fileName;
				log.comment("File Name is:" + NEW_LINE + "\t" + filePath);
			}
		} catch (Exception e) {
			log.exception(e);
		}
		return filePath;
	}

	/**
	 * 
	 * @param folderHierarchy
	 *            --->(seperated by #)<br>
	 * @return Go to a specific Folder according to the folderHierarchy provided.
	 */
	public void gotoFolder(String folderHierarchy) {
		try {
			if (folderHierarchy == null) {
				log.comment("Folder Hierarchy is null. Please confirm!!!");
				throw new Exception("Folder Hierarchy is null!");

			} else {
				click(commonLocators.getLocator("CMS.ControlPanel.DocAndLib.button_ListView"));
				waitForPageToLoad();
				click(getDynamicLocatorType("CMS.ControlPanel.DocAndLib.link_breadcrumb", "Home"));
				waitForPageToLoad();
				if (!"Home".equals(folderHierarchy)) {
					String[] parentFolder = folderHierarchy.split("#");
					for (int i = 0; i < parentFolder.length; i++) {
						click(getDynamicLocatorType("CMS.ControlPanel.datatable.link-cell_title", parentFolder[i]));
						waitFor(2);
						waitForPageToLoad();
					}
				}

				// for (String folder : parentFolder) {
				// click(getDynamicLocatorType(commonLocators, "CMS.ControlPanel.datatable.link-cell_title", "%COLUMNHEADER%", folder));
				// waitFor(2);
				// waitForPageToLoad();
				// }

			}
		} catch (Exception e) {
			log.exception(e);
			log.comment("Method Name", "No Locator", "Script did not complete successfully.", Log.FAIL, Log.MAJOR_ISSUE);
		}
	}

	/**
	 * @author lance
	 * @Descripton Switch for capture screen shot<br>
	 *             Configured in Common_TestSuite<br>
	 * @return void
	 */
	public void captureScreenShot() {
		if ("Enable".equals(parameters.get("CaptureScreenShot"))) {
			try {
				log.captureScreenShot();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 
	 * @param vinStr
	 * @return boolean This function checks if the VIN is Alpha-Numeric and if it is 17 Characters long.
	 */
	public boolean verifyVINformat(String vinStr) {
		boolean resFlag = false;
		if (vinStr.length() == 17)
			if (vinStr.matches("[a-zA-z0-9]*")) {
				resFlag = true;
			}
		return resFlag;
	}

	// ===============================================Log in start=====================================================

	protected void login(String user, String pwd) throws FrameworkException {
		try {
			if (isInSignOnPage()) {
				doLogin(user, pwd);
			} else {
				log.comment("The SignOn button is not displayed. It's not in sign on page please confirm!");
				throw new Exception("It's not in sign on page please confirm!");
			}
		} catch (Exception e) {
			log.exception(e);
		}
	}

	protected void doLogin(String user, String pwd) throws FrameworkException {
		try {
			log.comment("Input User Name and Password!");
			setText(commonLocators.getLocator("SSO.Login.TextBox_UserName"), user);
			setText(commonLocators.getLocator("SSO.Login.TextBox_PassWord"), pwd);
			click(commonLocators.getLocator("SSO.Login.Btn_SignOn"));
			waitForPageToLoad();
			waitFor(2);
			log.verifyStep(waitForElementPresent(commonLocators.getLocator("CMS.Welcome.TopMenuBar.dockbar")),
					"Verify dockbar presents on the top of Wecome page!", "Dockbar presents on the top of Wecome page!");
		} catch (Exception e) {
			log.exception(e);
		}
	}

	protected boolean isInSignOnPage() {
		boolean present = isElementPresent(commonLocators.getLocator("SSO.Login.Btn_SignOn"));
		return present;
	}

	public void logout() throws FrameworkException {
		try {
			if (!isLogout()) {
				doLogout();
			}
		} catch (Exception e) {
			log.exception(e);
		}
	}

	protected void navigatAndLogin(String url, String user, String pwd) throws FrameworkException {
		try {
			navigateTo(url);
			waitFor(2);
			waitForPageToLoad();
			if (isInSignOnPage()) {
				doLogin(user, pwd);
			} else {
				logout();
				navigateTo(url);
				login(user, pwd);
			}
		} catch (Exception e) {
			log.exception(e);
		}
	}

	protected void doLogout() throws FrameworkException {
		try {
			log.comment("Logout Core portal!");
			click(commonLocators.getLocator("CMS.Welcome.TopMenuBar.link_SignOut"));
			waitForPageToLoad();
			log.verifyStep(waitForElementPresent(commonLocators.getLocator("SSO.Login.Btn_SignOn")), "Verify Log Out successfully!",
					"Log Out successfully!");
		} catch (Exception e) {
			log.exception(e);
		}
	}

	protected boolean isLogout() {
		return !waitForElementPresent(commonLocators.getLocator("CMS.Welcome.TopMenuBar.link_SignOut"), 1);
	}

	protected void navigateTo(String url) {
		browser.navigateTo(url);
		browser.getSelenium().windowMaximize();
	}

	/**
	 * @author u124699
	 * @description User Log in core portal.
	 * @param Object
	 *            [] testDataItem
	 * @return void
	 * @date 2011-06-27
	 */
	public void loginCorePortal(UserRole userRole) throws FrameworkException {
		try {
			log.comment("Log in Core Portal!");
			if (getDataProvider(commonData, userRole.toString()).hasNext())
				commonDataItem = getDataProvider(commonData, userRole.toString()).next();
			switch (userRole) {
			case portal_admin:
				loginAsPortalAdmin((Object[]) commonDataItem[0]);
				break;
			case site_admin:
				loginAsNormalUser((Object[]) commonDataItem[0]);
				break;
			case site_owner:
				loginAsNormalUser((Object[]) commonDataItem[0]);
				break;
			case site_member:
				loginAsNormalUser((Object[]) commonDataItem[0]);
				break;
			case normal_user:
				loginAsNormalUser((Object[]) commonDataItem[0]);
				break;
			}

		} catch (Exception e) {
			log.exception(e);
		}

	}

	/**
	 * @author u124699
	 * @description User Log in core portal as a user.
	 * @param Object
	 *            [] testDataItem
	 * @return void
	 * @date 2011-06-27
	 */
	private void loginAsNormalUser(Object[] testDataItem) throws FrameworkException {
		try {
			log.comment("Log in as " + commonData.getTestDataValue(testDataItem, "TestCaseName") + "!");
			navigatAndLogin(commonData.getTestDataValue(testDataItem, "URL"), commonData.getTestDataValue(testDataItem, "UserName"),
					commonData.getTestDataValue(testDataItem, "PassWord"));
			verifyAdminToolNotExist();
			verifyUserAvatar(commonData.getTestDataValue(testDataItem, "FullName"));
		} catch (Exception e) {
			log.exception(e);
		}
	}

	/**
	 * @author u124699
	 * @description User Log in core portal as admin.
	 * @param Object
	 *            [] testDataItem
	 * @return void
	 * @date 2011-06-27
	 */
	private void loginAsPortalAdmin(Object[] testDataItem) throws FrameworkException {
		try {
			log.comment("Log in as " + commonData.getTestDataValue(testDataItem, "TestCaseName") + "!");
			navigatAndLogin(commonData.getTestDataValue(testDataItem, "URL"), commonData.getTestDataValue(testDataItem, "UserName"),
					commonData.getTestDataValue(testDataItem, "PassWord"));
			verifyAdminToolExist();
			verifyUserAvatar(commonData.getTestDataValue(testDataItem, "FullName"));
		} catch (Exception e) {
			log.exception(e);
		}
	}

	public void verifyUserAvatar(String fullName) throws FrameworkException {
		try {
			log.comment("Check Go To dorp down menu and User Name displayed!");
			verifyElementExist(commonLocators.getLocator("CMS.Welcome.TopMenuBar.avatar_UserAvatar"));
			verifyElementExist(commonLocators.getLocator("CMS.Welcome.TopMenuBar.link_SignOut"));
			verifyElementExist(commonLocators.getLocator("CMS.Welcome.TopMenuBar.menu-button_GoTo"));
			verifyElementExist(getDynamicLocatorType("CMS.Welcome.TopMenuBar.link_FullName", fullName));
		} catch (Exception e) {
			log.exception(e);
		}
	}

	public void verifyAdminToolExist() throws FrameworkException {
		try {
			log.comment("Check Add and Manage dorp down menu displayed!");
			verifyElementExist(commonLocators.getLocator("CMS.Welcome.TopMenuBar.menu-button_Add"));
			verifyElementExist(commonLocators.getLocator("CMS.Welcome.TopMenuBar.menu-button_Manage"));
			verifyElementExist(commonLocators.getLocator("CMS.Welcome.TopMenuBar.toggle_Edit"));
		} catch (Exception e) {
			log.exception(e);
		}
	}

	public void verifyAdminToolNotExist() throws FrameworkException {
		try {
			log.comment("Check Add and Manage dorp down menu are not displayed!");
			verifyElementNotExist(commonLocators.getLocator("CMS.Welcome.TopMenuBar.menu-button_Add"));
			verifyElementNotExist(commonLocators.getLocator("CMS.Welcome.TopMenuBar.menu-button_Manage"));
			verifyElementNotExist(commonLocators.getLocator("CMS.Welcome.TopMenuBar.toggle_Edit"));
		} catch (Exception e) {
			log.exception(e);
		}
	}

	// ===============================================Log in end=====================================================

	// ----------------------------------------------------- END PUBLIC METHOD SECTION -----------------------------------------------------

	/**
	 * @author u124699
	 * @description Every selection in this enum denote a specific User Role Core Portal <br>
	 *              portal_admin-->portal admin<br>
	 *              site_admin-->site admin<br>
	 *              site_owner-->site owner<br>
	 *              site_member-->site member<br>
	 *              site_user-->site user<br>
	 * @date 2011-07-26
	 */
	public enum UserRole {

		// Used for portal admin log in
		portal_admin,

		// Used for site admin log in
		site_admin,

		// Used for site owner log in
		site_owner,

		// Used for site member log in
		site_member,

		// Used for normal user log in
		normal_user,

		// Used for Guest log in
		power_user,

		// Used for Guest log in
		guest,

	}

}
