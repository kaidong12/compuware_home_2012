package com.covisint.cms;

import java.util.Map;
import java.util.Properties;

import org.testng.ITestContext;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.AfterSuite;

//import junit.framework.Assert;
//import junit.framework.AssertionFailedError;
import main.java.com.compuware.gdo.framework.core.Browser;
import main.java.com.compuware.gdo.framework.core.Locators;
import main.java.com.compuware.gdo.framework.core.TestData;
//import main.java.com.compuware.gdo.framework.core.enums.BrowserType;
import main.java.com.compuware.gdo.framework.core.exceptions.FrameworkException;
import main.java.com.compuware.gdo.framework.utilities.logging.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
//import org.openqa.selenium.By;
//import org.openqa.selenium.Keys;
import org.openqa.selenium.Proxy;
//import org.openqa.selenium.WebDriver;
//import org.openqa.selenium.WebElement;
//import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
//import org.openqa.selenium.io.TemporaryFilesystem;
//import org.openqa.selenium.support.ui.ExpectedCondition;
//import org.openqa.selenium.support.ui.WebDriverWait;

public class CMSTestSuite {

	protected Browser				browser				= null;
	protected TestData				testData			= null;
	protected Locators				locators			= null;
	protected ITestContext			context				= null;
	protected Map<String, String>	parameters			= null;
	protected String				firefoxProfilePath	= null;
	protected Properties			commonProperties	= null;
	protected static String			caseId				= "";
	public static Log				log					= null;

	@BeforeSuite
	public void beforeSuite(ITestContext context) {
		try {
			// Retrieve the Common_TestSuite.xml
			commonProperties = getCommonXmlParameters(context.getCurrentXmlTest().getParameter("commonTestSuite_XMLPath"));

			// Delete Last Run Results
			deletePreviousFolder();

			// Instantiate Log Object for results logging.
			log = new Log(commonProperties.getProperty("testResultsFile"));

			caseId = "BeforeSuite(Core Portal CMS Test)";
			log.startTestExecution(caseId);

			// Retrieve the context current testsuite
			this.context = context;

			log.setColumnuWidth(Integer.parseInt(commonProperties.getProperty("widthCol1")),
					Integer.parseInt(commonProperties.getProperty("widthCol2")), Integer.parseInt(commonProperties.getProperty("widthCol3")),
					Integer.parseInt(commonProperties.getProperty("widthCol4")));

			// Get the FireFox Profile Template Path.
			firefoxProfilePath = commonProperties.getProperty("firefox.profileTemplate");
			createFirefoxProfile();

			// Connect to VPN
			log.comment("Connect to VPN");
			vpnConnect("vpnconnect");

		} catch (IOException e) {
			log.comment("Method Name", "No Locator", "Script did not complete successfully.", Log.FAIL, Log.MAJOR_ISSUE, caseId);
			e.printStackTrace();
		} finally {
			log.endTestExecution();
		}
	}

	/**
	 * @author Terry Sun
	 * @description to create a template for firefox.
	 * @return null
	 * @throws IOException
	 * @date 2012-02-22
	 */

	public void createFirefoxProfile() throws IOException {

		Proxy pp = new Proxy();
		pp.setHttpProxy("10.66.1.20:8081");
		pp.setFtpProxy("10.66.1.20:8081");
		pp.setSslProxy("10.66.1.20:8081");

		// File destdir = new File("c:\\cms_ff");
		File destdir = new File(firefoxProfilePath);

		destdir.mkdir();

		FirefoxProfile ff = new FirefoxProfile();
		ff.setProxyPreferences(pp);
		ff.setAcceptUntrustedCertificates(true);
		ff.setAssumeUntrustedCertificateIssuer(false);
		ff.setPreference("app.update.auto", false);
		ff.setPreference("app.update.enabled", false);
		File tempdir = ff.layoutOnDisk();
		File[] listfile = tempdir.listFiles();
		int i;
		for (i = 0; i < listfile.length; i++) {
			listfile[i].renameTo(new File(destdir, listfile[i].getName()));
		}

	}

	/**
	 * @author Lance Yan
	 * @description remove last test results.
	 * @return null
	 * @date 2012-07-22
	 */
	public void deletePreviousFolder() {
		try {
			if ("Yes".equals(commonProperties.getProperty("DeleteLastResultsBeforeExecution"))) {
				String projectBaseDir = System.getProperty("user.dir");
				// log results
				File testResultsDir = new File(projectBaseDir + "\\TestResults");
				if (testResultsDir.exists())
					deleteFolder(testResultsDir);
				// testng output
				File testoutputDir = new File(projectBaseDir + "\\test-output");
				if (testoutputDir.exists())
					deleteFolder(testoutputDir);

			}

		} catch (Exception e) {
			log.comment("Method Name", "No Locator", "Script did not complete successfully.", Log.FAIL, Log.MAJOR_ISSUE, caseId);
			System.out.println("Deleting old test results/test output fails.");
		}

	}

	public void deleteFolder(File dir) {
		File filelist[] = dir.listFiles();
		int listlen = filelist.length;
		for (int i = 0; i < listlen; i++) {
			if (filelist[i].isDirectory()) {
				deleteFolder(filelist[i]);
			} else {
				filelist[i].delete();
			}
		}
		// dir.delete();// delete current dir.
	}

	/**
	 * @author Lance Yan
	 * @description connect or disconnect VPN
	 * @param action
	 *            [vpnconnect,vpndisconnect]
	 * 
	 * @return void
	 * @date 2012-07-22
	 */
	public void vpnConnect(String action) throws IOException {
		// String[] vpn = { action, context.getCurrentXmlTest().getParameter("profileName"), context.getCurrentXmlTest().getParameter("userName"),
		// context.getCurrentXmlTest().getParameter("password") };
		String[] vpn = { action, commonProperties.getProperty("profileName"), commonProperties.getProperty("userName"),
				commonProperties.getProperty("password") };
		Runtime.getRuntime().exec(
				commonProperties.getProperty("VPNConnector") + " \"" + vpn[0] + "\" \"" + vpn[1] + "\" \"" + vpn[2] + "\" \"" + vpn[3] + "\"");
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @author Lance Yan
	 * @description connect or disconnect VPN
	 * @param commonTestSuiteXML
	 *            Path of the Common_TestSuite.xml
	 * 
	 * @return Properties
	 * @date 2012-07-31
	 */
	public Properties getCommonXmlParameters(String commonTestSuiteXML) throws IOException {
		FileInputStream fileInputStream = null;
		Properties commonTestSuiteProperties = null;
		try {
			if (commonTestSuiteXML == null) {
				throw new FrameworkException("CMSTestSuite", "PARAMETER:", "'commonTestSuite_XMLPath' IS EITHER UNDEFINED OR COMMENTED OUT",
						Log.ERROR, Log.SCRIPT_ISSUE);
			}

			if (commonTestSuiteXML.isEmpty()) {
				throw new FrameworkException("CMSTestSuite", "PARAMETER:", "'commonTestSuite_XMLPath' MAKES REFERENCE TO AN EMPTY STRING", Log.ERROR,
						Log.SCRIPT_ISSUE);
			}

			// Read contents of "common_testsuite.xml" onto a "FileInputStream" object.
			fileInputStream = new FileInputStream(commonTestSuiteXML);

			// Instantiate a "Properties" object and populate ir with the contents of "common_testsuite.xml"
			commonTestSuiteProperties = new Properties();
			commonTestSuiteProperties.loadFromXML(fileInputStream);
			return commonTestSuiteProperties;
		} catch (IOException e) {
			throw new FrameworkException("CMSTestSuite", "", e.getMessage(), Log.ERROR, Log.SCRIPT_ISSUE);
		} finally {
			// Close all file resources
			if (fileInputStream != null) {
				try {
					fileInputStream.close();
				} catch (IOException e) {
					throw new FrameworkException("CMSTestSuite", "", e.getMessage(), Log.ERROR, Log.SCRIPT_ISSUE);
				}
			}
		}

	}

	@AfterSuite
	public void afterSuite() {
		try {
			caseId = "AfterSuite(Core Portal CMS Test)";
			log.startTestExecution(caseId);
			log.comment("Done WITH TESTING(Core Portal CMS Test)!!");
			// log.comment("Disconnect to VPN");
			// vpnConnect("vpndisconnect");
		} catch (Exception e) {
			log.comment("Method Name", "No Locator", "Script did not complete successfully.", Log.FAIL, Log.MAJOR_ISSUE, caseId);
			log.exception(e);
		} finally {
			log.endTestExecution();
			if ("Enable".equals(parameters.get("ViewResultsAfterExecution"))) {
				try {
					Thread.sleep(3000);
					Runtime.getRuntime().exec("cmd /c start " + log.getLogFilePath());
				} catch (IOException e) {
					e.printStackTrace();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
