package levit104.blss.labs.config.transactions;

import com.atomikos.spring.AtomikosDataSourceBean;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

import javax.sql.DataSource;

@Configuration
@EnableJpaRepositories(
        basePackages = "levit104.blss.labs.repos.main",
        entityManagerFactoryRef = "mainEntityManagerFactory",
        transactionManagerRef = "jtaTransactionManager"
)
public class MainDatasourceConfig {

    @Primary
    @Bean(initMethod = "init", destroyMethod = "close")
    @ConfigurationProperties("spring.jta.atomikos.datasource.main")
    public AtomikosDataSourceBean mainDataSource() {
        return new AtomikosDataSourceBean();
    }

    @Primary
    @Bean
    public LocalContainerEntityManagerFactoryBean mainEntityManagerFactory(
            EntityManagerFactoryBuilder builder,
            @Qualifier("mainDataSource") DataSource dataSource
    ) {
        return builder
                .dataSource(dataSource)
                .jta(true)
                .persistenceUnit("main_pu")
                .packages("levit104.blss.labs.models.main")
                .build();
    }
}
