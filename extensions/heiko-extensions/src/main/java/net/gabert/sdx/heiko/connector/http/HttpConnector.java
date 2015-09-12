package net.gabert.sdx.heiko.connector.http;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.eclipse.jetty.server.handler.ContextHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * @author Robert Gallas
 */
public class HttpConnector extends HttpService {
    private static String CONTEXT_PATH_KEY = "contextPath";

    private String connectorContextPath;

    @Override
    public void init(Map<String, Object> initParams) {
        super.init(initParams);

        this.connectorContextPath = (String)initParams.get(CONTEXT_PATH_KEY);

        addHandler(configureServiceContextHandler());

        start();
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
                           HttpServletRequest httpServletRequest,
                           HttpServletResponse httpServletResponse) throws IOException, ServletException {

//            Object portResponse = onRequest(httpServletRequest);
            baseRequest.setHandled(true);

            httpServletResponse.setContentType("text/html;charset=utf-8");
            httpServletResponse.setStatus(HttpServletResponse.SC_OK);
//            httpServletResponse.getWriter().println(portResponse);
        }
    }
}
