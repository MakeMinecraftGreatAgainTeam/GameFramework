package org.mmga.gameframework.test;

import org.bukkit.Location;
import org.mmga.gameframework.annotations.game.Explain;
import org.mmga.gameframework.annotations.game.settings.Save;
import org.mmga.gameframework.entities.GameSettings;

/**
 * Created On 2022/10/16 16:27
 *
 * @author MakeMinecraftGreatAgainTeam
 * @version 1.0.0
 */
public class TestGameSettings implements GameSettings {
    @Explain(content = "游戏名")
    @Save(path = "name")
    private String name;
    @Explain(content = "游戏起始点")
    @Save(path = "start")
    private Location location;
}
