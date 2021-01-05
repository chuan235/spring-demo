package top.gmfcj.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.DataSourceUnwrapper;
import org.springframework.boot.jdbc.metadata.DataSourcePoolMetadata;
import org.springframework.boot.jdbc.metadata.DataSourcePoolMetadataProvider;
import org.springframework.stereotype.Component;
import top.gmfcj.datasource.DynamicDataSource;

import javax.sql.DataSource;

@Component
public class DruidDataSourcePoolMetadataProvider implements DataSourcePoolMetadataProvider {

    @Override
    public DataSourcePoolMetadata getDataSourcePoolMetadata(DataSource dataSource) {
        DruidDataSource ds = DataSourceUnwrapper.unwrap(dataSource, DruidDataSource.class);
        if (ds != null) {
            return new DruidDataSourcePoolMetadata(ds);
        }
        return null;
    }
}
