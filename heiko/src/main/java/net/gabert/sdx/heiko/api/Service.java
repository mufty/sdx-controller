package net.gabert.sdx.heiko.api;

import net.gabert.sdx.heiko.core.HeikoMessage;
import net.gabert.sdx.heiko.mountpoint.ServiceMountPoint;
import net.gabert.sdx.kyla.api.Endpoint;

import java.util.Map;

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
            Endpoint.Message request = mountPoint.createMessage(null, null);
            return mountPoint.awaitResponse(request);
        }

        protected void setListener(String path, ValueListener value) {

        }
    }
}
