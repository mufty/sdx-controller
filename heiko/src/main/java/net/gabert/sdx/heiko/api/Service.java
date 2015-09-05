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

    public static interface Callback {
        void done(Object reponse);
    }

    protected class Context {
        private final String contextroot;

        private Context(String contextRoot) {
            this.contextroot = contextRoot;
        }

        public void setValue(String contextRelativePath, Object value) {
            LOGGER.debug("SET Value: {}{} -> {}", contextroot, contextRelativePath, value);

            mountPoint.rpc(toAbsolutePath(contextRelativePath),
                           HeikoMessage.Type.SET,
                           value);
        }

        public void setValueAsync(String contextRelativePath, Object value) {
            LOGGER.debug("SET Value Async: {}{} -> {}", contextroot, contextRelativePath, value);

            mountPoint.send(toAbsolutePath(contextRelativePath),
                            HeikoMessage.Type.SET_ASYNC,
                            value);
        }

        public void setValueAsync(String contextRelativePath, Object value, Callback callback) {
            LOGGER.debug("SET Value Async: {}{} -> {}", contextroot, contextRelativePath, value);

            mountPoint.rpc(toAbsolutePath(contextRelativePath),
                           HeikoMessage.Type.SET,
                           value,
                           callback);
        }

        public Object getValue(String contextRelativePath) {
            LOGGER.debug("GET Value: {}{}", contextroot, contextRelativePath);

            return mountPoint.rpc(toAbsolutePath(contextRelativePath),
                                  HeikoMessage.Type.GET,
                                  null).payload;
        }

        private String toAbsolutePath(String contextRelativePath) {
            return contextroot + contextRelativePath;
        }
    }
}
