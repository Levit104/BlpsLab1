package levit104.blps.lab1.services;

import levit104.blps.lab1.exceptions.EntityNotFoundException;
import levit104.blps.lab1.exceptions.InvalidDataException;
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
    private final UserService userService;
    private final TourService tourService;

    public Order getByIdAndClientUsername(Long id, String clientUsername) {
        return orderRepository.findByIdAndClient_Username(id, clientUsername).orElseThrow(() -> new EntityNotFoundException(
                "Заказ под номером %d пользователя '%s' не найден".formatted(id, clientUsername)
        ));
    }

    public Order getByIdAndGuideUsername(Long id, String guideUsername) {
        return orderRepository.findByIdAndGuide_Username(id, guideUsername).orElseThrow(() -> new EntityNotFoundException(
                "Заказ под номером %d для гида '%s' не найден".formatted(id, guideUsername)
        ));
    }

    public List<Order> findAllByClientUsername(String clientUsername) {
        List<Order> orders = orderRepository.findAllByClient_Username(clientUsername);

        if (orders.isEmpty())
            throw new EntityNotFoundException("Заказы пользователя '%s' не найдены".formatted(clientUsername));

        return orders;
    }

    public List<Order> findAllByGuideUsername(String guideUsername) {
        List<Order> orders = orderRepository.findAllByGuide_Username(guideUsername);

        if (orders.isEmpty())
            throw new EntityNotFoundException("Заказы для гида '%s' не найдены".formatted(guideUsername));

        return orders;
    }

    @Transactional
    public void createOrder(Order order, String clientUsername) {
        User client = userService.getByUsername(clientUsername);
        Tour tour = tourService.getByIdAndGuideUsername(order.getTour().getId(), order.getGuide().getUsername());
        OrderStatus orderStatus = orderStatusService.getByName("На рассмотрении");
        LocalDate orderDate = LocalDate.now();
        order.setClient(client);
        order.setTour(tour);
        order.setGuide(tour.getGuide());
        order.setStatus(orderStatus);
        order.setOrderDate(orderDate);
        orderRepository.save(order);
    }

    @Transactional
    public String changeStatus(Long orderId, String guideUsername, boolean accepted) {
        Order order = getByIdAndGuideUsername(orderId, guideUsername);
        OrderStatus status = orderStatusService.getByName("На рассмотрении");

        if (!order.getStatus().equals(status))
            throw new InvalidDataException("Заказ уже был рассмотрен");

        String statusName = accepted ? "Принят" : "Отклонён";
        order.setStatus(orderStatusService.getByName(statusName));
        orderRepository.save(order);

        return "Статус заказа изменён на '%s'".formatted(statusName);
    }
}
