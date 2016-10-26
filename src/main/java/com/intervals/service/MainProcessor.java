package com.intervals.service;

import com.intervals.interval.PersonalInterval;
import com.intervals.interval.ResultInterval;
import com.intervals.model.data.Data;
import com.intervals.model.request.Request;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class MainProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(MainProcessor.class);

    private ResourceHandler resourceHandler;
    private ReaderService readerService;
    private Request request;

    public void process(String[] args) {
        try {
            LOGGER.debug("Started processing");
            init(args);

            request = readerService.readRequest();
            Data data = readerService.readData();

            Map<String, Map<DateTime, List<PersonalInterval>>> normalized = prepareForProcessing(data);
            Map<Integer, Set<ResultInterval>> result = findMeetingTime(normalized);
            processResult(result);

        } catch (RuntimeException re) {
            LOGGER.error("Unexpected error happened during processing. Quitting.");
        }
    }

    private Map<String, Map<DateTime, List<PersonalInterval>>> prepareForProcessing(Data data) {
        return new IntervalsNormalizeService(readerService.readConfig(), request).normalizeIntervals(data);
    }

    private void processResult(Map<Integer, Set<ResultInterval>> result) {
        new ResultDisplayService(request).process(result);
    }

    private Map<Integer, Set<ResultInterval>> findMeetingTime(Map<String, Map<DateTime, List<PersonalInterval>>> normalized) {
        return new MeetingTimeService(normalized).findTimeSlot(request.getAttendees(), request.getMeetingDuration(), request.getNeededSlots(), request.getTimeSlot().getFrom(), request.getTimeSlot().getTo());
    }

    private void init(String[] args) {
        resourceHandler = new ResourceHandler(args);
        readerService = new ReaderService(resourceHandler);
    }

    public void setReaderService(ReaderService readerService) {
        this.readerService = readerService;
    }

    public void setResourceHandler(ResourceHandler resourceHandler) {
        this.resourceHandler = resourceHandler;
    }
}
