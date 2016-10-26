package com.intervals.model.jaxb;

import org.joda.time.Interval;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.util.ArrayList;
import java.util.List;

public class StringToIntervalListAdapter extends XmlAdapter<IntervalAdapter.EntryList, List<Interval>> implements IntervalAdapter {

    @Override
    public List<Interval> unmarshal(EntryList input) throws Exception {
        List<Interval> intervals = new ArrayList<>();
        for (Entry e : input.entry) {
            intervals.add(new Interval(ParseHelper.parseTime(e.from), ParseHelper.parseTime(e.to)));
        }
        return intervals;
    }

    @Override
    public StringToIntervalListAdapter.EntryList marshal(List<Interval> intervals) throws Exception {
        return null;
    }

}
