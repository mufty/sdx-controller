package net.gabert.sdx.services;

import static spark.Spark.post;

import net.gabert.sdx.services.endpoints.AddDeviceService;
import net.gabert.sdx.services.transformer.JsonTransformer;

public class MainServices {

	public static void main(String[] args) {
		post("/addDevice", (req, res) -> new AddDeviceService().call(req, res), new JsonTransformer());
	}

}
