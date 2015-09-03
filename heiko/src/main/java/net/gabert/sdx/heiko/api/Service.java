package net.gabert.sdx.heiko.api;

import net.gabert.sdx.heiko.core.HeikoMessage;
import net.gabert.sdx.heiko.mountpoint.ServiceMountPoint;
import net.gabert.util.LogUtil;
import org.slf4j.Logger;

import java.util.Map;

/**
 *
 * @author Robert Gallas
 */
public abstract class Service {
    private static final Logger LOGGER = LogUtil.getLogger();

    private ServiceMountPoint mountPoint;

    public abstract void init(Map<String, Object> initParams);

    public abstract void close();

    protected Context getContext() {
        return new Context("");
    }

    protected Context getContext(String contextRoot) {
        return new Context(contextRoot);
    }

    protected class Context {
        private final String contextroot;

        private Context(String contextRoot) {
            this.contextroot = contextRoot;
        }

        public void setValue(String contextRelativePath, Object value) {
            LOGGER.debug("SET Value: {}{} -> {}", contextroot, contextRelativePath, value);

            HeikoMessage heikoMessage = toHeikoMessage(contextRelativePath,
                                                       HeikoMessage.Type.SET,
                                                       value);
            mountPoint.send(heikoMessage);
        }

        public Object getValue(String contextRelativePath) {
            LOGGER.debug("GET Value: {}{}", contextroot, contextRelativePath);

            HeikoMessage heikoMessage = toHeikoMessage(contextRelativePath,
                                                       HeikoMessage.Type.GET,
                                                       null);
            return mountPoint.rpc(heikoMessage).payload;
        }

        private HeikoMessage toHeikoMessage(String contextRelativePath, HeikoMessage.Type type, Object payload) {
            HeikoMessage heikoMessage = new HeikoMessage();
            heikoMessage.absolutePath = toAbsolutePath(contextRelativePath);
            heikoMessage.type = type;
            heikoMessage.payload = payload;

            return heikoMessage;
        }

        private String toAbsolutePath(String contextRelativePath) {
            return contextroot + contextRelativePath;
        }
    }
}
