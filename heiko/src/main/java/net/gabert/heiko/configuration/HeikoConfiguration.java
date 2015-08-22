package net.gabert.heiko.configuration;

import java.util.List;
import java.util.Map;

public class HeikoConfiguration {
    public BusConfig bus;
    public Map<String, String> aliases;
    public List<MountPointConfig> mountPoints;
    public List<ApplicationConfig> applications;
}
