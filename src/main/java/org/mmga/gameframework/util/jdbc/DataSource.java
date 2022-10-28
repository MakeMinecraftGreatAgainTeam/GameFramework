package org.mmga.gameframework.util.jdbc;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidPooledConnection;
import org.bukkit.configuration.file.FileConfiguration;
import org.mmga.gameframework.GameFramework;

import java.sql.SQLException;
import java.util.logging.Logger;

/**
 * Created On 2022/10/16 14:02
 *
 * @author MakeMinecraftGreatAgainTeam
 * @version 1.0.0
 */
public class DataSource extends DruidDataSource {
    private final GameFramework plugin;
    public static final String MYSQL = "mysql";
    public DataSource(GameFramework plugin) {
        this.plugin = plugin;
        Logger logger = plugin.getLogger();
        FileConfiguration config = plugin.getConfig();
        String type = config.getString("datasource.type");
        if (MYSQL.equals(type)) {
            String host = config.getString("datasource.host");
            String port = config.getString("datasource.port");
            String db = config.getString("datasource.db");
            String user = config.getString("datasource.user");
            String pass = config.getString("datasource.pass");
            this.setUrl(String.format("jdbc:mysql://%s:%s/%s", host, port, db));
            this.setUsername(user);
            this.setPassword(pass);
            this.setDriverClassName("com.mysql.cj.jdbc.Driver");
        }
    }
    public void reloadConfig() {
        this.plugin.reloadConfig();
        for (DruidPooledConnection activeConnection : this.getActiveConnections()) {
            try {
                activeConnection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        FileConfiguration config = this.plugin.getConfig();
        String type = config.getString("datasource.type");
        if (MYSQL.equals(type)) {
            String host = config.getString("datasource.host");
            String port = config.getString("datasource.port");
            String db = config.getString("datasource.db");
            String user = config.getString("datasource.user");
            String pass = config.getString("datasource.pass");
            this.setUrl(String.format("jdbc:mysql://%s:%s/%s", host, port, db));
            this.setUsername(user);
            this.setPassword(pass);
            this.setDriverClassName("com.mysql.cj.jdbc.Driver");
        }
        this.resetStat();
    }
}
