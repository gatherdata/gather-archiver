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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.lessThan;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

import java.io.Serializable;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.persistence.EntityTransaction;

import org.easymock.internal.matchers.Contains;
import org.gatherdata.archiver.core.model.GatherArchive;
import org.gatherdata.archiver.core.model.MutableGatherArchive;
import org.gatherdata.archiver.core.spi.ArchiverDao;
import org.gatherdata.archiver.core.spi.BaseArchiverDaoTest;
import org.gatherdata.commons.io.MimeTypes;
import org.gatherdata.commons.net.CbidFactory;
import org.gatherdata.commons.net.GatherUrnFactory;
import org.gatherdata.commons.spi.dao.StorageDao;
import org.hamcrest.core.IsNot;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

import com.google.inject.Guice;
import com.google.inject.Injector;

/**
 * Unit tests for the behavior of the JpaArchiverDaoImpl.
 *
 */
public class JpaArchiverDaoImplTest extends BaseArchiverDaoTest {

    int mockPlainTextCount = 0;
    
    @Before
    public void setupTheDao() {
        dao = new JpaArchiverDaoImpl("hibernateInMemory");
        
        // guice up the instance
        Injector injector = Guice.createInjector(new JpaTestingModule());
        injector.injectMembers(dao);
        
        urnFactory = new GatherUrnFactory();
        cbidFactory = new CbidFactory();
    }

    @Override
    protected GatherArchive createMockEntity() {
        final String content = "mocked up plain text contents, for unit testing. item #" 
            + Integer.toString(mockPlainTextCount++);
        MutableGatherArchive mockEntity = new MutableGatherArchive();
        mockEntity.setDateCreated(new DateTime()); //urnFactory.getLocalUrn(), contents, MimeTypes.TEXT_PLAIN);
        mockEntity.setContent(content);
        mockEntity.setUid(CbidFactory.createCbid(content));
        return mockEntity;
    }
   
}
