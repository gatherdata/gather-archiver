/**
 * The contents of this file are subject to the AED Public Use License Agreement, Version 1.0 (the "License");
 * use in any manner is strictly prohibited except in compliance with the terms of the License.
 * The License is available at http://gatherdata.org/license.
 *
 * Copyright (c) AED.  All Rights Reserved
 */
package org.gatherdata.archiver.dao.neo4j.internal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertThat;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.verify;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import javax.swing.filechooser.FileSystemView;

import org.apache.commons.io.FileUtils;
import org.easymock.internal.matchers.Contains;
import org.gatherdata.archiver.core.model.GatherArchive;
import org.gatherdata.archiver.core.model.MutableGatherArchive;
import org.gatherdata.archiver.core.spi.ArchiverDao;
import org.gatherdata.archiver.core.spi.BaseArchiverDaoTest;
import org.gatherdata.archiver.dao.neo4j.internal.ArchiverDaoNeo;
import org.gatherdata.commons.db.neo4j.NeoServices;
import org.gatherdata.commons.net.CbidFactory;
import org.gatherdata.commons.net.GatherUrnFactory;
import org.hamcrest.core.IsNot;
import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.neo4j.api.core.EmbeddedNeo;
import org.neo4j.api.core.NeoService;
import org.neo4j.api.core.Transaction;
import org.neo4j.util.index.IndexService;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;

/**
 * Unit tests for the behavior of the NeoArchiverDaoImpl.
 *
 */
public class NeoArchiverDaoImplTest extends BaseArchiverDaoTest {

    @Inject
    NeoServices neo;
        
    int mockPlainTextCount = 0;

    private Transaction transaction;
    
    @Override
    protected ArchiverDao createStorageDaoImpl() {
        ArchiverDao dao = new ArchiverDaoNeo();
        
        // guice up the instance
        Injector injector = Guice.createInjector(new NeoTestingModule());
        injector.injectMembers(this);
        injector.injectMembers(dao);
        
        ArchiverDaoNeo.log.setLevel(Level.ALL);
        return dao;
    }
    
    @After
    public void shutdownNeo() {
        String neoStoreDir = ((EmbeddedNeo)neo.neo()).getStoreDir();
        neo.manualShutdown();
        try {
            FileUtils.cleanDirectory(new File(neoStoreDir));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected GatherArchive createMockEntity() {
        final String content = "mocked up plain text contents, for unit testing. item #" 
            + Integer.toString(mockPlainTextCount++);
        MutableGatherArchive mockEntity = new MutableGatherArchive();
        mockEntity.setDateCreated(new DateTime()); 
        mockEntity.setContent(content);
        mockEntity.setUid(CbidFactory.createCbid(content));
        return mockEntity;
    }

    @Override
    protected void beginTransaction() {
        this.transaction = neo.neo().beginTx();
    }

    @Override
    protected void endTransaction() {
        this.transaction.success();
        this.transaction.finish();
    }
   
}
