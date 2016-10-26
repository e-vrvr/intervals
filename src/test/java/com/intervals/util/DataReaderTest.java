package com.intervals.util;

import com.intervals.model.data.Data;
import com.intervals.model.data.DataEntry;
import com.intervals.service.ResourceHandler;
import org.hamcrest.CoreMatchers;
import org.joda.time.DateTimeZone;
import org.joda.time.Interval;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Before;
import org.junit.Test;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;

public class DataReaderTest {

    private DataReader reader;
    private ResourceHandler resourceHandler;

    @Before
    public void setUp() throws IOException {
        resourceHandler = new ResourceHandler(new String[0]);
        reader = new DataReader(resourceHandler);
    }

    @Test
    public void shouldReturnDataObject() throws IOException, JAXBException {
        // given

        // when
        Data data = reader.readData();

        // then
        assertThat(data.getDataEntries(), hasSize(4));
        DataEntry dataEntry1 = data.getDataEntries().get(0);
        assertThat(dataEntry1.getName(), equalTo("Jack Slater"));
        assertThat(dataEntry1.getTimezone(), CoreMatchers.equalTo(DateTimeZone.forID("+00:00")));
        checkDetails(1, dataEntry1, "2015-11-02", "10:00:00", "11:00:00", "15:00:00", "17:30:00");

        DataEntry dataEntry2 = data.getDataEntries().get(1);
        assertThat(dataEntry2.getName(), equalTo("Harry Tasker"));
        assertThat(dataEntry2.getTimezone(), CoreMatchers.equalTo(DateTimeZone.forID("+01:00")));
        checkDetails(1, dataEntry2, "2015-11-02", "09:00:00", "12:00:00", "14:00:00", "17:00:00");

        DataEntry dataEntry3 = data.getDataEntries().get(2);
        assertThat(dataEntry3.getName(), equalTo("Emil Rottmayer"));
        assertThat(dataEntry3.getTimezone(), CoreMatchers.equalTo(DateTimeZone.forID("+01:00")));
        checkDetails(2, dataEntry3, "2015-11-02", "10:00:00", "13:00:00", "15:00:00", "16:00:00");
    }

    private void checkDetails(int size, DataEntry dataEntry, String date, String intervalStart1, String intervalEnd1, String intervalStart2, String intervalEnd2) {
        DateTimeFormatter DATE_FORMATTER = DateTimeFormat.forPattern("yyyy-MM-dd");
        DateTimeFormatter TIME_FORMATTER = DateTimeFormat.forPattern("HH:mm:ss");

        assertThat(dataEntry.getBusyTime(), hasSize(size));
        assertThat(dataEntry.getBusyTime().get(0).getDate(), equalTo(DATE_FORMATTER.parseDateTime(date)));
        assertThat(dataEntry.getBusyTime().get(0).getBusyIntervals(), hasSize(2));
        List<Interval> intervals = dataEntry.getBusyTime().get(0).getBusyIntervals();
        assertThat(intervals.get(0), equalTo(new Interval(TIME_FORMATTER.parseDateTime(intervalStart1), TIME_FORMATTER.parseDateTime(intervalEnd1))));
        assertThat(intervals.get(1), equalTo(new Interval(TIME_FORMATTER.parseDateTime(intervalStart2), TIME_FORMATTER.parseDateTime(intervalEnd2))));
    }
}
