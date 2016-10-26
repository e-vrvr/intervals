package com.intervals.model.jaxb;

import org.joda.time.DateTime;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class StringToDateTimeAdapter extends XmlAdapter<String, DateTime> {

    @Override
    public DateTime unmarshal(String input) throws Exception {
        return ParseHelper.parseDateTime(input);
    }

    @Override
    public String marshal(DateTime input) throws Exception {
        return null;
    }
}
