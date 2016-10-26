package com.intervals.service;

import com.intervals.interval.PersonalInterval;
import com.intervals.model.config.Config;
import com.intervals.model.data.Data;
import com.intervals.model.data.DataEntry;
import com.intervals.model.data.TimeEntry;
import com.intervals.model.request.Request;
import com.intervals.model.request.TimeSlot;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Interval;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.util.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsMapContaining.hasKey;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsNull.nullValue;
import static org.mockito.Mockito.when;

public class IntervalsNormalizeServiceTest {

    public static final String OWNER = "sample name";
    private IntervalsNormalizeService intervalsNormalizeService;
    private Data data;

    @Mock
    private Config config;

    @Mock
    private Request request;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        TimeSlot timeSlot = new TimeSlot();
        DateTime workingFrom = DateTime.parse("2015-11-03T07:00:00");
        DateTime workingTo = DateTime.parse("2015-11-05T18:00:00");
        Interval workingHours = new Interval(workingFrom, workingTo);

        when(config.getWorkingHours()).thenReturn(workingHours);

        DateTime requestFrom = DateTime.parse("2015-11-03T12:00:00");
        DateTime requestTo = DateTime.parse("2015-11-05T12:00:00");

        timeSlot.setFrom(requestFrom);
        timeSlot.setTo(requestTo);
        when(request.getTimeSlot()).thenReturn(timeSlot);

        intervalsNormalizeService = new IntervalsNormalizeService(config, request);

        DateTime i1From = DateTime.parse("2015-11-03T10:00:00");
        DateTime i1To = DateTime.parse("2015-11-03T12:00:00");
        Interval interval1 = new Interval(i1From, i1To);

        DateTime i2From = DateTime.parse("2015-11-03T14:00:00");
        DateTime i2To = DateTime.parse("2015-11-03T16:00:00");
        Interval interval2 = new Interval(i2From, i2To);

        DateTime i3From = DateTime.parse("2015-11-03T17:00:00");
        DateTime i3To = DateTime.parse("2015-11-03T18:00:00");
        Interval interval3 = new Interval(i3From, i3To);

        DateTime dateTime = DateTime.parse("2015-11-03");

        TimeEntry timeEntry = new TimeEntry();
        timeEntry.setDate(dateTime);
        timeEntry.setBusyIntervals(Arrays.asList(interval1, interval2, interval3));

        DataEntry dataEntry = new DataEntry();
        dataEntry.setName(OWNER);
        dataEntry.setBusyTime(Collections.singletonList(timeEntry));
        dataEntry.setTimezone(DateTimeZone.forID("-02:00"));

        data = new Data();
        data.setDataEntries(Collections.singletonList(dataEntry));
    }

    @Test
    public void shouldReturnTimeCombinationsFor4Participants() throws IOException {
        // given
        data.getDataEntries().iterator().next().setTimezone(DateTimeZone.forID("-02:00"));

        // when
        Map<String, Map<DateTime, List<PersonalInterval>>> result = intervalsNormalizeService.normalizeIntervals(data);

        // then
        assertThat(result, not(nullValue()));
        assertThat(result.size(), equalTo(1));
        assertThat(result, hasKey("sample name"));

        Map<DateTime, List<PersonalInterval>> timeListMap = result.get(OWNER);
        assertThat(timeListMap.size(), equalTo(3));

        DateTime dateTime1 = DateTime.parse("2015-11-05");
        List<PersonalInterval> personalIntervals1 = timeListMap.get(dateTime1);
        assertThat(personalIntervals1.size(), equalTo(1));
        PersonalInterval personalInterval1 = personalIntervals1.iterator().next();
        assertThat(personalInterval1.getOwner(), equalTo(OWNER));
        DateTime i1From = DateTime.parse("2015-11-05T05:00:00");
        DateTime i1To = DateTime.parse("2015-11-05T16:00:00");
        assertThat(personalInterval1.getInterval(), equalTo(new Interval(i1From, i1To)));

        DateTime dateTime2 = DateTime.parse("2015-11-04");
        List<PersonalInterval> personalIntervals2 = timeListMap.get(dateTime2);
        assertThat(personalIntervals2.size(), equalTo(1));
        PersonalInterval personalInterval2 = personalIntervals2.iterator().next();
        assertThat(personalInterval2.getOwner(), equalTo(OWNER));
        DateTime i2From = DateTime.parse("2015-11-04T05:00:00");
        DateTime i2To = DateTime.parse("2015-11-04T16:00:00");
        assertThat(personalInterval2.getInterval(), equalTo(new Interval(i2From, i2To)));

        DateTime dateTime3 = DateTime.parse("2015-11-03");
        List<PersonalInterval> personalIntervals3 = timeListMap.get(dateTime3);
        assertThat(personalIntervals3.size(), equalTo(3));
        Iterator<PersonalInterval> iterator = personalIntervals3.iterator();
        DateTime i31From = DateTime.parse("2015-11-03T07:00:00");
        DateTime i31To = DateTime.parse("2015-11-03T08:00:00");
        assertThat(iterator.next().getInterval(), equalTo(new Interval(i31From, i31To)));

        DateTime i32From = DateTime.parse("2015-11-03T10:00:00");
        DateTime i32To = DateTime.parse("2015-11-03T12:00:00");
        assertThat(iterator.next().getInterval(), equalTo(new Interval(i32From, i32To)));

        DateTime i33From = DateTime.parse("2015-11-03T14:00:00");
        DateTime i33To = DateTime.parse("2015-11-03T15:00:00");
        assertThat(iterator.next().getInterval(), equalTo(new Interval(i33From, i33To)));
    }

}
