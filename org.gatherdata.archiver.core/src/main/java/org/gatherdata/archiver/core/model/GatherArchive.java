package org.gatherdata.archiver.core.model;

import java.io.Serializable;
import java.net.URI;
import java.util.Map;

import org.joda.time.DateTime;

public interface GatherArchive extends Serializable {

	public URI getUid();
	
	public Map<String, String> getMetadata();
	
	public DateTime getDateCreated();
	
	public Serializable getContent();
}
