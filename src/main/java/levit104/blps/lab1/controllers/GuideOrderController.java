package levit104.blps.lab1.controllers;

import levit104.blps.lab1.dto.OrderResponseDTO2;
import levit104.blps.lab1.exceptions.EntityNotFoundException;
import levit104.blps.lab1.models.Order;
import levit104.blps.lab1.services.OrderService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class GuideOrderController {
    private final ModelMapper modelMapper;
    private final OrderService orderService;

    // Заказы для гида
    @PreAuthorize("hasRole('GUIDE')")
    @GetMapping("/users/{username}/available-orders")
    public ResponseEntity<?> showOrders(@PathVariable String username,
                                        Principal principal) {
        if (!principal.getName().equals(username))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Нет доступа к чужой странице");

        List<Order> orders = orderService.findAllByGuideUsername(username, "На рассмотрении");
        List<OrderResponseDTO2> responseDTO = orders.stream().map(order -> modelMapper.map(order, OrderResponseDTO2.class)).toList();
        return ResponseEntity.ok(responseDTO);
    }

    // Принять или отклонить заказ
    @PreAuthorize("hasRole('GUIDE')")
    @PatchMapping("/users/{username}/available-orders/{id}")
    public ResponseEntity<?> considerOrder(@PathVariable String username,
                                           @PathVariable Long id,
                                           @RequestParam boolean accepted,
                                           Principal principal) {
        if (!principal.getName().equals(username))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Нет доступа к чужой странице");

        try {
            Order order = orderService.getByIdAndGuideUsername(id, username); // TODO orderService.changeStatus???
            orderService.changeStatus(order, accepted);
            return ResponseEntity.ok("Статус изменён");
        } catch (EntityNotFoundException | IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
}
