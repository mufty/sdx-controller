package net.gabert.sdx.heiko.connector.http;

import net.gabert.sdx.heiko.ctx.Context;
import net.gabert.util.JsonFileReader;
import net.gabert.util.JsonTransformation;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.eclipse.jetty.server.handler.ContextHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

/**
 * @author Robert Gallas
 */
public class HttpConnector extends HttpService {
    private static String CONTEXT_PATH_KEY = "contextPath";

    private String connectorContextPath;

    private Context ctx;

    @Override
    public void init(Map<String, Object> initParams) {
        super.init(initParams);

        this.connectorContextPath = (String)initParams.get(CONTEXT_PATH_KEY);

        addHandler(configureServiceContextHandler());

        start();

        this.ctx  = getPathContext("");
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
