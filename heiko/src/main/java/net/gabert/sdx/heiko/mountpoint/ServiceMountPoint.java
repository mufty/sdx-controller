package net.gabert.sdx.heiko.mountpoint;

import net.gabert.sdx.heiko.component.Service;
import net.gabert.sdx.heiko.configuration.schema.ServiceConfig;
import net.gabert.sdx.heiko.core.HeikoMessage;
import net.gabert.sdx.kyla.core.BusProxy;
import net.gabert.util.LogUtil;
import org.slf4j.Logger;

/**
 *
 * @author Robert Gallas
 */
public class ServiceMountPoint extends ComponentMountPoint<Service> {
    private static final Logger LOGGER = LogUtil.getLogger();

    private ServiceMountPoint(BusProxy busProxy, ServiceConfig serviceConfig) {
        super(busProxy, serviceConfig.serviceClass, serviceConfig);

        LOGGER.info("Initializing service: {}", getPlainDataSlotId());
    }

    private ServiceMountPoint(String dataSlotId, BusProxy busProxy, ServiceConfig serviceConfig) {
        super(dataSlotId, busProxy, serviceConfig.serviceClass, serviceConfig);

        LOGGER.info("Initializing service: {}", getPlainDataSlotId());
    }

    public static ServiceMountPoint newInstance(BusProxy busProxy, ServiceConfig serviceConfig) {
        return serviceConfig.id == null ? new ServiceMountPoint(busProxy, serviceConfig)
                                        : new ServiceMountPoint(serviceConfig.id, busProxy, serviceConfig);
    }

    @Override
    public void handle(Message<HeikoMessage> kylaMessage) {
        possiblyHandleCallback(kylaMessage);
    }
}
