package org.gatherdata.archiver.dao.jpa.model;

import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table (name="METADATA")
public class MetadataDTO {

    @Id 
    @GeneratedValue(strategy=GenerationType.AUTO)
    long dbid;
    
    String key;
    
    String value;
}
