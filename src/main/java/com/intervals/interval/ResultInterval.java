package com.intervals.interval;

import org.joda.time.Interval;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Represents result of search for available time slot.
 * Contains {@link org.joda.time.Interval} time slot and collection participants who are available at given time.
 */
public class ResultInterval {

    private Set<String> participants;
    private Interval interval;

    public Interval getInterval() {
        return interval;
    }

    public Set<String> getParticipants() {
        return new HashSet<>(participants);
    }

    public ResultInterval(Interval interval, Set<String> participants) {
        this.interval = interval;
        this.participants = new HashSet<>(participants);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ResultInterval that = (ResultInterval) o;

        if (!interval.equals(that.interval)) return false;
        if (!participants.equals(that.participants)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = interval.hashCode();
        result = 31 * result + participants.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "ResultInterval{" +
                "interval=" + interval +
                ", participants=" + Arrays.deepToString(participants.toArray()) +
                '}';
    }
}
