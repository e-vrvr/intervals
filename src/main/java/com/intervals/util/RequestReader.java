package com.intervals.util;

import com.intervals.model.request.Request;
import com.intervals.service.ResourceHandler;

import javax.xml.bind.JAXBException;
import java.io.IOException;

public class RequestReader extends Reader<Request> {

    public RequestReader(ResourceHandler handler) {
        super(handler);
    }

    public Request readRequest() throws JAXBException, IOException {
        return read(Request.class);
    }
}
