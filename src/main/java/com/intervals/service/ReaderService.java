package com.intervals.service;

import com.intervals.model.config.Config;
import com.intervals.model.data.Data;
import com.intervals.model.request.Request;
import com.intervals.util.ConfigReader;
import com.intervals.util.DataReader;
import com.intervals.util.RequestReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.util.Objects;

public class ReaderService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReaderService.class);

    private ConfigReader configReader;
    private DataReader dataReader;
    private RequestReader requestReader;

    public ReaderService(ResourceHandler resourceHandler) {
        Objects.requireNonNull(resourceHandler, "ResourceHandler cannot be null");
        this.configReader = new ConfigReader(resourceHandler);
        this.dataReader = new DataReader(resourceHandler);
        this.requestReader = new RequestReader(resourceHandler);
    }

    public Config readConfig() {
        return configReader.readConfiguration();
    }

    public Data readData() {
        try {
            return dataReader.readData();
        } catch (JAXBException | IOException e) {
            LOGGER.error("There was a problem reading 'data' file. ", e);
            throw new RuntimeException("There was a problem reading 'data' file. ", e);
        }
    }

    public Request readRequest() {
        try {
            return requestReader.readRequest();
        } catch (JAXBException | IOException e) {
            LOGGER.error("There was a problem reading 'request' file. ", e);
            throw new RuntimeException("There was a problem reading 'data' file. ", e);
        }
    }
}
