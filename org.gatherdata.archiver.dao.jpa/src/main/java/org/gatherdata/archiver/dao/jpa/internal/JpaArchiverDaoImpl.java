/**
 * 
 */
package org.gatherdata.archiver.dao.jpa.internal;

import java.io.Serializable;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

import org.apache.commons.lang.NotImplementedException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.gatherdata.archiver.core.spi.ArchiverDao;
import org.gatherdata.core.jpa.dto.EnvelopeDTO;
import org.gatherdata.core.model.Envelope;
import org.gatherdata.core.spi.dao.ContentStorageDao;

import com.google.inject.Inject;

/**
 * JPA based implementation of ArchiverDao.
 *
 */
public class JpaArchiverDaoImpl implements ArchiverDao {
    /**
     * Log variable for all child classes. Uses LogFactory.getLog(getClass()) from Commons Logging
     */
    protected final Log log = LogFactory.getLog(getClass());

    public static final String PERSISTENCE_UNIT_NAME = "ApplicationEntityManager";
    
    private EntityManager entityManager;

    /**
     * Constructor that takes in a class to see which type of entity to persist.
     * Use this constructor when subclassing or using dependency injection.
     * @param persistentClass the class type you'd like to persist
     * @param entityManager the configured EntityManager for JPA implementation.
     */
    @Inject
    public JpaArchiverDaoImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public EntityTransaction beginTransaction() {
        EntityTransaction newTransaction = this.entityManager.getTransaction();
        newTransaction.begin();
        return newTransaction;
    }
    
    public void endTransaction(EntityTransaction transaction) {
        transaction.commit();
    }

    public EntityManager getEntityManager() {
        return this.entityManager;
    }

    /* (non-Javadoc)
     * @see org.gatherdata.archiver.core.spi.ArchiverDao#getProxiedEnvelope(java.net.URI, org.gatherdata.core.spi.dao.ContentStorageDao)
     */
    public Envelope getProxiedEnvelope(URI identifiedByUid,
            ContentStorageDao<? extends Serializable> usingAlternateContentDoa) {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see org.gatherdata.archiver.core.spi.ArchiverDao#saveEmpty(org.gatherdata.core.model.Envelope)
     */
    public Envelope saveEmpty(Envelope envelopeToSave) {
        EnvelopeDTO adaptedEnvelope = new EnvelopeDTO(envelopeToSave);
        adaptedEnvelope.setContents(null);
        return save(adaptedEnvelope);
    }

    /* (non-Javadoc)
     * @see org.gatherdata.archiver.core.spi.ArchiverDao#saveProxy(org.gatherdata.core.model.Envelope, org.gatherdata.core.spi.dao.ContentStorageDao)
     */
    public Envelope saveProxy(Envelope envelopeToSave, ContentStorageDao<? extends Serializable> usingAlternateContentDoa) {
        Serializable contentToSave = envelopeToSave.getContents();
        
        Envelope savedEnvelope = saveEmpty(envelopeToSave);
        
        usingAlternateContentDoa.saveAdapted(contentToSave);
        
        return savedEnvelope;
    }

    /* (non-Javadoc)
     * @see org.gatherdata.core.spi.dao.EnvelopeStorageDao#envelopeExists(java.net.URI)
     */
    public boolean envelopeExists(URI identifiedByUid) {
        Query q = this.entityManager.createQuery("SELECT COUNT(env) FROM EnvelopeDTO env WHERE env.uidAsString = :uidOfEnvelope");
        q.setParameter("uidOfEnvelope", identifiedByUid.toASCIIString());

        Long result = (Long)q.getSingleResult();
        return (result != 0);
    }

    /* (non-Javadoc)
     * @see org.gatherdata.core.spi.dao.EnvelopeStorageDao#envelopeExists(java.net.URI)
     */
    public boolean contentExists(URI identifiedByUid) {
        Query q = this.entityManager.createQuery("SELECT COUNT(env) FROM EnvelopeDTO env WHERE env.uidAsString = :uidOfEnvelope AND env.contents is NOT NULL");
        q.setParameter("uidOfEnvelope", identifiedByUid.toASCIIString());

        Long result = (Long)q.getSingleResult();
        return (result != 0);
    }
    
    /* (non-Javadoc)
     * @see org.gatherdata.core.spi.dao.EnvelopeStorageDao#getAllEnvelopes()
     */
    @SuppressWarnings("unchecked")
    public List<? extends Envelope> getAllEnvelopes() {
        return this.entityManager.createQuery(
                "select obj from EnvelopeDTO obj")
                .getResultList();
    }

    /* (non-Javadoc)
     * @see org.gatherdata.core.spi.dao.EnvelopeStorageDao#getEnvelope(java.net.URI)
     */
    public Envelope getEnvelope(URI identifiedByUid) {
        Envelope entity = this.entityManager.find(EnvelopeDTO.class, identifiedByUid.toASCIIString());

        if (entity == null) {
            String msg = "Uh oh, '" + EnvelopeDTO.class + "' object with id '" + identifiedByUid + "' not found...";
            log.warn(msg);
            throw new EntityNotFoundException(msg);
        }

        return entity;
    }

    /* (non-Javadoc)
     * @see org.gatherdata.core.spi.dao.EnvelopeStorageDao#getNumberOfEnvelopes()
     */
    public long getNumberOfEnvelopes() {
        Query q = this.entityManager.createQuery("SELECT COUNT(env) FROM EnvelopeDTO env");

        return (Long)q.getSingleResult();
    }

    /* (non-Javadoc)
     * @see org.gatherdata.core.spi.dao.EnvelopeStorageDao#removeEnvelope(java.net.URI)
     */
    public void removeEnvelope(URI identifiedByUid) {
        Query q = this.entityManager.createQuery("DELETE env FROM EnvelopeDTO env WHERE env.uidAsString = :uidOfEnvelope");
        q.setParameter("uidOfEnvelope", identifiedByUid.toASCIIString());
        int deleted = q.executeUpdate ();
        if (deleted > 1) {
            throw new InternalError("Multiple rows deleted when removing Envelope with uid {" + identifiedByUid + "}");
        }
    }

    /* (non-Javadoc)
     * @see org.gatherdata.core.spi.dao.EnvelopeStorageDao#save(org.gatherdata.core.model.Envelope)
     */
    public Envelope save(Envelope envelopeToSave) {
        Envelope savedEnvelope = null; 
        if (envelopeToSave != null) {
            EnvelopeDTO saveableEnvelope = new EnvelopeDTO(envelopeToSave);
        
            EntityTransaction tx = beginTransaction();
            
            savedEnvelope = this.entityManager.merge(saveableEnvelope);
            
            endTransaction(tx);
        }
        return savedEnvelope;
    }

    /* (non-Javadoc)
     * @see org.gatherdata.core.spi.dao.ContentStorageDao#adapt(java.io.Serializable)
     */
    public Serializable adapt(Serializable contentToAdapt) {
        return contentToAdapt; // no adaption needed
    }

    /* (non-Javadoc)
     * @see org.gatherdata.core.spi.dao.ContentStorageDao#getAllContents()
     */
    @SuppressWarnings("unchecked")
    public List<? extends Serializable> getAllContents() {
        Query q = this.entityManager.createQuery("SELECT env.contents FROM EnvelopeDTO env");

        List<Serializable> results = (List<Serializable>)q.getResultList();
        
        return results;
    }

    /* (non-Javadoc)
     * @see org.gatherdata.core.spi.dao.ContentStorageDao#getContent(java.net.URI)
     */
    public Serializable getContent(URI uidOfEnvelope) {
        Query q = this.entityManager.createQuery("SELECT env.contents FROM EnvelopeDTO env WHERE env.uidAsString = :uidOfEnvelope");
        q.setParameter("uidOfEnvelope", uidOfEnvelope.toASCIIString());

        Serializable result = (Serializable)q.getSingleResult();
        if (result == null) {
            String msg = "Uh oh, contents with of envelope uid '" + uidOfEnvelope + "' not found...";
            log.warn(msg);
            throw new EntityNotFoundException(msg);
        }
        return result;
    }

    /* (non-Javadoc)
     * @see org.gatherdata.core.spi.dao.ContentStorageDao#removeContent(java.net.URI)
     */
    public void removeContent(URI uidOfEnvelope) {
        // ABKTODO: use a jpql update to null the contents of the envelope
        throw new NotImplementedException();
    }

    /* (non-Javadoc)
     * @see org.gatherdata.core.spi.dao.ContentStorageDao#save(java.io.Serializable)
     */
    public Serializable save(Serializable contentToSave) {
        // ABKTODO: implement this. stuff contents into envelope. save it.
        throw new NotImplementedException();
    }

    /* (non-Javadoc)
     * @see org.gatherdata.core.spi.dao.ContentStorageDao#savedAdapted(java.io.Serializable)
     */
    public Serializable saveAdapted(Serializable proxiedContentToSave) {
        return save(proxiedContentToSave);
    }

    public long getNumberOfQualifiedEnvelopes(String withQualifier) {
        Query q = this.entityManager.createQuery("SELECT COUNT(env) FROM EnvelopeDTO env WHERE env.qualifier = :envelopeQualifier");
        q.setParameter("envelopeQualifier", withQualifier);

        return (Long)q.getSingleResult();
    }

    /**
     * ABKTODO: this is a horrible wasteful implementation. Instead of building an in-memory list,
     * this should use a proxy list which contains all the uids, but pulls the individual 
     * elements from the db on demand.  
     * 
     */
    public List<Envelope> getAllProxiedEnvelopes(ContentStorageDao<? extends Serializable> usingAlternateContentDao) {
        List<? extends URI> contentUids = usingAlternateContentDao.getAllContentUids();
        List<Envelope> envelopeList = new ArrayList<Envelope>();
        for (URI uid : contentUids) {
            envelopeList.add(getProxiedEnvelope(uid, usingAlternateContentDao));
        }
        return envelopeList;
    }

    public List<URI> getAllContentUids() {
        // this is the same as all Envelopes with non-null content
        Query q = this.entityManager.createQuery("SELECT env.uidAsString FROM EnvelopeDTO env WHERE env.contents is NOT NULL");

        List<String> resultList = (List<String>)q.getResultList();
        
        // ABKTODO: ugh. look into auto-converting from string to URI when retrieving objects with jpa
        // or, if not possible, then return a proxied list backed by the strings which converts on demand
        // instead of doing it all up front
        List<URI> resultAsUri = new ArrayList<URI>(resultList.size());
        for (String uidAsString : resultList) {
            try {
                resultAsUri.add(new URI(uidAsString));
            } catch (URISyntaxException e) {
                log.error("Stored envelope uid {" + uidAsString + "} is not a valid URI");
                e.printStackTrace();
            }
        }

        return resultAsUri;
    }

}
