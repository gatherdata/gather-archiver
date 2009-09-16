package org.gatherdata.archiver.core.internal;

import java.net.URI;
import java.util.List;
import java.util.logging.Logger;

import org.gatherdata.archiver.core.model.GatherArchive;
import org.gatherdata.archiver.core.spi.ArchiverDao;
import org.gatherdata.archiver.core.spi.ArchiverService;

import com.google.inject.Inject;

public class ArchiverServiceImpl implements ArchiverService {

	Logger log = Logger.getLogger(ArchiverServiceImpl.class.getName());
	
	@Inject
    public
	ArchiverDao dao;

	public boolean exists(URI uid) {
	    return dao.exists(uid);
	}

	public GatherArchive get(URI uid) {
	    GatherArchive foundEntity = null;
	    try {
    	    dao.beginTransaction();
    	    foundEntity = dao.get(uid);
	    } finally {
	        dao.endTransaction();
	    }
	    return foundEntity;
	    
	}

	public Iterable<GatherArchive> getAll() {
	    dao.beginTransaction();
	    Iterable<GatherArchive> allArchives = (Iterable<GatherArchive>) dao.getAll();
	    dao.endTransaction();
		return allArchives;
	}

	public void remove(URI uid) {
		dao.remove(uid);
	}

	public GatherArchive save(GatherArchive instance) {
	    GatherArchive savedInstance = dao.save(instance);
		return savedInstance;
	}

}
