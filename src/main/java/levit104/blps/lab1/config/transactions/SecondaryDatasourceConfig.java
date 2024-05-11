package levit104.blps.lab1.config.transactions;

import com.atomikos.spring.AtomikosDataSourceBean;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

import javax.sql.DataSource;
import java.util.Map;

@Configuration
@EnableJpaRepositories(
        basePackages = "levit104.blps.lab1.repos.secondary",
        entityManagerFactoryRef = "secondaryEntityManagerFactory",
        transactionManagerRef = "jtaTransactionManager"
)
public class SecondaryDatasourceConfig {

    @Bean(initMethod = "init", destroyMethod = "close")
    @ConfigurationProperties("spring.jta.atomikos.datasource.secondary")
    public AtomikosDataSourceBean secondaryDataSource() {
        return new AtomikosDataSourceBean();
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean secondaryEntityManagerFactory(
            EntityManagerFactoryBuilder builder,
            @Qualifier("secondaryDataSource") DataSource dataSource,
            Map<String, String> jpaProperties
    ) {
        return builder
                .dataSource(dataSource)
                .properties(jpaProperties)
                .jta(true)
                .persistenceUnit("secondary_pu")
                .packages("levit104.blps.lab1.models.secondary")
                .build();
    }
}
