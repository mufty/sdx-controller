package net.gabert.sdx.heiko.mountpoint;

import net.gabert.util.ObjectUtil;
import net.gabert.sdx.kyla.core.BusProxy;
import net.gabert.sdx.heiko.api.Driver;
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

    private final Driver driver;

    public DriverMountPoint(BusProxy busProxy, DriverConfig driverConfig) {
        super(driverConfig.path,
              Collections.unmodifiableMap(driverConfig.initParams),
              busProxy);

        this.driver = ObjectUtil.newInstance(driverConfig.driverClassName);
    }

    public void init() {
        super.init();
        ObjectUtil.injectByValue(driver, this);
    }

    @Override
    public void handle(Message<HeikoMessage> message) {
        HeikoMessage heikoMessage = message.getData();

        switch (heikoMessage.type) {
            case SET: handleSetValue(message);
                      break;
            case GET: handleGetValue(message);
                      break;
            case CALL: handleCall(message);
                      break;
        }
    }

    private void handleSetValue(Message<HeikoMessage> message) {
        HeikoMessage heikoMessage = message.getData();
        String contextRelativePath = getContextRelativePath(heikoMessage.absolutePath);

        driver.setValue(contextRelativePath, heikoMessage.payload);
    }

    private void handleGetValue(Message<HeikoMessage> message) {
        HeikoMessage heikoMessage = message.getData();
        String contextRelativePath = getContextRelativePath(heikoMessage.absolutePath);

        Object result = driver.getValue(contextRelativePath);
        HeikoMessage reply = new HeikoMessage<>();
        reply.absolutePath = heikoMessage.absolutePath;
        reply.payload = result;

        this.send(message.createReply(reply));
    }

    private void handleCall(Message<HeikoMessage> message) {
        HeikoMessage heikoMessage = message.getData();
        String contextRelativePath = getContextRelativePath(heikoMessage.absolutePath);

        Object result = driver.call(contextRelativePath, heikoMessage.payload);
        HeikoMessage reply = new HeikoMessage<>();
        reply.absolutePath = heikoMessage.absolutePath;
        reply.payload = result;

        this.send(message.createReply(reply));
    }
}
