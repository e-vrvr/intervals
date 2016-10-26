package com.intervals.service;

import com.intervals.model.config.Config;
import com.intervals.model.data.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class ResourceHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(ResourceHandler.class);

    public static final String DATA = "-data";
    public static final String REQUEST = "-request";
    public static final Set<String> ALLOWED_PREFIXES = new HashSet<>(Arrays.asList(DATA, REQUEST));
    public static final String SEPARATOR = "=";
    public static final String CONFIG_XML = "/config/config.xml";
    public static final String SAMPLE_DATA_XML = "/data/data.xml";
    public static final String SAMPLE_REQUEST_XML = "/request/request.xml";

    private InputStream requestPath;
    private InputStream dataPath;

    public ResourceHandler(String[] args) {
        if (args.length > 0) {
            try {
                parseArguments(args);
            } catch (FileNotFoundException e) {
                LOGGER.error("Problem with reading 'request' or 'data' file. Quitting.", e);
                throw new RuntimeException("Problem with reading 'request' or 'data' file. Quitting.", e);
            }
        }
    }

    private void parseArguments(String[] args) throws FileNotFoundException {
        LOGGER.debug("Parsing input arguments " + Arrays.deepToString(args));

        for (String entry : args) {
            String[] param = entry.split(SEPARATOR);
            if (validParameter(param)) {
                File file = new File(param[1]);
                if (isOk(file)) {
                    LOGGER.debug("Parsing input arguments. File " + file);
                    if (param[0].equals(DATA)) {
                        dataPath = new FileInputStream(file);
                    } else {
                        requestPath = new FileInputStream(file);
                    }
                }
            }
        }
    }

    private boolean validParameter(String[] split) {
        return split.length == 2 && ALLOWED_PREFIXES.contains(split[0]) && !split[1].isEmpty();
    }

    private boolean isOk(File file) {
        return file.exists() && file.canRead() && !file.isDirectory();
    }

    public InputStream getResource(Class clazz) throws IOException {
        if (clazz.equals(Config.class)) {
            return getInputStream(CONFIG_XML);
        } else if (clazz.equals(Data.class)) {
            return null == dataPath ? getInputStream(SAMPLE_DATA_XML) : dataPath;
        } else
            return null == requestPath ? getInputStream(SAMPLE_REQUEST_XML) : requestPath;
    }

    private InputStream getInputStream(String location)  {
        InputStream resource = getClass().getResourceAsStream(location);
        return resource;
    }
}
