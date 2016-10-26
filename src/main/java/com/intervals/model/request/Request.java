package com.intervals.model.request;

import com.intervals.model.jaxb.StringToDurationAdapter;
import org.joda.time.Duration;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.List;

@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlRootElement(name = "intervals_request", namespace = "com.intervals")
public class Request {

    private Duration meetingDuration;
    private int neededSlots;
    private TimeSlot timeSlot;
    private List<String> attendees;

    public List<String> getAttendees() {
        return attendees;
    }

    @XmlElementWrapper(name = "attendees", namespace = "com.intervals")
    @XmlElement(name = "id", namespace = "com.intervals")
    public void setAttendees(List<String> attendees) {
        this.attendees = attendees;
    }

    @XmlJavaTypeAdapter(StringToDurationAdapter.class)
    @XmlElement(name = "meeting_duration_minutes", namespace = "com.intervals")
    public Duration getMeetingDuration() {
        return meetingDuration;
    }

    public void setMeetingDuration(Duration meetingDuration) {
        this.meetingDuration = meetingDuration;
    }

    public int getNeededSlots() {
        return neededSlots;
    }

    @XmlElement(name = "timeslots_needed", namespace = "com.intervals")
    public void setNeededSlots(int neededSlots) {
        this.neededSlots = neededSlots;
    }

    public TimeSlot getTimeSlot() {
        return timeSlot;
    }

    @XmlElement(name = "seeking_range", type = TimeSlot.class, namespace = "com.intervals")
    public void setTimeSlot(TimeSlot timeSlot) {
        this.timeSlot = timeSlot;
    }

    @Override
    public String toString() {
        return "Request{" +
                "meetingDuration='" + meetingDuration + '\'' +
                ", neededSlots=" + neededSlots +
                ", timeSlot=" + timeSlot +
                ", attendees=" + attendees +
                '}';
    }
}
