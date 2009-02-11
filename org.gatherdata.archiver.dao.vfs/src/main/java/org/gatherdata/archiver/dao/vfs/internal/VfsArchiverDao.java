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
import org.gatherdata.core.model.Envelope;
import org.gatherdata.core.spi.dao.ContentStorageDao;
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

	public Envelope retrieveEnvelope(URI uid) {
		Envelope foundEnvelope = null;
		try {
			FileObject envelopeFile = fsManager.resolveFile(fsBase, uid.toASCIIString());
			if(envelopeFile.exists()) {
				foundEnvelope = (Envelope) SerializationUtils.deserialize(envelopeFile.getContent().getInputStream());
			}
		} catch (FileSystemException e) {
			e.printStackTrace();
		}
		return foundEnvelope;
	}

	public List<? extends Envelope> getAll() {
	    List<Envelope> allEnvelopes = new ArrayList<Envelope>();
		try {
			for (FileObject envelopeFile : fsBase.getChildren()) {
				allEnvelopes.add((Envelope) SerializationUtils.deserialize(envelopeFile.getContent().getInputStream()));
			}
		} catch (FileSystemException e) {
			e.printStackTrace();
		}
		return allEnvelopes;
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

	public Envelope save(Envelope envelopeToSave) {
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

    public int getNumberOfStoredEnvelopes() {
        return 0;
    }

    public Envelope getProxiedEnvelope(URI identifiedByUid,
            ContentStorageDao<? extends Serializable> usingAlternateContentDoa) {
        // TODO Auto-generated method stub
        return null;
    }

    public Envelope saveEmpty(Envelope envelopeToSave) {
        // TODO Auto-generated method stub
        return null;
    }

    public Envelope saveProxy(Envelope envelopeToSave,
            ContentStorageDao<? extends Serializable> usingAlternateContentDoa) {
        // TODO Auto-generated method stub
        return null;
    }

    public boolean envelopeExists(URI identifiedByUid) {
        // TODO Auto-generated method stub
        return false;
    }

    public List<? extends Envelope> getAllEnvelopes() {
        // TODO Auto-generated method stub
        return null;
    }

    public Envelope getEnvelope(URI identifiedByUid) {
        // TODO Auto-generated method stub
        return null;
    }

    public long getNumberOfEnvelopes() {
        // TODO Auto-generated method stub
        return 0;
    }

    public void removeEnvelope(URI identifiedByUid) {
        // TODO Auto-generated method stub
        
    }

    public Serializable adapt(Serializable contentToAdapt) {
        // TODO Auto-generated method stub
        return null;
    }

    public List<? extends Serializable> getAllContents() {
        // TODO Auto-generated method stub
        return null;
    }

    public Serializable getContent(URI uidOfEnvelope) {
        // TODO Auto-generated method stub
        return null;
    }

    public void removeContent(URI uidOfEnvelope) {
        // TODO Auto-generated method stub
        
    }

    public Serializable save(Serializable contentToSave) {
        // TODO Auto-generated method stub
        return null;
    }

    public Serializable saveAdapted(Serializable proxiedContentToSave) {
        // TODO Auto-generated method stub
        return null;
    }

    public long getNumberOfQualifiedEnvelopes(String withQualifier) {
        // TODO Auto-generated method stub
        return 0;
    }

    public List<Envelope> getAllProxiedEnvelopes(
            ContentStorageDao<? extends Serializable> usingAlternateContentDao) {
        // ABKTODO: implement this
        throw new NotImplementedException();
    }

    public List<URI> getAllContentUids() {
        // TODO Auto-generated method stub
        return null;
    }

    public boolean contentExists(URI uidOfEnvelope) {
        // TODO Auto-generated method stub
        return false;
    }

}
