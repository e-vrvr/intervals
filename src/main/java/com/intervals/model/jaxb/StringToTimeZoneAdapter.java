package com.intervals.model.jaxb;

import org.joda.time.DateTimeZone;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class StringToTimeZoneAdapter extends XmlAdapter<String, DateTimeZone> {

    @Override
    public DateTimeZone unmarshal(String input) throws Exception {
        return DateTimeZone.forID(input);
    }

    @Override
    public String marshal(DateTimeZone input) throws Exception {
        return null;
    }

}
