package com.intervals.util;

import com.intervals.model.request.Request;
import com.intervals.service.ResourceHandler;
import org.joda.time.Duration;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Before;
import org.junit.Test;

import javax.xml.bind.JAXBException;
import java.io.IOException;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.assertThat;

public class RequestReaderTest {

    private RequestReader reader;
    private ResourceHandler resourceHandler;

    @Before
    public void setUp() throws IOException {
        resourceHandler = new ResourceHandler(new String[0]);
        reader = new RequestReader(resourceHandler);
    }

    @Test
    public void shouldReturnRequestObject() throws IOException, JAXBException {
        // given

        // when
        Request request = reader.readRequest();

        // then
        assertThat(request.getNeededSlots(), equalTo(2));
        assertThat(request.getMeetingDuration(), equalTo(Duration.standardMinutes(60)));
        assertThat(request.getAttendees(), containsInAnyOrder("Jack Slater", "Harry Tasker", "Emil Rottmayer"));

        DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss");
        assertThat(request.getTimeSlot().getFrom(), equalTo(DATE_TIME_FORMATTER.parseDateTime("2015-11-03T10:00:00")));
        assertThat(request.getTimeSlot().getTo(), equalTo(DATE_TIME_FORMATTER.parseDateTime("2015-11-04T14:00:00")));
    }
}
