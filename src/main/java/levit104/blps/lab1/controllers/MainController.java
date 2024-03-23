package levit104.blps.lab1.controllers;

import jakarta.validation.Valid;
import levit104.blps.lab1.dto.TourCreationDTO;
import levit104.blps.lab1.dto.TourResponseDTO;
import levit104.blps.lab1.dto.UserResponseDTO;
import levit104.blps.lab1.models.Tour;
import levit104.blps.lab1.models.User;
import levit104.blps.lab1.services.TourService;
import levit104.blps.lab1.services.UserService;
import levit104.blps.lab1.validation.TourValidator;
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

// TODO обработка исключений

@RestController
@RequiredArgsConstructor
public class MainController {
    private final UserService userService;
    private final TourService tourService;
    private final TourValidator tourValidator;
    private final ModelMapper modelMapper;

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/users/{username}")
    public ResponseEntity<?> showUserInfo(@PathVariable String username) {
        User user = userService.getByUsername(username);
        UserResponseDTO responseDTO = modelMapper.map(user, UserResponseDTO.class);
        return ResponseEntity.ok(responseDTO);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/users/{username}/tours")
    public ResponseEntity<?> showTours(@PathVariable String username) {
        List<Tour> tours = tourService.findAllByGuideUsername(username);
        List<TourResponseDTO> responseDTO = tours.stream().map(tour -> modelMapper.map(tour, TourResponseDTO.class)).toList();
        return ResponseEntity.ok(responseDTO);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/users/{username}/tours/{id}")
    public ResponseEntity<?> showTourInfo(@PathVariable String username, @PathVariable Long id) {
        Tour tour = tourService.getByIdAndGuideUsername(id, username);
        TourResponseDTO responseDTO = modelMapper.map(tour, TourResponseDTO.class);
        return ResponseEntity.ok(responseDTO);
    }


    @PreAuthorize("hasRole('GUIDE')")
    @PostMapping("/users/{username}/tours")
    public ResponseEntity<?> addTour(@PathVariable String username,
                                     @RequestBody @Valid TourCreationDTO requestDTO,
                                     BindingResult bindingResult,
                                     Principal principal) {

        if (!principal.getName().equals(username))
            return new ResponseEntity<>("Нет доступа", HttpStatus.FORBIDDEN); // TODO исключение/объект ошибки?

        Tour tour = modelMapper.map(requestDTO, Tour.class);

        tourValidator.validate(tour, bindingResult);
        if (bindingResult.hasErrors())
            return ResponseEntity.badRequest().body(returnErrors(bindingResult));

        User guide = userService.getByUsername(username);

        tourService.save(tour, guide);
        TourResponseDTO responseDTO = modelMapper.map(tour, TourResponseDTO.class);
        return ResponseEntity.ok(responseDTO);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/users/{username}/add-privilege")
    public ResponseEntity<?> addGuidePrivilege(@PathVariable String username,
                                               Principal principal) {

        if (principal.getName().equals(username))
            return ResponseEntity.badRequest().body("Назначение роли администратору"); // TODO исключение/объект ошибки?

        userService.giveGuideRole(username);
        return ResponseEntity.ok("OK");
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/tours")
    public ResponseEntity<?> showToursInCity(@RequestParam String country,
                                             @RequestParam String city) {
        List<Tour> tours = tourService.findAllByCityNameAndCountryName(city, country);
        List<TourResponseDTO> responseDTO = tours.stream().map(tour -> modelMapper.map(tour, TourResponseDTO.class)).toList();
        return ResponseEntity.ok(responseDTO);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/guides")
    public ResponseEntity<?> showGuidesInCity(@RequestParam String country,
                                              @RequestParam String city) {
        List<User> guides = userService.findAllByCityNameAndCountryName(city, country);
        List<UserResponseDTO> responseDTO = guides.stream().map(guide -> modelMapper.map(guide, UserResponseDTO.class)).toList();
        return ResponseEntity.ok(responseDTO);
    }

}
