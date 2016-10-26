package com.intervals.model.data;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "intervals_data", namespace = "com.intervals")
public class Data {

    private List<DataEntry> dataEntries;

    public List<DataEntry> getDataEntries() {
        return dataEntries;
    }

    @XmlElementWrapper(name = "data", namespace = "com.intervals")
    @XmlElement(name = "entry", namespace = "com.intervals")
    public void setDataEntries(List<DataEntry> dataEntries) {
        this.dataEntries = dataEntries;
    }

    @Override
    public String toString() {
        return "Data{" +
                "dataEntries=" + dataEntries +
                '}';
    }
}
