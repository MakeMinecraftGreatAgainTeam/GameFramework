package org.mmga.gameframework.exception;

/**
 * Created On 2022/10/23 22:29
 *
 * @author MakeMinecraftGreatAgainTeam
 * @version 1.0.0
 */
public class UnSupportTypeException extends RuntimeException {
    public UnSupportTypeException(Class<?> clazz) {
        super(clazz.getTypeName());
    }
}
