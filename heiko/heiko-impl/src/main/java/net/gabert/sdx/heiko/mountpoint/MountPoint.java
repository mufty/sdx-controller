package net.gabert.sdx.heiko.mountpoint;

import net.gabert.sdx.heiko.api.Driver;
import net.gabert.kyla.api.Bus;
import net.gabert.kyla.api.Endpoint;
import net.gabert.util.LogUtil;
import org.apache.log4j.Logger;

import java.util.Map;

public class MountPoint {
    private static final Logger LOGGER = LogUtil.getLogger();

    private final static String RPC_EP_SUFIX = "R";
    private final static String LISTENER_EP_SUFIX = "L";

    private String mountPointContextRoot;
    private Bus bus;
    private Map<String, Object> initParams;

    private Endpoint rpcEndpoint;

    private final Driver driver;

    public MountPoint(Driver driver) {
        this.driver = driver;
    }

    public void setMountPointContextRoot(String mountPointContextRoot) {
        if (this.mountPointContextRoot != null) {
            LOGGER.fatal("Attempt to reset mountPointContextRoot.");
            throw new IllegalStateException("MountPointContextRoot already set");
        }

        this.mountPointContextRoot = mountPointContextRoot;
    }

    public void setBus(Bus bus) {
        if (this.bus != null) {
            LOGGER.fatal("Attempt to reset bus.");
            throw new IllegalStateException("Bus already set");
        }

        this.bus = bus;
    }

    public void setInitParams(Map<String, Object> initParams) {
        if (this.initParams != null) {
            LOGGER.fatal("Attempt to reset initParams.");
            throw new IllegalStateException("InitParams already set");
        }

        this.initParams = initParams;
    }
}
