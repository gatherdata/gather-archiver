package org.gatherdata.archiver.dao.jpa.model;

import java.io.Serializable;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.gatherdata.archiver.core.model.AbstractGatherArchive;
import org.gatherdata.archiver.core.model.GatherArchive;
import org.gatherdata.archiver.core.model.MutableGatherArchive;
import org.joda.time.DateTime;

@Entity
@Table(name = "ARCHIVE")
public class GatherArchiveDTO extends AbstractGatherArchive implements GatherArchive {

    /**
     * 
     */
    private static final long serialVersionUID = -3621945065615956862L;

    public GatherArchiveDTO() {
        ;
    }

    @Id
    @Column(name = "UID")
    private String uidAsString;

    private DateTime dateCreated;

    private Serializable content;

    @OneToMany
    private Map<String, MetadataDTO> metadata;

    @Transient
    private Map<String, String> lazyMetadata;

    private void setDateCreated(DateTime dateCreated) {
        this.dateCreated = dateCreated;
    }

    private void setContent(Serializable content) {
        this.content = content;
    }

    public Serializable getContent() {
        return content;
    }

    public DateTime getDateCreated() {
        return dateCreated;
    }

    public Map<String, String> getMetadata() {
        if (lazyMetadata == null) {
            lazyMetadata = new HashMap<String, String>();
            if (metadata != null) {
                for (MetadataDTO metaEntry : metadata.values()) {
                    lazyMetadata.put(metaEntry.key, metaEntry.value);
                }
            }
        }
        return lazyMetadata;
    }

    public URI getUid() {
        if (uid == null) {
            try {
                this.uid = new URI(uidAsString);
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }
        return uid;
    }

    private void setUid(URI uid) {
        this.uidAsString = uid.toASCIIString();
        this.uid = uid;
    }
    
    public static GatherArchiveDTO deriveInstanceFrom(GatherArchive template) {
        GatherArchiveDTO dto = new GatherArchiveDTO();
        dto.setUid(template.getUid());
        dto.setContent(template.getContent());
        DateTime dateCreated = template.getDateCreated();
        if (dateCreated == null) {
            dateCreated = new DateTime();
        }
        dto.setDateCreated(dateCreated);
        dto.getMetadata().putAll(template.getMetadata());
        return dto;
    }


}
