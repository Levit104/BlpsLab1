package levit104.blps.lab1.controllers;

import jakarta.validation.Valid;
import levit104.blps.lab1.dto.OrderClientDTO;
import levit104.blps.lab1.dto.OrderCreationDTO;
import levit104.blps.lab1.models.main.Order;
import levit104.blps.lab1.services.OrderService;
import levit104.blps.lab1.utils.MappingUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class OrderClientController {
    private final MappingUtils mappingUtils;
    private final OrderService orderService;

    // Заказы клиента
    @PreAuthorize("hasRole('USER') && principal.username == #username")
    @GetMapping("/users/{username}/orders")
    public List<OrderClientDTO> showOrders(
            @PathVariable String username,
            @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        List<Order> orders = orderService.getAllByClientUsername(username, pageable);
        return mappingUtils.mapList(orders, OrderClientDTO.class);
    }

    // Конкретный заказ клиента
    @PreAuthorize("hasRole('USER') && principal.username == #username")
    @GetMapping("/users/{username}/orders/{id}")
    public OrderClientDTO showOrderInfo(@PathVariable String username, @PathVariable Long id) {
        Order order = orderService.getByIdAndClientUsername(id, username);
        return mappingUtils.mapObject(order, OrderClientDTO.class);
    }

    // Создать заказ
    @PreAuthorize("hasRole('USER') && principal.username == #username")
    @PostMapping("/users/{username}/orders")
    @ResponseStatus(HttpStatus.CREATED)
    public OrderClientDTO createOrder(
            @PathVariable String username,
            @RequestBody @Valid OrderCreationDTO requestDTO,
            BindingResult bindingResult
    ) {
        Order order = mappingUtils.mapObject(requestDTO, Order.class);
        orderService.createOrder(order, username, bindingResult);
        return mappingUtils.mapObject(order, OrderClientDTO.class);
    }
}
