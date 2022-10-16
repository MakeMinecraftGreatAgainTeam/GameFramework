package org.mmga.gameframework.util.jdbc;

import org.mmga.gameframework.util.StringConstant;

import java.io.File;

/**
 * Created On 2022/10/16 14:01
 *
 * @author xinsin
 * @version 1.0.0
 */
public class GameframeworkApplication {
    public static void run(Class<?> clazz){
        run(clazz,null);
    }
    public static void run(Class<?> clazz,String[] args){
        Package clazzPackage = clazz.getPackage();
        System.out.println(clazzPackage.getName().replace('.','/'));
        File file = new File(StringConstant.PATH + "/" + clazzPackage.getName().replace('.','/') + "/");
        System.out.println(file.getAbsolutePath());
        for (File packageFile : file.listFiles()) {
            System.out.println(packageFile.getAbsolutePath());
        }
    }
}
