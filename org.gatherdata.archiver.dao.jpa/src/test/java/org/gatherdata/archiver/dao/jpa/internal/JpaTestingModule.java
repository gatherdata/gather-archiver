package org.gatherdata.archiver.dao.jpa.internal;

import org.hibernate.ejb.HibernatePersistence;

import com.google.inject.AbstractModule;

public class JpaTestingModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(javax.persistence.spi.PersistenceProvider.class).to(HibernatePersistence.class);
    }

}
