package com.intervals.util;

import com.intervals.model.config.Config;
import com.intervals.service.ResourceHandler;

public class ConfigReader extends Reader<Config> {

    private static Config CONFIG;

    public ConfigReader(ResourceHandler handler) {
        super(handler);
    }

    public Config readConfiguration() {
        if (null != CONFIG) {
            return CONFIG;
        }
        CONFIG = read(Config.class);
        return CONFIG;
    }

}
