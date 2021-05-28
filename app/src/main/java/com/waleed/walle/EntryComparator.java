package com.waleed.walle;

import java.util.Comparator;

public class EntryComparator implements Comparator<Entry> {
    @Override
    public int compare(Entry o1, Entry o2) {
        return o2.getDate_created().compareTo(o1.getDate_created());
    }
}
