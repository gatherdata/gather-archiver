/**
 * The contents of this file are subject to the AED Public Use License Agreement, Version 1.0 (the "License");
 * use in any manner is strictly prohibited except in compliance with the terms of the License.
 * The License is available at http://gatherdata.org/license.
 *
 * Copyright (c) AED.  All Rights Reserved
 */
package org.gatherdata.archiver.dao.db4o.osgi;

import static org.ops4j.peaberry.Peaberry.service;

import java.io.File;

import com.db4o.EmbeddedObjectContainer;
import com.db4o.ObjectContainer;
import com.db4o.config.Configuration;
import com.db4o.osgi.Db4oService;
import com.db4o.typehandlers.TypeHandler4;
import com.db4o.typehandlers.TypeHandlerPredicate;
import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Provides;
import com.google.inject.name.Named;
import com.google.inject.name.Names;

import org.gatherdata.archiver.core.spi.ArchiverDao;
import org.gatherdata.archiver.dao.db4o.internal.ArchiverDaoDb4o;
import org.gatherdata.commons.db.db4o.DateTimeHandler;
import org.gatherdata.commons.db.db4o.DateTimeHandlerPredicate;
import org.ops4j.peaberry.Import;
import org.ops4j.peaberry.util.AbstractWatcher;

import static org.ops4j.peaberry.util.TypeLiterals.export;
import static org.ops4j.peaberry.util.Filters.ldap;

/**
 * The GuiceBindingModule specifies bindings to provided and
 * consumed services for this bundle using Google Guice with
 * Peaberry extensions for OSGi.
 *
 */
public class ArchiverDaoModule extends AbstractModule {
	
    public static final String DEFAULT_DATA_DIR = "archiver" + File.separatorChar + "db4o";

    @Inject
    Db4oService db4oService;
    
	@Override 
	protected void configure() {
        new File(DEFAULT_DATA_DIR).mkdirs();
        
        Configuration config = db4oService.newConfiguration();
        
        // ABKTODO: figure out what is wrong with the custom TypeHandler support
//        TypeHandlerPredicate dateTimePredicate = new DateTimeHandlerPredicate();
//        TypeHandler4 dateTimeHandler = new DateTimeHandler();
//        config.registerTypeHandler(dateTimePredicate, dateTimeHandler);
        
        ObjectContainer archiverObjectContainer = db4oService.openFile(config, DEFAULT_DATA_DIR + File.separatorChar + "db4o.db");

		// local binds
        bind(ObjectContainer.class).toInstance(archiverObjectContainer);

		// exports
		bind(export(ArchiverDao.class)).toProvider(service(ArchiverDaoDb4o.class).export());
		
		
	}
	
}
