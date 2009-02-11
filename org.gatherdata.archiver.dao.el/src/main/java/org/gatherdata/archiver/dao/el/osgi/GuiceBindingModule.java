package org.gatherdata.archiver.dao.el.osgi;

import static org.ops4j.peaberry.Peaberry.service;

import com.google.inject.AbstractModule;

import org.gatherdata.archiver.dao.el.internal.ElArchiverDaoImpl;
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
		// imports
		
		// exports
		bind(export(ArchiverDao.class)).toProvider(service(new ElArchiverDaoImpl()).export());
		
	}
}
