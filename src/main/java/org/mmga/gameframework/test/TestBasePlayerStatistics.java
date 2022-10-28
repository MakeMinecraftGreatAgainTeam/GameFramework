package org.mmga.gameframework.test;

import org.mmga.gameframework.annotations.game.statistics.DataField;
import org.mmga.gameframework.annotations.game.statistics.DataNotNull;
import org.mmga.gameframework.annotations.game.statistics.PrimaryKey;
import org.mmga.gameframework.annotations.game.statistics.Table;
import org.mmga.gameframework.entities.BasePlayerStatistics;

/**
 * Created On 2022/10/22 10:46
 *
 * @author MakeMinecraftGreatAgainTeam
 * @version 1.0.0
 */
@Table(name = "test")
public class TestBasePlayerStatistics extends BasePlayerStatistics {
    @PrimaryKey
    @DataNotNull
    public int id;
    @DataField(name = "playerName")
    public String playerName;
    @DataField(name = "winCount")
    public int winCount;
    @DataField(name = "totalCount")
    public int totalCount;
    @DataField(name = "loseCount")
    public Integer loseCount;
}
