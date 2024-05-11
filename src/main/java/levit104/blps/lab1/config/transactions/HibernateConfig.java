package levit104.blps.lab1.config.transactions;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class HibernateConfig {
    @Bean
    public Map<String, String> jpaProperties() {
        return Map.of(
                // https://www.atomikos.com/Documentation/HibernateIntegration
                "hibernate.connection.handling_mode", "DELAYED_ACQUISITION_AND_RELEASE_AFTER_STATEMENT",

                "hibernate.hbm2ddl.auto", "update",
                "hibernate.show_sql", "true",
                "hibernate.format_sql", "false",
                "hibernate.physical_naming_strategy", "org.hibernate.boot.model.naming.CamelCaseToUnderscoresNamingStrategy",
                "hibernate.implicit_naming_strategy", "org.springframework.boot.orm.jpa.hibernate.SpringImplicitNamingStrategy"
        );
    }
}
