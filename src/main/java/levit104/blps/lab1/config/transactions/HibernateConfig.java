package levit104.blps.lab1.config.transactions;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class HibernateConfig {
    private final Environment env;

    @Bean
    public Map<String, String> jpaProperties() {
        return Map.of(
                // https://www.atomikos.com/Documentation/HibernateIntegration
                "hibernate.connection.handling_mode", env.getRequiredProperty("hibernate.connection.handling_mode"),

                "hibernate.hbm2ddl.auto", env.getRequiredProperty("hibernate.hbm2ddl.auto"),
                "hibernate.show_sql", env.getRequiredProperty("hibernate.show_sql"),
                "hibernate.format_sql", env.getRequiredProperty("hibernate.format_sql"),
                "hibernate.physical_naming_strategy", env.getRequiredProperty("hibernate.physical_naming_strategy"),
                "hibernate.implicit_naming_strategy", env.getRequiredProperty("hibernate.implicit_naming_strategy")
        );
    }
}
