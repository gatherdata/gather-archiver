package org.gatherdata.archiver.dao.el.internal;

import java.io.Serializable;
import java.net.URI;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Vector;

import org.eclipse.persistence.exceptions.DatabaseException;
import org.eclipse.persistence.expressions.Expression;
import org.eclipse.persistence.expressions.ExpressionBuilder;
import org.eclipse.persistence.queries.Call;
import org.eclipse.persistence.queries.DirectReadQuery;
import org.eclipse.persistence.queries.ReportQuery;
import org.eclipse.persistence.sessions.DatabaseSession;
import org.eclipse.persistence.sessions.Record;
import org.eclipse.persistence.sessions.Session;
import org.gatherdata.archiver.core.model.GatherArchive;
import org.gatherdata.archiver.core.spi.ArchiverDao;
import org.apache.commons.lang.NotImplementedException;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

public class ElArchiverDaoImpl implements ArchiverDao {

    private Session elSession;

    public void setSession(Session sessionToUse) {
        this.elSession = sessionToUse;
    }


    private DatabaseSession createDbSession() {
        return elSession.getProject().createDatabaseSession();
    }


    /* (non-Javadoc)
     * @see org.gatherdata.core.spi.dao.EnvelopeStorageDao#envelopeExists(java.net.URI)
     */
//    public boolean envelopeExists(URI envelopeUid) {
//        // ABKTODO: prebuild the query, substituting in the selection criteria 
//        DatabaseSession dbSession = createDbSession();
//        dbSession.login();
//        ExpressionBuilder expBuilder = new ExpressionBuilder();
//        ReportQuery query = new ReportQuery(EnvelopeDTO.class, expBuilder);
//        query.setSelectionCriteria(expBuilder.get("uidAsString").equal(envelopeUid.toASCIIString()));
//        query.addCount();
//        query.setShouldReturnSingleValue(true);
//        Integer numberOfEnvelopeDTOs = (Integer) dbSession.executeQuery(query);
//        return (numberOfEnvelopeDTOs != 0);
//    }


    /* (non-Javadoc)
     * @see org.gatherdata.core.spi.dao.EnvelopeStorageDao#getAllEnvelopes()
     */
//    public List<? extends Envelope> getAllEnvelopes() {
//        DatabaseSession dbSession = createDbSession();
//        
//        dbSession.login();
//        
//        Vector<? extends Envelope> retrievedEnvelopes = (Vector<EnvelopeDTO>) dbSession.readAllObjects(EnvelopeDTO.class);
//        
//        dbSession.logout();
//        
//        return retrievedEnvelopes;
//    }


//    public Envelope getEnvelope(URI identifiedByUid) {
//        return getEnvelopeDTO(identifiedByUid);
//    }


//    public Envelope save(Envelope envelopeToSave) {
//        EnvelopeDTO envDTO = new EnvelopeDTO(envelopeToSave);
//        DatabaseSession dbSession = createDbSession();
//        dbSession.login();
//        EnvelopeDTO savedEnvelope = (EnvelopeDTO)dbSession.writeObject(envDTO);
//        dbSession.logout();
//        return savedEnvelope;
//    }


//    public long getNumberOfEnvelopes() {
//        DatabaseSession dbSession = createDbSession();
//        dbSession.login();
//        ExpressionBuilder expBuilder = new ExpressionBuilder();
//        ReportQuery query = new ReportQuery(EnvelopeDTO.class, expBuilder);
//        query.addCount();
//        query.setShouldReturnSingleValue(true);
//        Integer numberOfEnvelopeDTOs = (Integer) dbSession.executeQuery(query);
//        return numberOfEnvelopeDTOs;
//    }


//    public void removeEnvelope(URI identifiedByUid) {
//        // ABKTODO: implement this
//        throw new NotImplementedException();
//    }


//    public List<Serializable> getAllContents() {
//        // ABKTODO: figure out how to return a single column of data using DirectReadQuery
//        DirectReadQuery directReadQuery = new DirectReadQuery();
//        final String jpqlString = "figure out how to use this interface";
//        directReadQuery.setJPQLString(jpqlString);
//
//        // queryResults is a List of Record objects
//        List<Record> queryResults = (List<Record>)elSession.executeQuery(directReadQuery);
//
//        throw new NotImplementedException();
//    }


//    public Serializable getContent(URI uidOfEnvelope) {
//        // ABKTODO: switch to using the DirectReadQuery once you figure out how to use it
//        Envelope foundEnvelope = getEnvelope(uidOfEnvelope);
//        return (foundEnvelope != null) ? foundEnvelope.getContents() : null;
//    }


//    public void removeContent(URI uidOfEnvelope) {
//        // ABKTODO: implement this. 
//        // figure out an update query to null out the contents
//        throw new NotImplementedException();
//    }



//    public Envelope saveEmpty(Envelope envelopeToSave) {
//        EnvelopeDTO envDTO = new EnvelopeDTO(envelopeToSave);
//        envDTO.setContents(null);
//        DatabaseSession dbSession = createDbSession();
//        dbSession.login();
//        EnvelopeDTO savedEnvelope = (EnvelopeDTO)dbSession.writeObject(envDTO);
//        dbSession.logout();
//        return savedEnvelope;
//    }



//    private EnvelopeDTO getEnvelopeDTO(URI identifiedByUid) {
//        DatabaseSession dbSession = createDbSession();
//        
//        dbSession.login();
//        
//        ExpressionBuilder expBuilder = new ExpressionBuilder();
//        Expression envelopeMatchingUid = expBuilder.get("uidAsString").equal(identifiedByUid.toASCIIString());
//        EnvelopeDTO retrievedEnvelope = (EnvelopeDTO) dbSession.readObject(EnvelopeDTO.class, envelopeMatchingUid);
//        dbSession.logout();
//        
//        return retrievedEnvelope;
//    }


//    public Serializable adapt(Serializable contentToAdapt) {
//        return contentToAdapt;
//    }



//    public long getNumberOfQualifiedEnvelopes(String withQualifier) {
//        // ABKTODO: implement this
//        throw new NotImplementedException();
//    }



//    public List<URI> getAllContentUids() {
//        // TODO Auto-generated method stub
//        return null;
//    }


//    public boolean contentExists(URI uidOfEnvelope) {
//        // TODO Auto-generated method stub
//        return false;
//    }


    public boolean exists(URI arg0) {
        // TODO Auto-generated method stub
        return false;
    }


    public GatherArchive get(URI arg0) {
        // TODO Auto-generated method stub
        return null;
    }


    public List<GatherArchive> getAll() {
        // TODO Auto-generated method stub
        return null;
    }


    public void remove(URI arg0) {
        // TODO Auto-generated method stub
        
    }


    public GatherArchive save(GatherArchive arg0) {
        // TODO Auto-generated method stub
        return null;
    }


    public void beginTransaction() {
        // ABKTODO: implement this
        ;
    }


    public void endTransaction() {
        // ABKTODO: implement this
    }
    
}
