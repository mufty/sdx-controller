package net.gabert.sdx.services.endpoints;

import spark.Request;
import spark.Response;

public interface ServiceIntreface {

	public Object call(Request req, Response res);
	
}
