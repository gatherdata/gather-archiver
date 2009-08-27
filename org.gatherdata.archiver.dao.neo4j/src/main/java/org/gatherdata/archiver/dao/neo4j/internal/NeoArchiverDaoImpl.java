package org.gatherdata.archiver.dao.neo4j.internal;

import java.net.URI;
import java.util.List;
import java.util.logging.Logger;

import org.gatherdata.archiver.core.model.GatherArchive;
import org.gatherdata.archiver.core.spi.ArchiverDao;
import org.neo4j.api.core.NeoService;
import org.neo4j.api.core.Node;
import org.neo4j.api.core.NotFoundException;
import org.neo4j.api.core.Relationship;
import org.neo4j.api.core.RelationshipType;
import org.neo4j.api.core.Transaction;
import org.neo4j.util.index.IndexService;
import org.neo4j.util.timeline.Timeline;

import com.google.inject.Inject;

public class NeoArchiverDaoImpl implements ArchiverDao {

    public static Logger log = Logger.getLogger(NeoArchiverDaoImpl.class.getName());

    private NodeAdapter<GatherArchive> nodeAdapter = new GatherArchiveNodeAdapter();

    @Inject
    NeoService neo;

    @Inject
    IndexService neoIndex;

    public boolean exists(URI uid) {
        Node foundNode = null;
        try {
            foundNode = neoIndex.getSingleNode(GatherArchiveNode.UID_PROPERTY, uid.toASCIIString());
        } catch (NotFoundException nfe) {;} // not found means it doesn't exist
        return (foundNode != null);
    }

    public GatherArchive get(URI uid) {
        GatherArchive foundArchive = null;
        Node foundNode = neoIndex.getSingleNode(GatherArchiveNode.UID_PROPERTY, uid.toASCIIString());
        if (foundNode != null) {
            foundArchive = nodeAdapter.adaptFromNode(foundNode);
        }
        return foundArchive;
    }

    public Iterable<GatherArchive> getAll() {
        Iterable<Node> allNodes = neoIndex.getNodes(GatherArchiveNode.GATHER_NODETYPE_PROPERTY,
                GatherArchiveNode.GATHER_ARCHIVE_NODETYPE);

        return new IterableNodeWrapper(allNodes, nodeAdapter);
    }

    public void remove(URI uid) {
        Node foundNode = neoIndex.getSingleNode(GatherArchiveNode.UID_PROPERTY, uid.toASCIIString());
        if (foundNode != null) {
            Transaction tx = neo.beginTx();
            try {
                removeAllRelationships(foundNode);
                removeAllIndexes(foundNode);
                foundNode.delete();
                tx.success();
            } finally {
                tx.finish();
            }

        }

    }

    private void removeAllIndexes(Node nodeToRemove) {
        neoIndex
            .removeIndex(nodeToRemove, GatherArchiveNode.UID_PROPERTY, nodeToRemove.getProperty(GatherArchiveNode.UID_PROPERTY));
        neoIndex
            .removeIndex(nodeToRemove, GatherArchiveNode.GATHER_NODETYPE_PROPERTY, GatherArchiveNode.GATHER_ARCHIVE_NODETYPE);
    }

    private void removeAllRelationships(Node foundNode) {
        for (Relationship nodeRelationship : foundNode.getRelationships()) {
            Node otherNode = nodeRelationship.getOtherNode(foundNode);
            otherNode.delete();
            nodeRelationship.delete();
        }
        
    }

    public GatherArchive save(GatherArchive instance) {
        GatherArchiveNode savedInstance = null;
        if (!exists(instance.getUid())) {
            Transaction tx = neo.beginTx();
            try {
                savedInstance = GatherArchiveNode.deriveInstanceFrom(instance, neo);
                Node savedNode = savedInstance.getUnderlyingNode();
                index(savedNode);
                tx.success();
            } finally {
                tx.finish();
            }
        }
        return savedInstance;
    }

    private void index(Node savedNode) {
        // index by uid
        neoIndex
                .index(savedNode, GatherArchiveNode.UID_PROPERTY, savedNode.getProperty(GatherArchiveNode.UID_PROPERTY));

        // index by node type
        neoIndex
                .index(savedNode, GatherArchiveNode.GATHER_NODETYPE_PROPERTY, GatherArchiveNode.GATHER_ARCHIVE_NODETYPE);
    }

}
