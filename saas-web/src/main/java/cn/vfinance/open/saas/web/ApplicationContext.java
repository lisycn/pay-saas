package cn.vfinance.open.saas.web;

import cn.vfinance.open.saas.web.util.ExternalConfig;
import com.netfinworks.ufs.client.UFSClient;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.client.RestTemplate;

import javax.sql.DataSource;


@SpringBootApplication
@EnableAutoConfiguration
@MapperScan("cn.vfinance.open.saas.web.dao")
@ComponentScan(basePackages = {"cn.vfinance.open.saas.web"})
@EnableTransactionManagement()
public class ApplicationContext  extends SpringBootServletInitializer {

    @Autowired
    DataSource dataSource;

    public static void main(String[] args) {
        SpringApplication.run(ApplicationContext.class, args);
    }

    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(getClass());
    }

    //提供SqlSeesion
    @Bean
    public SqlSessionFactory sqlSessionFactoryBean() throws Exception {

        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource);

        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();

        sqlSessionFactoryBean.setMapperLocations(resolver.getResources("classpath:*/*Mapper.xml"));

        return sqlSessionFactoryBean.getObject();
    }

    @Bean
    public PlatformTransactionManager transactionManager() {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Autowired
    ExternalConfig externalConfig;

    @Bean
    public UFSClient ufsClient() {
        String user = externalConfig.getUfsUser();
        String password = externalConfig.getUfsPwd();
//        String host = externalConfig.getUfsHost();
        String serverFullPath = externalConfig.getUfsServerFullPath();

//        UFSClient ufsClient = new UFSClient(user, password, host);
        UFSClient ufsClient = new UFSClient();
        ufsClient.setUser(user);
        ufsClient.setPassword(password);
        ufsClient.setServerFullPath(serverFullPath);
        return ufsClient;
    }
}
