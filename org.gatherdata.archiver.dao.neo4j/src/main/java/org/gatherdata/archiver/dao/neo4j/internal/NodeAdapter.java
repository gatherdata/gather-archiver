package org.gatherdata.archiver.dao.neo4j.internal;

import org.neo4j.api.core.Node;

public interface NodeAdapter<T> {

    public T adaptFromNode(Node n);
    
}
