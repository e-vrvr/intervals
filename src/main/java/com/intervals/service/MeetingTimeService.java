package com.intervals.service;

import com.intervals.interval.PersonalInterval;
import com.intervals.interval.ResultInterval;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.Interval;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class MeetingTimeService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MeetingTimeService.class);

    public static final int MID_DAY = 12; // actually depends on timezone splitting. if equally around the globe, value should be definitely different.
    private Map<String, Map<DateTime, List<PersonalInterval>>> data = new HashMap<>();

    public MeetingTimeService(Map<String, Map<DateTime, List<PersonalInterval>>> data) {
        this.data = data;
    }

    public Map<Integer, Set<ResultInterval>> findTimeSlot(List<String> attendees, Duration meetingDuration, int neededSlots, DateTime start, DateTime end) {
        LOGGER.debug("Started looking for suitable time slot");

        Map<Integer, Set<ResultInterval>> resultMap = new HashMap<>();
        /**
         * get all customer records in start-end range
         */
        Map<DateTime, List<PersonalInterval>> intervalsByDateBeforeNoon = new HashMap<>();
        Map<DateTime, List<PersonalInterval>> intervalsByDateAfterNoon = new HashMap<>();
        for (String attendee : attendees) {
            for (DateTime key : data.get(attendee).keySet()) {
                List<PersonalInterval> intervalsByDateBeforeNoonList = new ArrayList<>();
                List<PersonalInterval> intervalsByDateAfterNoonList = new ArrayList<>();
                if (key.toDate().equals(start.withHourOfDay(0).toDate()) || key.toDate().equals(end.withHourOfDay(0).toDate()) || start.getMillis() <= key.getMillis() && end.getMillis() >= key.getMillis()) {
                    // in range
                    for (PersonalInterval interval : data.get(attendee).get(key)) {

                        if (interval.getInterval().getEnd().isBefore(start) || interval.getInterval().getStart().isAfter(end)) {
                            continue;
                        }

                        if (interval.getInterval().getEnd().getHourOfDay() <= MID_DAY) {
                            addToList(intervalsByDateBeforeNoonList, interval, meetingDuration);
                        } else if (interval.getInterval().getStart().getHourOfDay() >= MID_DAY) {
                            addToList(intervalsByDateAfterNoonList, interval, meetingDuration);
                        } else if (interval.getInterval().getEnd().getHourOfDay() >= MID_DAY && interval.getInterval().getStart().getHourOfDay() <= MID_DAY) {
                            addToList(intervalsByDateBeforeNoonList, interval, meetingDuration);
                            addToList(intervalsByDateAfterNoonList, interval, meetingDuration);
                        }
                    }
                }

                /**
                 * Put into map by day
                 */
                punIntoMap(intervalsByDateBeforeNoon, key, intervalsByDateBeforeNoonList);
                punIntoMap(intervalsByDateAfterNoon, key, intervalsByDateAfterNoonList);
                /**
                 * Clear
                 */
                intervalsByDateBeforeNoonList.clear();
                intervalsByDateAfterNoonList.clear();
            }
        }
        findTimeSlotInMaps(meetingDuration, neededSlots, start, end, resultMap, intervalsByDateBeforeNoon, intervalsByDateAfterNoon);

        return resultMap;
    }

    private void findTimeSlotInMaps(Duration meetingDuration, int neededSlots, DateTime start, DateTime end, Map<Integer, Set<ResultInterval>> resultMap, Map<DateTime, List<PersonalInterval>> intervalsByDateBeforeNoon, Map<DateTime, List<PersonalInterval>> intervalsByDateAfterNoon) {
        /**
         * Sort intervals by length
         */
        for (DateTime key : intervalsByDateBeforeNoon.keySet()) {
            Collections.sort(intervalsByDateBeforeNoon.get(key));
            Collections.sort(intervalsByDateAfterNoon.get(key));
        }

        /** Search for time slot. Lets start with bigger map */
        Map<DateTime, List<PersonalInterval>> checkFirst = intervalsByDateBeforeNoon;
        Map<DateTime, List<PersonalInterval>> checkSecond = intervalsByDateAfterNoon;

        if (intervalsByDateBeforeNoon.values().size() < intervalsByDateAfterNoon.values().size()) {
            checkFirst = intervalsByDateAfterNoon;
            checkSecond = intervalsByDateBeforeNoon;
        }

        checkAvailableTime(meetingDuration, neededSlots, start, end, resultMap, checkFirst);
        checkAvailableTime(meetingDuration, neededSlots, start, end, resultMap, checkSecond);
    }

    private void checkAvailableTime(Duration meetingDuration, int neededSlots, DateTime start, DateTime end, Map<Integer, Set<ResultInterval>> resultMap, Map<DateTime, List<PersonalInterval>> mapToCheck) {
        for (DateTime key : mapToCheck.keySet()) {
            List<PersonalInterval> toBeChecked = mapToCheck.get(key);

            for (PersonalInterval whatever : toBeChecked) {
                Interval intersect = new Interval(start, end);
                Collections.rotate(toBeChecked, 1);
                Interval prevIntersect = null;
                Set<String> owners = new HashSet<>();
                for (PersonalInterval i : toBeChecked) {
                    if (owners.contains(i.getOwner())) { // will have null intersection here
                        continue;
                    }
                    prevIntersect = intersect;
                    intersect = intersect.overlap(i.getInterval());

                    if (intersect == null) {
                        intersect = prevIntersect;
                        continue;
                    }
                    owners.add(i.getOwner());

                    if (owners.size() > 1) {
                        if (meetingDuration.multipliedBy(neededSlots).isEqual(intersect.toDuration()) || meetingDuration.multipliedBy(neededSlots).isShorterThan(intersect.toDuration())) {
                            addToResults(meetingDuration, resultMap, intersect, owners, neededSlots);
                        }
                        if (neededSlots > 1 && (intersect.toDuration().isEqual(meetingDuration) || intersect.toDuration().isLongerThan(meetingDuration))) {
                            addToResults(meetingDuration, resultMap, intersect, owners, 1);
                        }

                    }
                }
            }
        }
    }

    private void addToResults(Duration meetingDuration, Map<Integer, Set<ResultInterval>> resultMap, Interval intersect, Set<String> owners, Integer neededSlots) {
        Set<ResultInterval> intervalSet = resultMap.get(owners.size());

        if (null == intervalSet) {
            intervalSet = new HashSet<>();
            intervalSet.add(new ResultInterval(intersect.withEndMillis(intersect.getStartMillis() + neededSlots * meetingDuration.getMillis()), owners));
        } else {
            intervalSet.add(new ResultInterval(intersect.withEndMillis(intersect.getStartMillis() + neededSlots * meetingDuration.getMillis()), owners));
        }
        resultMap.put(owners.size(), intervalSet);
    }

    private void addToList(List<PersonalInterval> list, PersonalInterval interval, Duration meetingDuration) {
        if (interval.getInterval().toDuration().isEqual(meetingDuration) || interval.getInterval().toDuration().isLongerThan(meetingDuration)) {
            list.add(interval);
        }
    }

    /**
     * Put into map by day
     */
    private void punIntoMap(Map<DateTime, List<PersonalInterval>> map, DateTime key, List<PersonalInterval> values) {
        List<PersonalInterval> listFromMap = map.get(key);
        if (null == listFromMap || listFromMap.isEmpty()) {
            map.put(key, new ArrayList<>(values));
        } else {
            listFromMap.addAll(values);
        }
    }
}
