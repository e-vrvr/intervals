package com.intervals.interval;

import org.joda.time.Interval;

/**
 * Represents personalized {@link org.joda.time.Interval} interval.
 */
public class PersonalInterval implements Comparable<PersonalInterval> {

    private String owner;
    private Interval interval;

    public Interval getInterval() {
        return interval;
    }

    public PersonalInterval(String owner, Interval interval) {
        this.interval = interval;
        this.owner = owner;
    }

    public PersonalInterval(long start, long end) {
        this.interval = new Interval(start, end);
    }

    public PersonalInterval(String owner, long start, long end) {
        this(start, end);
        this.owner = owner;
    }

    public String getOwner() {
        return owner;
    }

    public long getStartMillis() {
        return interval.getStartMillis();
    }

    public long getEndMillis() {
        return interval.getEndMillis();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PersonalInterval that = (PersonalInterval) o;

        if (interval != null ? !interval.equals(that.interval) : that.interval != null) return false;
        if (owner != null ? !owner.equals(that.owner) : that.owner != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = owner != null ? owner.hashCode() : 0;
        result = 31 * result + (interval != null ? interval.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "OwnedInterval{" +
                "owner='" + owner + '\'' +
                ", interval=" + interval +
                '}';
    }

    @Override
    public int compareTo(PersonalInterval o) {
        return (int)(o.getInterval().toDurationMillis() - interval.toDurationMillis());
    }
}