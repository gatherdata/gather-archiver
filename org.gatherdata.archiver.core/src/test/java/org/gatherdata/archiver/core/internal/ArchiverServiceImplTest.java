package org.gatherdata.archiver.core.internal;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.*;

import java.io.Serializable;

import org.gatherdata.archiver.core.model.GatherArchive;
import org.gatherdata.archiver.core.model.MutableGatherArchive;
import org.gatherdata.archiver.core.spi.ArchiverDao;
import org.gatherdata.commons.net.CbidFactory;
import org.gatherdata.commons.spi.dao.StorageDao;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

public class ArchiverServiceImplTest {

    protected int mockPlainTextCount = 0;

    protected GatherArchive createMockArchive() {
        final String content = "mocked up plain text contents, for unit testing. item #" 
            + Integer.toString(mockPlainTextCount++);
        MutableGatherArchive mockEntity = new MutableGatherArchive();
        mockEntity.setDateCreated(new DateTime()); //urnFactory.getLocalUrn(), contents, MimeTypes.TEXT_PLAIN);
        mockEntity.setContent(content);
        mockEntity.setUid(CbidFactory.createCbid(content));
        return mockEntity;
    }
    
    @Test
    public void shouldUseDaoToSave()
    {
        ArchiverDao mockStorageDao = (ArchiverDao)createMock(ArchiverDao.class);
        ArchiverServiceImpl serviceImpl = new ArchiverServiceImpl();
        serviceImpl.dao = mockStorageDao;
        
        GatherArchive mockEntity = createMockArchive();

        expect(mockStorageDao.save(mockEntity)).andReturn(mockEntity);
        
        replay(mockStorageDao);
        
        serviceImpl.save(mockEntity);
        
        verify(mockStorageDao);
        
    }
    
    @Test
    public void shouldUseDaoToGet()
    {
        ArchiverDao mockStorageDao = (ArchiverDao)createMock(ArchiverDao.class);
        ArchiverServiceImpl serviceImpl = new ArchiverServiceImpl();
        serviceImpl.dao = mockStorageDao;
        
        GatherArchive mockEntity = createMockArchive();

        expect(mockStorageDao.get(mockEntity.getUid())).andReturn(mockEntity);
        
        replay(mockStorageDao);
        
        serviceImpl.get(mockEntity.getUid());
        
        verify(mockStorageDao);
        
    }

    @Test
    public void shouldUseDaoToCheckExists()
    {
        ArchiverDao mockStorageDao = (ArchiverDao)createMock(ArchiverDao.class);
        ArchiverServiceImpl serviceImpl = new ArchiverServiceImpl();
        serviceImpl.dao = mockStorageDao;
        
        GatherArchive mockEntity = createMockArchive();
        GatherArchive missingEntity = createMockArchive();

        expect(mockStorageDao.exists(mockEntity.getUid())).andReturn(true);
        expect(mockStorageDao.exists(missingEntity.getUid())).andReturn(false);
        
        replay(mockStorageDao);
        
        assertTrue(serviceImpl.exists(mockEntity.getUid()));
        assertFalse(serviceImpl.exists(missingEntity.getUid()));
        
        verify(mockStorageDao);
    }

}
