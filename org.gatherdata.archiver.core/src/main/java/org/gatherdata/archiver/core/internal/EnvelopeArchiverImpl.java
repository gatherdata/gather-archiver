package org.gatherdata.archiver.core.internal;

import java.io.Serializable;
import java.net.URI;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.gatherdata.core.model.Envelope;
import org.gatherdata.core.io.QualifiedType;
import org.gatherdata.archiver.core.spi.ArchiverDao;
import org.gatherdata.archiver.core.spi.EnvelopeArchiver;

import com.google.inject.Inject;

public class EnvelopeArchiverImpl implements EnvelopeArchiver {

	Log log = LogFactory.getLog(EnvelopeArchiverImpl.class);
	
	ArchiverDao dao;

    private final QualifiedType[] qualifiedTypes;

    @Inject
	public EnvelopeArchiverImpl(ArchiverDao dao) {
	    this.dao = dao;
	    
	    this.qualifiedTypes = new QualifiedType[] {
	            QualifiedType.ANY_TYPE
	    };
	}

    public List<? extends Envelope> retrieveAllEnvelopes() {
        return dao.getAllEnvelopes();
    }
    
	/* (non-Javadoc)
	 * @see org.gatherdata.core.spi.EnvelopeStorage#retrieveEnvelope(java.net.URI)
	 */
	public Envelope retrieveEnvelope(URI uriOfEnvelope) {
		if (log.isDebugEnabled()) {
			log.debug("get(" + uriOfEnvelope + ")");
		}
		
		Envelope foundEnvelope = null;
	    foundEnvelope = dao.getEnvelope(uriOfEnvelope);
	    
		return foundEnvelope;
	}

	/* (non-Javadoc)
	 * @see org.gatherdata.core.spi.EnvelopeStorage#save(org.gatherdata.core.model.Envelope)
	 */
	public Envelope save(Envelope envelopeToSave) {
		if (log.isDebugEnabled()) {
			log.debug("save(" + envelopeToSave + ")");
		}
		
		Envelope savedEnvelope = dao.save(envelopeToSave);
		
		if (savedEnvelope == null) {
		    log.warn("Unable to save envelope {" + envelopeToSave + "}.");
		}
		return savedEnvelope;
	}

    /* (non-Javadoc)
     * @see org.gatherdata.core.spi.EnvelopeStorage#getTotalNumberOfStoredEnvelopes()
     */
    public long getTotalNumberOfStoredEnvelopes() {
        return dao.getNumberOfEnvelopes();
    }

    public List<? extends Serializable> getAllContents() {
        return dao.getAllContents();
    }

    public Serializable getContent(URI uidOfContainingEnvelope) {
        return dao.getContent(uidOfContainingEnvelope);
    }

    public QualifiedType[] getAcceptableTypes() {
        return qualifiedTypes;
    }

}
