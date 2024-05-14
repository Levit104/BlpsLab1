package levit104.blss.labs.controllers;

import jakarta.validation.Valid;
import levit104.blss.labs.dto.TourCreationDTO;
import levit104.blss.labs.dto.TourDTO;
import levit104.blss.labs.dto.UserDTO;
import levit104.blss.labs.models.primary.Tour;
import levit104.blss.labs.models.primary.User;
import levit104.blss.labs.services.TourService;
import levit104.blss.labs.services.UserService;
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
public class TourController {
    private final MappingHelper mappingHelper;
    private final UserService userService;
    private final TourService tourService;

    // Экскурсии в городе
    @GetMapping("/tours")
    public List<TourDTO> showToursInCity(
            @RequestParam String country,
            @RequestParam String city,
            @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        List<Tour> tours = tourService.getAllByCityNameAndCountryName(city, country, pageable);
        return mappingHelper.mapList(tours, TourDTO.class);
    }

    // Гиды в городе
    @GetMapping("/guides")
    public List<UserDTO> showGuidesInCity(
            @RequestParam String country,
            @RequestParam String city,
            @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        List<User> guides = userService.getAllByCityNameAndCountryName(city, country, pageable);
        return mappingHelper.mapList(guides, UserDTO.class);
    }

    // Экскурсии гида
    @GetMapping("/users/{username}/tours")
    public List<TourDTO> showGuideTours(
            @PathVariable String username,
            @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        List<Tour> tours = tourService.getAllByGuideUsername(username, pageable);
        return mappingHelper.mapList(tours, TourDTO.class);
    }

    // Конкретная экскурсия гида
    @GetMapping("/users/{username}/tours/{id}")
    public TourDTO showGuideTourInfo(@PathVariable String username, @PathVariable Long id) {
        Tour tour = tourService.getByIdAndGuideUsername(id, username);
        return mappingHelper.mapObject(tour, TourDTO.class);
    }

    // Добавить экскурсию
    @PreAuthorize("hasRole('GUIDE') && principal.username == #username")
    @PostMapping("/users/{username}/tours")
    @ResponseStatus(HttpStatus.CREATED)
    public TourDTO addTour(
            @PathVariable String username,
            @RequestBody @Valid TourCreationDTO requestDTO,
            BindingResult bindingResult
    ) {
        Tour tour = mappingHelper.mapObject(requestDTO, Tour.class);
        tourService.add(tour, username, bindingResult);
        return mappingHelper.mapObject(tour, TourDTO.class);
    }
}
