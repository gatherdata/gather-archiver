/**
 * The contents of this file are subject to the AED Public Use License Agreement, Version 1.0 (the "License");
 * use in any manner is strictly prohibited except in compliance with the terms of the License.
 * The License is available at http://gatherdata.org/license.
 *
 * Copyright (c) AED.  All Rights Reserved
 */
package org.gatherdata.archiver.core.spi;

import java.io.Serializable;

import org.gatherdata.archiver.core.model.GatherArchive;
import org.gatherdata.commons.spi.StorageService;

/**
 * A service for storing GatherArchives. 
 *
 */
public interface ArchiverService extends StorageService<GatherArchive> {

}
