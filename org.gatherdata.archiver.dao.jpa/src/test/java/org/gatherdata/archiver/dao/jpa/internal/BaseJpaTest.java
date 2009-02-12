package org.gatherdata.archiver.dao.jpa.internal;

import static org.junit.Assert.assertNotNull;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Base class for jpa testing.
 *
 */
public class BaseJpaTest {

    EntityManagerFactory emf;
    
    EntityManager em = null;
    
    @Before
    public void setupEntityManager() {
            emf = Persistence.createEntityManagerFactory("hibernateInMemory");

            em = emf.createEntityManager();
    }

    @Test
    public void shouldHaveEntityManagerFactory() {
        assertNotNull(emf);
    }
    
    @Test
    public void shouldHaveEntityManager() {
        assertNotNull(em);
    }
}
