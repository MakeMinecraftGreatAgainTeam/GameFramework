package org.mmga.gameframework.exception;

/**
 * Created On 2022/10/16 14:19
 *
 * @author xinsin
 * @version 1.0.0
 */
public class NotInitSingleton extends RuntimeException{

    public NotInitSingleton() {
        super();
    }
    public NotInitSingleton(String s) {
        super(s);
    }
}
