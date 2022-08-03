package com.ay.common.datasource.druid.config;

import java.sql.SQLException;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import com.alibaba.druid.pool.DruidDataSource;
import com.github.pagehelper.PageInterceptor;

@Configuration
@MapperScan(basePackages = { "com.ay.**.mapper", "com.ay.**.dao", "com.yq.**.mapper", "com.yq.**.dao", "com.zny.**.mapper", "com.zny.**.dao"}, sqlSessionFactoryRef = "sqlSessionFactory")
public class DruidDataSourceConfig {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Value("${spring.datasource.driver-class-name}")
	private String driverClassName;

	@Value("${spring.datasource.url}")
	private String url;

	@Value("${spring.datasource.username}")
	private String username;

	@Value("${spring.datasource.password}")
	private String password;

	// 通过connectProperties属性来打开mergeSql功能；慢SQL记录
	@Value("${spring.datasource.connectionProperties}")
	private String connectionProperties;

	@Value("${spring.datasource.initialSize}")
	private int initialSize;

	@Value("${spring.datasource.minIdle}")
	private int minIdle;

	@Value("${spring.datasource.maxActive}")
	private int maxActive;

	@Value("${spring.datasource.maxWait}")
	private int maxWait; // 配置获取连接等待超时的时间

	@Value("${spring.datasource.validationQuery}")
	private String validationQuery;

	@Value("${spring.datasource.filters}")
	private String filters; // 配置监控统计拦截的filters，去掉后监控界面sql无法统计，'wall'用于防火墙

	@Value("${spring.datasource.testWhileIdle}")
	private boolean testWhileIdle;

	@Value("${spring.datasource.removeAbandoned:false}")
	private Boolean removeAbandoned;

	@Value("${spring.datasource.removeAbandonedTimeout:0}")
	private Integer removeAbandonedTimeout;

	@Value("${spring.datasource.logAbandoned:false}")
	private Boolean logAbandoned;

	@Value("${spring.datasource.time-between-eviction-runs-millis:60000}")
	private Long timeBetweenEvictionRunsMillis; // 配置间隔多久才进行一次检测，检测需要关闭的空闲连接，单位是毫秒

	@Value("${spring.datasource.keep-alive:false}")
	private Boolean keepAlive;

	@Value("${spring.datasource.pool-prepared-statements:false}")
	private Boolean poolPreparedStatements;

	@Value("${spring.datasource.max-pool-prepared-statement-per-connection-size:20}")
	private Integer maxPoolPreparedStatementPerConnectionSize;

	@Value("${spring.datasource.validation-query-timeout:10}")
	private int validationQueryTimeout;

	// @Value("${spring.jpa.properties.hibernate.show_sql}")
	// private String showSql;

	// @Value("${spring.jpa.properties.hibernate.connection.autoReconnect:true}")
	// private String autoReconnect;

	// @Value("${spring.jpa.properties.hibernate.connection.autoReconnectForPools:true}")
	// private String autoReconnectForPools;

	// @Value("${spring.jpa.properties.hibernate.connection.is-connection-validation-required:true}")
	// private String connectionValidRequired;

	@Bean("dataSource")
	@Primary
	public DataSource dataSource() {
		DruidDataSource dataSource = new DruidDataSource();
		dataSource.setDriverClassName(this.driverClassName);
		dataSource.setUrl(this.url);
		dataSource.setUsername(this.username);
		dataSource.setPassword(this.password);
		dataSource.setConnectionProperties(this.connectionProperties);
		// dataSource.setConnectionProperties("config.decrypt=false");
		dataSource.setInitialSize(this.initialSize);
		dataSource.setMinIdle(this.minIdle);
		dataSource.setMaxActive(this.maxActive);
		dataSource.setMaxWait(this.maxWait);
		dataSource.setValidationQuery(this.validationQuery);
		dataSource.setValidationQueryTimeout(this.validationQueryTimeout);
		dataSource.setTestWhileIdle(this.testWhileIdle);
		if (this.removeAbandoned != null) {
			dataSource.setRemoveAbandoned(this.removeAbandoned);
		}
		if (this.removeAbandonedTimeout != null && this.removeAbandonedTimeout != 0) {
			dataSource.setRemoveAbandonedTimeout(this.removeAbandonedTimeout);
		}
		if (this.logAbandoned != null) {
			dataSource.setLogAbandoned(this.logAbandoned);
		}
		dataSource.setTimeBetweenEvictionRunsMillis(this.timeBetweenEvictionRunsMillis);
		// 新版本增加参数
		if (this.keepAlive != null) {
			dataSource.setKeepAlive(this.keepAlive);
		}
		if (this.poolPreparedStatements != null) {
			dataSource.setPoolPreparedStatements(this.poolPreparedStatements);
		}
		if (this.maxPoolPreparedStatementPerConnectionSize != null) {
			dataSource.setMaxPoolPreparedStatementPerConnectionSize(this.maxPoolPreparedStatementPerConnectionSize);
		}
		try {
			dataSource.setFilters(filters);
			dataSource.init();
		} catch (SQLException e) {
			logger.error("druid configuration initialization filter", e);
		}
		return dataSource;
	}

	@Bean
	@Primary
	public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory) {
		return new SqlSessionTemplate(sqlSessionFactory);
	}

	@Bean(name = "sqlSessionFactory")
	@Primary
	public SqlSessionFactory sqlSessionFactory() throws Exception {
		SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
		sessionFactory.setDataSource(dataSource());
		// mybatis 分页
		PageInterceptor pageHelper = new PageInterceptor();
		Properties props = new Properties();
		props.setProperty("supportMethodsArguments", "true");
		props.setProperty("params", "pageNum=pageNumKey;pageSize=pageSizeKey;");
		pageHelper.setProperties(props);
		sessionFactory.setPlugins(new Interceptor[] { pageHelper });
		PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
		sessionFactory.setMapperLocations(resolver.getResources("classpath*:com/**/*Mapper.xml,classpath*:com/**/*Dao.xml"));
		sessionFactory.setConfigLocation(resolver.getResource("classpath:mybatis-config.xml"));
		return sessionFactory.getObject();
	}

	public String getUrl() {
		return url;
	}

}
