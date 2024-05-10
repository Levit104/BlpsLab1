package levit104.blps.lab1.controllers;

import jakarta.validation.Valid;
import levit104.blps.lab1.dto.TourCreationDTO;
import levit104.blps.lab1.dto.TourDTO;
import levit104.blps.lab1.dto.UserDTO;
import levit104.blps.lab1.models.main.Tour;
import levit104.blps.lab1.models.main.User;
import levit104.blps.lab1.services.TourService;
import levit104.blps.lab1.services.UserService;
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
public class TourController {
    private final MappingUtils mappingUtils;
    private final UserService userService;
    private final TourService tourService;

    // Экскурсии в городе
    @GetMapping("/tours")
    public List<TourDTO> showToursInCity(@RequestParam String country, @RequestParam String city) {
        List<Tour> tours = tourService.getAllByCityNameAndCountryName(city, country);
        return mappingUtils.mapList(tours, TourDTO.class);
    }

    // Гиды в городе
    @GetMapping("/guides")
    public List<UserDTO> showGuidesInCity(@RequestParam String country, @RequestParam String city) {
        List<User> guides = userService.getAllByCityNameAndCountryName(city, country);
        return mappingUtils.mapList(guides, UserDTO.class);
    }

    // Экскурсии гида
    @GetMapping("/users/{username}/tours")
    public List<TourDTO> showGuideTours(@PathVariable String username) {
        List<Tour> tours = tourService.getAllByGuideUsername(username);
        return mappingUtils.mapList(tours, TourDTO.class);
    }

    // Конкретная экскурсия гида
    @GetMapping("/users/{username}/tours/{id}")
    public TourDTO showGuideTourInfo(@PathVariable String username, @PathVariable Long id) {
        Tour tour = tourService.getByIdAndGuideUsername(id, username);
        return mappingUtils.mapObject(tour, TourDTO.class);
    }

    // Добавить экскурсию
    @PreAuthorize("hasRole('GUIDE')")
    @PostMapping("/users/{username}/tours")
    @ResponseStatus(HttpStatus.CREATED)
    public TourDTO addTour(@PathVariable String username,
                           @RequestBody @Valid TourCreationDTO requestDTO,
                           BindingResult bindingResult,
                           Principal principal) {
        ValidationUtils.checkAccess(principal.getName(), username);
        ValidationUtils.handleCreationErrors(bindingResult);
        Tour tour = mappingUtils.mapObject(requestDTO, Tour.class);
        tourService.add(tour, username);
        return mappingUtils.mapObject(tour, TourDTO.class);
    }
}
