package com.intervals.util;

import com.intervals.model.config.Config;
import com.intervals.service.ResourceHandler;
import org.joda.time.DateTimeZone;
import org.joda.time.Interval;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class ConfigReaderTest {

    private ConfigReader reader;
    private ResourceHandler resourceHandler;

    @Before
    public void setUp() throws IOException {
        resourceHandler = new ResourceHandler(new String[0]);
        reader = new ConfigReader(resourceHandler);
    }

    @Test
    public void shouldReturnConfigObject() throws IOException {
        // given
        DateTimeFormatter TIME_FORMATTER = DateTimeFormat.forPattern("HH:mm:ss");

        // when
        Config config = reader.readConfiguration();

        // then
        assertThat(config.getTimezone(), equalTo(DateTimeZone.forID("+00:00")));
        assertThat(config.getWorkingHours(), equalTo(new Interval(TIME_FORMATTER.parseDateTime("09:00:00"), TIME_FORMATTER.parseDateTime("18:00:00"))));
    }
}
