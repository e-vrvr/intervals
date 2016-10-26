package com.intervals.service;

import com.intervals.model.data.Data;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

public class ResourceHandlerTest {

    private ResourceHandler handler;
    private String[] args;

    @Test
    public void shouldReturnStreamIsEverythingIsFine() throws IOException {
        // given
        args = new String[1];
        args[0] = "-data=C:/a.txt";
        handler = new ResourceHandler(args);

        // when
        InputStream resource = handler.getResource(Data.class);

        // then
        assertThat(resource, not(nullValue()));
    }

    @Test
    public void shouldReturnStreamIsParamsAreFaulty() throws IOException {
        // given
        args = new String[1];
        args[0] = "-datqa=C:/a.txt";
        handler = new ResourceHandler(args);

        // when
        InputStream resource = handler.getResource(Data.class);

        // then
        assertThat(resource, not(nullValue()));
    }

    @Test
    public void shouldReturnStreamIfNoPathSpecified() throws IOException {
        // given
        args = new String[1];
        args[0] = "-data=";
        handler = new ResourceHandler(args);

        // when
        InputStream resource = handler.getResource(Data.class);

        // then
        assertThat(resource, not(nullValue()));
    }

    @Test
    public void shouldReturnStreamIfNoArgsPassed() throws IOException {
        // given
        args = new String[0];
        handler = new ResourceHandler(args);

        // when
        InputStream resource = handler.getResource(Data.class);

        // then
        assertThat(resource, not(nullValue()));
    }
}
