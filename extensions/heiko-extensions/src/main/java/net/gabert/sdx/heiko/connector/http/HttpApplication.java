package net.gabert.sdx.heiko.connector.http;

import org.eclipse.jetty.rewrite.handler.RedirectPatternRule;
import org.eclipse.jetty.rewrite.handler.RewriteHandler;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.ResourceHandler;

import java.util.Map;

/**
 * Class which is responsible for serving static html resources.
 *
 * @author Robert Gallas
 */
public class HttpApplication extends HttpService {
    private static String CONTEXT_PATH_KEY = "contextPath";
    private static String ROOT_PATH_KEY = "guiRootPath";
    private static String WELCOME_FILE_KEY = "welcomeFile";

    private String contextPath;
    private String guiRootPath;
    private String welcomeFile;

    @Override
    public void init(Map<String, Object> initParams) {
        super.init(initParams);

        this.contextPath = (String)initParams.get(CONTEXT_PATH_KEY);
        this.guiRootPath = (String)initParams.get(ROOT_PATH_KEY);
        this.welcomeFile = (String)initParams.get(WELCOME_FILE_KEY);

        addHandler(configureResourceHandler());
        addHandler(configureRootHandler());

        start();
    }

    private Handler configureResourceHandler() {
        ContextHandler context = new ContextHandler(this.contextPath);
        context.setClassLoader(Thread.currentThread().getContextClassLoader());

        ResourceHandler resourceHandler = new ResourceHandler();
        resourceHandler.setDirectoriesListed(false);
        resourceHandler.setWelcomeFiles(welcomeFiles());
        resourceHandler.setResourceBase(this.guiRootPath);

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