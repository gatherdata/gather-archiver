package org.gatherdata.archiver.dao.db4o.osgi;

import static org.ops4j.peaberry.Peaberry.service;

import java.io.File;

import com.db4o.ObjectContainer;
import com.db4o.osgi.Db4oService;
import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Provides;
import com.google.inject.name.Named;
import com.google.inject.name.Names;

import org.gatherdata.archiver.core.spi.ArchiverDao;
import org.gatherdata.archiver.dao.db4o.internal.ArchiverDaoDb4o;

import static org.ops4j.peaberry.util.TypeLiterals.export;
import static org.ops4j.peaberry.util.Filters.ldap;

/**
 * The GuiceBindingModule specifies bindings to provided and
 * consumed services for this bundle using Google Guice with
 * Peaberry extensions for OSGi.
 *
 */
public class GuiceBindingModule extends AbstractModule {
	
    public static final String DEFAULT_DATA_DIR = "archiver" + File.separatorChar + "db4o";

	@Override 
	protected void configure() {
		// imports
		bind(Db4oService.class).annotatedWith(Names.named("db4oservice")).toProvider(service(Db4oService.class).single());
		
		// exports
		bind(export(ArchiverDao.class)).toProvider(service(ArchiverDaoDb4o.class).export());
		
		// local binds
		bind(ObjectContainer.class).to(ObjectContainer.class);
		
	}
	
	@Provides
	public ObjectContainer createObjectContainer(@Named("db4oservice") Db4oService db4oService) {
            new File(DEFAULT_DATA_DIR).mkdirs();
        
            return db4oService.openFile(DEFAULT_DATA_DIR + File.separatorChar + "db4o.db");
	}
}
