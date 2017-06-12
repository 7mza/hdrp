package ujm.wi.m1.conf;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

@Configuration
public class Config {

	// validate, update, create, create-drop
	private static final String HBM2DDL = "create-drop";

	// H2 config
	private static final Class<org.h2.Driver> DRIVER_H2 = org.h2.Driver.class;
	private static final Class<org.hibernate.dialect.H2Dialect> DIALECT_H2 = org.hibernate.dialect.H2Dialect.class;
	private static final Database DB_H2 = Database.H2;
	private static final String URL_H2 = "jdbc:h2:~/test";
	private static final String USER_H2 = "sa";
	private static final String PASS_H2 = "";

	@Bean
	public DriverManagerDataSource dataSource() {
		DriverManagerDataSource bean = new DriverManagerDataSource();
		bean.setDriverClassName(DRIVER_H2.getName());
		bean.setUrl(URL_H2);
		bean.setUsername(USER_H2);
		bean.setPassword(PASS_H2);
		return bean;
	}

	@Bean
	public Map<String, Object> jpaProperties() {
		Map<String, Object> props = new HashMap<String, Object>();
		props.put("hibernate.dialect", DIALECT_H2.getName());
		props.put("hibernate.hbm2ddl.auto", HBM2DDL);
		props.put("hibernate.show_sql", false);
		return props;
	}

	@Bean
	public JpaVendorAdapter jpaVendorAdapter() {
		HibernateJpaVendorAdapter bean = new HibernateJpaVendorAdapter();
		bean.setShowSql(true);
		bean.setGenerateDdl(true);
		bean.setDatabase(DB_H2);
		return bean;
	}

	@Bean
	public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
		LocalContainerEntityManagerFactoryBean bean = new LocalContainerEntityManagerFactoryBean();
		bean.setDataSource(this.dataSource());
		bean.setJpaPropertyMap(this.jpaProperties());
		bean.setJpaVendorAdapter(this.jpaVendorAdapter());
		return bean;
	}

	@Bean
	public PlatformTransactionManager transactionManager() {
		return new JpaTransactionManager(entityManagerFactory().getObject());
	}

	@Bean
	public InternalResourceViewResolver jspViewResolver() {
		InternalResourceViewResolver bean = new InternalResourceViewResolver();
		bean.setViewClass(org.springframework.web.servlet.view.JstlView.class);
		bean.setPrefix("WEB-INF/views/");
		bean.setSuffix(".jsp");
		return bean;
	}

	/*
	 * files uploading handler, max size 100MB
	 */
	@Bean
	public CommonsMultipartResolver multipartResolver() {
		CommonsMultipartResolver bean = new CommonsMultipartResolver();
		bean.setDefaultEncoding("UTF-8");
		bean.setMaxUploadSize(100000000);
		return bean;
	}

}
