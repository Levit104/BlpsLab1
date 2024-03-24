package levit104.blps.lab1.controllers;

import jakarta.validation.Valid;
import levit104.blps.lab1.dto.OrderClientDTO;
import levit104.blps.lab1.dto.OrderCreationDTO;
import levit104.blps.lab1.exceptions.ForbiddenException;
import levit104.blps.lab1.models.Order;
import levit104.blps.lab1.services.OrderService;
import levit104.blps.lab1.utils.MappingUtils;
import levit104.blps.lab1.utils.ValidationUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class OrderClientController {
    private final MappingUtils mappingUtils;
    private final OrderService orderService;

    // Заказы клиента
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/users/{username}/orders")
    public List<OrderClientDTO> showOrders(@PathVariable String username,
                                           Principal principal) {
        if (!principal.getName().equals(username))
            throw new ForbiddenException("Нет доступа к чужой странице");

        List<Order> orders = orderService.findAllByClientUsername(username);
        return mappingUtils.mapList(orders, OrderClientDTO.class);
    }

    // Конкретный заказ клиента
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/users/{username}/orders/{id}")
    public OrderClientDTO showOrderInfo(@PathVariable String username,
                                        @PathVariable Long id,
                                        Principal principal) {
        if (!principal.getName().equals(username))
            throw new ForbiddenException("Нет доступа к чужой странице");

        Order order = orderService.getByIdAndClientUsername(id, username);
        return mappingUtils.mapObject(order, OrderClientDTO.class);
    }

    // Создать заказ
    @PreAuthorize("hasRole('USER')")
    @PostMapping("/users/{username}/orders")
    @ResponseStatus(HttpStatus.CREATED)
    public OrderClientDTO createOrder(@PathVariable String username,
                                      @RequestBody @Valid OrderCreationDTO requestDTO,
                                      BindingResult bindingResult,
                                      Principal principal) {
        if (!principal.getName().equals(username))
            throw new ForbiddenException("Нет доступа к чужой странице");

        Order order = mappingUtils.mapObject(requestDTO, Order.class);

        if (bindingResult.hasErrors())
            ValidationUtils.handleCreationErrors(bindingResult);

        orderService.createOrder(order, username);
        return mappingUtils.mapObject(order, OrderClientDTO.class);
    }
}
