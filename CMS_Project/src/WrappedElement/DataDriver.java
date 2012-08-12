package WrappedElement;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.Statement;
//import java.util.Locale;

import main.java.com.compuware.gdo.framework.core.exceptions.FrameworkException;
import main.java.com.compuware.gdo.framework.utilities.JDatabase;
import main.java.com.compuware.gdo.framework.utilities.logging.Log;

/**
 * @author Lance Yan<br>
 * <br>
 * 
 *         Class <code>DataDriver</code> is a extends of Francesco Ferrante's <code>JDatabase</code><br>
 *         which provides support for the following three basic actions:<br>
 * <br>
 * 
 *         1. Retrieve a specific value in a specific table in sqlite database.<br>
 *         2. Retrieve one row of data in one table or several tables with a specific TestCaseName.<br>
 *         3. Update site ID to database when a site is created.<br>
 *         4. Update database when a entity such as sitepage, site been deleted during test execution.<br>
 * <br>
 * 
 *         The sample code below illustrates the basic usage of class <code>DataDriver</code>.<br>
 * <br>
 * 
 *         <code>
 *   import java.sql.Connection;<br>
 *   import java.sql.Driver;<br>
 *   import java.sql.DriverManager;<br>
 *   import java.sql.ResultSet;<br>
 *   import java.sql.SQLException;<br>
 *   import java.sql.Statement;<br><br><br>
 *   
 *   try {<br>
 *     String dbConnectionString = "jdbc:oracle:thin:@localhost:9001:cccwtqa";<br>
 *     String dbUserName         = "crs";<br>
 *     String dbPassword         = "crs";<br><br>
 *     
 *     // Instantiate db object using a valid db connection string, db username and db password<br>
 *     JDatabase db = new JDatabase(dbConnectionString, dbUserName, dbPassword);<br><br>
 *     
 *      // Open connection to db.<br>
 *      db.openConnection();<br><br>
 *      
 *      // Eexecute sequel query...<br>
 *      Oject queryResult = db.executeQuery("SELECT * FROM TABLE_A");<br><br>
 *      
 *      if(queryResult != null) {<br><br>
 *        
 *        // Need to type cast class Object to class ResultSet<br>
 *        ResultSet resultSet = (ResultSet)queryResult;<br><br>
 *        
 *        // Code to manipulate ResultSet object goes here.<br> 
 *      }<br><br>
 *     
 *   } catch(SQLException sqle) {<br>
 *     // Code to handle sequel exceptions goes here.<br>
 *     sqle.printStackTrace();<br>
 *   } catch(Exception e) {<br>
 *     // Code to handle non-sequel execptions goes here.<br>   
 *     e.printStackTrace();<br>
 *   } finally {<br>
 *     try {<br>
 *      // Code to close db connection goes here.<br>
 *      db.closeConnection();<br>
 *     } catch(SQLException sqle) {<br>
 *       // More code to handle sequel exceptions goes here.<br>
 *       sqle.printStackTrace();<br>
 *     }<br>
 *   }<br><br>
 *   
 *  </code>
 */

public class DataDriver extends JDatabase {
	private Log				log			= null;
	private final String	NEW_LINE	= System.getProperty("line.separator");

	public DataDriver(Log log, String dbConnectionString) throws FrameworkException {
		super(log, dbConnectionString);
		this.log = log;
	}

	/**
	 * @param table
	 *            --table name in database
	 * @param fieldValue
	 *            (Key or Foreign Key in a table) <br>
	 *            --1,test case name in all the table<br>
	 *            --2,site name in site table<br>
	 *            --3,site page name in page table<br>
	 *            --4,doc name in basicdoc table<br>
	 *            --5,etc.<br>
	 * @param parameter
	 *            --column name <br>
	 * @return String <br>
	 * <br>
	 *         <b>USAGE:</b><br>
	 * <br>
	 *         <p>
	 *         Use this method to retrieve a specific value in table.<br>
	 *         It works like:<br>
	 *         <code>SELECT parameter FROM table WHERE TestCaseName='fieldValue';</code><br>
	 *         <code>SELECT UserName FROM user WHERE TestCaseName='QAtestCMS_3';</code><br>
	 *         <br>
	 */
	public String getParameterValue(datatable table, String parameter, String fieldValue) {
		return getDataProvider(fieldValue, table).get(parameter);
	}

	public ArrayList<String> getColummnData(String parameter, datatable table) {
		ArrayList<String> testdata = new ArrayList<String>();
		ResultSet rs = (ResultSet) executeQuery(getColumnSQL(parameter, table.toString()));
		try {
			while (rs.next()) {
				try {
					testdata.add(rs.getString(parameter));
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return testdata;
	}

	// public Map<String, String> getDataProvider(String table, String testCase) {
	// return dataProvider((ResultSet) executeQuery(getSQL(table, testCase)));
	// }

	/**
	 * @param fieldValue
	 *            (Key or Foreign Key in a table) <br>
	 *            --1,test case name in all the table<br>
	 *            --2,site name in site table<br>
	 *            --3,site page name in page table<br>
	 *            --4,doc name in basicdoc table<br>
	 *            --5,etc.<br>
	 * @param table
	 *            --table name [] <br>
	 * @return Map<String, String> <br>
	 * <br>
	 *         <b>NOTE:</b><br>
	 *         If there are columns with same name in table1 and table2 then<br>
	 *         <b>only columns in the later one (table2) will be return</b>;<br>
	 * <br>
	 *         <b>USAGE:</b><br>
	 * <br>
	 *         <p>
	 *         Use this method to retrieve one row of data in one table<br>
	 *         or several tables with the same TestCaseName.<br>
	 *         It works like:<br>
	 *         <code>SELECT parameter * table WHERE TestCaseName='testCase';</code><br>
	 *         <code>SELECT parameter * table1, table2 WHERE table1.TestCaseName=table2.TestCaseName;</code><br>
	 *         <code>SELECT * FROM user WHERE TestCaseName='QAtestCMS_3';</code><br>
	 *         <code>SELECT * FROM Site, SitePage WHERE Site.TestCaseName= SitePage.TestCaseName;</code><br>
	 *         <br>
	 * 
	 */
	public Map<String, String> getDataProvider(String fieldValue, datatable... table) throws FrameworkException {
		Map<String, String> testdata = null;
		try {
			if (!(fieldValue == null)) {
				if (table.length == 1) {
					testdata = dataProvider((ResultSet) executeQuery(getSQL(table[0].toString(), fieldValue)));
				} else {
					Map<String, String> tempdata = null;
					if (fieldValue.contains("#")) {
						String[] fieldValues = fieldValue.split("#");
						for (int i = 0; i < table.length; i++) {
							tempdata = dataProvider((ResultSet) executeQuery(getSQL(table[i].toString(), fieldValues[i])));
							if (testdata == null) {
								testdata = tempdata;
							} else {
								testdata.putAll(tempdata);
							}
						}

					} else {
						for (int i = 0; i < table.length; i++) {
							tempdata = dataProvider((ResultSet) executeQuery(getSQL(table[i].toString(), fieldValue)));
							if (testdata == null) {
								testdata = tempdata;
							} else {
								testdata.putAll(tempdata);
							}
						}

					}

				}
			} else {
				log.comment("Table Name or Field Value is NULL!");
			}

			if (testdata.isEmpty()) {
				throw new FrameworkException("DataDriver--->dataProvider", "No Locator", "Result Set is NULL!!!", Log.ERROR, Log.SCRIPT_ISSUE);
			}

		} catch (FrameworkException e) {
			throw new FrameworkException("DataDriver--->dataProvider", e.getClass().getName(), "Result Set is NULL!!!", Log.ERROR, Log.SCRIPT_ISSUE);
		}

		return testdata;
	}

	/**
	 * @param siteName
	 *            --site name
	 * @param siteID
	 *            --site ID <br>
	 * @return void <br>
	 *         s <br>
	 *         <b>USAGE:</b><br>
	 * <br>
	 *         <p>
	 *         Use this method to update site ID to database when a site is created.<br>
	 *         It works like:<br>
	 *         <code>UPDATE table SET SiteID= 'siteID' WHERE parameter= 'siteName';</code><br>
	 *         <code>UPDATE site SET SiteID= '51127' WHERE SiteName= 'Auto_CMS6_live_1';</code><br>
	 *         <br>
	 */
	public void updateSiteID(String siteName, String siteID) {
		executeQuery(getUpdateSiteIDSQL(siteName, siteID));
		executeQuery(getUpdateRemoteSiteIDSQL(siteName, siteID));
	}

	/**
	 * @param table
	 *            --table name in database(eg. DataDriver.datatable.basicdoc)
	 * @param parameter
	 *            --column name(eg. TestCaseName)<br>
	 * @param value
	 *            --data value(eg. QAtestCMS_61)<br>
	 * @return void <br>
	 * <br>
	 *         <b>USAGE:</b><br>
	 * <br>
	 *         <p>
	 *         Use this method to update database when a entity such as<br>
	 *         sitepage, site been deleted during test execution.<br>
	 *         It works like:<br>
	 *         <code>UPDATE table SET Deleted= 'Yes' WHERE parameter= 'value';</code><br>
	 *         <code>UPDATE site SET Deleted= 'Yes' WHERE SiteName= 'Auto_CMS6_live_1';</code><br>
	 *         <br>
	 */
	public void updateDeletedData(datatable table, String parameter, String value) {
		executeQuery(getDeleteSQL(table.toString(), parameter, value));

	}

	/**
	 * @param table
	 *            --table name in database(eg. DataDriver.datatable.basicdoc)
	 * @param parameter
	 *            --column name(eg. TestCaseName)<br>
	 * @param value
	 *            --data value(eg. QAtestCMS_61)<br>
	 * @return void <br>
	 * <br>
	 *         <b>USAGE:</b><br>
	 * <br>
	 *         <p>
	 *         Use this method to update database when a entity such as<br>
	 *         sitepage, site been deleted during test execution.<br>
	 *         It works like:<br>
	 *         <code>UPDATE table SET Deleted= 'Yes' WHERE parameter= 'value';</code><br>
	 *         <code>UPDATE site SET Deleted= 'Yes' WHERE SiteName= 'Auto_CMS6_live_1';</code><br>
	 *         <br>
	 */
	public void updateAddedData(datatable table, String parameter, String value) {
		executeQuery(getAddSQL(table.toString(), parameter, value));

	}

	@Override
	protected void createStatement() {
		try {
			sqlStatement = dbConnection.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY, ResultSet.CLOSE_CURSORS_AT_COMMIT);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private Map<String, String> dataProvider(ResultSet res) throws FrameworkException {
		// Map<String, String> oneRowData = new HashMap<String, String>();
		Map<String, String> oneRowData = Collections.synchronizedMap(new HashMap<String, String>());
		StringBuilder ResultSetStr = new StringBuilder();
		ResultSetStr.append("DataDriver--->dataProvider:" + NEW_LINE);
		try {
			while (res.next()) {
				for (int i = 1; i <= res.getMetaData().getColumnCount(); i++) {
					oneRowData.put(res.getMetaData().getColumnName(i), res.getString(i));
					ResultSetStr.append("\t" + res.getMetaData().getColumnName(i) + "--->" + res.getString(i) + NEW_LINE);
				}
			}
			System.out.println(ResultSetStr.toString());
			System.out.println();
			if (oneRowData.isEmpty()) {
				log.comment(
						"DataDriver--->dataProvider",
						"Populate a Map with test data retrieve form Database.\nBut the ResultSet is NULL, please check parameters in getDataProvider!",
						ResultSetStr.toString(), Log.WARN, Log.SCRIPT_ISSUE);
			} else {
				log.comment("DataDriver--->dataProvider", "Populate a Map with test data retrieve form Database", ResultSetStr.toString(), Log.DEBUG,
						Log.SCRIPT_ISSUE);
			}

		} catch (SQLException e) {
			throw new FrameworkException("dataProvider", e.getClass().getName(), e.getMessage(), Log.ERROR, Log.SCRIPT_ISSUE);
		}

		return oneRowData;
	}

	// SELECT * FROM user WHERE TestCaseName='QAtestCMS_3';
	// SELECT * FROM Site, SitePage WHERE Site.TestCaseName= SitePage.TestCaseName;
	private String getSQL(String table, String fieldValue) {
		StringBuilder sqlStr = new StringBuilder();
		if (fieldValue.toLowerCase().contains("test")) {
			sqlStr.append("SELECT * FROM ");
			sqlStr.append(table);
			sqlStr.append(" WHERE TestCaseName = '");
			sqlStr.append(fieldValue);
			sqlStr.append("'");
		} else {
			sqlStr.append("SELECT * FROM ");
			sqlStr.append(table);
			sqlStr.append(" WHERE ");
			sqlStr.append(table + "Name");
			sqlStr.append(" = '");
			sqlStr.append(fieldValue);
			sqlStr.append("'");
		}
		return sqlStr.toString();
	}

	// UPDATE site SET Deleted= 'Yes' WHERE SiteName= 'Auto_CMS6_live_1';
	private String getDeleteSQL(String table, String field, String value) {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append("UPDATE ");
		sqlStr.append(table);
		sqlStr.append(" SET Deleted = 'Yes' WHERE ");
		sqlStr.append(field);
		sqlStr.append(" = '");
		sqlStr.append(value);
		sqlStr.append("'");
		return sqlStr.toString();
	}

	// UPDATE site SET Deleted= 'Yes' WHERE SiteName= 'Auto_CMS6_live_1';
	private String getAddSQL(String table, String field, String value) {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append("UPDATE ");
		sqlStr.append(table);
		sqlStr.append(" SET Deleted = 'No' WHERE ");
		sqlStr.append(field);
		sqlStr.append(" = '");
		sqlStr.append(value);
		sqlStr.append("'");
		return sqlStr.toString();
	}

	// UPDATE site SET SiteID= '51127' WHERE SiteName= 'Auto_CMS6_live_1';
	private String getUpdateSiteIDSQL(String siteName, String siteId) {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append("UPDATE site SET SiteID = '");
		sqlStr.append(siteId);
		sqlStr.append("' WHERE SiteName = '");
		sqlStr.append(siteName);
		sqlStr.append("'");
		return sqlStr.toString();
	}

	// UPDATE site SET SiteID= '51127' WHERE SiteName= 'Auto_CMS6_live_1';
	private String getUpdateRemoteSiteIDSQL(String remoteSiteName, String siteId) {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append("UPDATE site SET RemoteSiteID = '");
		sqlStr.append(siteId);
		sqlStr.append("' WHERE RemoteSiteName = '");
		sqlStr.append(remoteSiteName);
		sqlStr.append("'");
		return sqlStr.toString();
	}

	// select TestCaseName from webcontent;
	private String getColumnSQL(String field, String table) {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append("SELECT ");
		sqlStr.append(field);
		sqlStr.append(" FROM ");
		sqlStr.append(table);
		return sqlStr.toString();
	}

	/**
	 * @author u124699
	 * @description Every selection in this enum denote a specific tabe sqlite database CMS.sqlite <br>
	 *              user, site, page, basicdoc, folder, category, vocabulary, etc.<br>
	 * @date 2011-07-30
	 */
	public enum datatable {

		user, site, page, basicdoc, folder, category, vocabulary, webcontent, tag, publish,

	}

}
