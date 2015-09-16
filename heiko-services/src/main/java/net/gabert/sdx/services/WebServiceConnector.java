package net.gabert.sdx.services;

import java.util.Map;

import net.gabert.sdx.heiko.component.Connector;
import net.gabert.sdx.heiko.ctx.Context;

import static spark.Spark.*;

public class WebServiceConnector extends Connector {
	
    private static String SERVICE_PORT_KEY = "servicePort";
    private static String SERVICE_ROOT_PATH_KEY = "rootPath";
    
    private int servicePortNumber;
    private String serviceRootPath = "/web/interface/device"; //default root path
    private Context ctx;

	@Override
	public void start(Map<String, Object> initParams) {
		this.servicePortNumber = ((Double)getParam(initParams, SERVICE_PORT_KEY)).intValue();
		
		Object rootPathParam = getParam(initParams, SERVICE_ROOT_PATH_KEY);
		if(rootPathParam != null && rootPathParam instanceof String)
			serviceRootPath = (String) rootPathParam;
		
		this.ctx  = getPathContext();
		
		start();
	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub
		
	}
	
	protected void start() {
		port(this.servicePortNumber);
		
	}
	
	protected Object getParam(Map<String, Object> initParams, String paramName) {
        if (initParams.containsKey(paramName) == false) {
            return null;
        } else {
            return initParams.get(paramName);
        }
    }

}
