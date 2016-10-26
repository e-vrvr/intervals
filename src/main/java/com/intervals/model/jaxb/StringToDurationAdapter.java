package com.intervals.model.jaxb;

import org.joda.time.Duration;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class StringToDurationAdapter extends XmlAdapter<String, Duration> {

    @Override
    public Duration unmarshal(String input) throws Exception {
        return Duration.standardMinutes(Long.valueOf(input));
    }

    @Override
    public String marshal(Duration input) throws Exception {
        return null;
    }

}
