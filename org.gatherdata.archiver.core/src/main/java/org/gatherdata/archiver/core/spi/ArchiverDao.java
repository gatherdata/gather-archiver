package org.gatherdata.archiver.core.spi;

import java.io.Serializable;
import java.net.URI;
import java.util.List;

import org.gatherdata.core.model.Envelope;
import org.gatherdata.core.spi.dao.ContentStorageDao;
import org.gatherdata.core.spi.dao.EnvelopeStorageDao;

/**
 * The DAO used by the EnvelopeArchiver. All content is persisted 
 * directly as a Serialized object.
 * 
 * The ArchiverDao builds on the basic EnvelopeStorageDao by providing
 * functionality to allow working in tandem with a content-specific dao.
 * 
 * Implementations can be loaded from other bundles by simply
 * registering the DAO as an OSGi service. 
 * 
 */
public interface ArchiverDao extends EnvelopeStorageDao, ContentStorageDao<Serializable> {

    Envelope saveEmpty(Envelope envelopeToSave);
    
    Envelope saveProxy(Envelope envelopeToSave, ContentStorageDao<? extends Serializable> usingAlternateContentDoa);
    
    Envelope getProxiedEnvelope(URI identifiedByUid, ContentStorageDao<? extends Serializable> usingAlternateContentDoa);
    
    List<Envelope> getAllProxiedEnvelopes(ContentStorageDao<? extends Serializable> usingAlternateContentDao);
    
    /**
     * Returns a count of Envelopes which have a particular qualifier.
     * 
     * @param withQualifier
     * @return
     */
    long getNumberOfQualifiedEnvelopes(String withQualifier);
}
