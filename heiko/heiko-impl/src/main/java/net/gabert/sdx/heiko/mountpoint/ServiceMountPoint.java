package net.gabert.sdx.heiko.mountpoint;

import net.gabert.sdx.heiko.api.Service;
import net.gabert.sdx.heiko.configuration.schema.ServiceConfig;
import net.gabert.sdx.kyla.core.BusProxy;
import net.gabert.util.ObjectUtil;

import java.util.Collections;

public class ServiceMountPoint extends MountPoint {
    private final Service service;

    public ServiceMountPoint(BusProxy busProxy, ServiceConfig serviceConfig) {
        super(serviceConfig.path,
              Collections.unmodifiableMap(serviceConfig.initParams),
              busProxy);

        this.service = ObjectUtil.newInstance(serviceConfig.serviceClassName);
    }

    @Override
    protected Class getImplementationClass() {
        return service.getClass();
    }
}
