package net.gabert.sdx.services.endpoints;

import net.gabert.sdx.model.DeviceModel;
import spark.Request;
import spark.Response;

public class DeviceService implements ServiceIntreface {
	
	private DeviceModel model;
	
	public DeviceService(DeviceModel model) {
		this.model = model;
	}

	@Override
	public Object call(Request req, Response res) {
		return model.getDeviceName();
	}

}
