package net.gabert.sdx.heiko.configuration.schema;

import java.util.List;
import java.util.Map;

public class HeikoConfiguration {
    public BusConfig bus;

    public Map<String, String> aliases;

    public List<DriverConfig> drivers;

    public List<ServiceConfig> services;
}
