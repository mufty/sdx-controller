package net.gabert.sdx.heiko.mountpoint;

import net.gabert.util.ObjectUtil;
import net.gabert.sdx.kyla.core.BusProxy;
import net.gabert.sdx.heiko.spi.Driver;
import net.gabert.sdx.heiko.configuration.schema.DriverConfig;
import net.gabert.sdx.heiko.core.HeikoMessage;
import net.gabert.util.LogUtil;
import org.slf4j.Logger;

import java.util.Collections;

/**
 *
 * @author Robert Gallas
 */
public class DriverMountPoint extends MountPoint {
    private static final Logger LOGGER = LogUtil.getLogger();

    //TODO: Collections.unmodifiableMap(driverConfig.initParams)
    private final Driver driver;

    private DriverMountPoint(String dataSlotId,
                             BusProxy busProxy,
                             DriverConfig driverConfig) {
        super(dataSlotId, busProxy);

        LOGGER.info("Initializing device: {}", getPlainDataSlotId());
        this.driver = ObjectUtil.newInstance(driverConfig.driverClassName);
    }

    private DriverMountPoint(BusProxy busProxy,
                             DriverConfig driverConfig) {
        super(busProxy);

        LOGGER.info("Initializing device: {}", getDataSlotId());
        this.driver = ObjectUtil.newInstance(driverConfig.driverClassName);
    }

    public static DriverMountPoint newInstance(BusProxy busProxy, DriverConfig driverConfig) {
        return driverConfig.id == null ? new DriverMountPoint(busProxy, driverConfig)
                                       : new DriverMountPoint(driverConfig.id, busProxy, driverConfig);
    }

    public void init() {
        super.init();
        ObjectUtil.injectByType(driver, this);
    }

    @Override
    public void start() {}

    @Override
    public void handle(Message<HeikoMessage> kylaMessage) {
        LOGGER.trace("IN <= {MountpointId: {}, KylaMessage: {}} ", getDataSlotId(), kylaMessage);
        HeikoMessage heikoMessage = kylaMessage.getData();

        switch (heikoMessage.type) {
            case SET: handleSetValue(kylaMessage);
                      break;
            case GET: handleGetValue(kylaMessage);
                      break;
            case CALL: handleCall(kylaMessage);
                      break;
        }
    }

    private void handleSetValue(Message<HeikoMessage> message) {
        HeikoMessage heikoMessage = message.getData();
//        String contextRelativePath = getContextRelativePath(heikoMessage.absolutePath);
//
//        driver.setValue(contextRelativePath, heikoMessage.payload);
    }

    private void handleGetValue(Message<HeikoMessage> message) {
        HeikoMessage heikoMessage = message.getData();
//        String contextRelativePath = getContextRelativePath(heikoMessage.absolutePath);

//        Object result = driver.getValue(contextRelativePath);
//        HeikoMessage reply = new HeikoMessage<>();
//        reply.absolutePath = heikoMessage.absolutePath;
//        reply.payload = result;
//
//        this.send(message.createReply(reply));
    }

    private void handleCall(Message<HeikoMessage> message) {
//        HeikoMessage heikoMessage = message.getData();
//        String contextRelativePath = getContextRelativePath(heikoMessage.absolutePath);
//
//        Object result = driver.call(contextRelativePath, heikoMessage.payload);
//        HeikoMessage reply = new HeikoMessage<>();
//        reply.absolutePath = heikoMessage.absolutePath;
//        reply.payload = result;
//        reply.type = HeikoMessage.Type.REPLY;
//
//        this.send(message.createReply(reply));
    }
}
