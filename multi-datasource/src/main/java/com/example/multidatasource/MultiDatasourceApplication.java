package com.example.multidatasource;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.JdbcTemplateAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.annotation.Resource;
import javax.sql.DataSource;

@Slf4j
@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class,
		DataSourceTransactionManagerAutoConfiguration.class,
		JdbcTemplateAutoConfiguration.class})
public class MultiDatasourceApplication {

	public static void main(String[] args) {
		SpringApplication.run(MultiDatasourceApplication.class, args);
	}


	@Bean
	@ConfigurationProperties("data1.datasource")
	public DataSourceProperties fooDataSourceProperties() {
		return new DataSourceProperties();
	}

	@Primary
	@Bean
	public DataSource dataSource1() {
		DataSourceProperties dataSourceProperties = fooDataSourceProperties();
		log.info("data1 datasource: {}", dataSourceProperties.getUrl());
		return dataSourceProperties.initializeDataSourceBuilder().build();
	}

	@Bean
	@Resource
	public PlatformTransactionManager fooTxManager(DataSource dataSource1) {
		return new DataSourceTransactionManager(dataSource1);
	}

	@Bean
	@ConfigurationProperties("data2.datasource")
	public DataSourceProperties barDataSourceProperties() {
		return new DataSourceProperties();
	}

	@Bean
	public DataSource dataSource2() {
		DataSourceProperties dataSourceProperties = barDataSourceProperties();
		log.info("data2 datasource: {}", dataSourceProperties.getUrl());
		return dataSourceProperties.initializeDataSourceBuilder().build();
	}

	@Bean
	@Resource
	public PlatformTransactionManager barTxManager(DataSource dataSource2) {
		return new DataSourceTransactionManager(dataSource2);
	}
}
