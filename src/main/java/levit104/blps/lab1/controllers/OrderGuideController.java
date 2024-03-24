package levit104.blps.lab1.controllers;

import levit104.blps.lab1.dto.OrderGuideDTO;
import levit104.blps.lab1.exceptions.ForbiddenException;
import levit104.blps.lab1.models.Order;
import levit104.blps.lab1.services.OrderService;
import levit104.blps.lab1.utils.MappingUtils;
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
    public List<OrderGuideDTO> showAvailableOrders(@PathVariable String username,
                                                   Principal principal) {
        if (!principal.getName().equals(username))
            throw new ForbiddenException("Нет доступа к чужой странице");

        List<Order> orders = orderService.findAllByGuideUsername(username);
        return mappingUtils.mapList(orders, OrderGuideDTO.class);
    }

    // Принять или отклонить заказ
    @PreAuthorize("hasRole('GUIDE')")
    @PatchMapping("/users/{username}/available-orders/{id}")
    public String considerOrder(@PathVariable String username,
                                @PathVariable Long id,
                                @RequestParam boolean accepted,
                                Principal principal) {
        if (!principal.getName().equals(username))
            throw new ForbiddenException("Нет доступа к чужой странице");

        return orderService.changeStatus(id, username, accepted);
    }
}
