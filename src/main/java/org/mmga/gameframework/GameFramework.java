package org.mmga.gameframework;

import org.bukkit.Server;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.mmga.gameframework.singleton.GetDataSource;
import org.mmga.gameframework.util.GameframeworkApplication;
import org.mmga.gameframework.util.jdbc.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author MakeMinecraftGreatAgainTeam
 * @version 1.0.0
 */
public final class GameFramework extends JavaPlugin {
    public static final Map<ClassLoader, Plugin> PLUGINS = new HashMap<>();
    public static final Logger LOGGER = LoggerFactory.getLogger("GameFramework");
    public void registerApplication(Class<? extends JavaPlugin> clazz){
        GameframeworkApplication.run(clazz);
    }

    private DataSource dataSource;
    @Override
    public void onEnable() {
        // Plugin startup logic
        this.saveDefaultConfig();
        dataSource = GetDataSource.initDataSource(this);
        Server server = this.getServer();
        PluginManager pluginManager = server.getPluginManager();
        Plugin[] plugins = pluginManager.getPlugins();
        PluginDescriptionFile myDescription = this.getDescription();
        String name = myDescription.getName();
        for (Plugin plugin : plugins) {
            PluginDescriptionFile description = plugin.getDescription();
            List<String> depend = description.getDepend();
            if (depend.contains(name)) {
                ClassLoader classLoader = plugin.getClass().getClassLoader();
                PLUGINS.put(classLoader, plugin);
            }
        }
        PLUGINS.put(this.getClassLoader(), this);
        this.registerApplication(this.getClass());
        LOGGER.info("GameFrameworkLoaded!");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        dataSource.close();
    }
}
