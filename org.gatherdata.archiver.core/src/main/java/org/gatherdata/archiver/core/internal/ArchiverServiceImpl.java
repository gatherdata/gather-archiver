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
	ArchiverDao dao;

	public boolean exists(URI uid) {
	    return dao.exists(uid);
	}

	public GatherArchive get(URI uid) {
	    return dao.get(uid);
	}

	public Iterable<GatherArchive> getAll() {
		return dao.getAll();
	}

	public void remove(URI uid) {
		dao.remove(uid);
	}

	public GatherArchive save(GatherArchive instance) {
		return dao.save(instance);
	}

}
