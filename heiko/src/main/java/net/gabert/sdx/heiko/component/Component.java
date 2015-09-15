package net.gabert.sdx.heiko.component;

import java.util.Map;

/**
 * @author Robert Gallas
 */
public interface Component {
    void start(Map<String, Object> initParams);

    void stop();
}
