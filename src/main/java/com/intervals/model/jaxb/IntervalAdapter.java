package com.intervals.model.jaxb;

import java.util.ArrayList;
import java.util.List;

public interface IntervalAdapter {

    public static class EntryList {
        public List<Entry> entry = new ArrayList<Entry>();
    }

    public static class Entry {
        public String from;
        public String to;
    }
}
