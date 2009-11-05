/**
 * The contents of this file are subject to the AED Public Use License Agreement, Version 1.0 (the "License");
 * use in any manner is strictly prohibited except in compliance with the terms of the License.
 * The License is available at http://gatherdata.org/license.
 *
 * Copyright (c) AED.  All Rights Reserved
 */
package org.gatherdata.archiver.dao.neo4j.osgi;

import static com.google.inject.Guice.createInjector;
import static org.ops4j.peaberry.Peaberry.osgiModule;

import java.util.Dictionary;
import java.util.Properties;
import java.util.logging.Logger;

import org.gatherdata.archiver.core.spi.ArchiverDao;
import org.ops4j.peaberry.Export;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import com.google.inject.Inject;

/**
 * Extension of the default OSGi bundle activator
 */
public final class OSGiActivator
    implements BundleActivator
{
    private static final Logger log = Logger.getLogger(OSGiActivator.class.getName());

    @Inject
    Export<ArchiverDao> dao;
    
    /**
     * Called whenever the OSGi framework starts our bundle
     */
    public void start( BundleContext bc )
        throws Exception
    {
        log.info( "STARTING org.gatherdata.forms.dao.neo4j" );

        createInjector(osgiModule(bc), new GuiceBindingModule()).injectMembers(this);

    }

    /**
     * Called whenever the OSGi framework stops our bundle
     */
    public void stop( BundleContext bc )
        throws Exception
    {
        log.info( "STOPPING org.gatherdata.forms.dao.neo4j" );
    }
}

