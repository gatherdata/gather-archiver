package org.gatherdata.archiver.dao.jpa.internal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.verify;

import java.io.Serializable;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.persistence.EntityTransaction;

import org.gatherdata.core.io.MimeTypes;
import org.gatherdata.core.model.Envelope;
import org.gatherdata.core.model.TextEnvelope;
import org.gatherdata.core.net.GatherUrnFactory;
import org.gatherdata.core.spi.dao.ContentStorageDao;
import org.junit.Before;
import org.junit.Test;

/**
 * Unit tests for the behavior of the JpaArchiverDaoImpl.
 *
 */
public class JpaArchiverDaoImplTest extends BaseJpaTest {

    JpaArchiverDaoImpl dao;
    GatherUrnFactory urnFactory;
    int mockPlainTextCount = 0;
    
    @Before
    public void setupTheDao() {
        dao = new JpaArchiverDaoImpl(em);
        urnFactory = new GatherUrnFactory();
    }

    private Envelope createMockEnvelopeWithPlainTextContents() {
        final String contents = "mocked up plain text contents, for unit testing. item #" 
            + Integer.toString(mockPlainTextCount++);
        TextEnvelope mockEnvelope = new TextEnvelope(urnFactory.getLocalUrn(), contents, MimeTypes.TEXT_PLAIN);
        mockEnvelope.setQualifier("en_US");
        return mockEnvelope;
    }
    
    /**
     * The Envelope returned from a save operation should be equal to the
     * Envelope passed in (though it may not be the same object).
     */
    @Test 
    public void shouldReturnEquivalentEnvelopeDuringSaveOfEnvelopeWithPlainTextContents() {
        Envelope envelopeToSave = createMockEnvelopeWithPlainTextContents();
        Envelope savedEnvelope = dao.save(envelopeToSave);
        assertEquals(envelopeToSave, savedEnvelope);
    }
    
    /**
     * In support of proxied content storage, the archiver dao is expected
     * to save an Envelope without saving the content inside.
     * 
     */
    @Test 
    public void shouldSaveEnvelopeWithoutSavingContents() {
        Envelope envelopeToSave = createMockEnvelopeWithPlainTextContents();
        Envelope savedEnvelope = dao.saveEmpty(envelopeToSave);
        assertNull(savedEnvelope.getContents());
    }
    
    /**
     * In support of proxied content storage, the archiver dao should
     * use a provided ContentStorageDao to save the content of an envelope,
     * saving the envelope itself without the contents.
     * 
     */
    @SuppressWarnings("unchecked")
    @Test
    public void shouldSaveContentsUsingAProxyContentStorage() {

        Envelope envelopeToSave = createMockEnvelopeWithPlainTextContents();
        Serializable contents = envelopeToSave.getContents();
        
        ContentStorageDao<Serializable> mockContentStorageDao;
        mockContentStorageDao = (ContentStorageDao<Serializable>)createMock(ContentStorageDao.class);
        
        expect(mockContentStorageDao.saveAdapted(contents)).andReturn(contents);
        
        replay(mockContentStorageDao);
        
        Envelope savedEnvelope = dao.saveProxy(envelopeToSave, mockContentStorageDao);
        assertNotNull(savedEnvelope);
        assertNull(savedEnvelope.getContents());
        
        verify(mockContentStorageDao);
    }
    
    /**
     * After saving an Envelope, the dao should affirm that it exists
     * in the storage.
     * 
     */
    @Test
    public void shouldAffirmThatASavedEnvelopeExists() {
        Envelope envelopeToSave = createMockEnvelopeWithPlainTextContents();
        
        dao.save(envelopeToSave);
        
        assertTrue(dao.envelopeExists(envelopeToSave.getUid()));
    }
    
    @Test
    public void shouldGetAllSavedEnvelopes() {
        final int EXPECTED_NUMBER_OF_ENVELOPES = new Random().nextInt(100);
        List<Envelope> envelopesToSave = new ArrayList<Envelope>();
        
        for (int i=0; i< EXPECTED_NUMBER_OF_ENVELOPES; i++) {
            Envelope envelopeToSave = createMockEnvelopeWithPlainTextContents();
            envelopesToSave.add(envelopeToSave);
            dao.save(envelopeToSave);
        }
        
        List<? extends Envelope> savedEnvelopeList = dao.getAllEnvelopes();
        assertEquals(EXPECTED_NUMBER_OF_ENVELOPES, savedEnvelopeList.size());
    }
    
    @Test
    public void shouldGetAllSavedContentUids() {

        final int EXPECTED_NUMBER_OF_ENVELOPES = new Random().nextInt(100);
        List<Envelope> envelopesToSave = new ArrayList<Envelope>();
        
        for (int i=0; i< EXPECTED_NUMBER_OF_ENVELOPES; i++) {
            Envelope envelopeToSave = createMockEnvelopeWithPlainTextContents();
            envelopesToSave.add(envelopeToSave);
            dao.save(envelopeToSave);
        }
        
        List<URI> savedUidList = dao.getAllContentUids();
        assertEquals(envelopesToSave.size(), savedUidList.size());
    }
    
    @Test
    public void shouldGetASpecificEnvelopeIdentifiedByUid() {
        final int EXPECTED_NUMBER_OF_ENVELOPES = new Random().nextInt(100);
        List<Envelope> envelopesToSave = new ArrayList<Envelope>();
        
        for (int i=0; i< EXPECTED_NUMBER_OF_ENVELOPES; i++) {
            Envelope envelopeToSave = createMockEnvelopeWithPlainTextContents();
            envelopesToSave.add(envelopeToSave);
            
            dao.save(envelopeToSave);
        }
        
        for (Envelope envelopeToRetreive : envelopesToSave) {
            Envelope retrievedEnvelope = dao.getEnvelope(envelopeToRetreive.getUid());
            assertNotNull(retrievedEnvelope);
            assertEquals(envelopeToRetreive, retrievedEnvelope);
        }
    }
    
    @Test
    public void shouldReportTheNumberOfSavedEnvelopes() {
        final int EXPECTED_NUMBER_OF_ENVELOPES = new Random().nextInt(100);
        List<Envelope> envelopesToSave = new ArrayList<Envelope>();
        
        for (int i=0; i< EXPECTED_NUMBER_OF_ENVELOPES; i++) {
            Envelope envelopeToSave = createMockEnvelopeWithPlainTextContents();
            envelopesToSave.add(envelopeToSave);
            
            dao.save(envelopeToSave);
        }
        
        assertEquals(EXPECTED_NUMBER_OF_ENVELOPES, dao.getNumberOfEnvelopes());
    }
    
    @Test
    public void shouldReportTheNumberOfQualifiedSavedEnvelopes() {
        final int EXPECTED_NUMBER_OF_ENVELOPES = new Random().nextInt(100);
        List<Envelope> envelopesToSave = new ArrayList<Envelope>();
        
        for (int i=0; i< EXPECTED_NUMBER_OF_ENVELOPES; i++) {
            Envelope envelopeToSave = createMockEnvelopeWithPlainTextContents();
            envelopesToSave.add(envelopeToSave);
            
            dao.save(envelopeToSave);
        }
        
        assertEquals(EXPECTED_NUMBER_OF_ENVELOPES, dao.getNumberOfQualifiedEnvelopes("en_US"));
    }

    @Test
    public void shouldAffirmExistenceOfContentsFromFilledEnvelope() {

        Envelope envelopeToSave = createMockEnvelopeWithPlainTextContents();
        
        dao.save(envelopeToSave);
        
        assertTrue(dao.contentExists(envelopeToSave.getUid()));
    }
    
    @Test
    public void shouldDenyExistenceOfContentsFromEmptyEnvelope() {

        Envelope envelopeToSave = createMockEnvelopeWithPlainTextContents();
        
        dao.saveEmpty(envelopeToSave);
        
        assertFalse(dao.contentExists(envelopeToSave.getUid()));
    }

}
