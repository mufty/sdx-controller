package net.gabert.sdx.heiko.mountpoint;

import net.gabert.kyla.bus.BusProxy;
import net.gabert.sdx.heiko.api.Driver;
import net.gabert.kyla.api.Bus;
import net.gabert.kyla.api.Endpoint;
import net.gabert.sdx.heiko.configuration.schema.MountPointConfig;
import net.gabert.sdx.heiko.core.HeikoMessage;
import net.gabert.util.LogUtil;
import org.apache.log4j.Logger;

import java.util.Collections;
import java.util.Map;

public class MountPoint {
    private static final Logger LOGGER = LogUtil.getLogger();

    private final static String RPC_EP_SUFIX = "R";
    private final static String LISTENER_EP_SUFIX = "L";

    private final Driver driver;
    private final String mountPointContextRoot;
    private final Map<String, Object> initParams;
    private final BusProxy busProxy;
    private final Endpoint rpcEndpoint;

    public MountPoint(BusProxy busProxy, MountPointConfig mountPointConfig) {
        this.mountPointContextRoot = mountPointConfig.path;
        this.driver = loadDriver(mountPointConfig);
        this.initParams = Collections.unmodifiableMap(mountPointConfig.initParams);
        this.busProxy = busProxy;
        this.rpcEndpoint = new HeikoRpcEndpoint();

        LOGGER.info("Instantiated Endpoint: " + this.rpcEndpoint);
        busProxy.registerExclusive(this.rpcEndpoint, mountPointContextRoot);
    }

    private Driver loadDriver(MountPointConfig mountPointConfig) {
        try {
            Class<?> clazz = Class.forName(mountPointConfig.driverClassName);
            return (Driver) clazz.getConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String getMountPointContextRoot() {
        return mountPointContextRoot;
    }

    private class HeikoRpcEndpoint extends Endpoint<HeikoMessage> {

        protected HeikoRpcEndpoint() {
            super(busProxy);
        }

        @Override
        public void handle(Message<HeikoMessage> message) {
            Object result = driver.call(message.getData().absolutePath,
                                        message.getData().payload);

            HeikoMessage reply = new HeikoMessage<>();
            reply.absolutePath = message.getData().absolutePath;
            reply.payload = result;

            this.send(message.createReply(reply));
        }
    }

    @Override
    public String toString() {
        return "["+mountPointContextRoot+" -> "+driver.getClass()+"]";
    }
}
