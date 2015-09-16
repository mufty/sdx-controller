package net.gabert.sdx.heiko.component.http;

import net.gabert.sdx.heiko.component.Service;
import org.eclipse.jetty.rewrite.handler.RedirectPatternRule;
import org.eclipse.jetty.rewrite.handler.RewriteHandler;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;

import java.util.Map;

/**
 * Class which is responsible for serving static html resources.
 *
 * @author Robert Gallas
 */
public class HttpApplication extends Service {
    private static String SERVICE_PORT_KEY = "servicePort";
    private static String CONTEXT_PATH_KEY = "contextPath";
    private static String ROOT_PATH_KEY    = "guiRootPath";
    private static String WELCOME_FILE_KEY = "welcomeFile";

    private int servicePortNumber;
    private HandlerList handlers = new HandlerList();
    private Server serverInstance;

    private String contextPath;
    private String guiRootPath;
    private String welcomeFile;

    @Override
    public void start(Map<String, Object> initParams) {
        this.servicePortNumber = ((Double)getParam(initParams, SERVICE_PORT_KEY)).intValue();
        this.contextPath = (String)initParams.get(CONTEXT_PATH_KEY);
        this.guiRootPath = (String)initParams.get(ROOT_PATH_KEY);
        this.welcomeFile = (String)initParams.get(WELCOME_FILE_KEY);

        addHandler(configureResourceHandler());
        addHandler(configureRootHandler());

        start();
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

    protected void addHandler(Handler handler) {
        this.handlers.addHandler(handler);
    }

    protected Object getParam(Map<String, Object> initParams, String paramName) {
        if (initParams.containsKey(paramName) == false) {
            throw new IllegalArgumentException("Missing initialization parameter: " + paramName);
        } else {
            return initParams.get(paramName);
        }
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

    private Handler configureResourceHandler() {
        ContextHandler context = new ContextHandler(this.contextPath);
        context.setClassLoader(Thread.currentThread().getContextClassLoader());

        ResourceHandler resourceHandler = new ResourceHandler();
        resourceHandler.setDirectoriesListed(false);
        resourceHandler.setWelcomeFiles(welcomeFiles());
        String folder = this.getClass().getClassLoader().getResource(this.guiRootPath).toExternalForm();
        resourceHandler.setResourceBase(folder);

        context.setHandler(resourceHandler);

        return context;
    }

    private String[] welcomeFiles() {
        return new String[]{welcomeFile};
    }

    private Handler configureRootHandler() {
        RedirectPatternRule redirect = new RedirectPatternRule();
        redirect.setPattern("/");
        redirect.setLocation(this.contextPath);

        RewriteHandler rewrite = new RewriteHandler();
        rewrite.addRule(redirect);

        return rewrite;
    }
}