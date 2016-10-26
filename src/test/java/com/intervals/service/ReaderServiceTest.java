package com.intervals.service;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.xml.bind.UnmarshalException;
import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ReaderServiceTest {

    private ReaderService readerService;

    @Mock
    private ResourceHandler resourceHandler;
    @Mock
    private java.io.InputStream is;

    @Before
    public void setUp() throws IOException {
        MockitoAnnotations.initMocks(this);
        when(resourceHandler.getResource(Matchers.any())).thenReturn(is);
        readerService = new ReaderService(resourceHandler);
    }

    @Test
    public void shouldTryToGetConfig() throws IOException {
        // given
        // when
        try {
            readerService.readConfig();
        } catch (RuntimeException e) {
            // then
            verify(resourceHandler).getResource(Matchers.any());
            assertThat(e.getMessage(), equalTo("There was a problem getting XML data"));
            assertThat(e.getCause().getClass(), equalTo(UnmarshalException.class));
        }
    }

    @Test
    public void shouldTryToGetData() throws IOException {
        // given
        // when
        try {
            readerService.readData();
        } catch (RuntimeException e) {
            // then
            verify(resourceHandler).getResource(Matchers.any());
            assertThat(e.getMessage(), equalTo("There was a problem getting XML data"));
            assertThat(e.getCause().getClass(), equalTo(UnmarshalException.class));
        }
    }

    @Test
    public void shouldTryToGetRequest() throws IOException {
        // given
        // when
        try {
            readerService.readRequest();
        } catch (RuntimeException e) {
            // then
            verify(resourceHandler).getResource(Matchers.any());
            assertThat(e.getMessage(), equalTo("There was a problem getting XML data"));
            assertThat(e.getCause().getClass(), equalTo(UnmarshalException.class));
        }
    }

}
