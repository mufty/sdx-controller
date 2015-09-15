package net.gabert.sdx.heiko.component.http;

import net.gabert.sdx.heiko.component.Callback;
import net.gabert.sdx.heiko.component.Connector;
import net.gabert.sdx.heiko.ctx.Context;
import net.gabert.util.JsonFileReader;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.HandlerList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

/**
 * @author Robert Gallas
 */
public class HttpConnector extends Connector {
    private static String CONTEXT_PATH_KEY = "contextPath";

    private static String SERVICE_PORT_KEY = "servicePort";

    private int servicePortNumber;

    private HandlerList handlers = new HandlerList();
    private Server serverInstance;
    private String connectorContextPath;

    private Context ctx;

    @Override
    public void start(Map<String, Object> initParams) {
        this.servicePortNumber = ((Double)getParam(initParams, SERVICE_PORT_KEY)).intValue();
        this.connectorContextPath = (String)initParams.get(CONTEXT_PATH_KEY);

        addHandler(configureServiceContextHandler());

        start();

        this.ctx  = getPathContext();
    }

    private void start() {
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

    @Override
    public void stop() {
        try {
            this.serverInstance.stop();
            this.serverInstance.join();
            this.serverInstance = null;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Handler configureServiceContextHandler() {
        ContextHandler context = new ContextHandler();
        context.setContextPath(this.connectorContextPath);
        context.setClassLoader(Thread.currentThread().getContextClassLoader());
        context.setAllowNullPathInfo(true);

        ServiceHandler serviceHandler = new ServiceHandler();
        context.setHandler(serviceHandler);
        return context;
    }

    // -------------------------------
    // ------ Bussiness handling -----
    // -------------------------------
    private class ServiceHandler extends AbstractHandler {
        @Override
        public void handle(String target,
                           Request baseRequest,
                           HttpServletRequest request,
                           HttpServletResponse response) throws IOException, ServletException {

            String jsonString = toJsonString(request);
            JsonRequest jsonRequest = JsonFileReader.fromString(jsonString, JsonRequest.class);

            switch(jsonRequest.type) {
                case "SET": handleSet(jsonRequest, baseRequest, response);
                            break;
                case "GET": handleGet(jsonRequest, baseRequest, response);
                            break;
            }
        }

        private String toJsonString(HttpServletRequest httpServletRequest) throws IOException {
            try(java.util.Scanner s = new java.util.Scanner(httpServletRequest.getInputStream())) {
                return s.useDelimiter("\\A").hasNext() ? s.next() : "";
            }
        }
    }

    private static class JsonRequest {
        public String type;
        public String path;
        public Object value;
    }

    private void handleSet(JsonRequest jsonRequest,
                           Request request,
                           HttpServletResponse response) {
        ctx.setValue(jsonRequest.path, jsonRequest.value);

        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
        request.setHandled(true);
    }

    private void handleGet(JsonRequest jsonRequest,
                           final Request request,
                           final HttpServletResponse response) {
        CountDownLatch latch = new CountDownLatch(1);

        ctx.getValue(jsonRequest.path, new Callback() {
            @Override
            public void done(Object data)  {
                try {
                    response.setContentType("text/html;charset=utf-8");
                    response.setStatus(HttpServletResponse.SC_OK);
                    response.getOutputStream().println(data.toString());
                    request.setHandled(true);
                    latch.countDown();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
