package net.gabert.sdx.heiko.ctx;

import net.gabert.sdx.heiko.component.Callback;
import net.gabert.sdx.heiko.component.Service;
import net.gabert.sdx.heiko.component.ValueListener;
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
final class PathContext extends Context {
    private static final Logger LOGGER = LogUtil.getLogger();

    private final MappingService mappingService;

    private final String contextRoot;

    PathContext(String contextRoot, ComponentMountPoint mountPoint) {
        super(mountPoint);
        this.contextRoot = contextRoot;
        this.mappingService = Controller.getService(MappingService.class);
    }

    @Override
    public void setValue(String contextRelativePath, Object value) {
        LOGGER.debug("SET Value: {}{} -> {}", contextRoot, contextRelativePath, value);

        String absolutePath = toAbsolutePath(contextRelativePath);

        Endpoint.Message kylaMessage = toKylaMessage(mappingService.resolveDataSlotId(absolutePath),
                                                     toHeikoMessage(absolutePath,
                                                                    HeikoMessage.Type.SET,
                                                                    value));

        mountPoint.send(kylaMessage);
    }

    @Override
    public void setValue(String contextRelativePath, Object value, Callback callback) {
        LOGGER.debug("SET Value: {}{} -> {}", contextRoot, contextRelativePath, value);

        String absolutePath = toAbsolutePath(contextRelativePath);

        Endpoint.Message kylaMessage = toKylaMessage(mappingService.resolveDataSlotId(absolutePath),
                                                     toHeikoMessage(absolutePath,
                                                                    HeikoMessage.Type.SET_ACK,
                                                                    value));

        mountPoint.send(kylaMessage, callback);
    }

    @Override
    public void getValue(String contextRelativePath, Callback callback) {
        LOGGER.debug("GET Value: {}{}", contextRoot, contextRelativePath);

        String absolutePath = toAbsolutePath(contextRelativePath);

        Endpoint.Message kylaMessage = toKylaMessage(mappingService.resolveDataSlotId(absolutePath),
                                                     toHeikoMessage(absolutePath,
                                                                    HeikoMessage.Type.GET,
                                                                    null));

        mountPoint.send(kylaMessage, callback);
    }

    @Override
    public void call(String contextRelativePath, Object[] params, Callback callback) {
        LOGGER.debug("CALL Value: {}{}", contextRoot, contextRelativePath);

        String absolutePath = toAbsolutePath(contextRelativePath);

        Endpoint.Message kylaMessage = toKylaMessage(mappingService.resolveDataSlotId(absolutePath),
                                                     toHeikoMessage(absolutePath,
                                                                    HeikoMessage.Type.CALL,
                                                                    params));

        mountPoint.send(kylaMessage, callback);
    }

    @Override
    public void registerListener(String path, ValueListener valueListener) {
        mountPoint.registerListener(toAbsolutePath(path), valueListener);
    }

    private String toAbsolutePath(String contextRelativePath) {
        return contextRoot + contextRelativePath;
    }

    private HeikoMessage toHeikoMessage(String absolutePath, HeikoMessage.Type type, Object payload) {
        HeikoMessage heikoMessage = new HeikoMessage();
        heikoMessage.mountPointRelativePath = Controller.getService(MappingService.class)
                                                        .getMountPointRelativePath(absolutePath);
        heikoMessage.type = type;
        heikoMessage.payload = payload;

        return heikoMessage;
    }
}
