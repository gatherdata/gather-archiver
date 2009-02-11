package org.gatherdata.archiver.core.spi;

import java.io.Serializable;

import org.gatherdata.core.spi.EnvelopeStorage;

/**
 * An EnvelopeStorage which handles generic Serializable contents. 
 *
 */
public interface EnvelopeArchiver extends EnvelopeStorage<Serializable> {

}
