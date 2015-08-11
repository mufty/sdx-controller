package net.gabert.heiko.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Family on 11. 8. 2015.
 */
public class OracleDriver implements Driver {
    private List<JdbcConnectionConfig> configs = new ArrayList<>();

    public void setConfig(JdbcConnectionConfig... config) {
        configs.addAll(Arrays.asList(config));

    }
}
