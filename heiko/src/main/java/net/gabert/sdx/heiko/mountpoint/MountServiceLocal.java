package net.gabert.sdx.heiko.mountpoint;

import net.gabert.sdx.heiko.configuration.schema.DriverConfig;
import net.gabert.sdx.heiko.configuration.schema.ServiceConfig;
import net.gabert.sdx.heiko.core.Controller;
import net.gabert.sdx.heiko.core.MappingService;
import net.gabert.sdx.kyla.core.BusProxy;
import net.gabert.util.*;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 *
 * @author Robert Gallas
 */
public class MountServiceLocal implements MountService {
    private static final Logger LOGGER = LogUtil.getLogger();

    private final String deviceTemplate;
    private final String serviceTemplate;
    private final String connectorTemplate;

    private final BusProxy busProxy;

    public MountServiceLocal(BusProxy busProxy) {
        this.busProxy = busProxy;
        Properties prop = PropertiesLoader.fromClassPath("system.templates.properties");

        deviceTemplate = (String)prop.get("device.template");
        serviceTemplate = (String)prop.get("service.template");
        connectorTemplate = (String)prop.get("connector.template");

        LOGGER.info(this.getClass().getSimpleName() + " initialized.");
    }

    @Override
    public void mount(DriverConfig driverConfig)  {
        try {
            DriverMountPoint driverMountPoint = DriverMountPoint.newInstance(busProxy, driverConfig);
            mount(deviceTemplate, driverMountPoint);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void mount(ServiceConfig serviceConfig) {
        try {
            ServiceMountPoint serviceMountPoint = ServiceMountPoint.newInstance(busProxy, serviceConfig);
            mount(serviceTemplate, serviceMountPoint);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void mount(String mountTemplate, MountPoint mountPoint) {
        mountPoint.init();

        String mountPath = Alias.normalize(mountTemplate, "id", mountPoint.getPlainDataSlotId());
        Controller.getService(MappingService.class).map(mountPath, mountPoint.getDataSlotId());

        mountPoint.start();
        LOGGER.info("MountPoint started: {}", mountPoint);
    }
}
