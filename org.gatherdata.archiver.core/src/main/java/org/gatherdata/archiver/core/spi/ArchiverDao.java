package org.gatherdata.archiver.core.spi;

import java.io.Serializable;
import java.net.URI;
import java.util.List;

import org.gatherdata.archiver.core.model.GatherArchive;
import org.gatherdata.commons.spi.dao.StorageDao;


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
