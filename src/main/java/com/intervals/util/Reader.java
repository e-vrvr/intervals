package com.intervals.util;

import com.intervals.service.ResourceHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

public abstract class Reader<T> {

    private static final Logger LOGGER = LoggerFactory.getLogger(Reader.class);
    private ResourceHandler handler;

    public Reader(ResourceHandler handler) {
        Objects.requireNonNull(handler, "ResourceHandler cannot be null");
        this.handler = handler;
    }

    protected T read(Class<T> clazz) {
        T data = null;
        InputStream is = null;
        try {
            is = handler.getResource(clazz);
            JAXBContext context = JAXBContext.newInstance(clazz);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            data = (T) unmarshaller.unmarshal(is);
        } catch (IOException |  JAXBException e) {
            LOGGER.error("There was a problem getting XML data", e);
            throw new RuntimeException("There was a problem getting XML data", e);
        }
        return data;
    }

}
