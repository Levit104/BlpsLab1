package levit104.blps.lab1.services;

import levit104.blps.lab1.exceptions.EntityNotFoundException;
import levit104.blps.lab1.models.OrderStatus;
import levit104.blps.lab1.repos.OrderStatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderStatusService {
    private final OrderStatusRepository orderStatusRepository;

    public OrderStatus getByName(String name) {
        return orderStatusRepository.findByName(name).orElseThrow(() -> new EntityNotFoundException("Ошибка при создании заказа"));
    }
}
