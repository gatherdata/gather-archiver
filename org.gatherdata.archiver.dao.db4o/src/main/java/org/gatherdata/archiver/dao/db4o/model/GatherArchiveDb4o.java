package org.gatherdata.archiver.dao.db4o.model;

import java.io.Serializable;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import org.gatherdata.archiver.core.model.GatherArchive;
import org.gatherdata.commons.model.UniqueEntitySupport;
import org.joda.time.DateTime;

public class GatherArchiveDb4o implements GatherArchive {

    private static final UniqueEntitySupport support = new UniqueEntitySupport();

    private Serializable content;
    private Map<String, String> metadata;
    private transient DateTime lazyDateCreated;
    private long dateCreatedMillis;
    private URI uid;

    public Serializable getContent() {
        return this.content;
    }
    
    public void setContent(Serializable content) {
        this.content = content;
    }

    public Map<String, String> getMetadata() {
        if (metadata == null) {
            metadata = new HashMap<String, String>();
        }
        return this.metadata;
    }

    public DateTime getDateCreated() {
        if (lazyDateCreated == null) {
            lazyDateCreated = new DateTime(dateCreatedMillis);
        }
        return lazyDateCreated;
    }
    
    

    public long getDateCreatedMillis() {
        return dateCreatedMillis;
    }

    public void setDateCreatedMillis(long dateCreatedMillis) {
        this.dateCreatedMillis = dateCreatedMillis;
    }

    public URI getUid() {
        return this.uid;
    }
    
    public void setUid(URI uid) {
        this.uid = uid;
    }

    public static GatherArchive deriveInstanceFrom(GatherArchive template) {
        GatherArchiveDb4o derivedInstance = new GatherArchiveDb4o();

        derivedInstance.setUid(template.getUid());
        derivedInstance.setContent(template.getContent());
        derivedInstance.setDateCreatedMillis(template.getDateCreated().getMillis());
        
        derivedInstance.getMetadata().putAll(template.getMetadata());
        
        return derivedInstance;
    }
    

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof GatherArchive)) return false;
        GatherArchive rhs = (GatherArchive)obj;
        return support.equals(this, rhs);
    }

    @Override
    public int hashCode() {
        return support.hashCode(this);
    }    

}
