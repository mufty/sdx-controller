package net.gabert.sdx.heiko.mountpoint;

import net.gabert.sdx.heiko.api.Service;
import net.gabert.sdx.heiko.configuration.schema.ServiceConfig;
import net.gabert.sdx.heiko.core.Controller;
import net.gabert.sdx.heiko.core.HeikoMessage;
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
    private final Map<UUID, Exchange> pendingResponses = new ConcurrentHashMap<>();

    public ServiceMountPoint(BusProxy busProxy, ServiceConfig serviceConfig) {
        super(serviceConfig.path,
              Collections.unmodifiableMap(serviceConfig.initParams),
              busProxy);

        this.service = ObjectUtil.newInstance(serviceConfig.serviceClassName);
    }

    public void init() {
        super.init();
        ObjectUtil.injectByType(service, this);
        service.init(getInitParams());
    }

    @Override
    public void handle(Message message) {
        Exchange exchange = pendingResponses.remove(message.getConversationId());
        if (exchange != null) {
            exchange.setResponse(message);
        }
    }

    public void send(HeikoMessage heikoMessage) {
        Message kylaMessage = toKylaMessage(heikoMessage);

        LOGGER.trace("OUT => {MountpointId: {}, KylaMessage: {}} ", getDataSlotId(), kylaMessage);

        send(kylaMessage);
    }

    public HeikoMessage rpc(HeikoMessage heikoRequest) {
        Message kylaMessage = toKylaMessage(heikoRequest);
        Exchange exchange = createExchange(kylaMessage);

        LOGGER.trace("OUT => {MountpointId: {}, KylaMessage: {}} ", getDataSlotId(), kylaMessage);

        send(kylaMessage);

        return exchange.getResponse();
    }

    private Exchange createExchange(Message kylaMessage) {
        Exchange exchange = new Exchange();
        pendingResponses.put(kylaMessage.getConversationId(), exchange);

        return  exchange;
    }

    private Message<HeikoMessage> toKylaMessage(HeikoMessage heikoMessage) {
        MountService mountService = Controller.getService(MountService.class);
        String dataSlotId = mountService.getMountPoint(heikoMessage.absolutePath).getMountPointContextRoot();

        return createMessage(dataSlotId, heikoMessage);
    }

    private static class Exchange {
        private HeikoMessage heikoResponse;
        private final CountDownLatch latch = new CountDownLatch(1);

        public HeikoMessage getResponse() {
            try {
                latch.await();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            synchronized (this) {
                return heikoResponse;
            }
        }

        public synchronized void setResponse(Endpoint.Message kylaResponse) {
            heikoResponse = (HeikoMessage)kylaResponse.getData();
            latch.countDown();
        }
    }
}
