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
import org.springframework.stereotype.Service;
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

    public List<Order> getAllByClientUsername(String clientUsername) {
        List<Order> orders = orderRepository.findAllByClient_Username(clientUsername);

        if (orders.isEmpty())
            throw new EntityNotFoundException("Заказы у '%s' не найдены".formatted(clientUsername));

        return orders;
    }

    public List<Order> getAllByGuideUsername(String guideUsername) {
        List<Order> orders = orderRepository.findAllByGuide_Username(guideUsername);

        if (orders.isEmpty())
            throw new EntityNotFoundException("Заказы для '%s' не найдены".formatted(guideUsername));

        return orders;
    }

    @Transactional
    public void createOrder(Order order, String clientUsername) {
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
    }

    @Transactional
    public String changeStatus(Long orderId, String guideUsername, boolean accepted) {
        Order order = getByIdAndGuideUsername(orderId, guideUsername);
        OrderStatus status = orderStatusService.getByName("На рассмотрении");

        if (!Objects.equals(order.getStatus(), status))
            throw new InvalidDataException("Заказ уже был рассмотрен");

        String statusName = accepted ? "Принят" : "Отклонён";
        order.setStatus(orderStatusService.getByName(statusName));
        orderRepository.save(order);

        return "Статус заказа изменён на '%s'".formatted(statusName);
    }
}
