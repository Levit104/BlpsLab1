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
        basePackages = "levit104.blss.labs.repos.primary",
        entityManagerFactoryRef = "primaryEntityManagerFactory"
)
public class PrimaryDatasourceConfig {

    @Primary
    @Bean(initMethod = "init", destroyMethod = "close")
    @ConfigurationProperties("spring.jta.atomikos.datasource.primary")
    public AtomikosDataSourceBean primaryDataSource() {
        return new AtomikosDataSourceBean();
    }

    @Primary
    @Bean
    public LocalContainerEntityManagerFactoryBean primaryEntityManagerFactory(
            EntityManagerFactoryBuilder factoryBuilder,
            @Qualifier("primaryDataSource") DataSource dataSource
    ) {
        return factoryBuilder
                .dataSource(dataSource)
                .jta(true)
                .persistenceUnit("main_pu")
                .packages("levit104.blss.labs.models.primary")
                .build();
    }
}
