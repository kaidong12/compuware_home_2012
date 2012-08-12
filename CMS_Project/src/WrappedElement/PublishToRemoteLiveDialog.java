package WrappedElement;

import java.util.List;
import main.java.com.compuware.gdo.framework.utilities.logging.Log;

import main.java.com.compuware.gdo.framework.core.Locators;
import main.java.com.compuware.gdo.framework.core.Locators.LocatorType;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.covisint.cms.CMSWebApp;

public class PublishToRemoteLiveDialog implements remotePublishDialog {
	protected CMSWebApp	aut			= null;
	protected WebDriver	driver		= null;
	protected Log		log			= null;
	Locators			locators	= null;

	// protected final static String NEW_LINE = System.getProperty("line.separator");
	//
	// protected final String dialog_Base_Xpath = "//div[@role='dialog']";
	// protected final String colse_Button_Xpath = "//span[@class='aui-toolbar-content']/button[@title='Close dialog']";
	// // match the dialog title
	// protected final String dialog_Title_Xpath = "//span[@class='aui-panel-hd-text'  and contains(.,'%TEXT%')]";
	//
	// protected final String options_Base_Xpath = "//div[contains(@id,'_publishOptions')]";
	// protected final String publish_Button_Xpath = "//div[@class='aui-button-holder ']//input[@value='Publish']";
	// protected final String change_Selection_Xpath = "//div[@class='aui-button-holder ']//input[@value='Change Selection']";
	//
	// protected final String pages_Base_Xpath = "//div[@id='layoutsAdminExportPagesPagesPanel']";
	// protected final String applicatons_Base_Xpath = "//div[@id='layoutsAdminExportPagesPortletsPanel']";
	// protected final String other_Base_Xpath = "//div[@id='layoutsAdminExportPagesOptionsPanel']";
	// protected final String connection_Base_Xpath = "//div[@id='layoutsAdminExportPagesConnectionPanel']";
	//
	// protected final String collapse_Button_Xpath = "//a[@title='Collapse']";
	// protected final String expand_Button_Xpath = "//a[@title='Expand']";
	//
	// // Pages section
	// protected final String pages_radio_Xpath = "//span[following-sibling::label[normalize-space(text())='%TEXT%']]/input[@type='radio']";
	//
	// // Applications section
	// // match all the radio checkbox on the dialog
	// protected final String portlets_radio_Xpath = "//span[following-sibling::label[normalize-space(text())='%TEXT%']]/input[@type='radio']";
	// // match only the first class checkbox under Applications section
	// protected final String portlets_checkbox_Xpath =
	// "//span[following-sibling::label[normalize-space(text())='%TEXT%']]/input[contains(@id,'PORTLET_DATA') and @type='checkbox']";
	//
	// // Other section
	// // match all the checkbox in other and pages section
	// protected final String other_checkbox_Xpath = "//span[following-sibling::label[normalize-space(text())='%TEXT%']]/input[@type='checkbox']";
	//
	// // Remote connection section
	// protected final String connection_address_Xpath = "//input[contains(@id,'_remoteAddress') and @type='text']";
	// protected final String connection_port_Xpath = "//input[contains(@id,'_remotePort') and @type='text']";
	// protected final String connection_siteID_Xpath = "//input[contains(@id,'_remoteGroupId') and @type='text']";

	public PublishToRemoteLiveDialog(CMSWebApp aut, Log log) {
		this.aut = aut;
		this.log = log;
		driver = aut.getBrowser().getWebDriver();
	}

	public void openPanel(PublishToRemoteLiveDialog.Panel panel) {
		switch (panel) {
		case pages:
			doOpenPanel(remotePublishDialog.options.pages.base);
			break;
		case applicatons:
			doOpenPanel(remotePublishDialog.options.applicatons.base);
			break;
		case other:
			doOpenPanel(remotePublishDialog.options.other.base);
			break;
		case connection:
			doOpenPanel(remotePublishDialog.options.connection.base);
			break;
		}
	}

	public void openAllPanels() {
		log.comment("**********Open all Panels on Remote Publish Dialog**********");
		openPanel(Panel.pages);
		openPanel(Panel.applicatons);
		openPanel(Panel.other);
		openPanel(Panel.connection);
	}

	private void doOpenPanel(String panelXpath) {
		if (!isPanelOpened(panelXpath)) {
			driver.findElement(By.xpath(panelXpath + remotePublishDialog.options.expand)).click();
		}
	}

	private boolean isPanelOpened(String panelXpath) {
		String iconXpath = panelXpath + remotePublishDialog.options.collapse;
		return aut.isElementExistHelper(iconXpath);
	}

	/**
	 * @author u124699
	 * @description Select the pages need to be published<br>
	 * @date 2011-08-07
	 */
	public void selectPages(String pages) {

		try {
			log.comment("**********Select pages to be published**********");
			if (!(pages == null)) {
				log.comment("Select pages in [Change Selection-->Page List Pane] and check result in [Pages-->Page Table]");
				driver.findElement(By.xpath(remotePublishDialog.options.pages.button.changeSelection)).click();
				aut.waitFor(2);
				String publicPageCheckboxXpath = remotePublishDialog.options.pages.selectPagePane.checkbox.replace("%TEXT%", "Public Pages");
				driver.findElement(By.xpath(publicPageCheckboxXpath)).click();
				driver.findElement(By.xpath(publicPageCheckboxXpath)).click();
				aut.waitFor(1);
				String[] page = pages.split("#");
				for (int i = 0; i < page.length; i++) {
					if (page[i].endsWith("*")) {
						driver.findElement(
								By.xpath(remotePublishDialog.options.pages.selectPagePane.checkbox.replace("%TEXT%", page[i].replace("*", ""))))
								.click();
					} else if (page[i].endsWith("$")) {
						log.comment("PublishToRemoteLiveDialog-->selectPages:\t" + page[i].replace("$", "") + " is page added in Live Site!");
						String pageNameXpath = remotePublishDialog.options.pages.selectPagePane.checkbox.replace("%TEXT%", page[i].replace("$", ""));
						log.verifyStep(!aut.isElementExistHelper(pageNameXpath), "Verify Page selected." + NEW_LINE
								+ "remotePublishDialog.options.pages.table.pageName", "Page is not displayed. Element XPATH:" + NEW_LINE
								+ pageNameXpath);

					} else {
						driver.findElement(By.xpath(remotePublishDialog.options.pages.selectPagePane.checkbox.replace("%TEXT%", page[i]))).click();
					}
				}
				aut.waitFor(1);
				driver.findElement(By.xpath(remotePublishDialog.options.pages.selectPagePane.button.select)).click();
				aut.waitFor(2);
				aut.waitForPageToLoad();
				String pageXpath = null;
				for (int i = 0; i < page.length; i++) {

					if (page[i].endsWith("*")) {
						log.comment("**********Change link --> Delete live page.**********");
						pageXpath = remotePublishDialog.options.pages.table.pageName.replace("%TEXT%", page[i].replace("*", ""));
						driver.findElement(By.xpath(remotePublishDialog.options.pages.table.change.replace("%TEXT%", page[i].replace("*", ""))))
								.click();
						driver.findElement(By.xpath(remotePublishDialog.options.radio.replace("%TEXT%", "Delete live page."))).click();
						log.comment("**********Check Pages selected displayed on the page list**********");
						log.verifyStep(aut.isElementExistHelper(pageXpath), "Verify Page selected." + NEW_LINE
								+ "remotePublishDialog.options.pages.table.pageName", "Page has been selected successfully. Element XPATH:"
								+ NEW_LINE + pageXpath);

					} else if (page[i].endsWith("$")) {
						log.comment("**********Check pages added in Live site did not displayed in Staging Site**********");
						log.comment("PublishToRemoteLiveDialog-->selectPages:\t" + page[i].replace("$", "") + " is page added in Live Site!");
						pageXpath = remotePublishDialog.options.pages.table.pageName.replace("%TEXT%", page[i].replace("$", ""));
						log.verifyStep(!aut.isElementExistHelper(pageXpath), "Verify Page selected." + NEW_LINE
								+ "remotePublishDialog.options.pages.table.pageName", "Page is not displayed. Element XPATH:" + NEW_LINE + pageXpath);

					} else {
						log.comment("**********Check Pages selected displayed on the page list**********");
						pageXpath = remotePublishDialog.options.pages.table.pageName.replace("%TEXT%", page[i]);
						log.verifyStep(aut.isElementExistHelper(pageXpath), "Verify Page selected." + NEW_LINE
								+ "remotePublishDialog.options.pages.table.pageName", "Page has been selected successfully. Element XPATH:"
								+ NEW_LINE + pageXpath);

					}

				}

			}
			aut.captureScreenShot();

		} catch (Exception e) {
			log.exception(e);
			log.comment("Method Name", "No Locator", "Script did not complete successfully.", Log.FAIL, Log.MAJOR_ISSUE);
		}
	}

	/**
	 * @author u124699
	 * @description Check "Delete Missing Pages" checkbox<br>
	 * @date 2011-08-07
	 */
	public void checkPageOptions(String options) {

		try {
			if (!(options == null)) {
				log.comment("Check \"Delete Missing Pages\" checkbox");
				driver.findElement(By.xpath(remotePublishDialog.options.pages.checkbox.replace("%TEXT%", "Delete Missing Pages"))).click();
			}
			aut.captureScreenShot();
		} catch (Exception e) {
			log.exception(e);
			log.comment("Method Name", "No Locator", "Script did not complete successfully.", Log.FAIL, Log.MAJOR_ISSUE);
		}

	}

	/**
	 * @author u124699
	 * @description Check "Range" Option radio under Applications section<br>
	 * @date 2011-08-09
	 */
	public void checkRangeOption(String option) {

		try {
			if (!(option == null)) {
				log.comment("Check Option Radiobox under [Applications-->Range] Section");
				String rangeRadioXpath = remotePublishDialog.options.radio.replace("%TEXT%", option);
				if (aut.isElementExistHelper(rangeRadioXpath)) {
					aut.getBrowser().getSelenium().check(rangeRadioXpath);
				} else {
					throw new Exception("Method Name: checkRangeOption, but Radiobox: " + option + NEW_LINE + "XPATH: " + rangeRadioXpath + NEW_LINE
							+ "does not exist");
				}
				aut.waitFor(1);
				aut.captureScreenShot();

			}
		} catch (Exception e) {
			log.exception(e);
			log.comment("Method Name", "No Locator", "Script did not complete successfully.", Log.FAIL, Log.MAJOR_ISSUE);
		}

	}

	/**
	 * @author u124699
	 * @description Check "Delete Missing Pages" checkbox<br>
	 * @date 2011-08-09
	 */
	public void checkOtherOptions(String options) {

		try {
			if (!(options == null)) {
				log.comment("Check Option checkboxes under [Other] Section");
				String[] otherOptions = options.split("#");
				for (String opt : otherOptions) {
					// driver.findElement(By.xpath(remotePublishDialog.options.other.checkbox.replace("%TEXT%", opt))).click();
					if (opt.endsWith("*")) {
						aut.getBrowser().getSelenium().uncheck(remotePublishDialog.options.other.checkbox.replace("%TEXT%", opt.replace("*", "")));
					} else {
						aut.getBrowser().getSelenium().check(remotePublishDialog.options.other.checkbox.replace("%TEXT%", opt));
					}

				}
				aut.waitFor(1);
				aut.captureScreenShot();
			}
		} catch (Exception e) {
			log.exception(e);
			log.comment("Method Name", "No Locator", "Script did not complete successfully.", Log.FAIL, Log.MAJOR_ISSUE);
		}

	}

	/**
	 * @author u124699
	 * @description Check "Web Content" Options checkbox under Applications section<br>
	 * @date 2011-08-09
	 */
	public void checkWebContentOptions(String options) {

		try {
			if (!(options == null)) {
				log.comment("Check Option checkboxes under [Applications-->Web Content] Section");
				if ("Yes".equals(options)) {
					aut.getBrowser().getSelenium().check(remotePublishDialog.options.applicatons.checkbox.replace("%TEXT%", "Web Content"));
				} else {
					String[] otherOptions = options.split("#");
					for (String opt : otherOptions) {
						// driver.findElement(By.xpath(remotePublishDialog.options.other.checkbox.replace("%TEXT%", opt))).click();
						if (opt.endsWith("*")) {
							aut.getBrowser().getSelenium()
									.uncheck(remotePublishDialog.options.other.checkbox.replace("%TEXT%", opt.replace("*", "")));
						} else {
							aut.getBrowser().getSelenium().check(remotePublishDialog.options.other.checkbox.replace("%TEXT%", opt));
						}

					}

				}
				aut.waitFor(1);
				aut.captureScreenShot();

			}
		} catch (Exception e) {
			log.exception(e);
			log.comment("Method Name", "No Locator", "Script did not complete successfully.", Log.FAIL, Log.MAJOR_ISSUE);
		}

	}

	/**
	 * @author u124699
	 * @description Publishing Site by clicking Publish button<br>
	 * @date 2011-08-02
	 */
	public void publish() {
		log.comment("**********Publishing by clicking \"Publish\" button**********");
		aut.waitFor(2);
		driver.findElement(By.xpath(remotePublishDialog.options.buttons.publish)).click();
		aut.acceptAlert();
		aut.waitFor(5);
		aut.waitForPageToLoad();
	}

	/**
	 * @author u124699
	 * @description Close dialog<br>
	 * @date 2011-08-02
	 */
	public void close() {
		driver.findElement(By.xpath(remotePublishDialog.close)).click();
	}

	/**
	 * @author u124699
	 * @description Every selection in this enum denote a specific Section in Remote Publish Dialog<br>
	 * @date 2011-08-02
	 */
	public enum Panel {

		pages, applicatons, other, connection,

	}

}
