package net.gabert.sdx.heiko.mountpoint;

import net.gabert.sdx.heiko.api.Service;
import net.gabert.sdx.heiko.configuration.schema.ServiceConfig;
import net.gabert.sdx.heiko.core.Controller;
import net.gabert.sdx.heiko.core.HeikoMessage;
import net.gabert.sdx.kyla.api.Endpoint;
import net.gabert.sdx.kyla.core.BusProxy;
import net.gabert.util.ObjectUtil;

import java.util.Collections;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;

public class ServiceMountPoint extends MountPoint {
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
        ObjectUtil.injectByValue(service, this);
        service.init(getInitParams());
    }

    public Message awaitResponse(Message request) {
        Exchange exchange = new Exchange();
        pendingResponses.put(request.getConversationId(), exchange);
        send(request);
        return exchange.getResponse();
    }

    public void setValue(String absolutePath, Object value) {
        MountService mountService  = Controller.getService(MountService.class);
        String dataSlotId = mountService.getMountPoint(absolutePath).getMountPointContextRoot();

        HeikoMessage message = new HeikoMessage();
        message.absolutePath = absolutePath;
        message.payload = value;
        message.type = HeikoMessage.Type.SET;

        this.send(createMessage(dataSlotId, message));
    }

    @Override
    public void handle(Message message) {
        Exchange exchange = pendingResponses.remove(message.getConversationId());
        if (exchange != null) {
            exchange.setResponse(message);
        }
    }

    private static class Exchange {
        private Endpoint.Message response;
        private final CountDownLatch latch = new CountDownLatch(1);

        public Endpoint.Message getResponse() {
            try {
                latch.await();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            synchronized (this) {
                return response;
            }
        }

        public synchronized void setResponse(Endpoint.Message response) {
            this.response = response;
            latch.countDown();
        }
    }


    @Override
    protected Class getImplementationClass() {
        return service.getClass();
    }
}
