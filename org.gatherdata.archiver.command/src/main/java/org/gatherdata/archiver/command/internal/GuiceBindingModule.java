package org.gatherdata.archiver.command.internal;

import java.util.Properties;

import org.gatherdata.archiver.core.spi.ArchiverService;
import org.osgi.framework.Constants;

import com.google.inject.AbstractModule;
import static org.ops4j.peaberry.Peaberry.service;
import static org.ops4j.peaberry.util.Attributes.properties;
import static org.ops4j.peaberry.util.TypeLiterals.export;
import static org.ops4j.peaberry.util.TypeLiterals.iterable;

public class GuiceBindingModule extends AbstractModule {

    @Override
    protected void configure() {
        // import all ArchiverService
        bind(ArchiverService.class).toProvider(service(ArchiverService.class).single());
        
        // export the CamelCommandImpl
        Properties ccAttrs = new Properties();
        ccAttrs.put(Constants.SERVICE_RANKING, new Long(100));
        bind(export(org.apache.felix.shell.Command.class))
            .toProvider(service(new ArchiverCommandImpl())
                .attributes(properties(ccAttrs))
                .export());

    }
    
}
