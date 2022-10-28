package org.mmga.gameframework.annotations.game.settings;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created On 2022/10/20 23:23
 *
 * @author MakeMinecraftGreatAgainTeam
 * @version 1.0.0
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Save {
    String path();
}
