package org.gatherdata.archiver.core.model;

import java.io.Serializable;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import org.joda.time.DateTime;

public class MutableGatherArchive extends AbstractGatherArchive implements GatherArchive {

    /**
     * 
     */
    private static final long serialVersionUID = -2032179948032352681L;
    
    private Serializable content;
    private DateTime dateCreated;
    private Map<String, String> metadata;

    private URI uid;

    public Serializable getContent() {
        return content;
    }
    
    public void setContent(Serializable content) {
        this.content = content;
    }

    public DateTime getDateCreated() {
        return dateCreated;
    }
    
    public void setDateCreated(DateTime dateCreated) {
        this.dateCreated = dateCreated;
    }

    public URI getUid() {
        return uid;
    }
    
    public void setUid(URI uid) {
        this.uid = uid;
    }

    public Map<String, String> getMetadata() {
        if (metadata == null) {
            metadata = new HashMap<String, String>();
        }
        return metadata;
    }    
    
}
