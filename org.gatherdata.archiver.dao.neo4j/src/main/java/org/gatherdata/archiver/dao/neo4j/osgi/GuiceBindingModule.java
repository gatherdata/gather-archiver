package org.gatherdata.archiver.dao.neo4j.osgi;

import static org.ops4j.peaberry.Peaberry.service;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;

import org.gatherdata.archiver.core.spi.ArchiverDao;
import org.gatherdata.archiver.dao.neo4j.internal.NeoArchiverDaoImpl;
import org.neo4j.api.core.NeoService;
import org.neo4j.util.index.IndexService;

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
		bind(NeoService.class).toProvider(service(NeoService.class).single());
        bind(IndexService.class).toProvider(service(IndexService.class).single());
		
		// exports
		bind(export(ArchiverDao.class)).toProvider(service(NeoArchiverDaoImpl.class).export());
		
	}
	
}
