package com.intervals.model.data;

import com.intervals.model.jaxb.StringToTimeZoneAdapter;
import org.joda.time.DateTimeZone;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.List;

@XmlRootElement(name = "entry", namespace = "com.intervals")
public class DataEntry {

    private String id;
    private String name;
    private DateTimeZone timezone;
    private List<TimeEntry> busyTime;

    public String getId() {
        return id;
    }

    @XmlElement(name = "id", namespace = "com.intervals")
    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    @XmlElement(name = "name", namespace = "com.intervals")
    public void setName(String name) {
        this.name = name;
    }

    public DateTimeZone getTimezone() {
        return timezone;
    }

    @XmlJavaTypeAdapter(StringToTimeZoneAdapter.class)
    @XmlElement(name = "timezone", namespace = "com.intervals")
    public void setTimezone(DateTimeZone timezone) {
        this.timezone = timezone;
    }

    public List<TimeEntry> getBusyTime() {
        return busyTime;
    }

    @XmlElementWrapper(name = "busy_time", namespace = "com.intervals")
    @XmlElement(name = "entry", namespace = "com.intervals")
    public void setBusyTime(List<TimeEntry> busyTime) {
        this.busyTime = busyTime;
    }

    @Override
    public String toString() {
        return "DataEntry{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", timezone='" + timezone + '\'' +
                ", busyTime=" + busyTime +
                '}';
    }

}
