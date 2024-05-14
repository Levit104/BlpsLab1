package levit104.blss.labs.services;

import levit104.blss.labs.exceptions.EntityNotFoundException;
import levit104.blss.labs.exceptions.InvalidDataException;
import levit104.blss.labs.models.primary.Order;
import levit104.blss.labs.models.primary.OrderStatus;
import levit104.blss.labs.models.primary.Tour;
import levit104.blss.labs.models.primary.User;
import levit104.blss.labs.repos.primary.OrderRepository;
import levit104.blss.labs.utils.ValidationUtils;
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

        if (orderRepository.findAllByClient_Username(clientUsername, Pageable.unpaged())
                .stream().anyMatch(clientOrder ->
                        Objects.equals(clientOrder.getTour(), tour) &&
                        Objects.equals(clientOrder.getGuide(), guide) &&
                        clientOrder.getTourDate().isEqual(order.getTourDate())
                )
        ) {
            throw new InvalidDataException("Заказ на данную экскурсию уже создан");
        }

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
