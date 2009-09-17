package org.gatherdata.archiver.dao.db4o.model;

import org.gatherdata.archiver.core.model.GatherArchive;
import org.gatherdata.archiver.core.model.MutableGatherArchive;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;

public class GatherArchiveDb4oTest {

    @Test
    public void shouldEqualOriginalWhenDerived() {
        GatherArchive originalEntity = new MutableGatherArchive();
        
        GatherArchiveDb4o derivedEntity = new GatherArchiveDb4o();
        derivedEntity.copy(originalEntity);
        
        assertThat(derivedEntity, is(originalEntity));
    }
}
