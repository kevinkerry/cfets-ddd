package com.cfets.ts.u.platform;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import com.cfets.ts.u.platform.filter.Log4jFilter;

/**
 * zrp测试1111111111111111 Created by Dadepo Aderemi.
 */
@Configuration
public class AppConfiguration {
	private static final Logger logger = Logger
			.getLogger(AppConfiguration.class);

	@Bean
	public DataSource dataSource() {
		return DataSourceBuilder.create().username("tsdev").password("tsdev")
				.url("jdbc:oracle:thin:@200.31.156.230:1521:dev2")
				.driverClassName("oracle.jdbc.driver.OracleDriver").build();

	}

	@Bean
	public JdbcTemplate getJdbcTemplate() {
		return new JdbcTemplate(dataSource());
	}

	@Bean
	public FilterRegistrationBean registerBean() {
		FilterRegistrationBean registionBean = new FilterRegistrationBean(
				new Log4jFilter());
		registionBean.addUrlPatterns("/");
		return registionBean;
	}

}
