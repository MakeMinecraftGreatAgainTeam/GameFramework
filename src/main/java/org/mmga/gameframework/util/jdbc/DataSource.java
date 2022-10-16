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
    private final Logger logger;
    public DataSource(GameFramework plugin){
        this.plugin = plugin;
        this.logger = plugin.getLogger();
        FileConfiguration config = plugin.getConfig();
    }
    public void reloadConfig(){
        this.plugin.reloadConfig();
        for (DruidPooledConnection activeConnection : this.getActiveConnections()) {
            try {
                activeConnection.close();
            } catch (SQLException e) {
            }
        }
    }
}
