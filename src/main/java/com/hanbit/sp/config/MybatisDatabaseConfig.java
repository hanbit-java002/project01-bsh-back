package com.hanbit.sp.config;

import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.tomcat.jdbc.pool.DataSource;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.mysql.jdbc.Driver;

@Configuration
@EnableTransactionManagement
public class MybatisDatabaseConfig {
	
	@Autowired
	private ApplicationContext applicationContext;
	
	@Bean(destroyMethod="close")
	public DataSource dataSource() {
		DataSource dataSource = new DataSource();
		dataSource.setDriverClassName(Driver.class.getName());
		dataSource.setUrl("jdbc:mysql://localhost:3306/sooplace"); //스키마명
		dataSource.setConnectionProperties("characterEncoding=utf8;");
		dataSource.setUsername("hanbit"); //유저명
		dataSource.setPassword("hanbit"); //유저명
		
		return dataSource;
	}
	
	@Bean
	public PlatformTransactionManager transactionManager(DataSource dataSource) {
		return new DataSourceTransactionManager(dataSource);
	}
	
	@Bean
	public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception {
		SqlSessionFactoryBean sqlSessionFactory = new SqlSessionFactoryBean();
		sqlSessionFactory.setDataSource(dataSource);
		sqlSessionFactory.setConfigLocation(
				applicationContext.getResource("classpath:mybatis/mybatis-config.xml"));
		sqlSessionFactory.setMapperLocations(
				applicationContext.getResources("classpath:mybatis/mapper/**/*.xml"));
		
		return sqlSessionFactory.getObject();
	}
	
	@Bean
	public SqlSessionTemplate sqlSession(SqlSessionFactory sqlSessionFactory) {
		return new SqlSessionTemplate(sqlSessionFactory);
	}

}

