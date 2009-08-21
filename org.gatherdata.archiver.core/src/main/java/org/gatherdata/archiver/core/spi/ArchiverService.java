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
