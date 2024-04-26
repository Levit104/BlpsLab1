package levit104.blps.lab1.controllers;

import jakarta.validation.Valid;
import levit104.blps.lab1.dto.OrderClientDTO;
import levit104.blps.lab1.dto.OrderCreationDTO;
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
    public List<OrderClientDTO> showOrders(@PathVariable String username, Principal principal) {
        ValidationUtils.checkAccess(principal.getName(), username);
        List<Order> orders = orderService.getAllByClientUsername(username);
        return mappingUtils.mapList(orders, OrderClientDTO.class);
    }

    // Конкретный заказ клиента
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/users/{username}/orders/{id}")
    public OrderClientDTO showOrderInfo(@PathVariable String username, @PathVariable Long id, Principal principal) {
        ValidationUtils.checkAccess(principal.getName(), username);
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
        ValidationUtils.checkAccess(principal.getName(), username);
        ValidationUtils.handleCreationErrors(bindingResult);
        Order order = mappingUtils.mapObject(requestDTO, Order.class);
        orderService.createOrder(order, username);
        return mappingUtils.mapObject(order, OrderClientDTO.class);
    }
}
