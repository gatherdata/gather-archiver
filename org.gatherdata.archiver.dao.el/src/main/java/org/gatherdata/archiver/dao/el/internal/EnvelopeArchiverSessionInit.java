package org.gatherdata.archiver.dao.el.internal;

import java.util.Map;
import java.util.Vector;

import org.eclipse.persistence.exceptions.DatabaseException;
import org.eclipse.persistence.sessions.DatabaseSession;
import org.eclipse.persistence.sessions.Session;
import org.eclipse.persistence.sessions.factories.SessionManager;
import org.eclipse.persistence.sessions.factories.XMLSessionConfigLoader;
import org.eclipse.persistence.tools.schemaframework.SchemaManager;
import org.eclipse.persistence.tools.schemaframework.TableCreator;
import org.eclipse.persistence.tools.schemaframework.TableDefinition;

public class EnvelopeArchiverSessionInit {

	private Session elSession;
	private TableCreator tableCreator = new EnvelopeArchiveTableCreator();
	
	public EnvelopeArchiverSessionInit() {
		SessionManager manager = SessionManager.getManager();
		
		elSession = manager.getSession(new XMLSessionConfigLoader(),
				"hsqlDbSession", // session name
				EnvelopeArchiverSessionInit.class.getClassLoader(), // class loader
				true, // log in session
				false); // do not refresh session
	}
	
	public Session getSession() {
		return elSession;
	}

	public TableCreator getDefaultTableCreator() {
		return tableCreator;
	}
	
	protected void initializeDatabase() {
		DatabaseSession dbSession = createDefaultDatabaseSession();

		tableCreator = new EnvelopeArchiveTableCreator();
		tableCreator.setTableDefinitions(findUndefinedTables(dbSession));
		try {
			dbSession.login();
			tableCreator.createTables(dbSession);
		} catch (Exception sqle) {
			sqle.printStackTrace();
		} finally {	
			dbSession.logout();
		}
	}

	protected DatabaseSession createDefaultDatabaseSession() {
		return elSession.getProject().createDatabaseSession();
	}

	@SuppressWarnings("unchecked")
	protected Vector<TableDefinition> findUndefinedTables(DatabaseSession dbSession) {
		dbSession.login();
		Vector<TableDefinition> undefinedTables = new Vector<TableDefinition>();
		for (TableDefinition availableDefinition : (Vector<TableDefinition>)tableCreator.getTableDefinitions()) {
			availableDefinition.getName();
			try {
				dbSession.executeSQL("SELECT * FROM " + availableDefinition.getName() + ";");
			} catch (DatabaseException dbe) {
				// presume that table simply doesn't exist
				undefinedTables.add(availableDefinition);
			}
		}
		dbSession.logout();
		return undefinedTables;
	}

	public void reinitializeDatabase() {
		DatabaseSession dbSession = createDefaultDatabaseSession();

		tableCreator = new EnvelopeArchiveTableCreator();
		Vector<TableDefinition> defaultTables = (Vector<TableDefinition>)tableCreator.getTableDefinitions();
		Vector<TableDefinition> undefinedTables = findUndefinedTables(dbSession);
		defaultTables.removeAll(undefinedTables);
		tableCreator.setTableDefinitions(defaultTables);
		
		dbSession.login();
		try {
			tableCreator.dropTables(dbSession);
		} catch (Exception sqle) {
			sqle.printStackTrace();
		}

		tableCreator = new EnvelopeArchiveTableCreator();
		try {
			tableCreator.createTables(dbSession);
		} catch (Exception sqle) {
			sqle.printStackTrace();
		}
		dbSession.logout();
		
	}

}
