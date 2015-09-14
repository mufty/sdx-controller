package net.gabert.sdx.services.endpoints;

import static spark.Spark.post;

import org.slf4j.Logger;

import net.gabert.sdx.model.DeviceModel;
import net.gabert.util.LogUtil;
import spark.Request;
import spark.Response;

public class AddDeviceService implements ServiceIntreface {
	
	private static final Logger LOGGER = LogUtil.getLogger();
	
	private String address;

	@Override
	public DeviceModel call(Request req, Response res) {
		address = req.scheme() + "://" + req.host();
		DeviceModel model = new DeviceModel(req);
		createEndpoint(model);
		return model;
	}

	protected void createEndpoint(DeviceModel model) {
		String servicePath = model.getDeviceName() + "/" + model.getDevicePath(); 
		post(servicePath, (req, res) -> new DeviceService(model).call(req, res));
		LOGGER.info("Created Endpoint with path: {}", address + "/" + servicePath);
	}

}
