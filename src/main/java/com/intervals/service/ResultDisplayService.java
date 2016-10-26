package com.intervals.service;

import com.intervals.interval.ResultInterval;
import com.intervals.model.request.Request;
import org.joda.time.Interval;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class ResultDisplayService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ResultDisplayService.class);

    public static final String DATE_PATTERN = "yyyy-MM-dd HH:mm";
    public static final String TIME_PATTERN = "HH:mm";
    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormat.forPattern(DATE_PATTERN);
    public static final DateTimeFormatter TIME_FORMATTER = DateTimeFormat.forPattern(TIME_PATTERN);
    private Request request;

    public ResultDisplayService(Request request) {
        this.request = request;
    }

    public void process(Map<Integer, Set<ResultInterval>> result) {
        printResults(result);
    }

    private void printResults(Map<Integer, Set<ResultInterval>> result) {
        LOGGER.debug("Printing search results");

        List<String> attendees = request.getAttendees();
        Set<ResultInterval> intervals;

        printHeader();

        if (result.size() == 0) {
            System.out.println("- Unfortunately, it is not possible");
            System.out.println("- to arrange meeting for following people:");
            printAttendees(attendees);
            System.out.println("- within given range at all. Try providing different time range.");
        }

        for (int i = attendees.size(); i > 0; i--) {
            intervals = result.get(Integer.valueOf(i));
            if (null != intervals) {
                if (i != attendees.size()) {
                    System.out.println("- Unfortunately, it is not possible");
                    System.out.println("- to arrange meeting for following people:");
                    printAttendees(attendees);
                    System.out.println();
                    System.out.println("But part of them can meet:");
                    printIntervalsExtended(intervals);
                } else {
                    System.out.println("- Following people:");
                    printAttendees(attendees);
                    System.out.println(" can meet on");
                    printIntervals(intervals);
                }
                break;
            }
        }
    }

    private void printHeader() {
        System.out.println("Request >");
        System.out.println("- Find " + request.getNeededSlots() + " available time slot(s) with " + request.getMeetingDuration().toStandardMinutes().getMinutes() + " minutes duration");
        System.out.println("- between " + request.getTimeSlot().getFrom().toString(DATE_TIME_FORMATTER) + " and " + request.getTimeSlot().getTo().toString(DATE_TIME_FORMATTER));
        System.out.println("------------");
    }

    private void printIntervalsExtended(Set<ResultInterval> intervals) {
        for (ResultInterval interval : intervals) {
            for (String attendee : interval.getParticipants()) {
                printAttendee(attendee);
            }
            System.out.print(" at");
            printInterval(interval.getInterval());
            System.out.println();
        }
    }

    private void printAttendee(String attendee) {
        System.out.println(">> " + attendee);
    }

    private void printIntervals(Set<ResultInterval> intervals) {
        for (ResultInterval interval : intervals) {
            printInterval(interval.getInterval());
        }
    }

    private void printInterval(Interval interval) {
        System.out.println(" " + interval.getStart().toString(DATE_TIME_FORMATTER) + "-" + interval.getEnd().toString(TIME_FORMATTER));
    }

    private void printAttendees(List<String> attendees) {
        for (String attendee : attendees) {
            printAttendee(attendee);
        }
    }
}
