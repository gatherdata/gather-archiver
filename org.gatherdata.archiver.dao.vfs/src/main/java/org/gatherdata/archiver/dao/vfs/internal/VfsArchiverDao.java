package org.gatherdata.archiver.dao.vfs.internal;

import java.io.Serializable;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.NotImplementedException;
import org.apache.commons.lang.SerializationUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.FileSystemException;
import org.apache.commons.vfs.FileSystemManager;
import org.apache.commons.vfs.impl.StandardFileSystemManager;
import org.gatherdata.archiver.core.model.GatherArchive;
import org.gatherdata.archiver.core.spi.ArchiverDao;

/**
 * An ArchiverDao which uses Apache Commons VFS to store
 * serialized objects on a file system.
 * 
 */
public class VfsArchiverDao implements ArchiverDao {
	private FileSystemManager fsManager;
	private FileObject fsBase;
	
	public VfsArchiverDao() {
		fsManager = new StandardFileSystemManager();
		try {
			fsBase = fsManager.getBaseFile();
		} catch (FileSystemException e) {
			e.printStackTrace();
		}
	}
	
	public boolean exists(URI uid) {
		boolean fileExists = false;
		try {
			FileObject envelopeFile = fsManager.resolveFile(fsBase, uid.toASCIIString());
			fileExists = envelopeFile.exists();
		} catch (FileSystemException e) {
			e.printStackTrace();
		}
		return fileExists;
	}

	public GatherArchive retrieveEnvelope(URI uid) {
	    GatherArchive foundEnvelope = null;
		try {
			FileObject envelopeFile = fsManager.resolveFile(fsBase, uid.toASCIIString());
			if(envelopeFile.exists()) {
				foundEnvelope = (GatherArchive) SerializationUtils.deserialize(envelopeFile.getContent().getInputStream());
			}
		} catch (FileSystemException e) {
			e.printStackTrace();
		}
		return foundEnvelope;
	}

	public List<GatherArchive> getAll() {
	    List<GatherArchive> allEnvelopes = new ArrayList<GatherArchive>();
		try {
			for (FileObject envelopeFile : fsBase.getChildren()) {
				allEnvelopes.add((GatherArchive) SerializationUtils.deserialize(envelopeFile.getContent().getInputStream()));
			}
		} catch (FileSystemException e) {
			e.printStackTrace();
		}
		return allEnvelopes;
	}
	
	public int getCount() {
	    try {
            return fsBase.getChildren().length;
        } catch (FileSystemException e) {
            e.printStackTrace();
        }
        return -1;
	}

	public void remove(URI uid) {
		try {
			FileObject envelopeFile = fsManager.resolveFile(fsBase, uid.toASCIIString());
			if(envelopeFile.exists()) {
				envelopeFile.delete();
			}
		} catch (FileSystemException e) {
			e.printStackTrace();
		}
	}

	public GatherArchive save(GatherArchive envelopeToSave) {
		try {
			FileObject envelopeFile = fsManager.resolveFile(fsBase, envelopeToSave.getUid().toASCIIString());
			if(envelopeFile.exists()) {
				envelopeFile.delete();
			}
			envelopeFile.createFile();
			SerializationUtils.serialize(envelopeToSave, envelopeFile.getContent().getOutputStream());
		} catch (FileSystemException e) {
			e.printStackTrace();
		}

		return envelopeToSave;
	}

    public GatherArchive get(URI arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    public void beginTransaction() {
        // ABKTODO: no-op? file lock of some kind?
        
    }

    public void endTransaction() {
        // ABKTODO: no-op? file lock of some kind?
        
    }




}
