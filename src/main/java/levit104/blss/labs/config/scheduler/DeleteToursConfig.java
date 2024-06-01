package levit104.blss.labs.config.scheduler;

import levit104.blss.labs.jobs.DeleteTours;
import org.quartz.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DeleteToursConfig {
    @Bean
    public JobDetail deleteToursJob() {
        return JobBuilder.newJob(DeleteTours.class)
                .withIdentity("delete-tours-job")
                .storeDurably()
                .requestRecovery(true)
                .build();
    }

    @Bean
    public Trigger deleteToursTrigger() {
        return TriggerBuilder.newTrigger()
                .forJob("delete-tours-job")
                .withIdentity("delete-tours-trigger")
                .withSchedule(CronScheduleBuilder.cronSchedule("0 0 0 * * ?"))
                .build();
    }
}
