package com.intervals.model.jaxb;

import org.joda.time.Interval;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class StringToIntervalAdapter extends XmlAdapter<IntervalAdapter.Entry, Interval> implements IntervalAdapter {

    @Override
    public Interval unmarshal(Entry input) throws Exception {
        return new Interval(ParseHelper.parseTime(input.from), ParseHelper.parseTime(input.to));
    }

    @Override
    public Entry marshal(Interval input) throws Exception {
        return null;
    }

}
