package net.gabert.sdx.heiko.api;

import net.gabert.sdx.heiko.mountpoint.ServiceMountPoint;

import java.util.Map;

/**
 *
 * @author Robert Gallas
 */
public abstract class Service {
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

        public void setValue(String path, Object value) {
            String absolutePath = contextroot + path;
            mountPoint.setValue(absolutePath, value);
        }

        public Object getValue(String path) {
            String absolutePath = contextroot + path;
            return mountPoint.getValue(absolutePath);
        }

        protected void setListener(String path, ValueListener value) {

        }
    }
}