package org.gatherdata.archiver.dao.neo4j.internal;

import org.gatherdata.archiver.core.model.GatherArchive;
import org.gatherdata.archiver.dao.neo4j.internal.GatherArchiveNode.ArchiveRelationships;
import org.neo4j.api.core.Direction;
import org.neo4j.api.core.Node;
import org.neo4j.api.core.Relationship;

public class GatherArchiveNodeAdapter implements NodeAdapter<GatherArchive> {

    public GatherArchive adaptFromNode(Node nodeToAdapt) {
        GatherArchiveNode derivedInstance = null;

        if (GatherArchiveNode.GATHER_ARCHIVE_NODETYPE.equals(nodeToAdapt.getProperty(GatherArchiveNode.GATHER_NODETYPE_PROPERTY))) {
            Node metadataNode = null;
            Relationship relateFromMeta = nodeToAdapt.getSingleRelationship(ArchiveRelationships.METADATA_ABOUT,
                    Direction.INCOMING);
            if (relateFromMeta != null) {
                metadataNode = relateFromMeta.getStartNode();
            }
            derivedInstance = new GatherArchiveNode(nodeToAdapt, metadataNode);
            derivedInstance.getUid(); 
        }

        return derivedInstance;
    }

}
