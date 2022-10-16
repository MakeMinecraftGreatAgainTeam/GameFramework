package org.mmga.gameframework;

import org.bukkit.Server;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.mmga.gameframework.entities.GameSettings;
import org.mmga.gameframework.test.TestGameSettings;
import org.mmga.gameframework.util.jdbc.DataSource;
import org.mmga.gameframework.util.jdbc.GameframeworkApplication;
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
    public static DataSource DATA_SOURCE;
    public static final Map<ClassLoader, Plugin> PLUGINS = new HashMap<>();
    private static final Logger LOGGER = LoggerFactory.getLogger("GameFramework");
    public void registerApplication(Class<? extends JavaPlugin> clazz){
        GameframeworkApplication.run(clazz);
    }
    public void registerGameSettings(GameSettings settings){
        ClassLoader classLoader = settings.getClass().getClassLoader();
        Plugin plugin = PLUGINS.get(classLoader);

    }
    @Override
    public void onEnable() {
        // Plugin startup logic
        DATA_SOURCE = new DataSource(this);
        Server server = this.getServer();
        PluginManager pluginManager = server.getPluginManager();
        Plugin[] plugins = pluginManager.getPlugins();
        PluginDescriptionFile myDescription = this.getDescription();
        String name = myDescription.getName();
        for (Plugin plugin : plugins) {
            PluginDescriptionFile description = plugin.getDescription();
            List<String> depend = description.getDepend();
            if (depend.contains(name)){
                ClassLoader classLoader = plugin.getClass().getClassLoader();
                PLUGINS.put(classLoader, plugin);
            }
        }
        PLUGINS.put(this.getClassLoader(), this);
        this.registerGameSettings(new TestGameSettings());
        this.registerApplication(this.getClass());
        LOGGER.info("GameFrameworkLoaded!");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
