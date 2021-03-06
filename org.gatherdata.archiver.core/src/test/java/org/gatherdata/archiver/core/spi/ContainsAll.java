/**
 * The contents of this file are subject to the AED Public Use License Agreement, Version 1.0 (the "License");
 * use in any manner is strictly prohibited except in compliance with the terms of the License.
 * The License is available at http://gatherdata.org/license.
 *
 * Copyright (c) AED.  All Rights Reserved
 */
package org.gatherdata.archiver.core.spi;

import java.util.Collection;
import java.util.Vector;

import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.junit.internal.matchers.TypeSafeMatcher;

public class ContainsAll<ItemType> extends TypeSafeMatcher<Iterable<ItemType>> {

    private Vector<ItemType> requiredVector;

    public ContainsAll(Collection<ItemType> requiredItems) {
        this.requiredVector =  new Vector<ItemType>(requiredItems);
    }

    @Override
    public boolean matchesSafely(Iterable<ItemType> availableItems) {
        if (availableItems == null) {
            return false;
        }
        if (!(availableItems instanceof Iterable<?>)) {
            return false;
        }
        for (ItemType item : (Iterable<ItemType>)availableItems) {
            if (requiredVector.contains(item)) {
                requiredVector.remove(item);
            }
        }
        return requiredVector.isEmpty();
    }

    public void describeTo(Description description) {
        description.appendText("contains all");
    }
        
    @Factory
    public static <T, K> Matcher<Iterable<K>> containsAll(Collection<K> requiredItems) {
        return new ContainsAll(requiredItems);
    }


}