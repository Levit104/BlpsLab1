package levit104.blps.lab1.controllers;

import jakarta.validation.Valid;
import levit104.blps.lab1.dto.TourCreationDTO;
import levit104.blps.lab1.dto.TourDTO;
import levit104.blps.lab1.dto.UserDTO;
import levit104.blps.lab1.exceptions.ForbiddenException;
import levit104.blps.lab1.models.Tour;
import levit104.blps.lab1.models.User;
import levit104.blps.lab1.services.TourService;
import levit104.blps.lab1.services.UserService;
import levit104.blps.lab1.utils.MappingUtils;
import levit104.blps.lab1.utils.ValidationUtils;
import levit104.blps.lab1.validation.TourValidator;
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
    private final TourValidator tourValidator;

    // Экскурсии в городе
    @GetMapping("/tours")
    public List<TourDTO> showToursInCity(@RequestParam String country,
                                         @RequestParam String city) {
        List<Tour> tours = tourService.findAllByCityNameAndCountryName(city, country);
        return mappingUtils.mapList(tours, TourDTO.class);
    }

    // Гиды в городе
    @GetMapping("/guides")
    public List<UserDTO> showGuidesInCity(@RequestParam String country,
                                          @RequestParam String city) {
        List<User> guides = userService.findAllByCityNameAndCountryName(city, country);
        return mappingUtils.mapList(guides, UserDTO.class);
    }

    // Экскурсии гида
    @GetMapping("/users/{username}/tours")
    public List<TourDTO> showGuideTours(@PathVariable String username) {
        List<Tour> tours = tourService.findAllByGuideUsername(username);
        return mappingUtils.mapList(tours, TourDTO.class);
    }

    // Конкретная экскурсия гида
    @GetMapping("/users/{username}/tours/{id}")
    public TourDTO showGuideTourInfo(@PathVariable String username,
                                     @PathVariable Long id) {
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
        if (!principal.getName().equals(username))
            throw new ForbiddenException("Нет доступа к чужой странице");

        Tour tour = mappingUtils.mapObject(requestDTO, Tour.class);

        tourValidator.validate(tour, bindingResult);
        if (bindingResult.hasErrors())
            ValidationUtils.handleCreationErrors(bindingResult);

        tourService.add(tour, username);
        return mappingUtils.mapObject(tour, TourDTO.class);
    }
}
