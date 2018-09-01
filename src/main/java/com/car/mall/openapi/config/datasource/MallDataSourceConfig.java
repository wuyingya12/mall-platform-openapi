package com.car.mall.openapi.config.datasource;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.boot.autoconfigure.SpringBootVFS;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

@Configuration
@MapperScan(basePackages = {"com.car.mall.openapi.dao"}, sqlSessionFactoryRef = "stsdbSqlSessionFactory")
public class MallDataSourceConfig {

    static final String MAPPER_LOCATION = "classpath:mybatis/mapper/mall/*.xml";

    @Bean(name = "stsdbDataSource")
    @ConfigurationProperties(prefix = "datasource.stsdb")
    @Primary
    public DataSource hipiaoDataSource() {
        return new DruidDataSource();
    }

    @Bean(name = "stsdbTransactionManager")
    @Primary
    public DataSourceTransactionManager hipiaoTransactionManager() {
        return new DataSourceTransactionManager(hipiaoDataSource());
    }

    @Bean(name = "stsdbSqlSessionFactory")
    @Primary
    public SqlSessionFactory hipiaoSqlSessionFactory(@Qualifier("stsdbDataSource") DataSource hipiaoDataSource)
            throws Exception {
        final SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(hipiaoDataSource);
        sessionFactory.setVfs(SpringBootVFS.class);
        sessionFactory.setMapperLocations(new PathMatchingResourcePatternResolver()
                .getResources(MallDataSourceConfig.MAPPER_LOCATION));
        return sessionFactory.getObject();
    }

    @Bean(name = "stsdbJdbcTemplate")
    @Primary
    public JdbcTemplate jdbcTemplate(@Qualifier("stsdbDataSource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
}
