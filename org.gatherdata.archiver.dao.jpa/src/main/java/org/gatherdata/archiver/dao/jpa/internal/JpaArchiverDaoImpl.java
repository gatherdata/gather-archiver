/**
 * 
 */
package org.gatherdata.archiver.dao.jpa.internal;

import java.io.Serializable;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.List;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityNotFoundException;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import javax.persistence.spi.PersistenceProvider;

import org.gatherdata.archiver.core.model.GatherArchive;
import org.gatherdata.archiver.core.spi.ArchiverDao;
import org.gatherdata.archiver.dao.jpa.model.GatherArchiveDTO;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;

import com.google.inject.Inject;

/**
 * JPA based implementation of ArchiverDao.
 * 
 */
public class JpaArchiverDaoImpl implements ArchiverDao, ManagedService {

    private final Logger log = Logger.getLogger(JpaArchiverDaoImpl.class.getName());

    private String persistenceUnitName = null;

    private EntityManagerFactory emf;

    private EntityManager em;

    @Inject
    private PersistenceProvider persistenceProvider;


    /**
     * Constructor that takes in a class to see which type of entity to persist.
     * Use this constructor when subclassing or using dependency injection.
     * 
     * @param persistenceUnitName name of the persistenceUnit from 
     *  which an EntityManager will be derived
     */
    public JpaArchiverDaoImpl(String persistenceUnitName) {
        this.persistenceUnitName = persistenceUnitName;
    }

    public EntityTransaction beginTransaction() {
        EntityTransaction newTransaction = getEntityManager().getTransaction();
        newTransaction.begin();
        return newTransaction;
    }

    public void endTransaction(EntityTransaction transaction) {
        transaction.commit();
    }

    public EntityManager getEntityManager() {
        if (em == null) {
            em = getEntityManagerFactory().createEntityManager();
        }
        return this.em;
    }

    private EntityManagerFactory getEntityManagerFactory() {
        if (emf == null) {
            emf = persistenceProvider.createEntityManagerFactory(persistenceUnitName, null);
        }
        return emf;
    }

    public boolean exists(URI archiveIdentifiedByUid) {
        Query q = this.em
                .createQuery("SELECT COUNT(arc) FROM GatherArchiveDTO arc WHERE arc.uidAsString = :uidOfArchive");
        q.setParameter("uidOfArchive", archiveIdentifiedByUid.toASCIIString());

        Long result = (Long) q.getSingleResult();
        return (result != 0);
    }

    public GatherArchive get(URI archiveIdentifiedByUid) {
        GatherArchiveDTO entity = this.em.find(GatherArchiveDTO.class, 
                archiveIdentifiedByUid.toASCIIString());

        if (entity == null) {
            String msg = "Uh oh, '" + GatherArchiveDTO.class + "' object with id '" + archiveIdentifiedByUid
                    + "' not found...";
            log.warning(msg);
            throw new EntityNotFoundException(msg);
        }

        return entity;
    }

    @SuppressWarnings("unchecked")
    public List<GatherArchive> getAll() {
        return this.em.createQuery("select obj from GatherArchiveDTO obj").getResultList();
    }

    public void remove(URI archiveIdentifiedBy) {
        EntityTransaction tx = beginTransaction();
        GatherArchive entityToRemove = get(archiveIdentifiedBy);
        this.em.remove(entityToRemove);
        endTransaction(tx);
    }

    public GatherArchive save(GatherArchive entityToSave) {
        GatherArchive savedEntity = null;
        if (entityToSave != null) {
            GatherArchiveDTO saveableEntity = GatherArchiveDTO.deriveInstanceFrom(entityToSave);

            EntityTransaction tx = beginTransaction();

            savedEntity = this.em.merge(saveableEntity);

            endTransaction(tx);
        }
        return savedEntity;
    }

    /**
     * ABKTODO: implement this to allow easy configuration of the dao
     */
    public void updated(Dictionary updatedProperties) throws ConfigurationException {
        if (updatedProperties == null) {
            // no configuration from configuration admin
            // or old configuration has been deleted.
            // ignore it
        } else {
            // apply configuration from config admin
        }
        
    }

}
