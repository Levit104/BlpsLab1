package levit104.blss.labs.config.transactions;

import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.jta.JtaTransactionManager;

@Configuration
public class AtomikosConfig {
    public AtomikosConfig(JtaTransactionManager transactionManager) {
        transactionManager.setAllowCustomIsolationLevels(true);
    }
}
