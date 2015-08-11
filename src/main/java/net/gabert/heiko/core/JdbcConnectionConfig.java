package net.gabert.heiko.core;

/**
 * Created by Family on 11. 8. 2015.
 */
public class JdbcConnectionConfig {
    private String password;
    private String username;
    private String jdbcUrl;

    public void setJdbcUrl(String jdbcUrl) {
        this.jdbcUrl = jdbcUrl;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }

    public String getJdbcUrl() {
        return jdbcUrl;
    }
}
