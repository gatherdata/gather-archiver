/**
 * The contents of this file are subject to the AED Public Use License Agreement, Version 1.0 (the "License");
 * use in any manner is strictly prohibited except in compliance with the terms of the License.
 * The License is available at http://gatherdata.org/license.
 *
 * Copyright (c) AED.  All Rights Reserved
 */
package org.gatherdata.archiver.dao.neo4j.internal;

import org.gatherdata.archiver.core.model.GatherArchive;
import org.gatherdata.archiver.dao.neo4j.internal.GatherArchiveNodeWrapper.ArchiveRelationships;
import org.gatherdata.commons.model.neo4j.NodeAdapter;
import org.neo4j.api.core.Direction;
import org.neo4j.api.core.NeoService;
import org.neo4j.api.core.Node;
import org.neo4j.api.core.Relationship;

public class GatherArchiveNodeAdapter implements NodeAdapter<GatherArchive, GatherArchiveNodeWrapper> {

    public GatherArchive adaptFromNode(Node nodeToAdapt, NeoService neo) {
        GatherArchiveNodeWrapper derivedInstance = null;

        if (GatherArchiveNodeWrapper.GATHER_ARCHIVE_NODETYPE.equals(nodeToAdapt.getProperty(GatherArchiveNodeWrapper.GATHER_NODETYPE_PROPERTY))) {
            Node metadataNode = null;
            Relationship relateFromMeta = nodeToAdapt.getSingleRelationship(ArchiveRelationships.METADATA_ABOUT,
                    Direction.INCOMING);
            if (relateFromMeta != null) {
                metadataNode = relateFromMeta.getStartNode();
            }
            derivedInstance = new GatherArchiveNodeWrapper(nodeToAdapt, metadataNode);
            derivedInstance.getUid(); 
        }

        return derivedInstance;
    }

    public GatherArchiveNodeWrapper deriveInstanceFrom(GatherArchive template, NeoService inGraph) {
        Node archiveNode = inGraph.createNode();
        archiveNode.setProperty(GatherArchiveNodeWrapper.GATHER_NODETYPE_PROPERTY, GatherArchiveNodeWrapper.GATHER_ARCHIVE_NODETYPE);
        Node archiveMetadata = inGraph.createNode();
        archiveMetadata.setProperty(GatherArchiveNodeWrapper.GATHER_NODETYPE_PROPERTY, GatherArchiveNodeWrapper.ARCHIVE_METADATA_NODETYPE);

        Relationship relationship = archiveMetadata.createRelationshipTo(archiveNode,
                ArchiveRelationships.METADATA_ABOUT);

        GatherArchiveNodeWrapper derivedInstance = new GatherArchiveNodeWrapper(archiveNode, archiveMetadata);

        derivedInstance.setUid(template.getUid());
        derivedInstance.setContent(template.getContent());
        derivedInstance.setDateCreated(template.getDateCreated());
        derivedInstance.setMetadata(template.getMetadata());
        return derivedInstance;
    }

}
