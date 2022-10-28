package org.mmga.gameframework.exception;

/**
 * Created On 2022/10/23 23:16
 *
 * @author MakeMinecraftGreatAgainTeam
 * @version 1.0.0
 */
public class PrimaryKeyNullException extends RuntimeException {
    public PrimaryKeyNullException(String name, String table) {
        super("a primary key" + name + "is null in table" + table);
    }
}
