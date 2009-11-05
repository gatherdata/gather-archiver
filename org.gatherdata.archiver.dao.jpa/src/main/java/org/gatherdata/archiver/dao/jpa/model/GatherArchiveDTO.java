/**
 * The contents of this file are subject to the AED Public Use License Agreement, Version 1.0 (the "License");
 * use in any manner is strictly prohibited except in compliance with the terms of the License.
 * The License is available at http://gatherdata.org/license.
 *
 * Copyright (c) AED.  All Rights Reserved
 */
package org.gatherdata.archiver.dao.jpa.model;

import java.io.Serializable;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.gatherdata.archiver.core.model.GatherArchive;
import org.gatherdata.archiver.core.model.MutableGatherArchive;
import org.gatherdata.commons.model.impl.UniqueEntitySupport;
import org.gatherdata.commons.net.CbidFactory;
import org.joda.time.DateTime;

@Entity
@Table(name = "ARCHIVE")
public class GatherArchiveDTO implements GatherArchive {

    /**
     * 
     */
    private static final long serialVersionUID = -3621945065615956862L;

    protected static final UniqueEntitySupport support = new UniqueEntitySupport();
    
    @Id    
    @Column(name = "UID")
    private String uidAsString;
    
    @Transient
    private URI lazyuid;

    @OneToMany (mappedBy = "archive", cascade = CascadeType.ALL)
    private List<MetadataDTO> metadata;

    @Transient
    private Map<String, String> lazyMetadata;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "DATE_CREATED")
    private Calendar dateCreatedAsCalendar;

    @Transient
    private DateTime lazyDateCreated;

    @Lob
    @Column(name = "CONTENT")
    private Serializable content;

    public GatherArchiveDTO() {
        ;
    }

    public URI getUid() {
        if (lazyuid == null) {
            try {
                this.lazyuid = new URI(uidAsString);
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }
        return lazyuid;
    }

    public void setUid(URI uid) {
        this.uidAsString = uid.toASCIIString();
        this.lazyuid = uid;
    }

    public DateTime getDateCreated() {
        if ((lazyDateCreated == null) && (dateCreatedAsCalendar != null)) {
            lazyDateCreated = new DateTime(dateCreatedAsCalendar.getTimeInMillis());
        }
        return lazyDateCreated;
    }

    public void setDateCreated(DateTime dateCreated) {
        this.lazyDateCreated = dateCreated;
		if (dateCreated != null) {
        	if (dateCreatedAsCalendar == null) {
            	dateCreatedAsCalendar = new GregorianCalendar();
        	}
        	dateCreatedAsCalendar.setTimeInMillis(dateCreated.getMillis());
		} else {
			dateCreatedAsCalendar = null;
		}
    }

    public Map<String, String> getMetadata() {
        if (lazyMetadata == null) {
            lazyMetadata = new HashMap<String, String>();
            if (metadata != null) {
                for (MetadataDTO metaEntry : metadata) {
                    lazyMetadata.put(metaEntry.key, metaEntry.value);
                }
            }
        }
        return lazyMetadata;
    }

    public Serializable getContent() {
        return content;
    }

    public void setContent(Serializable content) {
        this.content = content;
    }

    public GatherArchiveDTO copy(GatherArchive template) {
        if (template != null) {
            setUid(template.getUid());
            setContent(template.getContent());
            DateTime dateCreated = template.getDateCreated();
            if (dateCreated == null) {
                dateCreated = new DateTime();
            }
            setDateCreated(dateCreated);
            lazyMetadata = null;
            Map<String, String> templateMetadata = template.getMetadata();
            if (templateMetadata != null) {
                metadata = new ArrayList<MetadataDTO>();
                for (String templateMetaKey : templateMetadata.keySet()) {
                    MetadataDTO newMeta = new MetadataDTO();
                    newMeta.value  = templateMetadata.get(templateMetaKey);
                    newMeta.key = templateMetaKey;
                    newMeta.archive = this;
                    metadata.add(newMeta);
                }
            }
        }
        return this;
    }

    public GatherArchiveDTO update(GatherArchive template) {
        if (template != null) {
            ; // ABKTODO: implement, or deprecate
        }
        return this;
    }

    public URI selfIdentify() {
        if (dateCreatedAsCalendar == null) {
            dateCreatedAsCalendar = GregorianCalendar.getInstance();
        }
        URI selfId = CbidFactory.createCbid(GatherArchive.class.getSimpleName() + getDateCreated() + Integer.toHexString(hashCode()));
        if (this.getUid() == null) {
            setUid(selfId);
        }
        return selfId;
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

    @Override
    public String toString() {
        return "GatherArchive [uid=" + getUid() + ", dateCreated=" + getDateCreated() +  "]";
    }

    

}
