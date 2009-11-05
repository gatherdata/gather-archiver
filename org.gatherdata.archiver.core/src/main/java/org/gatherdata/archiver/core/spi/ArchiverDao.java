/**
 * The contents of this file are subject to the AED Public Use License Agreement, Version 1.0 (the "License");
 * use in any manner is strictly prohibited except in compliance with the terms of the License.
 * The License is available at http://gatherdata.org/license.
 *
 * Copyright (c) AED.  All Rights Reserved
 */
package org.gatherdata.archiver.core.spi;

import org.gatherdata.archiver.core.model.GatherArchive;
import org.gatherdata.commons.spi.StorageDao;


/**
 * The DAO used by the ArchiverService. All content is persisted 
 * directly as a Serialized object.
 * 
 * Implementations can be loaded from other bundles by simply
 * registering the DAO as an OSGi service. 
 * 
 */
public interface ArchiverDao extends StorageDao<GatherArchive> {


}
