package WrappedElement;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import main.java.com.compuware.gdo.framework.utilities.JDatabase;
import main.java.com.compuware.gdo.framework.utilities.logging.Log;

public class DataDriver_unittests {
	private Log			log	= null;
	// private JDatabase db = null;
	private DataDriver	db	= null;

	public DataDriver_unittests(String testResults) {
		log = new Log(testResults);
	}

	public void getTestCaseDataTest(String dbUrl) {
		try {
			System.out.println("===================getTestCaseDataTest===================");
			log.startTestExecution("runQueryTest");
			db = new DataDriver(log, dbUrl);
			db.openConnection();
			System.out.println(db.getParameterValue(DataDriver.datatable.user, "UserName", "QAtestCMS_3"));
		} catch (Exception e) {
			log.exception(e);
		} finally {
			db.closeConnection();
			log.endTestExecution();
		}
	}

	public void getTestCaseDataTest2(String dbUrl) {
		try {
			System.out.println("===================getTestCaseDataTest2===================");
			log.startTestExecution("runQueryTest");
			db = new DataDriver(log, dbUrl);
			db.openConnection();
			System.out.println(db.getParameterValue(DataDriver.datatable.user, "FullName", "lance03"));
		} catch (Exception e) {
			log.exception(e);
		} finally {
			db.closeConnection();
			log.endTestExecution();
		}
	}

	public void updateTestDataTest(String dbUrl) {
		try {
			System.out.println("===================updateTestDataTest===================");
			log.startTestExecution("runQueryTest");
			db = new DataDriver(log, dbUrl);
			db.openConnection();
			// db.updateTestData(DataDriver.datatable.site, "SiteName", "Auto_CMS6_live_1");
			db.updateDeletedData(DataDriver.datatable.site, "TestCaseName", "QAtestCMS_6");
			System.out.println();
		} catch (Exception e) {
			log.exception(e);
		} finally {
			db.closeConnection();
			log.endTestExecution();
		}
	}

	public void updateTestDataTest2(String dbUrl) {
		try {
			System.out.println("===================updateTestDataTest===================");
			log.startTestExecution("runQueryTest");
			db = new DataDriver(log, dbUrl);
			db.openConnection();
			// db.updateTestData(DataDriver.datatable.site, "SiteName", "Auto_CMS6_live_1");
			db.updateAddedData(DataDriver.datatable.site, "TestCaseName", "QAtestCMS_6");
			System.out.println();
		} catch (Exception e) {
			log.exception(e);
		} finally {
			db.closeConnection();
			log.endTestExecution();
		}
	}

	public void getDataProviderTest1(String dbUrl) {
		try {
			log.startTestExecution("runQueryTest");
			db = new DataDriver(log, dbUrl);
			db.openConnection();
			Map<String, String> map = db.getDataProvider("QAtestCMS_3", DataDriver.datatable.user);
			System.out.println("===================getDataProviderTest1===================");
			log.comment("getDataProviderTest1: one table and select by test case name");
			Iterator<Entry<String, String>> it = map.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry<String, String> entry = (Map.Entry<String, String>) it.next();
				Object key = entry.getKey();
				Object value = entry.getValue();
				System.out.println("key=" + key + "  value=" + value);
			}
			System.out.println();
		} catch (Exception e) {
			log.exception(e);
		} finally {
			db.closeConnection();
			log.endTestExecution();
		}
	}

	public void getDataProviderTest2(String dbUrl) {
		try {
			log.startTestExecution("runQueryTest");
			db = new DataDriver(log, dbUrl);
			db.openConnection();
			Map<String, String> map = db.getDataProvider("QAtestCMS_111", DataDriver.datatable.site, DataDriver.datatable.page);
			System.out.println("===================getDataProviderTest2===================");
			log.comment("getDataProviderTest2: two tables and select by test case name");
			Iterator<Entry<String, String>> it = map.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry<String, String> entry = (Map.Entry<String, String>) it.next();
				Object key = entry.getKey();
				Object value = entry.getValue();
				System.out.println("key=" + key + "  value=" + value);
			}
			System.out.println();
		} catch (Exception e) {
			log.exception(e);
		} finally {
			db.closeConnection();
			log.endTestExecution();
		}
	}

	public void getDataProviderTest3(String dbUrl) {
		try {
			log.startTestExecution("runQueryTest");
			db = new DataDriver(log, dbUrl);
			db.openConnection();
			Map<String, String> map = db.getDataProvider("QAtestCMS_111", DataDriver.datatable.site, DataDriver.datatable.page,
					DataDriver.datatable.user);
			System.out.println("===================getDataProviderTest3===================");
			log.comment("getDataProviderTest3: three tables and select by test case name");
			Iterator<Entry<String, String>> it = map.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry<String, String> entry = (Map.Entry<String, String>) it.next();
				Object key = entry.getKey();
				Object value = entry.getValue();
				System.out.println("key=" + key + "  value=" + value);
			}
			System.out.println();
		} catch (Exception e) {
			log.exception(e);
		} finally {
			db.closeConnection();
			log.endTestExecution();
		}
	}

	public void getDataProviderTest4(String dbUrl) {
		try {
			log.startTestExecution("runQueryTest");
			db = new DataDriver(log, dbUrl);
			db.openConnection();
			Map<String, String> map = db.getDataProvider("lance03", DataDriver.datatable.user);
			System.out.println("===================getDataProviderTest4===================");
			log.comment("getDataProviderTest4: one table and select by field other than test case name");
			Iterator<Entry<String, String>> it = map.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry<String, String> entry = (Map.Entry<String, String>) it.next();
				Object key = entry.getKey();
				Object value = entry.getValue();
				System.out.println("key=" + key + "  value=" + value);
			}
			System.out.println();
		} catch (Exception e) {
			log.exception(e);
		} finally {
			db.closeConnection();
			log.endTestExecution();
		}
	}

	public void getDataProviderTest5(String dbUrl) {
		try {
			log.startTestExecution("runQueryTest");
			db = new DataDriver(log, dbUrl);
			db.openConnection();
			Map<String, String> map = db.getDataProvider("QAtestCMS_81", DataDriver.datatable.publish);
			System.out.println("===================getDataProviderTest5===================");
			log.comment("getDataProviderTest5: null check");
			Iterator<Entry<String, String>> it = map.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry<String, String> entry = (Map.Entry<String, String>) it.next();
				String key = entry.getKey();
				String value = entry.getValue();
				if (!(value == null)) {
					System.out.println("key=" + key + "  value=" + value);
				}

			}
			System.out.println();
		} catch (Exception e) {
			log.exception(e);
		} finally {
			db.closeConnection();
			log.endTestExecution();
		}
	}

	public static void main(String[] args) throws Exception {
		// Class.forName("org.sqlite.JDBC");
		System.setProperty("jdbc.drivers", "org.sqlite.JDBC");
		String constr = "jdbc:sqlite:TestData/CMS.sqlite";
		DataDriver_unittests dataDriver_unittests = new DataDriver_unittests("/TestResults/DataDriver_UnitTest.html");
		// dataDriver_unittests.getTestCaseDataTest(constr);
		// dataDriver_unittests.getDataProviderTest1(constr);
		// dataDriver_unittests.updateTestDataTest(constr);
		// dataDriver_unittests.updateTestDataTest2(constr);
		// dataDriver_unittests.getDataProviderTest2(constr);
		// dataDriver_unittests.getDataProviderTest3(constr);
		// dataDriver_unittests.getDataProviderTest4(constr);
		dataDriver_unittests.getDataProviderTest5(constr);
	}
	// old test

	// public static void main(String[] args) throws Exception {
	// Class.forName("org.sqlite.JDBC");
	// Connection connection = DriverManager.getConnection("jdbc:sqlite:TestData/CMS.sqlite");
	//
	// Statement statement = connection.createStatement();
	// ResultSet resultSet = statement.executeQuery("select * from user");
	// while (resultSet.next()) {
	// System.out.println("name: " + resultSet.getString("UserName"));
	// }
	// resultSet.close();
	// statement.close();
	// connection.close();
	// }

	// JDatabase test

	// public static void main(String... args) {
	// System.setProperty("jdbc.drivers", "org.sqlite.JDBC");
	//
	// JDatabase db = null;
	// Log log = null;
	//
	// try {
	// String constr = "jdbc:sqlite:TestData/CMS.sqlite";
	// log = new Log("/TestResults/DataDriver_UnitTest.html");
	// log.startTestExecution("MyTable");
	// db = new JDatabase(log, constr);
	// String sqlstr = "select * from MyTable";
	// db.openConnection();
	// db.executeQuery(sqlstr);
	// } catch (Exception e) {
	// // TODO Auto-generated catch block
	// log.exception(e);
	// } finally {
	// // if(db != null) {
	// // db.closeConnection();
	// // }
	// log.endTestExecution();
	// }
	// }
}
