package org.gatherdata.archiver.dao.neo4j.internal;

import java.net.URI;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.gatherdata.archiver.core.model.GatherArchive;
import org.gatherdata.archiver.core.spi.ArchiverDao;
import org.gatherdata.commons.db.neo4j.NeoServices;
import org.gatherdata.commons.model.neo4j.IterableNodeWrapper;
import org.gatherdata.commons.model.neo4j.NodeAdapter;
import org.neo4j.api.core.NeoService;
import org.neo4j.api.core.Node;
import org.neo4j.api.core.NotFoundException;
import org.neo4j.api.core.Relationship;
import org.neo4j.api.core.RelationshipType;
import org.neo4j.api.core.Transaction;
import org.neo4j.util.NeoServiceLifecycle;
import org.neo4j.util.index.IndexService;
import org.neo4j.util.timeline.Timeline;

import com.google.inject.Inject;

public class NeoArchiverDaoImpl implements ArchiverDao {

    public static Logger log = Logger.getLogger(NeoArchiverDaoImpl.class.getName());

    private NodeAdapter<GatherArchive, GatherArchiveNodeWrapper> nodeAdapter = new GatherArchiveNodeAdapter();

    @Inject
    NeoServices neo;

    private Transaction currentTransaction;

    public boolean exists(URI uid) {
        Node foundNode = null;
        if (uid != null) {
            try {
                foundNode = neo.indexService().getSingleNode(GatherArchiveNodeWrapper.UID_PROPERTY, uid.toASCIIString());
            } catch (NotFoundException nfe) {
                ;
            } // not found means it doesn't exist
        }
        return (foundNode != null);
    }

    public GatherArchive get(URI uid) {
        GatherArchive foundArchive = null;
        Node foundNode = neo.indexService().getSingleNode(GatherArchiveNodeWrapper.UID_PROPERTY, uid.toASCIIString());
        if (foundNode != null) {
            foundArchive = nodeAdapter.adaptFromNode(foundNode, neo.neo());
        }
        return foundArchive;
    }

    public Iterable<GatherArchive> getAll() {
        Iterable<Node> allNodes = neo.indexService().getNodes(GatherArchiveNodeWrapper.GATHER_NODETYPE_PROPERTY,
                GatherArchiveNodeWrapper.GATHER_ARCHIVE_NODETYPE);

        return new IterableNodeWrapper(neo.neo(), allNodes, nodeAdapter);
    }

    public void remove(URI uid) {
        Node foundNode = neo.indexService().getSingleNode(GatherArchiveNodeWrapper.UID_PROPERTY, uid.toASCIIString());
        if (foundNode != null) {
            Transaction tx = neo.neo().beginTx();
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
        neo.indexService().removeIndex(nodeToRemove, GatherArchiveNodeWrapper.UID_PROPERTY,
                nodeToRemove.getProperty(GatherArchiveNodeWrapper.UID_PROPERTY));
        neo.indexService().removeIndex(nodeToRemove, GatherArchiveNodeWrapper.GATHER_NODETYPE_PROPERTY,
                GatherArchiveNodeWrapper.GATHER_ARCHIVE_NODETYPE);
    }

    private void removeAllRelationships(Node foundNode) {
        for (Relationship nodeRelationship : foundNode.getRelationships()) {
            Node otherNode = nodeRelationship.getOtherNode(foundNode);
            otherNode.delete();
            nodeRelationship.delete();
        }

    }

    public GatherArchive save(GatherArchive instance) {
        GatherArchiveNodeWrapper savedInstance = null;
        Transaction tx = neo.neo().beginTx();
        try {
            if (!exists(instance.getUid())) {
                savedInstance = nodeAdapter.deriveInstanceFrom(instance, neo.neo());
                Node savedNode = savedInstance.getUnderlyingNode();
                index(savedNode);
            }
            tx.success();
        } finally {
            tx.finish();
        }
        return savedInstance;
    }

    private void index(Node savedNode) {
        // index by uid
        neo.indexService().index(savedNode, GatherArchiveNodeWrapper.UID_PROPERTY,
                savedNode.getProperty(GatherArchiveNodeWrapper.UID_PROPERTY));

        // index by node type
        neo.indexService().index(savedNode, GatherArchiveNodeWrapper.GATHER_NODETYPE_PROPERTY,
                GatherArchiveNodeWrapper.GATHER_ARCHIVE_NODETYPE);
    }

    public void beginTransaction() {
        this.currentTransaction = neo.neo().beginTx();

    }

    public void endTransaction() {
        this.currentTransaction.success();

    }

}
