package levit104.blss.labs.controllers;

import jakarta.validation.Valid;
import levit104.blss.labs.dto.OrderClientDTO;
import levit104.blss.labs.dto.OrderCreationDTO;
import levit104.blss.labs.models.main.Order;
import levit104.blss.labs.services.OrderService;
import levit104.blss.labs.utils.MappingHelper;
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
    private final MappingHelper mappingHelper;
    private final OrderService orderService;

    // Заказы клиента
    @PreAuthorize("hasRole('USER') && principal.username == #username")
    @GetMapping("/users/{username}/orders")
    public List<OrderClientDTO> showOrders(
            @PathVariable String username,
            @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        List<Order> orders = orderService.getAllByClientUsername(username, pageable);
        return mappingHelper.mapList(orders, OrderClientDTO.class);
    }

    // Конкретный заказ клиента
    @PreAuthorize("hasRole('USER') && principal.username == #username")
    @GetMapping("/users/{username}/orders/{id}")
    public OrderClientDTO showOrderInfo(@PathVariable String username, @PathVariable Long id) {
        Order order = orderService.getByIdAndClientUsername(id, username);
        return mappingHelper.mapObject(order, OrderClientDTO.class);
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
        Order order = mappingHelper.mapObject(requestDTO, Order.class);
        orderService.createOrder(order, username, bindingResult);
        return mappingHelper.mapObject(order, OrderClientDTO.class);
    }
}
