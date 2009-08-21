package org.gatherdata.archiver.core.spi;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.verify;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.not;

import java.io.Serializable;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.gatherdata.archiver.core.spi.ContainsAll.containsAll;
import org.easymock.internal.matchers.Contains;
import org.gatherdata.archiver.core.model.GatherArchive;
import org.gatherdata.archiver.core.model.MutableGatherArchive;
import org.gatherdata.archiver.core.spi.ArchiverDao;
import org.gatherdata.commons.io.MimeTypes;
import org.gatherdata.commons.net.CbidFactory;
import org.gatherdata.commons.net.GatherUrnFactory;
import org.gatherdata.commons.spi.dao.StorageDao;
import org.hamcrest.core.IsNot;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

/**
 * Base unit tests for the behavior of ArchiverDao implementations.
 *
 */
public abstract class BaseArchiverDaoTest {

    protected ArchiverDao dao;
    protected GatherUrnFactory urnFactory;
    protected CbidFactory cbidFactory;

    protected abstract GatherArchive createMockEntity();

    protected abstract void beginTransaction();
    
    protected abstract void endTransaction();
    
    /**
     * The entity returned from a save operation should be equal to the
     * entity passed in (though it may not be the same object).
     */
    @Test 
    public void shouldReturnEntityWhichIsEqualToSavedEntity() {
        GatherArchive entityToSave = createMockEntity();
        GatherArchive savedEntity = dao.save(entityToSave);
        assertEquals(entityToSave, savedEntity);
    }
    
    /**
     * After saving an entity, the dao should affirm that it exists
     * in the storage.
     */
    @Test
    public void shouldAffirmThatASavedEntityExists() {
        GatherArchive entityToSave = createMockEntity();
        GatherArchive savedEntity = dao.save(entityToSave);
        
        assertTrue(dao.exists(entityToSave.getUid()));
    }
    
    @Test
    public void shouldGetAllSavedEntities() {
        final int EXPECTED_NUMBER_OF_ENTITIES = new Random().nextInt(100);
        List<GatherArchive> entitiesToSave = new ArrayList<GatherArchive>();
        
        for (int i=0; i< EXPECTED_NUMBER_OF_ENTITIES; i++) {
            GatherArchive entityToSave = createMockEntity();
            entitiesToSave.add(entityToSave);
            dao.save(entityToSave);
        }
        
        beginTransaction();
        Iterable<GatherArchive> allEntitiesList = dao.getAll();
        assertThat(allEntitiesList, containsAll(entitiesToSave));
        endTransaction();
    }

    @Test
    public void shouldGetASpecificEntityIdentifiedByUid() {
        final int EXPECTED_NUMBER_OF_ENTITIES = new Random().nextInt(100);
        List<GatherArchive> entitiesToSave = new ArrayList<GatherArchive>();
        
        for (int i=0; i< EXPECTED_NUMBER_OF_ENTITIES; i++) {
            GatherArchive entityToSave = createMockEntity();
            entitiesToSave.add(entityToSave);
            dao.save(entityToSave);
        }
        
        for (GatherArchive entityToRetrieve : entitiesToSave) {
            GatherArchive retrievedEntity = dao.get(entityToRetrieve.getUid());
            assertNotNull(retrievedEntity);
            assertEquals(entityToRetrieve, retrievedEntity);
        }
    }
    
    @Test
    public void shouldRemoveSpecificEntityIdentifiedByUid() {
        final int INITIAL_NUMBER_OF_ENTITIES = new Random().nextInt(100);
        final int INDEX_OF_ENTITY_TO_REMOVE = new Random().nextInt(INITIAL_NUMBER_OF_ENTITIES);
        List<GatherArchive> entitiesToSave = new ArrayList<GatherArchive>();
        
        GatherArchive entityToRemove = null;
        for (int i=0; i< INITIAL_NUMBER_OF_ENTITIES; i++) {
            GatherArchive entityToSave = createMockEntity();
            entitiesToSave.add(entityToSave);
            dao.save(entityToSave);
            if (i == INDEX_OF_ENTITY_TO_REMOVE) {
                entityToRemove = entityToSave;
            }
        }
        
        dao.remove(entityToRemove.getUid());
        
        assertFalse(dao.exists(entityToRemove.getUid()));

        Iterable<GatherArchive> allEntitiesList = dao.getAll();
        assertThat(allEntitiesList, not(containsAll(entitiesToSave)) );
    }

}
