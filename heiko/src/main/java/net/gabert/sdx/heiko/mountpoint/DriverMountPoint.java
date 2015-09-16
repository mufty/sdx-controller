package net.gabert.sdx.heiko.mountpoint;

import net.gabert.sdx.kyla.core.BusProxy;
import net.gabert.sdx.heiko.component.Driver;
import net.gabert.sdx.heiko.configuration.schema.DriverConfig;
import net.gabert.sdx.heiko.core.HeikoMessage;
import net.gabert.util.LogUtil;
import org.slf4j.Logger;

/**
 *
 * @author Robert Gallas
 */
public class DriverMountPoint extends ComponentMountPoint<Driver> {
    private static final Logger LOGGER = LogUtil.getLogger();

    private String publishSlot;

    private DriverMountPoint(BusProxy busProxy,
                             DriverConfig driverConfig) {
        super(busProxy, driverConfig.driverClass, driverConfig);

        LOGGER.info("Initializing device: {}", getPlainDataSlotId());
    }

    private DriverMountPoint(String dataSlotId,
                             BusProxy busProxy,
                             DriverConfig driverConfig) {
        super(dataSlotId, busProxy, driverConfig.driverClass, driverConfig);

        LOGGER.info("Initializing device: {}", getPlainDataSlotId());
    }

    public static DriverMountPoint newInstance(BusProxy busProxy, DriverConfig driverConfig) {
        return driverConfig.id == null ? new DriverMountPoint(busProxy, driverConfig)
                                       : new DriverMountPoint(driverConfig.id, busProxy, driverConfig);
    }

    @Override
    public void init() {
        super.init();
        publishSlot = asPublishDataslot(getDataSlotId());
    }

    public static String asPublishDataslot(String dataSlotId) {
        return dataSlotId + ":publish";
    }

    @Override
    public void handle(Message<HeikoMessage> kylaMessage) {
        LOGGER.trace("IN <= {MountPointId: {}, KylaMessage: {}} ", getDataSlotId(), kylaMessage);
        HeikoMessage heikoMessage = kylaMessage.getData();

        switch (heikoMessage.type) {
            case SET: handleSetValue(kylaMessage);
                      break;
            case SET_ACK: handleSetValueACK(kylaMessage);
                          break;
            case GET: handleGetValue(kylaMessage);
                      break;
            case CALL: handleCall(kylaMessage);
                       break;
        }
    }

    private void handleSetValue(Message<HeikoMessage> kylaMessage) {
        HeikoMessage heikoMessage = kylaMessage.getData();
        String contextRelativePath = heikoMessage.mountPointRelativePath;

        getComponent().setValue(contextRelativePath, heikoMessage.payload);
    }

    private void handleSetValueACK(Message<HeikoMessage> kylaMessage) {
        HeikoMessage heikoMessage = kylaMessage.getData();
        String contextRelativePath = heikoMessage.mountPointRelativePath;

        getComponent().setValue(contextRelativePath, heikoMessage.payload);

        reply(kylaMessage, null);
    }

    private void handleGetValue(Message<HeikoMessage> kylaMessage) {
        HeikoMessage heikoMessage = kylaMessage.getData();
        String contextRelativePath = heikoMessage.mountPointRelativePath;

        Object result = getComponent().getValue(contextRelativePath);

        reply(kylaMessage, result);
    }

    private void handleCall(Message<HeikoMessage> kylaMessage) {
        HeikoMessage heikoMessage = kylaMessage.getData();
        String contextRelativePath = heikoMessage.mountPointRelativePath;

        Object result = getComponent().call(contextRelativePath, (Object[]) heikoMessage.payload);

        reply(kylaMessage, result);
    }

    private void reply(Message<HeikoMessage> kylaMessage, Object result) {
        HeikoMessage reply = new HeikoMessage<>();
        reply.payload = result;
        reply.type = HeikoMessage.Type.REPLY;

        this.send(kylaMessage.createReply(reply));
    }

    public String getPublishSlot() {
        return publishSlot;
    }
}
