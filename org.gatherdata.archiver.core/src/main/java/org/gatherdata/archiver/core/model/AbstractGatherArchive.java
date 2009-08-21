package org.gatherdata.archiver.core.model;

import java.net.URI;

public abstract class AbstractGatherArchive implements GatherArchive {
    
    /**
     * 
     */
    private static final long serialVersionUID = 1873728138521921340L;
    
    @Override
    public int hashCode() {
        URI uid = getUid();
        final int prime = 31;
        int result = 1;
        result = prime * result + ((uid == null) ? 0 : uid.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof GatherArchive)) {
            return false;
        }
        GatherArchive other = (GatherArchive) obj;
        URI ourUid = getUid();
        if (ourUid == null) {
            if (other.getUid() != null) {
                return false;
            }
        } else if (!ourUid.equals(other.getUid())) {
            return false;
        }
        return true;
    }
    
    
}
