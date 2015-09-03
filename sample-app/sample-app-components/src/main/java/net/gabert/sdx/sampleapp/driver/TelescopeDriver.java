package net.gabert.sdx.sampleapp.driver;

import net.gabert.sdx.heiko.spi.Driver;
import net.gabert.util.LogUtil;
import org.slf4j.Logger;

/**
 *
 * @author Robert Gallas
 */
public class TelescopeDriver extends Driver {
    private static final Logger LOGGER = LogUtil.getLogger();

    private int azimuth;
    private int altitude;

    private final SimpleValueHolder db = new SimpleValueHolder("/azimuth", "/altitude");

    @Override
    public Object getValue(String path) {
        LOGGER.debug("Telescope Driver: GET, Path: {}, Returns: {}", path, db.getValue(path));
        return db.getValue(path);

    }

    @Override
    public void setValue(String path, Object value) {
        LOGGER.debug("Telescope Driver: SET, Path: {}, Value: {}", path, value);
        db.setValue(path, value);
    }

    @Override
    public void onListenerRegistered(String path) {
    }

    @Override
    public Object call(String path, Object... params) {
        return null;
    }
}
