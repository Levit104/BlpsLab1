package levit104.blps.lab1.services;

import levit104.blps.lab1.exceptions.EntityNotFoundException;
import levit104.blps.lab1.exceptions.InvalidDataException;
import levit104.blps.lab1.models.main.Order;
import levit104.blps.lab1.models.main.OrderStatus;
import levit104.blps.lab1.models.main.Tour;
import levit104.blps.lab1.models.main.User;
import levit104.blps.lab1.repos.main.OrderRepository;
import levit104.blps.lab1.utils.ValidationUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderStatusService orderStatusService;
    private final UserService userService;
    private final TourService tourService;
    private final NotificationService notificationService;

    public Order getByIdAndClientUsername(Long id, String clientUsername) {
        return orderRepository.findByIdAndClient_Username(id, clientUsername).orElseThrow(() -> new EntityNotFoundException(
                "Заказ №%d у '%s' не найден".formatted(id, clientUsername)
        ));
    }

    public Order getByIdAndGuideUsername(Long id, String guideUsername) {
        return orderRepository.findByIdAndGuide_Username(id, guideUsername).orElseThrow(() -> new EntityNotFoundException(
                "Заказ №%d для '%s' не найден".formatted(id, guideUsername)
        ));
    }

    public List<Order> getAllByClientUsername(String clientUsername, Pageable pageable) {
        List<Order> orders = orderRepository.findAllByClient_Username(clientUsername, pageable);

        if (orders.isEmpty())
            throw new EntityNotFoundException("Заказы у '%s' не найдены".formatted(clientUsername));

        return orders;
    }

    public List<Order> getAllByGuideUsername(String guideUsername, Pageable pageable) {
        List<Order> orders = orderRepository.findAllByGuide_Username(guideUsername, pageable);

        if (orders.isEmpty())
            throw new EntityNotFoundException("Заказы для '%s' не найдены".formatted(guideUsername));

        return orders;
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void createOrder(Order order, String clientUsername, BindingResult bindingResult) {
        ValidationUtils.handleCreationErrors(bindingResult);

        order.setId(null); // т.к. ModelMapper неправильно мапит id
        User client = userService.getByUsername(clientUsername);
        Tour tour = tourService.getByIdAndGuideUsername(order.getTour().getId(), order.getGuide().getUsername());
        User guide = tour.getGuide();

        if (Objects.equals(client, guide))
            throw new InvalidDataException("Клиент и Гид - один и тот же пользователь");

        order.setClient(client);
        order.setTour(tour);
        order.setGuide(guide);
        order.setStatus(orderStatusService.getByName("На рассмотрении"));
        order.setOrderDate(LocalDate.now());
        orderRepository.save(order);

        notificationService.createNotification("Заказ %d создан".formatted(order.getId()), clientUsername);
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public String changeStatus(Long orderId, String guideUsername, boolean accepted) {
        Order order = getByIdAndGuideUsername(orderId, guideUsername);
        OrderStatus status = orderStatusService.getByName("На рассмотрении");

        if (!Objects.equals(order.getStatus(), status))
            throw new InvalidDataException("Заказ уже был рассмотрен");

        String statusName = accepted ? "Принят" : "Отклонён";
        order.setStatus(orderStatusService.getByName(statusName));
        orderRepository.save(order);

        String message = "Статус заказа изменён на '%s'".formatted(statusName);
        notificationService.createNotification(message, guideUsername);
        return message;
    }
}
