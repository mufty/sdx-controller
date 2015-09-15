package net.gabert.sdx.heiko.connector.http;

import net.gabert.sdx.heiko.component.Connector;
import net.gabert.sdx.heiko.component.Service;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;

import java.util.Map;

/**
 * @author Robert Gallas
 */
public abstract class HttpService extends Connector {
    private static String SERVICE_PORT_KEY = "servicePort";

    private int servicePortNumber;

    private HandlerList handlers = new HandlerList();
    private Server serverInstance;

    @Override
    public void init(Map<String, Object> initParams) {
        this.servicePortNumber = ((Double)getParam(initParams, SERVICE_PORT_KEY)).intValue();
    }

    protected Object getParam(Map<String, Object> initParams, String paramName) {
        if (initParams.containsKey(paramName) == false) {
            throw new IllegalArgumentException("Missing initialization parameter: " + paramName);
        } else {
            return initParams.get(paramName);
        }
    }

    protected void addHandler(Handler handler) {
        this.handlers.addHandler(handler);
    }

    protected void start() {
        if (this.serverInstance != null) return;

        this.serverInstance = getServer();
        try {
            this.serverInstance.start();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Server getServer() {
        Server server = new Server(this.servicePortNumber);
        server.setHandler(this.handlers);
        return server;
    }

    @Override
    public void close() {
        try {
            this.serverInstance.stop();
            this.serverInstance.join();
            this.serverInstance = null;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
