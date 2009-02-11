package org.gatherdata.archiver.dao.el.internal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.net.URL;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

import org.eclipse.persistence.tools.schemaframework.TableCreator;
import org.eclipse.persistence.tools.schemaframework.TableDefinition;
import org.junit.Ignore;
import org.junit.Test;

/**
 * Unit tests for the expected behavior of FlatFormsSessionInit.
 *
 */
public class EnvelopeArchiverSessionInitTest extends AbstractDbTestCase {

	public EnvelopeArchiverSessionInitTest() {
		super(null, true);
		
		serverProps = "database.1=mem:gather;dbname.1=gather";
	}
	
	@Test
	@Ignore
	public void shouldFindSessionsDotXmlOnClasspath() {
	    URL sessionsURL = this.getClass().getResource("/META-INF/sessions.xml");
	    assertNotNull(sessionsURL);
        File sessionsFile = new File(sessionsURL.getFile());
        assertTrue(sessionsFile.exists());
	}

	/**
	 * A test of the test-case, to ensure that it actually
	 * starts up the needed database.
	 * 
	 * @throws SQLException
	 */
	@Test
	@Ignore
	public void shouldInstantiateHsqldbServer() throws SQLException {
		Connection gatherdbConn =  DriverManager.getConnection("jdbc:hsqldb:hsql://localhost/gather", user, password);
		assertNotNull(gatherdbConn);
		
		// to be sure: create, then drop a table
		
		Statement sqlStmt = gatherdbConn.createStatement();
		final String TEMP_TABLE_NAME = "NOSUCHTABLE";
		sqlStmt.execute("DROP TABLE " + TEMP_TABLE_NAME + " IF EXISTS;");
		sqlStmt.execute("CREATE TABLE " + TEMP_TABLE_NAME + " (USER_ID INTEGER NOT NULL PRIMARY KEY,LOGIN_ID VARCHAR(128) NOT NULL);");
		sqlStmt.execute("DROP TABLE " + TEMP_TABLE_NAME + ";");
		
		// for good meausure, try something that should fail to make sure
		// we weren't getting silent failures above
		Exception expectedException = null;
		try {
			sqlStmt.execute("THIS IS NOT SQL");
		} catch (SQLException e) {
			expectedException = e;
		}
		assertNotNull(expectedException);
	}
	
	/**
	 * FlatFormSessionInit should initialize a database session
	 * without causing an exception. For this to succeed, the 
	 * testcase must successfully instantiate an in-memory
	 * hsqldb server.
	 * 
	 */
	@Test
	@Ignore
	public void shouldInitSessionWithoutException() {
		EnvelopeArchiverSessionInit sessionInit = new EnvelopeArchiverSessionInit();
	}
	
	@Test
	@Ignore
	public void shouldInitiallyFindAllTablesUndefined() {
	    EnvelopeArchiverSessionInit sessionInit = new EnvelopeArchiverSessionInit();
		
		TableCreator defaultTableCreator = sessionInit.getDefaultTableCreator();
		int initialTableDefinitions = defaultTableCreator.getTableDefinitions().size();
		Vector<TableDefinition> undefinedTables = sessionInit.findUndefinedTables(sessionInit.createDefaultDatabaseSession());
		assertNotNull(undefinedTables);
		assertEquals(initialTableDefinitions, undefinedTables.size());
	}
	
	@Test
	@Ignore
	public void shouldFindNoTablesUndefinedAfterTablesCreated() {
	    EnvelopeArchiverSessionInit sessionInit = new EnvelopeArchiverSessionInit();
		
		TableCreator defaultTableCreator = sessionInit.getDefaultTableCreator();
		int initialTableDefinitions = defaultTableCreator.getTableDefinitions().size();
		
		sessionInit.initializeDatabase();
		
		Vector<TableDefinition> undefinedTables = sessionInit.findUndefinedTables(sessionInit.createDefaultDatabaseSession());
		assertNotNull(undefinedTables);
		assertEquals(0, undefinedTables.size());
	}
	
	@Test
	@Ignore
	public void shouldReinitializeTablesOnDemand() {
	    EnvelopeArchiverSessionInit sessionInit = new EnvelopeArchiverSessionInit();
		
		sessionInit.reinitializeDatabase();
		
		Vector<TableDefinition> postReinitUndefinedTables = sessionInit.findUndefinedTables(sessionInit.createDefaultDatabaseSession());
		assertEquals(0, postReinitUndefinedTables.size());
	}
}
