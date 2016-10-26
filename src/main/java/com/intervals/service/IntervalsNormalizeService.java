package com.intervals.service;

import com.intervals.interval.PersonalInterval;
import com.intervals.model.config.Config;
import com.intervals.model.data.Data;
import com.intervals.model.data.DataEntry;
import com.intervals.model.data.TimeEntry;
import com.intervals.model.request.Request;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Interval;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class IntervalsNormalizeService {

    private static final Logger LOGGER = LoggerFactory.getLogger(IntervalsNormalizeService.class);

    private Config config;
    private Request request;

    public IntervalsNormalizeService(Config config, Request request) {
        this.config = config;
        this.request = request;
    }

    /**
     * Given input data, transforms it into per customer-date-free time collection.
     *
     * @return
     */
    public Map<String, Map<DateTime, List<PersonalInterval>>> normalizeIntervals(Data data) {
        LOGGER.debug("Started normalizing intervals.");

        Interval workingHours = config.getWorkingHours();

        DateTime from = request.getTimeSlot().getFrom().withHourOfDay(0);
        DateTime to = request.getTimeSlot().getTo().withHourOfDay(0);
        Map<String, Map<DateTime, List<PersonalInterval>>> all = new HashMap<>();

        for (DataEntry dataEntry : data.getDataEntries()) {
            Map<DateTime, List<PersonalInterval>> result = new HashMap<>();
            List<PersonalInterval> resultList = new ArrayList<>();

            Map<Date, List<Interval>> intervalsByDate = splitByDate(dataEntry);
            DateTime loopingDate = from;

            while ((loopingDate.isAfter(from) || loopingDate.isEqual(from)) && (loopingDate.isBefore(to) || loopingDate.isEqual(to))) {
                invertIntervals(workingHours, dataEntry, intervalsByDate, result, resultList, loopingDate);
                resultList.clear();
                loopingDate = loopingDate.plusDays(1);
            }

            all.put(dataEntry.getName(), result);

            LOGGER.debug("Normalizing for " + dataEntry.getName() + " done.");
        }
        return all;
    }

    /**
     * Inverts intervals, that is transforms occupied time in a day into free by "inverting" hours.
     *
     * @return
     */
    private void invertIntervals(Interval workingHours, DataEntry dataEntry, Map<Date, List<Interval>> intervalsByDate, Map<DateTime, List<PersonalInterval>> result, List<PersonalInterval> resultList, DateTime loopingDate) {
        LOGGER.debug("Started inverting intervals.");

        List<Interval> dateList = intervalsByDate.get(loopingDate.toDate());

        DateTime dayStart = loopingDate.withHourOfDay(workingHours.getStart().getHourOfDay());
        DateTime dayEnd = loopingDate.withHourOfDay(workingHours.getEnd().getHourOfDay());

        boolean noBusyTime = null == dateList || dateList.isEmpty();
        if (noBusyTime) {
            result.put(loopingDate, Arrays.asList(new PersonalInterval(dataEntry.getName(), timezoneAdjusted(dayStart, dayEnd, dataEntry))));
        } else {
            DateTime lastStart = dayStart;
            DateTime lastEnd = null;
            performInverting(dataEntry, resultList, dateList, dayStart, dayEnd, lastStart, lastEnd);
            result.put(loopingDate, new ArrayList<>(resultList));
        }

        LOGGER.debug("Done.");
    }

    private void performInverting(DataEntry dataEntry, List<PersonalInterval> resultList, List<Interval> dateList, DateTime dayStart, DateTime dayEnd, DateTime start, DateTime end) {
        Interval busyInterval;
        for (int i = 0; i < dateList.size() + 1; i++) {
            if (i >= dateList.size()) {
                if (start.isBefore(dayEnd.plusHours(getTimezoneOffset(dataEntry)))){
                    addToList(dataEntry, resultList, start, dayEnd);
                }
                break;
            } else {
                busyInterval = dateList.get(i);
            }
            if (busyInterval.getStart().isBefore(dayStart) || busyInterval.getStart().isEqual(dayStart)) {
                start = busyInterval.getEnd();
                end = dateList.get(i + 1).getStart();
            } else {
                end = busyInterval.getStart();
            }
            addToList(dataEntry, resultList, start, end);
            start = busyInterval.getEnd();
        }
    }

    private void addToList(DataEntry dataEntry, List<PersonalInterval> resultList, DateTime start, DateTime end) {
        if (!start.equals(end)) {
            resultList.add(new PersonalInterval(dataEntry.getName(), new Interval(start, end)));
        }
    }

    private Map<Date, List<Interval>> splitByDate(DataEntry dataEntry) {
        Map<Date, List<Interval>> result = new HashMap<>();
        List<Interval> intervals = new ArrayList<>();
        for (TimeEntry timeEntry : dataEntry.getBusyTime()) {
            for (Interval i : timeEntry.getBusyIntervals()) {
                intervals.add(timezoneAdjusted(timeEntry.getDate().plusHours(i.getStart().getHourOfDay()), timeEntry.getDate().plusHours(i.getEnd().getHourOfDay()), dataEntry));
            }
            result.put(timeEntry.getDate().toDate(), new ArrayList<>(intervals));
            intervals.clear();
        }
        return result;
    }

    private Interval timezoneAdjusted(DateTime start, DateTime end, DataEntry dataEntry) {
        int offsetHours = getTimezoneOffset(dataEntry);
        return new Interval(start.plusHours(offsetHours), end.plusHours(offsetHours));
    }

    private int getTimezoneOffset(DataEntry dataEntry) {
        DateTimeZone timeZone = dataEntry.getTimezone();
        return timeZone.getOffset(new DateTime().getMillis()) / 1000 / 3600;
    }

}
