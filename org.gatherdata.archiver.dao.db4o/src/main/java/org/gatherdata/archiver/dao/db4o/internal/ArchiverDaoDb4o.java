/**
 * The contents of this file are subject to the AED Public Use License Agreement, Version 1.0 (the "License");
 * use in any manner is strictly prohibited except in compliance with the terms of the License.
 * The License is available at http://gatherdata.org/license.
 *
 * Copyright (c) AED.  All Rights Reserved
 */
package org.gatherdata.archiver.dao.db4o.internal;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.gatherdata.archiver.core.model.GatherArchive;
import org.gatherdata.archiver.core.spi.ArchiverDao;
import org.gatherdata.archiver.dao.db4o.model.GatherArchiveDb4o;

import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import com.db4o.osgi.Db4oService;
import com.db4o.query.Predicate;
import com.google.inject.Inject;

/**
 * Internal implementation of our example OSGi service
 */
public final class ArchiverDaoDb4o implements ArchiverDao {
    protected static final Log log = LogFactory.getLog(ArchiverDaoDb4o.class);

    @Inject
    ObjectContainer db4o;

    public void beginTransaction() {
        // TODO Auto-generated method stub

    }

    public void endTransaction() {
        db4o.commit();
    }

    public boolean exists(URI uid) {
        return (get(uid) != null);
    }

    @SuppressWarnings("serial")
    public GatherArchive get(URI uid) {
        GatherArchive foundEntity = null;
        
        final URI queryUid = uid;
        ObjectSet<GatherArchiveDb4o> result = db4o.query(new Predicate<GatherArchiveDb4o>() {
            @Override
            public boolean match(GatherArchiveDb4o possibleMatch) {
                return possibleMatch.getUid().equals(queryUid);
            }
        });
        if (result.size() > 1) {
            log.warn("more than one GatherArchive found for uid: " + queryUid);
        }
        if (result.hasNext()) {
            foundEntity = result.next();
        }
        
        return foundEntity;
    }

    public Iterable<? extends GatherArchive> getAll() {
        return db4o.query(GatherArchiveDb4o.class);
    }
    
    public int getCount() {
        return db4o.query(GatherArchiveDb4o.class).size();
    }

    public void remove(URI uid) {
        db4o.delete(get(uid));
    }

    public GatherArchive save(GatherArchive entityToSave) {
        GatherArchiveDb4o entityDto = (GatherArchiveDb4o) get(entityToSave.getUid());
        if (entityDto == null) {
            entityDto = new GatherArchiveDb4o();
        }
        entityDto.copy(entityToSave);
        db4o.store(entityDto);
        
        return get(entityToSave.getUid());
    }
}
