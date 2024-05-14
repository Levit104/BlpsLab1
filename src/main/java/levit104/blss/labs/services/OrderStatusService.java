package levit104.blss.labs.services;

import levit104.blss.labs.exceptions.EntityNotFoundException;
import levit104.blss.labs.models.primary.OrderStatus;
import levit104.blss.labs.repos.primary.OrderStatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderStatusService {
    private final OrderStatusRepository orderStatusRepository;

    public OrderStatus getByName(String name) {
        return orderStatusRepository.findByName(name).orElseThrow(() -> new EntityNotFoundException(
                "Статус '%s' не найден".formatted(name)
        ));
    }
}
