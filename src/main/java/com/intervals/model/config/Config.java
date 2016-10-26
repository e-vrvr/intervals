package com.intervals.model.config;

import com.intervals.model.jaxb.StringToIntervalAdapter;
import com.intervals.model.jaxb.StringToTimeZoneAdapter;
import org.joda.time.DateTimeZone;
import org.joda.time.Interval;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlRootElement(name = "intervals_config", namespace = "com.intervals")
public class Config {

    private DateTimeZone timezone;
    private Interval workingHours;

    public DateTimeZone getTimezone() {
        return timezone;
    }

    @XmlJavaTypeAdapter(StringToTimeZoneAdapter.class)
    @XmlElement(name = "timezone", namespace = "com.intervals")
    public void setTimezone(DateTimeZone timezone) {
        this.timezone = timezone;
    }

    public Interval getWorkingHours() {
        return workingHours;
    }

    @XmlJavaTypeAdapter(StringToIntervalAdapter.class)
    @XmlElement(name = "working_hours", namespace = "com.intervals")
    public void setWorkingHours(Interval workingHours) {
        this.workingHours = workingHours;
    }

    @Override
    public String toString() {
        return "Config{" +
                "timezone='" + timezone + '\'' +
                ", workingHours='" + workingHours + '\'' +
                '}';
    }
}
