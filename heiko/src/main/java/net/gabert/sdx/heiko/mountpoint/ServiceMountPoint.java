package net.gabert.sdx.heiko.mountpoint;

import net.gabert.sdx.heiko.api.Service;
import net.gabert.sdx.heiko.configuration.schema.ServiceConfig;
import net.gabert.sdx.heiko.core.Controller;
import net.gabert.sdx.heiko.core.HeikoMessage;
import net.gabert.sdx.heiko.core.MappingService;
import net.gabert.sdx.kyla.api.Endpoint;
import net.gabert.sdx.kyla.core.BusProxy;
import net.gabert.util.LogUtil;
import net.gabert.util.ObjectUtil;
import org.slf4j.Logger;

import java.util.Collections;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;

/**
 *
 * @author Robert Gallas
 */
public class ServiceMountPoint extends MountPoint {
    private static final Logger LOGGER = LogUtil.getLogger();

    private final Service service;

    private final Map<String, Object> initParams;

    private ServiceMountPoint(BusProxy busProxy, ServiceConfig serviceConfig) {
        super(busProxy);

        LOGGER.info("Initializing service: {}", getDataSlotId());
        this.service = ObjectUtil.newInstance(serviceConfig.serviceClassName);
        this.initParams = Collections.unmodifiableMap(serviceConfig.initParams);
    }

    private ServiceMountPoint(String dataSlotId, BusProxy busProxy, ServiceConfig serviceConfig) {
        super(dataSlotId, busProxy);

        LOGGER.info("Initializing service: {}", getDataSlotId());
        this.service = ObjectUtil.newInstance(serviceConfig.serviceClassName);
        this.initParams = Collections.unmodifiableMap(serviceConfig.initParams);
    }

    public static ServiceMountPoint newInstance(BusProxy busProxy, ServiceConfig serviceConfig) {
        return serviceConfig.id == null ? new ServiceMountPoint(busProxy, serviceConfig)
                                        : new ServiceMountPoint(serviceConfig.id, busProxy, serviceConfig);
    }

    public void init() {
        super.init();
        ObjectUtil.injectByType(service, this);
    }

    @Override
    public void start() {
        service.init(initParams);
    }

    @Override
    public void handle(Message kylaMessage) {
        Exchange.possiblySetResponse(kylaMessage);
    }

    public void send(String absolutePath, HeikoMessage.Type type, Object payload) {
        Message kylaMessage = toKylaMessage(absolutePath,
                                            toHeikoMessage(absolutePath, type, payload));

        LOGGER.trace("OUT => {MountpointId: {}, KylaMessage: {}} ", getDataSlotId(), kylaMessage);

        send(kylaMessage);
    }

    public HeikoMessage rpc(String absolutePath, HeikoMessage.Type type, Object payload) {
        Message kylaMessage = toKylaMessage(absolutePath,
                                            toHeikoMessage(absolutePath, type,  payload));
        Exchange exchange = Exchange.createExchange(kylaMessage);

        LOGGER.trace("OUT => {MountpointId: {}, KylaMessage: {}} ", getDataSlotId(), kylaMessage);

        send(kylaMessage);

        return exchange.getResponse();
    }

    private HeikoMessage toHeikoMessage(String absolutePath, HeikoMessage.Type type, Object payload) {
        HeikoMessage heikoMessage = new HeikoMessage();
        heikoMessage.mountPointRelativePath = Controller.getService(MappingService.class)
                                                        .getMountPointRelativePath(absolutePath);
        heikoMessage.type = type;
        heikoMessage.payload = payload;

        return heikoMessage;
    }

    private Message<HeikoMessage> toKylaMessage(String absolutePath, HeikoMessage heikoMessage) {
        String dataSlotId = Controller.getService(MappingService.class).resolveDataSlotId(absolutePath);

        return createMessage(dataSlotId, heikoMessage);
    }
}
