package org.mmga.gameframework.annotations.game;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created On 2022/10/16 15:38
 *
 * @author MakeMinecraftGreatAgainTeam
 * @version 1.0.0
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Explain {
    String[] content();
}
