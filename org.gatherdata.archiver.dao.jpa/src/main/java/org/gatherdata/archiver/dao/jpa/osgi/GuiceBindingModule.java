/**
 * The contents of this file are subject to the AED Public Use License Agreement, Version 1.0 (the "License");
 * use in any manner is strictly prohibited except in compliance with the terms of the License.
 * The License is available at http://gatherdata.org/license.
 *
 * Copyright (c) AED.  All Rights Reserved
 */
package org.gatherdata.archiver.dao.jpa.osgi;

import static org.ops4j.peaberry.Peaberry.service;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;

import org.gatherdata.archiver.dao.jpa.internal.JpaArchiverDaoImpl;
import org.gatherdata.archiver.core.spi.ArchiverDao;

import static org.ops4j.peaberry.util.TypeLiterals.export;

/**
 * The GuiceBindingModule specifies bindings to provided and
 * consumed services for this bundle using Google Guice with
 * Peaberry extensions for OSGi.
 *
 */
public class GuiceBindingModule extends AbstractModule {
	
	@Override 
	protected void configure() {
	    
		// local binds
        bind(javax.persistence.spi.PersistenceProvider.class).to(org.eclipse.persistence.jpa.osgi.PersistenceProvider.class);
		
		// exports
		bind(export(ArchiverDao.class)).toProvider(service(JpaArchiverDaoImpl.class).export());
		
	}
	
}
