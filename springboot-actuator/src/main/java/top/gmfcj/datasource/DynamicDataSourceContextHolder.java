package top.gmfcj.datasource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.gmfcj.enums.DataSourceType;

public class DynamicDataSourceContextHolder {

    public static final Logger log = LoggerFactory.getLogger(DynamicDataSourceContextHolder.class);
    private static final ThreadLocal<DataSourceType> CONTEXT_HOLDER = new ThreadLocal();

    public DynamicDataSourceContextHolder() {
    }

    public static void setDataSoureType(DataSourceType dsType) {
        log.info("切换到{}数据源", dsType);
        CONTEXT_HOLDER.set(dsType);
    }

    public static void master(){
        log.info("使用master进行写操作");
        CONTEXT_HOLDER.set(DataSourceType.MASTER);
    }

    public static void slave(){
        log.info("使用slave进行读操作");
        CONTEXT_HOLDER.set(DataSourceType.SLAVE);
    }

    public static DataSourceType getDataSoureType() {
        return CONTEXT_HOLDER.get();
    }

    public static void clearDataSoureType() {
        CONTEXT_HOLDER.remove();
    }

}
