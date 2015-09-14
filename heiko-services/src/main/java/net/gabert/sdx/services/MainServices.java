package net.gabert.sdx.services;

import static spark.Spark.*;

import net.gabert.sdx.heiko.core.Controller;
import net.gabert.sdx.heiko.core.MappingService;
import net.gabert.sdx.services.endpoints.AddDeviceService;
import net.gabert.sdx.services.transformer.JsonTransformer;

public class MainServices {

	public static void main(String[] args) {
		Controller controller = Controller.boot();
        controller.shutDown();
        
        // port(5678); set different port then 4567
        
		post("/addDevice", (req, res) -> new AddDeviceService().call(req, res), new JsonTransformer());
		
		//TODO MappingService service = controller.getService(MappingService.class);
	}

}
