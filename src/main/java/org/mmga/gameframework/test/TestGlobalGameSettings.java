package org.mmga.gameframework.test;

import org.mmga.gameframework.annotations.game.Explain;
import org.mmga.gameframework.annotations.game.settings.Global;
import org.mmga.gameframework.annotations.game.settings.Save;
import org.mmga.gameframework.entities.GameSettings;

/**
 * Created On 2022/10/20 23:33
 *
 * @author MakeMinecraftGreatAgainTeam
 * @version 1.0.0
 */
@Global
public class TestGlobalGameSettings implements GameSettings {
    @Explain(content = "最大玩家数")
    @Save(path = "max_player")
    private int maxPlayer;
    @Explain(content = "最小玩家数")
    @Save(path = "min_player")
    private int minPlayer;
    @Explain(content = "数据库url")
    @Save(path = "db")
    private String dataBaseUrl;
}
