package net.gabert.sdx.heiko.mountpoint;

import net.gabert.sdx.heiko.core.HeikoMessage;
import net.gabert.sdx.kyla.api.Endpoint;
import net.gabert.sdx.kyla.core.BusProxy;

public class HeikoRpcEndpoint extends Endpoint<HeikoMessage> {

    protected HeikoRpcEndpoint(BusProxy busProxy) {
        super(busProxy);
    }

    @Override
    public void handle(Message<HeikoMessage> message) {
//        Object result = driver.call(message.getData().absolutePath,
//                message.getData().payload);
//
//        HeikoMessage reply = new HeikoMessage<>();
//        reply.absolutePath = message.getData().absolutePath;
//        reply.payload = result;
//
//        this.send(message.createReply(reply));
    }
}

