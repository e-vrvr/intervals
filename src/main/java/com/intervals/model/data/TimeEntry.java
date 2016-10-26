package com.intervals.model.data;

import com.intervals.model.jaxb.StringToDateAdapter;
import com.intervals.model.jaxb.StringToIntervalListAdapter;
import org.joda.time.DateTime;
import org.joda.time.Interval;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.List;

@XmlAccessorType(XmlAccessType.PUBLIC_MEMBER)
@XmlRootElement(name = "busy_time", namespace = "com.intervals")
public class TimeEntry {

    private DateTime date;
    private List<Interval> busyIntervals;

    public DateTime getDate() {
        return date;
    }

    @XmlJavaTypeAdapter(StringToDateAdapter.class)
    @XmlElement(name = "date", namespace = "com.intervals")
    public void setDate(DateTime date) {
        this.date = date;
    }

    public List<Interval> getBusyIntervals() {
        return busyIntervals;
    }

    @XmlJavaTypeAdapter(StringToIntervalListAdapter.class)
    @XmlElement(name = "time_entries", namespace = "com.intervals")
    public void setBusyIntervals(List<Interval> busyIntervals) {
        this.busyIntervals = busyIntervals;
    }

    @Override
    public String toString() {
        return "TimeEntry{" +
                "date=" + date +
                ", busyIntervals=" + busyIntervals +
                '}';
    }
}
