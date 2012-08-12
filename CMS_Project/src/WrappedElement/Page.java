package WrappedElement;

import java.io.IOException;
import java.util.List;

import com.covisint.cms.CMSWebApp;
import main.java.com.compuware.gdo.framework.utilities.logging.Log;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

public class Page {

	// public CMS_GENERIC_Lib aut;
	public CMSWebApp			aut;
	public WebDriver			driver;
	public Log					log;
	private final static String	NEW_LINE			= System.getProperty("line.separator");

	private final String		pageTitleXpath		= "//h1[@id='cpPortletTitle']/span[contains(text(),'%TITLE%')]";
	private final String		sectionTitleXpath	= "//h1[@class='header-title']/span[contains(text(),'%TITLE%')]";
	private final String		goBackXpath			= "//span[@class='header-back-to']/a[contains(text(),'Back')]";
	// private final String messageXpath = "//div[@class='portlet-msg-success' and contains(text(),'%MESSAGE%')]";
	private final String		messageXpath		= "//div[contains(@class,'portlet-msg-success') and contains(text(),'%MESSAGE%')]";
	private final String		backToViewPageXpath	= "//a[@class='portlet-icon-back nobr' and contains(text(),'Back to')]";

	private final String		stagingButton		= "//div[@class='lfr-header-row-content' and contains(.,'Staging')]//a[@class='nobr']";
	private final String		publishTypeXPath	= "//a[@role='menuitem' and contains(.,'%ITEM%')]";
	private final String		publishNow			= "Publish to Remote Live Now";
	private final String		schedulePublish		= "Schedule Publication to Remote Live";
	private final String		dialogTitleXPath	= "//div[@role='dialog']//span[text()='%ITEM%']";

	private TopMenuBar			toolbar				= null;
	private SecondTopMenuBar	toolbar2			= null;
	private ControlPanelMenu	controlpanel		= null;
	private RightSideMenu		menuform			= null;
	private NavigationBar		navBar				= null;
	private PermissionDialog	permissiondialog	= null;

	// public Page(CMS_GENERIC_Lib aut, Log log) {
	public Page(CMSWebApp aut, Log log) {
		this.aut = aut;
		this.log = log;
		driver = aut.getBrowser().getWebDriver();
		this.toolbar = new TopMenuBar();
		this.toolbar2 = new SecondTopMenuBar();
		this.controlpanel = new ControlPanelMenu();
		this.menuform = new RightSideMenu();
		this.navBar = new NavigationBar();
		this.permissiondialog = new PermissionDialog();
	}

	// public static Page getPage(CMS_GENERIC_Lib aut) {
	// return new Page(aut);
	// }

	/**
	 * @author Lance Yan
	 * @description Click Ok in pop up message box by Autoit scripts<br>
	 *              This can be done by CMS_GENERIC_lib.acceptAlert() too;
	 * @param
	 * @return void
	 * @date 2012-07-02
	 */
	public void click_Ok() {
		try {
			Runtime.getRuntime().exec("../CMS_Project/tool/Delete_Page_OK.exe");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @author Lance Yan
	 * @description Upload a file by Autoit scripts
	 * @param
	 * @return void
	 * @date 2012-07-26
	 */
	public void UploadFile(String browser, String filePath) {
		try {
			Runtime.getRuntime().exec("../CMS_Project/tool/UploadFile.exe " + browser + " " + filePath);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @author Lance Yan
	 * @description Click "Back" in right end of section title bar.
	 * @param
	 * @return void
	 * @date 2012-07-02
	 */
	public void goBack() {
		driver.findElement(By.xpath(goBackXpath)).click();
		aut.waitFor(2);
		aut.waitForPageToLoad();
	}

	/**
	 * @author Lance Yan
	 * @description Click "Back to covisint.com" on the top tool bar of page.
	 * @param
	 * @return void
	 * @date 2012-07-24
	 */
	public void goBackToViewPage() {
		if (aut.isElementExistHelper(backToViewPageXpath)) {
			driver.findElement(By.xpath(backToViewPageXpath)).click();
			aut.waitFor(3);
		}
	}

	/**
	 * @author Lance Yan
	 * @description Check if a specific page title displays on the page.
	 * @param title
	 *            -->title of the page(Sites, Site Pages, etc.)
	 * @param caseId
	 * @return void
	 * @date 2012-07-02
	 */
	public boolean verifyPageTitle(String title) {
		boolean flag = false;
		String titleXpath = pageTitleXpath.replace("%TITLE%", title);
		try {
			log.comment("**********Check if a specific page title displays on the page.**********");
			flag = log.verifyStep(aut.isElementExistHelper(titleXpath), "verify Page title displays", "Page " + title + " displays");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;

	}

	/**
	 * @author Lance Yan
	 * @description Check if a specific section title displays on the page.
	 * @param title
	 *            -->title of the section(Add New Site, Edit Site, etc.)
	 * @param caseId
	 * @return void
	 * @date 2012-07-02
	 */
	public boolean verifysectionTitle(String title) {
		boolean flag = false;
		String titleXpath = sectionTitleXpath.replace("%TITLE%", title);
		try {
			log.comment("**********Check if a specific section title displays on the page.**********");
			flag = log.verifyStep(aut.isElementExistHelper(titleXpath), "verify Section title displays", "Section " + title + " displays");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}

	/**
	 * @author Lance Yan
	 * @description Check if message <br>
	 *              "Your request completed successfully."<br>
	 *              or "Your request processed successfully."<br>
	 *              displays on the page.<br>
	 * @param caseId
	 * @return void
	 * @date 2012-07-02
	 */
	public boolean verifySuccessMessage() {
		boolean flag = false;
		// flag = verifyMessageHelper("Your request completed successfully.");
		// flag = verifyMessageHelper("Your request processed successfully.");
		flag = verifyMessageHelper("successfully");
		return flag;
	}

	/**
	 * @author Lance Yan
	 * @description Check if message <br>
	 *              "You now have an indefinite lock on this document. No one else can edit this document until you unlock it. This lock will never expire. "
	 * <br>
	 *              displays on the page.<br>
	 * @param caseId
	 * @return void
	 * @date 2012-08-01
	 */
	public boolean verifyCheckOutMessage() {
		boolean flag = false;
		flag = verifyMessageHelper("You now have an indefinite lock on this document. No one else can edit this document until you unlock it. This lock will never expire.");
		return flag;
	}

	/**
	 * @author Lance Yan
	 * @description Check if a specific message displays on the page.<br>
	 * @param msg
	 *            --> message
	 * @param caseId
	 * @return void
	 * @date 2012-07-02
	 */
	public boolean verifyMessageHelper(String msg) {
		boolean flag = false;
		String msgXpath = messageXpath.replace("%MESSAGE%", msg);
		try {
			log.comment("**********Check if a specific message displays on the page.**********");
			flag = log.verifyStep(aut.isElementExistHelper(msgXpath), "verify Interactive Message displays", "Message: \n" + msg + "\ndisplays");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}

	/**
	 * @author Lance Yan
	 * @description Go to a specific page via "Go To" menu<br>
	 * @param pageName
	 *            --> Control Panel, View page of a specific site, etc.
	 * @param caseId
	 * @return void
	 * @date 2012-07-02
	 */
	public void goToPage(String pageName) {
		toolbar.goTo(pageName);
	}

	/**
	 * @author u124699
	 * @description Open the [Publish to Remote Live Now] dialog by clicking the item name in the Staging menu
	 * @return void
	 * @date 2011-08-02
	 */
	public void openDialogPublishNow() {

		log.comment("**********Open [" + publishNow + "] Dialog**********");

		try {
			if (aut.isElementExistHelper(stagingButton)) {
				driver.findElement(By.xpath(stagingButton)).click();
				aut.waitFor(3);
				toolbar2.selectPublishType(publishNow);
			} else {
				toolbar2.remotePublish(publishNow);
			}

			// verifyStep(boolean condition, String verifyName, String passMessage)
			String dialogTitle = dialogTitleXPath.replace("%ITEM%", publishNow);
			log.verifyStep(aut.isElementExistHelper(dialogTitle), "Open dialog: " + publishNow, "Dialog [" + publishNow + "] displays!");

		} catch (Exception e) {
			log.exception(e);
			log.comment("Method Name", "No Locator", "Script did not complete successfully.", Log.FAIL, Log.MAJOR_ISSUE);
		}
	}

	/**
	 * @author u124699
	 * @description Open the [Schedule Publication to Remote Live] dialog by clicking the item name in the Staging menu
	 * @return void
	 * @date 2011-08-02
	 */
	public void openDialogSchedulePublish() {

		log.comment("**********Open [" + schedulePublish + "] Dialog**********");

		try {
			if (aut.isElementExistHelper(stagingButton)) {
				driver.findElement(By.xpath(stagingButton)).click();
				aut.waitFor(3);
				toolbar2.selectPublishType(schedulePublish);
			} else {
				toolbar2.remotePublish(schedulePublish);
			}

			// verifyStep(boolean condition, String verifyName, String passMessage)
			String dialogTitle = dialogTitleXPath.replace("%ITEM%", schedulePublish);
			log.verifyStep(aut.isElementExistHelper(dialogTitle), "Open dialog: " + schedulePublish, "Dialog [" + schedulePublish + "] displays!");

		} catch (Exception e) {
			log.exception(e);
			log.comment("Method Name", "No Locator", "Script did not complete successfully.", Log.FAIL, Log.MAJOR_ISSUE);
		}
	}

	/**
	 * @author u124699
	 * @description Open Remote Live page by clicking Go to Remote Live Button
	 * @return void
	 * @date 2011-08-02
	 */
	public void openRemotePage() {
		toolbar2.goRemoteLive();
	}

	/**
	 * @author Lance Yan
	 * @description Open a specific page in "Control Panel" menu<br>
	 * @param menuName
	 *            -->my, content, portal, server
	 * @param pageName
	 *            -->Users and Organizations, Sites, Site Templates, etc.
	 * @return void
	 * @date 2012-07-02
	 */
	public void openPage(Menu menuNameEnum, String pageName) {
		controlpanel.openItemPage(menuNameEnum, pageName);
	}

	/**
	 * @author Lance Yan
	 * @description Go to a specific attribute page of Site or Page by clicking the menu item in right side menu
	 * @param itemName
	 *            -->Staging, Analytics, etc.
	 * @return void
	 * @date 2012-07-02
	 */
	public void openFrame(String frameName) {
		menuform.openAttributePage(frameName);
	}

	/**
	 * @author u124699
	 * @description Click button on the right side menu
	 * @param buttonName
	 *            -->Save, Cancel, etc.
	 * @return void
	 * @date 2011-07-02
	 */
	public void clickButton(String buttonName) {
		menuform.clickButton(buttonName);
	}

	/**
	 * @author u124699
	 * @description Add a specific item to the current site or page
	 * @param itemName
	 *            -->Page, Asset Publisher, etc.
	 * @return void
	 * @date 2011-07-02
	 */
	public void addPortlet(String portletName) {
		toolbar.addMenu(portletName);
	}

	/**
	 * @author u124699
	 * @description Add a specific Web Utility to the current site or page
	 * @param webUtility
	 *            -->Page, Asset Publisher, etc.
	 * @return void
	 * @date 2011-07-11
	 */
	public void manageUtility(String webUtility) {
		toolbar.manage(webUtility);
	}

	/**
	 * @author u124699
	 * @description Open a specific page by clicking its name on navigation bar
	 * @param pageName
	 *            -->Home, Search, etc.
	 * @return void
	 * @date 2011-07-02
	 */
	public void viewPageViaNavBar(String pageName) {
		navBar.openPageOnNavigationBar(pageName);
	}

	/**
	 * @author u124699
	 * @description Delete a specific page by clicking its name on navigation bar
	 * @param pageName
	 *            -->Home, Search, etc.
	 * @return void
	 * @date 2011-07-02
	 */
	public boolean deletePageOnNavigationBar(String pageName) {
		return navBar.deletePageOnNavigationBar(pageName);
	}

	/**
	 * @author u124699
	 * @description verify Site name displays on the header of the page in view page
	 * @param siteName
	 * @return boolean
	 * @date 2011-07-02
	 */
	public boolean headerCheck(String siteName) {
		boolean flag = false;
		try {
			log.comment("**********Check if the Site name displays on the header of the page in view page.**********");
			flag = log.verifyStep(navBar.verifySiteName(siteName), "verify Site name displays on the header of the page in view page",
					"Site Name: \n" + siteName + "\ndisplays");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}

	/**
	 * @author u124699
	 * @description verify Page name displays on the navigation bar in view page
	 * @param pageName
	 * @return boolean
	 * @date 2011-07-02
	 */
	public boolean verifyPageName(String pageName) {
		boolean flag = false;
		try {
			log.comment("**********Check if the Page name displays on the navigation bar in view page.**********");
			flag = log.verifyStep(navBar.isPageDisplay(pageName), "verify Page name displays on the navigation bar in view page", "Page Name: \n"
					+ pageName + "\ndisplays");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}

	/**
	 * @author u124699
	 * @description Select a specific site as current site in the control panel menu
	 * @param siteName
	 * @return void
	 * @date 2011-07-03
	 */
	public void setCurrentSite(String siteName) {
		controlpanel.selectSite(siteName);
	}

	/**
	 * @author u124699
	 * @description set permissions for a specific role
	 * @param role
	 * @param permissions
	 * @return void
	 * @date 2011-08-08
	 */
	public void setPermissions(CMSWebApp.UserRole role, Page.UserActions... permissions) {
		log.comment("**********Set on Permissions for role: " + role.toString() + "**********");
		for (int i = 0; i < permissions.length; i++) {
			permissiondialog.checkOnPermission(role, permissions[i]);
		}
	}

	/**
	 * @author u124699
	 * @description set off permissions for a specific role
	 * @param role
	 * @param permissions
	 * @return void
	 * @date 2011-08-08
	 */
	public void unsetPermissions(CMSWebApp.UserRole role, Page.UserActions... permissions) {
		log.comment("**********Set Off Permissions for role: " + role.toString() + "**********");
		for (int i = 0; i < permissions.length; i++) {
			permissiondialog.checkOffPermission(role, permissions[i]);
		}
	}

	/**
	 * @author u124699
	 * @description Verify permissions for a specific role
	 * @param role
	 * @param permissions
	 * @return void
	 * @date 2011-08-09
	 */
	public void verifyPermissions(CMSWebApp.UserRole role, Page.UserActions... permissions) {
		log.comment("**********Verify permissions for a specific role**********");
		for (int i = 0; i < permissions.length; i++) {
			permissiondialog.verifyPermission(role, permissions[i]);
		}
	}

	/**
	 * @author u124699
	 * @description Verify No permissions is set on for a specific role
	 * @param role
	 * @param permissions
	 * @return void
	 * @date 2011-08-09
	 */
	public void verifyNoPermissions(CMSWebApp.UserRole role) {
		log.comment("**********Verify permissions for a specific role**********");
		permissiondialog.verifyAllPermissionsOff(role);

	}

	/**
	 * @author u124699
	 * @description set off all permissions for a specific role
	 * @param role
	 * @return void
	 * @date 2011-08-08
	 */
	public void setOffAllPermissions(CMSWebApp.UserRole role) {
		log.comment("**********Set Off all Permissions for role: " + role.toString() + "**********");
		permissiondialog.checkOffAllPermission(role);
	}

	/**
	 * @author u124699
	 * @description set on all permissions for a specific role
	 * @param role
	 * @return void
	 * @date 2011-08-08
	 */
	public void setOnAllPermissions(CMSWebApp.UserRole role) {
		log.comment("**********Set On all Permissions for role: " + role.toString() + "**********");
		permissiondialog.checkOnAllPermission(role);
	}

	/**
	 * @author u124699
	 * @description Emulate mouseover by mouseMove+Click
	 * @param elementXpath
	 * @return void
	 * @date 2011-07-11
	 */
	public void mouseMove_Click(String elementXpath) {
		aut.getBrowser().getSelenium().mouseMove(elementXpath);
		driver.findElement(By.xpath(elementXpath)).click();
		aut.waitFor(2);
		aut.waitForPageToLoad();
	}

	/**
	 * @author u124699
	 * @description Emulate mouseover by mouseMove+Click
	 * @param elementXpath
	 * @return void
	 * @date 2011-08-01
	 */
	public void mouseMove_mouseOver(String elementXpath) {
		aut.getBrowser().getSelenium().mouseMove(elementXpath);
		aut.getBrowser().getSelenium().mouseOver(elementXpath);
		aut.waitFor(2);
	}

	// public boolean exists(String elementXpath) {
	// int timeout = aut.getWaitTimeout();
	// try {
	// driver.findElement(By.xpath(elementXpath));
	// return true;
	// } catch (Exception e) {
	// return false;
	// } finally {
	// aut.setWaitTimeout(timeout);
	// }
	// }
	//
	// public boolean isVisible(String elementXpath) {
	// if (exists(elementXpath)) {
	// return driver.findElement(By.xpath(elementXpath)).isDisplayed();
	// } else {
	// return false;
	// }
	// }

	// ---------------------------------------------------------------------------------------------------------------------
	// ---------------------------------------------------Start of Inner Classes--------------------------------------------
	// ---------------------------------------------------------------------------------------------------------------------

	// ----------------Start of TopMenuBar----------------------------------------------------------------------------------
	public class TopMenuBar {

		// public CMS_GENERIC_Lib aut;
		// public WebDriver driver;
		//
		public String	commonPath	= "//div[@class='portlet-body']/div[@id='dockbar']";

		// public TopMenuBar(CMS_GENERIC_Lib aut) {
		// this.aut = aut;
		// driver = aut.getBrowser().getWebDriver();
		// }

		/**
		 * @author u124699
		 * @description Open a specific page by clicking the item name in the Go to menu
		 * @param itemName
		 *            -->Control Panel, {Site Name}, etc.
		 * @return void
		 * @date 2011-06-29
		 */
		public void goTo(String itemName) {
			try {
				log.comment("**********Go To " + itemName + " page**********");
				String gotoXPath = commonPath + "/ul[@class='aui-toolbar user-toolbar']/li/a[contains(.,'Go to')]";
				// aut.mouseOver(gotoXPath);
				// driver.findElement(By.xpath(gotoXPath)).click();
				mouseMove_Click(gotoXPath);

				// String itemXPath = "//div[starts-with(@id,'aui_')]/ul[starts-with(@id,'aui_')]/li/a[contains(.,'" + itemName + "')]/span";
				String itemXPath = "//span[contains(text(),'" + itemName + "')]";
				if (aut.isElementExistHelper(itemXPath)) {
					driver.findElement(By.xpath(itemXPath)).click();
				} else {
					System.out.println("Try to open " + itemName + " page!");
					throw new Exception("Method Name: goTo, but Menu Item: " + itemName + NEW_LINE + "XPATH: " + itemXPath + NEW_LINE
							+ "does not exist");
				}
				aut.waitFor(3);
				aut.waitForPageToLoad();
				// verifyStep(boolean condition, String verifyName, String passMessage)

				if (itemName.contains("Control Panel")) {
					// log.verifyStep(aut.isElementExistHelper(backToViewPageXpath), "Go To Page: " + itemName, "Page [" + itemName + "] displays!");
					log.verifyStep(driver.getTitle().contains("Control Panel"), "Go To Page: " + itemName, "Page [" + itemName + "] displays!");
				} else {
					log.verifyStep(navBar.verifySiteName(itemName), "Go To Page: " + itemName, "Page [" + itemName + "] displays!");
				}

			} catch (Exception e) {
				log.exception(e);
				log.comment("Method Name", "No Locator", "Script did not complete successfully.", Log.FAIL, Log.MAJOR_ISSUE);
			}
		}

		/**
		 * @author u124699
		 * @description Add a specific item to the current site or page
		 * @param itemName
		 *            -->Page, Asset Publisher, etc.
		 * @return void
		 * @date 2011-07-02
		 */
		public void addMenu(String itemName) {
			String addXPath = commonPath + "/ul[@class='aui-toolbar']/li/a[contains(.,'Add')]";
			String baseXPath = "//div[contains(@class,'add-content-menu')]/div/ul/li";
			String itemXPath = null;
			String menuItemXPath = null;
			try {
				if (aut.isElementExistHelper(addXPath)) {
					// aut.mouseOver(addXPath);
					// mouseOver(driver.findElement(By.xpath(addXPath)));
					// mouseOverAction(addXPath, itemXPath);
					// driver.findElement(By.xpath(addXPath)).click();
					mouseMove_Click(addXPath);
				} else {
					System.out.println("Try to open Add drop down list!");
					log.comment("Try to open Add drop down list!");
					throw new Exception("Method Name: addMenu, but Menu: Add" + NEW_LINE + "XPATH: " + addXPath + NEW_LINE + "does not exist");
				}

				if ("Page".equals(itemName)) {
					log.comment("**********Add a Page to the current site.**********");
					menuItemXPath = baseXPath + "/a[contains(.,'%ITEM%')]";
				} else {
					log.comment("**********Add a specific portlet to the current page.**********");
					menuItemXPath = "//div/ul/li/a[contains(.,'%ITEM%')]";
				}
				itemXPath = menuItemXPath.replace("%ITEM%", itemName);
				if (aut.isElementExistHelper(itemXPath)) {
					driver.findElement(By.xpath(itemXPath)).click();
					aut.waitFor(2);
				} else {
					System.out.println("Try to add " + itemName + " portlet!");
					log.comment("Try to add [" + itemName + "] portlet!");
					throw new Exception("Method Name: addMenu, but Menu Item: " + itemName + NEW_LINE + "XPATH: " + itemXPath + NEW_LINE
							+ "does not exist");

				}
				aut.waitForPageToLoad();

			} catch (Exception e) {
				log.exception(e);
				log.comment("Method Name", "No Locator", "Script did not complete successfully.", Log.FAIL, Log.MAJOR_ISSUE);
			}
		}

		/**
		 * @author u124699
		 * @description Open a specific item under Manage drop down menu.
		 * @param itemName
		 *            -->Page, Asset Publisher, etc.
		 * @return void
		 * @date 2011-07-11
		 */
		public void manage(String itemName) {
			String manageXPath = commonPath + "/ul[@class='aui-toolbar']/li/a[contains(.,'Manage')]";
			String menuItemXPath = "//div[contains(@class,'manage-content-menu')]/div/ul/li/a[text()='%ITEM%']";

			try {
				if (aut.isElementExistHelper(manageXPath)) {
					mouseMove_Click(manageXPath);
				} else {
					System.out.println("Try to open Manage drop down list!");
					log.comment("Try to open Manage drop down list!");
					throw new Exception("Method Name: manage, but Menu: Manage" + NEW_LINE + "XPATH: " + manageXPath + NEW_LINE + "does not exist");
				}

				String itemXPath = menuItemXPath.replace("%ITEM%", itemName);
				if (aut.isElementExistHelper(itemXPath)) {
					log.comment("**********Open a specific item under Manage drop down menu.**********");
					driver.findElement(By.xpath(itemXPath)).click();
					aut.waitFor(3);
				} else {
					System.out.println("Try to manage [" + itemName + "] portlet!");
					log.comment("Try to manage [" + itemName + "] portlet!");
					throw new Exception("Method Name: manage, but Menu Item: " + itemName + NEW_LINE + "XPATH: " + itemXPath + NEW_LINE
							+ "does not exist");
				}
				aut.waitFor(2);
				aut.waitForPageToLoad();

			} catch (Exception e) {
				log.exception(e);
				log.comment("Method Name", "No Locator", "Script did not complete successfully.", Log.FAIL, Log.MAJOR_ISSUE);
			}
		}

		/**
		 * @author u124699
		 * @description Emulate mouseover by Actions Builder
		 * @param elementXpath
		 * @return void
		 * @date 2011-07-11
		 */
		private void mouseOverAction(String menuXpath, String optionXpath) {
			// get the element that shows menu with the mouseOver event
			WebElement menu = driver.findElement(By.xpath(menuXpath));

			// build and perform the mouseOver with Advanced User Interactions API
			Actions builder = new Actions(driver);
			builder.moveToElement(menu).build().perform();

			// the element that I want to click (hidden)
			WebElement menuOption = driver.findElement(By.xpath(optionXpath));

			// then click when menu option is visible
			menuOption.click();

			// WebElement menuRegistrar = driver.findElement(By.xpath(menuXpath));
			// Actions builder = new Actions(driver); // Or maybe seleniumDriver? Not sure which one to use
			// Actions hoverOverRegistrar = builder.moveToElement(menuRegistrar);
			// hoverOverRegistrar.perform();

			aut.waitForPageToLoad();
		}

		/**
		 * @author u124699
		 * @description Emulate mouseover by executeScript
		 * @param elementXpath
		 * @return void
		 * @date 2011-07-11
		 */
		private void mouseOver(WebElement element) {
			String code = "var fireOnThis = arguments[0];" + "var evObj = document.createEvent('MouseEvents');"
					+ "evObj.initEvent( 'mouseover', true, true );" + "fireOnThis.dispatchEvent(evObj);";
			((JavascriptExecutor) driver).executeScript(code, element);
		}
	}

	// --------------------End of TopMenuBar-----------------------------------------------------------------------------------

	// ----------------Start of SecondTopMenuBar----------------------------------------------------------------------------------
	class SecondTopMenuBar {

		public String	commonPath		= "//div[@class='staging-bar']";
		public String	remoteLiveXPath	= commonPath + "//span[@class='aui-tab-label']/a[@class='taglib-icon' and contains(.,'Go to Remote Live')]";

		/**
		 * @author u124699
		 * @description Open a specific dialog by clicking the item name in the Staging menu
		 * @param itemName
		 * <br>
		 *            Publish to Remote Live Now <br>
		 *            Schedule Publication to Remote Live
		 * @return void
		 * @date 2011-08-02
		 */
		public void remotePublish(String itemName) {
			try {
				String stagingXPath = commonPath + "//span[@class='aui-tab-label' and contains(.,'Staging')]//a[@class='nobr']";
				if (aut.isElementExistHelper(stagingXPath)) {
					aut.getBrowser().getSelenium().mouseMove(stagingXPath);
					driver.findElement(By.xpath(stagingXPath)).click();
					aut.waitFor(2);
				} else {
					System.out.println("Try to Open Staging drop down list!");
					log.comment("Try to Open Staging drop down list!");
					throw new Exception("Method Name: remotePublishNow, but Menu: Staging" + NEW_LINE + "XPATH: " + stagingXPath + NEW_LINE
							+ "does not exist");
				}

				selectPublishType(itemName);

			} catch (Exception e) {
				log.exception(e);
				log.comment("Method Name", "No Locator", "Script did not complete successfully.", Log.FAIL, Log.MAJOR_ISSUE);
			}
		}

		/**
		 * @author u124699
		 * @description Clicking Go to Remote Live Button
		 * @return void
		 * @date 2011-08-06
		 */
		public void selectPublishType(String itemName) {
			try {
				String itemXPath = publishTypeXPath.replace("%ITEM%", itemName);
				if (aut.isElementExistHelper(itemXPath)) {
					driver.findElement(By.xpath(itemXPath)).click();
				} else {
					System.out.println("Try to open [" + itemName + "] dialog!");
					log.comment("Try to open [" + itemName + "] dialog!");
					throw new Exception("Method Name: selectPublishType, but Menu Item: " + itemName + NEW_LINE + "XPATH: " + itemXPath + NEW_LINE
							+ "does not exist");
				}
				aut.waitFor(3);
				aut.waitForPageToLoad();

			} catch (Exception e) {
				log.exception(e);
				log.comment("Method Name", "No Locator", "Script did not complete successfully.", Log.FAIL, Log.MAJOR_ISSUE);
			}
		}

		/**
		 * @author u124699
		 * @description Open Remote Live page by clicking Go to Remote Live Button
		 * @return void
		 * @date 2011-08-02
		 */
		public void goRemoteLive() {
			try {
				if (aut.isElementExistHelper(remoteLiveXPath)) {
					driver.findElement(By.xpath(remoteLiveXPath)).click();
					aut.waitFor(1);
					aut.acceptAlert();
					aut.waitFor(3);

				} else {
					System.out.println("Try to open Remote Live Page!");
					log.comment("Try to open Remote Live Page!");
					throw new Exception("Method Name: goRemoteLive, but Go to Remote Live Button" + NEW_LINE + "XPATH: " + remoteLiveXPath + NEW_LINE
							+ "does not exist");
				}
				log.verifyStep(false, "Open Remote Live Page", "Remote Live Page displays!");

			} catch (Exception e) {
				log.exception(e);
				log.comment("Method Name", "No Locator", "Script did not complete successfully.", Log.FAIL, Log.MAJOR_ISSUE);
			}
		}
	}

	// --------------------End of SecondTopMenuBar-----------------------------------------------------------------------------------

	// --------------------Start of NavigationBar------------------------------------------------------------------------------
	class NavigationBar {

		// public CMS_GENERIC_Lib aut;
		// public WebDriver driver;
		private String	commonPath		= "//header[@id='banner']";
		private String	breadcrumb		= "//nav[@id='breadcrumbs']//a[contains(text(),'%PAGE%')]";
		private String	siteNameXpath	= null;
		private String	pageNameXpath	= null;

		// public NavigationBar(CMS_GENERIC_Lib aut) {
		// this.aut = aut;
		// driver = aut.getBrowser().getWebDriver();
		// }

		/**
		 * @author u124699
		 * @description Open a specific page by clicking its name on navigation bar
		 * @param itemName
		 *            -->Home, Search, etc.
		 * @return void
		 * @date 2011-07-02
		 */
		public void openPageOnNavigationBar(String pageName) {
			pageNameXpath = commonPath + "/nav[@id='navigation']/ul/li/a[contains(.,'" + pageName + "')]";

			try {
				if (aut.isElementExistHelper(pageNameXpath)) {
					log.comment("**********Open a specific page by clicking its name on navigation bar.**********");

					// driver.findElement(By.xpath(pageNameXpath)).click();
					mouseMove_Click(pageNameXpath);
				} else {
					System.out.println("Try to open page in navigation bar!");
					log.comment("Try to open page in navigation bar!");
					throw new Exception("Method Name: openPageOnNavigationBar, but Page Name: " + pageName + NEW_LINE + "XPATH: " + pageNameXpath
							+ NEW_LINE + "does not exist");
				}

				aut.waitForPageToLoad();

				String pageInbreadcrumbXpath = breadcrumb.replace("%PAGE%", pageName);
				log.verifyStep(aut.isElementExistHelper(pageInbreadcrumbXpath), "View [" + pageName + "] Page",
						"Page name displays on breadcrumb successfully!" + NEW_LINE + "Xpath: " + pageInbreadcrumbXpath);

			} catch (Exception e) {
				log.exception(e);
				log.comment("Method Name", "No Locator", "Script did not complete successfully.", Log.FAIL, Log.MAJOR_ISSUE);
			}
		}

		/**
		 * @author u124699
		 * @description Delete a specific page by clicking its name on navigation bar
		 * @param pageName
		 *            -->Home, Search, etc.
		 * @return void
		 * @date 2011-07-02
		 */
		public boolean deletePageOnNavigationBar(String pageName) {
			boolean deleted = false;
			pageNameXpath = commonPath + "/nav[@id='navigation']/ul/li/a[contains(.,'" + pageName + "')]";
			try {
				if (aut.isElementExistHelper(pageNameXpath)) {
					// mouseOverHelper(pageNameXpath);
					mouseMove_mouseOver(pageNameXpath);
				} else {
					System.out.println("\n\n\nPage-->NavigationBar-->deletePage: No such page exists!\n\n\n");
					throw new Exception("Method Name: deletePageOnNavigationBar, but Page: " + pageName + NEW_LINE + "XPATH: " + pageNameXpath
							+ NEW_LINE + "does not exist");
				}
				String iconXpath = commonPath + "/nav[@id='navigation']/ul/li/span[@class='delete-tab' and text()='X']";
				if (aut.isElementExistHelper(iconXpath)) {
					log.comment("**********Delete a specific page by clicking its name on navigation bar.**********");
					driver.findElement(By.xpath(iconXpath)).click();
					aut.waitFor(2);
					aut.acceptAlert();
					aut.waitFor(1);
					deleted = !aut.isElementExistHelper(pageNameXpath);
				} else {
					throw new Exception("deletePageOnNavigationBar, but Button: Delete" + NEW_LINE + "XPATH:" + iconXpath + NEW_LINE
							+ "does not exist");
				}

			} catch (Exception e) {
				log.exception(e);
				log.comment("Method Name", "No Locator", "Script did not complete successfully.", Log.FAIL, Log.MAJOR_ISSUE);
			}
			return deleted;

		}

		/**
		 * @author u124699
		 * @description verify Site name displays on the header of the page in view page
		 * @param siteName
		 * @return boolean
		 * @date 2011-07-02
		 */
		public boolean verifySiteName(String siteName) {
			siteNameXpath = commonPath + "/div[@id='heading']/h1/span[contains(.,'" + siteName + "')]";
			return aut.isElementExistHelper(siteNameXpath);
		}

		/**
		 * @author u124699
		 * @description verify Page name displays on the navigation bar in view page
		 * @param pageName
		 * @return boolean
		 * @date 2011-07-02
		 */
		public boolean isPageDisplay(String pageName) {
			String pageXpath = "//nav[@id='navigation']/ul/li/a[contains(.,'" + pageName + "')]";
			return aut.isElementExistHelper(pageXpath);
		}

	}

	// --------------------End of NavigationBar-----------------------------------------------------------------------------------

	// --------------------Start of ControlPanelMenu------------------------------------------------------------------------------
	class ControlPanelMenu {

		// public CMS_GENERIC_Lib aut;
		// public WebDriver driver;
		public String	menuXPath		= null;
		public String	commonPath		= "//div[@class='portlet-body']/div[@class='portlet-borderless-container']/div[@class='portlet-body']";
		public String	menuNameXpath	= "//div[@class='lfr-panel-title' and contains(.,'%SITE%')]";

		// public ControlPanelMenu(CMS_GENERIC_Lib aut) {
		// this.aut = aut;
		// driver = aut.getBrowser().getWebDriver();
		// }

		/**
		 * @author u124699
		 * @description Open a specific page by clicking the item name in the control panel menu
		 * @param menuName
		 *            -->my, content, portal, server
		 * @param itemName
		 *            -->Users and Organizations, Sites, Site Templates, etc.
		 * @return void
		 * @date 2011-06-29
		 */
		public void openItemPage(Menu menuNameEnum, String itemName) {
			try {
				String menuName = menuNameEnum.toString();
				menuXPath = commonPath
						+ "/div[@class='portal-add-content']/div[@id='controlPanelMenuAddContentPanelContainer']/div[@id='panel-manage-" + menuName
						+ "']/div";
				aut.waitFor(2);
				expandMenu(menuXPath);
				String itemXpath = menuXPath + "[@class='lfr-panel-content']/ul/li/a[contains(.,'" + itemName + "')]";
				if (aut.isElementExistHelper(itemXpath)) {
					log.comment("**********Open [" + menuName + "-->" + itemName + "] page in Control Panel.**********");
					driver.findElement(By.xpath(itemXpath)).click();
					aut.waitForPageToLoad();
					aut.waitFor(1);
				} else {
					throw new Exception("Method Name: openItemPage, but Menu Item: " + itemName + NEW_LINE + "XPATH: " + itemXpath + NEW_LINE
							+ "does not exist");

				}

				log.verifyStep(verifyPageTitle(itemName), "Open Page: " + itemName, "Page [" + itemName + "] displays!");

			} catch (Exception e) {
				log.exception(e);
				log.comment("Method Name", "No Locator", "Script did not complete successfully.", Log.FAIL, Log.MAJOR_ISSUE);
			}
		}

		/**
		 * @author u124699
		 * @description Select a specific site as current site in the control panel menu
		 * @param siteName
		 * @return void
		 * @date 2011-07-03
		 */
		public void selectSite(String siteName) {
			try {
				log.comment("**********Select a specific site as current site in the control panel menu.**********");
				String contentMenuXPath = commonPath
						+ "/div[@class='portal-add-content']/div[@id='controlPanelMenuAddContentPanelContainer']/div[@id='panel-manage-content']/div";
				expandMenu(contentMenuXPath);
				aut.waitFor(2);
				String siteListXpath = contentMenuXPath + "/div[@class='lfr-panel-title']/span/span/ul/li[@class='lfr-trigger']";
				driver.findElement(By.xpath(siteListXpath)).click();

				String siteXpath = "//div[@class='lfr-component lfr-menu-list']/ul/li/a[contains(.,'" + siteName + "')]";
				if (aut.isElementExistHelper(siteXpath)) {
					driver.findElement(By.xpath(siteXpath)).click();
					aut.waitFor(1);
					aut.waitForPageToLoad();
					aut.waitFor(1);

				} else {
					throw new Exception("Method Name: selectSite, but Site: " + siteName + NEW_LINE + "XPATH: " + siteXpath + NEW_LINE
							+ "does not exist");

				}

				menuNameXpath = menuNameXpath.replace("%SITE%", siteName);
				log.verifyStep(aut.isElementExistHelper(menuNameXpath), "Select Site as Current Site: " + siteName, "Site [" + siteName
						+ "] displays!");

			} catch (Exception e) {
				log.exception(e);
				log.comment("Method Name", "No Locator", "Script did not complete successfully.", Log.FAIL, Log.MAJOR_ISSUE);
			}

		}

		/**
		 * @author u124699
		 * @description Expand a specific sub menu in the control panel menu
		 * @param mXpath
		 *            -->Xpath of the sub menu
		 * @return void
		 * @date 2011-07-03
		 */
		public void expandMenu(String mXpath) {
			if (isMenuExpand(mXpath)) {
				log.comment("**********Expand a specific sub menu in the control panel menu.**********");
				driver.findElement(By.xpath(mXpath)).click();
			}
		}

		/**
		 * @author u124699
		 * @description Collapse a specific sub menu in the control panel menu
		 * @param mXpath
		 *            -->Xpath of the sub menu
		 * @return void
		 * @date 2011-07-03
		 */
		public void collapseMenu(String mXpath) {
			if (!isMenuExpand(mXpath)) {
				log.comment("**********Collapse a specific sub menu in the control panel menu.**********");
				driver.findElement(By.xpath(mXpath)).click();
			}
		}

		/**
		 * @author u124699
		 * @description Check a specific sub menu in the control panel menu is expanded
		 * @param mXpath
		 *            -->Xpath of the sub menu
		 * @return boolean
		 * @date 2011-07-03
		 */
		private boolean isMenuExpand(String menuTitleXpath) {
			// String iconXpath = menuXPath +
			// "/div/a[@class='lfr-panel-button' and @title='Collapse']";
			String iconXpath = menuTitleXpath + "/a[@class='lfr-panel-button' and @title='Expand']";
			return aut.isElementExistHelper(iconXpath);
		}

	}

	// --------------------End of ControlPanelMenu------------------------------------------------------------------------------

	// --------------------Start of RightSideMenu------------------------------------------------------------------------------
	class RightSideMenu {

		// public CMS_GENERIC_Lib aut;
		// public WebDriver driver;

		// public String menuName;
		// public String menuItem;

		public String	menuItemXPath;
		public String	commonPath	= "//div[@class='lfr-component form-navigator']";

		// public RightSideMenu(CMS_GENERIC_Lib aut) {
		// this.aut = aut;
		// driver = aut.getBrowser().getWebDriver();
		// }

		/**
		 * @author u124699
		 * @description Go to a specific attribute page of Site or Page by clicking the menu item in right side menu
		 * @param itemName
		 *            -->Staging, Analytics, etc.
		 * @return void
		 * @date 2011-07-02
		 */
		public void openAttributePage(String itemName) {
			menuItemXPath = commonPath + "/div[@class='menu-group']/ul/li/a[contains(text(),'" + itemName + "')]";
			if (isItemDisplay(menuItemXPath)) {
				log.comment("**********Go to a specific attribute page of Site or Page by clicking the menu item in right side menu.**********");
				driver.findElement(By.xpath(menuItemXPath)).click();
				aut.waitForPageToLoad();
			}
		}

		/**
		 * @author u124699
		 * @description Click button on the right side menu
		 * @param itemName
		 *            -->Save, Cancel, etc.
		 * @return void
		 * @date 2011-07-02
		 */
		public void clickButton(String buttonName) {
			menuItemXPath = commonPath + "/div[contains(@class,'aui-button-holder')]/span/span/input[@value='" + buttonName + "']";
			if (isItemDisplay(menuItemXPath)) {
				log.comment("**********Click button " + buttonName + " on the right side menu.**********");
				driver.findElement(By.xpath(menuItemXPath)).click();
				aut.waitForPageToLoad();
			}
		}

		/**
		 * @author u124699
		 * @description Check if a specific element displays on the right side menu
		 * @param elementXpath
		 *            --XPATH of the element
		 * @return void
		 * @date 2011-07-02
		 */
		private boolean isItemDisplay(String itemXPath) {
			boolean isDisplay = false;
			if (driver.findElement(By.xpath(itemXPath)).isDisplayed()) {
				isDisplay = true;
			}
			return isDisplay;
		}
	}

	// --------------------End of RightSideMenu------------------------------------------------------------------------------

	// --------------------Start of PermissionDialog------------------------------------------------------------------------------
	class PermissionDialog {

		private String	editPermissionsXpath	= "//div[@class='edit-permissions']";
		private String	utilityNameXpath		= editPermissionsXpath + "//h1[@class='header-title' and contains(.,'%NAME%')]";
		private String	tableTitleXpath			= editPermissionsXpath + "//span[@class='aui-tab-content']/span[contains(.,'Permissions')]";
		private String	checkboxXpath			= editPermissionsXpath
														+ "//table[@class='taglib-search-iterator']//tr[td[1]/a[starts-with(.,'%ROLE%')]]/td/input[@type='checkbox' and @name=concat(substring-before(@name,'%ACTION%'),'%ACTION%')]";
		private String	checkboxOwnerXpath		= editPermissionsXpath
														+ "//table[@class='taglib-search-iterator']//tr[td[1][contains(.,'%ROLE%')]]/td/input[@type='checkbox' and @name=concat(substring-before(@name,'%ACTION%'),'%ACTION%')]";
		private String	checkboxesInaRowXpath	= editPermissionsXpath
														+ "//table[@class='taglib-search-iterator']//tr[td[1]/a[starts-with(.,'%ROLE%')]]/td/input[@type='checkbox']";
		private String	checkboxOwnerRowXpath	= editPermissionsXpath
														+ "//table[@class='taglib-search-iterator']//tr[td[1][contains(.,'%ROLE%')]]/td/input[@type='checkbox']";

		/**
		 * @author u124699
		 * @description Check a checkbox
		 * @return void
		 * @throws Exception
		 * @date 2011-08-08
		 */
		private void checkOn(String checkbox) throws Exception {
			WebElement element = driver.findElement(By.xpath(checkbox));
			if (element.isEnabled() == true) {
				aut.getBrowser().getSelenium().check(checkbox);
				log.verifyStep(driver.findElement(By.xpath(checkbox)).isSelected(), "Check On a Checkbox", "The Checkbox has been checked!"
						+ NEW_LINE + "XPATH: " + checkbox);
			}
		}

		/**
		 * @author u124699
		 * @description Uncheck a checkbox
		 * @return void
		 * @throws Exception
		 * @date 2011-08-08
		 */
		private void checkOff(String checkbox) throws Exception {
			WebElement element = driver.findElement(By.xpath(checkbox));
			if (element.isEnabled() == true) {
				aut.getBrowser().getSelenium().uncheck(checkbox);
				log.verifyStep(!driver.findElement(By.xpath(checkbox)).isSelected(), "Check Off a Checkbox", "The Checkbox has been checked off!"
						+ NEW_LINE + "XPATH: " + checkbox);
			}

		}

		/**
		 * @author u124699
		 * @description Check if a specific Utility displayed on the page
		 * @return boolean
		 * @date 2011-08-08
		 */
		public boolean verifyUtilityName(String utilityName) {
			boolean exist = false;
			String uNameXpath = utilityNameXpath.replace("%NAME%", utilityName);
			try {
				log.comment("**********Check if a specific Utility displayed on the page.**********");
				exist = aut.isElementExistHelper(uNameXpath);
				log.verifyStep(exist, "Check if the Utility: [" + utilityName + "] displayed on the page", "The Utility displayed!" + NEW_LINE
						+ "XPATH: " + uNameXpath);
			} catch (Exception e) {
				log.exception(e);
				log.comment("Method Name", "No Locator", "Script did not complete successfully.", Log.FAIL, Log.MAJOR_ISSUE);
			}
			return exist;
		}

		/**
		 * @author u124699
		 * @description Check if the Permission Table displayed on the page
		 * @return boolean
		 * @date 2011-08-08
		 */
		public boolean verifyPermissionTable(String utilityName) {
			boolean exist = false;
			try {
				log.comment("**********Check if the Permission Table displayed on the page.**********");
				exist = aut.isElementExistHelper(tableTitleXpath);
				log.verifyStep(exist, "Check if the Permission Table displayed on the page.", "The Permission Table displayed!" + NEW_LINE
						+ "XPATH: " + tableTitleXpath);
			} catch (Exception e) {
				log.exception(e);
				log.comment("Method Name", "No Locator", "Script did not complete successfully.", Log.FAIL, Log.MAJOR_ISSUE);
			}
			return exist;
		}

		/**
		 * @author u124699
		 * @description Check if a specific Permission has been checked on
		 * @return boolean
		 * @date 2011-08-08
		 */
		public boolean verifyPermission(CMSWebApp.UserRole role, Page.UserActions permission) {
			boolean checked = false;
			String checkXpath = null;
			String urole = getRole(role);
			String upermission = getAction(permission);

			try {
				log.comment("**********Check if a specific Permission has been checked on**********");

				if (role.equals("Owner")) {
					checkXpath = (checkboxOwnerXpath.replace("%ROLE%", urole)).replace("%ACTION%", upermission);
				} else {
					checkXpath = (checkboxXpath.replace("%ROLE%", urole)).replace("%ACTION%", upermission);
				}

				WebElement element = driver.findElement(By.xpath(checkXpath));
				checked = element.isSelected();
				log.verifyStep(checked, "Check if a specific Permission has been checked on.", "The Permission: " + permission.toString()
						+ " for User: " + role.toString() + NEW_LINE + "XPATH: " + checkXpath + NEW_LINE + "has been check on.");

				return checked;

			} catch (Exception e) {
				log.exception(e);
				log.comment("Method Name", "No Locator", "Script did not complete successfully.", Log.FAIL, Log.MAJOR_ISSUE);
			}
			return checked;
		}

		/**
		 * @author u124699
		 * @description Check if a specific Permission has been checked on
		 * @return boolean
		 * @date 2011-08-08
		 */
		public boolean verifyAllPermissionsOff(CMSWebApp.UserRole role) {
			boolean checked = true;
			String checkInaRowXpath = null;
			try {
				log.comment("**********Check if all Permissions has been checked off**********");
				String urole = getRole(role);
				if (role.equals("Owner")) {
					checkInaRowXpath = checkboxOwnerRowXpath.replace("%ROLE%", urole);
				} else {
					checkInaRowXpath = checkboxesInaRowXpath.replace("%ROLE%", urole);
				}

				List<WebElement> elements = driver.findElements(By.xpath(checkInaRowXpath));
				for (WebElement e : elements) {
					checked = e.isSelected();
					log.verifyStep(!checked, "Check if a specific Permission has been checked off.", "The Permission: " + e.getAttribute("name")
							+ " for User: " + role.toString() + NEW_LINE + "XPATH: " + checkInaRowXpath + NEW_LINE + "has been check off.");

				}
				return checked;

			} catch (Exception e) {
				log.exception(e);
				log.comment("Method Name", "No Locator", "Script did not complete successfully.", Log.FAIL, Log.MAJOR_ISSUE);
			}
			return checked;
		}

		/**
		 * @author u124699
		 * @description Check a Permission
		 * @return void
		 * @date 2011-08-08
		 */
		public void checkOnPermission(CMSWebApp.UserRole role, Page.UserActions permission) {
			String checkXpath = null;
			String urole = getRole(role);
			String upermission = getAction(permission);

			try {
				if (role.equals("Owner")) {
					checkXpath = (checkboxOwnerXpath.replace("%ROLE%", urole)).replace("%ACTION%", upermission);
				} else {
					checkXpath = (checkboxXpath.replace("%ROLE%", urole)).replace("%ACTION%", upermission);
				}
				checkOn(checkXpath);

			} catch (Exception e) {
				log.exception(e);
				log.comment("Method Name", "No Locator", "Script did not complete successfully.", Log.FAIL, Log.MAJOR_ISSUE);
			}
		}

		/**
		 * @author u124699
		 * @description Uncheck a Permission
		 * @return void
		 * @date 2011-08-08
		 */
		public void checkOffPermission(CMSWebApp.UserRole role, Page.UserActions permission) {
			String checkXpath = null;
			String urole = getRole(role);
			String upermission = getAction(permission);
			try {
				if (role.equals("Owner")) {
					checkXpath = (checkboxOwnerXpath.replace("%ROLE%", urole)).replace("%ACTION%", upermission);
				} else {
					checkXpath = (checkboxXpath.replace("%ROLE%", urole)).replace("%ACTION%", upermission);
				}
				checkOff(checkXpath);
			} catch (Exception e) {
				log.exception(e);
				log.comment("Method Name", "No Locator", "Script did not complete successfully.", Log.FAIL, Log.MAJOR_ISSUE);
			}
		}

		/**
		 * @author u124699
		 * @description Uncheck all Permission of a specific role
		 * @return void
		 * @date 2011-08-08
		 */
		public void checkOffAllPermission(CMSWebApp.UserRole role) {
			String checkInaRowXpath = null;

			String urole = getRole(role);
			if (role.equals("Owner")) {
				checkInaRowXpath = checkboxOwnerRowXpath.replace("%ROLE%", urole);
			} else {
				checkInaRowXpath = checkboxesInaRowXpath.replace("%ROLE%", urole);
			}

			List<WebElement> elements = driver.findElements(By.xpath(checkInaRowXpath));
			for (WebElement e : elements) {
				if (e.isSelected() == true && e.isEnabled() == true) {
					e.click();
				}
			}

		}

		/**
		 * @author u124699
		 * @description Check all Permission of a specific role
		 * @return void
		 * @date 2011-08-08
		 */
		public void checkOnAllPermission(CMSWebApp.UserRole role) {
			String checkInaRowXpath = null;

			String urole = getRole(role);
			if (role.equals("Owner")) {
				checkInaRowXpath = checkboxOwnerRowXpath.replace("%ROLE%", urole);
			} else {
				checkInaRowXpath = checkboxesInaRowXpath.replace("%ROLE%", urole);
			}

			List<WebElement> elements = driver.findElements(By.xpath(checkInaRowXpath));
			for (WebElement e : elements) {
				if (e.isSelected() == false && e.isEnabled() == true) {
					e.click();
				}
			}

		}

		/**
		 * @author u124699
		 * @description convert CMSWebApp.UserRole to String
		 * @return void
		 * @date 2011-08-08
		 */
		private String getRole(CMSWebApp.UserRole userRole) {
			String role = null;
			switch (userRole) {
			case portal_admin:
				role = "Owner";
				break;
			case site_admin:
				role = "Owner";
				break;
			case site_owner:
				role = "Owner";
				break;
			case site_member:
				role = "Site Member";
				break;
			case normal_user:
				role = "User";
				break;
			case power_user:
				role = "Power User";
				break;
			case guest:
				role = "Guest";
				break;
			}
			return role;
		}

		/**
		 * @author u124699
		 * @description convert Page.UserActions to String
		 * @return void
		 * @date 2011-08-08
		 */
		private String getAction(Page.UserActions permission) {
			String per = null;
			switch (permission) {

			case add_discussion:
				per = "_ACTION_ADD_DISCUSSION";
				break;

			case configure_portlets:
				per = "_ACTION_CONFIGURE_PORTLETS";
				break;

			case add_page:
				per = "_ACTION_ADD_LAYOUT";
				break;

			case customize:
				per = "_ACTION_CUSTOMIZE";
				break;

			case delete:
				per = "_ACTION_DELETE";
				break;

			case delete_discussion:
				per = "_ACTION_DELETE_DISCUSSION";
				break;

			case permissions:
				per = "_ACTION_PERMISSIONS";
				break;

			case update:
				per = "_ACTION_UPDATE";
				break;

			case update_discussion:
				per = "_ACTION_UPDATE_DISCUSSION";
				break;

			case view:
				per = "_ACTION_VIEW";
				break;

			case access:
				per = "_ACTION_ACCESS";
				break;

			case add_document:
				per = "_ACTION_ADD_DOCUMENT";
				break;

			case add_shortcut:
				per = "_ACTION_ADD_SHORTCUT";
				break;

			case add_subfolder:
				per = "_ACTION_ADD_SUBFOLDER";
				break;

			case subscribe:
				per = "_ACTION_SUBSCRIBE";
				break;

			}
			return per;
		}

	}

	// --------------------End of PermissionDialog------------------------------------------------------------------------------

	// --------------------Start of Grid------------------------------------------------------------------------------

	// TODO Grid -- webtable need to implement

	// --------------------End of Grid------------------------------------------------------------------------------

	/**
	 * @author u124699
	 * @description Every selection in this enum denote a specific Sub Menu in Control Panel Menu <br>
	 *              my-->My Account Menu<br>
	 *              content-->Current Site Management Menu<br>
	 *              portal-->Portal Menu<br>
	 *              server-->Server Menu<br>
	 * @date 2011-07-26
	 */
	public enum Menu {

		// My Account Setting Menu
		my,

		// Current Site Configuration Menu
		content,

		// Portal Menu
		portal,

		// Server Menu
		server,

	}

	/**
	 * @author u124699
	 * @description Every selection in this enum denote a specific Permission to a Page or Doc <br>
	 * @date 2011-08-08
	 */
	public enum UserActions {
		// Page and Document

		// _ACTION_ADD_DISCUSSION
		add_discussion,
		// _ACTION_ADD_LAYOUT
		add_page,
		// _ACTION_CONFIGURE_PORTLETS
		configure_portlets,
		// _ACTION_CUSTOMIZE
		customize,
		// _ACTION_DELETE
		delete,
		// _ACTION_DELETE_DISCUSSION
		delete_discussion,
		// _ACTION_PERMISSIONS
		permissions,
		// _ACTION_UPDATE
		update,
		// _ACTION_UPDATE_DISCUSSION
		update_discussion,
		// _ACTION_VIEW"
		view,

		// Folder

		// _ACTION_ACCESS
		access,
		// _ACTION_ADD_DOCUMENT
		add_document,
		// _ACTION_ADD_SHORTCUT
		add_shortcut,
		// _ACTION_ADD_SUBFOLDER
		add_subfolder,
		// _ACTION_SUBSCRIBE
		subscribe,

	}

}
