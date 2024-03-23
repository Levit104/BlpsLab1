package levit104.blps.lab1.services;

import levit104.blps.lab1.exceptions.EntityNotFoundException;
import levit104.blps.lab1.models.Order;
import levit104.blps.lab1.models.OrderStatus;
import levit104.blps.lab1.models.Tour;
import levit104.blps.lab1.models.User;
import levit104.blps.lab1.repos.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderStatusService orderStatusService;

    public Order getByIdAndClientUsername(Long id, String clientUsername) throws EntityNotFoundException {
        return orderRepository.findByIdAndClient_Username(id, clientUsername).orElseThrow(() -> new EntityNotFoundException(
                "Заказ под номером %d пользователя %s не найден".formatted(id, clientUsername)
        ));
    }

    public Order getByIdAndGuideUsername(Long id, String guideUsername) throws EntityNotFoundException {
        return orderRepository.findByIdAndGuide_Username(id, guideUsername).orElseThrow(() -> new EntityNotFoundException(
                "Заказ под номером %d гида %s не найден".formatted(id, guideUsername)
        ));
    }

    public List<Order> findAllByClientUsername(String clientUsername) {
        return orderRepository.findAllByClient_Username(clientUsername);
    }

    public List<Order> findAllByGuideUsername(String guideUsername, String statusName) {
        return orderRepository.findAllByGuide_UsernameAndStatus_Name(guideUsername, statusName);
    }

    @Transactional
    public void createOrder(Order order, User client, Tour tour) throws EntityNotFoundException {
        LocalDate orderDate = LocalDate.now();
        OrderStatus orderStatus = orderStatusService.getByName("На рассмотрении");
        order.setClient(client);
        order.setTour(tour);
        order.setGuide(tour.getGuide());
        order.setOrderDate(orderDate);
        order.setStatus(orderStatus);
        orderRepository.save(order);
    }

    @Transactional
    public void changeStatus(Order order, boolean accepted) throws EntityNotFoundException {
        if (order.getStatus().getName().equalsIgnoreCase("На рассмотрении"))
            return;

        String statusName = accepted ? "Принят" : "Отклонён";
        order.setStatus(orderStatusService.getByName(statusName));
        orderRepository.save(order);
    }
}
