package org.mmga.gameframework.util;

import com.alibaba.druid.pool.DruidPooledConnection;
import lombok.SneakyThrows;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.mmga.gameframework.annotations.game.Explain;
import org.mmga.gameframework.annotations.game.settings.Global;
import org.mmga.gameframework.annotations.game.settings.Save;
import org.mmga.gameframework.annotations.game.statistics.*;
import org.mmga.gameframework.entities.BaseGameData;
import org.mmga.gameframework.entities.BasePlayerStatistics;
import org.mmga.gameframework.entities.GameSettings;
import org.mmga.gameframework.exception.PrimaryKeyNullException;
import org.mmga.gameframework.exception.UnSupportTypeException;
import org.mmga.gameframework.singleton.GetDataSource;
import org.mmga.gameframework.util.jdbc.DataSource;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.net.URLClassLoader;
import java.sql.Statement;
import java.sql.Time;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import static org.mmga.gameframework.GameFramework.LOGGER;
import static org.mmga.gameframework.GameFramework.PLUGINS;

/**
 * Created On 2022/10/16 14:01
 *
 * @author xinsin
 * @version 1.0.0
 */
public class GameframeworkApplication {
    private final static Map<Plugin, Class<? extends BasePlayerStatistics>> STATISTICS_MAP = new HashMap<>();
    private final static Map<Plugin, Class<? extends BaseGameData>> GAMEDATA_MAP = new HashMap<>();
    private final static int DATETIME_LENGTH = 6;

    public static void run(Class<? extends JavaPlugin> clazz) {
        run(clazz, null);
    }

    /**
     * 注册一个GameSettings类
     *
     * @param aClass GameSettings类
     */
    private static void registerGameSettings(Class<?> aClass) {
        ClassLoader classLoader = aClass.getClassLoader();
        Plugin plugin = PLUGINS.get(classLoader);
        Global global = aClass.getAnnotation(Global.class);
        if (global != null) {
            LOGGER.info("插件" + plugin.getName() + "有全局游戏配置：");
        } else {
            LOGGER.info("插件" + plugin.getName() + "有游戏配置：");
        }
        String className = aClass.getName();
        LOGGER.info(className);
        Field[] fields = aClass.getDeclaredFields();
        for (Field field : fields) {
            String name = field.getName();
            Save save = field.getAnnotation(Save.class);
            Explain explain = field.getAnnotation(Explain.class);
            if (save == null) {
                LOGGER.warn("字段{} 未找到@Save(path = ?)注解", name);
                continue;
            }
            if (explain == null) {
                LOGGER.warn("字段{} 未找到@Explain(content = ?)注解", name);
                continue;
            }
            Class<?> type = field.getType();
            LOGGER.info("有值{},类型：{},保存路径：{},解释：{}", name, type.getName(), save.path(), explain.content());
        }
    }

    private static void registerPlayerStatistics(BasePlayerStatistics basePlayerStatistics) {
        Class<? extends BasePlayerStatistics> aClass = basePlayerStatistics.getClass();
        ClassLoader classLoader = aClass.getClassLoader();
        Plugin plugin = PLUGINS.get(classLoader);
    }

    /**
     * 处理单个属性
     *
     * @param field  属性
     * @param maxLen 长度
     * @return 此类型sql
     */
    private static String handlerField(Field field, int maxLen) {
        Class<?> type = field.getType();
        String typeSql;
        if (type.equals(int.class) || type.equals(Integer.class)) {
            typeSql = "int";
        } else if (type.equals(float.class) || type.equals(Float.class)) {
            typeSql = "float";
        } else if (type.equals(double.class) || type.equals(Double.class)) {
            typeSql = "double";
        } else if (type.equals(long.class) || type.equals(Long.class)) {
            typeSql = "bigint";
        } else if (type.equals(short.class) || type.equals(Short.class)) {
            typeSql = "tinyint";
        } else if (type.equals(char.class) || type.equals(Character.class)) {
            typeSql = "tinytext";
        } else if (type.equals(byte.class) || type.equals(Byte.class)) {
            typeSql = "varbinary(" + maxLen + ")";
        } else if (type.equals(boolean.class) || type.equals(Boolean.class)) {
            typeSql = "bit";
        } else if (CharSequence.class.isAssignableFrom(type)) {
            typeSql = "varchar(" + maxLen + ")";
        } else if (type.equals(Time.class)) {
            if (maxLen > DATETIME_LENGTH) {
                LOGGER.warn("too big length {} for datetime, change to 6", maxLen);
                maxLen = DATETIME_LENGTH;
            }
            typeSql = "datetime(" + maxLen + ")";
        } else {
            throw new UnSupportTypeException(type);
        }
        return typeSql;
    }

    /**
     * 获取一个GameData类的建表SQL
     *
     * @param clazz GameData类
     * @return 建表SQL
     */
    private static String getCreateTableSql(Class<?> clazz, String pluginName) {
        Table table = clazz.getAnnotation(Table.class);
        if (table != null) {
            boolean hasPrimaryKey = false;
            String primaryKey = "";
            String tableName = pluginName + "_" + table.name();
            Field[] fields = clazz.getDeclaredFields();
            StringBuilder totalSql = new StringBuilder();
            totalSql.append("CREATE TABLE `").append(tableName).append("`(");
            for (Field field : fields) {
                int modifiers = field.getModifiers();
                if (!Modifier.isPrivate(modifiers)) {
                    int maxLen = 255;
                    MaxLength maxLength = field.getAnnotation(MaxLength.class);
                    if (maxLength != null) {
                        maxLen = maxLength.value();
                    }
                    DataField dataField = field.getAnnotation(DataField.class);
                    String fieldName = field.getName();
                    if (dataField != null) {
                        fieldName = dataField.name();
                    }
                    boolean isPrimaryKey = field.getAnnotation(PrimaryKey.class) != null;
                    boolean isNotNull = field.getAnnotation(DataNotNull.class) != null;
                    if (isPrimaryKey) {
                        if (isNotNull) {
                            if (!hasPrimaryKey) {
                                hasPrimaryKey = true;
                                primaryKey = fieldName;
                            } else {
                                LOGGER.error("Already have primaryKey {}", primaryKey);
                            }
                        } else {
                            throw new PrimaryKeyNullException(fieldName, tableName);
                        }
                    }
                    String typeSql = handlerField(field, maxLen);
                    String thisLine;
                    if (isNotNull) {
                        thisLine = "`" + fieldName + "` " + typeSql + " NOT NULL,";
                    } else {
                        thisLine = "`" + fieldName + "` " + typeSql + " NULL,";
                    }
                    totalSql.append(" ").append(thisLine);
                }
            }
            totalSql.deleteCharAt(totalSql.lastIndexOf(","));
            if (hasPrimaryKey) {
                totalSql.append(", ").append("PRIMARY KEY (`").append(primaryKey).append("`) USING BTREE");
            }
            totalSql.append(")ENGINE = ").append(table.engine()).append(" CHARACTER SET = ").append(table.character()).append(" COLLATE = ").append(table.collate()).append(" ROW_FORMAT = ").append(table.rowFormat()).append(";");
            return totalSql.toString();
        }
        return null;
    }

    /**
     * 注册一个GameData类
     *
     * @param aClass GameData类
     */
    @SneakyThrows
    private static void registerGameData(Class<?> aClass) {
        ClassLoader classLoader = aClass.getClassLoader();
        Plugin plugin = PLUGINS.get(classLoader);
        String name = plugin.getName();
        String className = aClass.getName();
        Table table = aClass.getAnnotation(Table.class);
        if (table == null) {
            LOGGER.warn("插件{}的游戏数据类{}未找到@Table(name = ?)注解", name, className);
            return;
        }
        String createTableSql = getCreateTableSql(aClass, name);
        DataSource dataSource = GetDataSource.getDataSource();
        DruidPooledConnection connection = dataSource.getConnection();
        Statement statement = connection.createStatement();
        statement.executeUpdate(createTableSql);
        statement.close();
        connection.close();
        LOGGER.info("为游戏数据类{}建表{}", className, name + "_" + table.name());
    }

    public static void run(Class<? extends JavaPlugin> clazz, String[] args) {
        ClassLoader classLoader = clazz.getClassLoader();
        if (classLoader instanceof URLClassLoader) {
            URL url = ((URLClassLoader) classLoader).getURLs()[0];
            String file = url.getFile();
            File jarFile = new File(file);
            JarFile jarf;
            try {
                jarf = new JarFile(jarFile);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            String name = clazz.getPackage().getName().replace('.', '/');
            Enumeration<JarEntry> entries = jarf.entries();
            while (entries.hasMoreElements()) {
                JarEntry jarEntry = entries.nextElement();
                String entryName = jarEntry.getName();
                if (entryName.contains(name) && entryName.contains(".class")) {
                    Class<?> aClass;
                    String className = entryName.replace('/', '.').replace(".class", "");
                    try {
                        aClass = classLoader.loadClass(className);
                    } catch (ClassNotFoundException ignored) {
                        LOGGER.warn("加载类{}时出现错误：类未找到", className);
                        continue;
                    }
                    if (GameSettings.class.isAssignableFrom(aClass) && !aClass.equals(GameSettings.class)) {
                        registerGameSettings(aClass);
                    }
                    if (BaseGameData.class.isAssignableFrom(aClass) && !aClass.equals(BaseGameData.class) && !aClass.equals(BasePlayerStatistics.class)) {
                        registerGameData(aClass);
                    }
                }
            }
            try {
                jarf.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
