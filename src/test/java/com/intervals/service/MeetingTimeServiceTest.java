package com.intervals.service;

import com.intervals.interval.PersonalInterval;
import com.intervals.interval.ResultInterval;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.Interval;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsMapContaining.hasKey;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsNull.nullValue;

public class MeetingTimeServiceTest {

    private MeetingTimeService meetingTimeService;
    Map<String, Map<DateTime, List<PersonalInterval>>> all = new HashMap<>();

    @Before
    public void setUp() {

        DateTime dateTime = DateTime.parse("2015-11-03");
        DateTime dateTime2 = DateTime.parse("2015-11-04");
        DateTime dateTime3 = DateTime.parse("2015-11-05");

        PersonalInterval a1 = new PersonalInterval("A", dateTime.plusHours(5).getMillis(), dateTime.plusHours(13).getMillis());
        PersonalInterval a2 = new PersonalInterval("A", dateTime.plusHours(16).getMillis(), dateTime.plusHours(17).getMillis());
        PersonalInterval a3 = new PersonalInterval("A", dateTime2.plusHours(9).getMillis(), dateTime2.plusHours(11).getMillis());
        PersonalInterval a4 = new PersonalInterval("A", dateTime3.plusHours(9).getMillis(), dateTime3.plusHours(10).getMillis());
        PersonalInterval a5 = new PersonalInterval("A", dateTime3.plusHours(13).getMillis(), dateTime3.plusHours(14).getMillis());
        List<PersonalInterval> intervalsA1 = Arrays.asList(a1, a2);
        List<PersonalInterval> intervalsA2 = Arrays.asList(a3);
        List<PersonalInterval> intervalsA3 = Arrays.asList(a4, a5);
        Map<DateTime, List<PersonalInterval>> aIbyDate = new HashMap<>(3);
        aIbyDate.put(dateTime, intervalsA1);
        aIbyDate.put(dateTime2, intervalsA2);
        aIbyDate.put(dateTime3, intervalsA3);

        PersonalInterval b1 = new PersonalInterval("B", dateTime.plusHours(10).getMillis(), dateTime.plusHours(16).getMillis());
        PersonalInterval b2 = new PersonalInterval("B", dateTime2.plusHours(11).getMillis(), dateTime2.plusHours(15).getMillis());
        PersonalInterval b3 = new PersonalInterval("B", dateTime2.plusHours(9).getMillis(), dateTime2.plusHours(10).getMillis());
        PersonalInterval b4 = new PersonalInterval("B", dateTime3.plusHours(9).getMillis(), dateTime3.plusHours(11).getMillis());
        PersonalInterval b5 = new PersonalInterval("B", dateTime3.plusHours(12).getMillis(), dateTime3.plusHours(14).getMillis());
        List<PersonalInterval> intervalsB1 = Arrays.asList(b1);
        List<PersonalInterval> intervalsB2 = Arrays.asList(b2, b3);
        List<PersonalInterval> intervalsB3 = Arrays.asList(b4, b5);
        Map<DateTime, List<PersonalInterval>> bIbyDate = new HashMap<>(3);
        bIbyDate.put(dateTime, intervalsB1);
        bIbyDate.put(dateTime2, intervalsB2);
        bIbyDate.put(dateTime3, intervalsB3);

        PersonalInterval c1 = new PersonalInterval("C", dateTime.plusHours(8).getMillis(), dateTime.plusHours(11).getMillis());
        PersonalInterval c2 = new PersonalInterval("C", dateTime2.plusHours(9).getMillis(), dateTime2.plusHours(10).getMillis());
        PersonalInterval c3 = new PersonalInterval("C", dateTime2.plusHours(12).getMillis(), dateTime2.plusHours(13).getMillis());
        PersonalInterval c4 = new PersonalInterval("C", dateTime3.plusHours(11).getMillis(), dateTime3.plusHours(12).getMillis());
        PersonalInterval c5 = new PersonalInterval("C", dateTime3.plusHours(13).getMillis(), dateTime3.plusHours(14).getMillis());
        List<PersonalInterval> intervalsC1 = Arrays.asList(c1);
        List<PersonalInterval> intervalsC2 = Arrays.asList(c2, c3);
        List<PersonalInterval> intervalsC3 = Arrays.asList(c4, c5);
        Map<DateTime, List<PersonalInterval>> cIbyDate = new HashMap<>(3);
        cIbyDate.put(dateTime, intervalsC1);
        cIbyDate.put(dateTime2, intervalsC2);
        cIbyDate.put(dateTime3, intervalsC3);

        PersonalInterval d1 = new PersonalInterval("D", dateTime.plusHours(15).getMillis(), dateTime.plusHours(17).getMillis());
        PersonalInterval d2 = new PersonalInterval("D", dateTime2.plusHours(9).getMillis(), dateTime2.plusHours(13).getMillis());
        PersonalInterval d3 = new PersonalInterval("D", dateTime2.plusHours(12).getMillis(), dateTime2.plusHours(13).getMillis());
        PersonalInterval d4 = new PersonalInterval("D", dateTime3.plusHours(11).getMillis(), dateTime3.plusHours(14).getMillis());
        PersonalInterval d5 = new PersonalInterval("D", dateTime3.plusHours(15).getMillis(), dateTime3.plusHours(18).getMillis());
        List<PersonalInterval> intervalsD1 = Arrays.asList(d1);
        List<PersonalInterval> intervalsD2 = Arrays.asList(d2, d3);
        List<PersonalInterval> intervalsD3 = Arrays.asList(d4, d5);
        Map<DateTime, List<PersonalInterval>> dIbyDate = new HashMap<>(3);
        dIbyDate.put(dateTime, intervalsD1);
        dIbyDate.put(dateTime2, intervalsD2);
        dIbyDate.put(dateTime3, intervalsD3);

        all.put("A", aIbyDate);
        all.put("B", bIbyDate);
        all.put("C", cIbyDate);
        all.put("D", dIbyDate);
    }

    @Test
    public void shouldReturnTimeCombinationsFor4Participants() throws IOException {

        // given
        meetingTimeService = new MeetingTimeService(all);
        DateTime from = DateTime.parse("2015-11-03T07:00:00");
        DateTime to = DateTime.parse("2015-11-05T18:00:00");
        Duration duration = Duration.standardMinutes(60);

        // when
        Map<Integer, Set<ResultInterval>> result = meetingTimeService.findTimeSlot(Arrays.asList("A", "B", "C", "D"), duration, 1, from, to);

        // then
        assertThat(result, not(nullValue()));
        assertThat(result.size(), equalTo(3));
        assertThat(result, hasKey(2));
        assertThat(result, hasKey(3));
        assertThat(result, hasKey(4));

        Set<ResultInterval> intervalSet = result.get(4);
        assertThat(intervalSet.size(), equalTo(2));
        Iterator<ResultInterval> iterator = intervalSet.iterator();

        DateTime dateTime1 = DateTime.parse("2015-11-04T09:00:00");
        DateTime dateTime2 = DateTime.parse("2015-11-04T10:00:00");
        assertThat(iterator.next().getInterval(), equalTo(new Interval(dateTime1, dateTime2)));

        DateTime dateTime3 = DateTime.parse("2015-11-05T13:00:00");
        DateTime dateTime4 = DateTime.parse("2015-11-05T14:00:00");
        assertThat(iterator.next().getInterval(), equalTo(new Interval(dateTime3, dateTime4)));
    }

    @Test
    public void shouldReturnTimeCombinationsFor3Participants() throws IOException {

        // given
        meetingTimeService = new MeetingTimeService(all);
        DateTime from = DateTime.parse("2015-11-03T07:00:00");
        DateTime to = DateTime.parse("2015-11-05T18:00:00");
        Duration duration = Duration.standardMinutes(160);

        // when
        Map<Integer, Set<ResultInterval>> result = meetingTimeService.findTimeSlot(Arrays.asList("A", "B", "C"), duration, 1, from, to);

        // then
        assertThat(result, not(nullValue()));
        assertThat(result.size(), equalTo(1));
        assertThat(result, hasKey(2));
        assertThat(result, not(hasKey(3)));
        assertThat(result, not(hasKey(4)));

        Set<ResultInterval> intervalSet = result.get(2);
        assertThat(intervalSet.size(), equalTo(2));
        Iterator<ResultInterval> iterator = intervalSet.iterator();

        DateTime dateTime1 = DateTime.parse("2015-11-03T08:00:00");
        DateTime dateTime2 = DateTime.parse("2015-11-03T10:40:00");
        assertThat(iterator.next().getInterval(), equalTo(new Interval(dateTime1, dateTime2)));

        DateTime dateTime3 = DateTime.parse("2015-11-03T10:00:00");
        DateTime dateTime4 = DateTime.parse("2015-11-03T12:40:00");
        assertThat(iterator.next().getInterval(), equalTo(new Interval(dateTime3, dateTime4)));
    }

}
