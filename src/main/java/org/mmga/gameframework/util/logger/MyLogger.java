package org.mmga.gameframework.util.logger;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.slf4j.Logger;

import java.sql.SQLException;

/**
 * Created On 2022/10/16 14:10
 *
 * @author MakeMinecraftGreatAgainTeam
 * @version 1.0.0
 */
@Data
public class MyLogger {
    private final Logger logger;
    public MyLogger(Logger logger){
        this.logger = logger;
    }
    //warn error info mysql
    public void warn(String s,Object... o){
        logger.warn(s, o);
    }
    public void error(String s,Object... o){
        logger.error(s, o);
    }
    public void info(String s,Object... o){
        logger.info(s, o);
    }
    public void errorOnSqlException(SQLException e){
        logger.info("SqlState = " + e.getSQLState());
        logger.info("ErrorMessage = " + e.getLocalizedMessage());
        logger.info("ErrorCode = " + e.getErrorCode());
    }
}
