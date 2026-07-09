package kr.co.ibk.config;

import com.zaxxer.hikari.HikariDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

@Configuration
@MapperScan(value = "kr.co.ibk.repository", sqlSessionFactoryRef = "sqlSessionFactory")
public class WebDataSourceConfig {

    @Autowired
    private Environment env;

    @Primary
    @Bean(name = "dataSource")
    @ConfigurationProperties(prefix = "spring.datasource.web")
    public DataSource dataSource() {
        HikariDataSource dataSource = DataSourceBuilder.create().type(HikariDataSource.class).build();
        // maximum-pool, maximum-lifetime 설정 (프로퍼티 설정 값)
        dataSource.setMaximumPoolSize(env.getProperty("spring.datasource.hikari.maximum-pool-size", Integer.class));
        dataSource.setMaxLifetime(env.getProperty("spring.datasource.hikari.max-lifetime", Integer.class));
        return dataSource;
//        return DataSourceBuilder.create().build();
    }


    @Primary
    @Bean(name = "sqlSessionFactory")
    public SqlSessionFactory sqlSessionFactoryBean(@Autowired @Qualifier("dataSource") DataSource dataSource, ApplicationContext applicationContext)
            throws Exception {
        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
        factoryBean.setDataSource(dataSource);
        factoryBean.setConfigLocation(applicationContext.getResource("classpath:mybatis-config.xml"));
        factoryBean.setMapperLocations(applicationContext.getResources("classpath:mapper/**/*.xml"));
        return factoryBean.getObject();
    }

    @Primary
    @Bean(name = "sqlSession")
    public SqlSessionTemplate sqlSession(@Autowired @Qualifier("sqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

    @Primary
    @Bean(name = "transactionManager")
    public DataSourceTransactionManager transactionManager(@Autowired @Qualifier("dataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }
}
