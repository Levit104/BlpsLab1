package levit104.blps.lab1.controllers;

import jakarta.validation.Valid;
import levit104.blps.lab1.dto.OrderCreationDTO;
import levit104.blps.lab1.dto.OrderResponseDTO;
import levit104.blps.lab1.models.Order;
import levit104.blps.lab1.models.Tour;
import levit104.blps.lab1.models.User;
import levit104.blps.lab1.services.OrderService;
import levit104.blps.lab1.services.TourService;
import levit104.blps.lab1.services.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

import static levit104.blps.lab1.validation.ErrorsUtils.returnErrors;

@RestController
@RequiredArgsConstructor
public class ClientOrderController {
    private final ModelMapper modelMapper;
    private final OrderService orderService;
    private final UserService userService;
    private final TourService tourService;

    // Заказы клиента
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/users/{username}/orders")
    public ResponseEntity<?> showOrders(@PathVariable String username,
                                        Principal principal) {
        if (!principal.getName().equals(username))
            return new ResponseEntity<>("Нет доступа", HttpStatus.FORBIDDEN); // TODO исключение/объект ошибки?

        List<Order> orders = orderService.findAllByClientUsername(username);

        List<OrderResponseDTO> responseDTO = orders.stream().map(order -> modelMapper.map(order, OrderResponseDTO.class)).toList();
        return ResponseEntity.ok(responseDTO);
    }

    // Конкретный заказ клиента
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/users/{username}/orders/{id}")
    public ResponseEntity<?> showOrderInfo(@PathVariable String username,
                                           @PathVariable Long id,
                                           Principal principal) {
        if (!principal.getName().equals(username))
            return new ResponseEntity<>("Нет доступа", HttpStatus.FORBIDDEN); // TODO исключение/объект ошибки?

        Order order = orderService.getByIdAndClientUsername(id, username);

        OrderResponseDTO responseDTO = modelMapper.map(order, OrderResponseDTO.class);
        return ResponseEntity.ok(responseDTO);
    }

    // TODO POST: /users/orders
    // Создать заказ
    @PreAuthorize("hasRole('USER')")
    @PostMapping("/users/{username}/tours/{id}/book")
    public ResponseEntity<?> createOrder(@PathVariable String username,
                                         @PathVariable Long id,
                                         @RequestBody @Valid OrderCreationDTO requestDTO,
                                         BindingResult bindingResult,
                                         Principal principal) {
        Order order = modelMapper.map(requestDTO, Order.class);

        if (bindingResult.hasErrors())
            return ResponseEntity.badRequest().body(returnErrors(bindingResult));

        User client = userService.getByUsername(principal.getName());
        Tour tour = tourService.getByIdAndGuideUsername(id, username);
        orderService.createOrder(order, client, tour);

        OrderResponseDTO responseDTO = modelMapper.map(order, OrderResponseDTO.class);
        return ResponseEntity.ok(responseDTO);
    }
}
