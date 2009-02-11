package org.gatherdata.archiver.core.osgi;

import static org.ops4j.peaberry.Peaberry.service;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;

import org.gatherdata.archiver.core.internal.EnvelopeArchiverImpl;
import org.gatherdata.archiver.core.spi.ArchiverDao;
import org.gatherdata.archiver.core.spi.EnvelopeArchiver;
import org.ops4j.peaberry.Export;

import static org.ops4j.peaberry.util.TypeLiterals.iterable;
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
		bind(iterable(ArchiverDao.class)).toProvider(service(ArchiverDao.class).multiple());
		
		// exports
		bind(export(EnvelopeArchiver.class)).toProvider(service(EnvelopeArchiverImpl.class).export());
		
	}
}