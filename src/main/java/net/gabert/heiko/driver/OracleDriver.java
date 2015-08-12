package net.gabert.heiko.driver;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class OracleDriver {
    private List<JdbcConnectionConfig> configs = new ArrayList<>();

    public void setConfig(JdbcConnectionConfig... config) {
        configs.addAll(Arrays.asList(config));

    }
}
