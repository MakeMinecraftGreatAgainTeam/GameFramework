package org.mmga.gameframework.annotations.game.statistics;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created On 2022/10/22 10:43
 *
 * @author MakeMinecraftGreatAgainTeam
 * @version 1.0.0
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Table {
    String name();

    String engine() default "InnoDB";

    String character() default "utf8mb4";

    String collate() default "utf8mb4_general_ci";

    String rowFormat() default "Dynamic";
}
