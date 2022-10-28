package org.mmga.gameframework.exception;

/**
 * Created On 2022/10/16 14:19
 *
 * @author xinsin
 * @version 1.0.0
 */
public class NotInitSingletonException extends RuntimeException {
    public NotInitSingletonException(String s) {
        super(s);
    }
}
