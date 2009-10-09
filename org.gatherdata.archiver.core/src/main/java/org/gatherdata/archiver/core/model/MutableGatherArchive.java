package org.gatherdata.archiver.core.model;

import java.io.Serializable;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import org.gatherdata.commons.model.DescribedEntity;
import org.gatherdata.commons.model.impl.DescribedEntitySupport;
import org.gatherdata.commons.model.impl.MutableDescribedEntity;
import org.gatherdata.commons.model.impl.UniqueEntitySupport;
import org.joda.time.DateTime;

public class MutableGatherArchive extends MutableDescribedEntity implements GatherArchive {

    /**
     * 
     */
    private static final long serialVersionUID = -2032179948032352681L;
    
    private static final DescribedEntitySupport support = new DescribedEntitySupport();
    
    private Serializable content;
    private Map<String, String> metadata;

    public Serializable getContent() {
        return content;
    }
    
    public void setContent(Serializable content) {
        this.content = content;
    }

    public Map<String, String> getMetadata() {
        if (metadata == null) {
            metadata = new HashMap<String, String>();
        }
        return metadata;
    }

    public GatherArchive copy(GatherArchive template) {
        if (template != null) {
            super.copy(template);
            setContent(template.getContent());
            metadata.clear();
            metadata.putAll(template.getMetadata());
        }
        return this;
    }

    public GatherArchive update(GatherArchive template) {
        if (template != null) {
            super.update(template);
            Serializable updatedContent = template.getContent();
            if (updatedContent != null) setContent(updatedContent);
            Map<String, String> updatedMetadata = template.getMetadata();
            if (updatedMetadata != null) {
                metadata.putAll(updatedMetadata);
            }       
        }
        return this;
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
