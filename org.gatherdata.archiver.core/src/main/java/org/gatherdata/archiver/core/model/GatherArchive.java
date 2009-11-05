/**
 * The contents of this file are subject to the AED Public Use License Agreement, Version 1.0 (the "License");
 * use in any manner is strictly prohibited except in compliance with the terms of the License.
 * The License is available at http://gatherdata.org/license.
 *
 * Copyright (c) AED.  All Rights Reserved
 */
package org.gatherdata.archiver.core.model;

import java.io.Serializable;
import java.net.URI;
import java.util.Map;

import org.gatherdata.commons.model.UniqueEntity;
import org.joda.time.DateTime;

public interface GatherArchive extends UniqueEntity {
	
	public Map<String, String> getMetadata();
		
	public Serializable getContent();
}
