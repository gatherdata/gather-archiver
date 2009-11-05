/**
 * The contents of this file are subject to the AED Public Use License Agreement, Version 1.0 (the "License");
 * use in any manner is strictly prohibited except in compliance with the terms of the License.
 * The License is available at http://gatherdata.org/license.
 *
 * Copyright (c) AED.  All Rights Reserved
 */
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
import org.gatherdata.archiver.core.spi.ArchiverService;
import org.gatherdata.commons.net.CbidFactory;
import org.gatherdata.commons.spi.BaseStorageServiceTest;
import org.gatherdata.commons.spi.StorageDao;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

public class ArchiverServiceImplTest extends BaseStorageServiceTest<GatherArchive, ArchiverDao, ArchiverServiceImpl> {

    protected int mockPlainTextCount = 0;

    protected GatherArchive createMockEntity() {
        final String content = "mocked up plain text contents, for unit testing. item #" 
            + Integer.toString(mockPlainTextCount++);
        MutableGatherArchive mockEntity = new MutableGatherArchive();
        mockEntity.setDateCreated(new DateTime()); //urnFactory.getLocalUrn(), contents, MimeTypes.TEXT_PLAIN);
        mockEntity.setContent(content);
        mockEntity.setUid(CbidFactory.createCbid(content));
        return mockEntity;
    }

    @Override
    protected ArchiverDao createMockDao() {
        return createMock(ArchiverDao.class);
    }

    @Override
    protected ArchiverServiceImpl createStorageServiceImpl() {
        return new ArchiverServiceImpl();
    }

    @Override
    protected void injectDaoIntoService(ArchiverDao dao, ArchiverServiceImpl service) {
        service.dao = dao;
    }
    

}
