
package com.mindslab.web.config;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import com.zaxxer.hikari.HikariDataSource;

@Configuration
@MapperScan(value = "com.mindslab.web.mapper.maria", sqlSessionFactoryRef = "mariaSqlSessionFactory")
public class MybatisMariaConfig{
	@Bean(name = "mariaDataSource")
	@Primary
	@ConfigurationProperties(prefix = "spring.maria.datasource")
	public DataSource mariaSessionFactoryDataSource(){
		return DataSourceBuilder.create().type(HikariDataSource.class).build();
	}

	@Bean(name = "mariaSqlSessionFactory")
	@Primary
	public SqlSessionFactory mariaSqlSessionFactory(
			@Qualifier("mariaDataSource") DataSource mariaDataSource)
			throws Exception{
		SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
		sessionFactory.setDataSource(mariaDataSource);

		PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
		sessionFactory.setMapperLocations(
				resolver.getResources("classpath:mybatis/mapper/maria/*.xml"));
		sessionFactory.setConfigLocation(
				resolver.getResource("classpath:mybatis/mybatis-config.xml"));

		return sessionFactory.getObject();
	}

	@Bean(name = "mariaSqlSessionTemplate")
	@Primary
	public SqlSessionTemplate mariaSqlSessionTemplate(
			SqlSessionFactory mariaSqlSessionFactory) throws Exception{

		return new SqlSessionTemplate(mariaSqlSessionFactory);
	}

}
