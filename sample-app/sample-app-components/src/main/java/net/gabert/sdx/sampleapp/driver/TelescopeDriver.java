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

    private final SimpleDB db = new SimpleDB("/azimuth", "/altitude");

    @Override
    public Object getValue(String path) {
        LOGGER.info("Telescope Driver: GET, Path: {}, Returns: {}", path, db.getValue(path));
        return db.getValue(path);
    }

    @Override
    public void setValue(String path, Object value) {
        LOGGER.info("Telescope Driver: SET, Path: {}, Value: {}", path, value);
        db.setValue(path, value);
    }

    @Override
    public void onListenerRegistered(String path) {
    }

    @Override
    public Object call(String path, Object[] params) {
        LOGGER.debug("Telescope Driver: CALL, Path: {}", path);
        if (path.equals("/snapshot")) {
            return snapshot((int)params[0], (int)params[1], (int)params[2]);
        } else {
            return null;
        }
    }

    private Object snapshot(int shutterSpeed, int aperture, int iso) {
        return "Andromeda galaxy snapshot with shutter speed: 1/" + shutterSpeed +
               ", aperture: " + aperture +
               ", iso: " + iso + ".";
    }
}
