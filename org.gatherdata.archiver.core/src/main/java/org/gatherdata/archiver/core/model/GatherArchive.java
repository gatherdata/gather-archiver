package org.gatherdata.archiver.core.model;

import java.io.Serializable;
import java.net.URI;
import java.util.Map;

import org.gatherdata.commons.model.UniqueEntity;
import org.joda.time.DateTime;

public interface GatherArchive extends UniqueEntity {
	
	public Map<String, String> getMetadata();
	
	public DateTime getDateCreated();
	
	public Serializable getContent();
}
