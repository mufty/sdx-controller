package net.gabert.sdx.heiko.mountpoint;

import net.gabert.sdx.heiko.component.Callback;
import net.gabert.sdx.heiko.component.Component;
import net.gabert.sdx.heiko.component.Service;
import net.gabert.sdx.heiko.component.ValueListener;
import net.gabert.sdx.heiko.configuration.schema.ComponentConfig;
import net.gabert.sdx.heiko.core.Controller;
import net.gabert.sdx.heiko.core.HeikoMessage;
import net.gabert.sdx.heiko.core.MappingService;
import net.gabert.sdx.kyla.api.Endpoint;
import net.gabert.sdx.kyla.core.BusProxy;
import net.gabert.util.ObjectUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author Robert Gallas
 */
public abstract class ComponentMountPoint<T extends Component> extends Endpoint<HeikoMessage> {
    private final BusProxy busProxy;

    private T component;

    private final Map<String, Object> initParams;

    private final Map<Long, Callback> pendingResponses = new ConcurrentHashMap<>();

    private final Map<String, Map<String, List<ValueListener>>> listenerRegistrations = new ConcurrentHashMap<>();

    public ComponentMountPoint(BusProxy busProxy,
                               String componentClassName,
                               ComponentConfig config) {
        super(busProxy);

        this.busProxy = busProxy;
        this.component = ObjectUtil.newInstance(componentClassName);
        this.initParams = Collections.unmodifiableMap(config.initParams);
    }

    public ComponentMountPoint(String dataslotId,
                               BusProxy busProxy,
                               String componentClassName,
                               ComponentConfig config) {
        super(dataslotId, busProxy);

        this.busProxy = busProxy;
        this.component = ObjectUtil.newInstance(componentClassName);
        this.initParams = (config.initParams == null) ? null : Collections.unmodifiableMap(config.initParams);
    }

    protected T getComponent() {
        return component;
    }

    public void init() {
        busProxy.register(this);
        ObjectUtil.injectByType(component, this);
    }

    public void send(Message kylaMessage, Callback callback) {
        pendingResponses.put(kylaMessage.getConversationId(), callback);
        send(kylaMessage);
    }

    public synchronized void registerListener(String absolutePath, ValueListener valueListener) {
        MappingService mappingService = Controller.getService(MappingService.class);

        String dataSlotId = mappingService.resolveDataSlotId(absolutePath);
        String publishMountPoint = DriverMountPoint.asPublishDataslot(dataSlotId);
        busProxy.registerShared(this, publishMountPoint);

        if (listenerRegistrations.containsKey(publishMountPoint) == false) {
            listenerRegistrations.put(publishMountPoint, new ConcurrentHashMap<>());
        }

        Map<String, List<ValueListener>> subscribers = listenerRegistrations.get(publishMountPoint);

        String mountPointRelativePath = mappingService.getMountPointRelativePath(absolutePath);
        if (subscribers.containsKey(mountPointRelativePath) == false) {
            subscribers.put(mountPointRelativePath, new ArrayList<>());
        }

        subscribers.get(mountPointRelativePath).add(valueListener);
    }

    protected void publishValue(Message<HeikoMessage> kylaMessage) {
        Map<String, List<ValueListener>> subscribers = listenerRegistrations.get(kylaMessage.getDestinationSlotId());

        if (subscribers == null) {
            return;
        }

        List<ValueListener> listeners = subscribers.get(kylaMessage.getData().mountPointRelativePath);

        if (listeners == null) {
            return;
        }

        for (ValueListener listener : listeners) {
            listener.onDataChanged(kylaMessage.getData().payload);
        }
    }

    protected void possiblyHandleCallback(Message kylaMessage) {
        long conversationId = kylaMessage.getConversationId();

        Callback callback = pendingResponses.remove(conversationId);

        if (callback != null) {
            HeikoMessage heikoMessage = (HeikoMessage) kylaMessage.getData();
            callback.done(heikoMessage.payload);
        }
    }

    public void start() {
        component.start(this.initParams);
    }

    public void stop() {
        component.stop();
    }
}
