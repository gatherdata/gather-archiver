package org.gatherdata.archiver.dao.el.internal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.Serializable;
import java.net.URI;
import java.net.URISyntaxException;

import org.eclipse.persistence.sessions.DatabaseSession;
import org.eclipse.persistence.sessions.Session;
import org.eclipse.persistence.tools.schemaframework.TableCreator;
import org.gatherdata.archiver.core.spi.ArchiverDao;
import org.gatherdata.commons.io.MimeTypes;
import org.gatherdata.commons.net.CbidFactory;
import org.gatherdata.commons.net.GatherUrnFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

/**
 * Unit tests for the ElArchiverDaoImpl, which relies on the EnvelopeArchiverSessionInit 
 * to be working properly (always check that the EnvelopeArchiverSessionInitTest first).
 * 
 */
public class ElArchiverDaoImplTest extends AbstractDbTestCase {

	private EnvelopeArchiverSessionInit sessionInit;

    private int mockEnvelopeCount = 0;

	private static URI MOCK_NAMESPACE;
	private static Integer renderedValueCounter = new Integer(1);
	
	static {
		try {
			MOCK_NAMESPACE = new URI("http://mock.testing.namespace");
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}
	
	@Before
	public void reinitDatabaseWithCleanTables() {
		sessionInit = new EnvelopeArchiverSessionInit();
		sessionInit.reinitializeDatabase();
	}
	
	public ElArchiverDaoImplTest() {
		super(null, true);
		serverProps = "database.1=mem:gather;dbname.1=gather";
	}

	@Test
	@Ignore
	public void shouldSaveEnvelope() throws URISyntaxException {
//	    ElArchiverDaoImpl dao = new ElArchiverDaoImpl();
//		dao.setSession(sessionInit.getSession());
//		
//		int initialCount = countRecordsOf(EnvelopeDTO.class);
//		
//		Envelope envToSave = creatMockEnvelope();
//		
//		Envelope savedEnv = dao.save(envToSave);
//		assertNotNull(savedEnv);
//
//		int resultingCount = countRecordsOf(EnvelopeDTO.class);
//		
//		assertEquals(initialCount + 1, resultingCount);
	}

    @Test
    @Ignore
	public void shouldRetrieveSavedEnvelope() throws URISyntaxException {
//	    ElArchiverDaoImpl dao = new ElArchiverDaoImpl();
//		dao.setSession(sessionInit.getSession());
//
//        Envelope envToSave = creatMockEnvelope();
//        
//        Envelope savedEnv = dao.save(envToSave);
//        assertNotNull(savedEnv);
//
//        Envelope retrievedEnv = dao.getEnvelope(envToSave.getUid());
//		
//		assertEquals(savedEnv, retrievedEnv);
	}

    @Test
    @Ignore
    public void shouldReportNumberOfStoredEnvelopes() {
//        ElArchiverDaoImpl dao = new ElArchiverDaoImpl();
//        dao.setSession(sessionInit.getSession());
//        
//        int initialCount = countRecordsOf(EnvelopeDTO.class);
//        
//        Envelope envToSave = creatMockEnvelope();
//        
//        Envelope savedEnv = dao.save(envToSave);
//        assertNotNull(savedEnv);
//
//        int countingByCollectionSize = countRecordsOf(EnvelopeDTO.class);
//        
//        long countingByReportedNumber = dao.getNumberOfEnvelopes();
//
//        assertEquals(1, countingByCollectionSize);
//        assertEquals(1, countingByReportedNumber);

    }

//    private Envelope creatMockEnvelope() {
//        
//        String contents = "mock#"+ ++mockEnvelopeCount;
//        URI uid = CbidFactory.createCbid(contents);
//        
//        return new TextEnvelope(uid, contents, null);
//    }

	private int countRecordsOf(Class<?> entityClass) {
	    Session defaultSession = sessionInit.getSession();
	    DatabaseSession dbSession = defaultSession.getProject().createDatabaseSession();
	    dbSession.login();
	    int recordCount = dbSession.readAllObjects(entityClass).size();
	    dbSession.logout();
	    return recordCount;
	}
}
