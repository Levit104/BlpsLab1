package levit104.blss.labs.controllers;

import levit104.blss.labs.dto.OrderGuideDTO;
import levit104.blss.labs.models.main.Order;
import levit104.blss.labs.services.OrderService;
import levit104.blss.labs.utils.MappingUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class OrderGuideController {
    private final MappingUtils mappingUtils;
    private final OrderService orderService;

    // Заказы для гида
    @PreAuthorize("hasRole('GUIDE') && principal.username == #username")
    @GetMapping("/users/{username}/available-orders")
    public List<OrderGuideDTO> showAvailableOrders(
            @PathVariable String username,
            @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        List<Order> orders = orderService.getAllByGuideUsername(username, pageable);
        return mappingUtils.mapList(orders, OrderGuideDTO.class);
    }

    // Конкретный заказ для гида
    @PreAuthorize("hasRole('GUIDE') && principal.username == #username")
    @GetMapping("/users/{username}/available-orders/{id}")
    public OrderGuideDTO showAvailableOrderInfo(@PathVariable String username, @PathVariable Long id) {
        Order order = orderService.getByIdAndGuideUsername(id, username);
        return mappingUtils.mapObject(order, OrderGuideDTO.class);
    }

    // Принять или отклонить заказ
    @PreAuthorize("hasRole('GUIDE') && principal.username == #username")
    @PatchMapping("/users/{username}/available-orders/{id}")
    public String considerOrder(@PathVariable String username, @PathVariable Long id, @RequestParam boolean accepted) {
        return orderService.changeStatus(id, username, accepted);
    }
}
