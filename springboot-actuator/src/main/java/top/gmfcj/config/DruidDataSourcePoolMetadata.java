package top.gmfcj.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.boot.jdbc.metadata.AbstractDataSourcePoolMetadata;

/**
 * @description: 监控数据库连接池
 * @create: 2021-01-05 13:29
 */
public class DruidDataSourcePoolMetadata extends AbstractDataSourcePoolMetadata<DruidDataSource> {
    public DruidDataSourcePoolMetadata(DruidDataSource dataSource) {
        super(dataSource);
    }

    @Override
    public Integer getActive() {
        try {
            return getDataSource().getActiveCount();
        }catch (Exception ex) {
            return 0;
        }
    }

    @Override
    public Integer getMax() {
        return getDataSource().getMaxActive();
    }

    @Override
    public Integer getMin() {
        return getDataSource().getMinIdle();
    }

    @Override
    public String getValidationQuery() {
        return getDataSource().getValidationQuery();
    }

    @Override
    public Boolean getDefaultAutoCommit() {
        return getDataSource().isDefaultAutoCommit();
    }
}
