/**
 * The contents of this file are subject to the AED Public Use License Agreement, Version 1.0 (the "License");
 * use in any manner is strictly prohibited except in compliance with the terms of the License.
 * The License is available at http://gatherdata.org/license.
 *
 * Copyright (c) AED.  All Rights Reserved
 */
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
import javax.transaction.Transaction;

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

    protected String persistenceUnitName = "archiver-hsql-server";

    private EntityManagerFactory emf;

    private EntityManager em;

    @Inject
    private PersistenceProvider persistenceProvider;

    private EntityTransaction currentTransaction;

    public JpaArchiverDaoImpl() {
        ;
    }

    public void beginTransaction() {
        EntityTransaction newTransaction = getEntityManager().getTransaction();
        newTransaction.begin();
        this.currentTransaction = newTransaction;
    }

    public void endTransaction() {
        currentTransaction.commit();
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
        Query q = getEntityManager()
                .createQuery("SELECT COUNT(arc) FROM GatherArchiveDTO arc WHERE arc.uidAsString = :uidOfArchive");
        q.setParameter("uidOfArchive", archiveIdentifiedByUid.toASCIIString());

        Long result = (Long) q.getSingleResult();
        
        System.out.println("exists: query [" + q + "] returned " + result);
        return (result != 0);
    }

    public GatherArchive get(URI archiveIdentifiedByUid) {
        GatherArchiveDTO entity = getEntityManager().find(GatherArchiveDTO.class, 
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
        return getEntityManager().createQuery("select obj from GatherArchiveDTO obj").getResultList();
    }

    public int getCount() {
        Query q = em.createQuery("SELECT COUNT(obj) FROM GatherArchiveDTO obj");
        Number result = (Number) q.getSingleResult();
        return result.intValue();
    }
    
    public void remove(URI archiveIdentifiedBy) {
        beginTransaction();
        GatherArchive entityToRemove = get(archiveIdentifiedBy);
        getEntityManager().remove(entityToRemove);
        endTransaction();
    }

    public GatherArchive save(GatherArchive entityToSave) {
        GatherArchive savedEntity = null;
        if (entityToSave != null) {
            GatherArchiveDTO saveableEntity = new GatherArchiveDTO().copy(entityToSave);
            saveableEntity.selfIdentify();

            savedEntity = getEntityManager().merge(saveableEntity);

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
