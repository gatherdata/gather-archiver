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
import com.db4o.osgi.Db4oService;
import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Provides;
import com.google.inject.name.Named;
import com.google.inject.name.Names;

import org.gatherdata.archiver.core.spi.ArchiverDao;
import org.gatherdata.archiver.dao.db4o.internal.ArchiverDaoDb4o;
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
public class GuiceBindingModule extends AbstractModule {
	
	@Override 
	protected void configure() {
		// imports
		bind(Db4oService.class).toProvider(service(Db4oService.class).single());
	}
	
}
