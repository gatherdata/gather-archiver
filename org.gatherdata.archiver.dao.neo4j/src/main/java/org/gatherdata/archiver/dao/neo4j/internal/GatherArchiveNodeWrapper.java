package org.gatherdata.archiver.dao.neo4j.internal;

import java.io.Serializable;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.gatherdata.archiver.core.model.GatherArchive;
import org.gatherdata.archiver.core.model.MutableGatherArchive;
import org.gatherdata.commons.model.neo4j.GatherNodeWrapper;
import org.gatherdata.commons.net.CbidFactory;
import org.joda.time.DateTime;
import org.neo4j.api.core.Direction;
import org.neo4j.api.core.NeoService;
import org.neo4j.api.core.Node;
import org.neo4j.api.core.NotFoundException;
import org.neo4j.api.core.Relationship;
import org.neo4j.api.core.RelationshipType;

public class GatherArchiveNodeWrapper implements GatherArchive, GatherNodeWrapper {

    private static Logger log = Logger.getLogger(GatherArchiveNodeWrapper.class.getName());
    
    public enum ArchiveRelationships implements RelationshipType {
        METADATA_ABOUT
    }

    /**
     * 
     */
    private static final long serialVersionUID = 3942044815055659432L;

    public static final String CONTENT_PROPERTY = "content";

    public static final String GATHER_ARCHIVE_NODETYPE = "GatherArchive";

    public static final String ARCHIVE_METADATA_NODETYPE = "ArchiveMetadata";

    private final Node underlyingNode;

    private final Node metadataNode;

    // lazily created copies of some properties
    private URI lazyUid;
    
    private DateTime lazyDateCreated;

    private Map<String, String> lazyMetadata;

    GatherArchiveNodeWrapper(Node underylingNode, Node metadataNode) {
        this.underlyingNode = underylingNode;
        this.metadataNode = metadataNode;
    }

    public Node getUnderlyingNode() {
        return this.underlyingNode;
    }

    public Serializable getContent() {
        return (Serializable) underlyingNode.getProperty(CONTENT_PROPERTY);
    }

    protected void setContent(Serializable content) {
        underlyingNode.setProperty(CONTENT_PROPERTY, content);
    }

    public DateTime getDateCreated() {
        if (lazyDateCreated == null) {
            lazyDateCreated = new DateTime((Long) underlyingNode.getProperty(DATE_CREATED_PROPERTY));
        }
        return lazyDateCreated;
    }

    public void setDateCreated(DateTime dateCreated) {
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

    public void setUid(URI uid) {
        underlyingNode.setProperty(UID_PROPERTY, uid.toASCIIString());
    }

    public URI selfIdentify() {
        if (getDateCreated() == null) {
            setDateCreated(new DateTime());
        }
        URI selfId = CbidFactory.createCbid(getDateCreated() + Integer.toHexString(hashCode()));
        if (this.getUid() == null) {
            setUid(selfId);
        }
        return selfId;
    }

}
