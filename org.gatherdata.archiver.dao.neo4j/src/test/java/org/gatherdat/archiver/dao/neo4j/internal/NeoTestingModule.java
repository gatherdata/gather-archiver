package org.gatherdat.archiver.dao.neo4j.internal;

import org.neo4j.api.core.EmbeddedNeo;
import org.neo4j.api.core.NeoService;
import org.neo4j.util.index.IndexService;
import org.neo4j.util.index.NeoIndexService;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Provides;

public class NeoTestingModule extends AbstractModule {
    
    private EmbeddedNeo singletonNeo;

    @Override
    protected void configure() {
        bind(NeoService.class).to(EmbeddedNeo.class);
        bind(IndexService.class).to(NeoIndexService.class);
    }
    
    @Provides
    public EmbeddedNeo createNeoService() {
        if (singletonNeo == null) {
            singletonNeo =  new EmbeddedNeo("target/testingNeo");
        }
        return singletonNeo;
    }
    
    @Provides
    @Inject
    public NeoIndexService createNeoIndexService(NeoService neo) {
        return new NeoIndexService(neo);
    }
}
