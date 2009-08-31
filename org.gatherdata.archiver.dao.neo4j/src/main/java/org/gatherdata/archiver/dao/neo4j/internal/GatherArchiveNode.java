package org.gatherdata.archiver.dao.neo4j.internal;

import java.io.Serializable;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.gatherdata.archiver.core.model.AbstractGatherArchive;
import org.gatherdata.archiver.core.model.GatherArchive;
import org.gatherdata.archiver.core.model.MutableGatherArchive;
import org.joda.time.DateTime;
import org.neo4j.api.core.Direction;
import org.neo4j.api.core.NeoService;
import org.neo4j.api.core.Node;
import org.neo4j.api.core.NotFoundException;
import org.neo4j.api.core.Relationship;
import org.neo4j.api.core.RelationshipType;

public class GatherArchiveNode extends AbstractGatherArchive implements GatherArchive {

    private static Logger log = Logger.getLogger(GatherArchiveNode.class.getName());
    
    public enum ArchiveRelationships implements RelationshipType {
        METADATA_ABOUT
    }

    /**
     * 
     */
    private static final long serialVersionUID = 3942044815055659432L;

    public static final String GATHER_NODETYPE_PROPERTY = "GatherNodeType";

    public static final String CONTENT_PROPERTY = "content";

    public static final String UID_PROPERTY = "uid";

    public static final String DATE_CREATED_PROPERTY = "dateCreated";

    public static final String GATHER_ARCHIVE_NODETYPE = "GatherArchive";

    public static final String ARCHIVE_METADATA_NODETYPE = "ArchiveMetadata";

    private final Node underlyingNode;

    private final Node metadataNode;

    // lazily created copies of some properties
    private URI lazyUid;
    
    private DateTime lazyDateCreated;

    private Map<String, String> lazyMetadata;

    GatherArchiveNode(Node underylingNode, Node metadataNode) {
        this.underlyingNode = underylingNode;
        this.metadataNode = metadataNode;
    }

    Node getUnderlyingNode() {
        return this.underlyingNode;
    }

    public Serializable getContent() {
        return (Serializable) underlyingNode.getProperty(CONTENT_PROPERTY);
    }

    private void setContent(Serializable content) {
        underlyingNode.setProperty(CONTENT_PROPERTY, content);
    }

    public DateTime getDateCreated() {
        if (lazyDateCreated == null) {
            lazyDateCreated = new DateTime((Long) underlyingNode.getProperty(DATE_CREATED_PROPERTY));
        }
        return lazyDateCreated;
    }

    private void setDateCreated(DateTime dateCreated) {
        underlyingNode.setProperty(DATE_CREATED_PROPERTY, dateCreated.getMillis());
    }

    public Map<String, String> getMetadata() {
        if (lazyMetadata == null) {
            Map<String, String> backingMap = new HashMap<String, String>();
            if (metadataNode != null) {
                for (String key : metadataNode.getPropertyKeys()) {
                    backingMap.put(key, (String) metadataNode.getProperty(key));
                }
            }
            lazyMetadata = backingMap; // ABKTODO: immutable?
                                       // Collections.unmodifiableMap(backingMap);
        }
        return lazyMetadata;
    }

    public void setMetadata(Map<String, String> metadata) {
        log.fine("setMetadata("+ metadata + ")");
        for (String key : metadata.keySet()) {
            metadataNode.setProperty(key, metadata.get(key));
        }
    }

    public URI getUid() {
        if (lazyUid == null) {
            try {
                String uidAsString = (String) underlyingNode.getProperty(UID_PROPERTY);
                lazyUid = new URI(uidAsString);
            } catch (URISyntaxException e) {
                e.printStackTrace();
            } catch (NotFoundException nfe) {
                lazyUid = null;
            }
        }
        return lazyUid;
    }

    private void setUid(URI uid) {
        underlyingNode.setProperty(UID_PROPERTY, uid.toASCIIString());
    }

    public static GatherArchiveNode deriveInstanceFrom(GatherArchive template, NeoService inGraph) {
        Node archiveNode = inGraph.createNode();
        archiveNode.setProperty(GATHER_NODETYPE_PROPERTY, GATHER_ARCHIVE_NODETYPE);
        Node archiveMetadata = inGraph.createNode();
        archiveMetadata.setProperty(GATHER_NODETYPE_PROPERTY, ARCHIVE_METADATA_NODETYPE);

        Relationship relationship = archiveMetadata.createRelationshipTo(archiveNode,
                ArchiveRelationships.METADATA_ABOUT);

        GatherArchiveNode derivedInstance = new GatherArchiveNode(archiveNode, archiveMetadata);

        derivedInstance.setUid(template.getUid());
        derivedInstance.setContent(template.getContent());
        derivedInstance.setDateCreated(template.getDateCreated());
        derivedInstance.setMetadata(template.getMetadata());
        return derivedInstance;
    }

}
