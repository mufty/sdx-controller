package net.gabert.sdx.heiko.mountpoint;

import net.gabert.sdx.heiko.component.Callback;
import net.gabert.sdx.heiko.component.Component;
import net.gabert.sdx.heiko.component.Service;
import net.gabert.sdx.heiko.configuration.schema.ComponentConfig;
import net.gabert.sdx.heiko.core.HeikoMessage;
import net.gabert.sdx.kyla.api.Endpoint;
import net.gabert.sdx.kyla.core.BusProxy;
import net.gabert.util.ObjectUtil;

import java.util.Collections;
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
        this.initParams = Collections.unmodifiableMap(config.initParams);
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
