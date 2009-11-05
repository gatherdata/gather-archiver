/**
 * The contents of this file are subject to the AED Public Use License Agreement, Version 1.0 (the "License");
 * use in any manner is strictly prohibited except in compliance with the terms of the License.
 * The License is available at http://gatherdata.org/license.
 *
 * Copyright (c) AED.  All Rights Reserved
 */
package org.gatherdata.archiver.dao.db4o.internal;

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

import org.apache.commons.io.FileUtils;
import org.easymock.internal.matchers.Contains;
import org.gatherdata.archiver.core.model.GatherArchive;
import org.gatherdata.archiver.core.model.MutableGatherArchive;
import org.gatherdata.archiver.core.spi.ArchiverDao;
import org.gatherdata.archiver.core.spi.BaseArchiverDaoTest;
import org.gatherdata.commons.net.CbidFactory;
import org.gatherdata.commons.net.GatherUrnFactory;
import org.hamcrest.core.IsNot;
import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.db4o.ObjectContainer;
import com.db4o.osgi.Db4oService;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;

/**
 * Unit tests for the behavior of the Db4oArchiverDao.
 *
 */
public class Db4oArchiverDaoTest extends BaseArchiverDaoTest {

    @Inject
    ObjectContainer db4o;

    @Override
    protected ArchiverDao createStorageDaoImpl() {
        ArchiverDao dao = new ArchiverDaoDb4o();
        
        // guice up the instance
        Injector injector = Guice.createInjector(new Db4oTestingModule());
        injector.injectMembers(this);
        injector.injectMembers(dao);
        
        return dao;
    }
    
    @After
    public void shutdownDatabase() {
        db4o.commit();
        db4o.close();
    }
    
    @Override
    protected void beginTransaction() {
        ;
    }

    @Override
    protected void endTransaction() {
        ;
    }
   
}
