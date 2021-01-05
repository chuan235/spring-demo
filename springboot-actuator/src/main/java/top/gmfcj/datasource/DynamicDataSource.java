package top.gmfcj.datasource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import top.gmfcj.enums.DataSourceType;

import javax.sql.DataSource;
import java.util.Map;

public class DynamicDataSource extends AbstractRoutingDataSource {

    private static final Logger LOGGER = LoggerFactory.getLogger(DynamicDataSource.class);

    public DynamicDataSource(DataSource defaultTargetDataSource, Map<Object, Object> targetDataSources) {
        super.setDefaultTargetDataSource(defaultTargetDataSource);
        super.setTargetDataSources(targetDataSources);
        super.afterPropertiesSet();
    }

    @Override
    protected Object determineCurrentLookupKey() {
        /**
         * @see AbstractRoutingDataSource#determineTargetDataSource()
         */
        DataSourceType typeKey = DynamicDataSourceContextHolder.getDataSoureType();
        if(typeKey == DataSourceType.MASTER){
            LOGGER.info("使用了master - 写库");
        }else{
            LOGGER.info("使用了slave - 读库");
        }
        return typeKey;
    }

}
