package com.intervals.model.request;

import com.intervals.model.jaxb.StringToDateTimeAdapter;
import org.joda.time.DateTime;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlType(name = "seeking_range", namespace = "com.intervals")
@XmlRootElement(name = "seeking_range", namespace = "com.intervals")
public class TimeSlot {

    private DateTime from;
    private DateTime to;

    public DateTime getFrom() {
        return from;
    }

    @XmlJavaTypeAdapter(StringToDateTimeAdapter.class)
    @XmlElement(name = "from", namespace = "com.intervals")
    public void setFrom(DateTime from) {
        this.from = from;
    }

    public DateTime getTo() {
        return to;
    }

    @XmlJavaTypeAdapter(StringToDateTimeAdapter.class)
    @XmlElement(name = "to", namespace = "com.intervals")
    public void setTo(DateTime to) {
        this.to = to;
    }

    @Override
    public String toString() {
        return "TimeSlot{" +
                "from='" + from + '\'' +
                ", to='" + to + '\'' +
                '}';
    }
}
