package net.gabert.sdx.heiko.mountpoint;

import net.gabert.sdx.heiko.configuration.schema.ConnectorConfig;
import net.gabert.sdx.heiko.configuration.schema.DriverConfig;
import net.gabert.sdx.heiko.configuration.schema.ServiceConfig;
import net.gabert.sdx.heiko.core.Controller;
import net.gabert.sdx.heiko.core.MappingService;
import net.gabert.sdx.kyla.core.BusProxy;
import net.gabert.util.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 *
 * @author Robert Gallas
 */
public class MountServiceLocal implements MountService {
    private final String deviceTemplate;
    private final String serviceTemplate;
    private final String connectorTemplate;

    private List<ComponentMountPoint> mountPointRegistry = new ArrayList<>();

    private final BusProxy busProxy;

    public MountServiceLocal(BusProxy busProxy) {
        this.busProxy = busProxy;
        Properties prop = PropertiesLoader.fromClassPath("system.templates.properties");

        deviceTemplate = (String)prop.get("device.template");
        serviceTemplate = (String)prop.get("service.template");
        connectorTemplate = (String)prop.get("connector.template");
    }

    @Override
    public void mount(DriverConfig driverConfig)  {
        try {
            DriverMountPoint mountPoint = DriverMountPoint.newInstance(busProxy, driverConfig);
            mount(deviceTemplate, mountPoint);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void mount(ServiceConfig serviceConfig) {
        try {
            ServiceMountPoint mountPoint = ServiceMountPoint.newInstance(busProxy, serviceConfig);
            mount(serviceTemplate, mountPoint);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void mount(ConnectorConfig connectorConfig) {
        try {
            ConnectorMountPoint mountPoint = ConnectorMountPoint.newInstance(busProxy, connectorConfig);
            mount(connectorTemplate, mountPoint);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void startMontPoints() {
        for (ComponentMountPoint mp : mountPointRegistry) {
            mp.start();
        }
    }

    private void mount(String mountTemplate, ComponentMountPoint mountPoint) {
        mountPoint.init();

        String mountPath = Alias.normalize(mountTemplate, "id", mountPoint.getPlainDataSlotId());
        Controller.getService(MappingService.class).map(mountPath, mountPoint.getDataSlotId());
        mountPointRegistry.add(mountPoint);
    }
}
