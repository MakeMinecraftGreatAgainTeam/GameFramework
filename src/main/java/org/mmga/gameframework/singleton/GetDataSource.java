package org.mmga.gameframework.singleton;

import org.mmga.gameframework.GameFramework;
import org.mmga.gameframework.exception.NotInitSingleton;
import org.mmga.gameframework.util.jdbc.DataSource;

/**
 * Created On 2022/10/16 14:07
 *
 * @author xinsin
 * @version 1.0.0
 */
public class GetDataSource {
    private volatile static DataSource dataSource;
    private GetDataSource(){

    }
    public static DataSource initDataSource(GameFramework gameFramework){
        if (dataSource == null){
            synchronized (GetDataSource.class){
                if (dataSource == null){
                    dataSource = new DataSource(gameFramework);
                }
            }
        }
        return dataSource;
    }
    public static DataSource getDataSource(){
        if (dataSource == null){
            throw new NotInitSingleton("not init GetDataSource Object");
        }else {
            return dataSource;
        }
    }
}
