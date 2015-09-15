package net.gabert.sdx.heiko.mountpoint;

import net.gabert.sdx.heiko.component.Connector;
import net.gabert.sdx.heiko.configuration.schema.ConnectorConfig;
import net.gabert.sdx.heiko.core.HeikoMessage;
import net.gabert.sdx.kyla.core.BusProxy;
import net.gabert.util.LogUtil;
import org.slf4j.Logger;

/**
 * @author Robert Gallas
 */
public class ConnectorMountPoint extends ComponentMountPoint<Connector> {
    private static final Logger LOGGER = LogUtil.getLogger();

    private ConnectorMountPoint(BusProxy busProxy, ConnectorConfig connectorConfig) {
        super(busProxy, connectorConfig.connectorClass, connectorConfig);

        LOGGER.info("Initializing connector: {}", getPlainDataSlotId());
    }

    private ConnectorMountPoint(String dataSlotId, BusProxy busProxy, ConnectorConfig connectorConfig) {
        super(dataSlotId, busProxy, connectorConfig.connectorClass, connectorConfig);

        LOGGER.info("Initializing connector: {}", getPlainDataSlotId());
    }

    public static ConnectorMountPoint newInstance(BusProxy busProxy, ConnectorConfig connectorConfig) {
        return connectorConfig.id == null ? new ConnectorMountPoint(busProxy, connectorConfig)
                                          : new ConnectorMountPoint(connectorConfig.id, busProxy, connectorConfig);
    }

    @Override
    public void handle(Message<HeikoMessage> kylaMessage) {
        possiblyHandleCallback(kylaMessage);
    }
}
