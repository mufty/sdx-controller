package net.gabert.sdx.heiko.ctx;

import net.gabert.sdx.heiko.component.Callback;
import net.gabert.sdx.heiko.component.Service;
import net.gabert.sdx.heiko.core.Controller;
import net.gabert.sdx.heiko.core.HeikoMessage;
import net.gabert.sdx.heiko.core.MappingService;
import net.gabert.sdx.heiko.mountpoint.ComponentMountPoint;
import net.gabert.sdx.kyla.api.Endpoint;
import net.gabert.util.LogUtil;
import org.slf4j.Logger;

/**
 * @author Robert Gallas
 */
final class DeviceContext extends Context {
    private static final Logger LOGGER = LogUtil.getLogger();

    private final MappingService mappingService;

    private final String contextRoot;

    private final String dataSlotId;

    DeviceContext(String contextRoot, ComponentMountPoint mountPoint) {
        super(mountPoint);
        this.contextRoot = contextRoot;
        this.mappingService = Controller.getService(MappingService.class);
        this.dataSlotId = mappingService.resolveDataSlotId(contextRoot);
    }

    @Override
    public void setValue(String contextRelativePath, Object value) {
        LOGGER.debug("SET Value Async: {}{} -> {}", contextRoot, contextRelativePath, value);
        Endpoint.Message kylaMessage = toKylaMessage(dataSlotId,
                                                     toHeikoMessage(contextRelativePath,
                                                                    HeikoMessage.Type.SET,
                                                                    value));

        mountPoint.send(kylaMessage);
    }

    @Override
    public void setValue(String contextRelativePath, Object value, Callback callback) {

    }

    @Override
    public void getValue(String contextRelativePath, Callback callback) {

    }

    @Override
    public void call(String contextRelativePath, Object[] params, Callback callback) {

    }

    private HeikoMessage toHeikoMessage(String contextRelativePath, HeikoMessage.Type type, Object payload) {
        HeikoMessage heikoMessage = new HeikoMessage();
        heikoMessage.mountPointRelativePath = contextRelativePath;
        heikoMessage.type = type;
        heikoMessage.payload = payload;

        return heikoMessage;
    }
}
