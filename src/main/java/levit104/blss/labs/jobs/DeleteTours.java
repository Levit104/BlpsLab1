package levit104.blss.labs.jobs;

import levit104.blss.labs.models.primary.Tour;
import levit104.blss.labs.services.TourService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.springframework.lang.NonNull;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class DeleteTours extends QuartzJobBean {
    private final TourService tourService;

    @Override
    protected void executeInternal(@NonNull JobExecutionContext context) {
        List<Tour> tours = tourService.deleteAllDisapprovedWithCreationDateBefore(LocalDate.now().minusDays(7));
        tours.forEach(tour -> log.info("Удалена экскурсия (id={}, name={})", tour.getId(), tour.getName()));
    }
}
