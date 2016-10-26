package com.intervals.util;

import com.intervals.model.data.Data;
import com.intervals.service.ResourceHandler;

import javax.xml.bind.JAXBException;
import java.io.IOException;

public class DataReader extends Reader<Data> {

    public DataReader(ResourceHandler handler) {
        super(handler);
    }

    public Data readData() throws JAXBException, IOException {
        return read(Data.class);
    }


}
