package levit104.blps.lab1.controllers;

import levit104.blps.lab1.dto.OrderGuideDTO;
import levit104.blps.lab1.models.Order;
import levit104.blps.lab1.services.OrderService;
import levit104.blps.lab1.utils.MappingUtils;
import levit104.blps.lab1.utils.ValidationUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class OrderGuideController {
    private final MappingUtils mappingUtils;
    private final OrderService orderService;

    // Заказы для гида
    @PreAuthorize("hasRole('GUIDE')")
    @GetMapping("/users/{username}/available-orders")
    public List<OrderGuideDTO> showAvailableOrders(@PathVariable String username, Principal principal) {
        ValidationUtils.checkAccess(principal.getName(), username);
        List<Order> orders = orderService.findAllByGuideUsername(username);
        return mappingUtils.mapList(orders, OrderGuideDTO.class);
    }

    // Конкретный заказ для гида
    @PreAuthorize("hasRole('GUIDE')")
    @GetMapping("/users/{username}/available-orders/{id}")
    public OrderGuideDTO showAvailableOrderInfo(@PathVariable String username, @PathVariable Long id, Principal principal) {
        ValidationUtils.checkAccess(principal.getName(), username);
        Order order = orderService.getByIdAndGuideUsername(id, username);
        return mappingUtils.mapObject(order, OrderGuideDTO.class);
    }

    // Принять или отклонить заказ
    @PreAuthorize("hasRole('GUIDE')")
    @PatchMapping("/users/{username}/available-orders/{id}")
    public String considerOrder(@PathVariable String username,
                                @PathVariable Long id,
                                @RequestParam boolean accepted,
                                Principal principal) {
        ValidationUtils.checkAccess(principal.getName(), username);
        return orderService.changeStatus(id, username, accepted);
    }
}
