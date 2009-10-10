package org.gatherdata.archiver.dao.jpa.model;

import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table (name="METADATA")
public class MetadataDTO {

    @Id 
    @GeneratedValue(strategy=GenerationType.AUTO)
    int dbid;
        
    @ManyToOne(optional = false)
    GatherArchiveDTO archive;
    
    String key;
    
    String value;
}
