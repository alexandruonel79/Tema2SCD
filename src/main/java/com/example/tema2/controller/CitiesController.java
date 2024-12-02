package com.example.tema2.controller;

import com.example.tema2.dto.CityDto;
import com.example.tema2.dto.CityRes;
import com.example.tema2.dto.CountryDto;
import com.example.tema2.service.CitiesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cities")
public class CitiesController {
    private final CitiesService citiesService;

    @Autowired
    public CitiesController(CitiesService citiesService) {
        this.citiesService = citiesService;
    }

    @PostMapping
    public ResponseEntity<?> addCity(@RequestBody CityDto cityDto){
        return citiesService.addCity(cityDto);
    }

    @GetMapping
    public ResponseEntity<?> getAllCities(){
        return citiesService.getAllCities();
    }

    @GetMapping("/country/{countryId}")
    public ResponseEntity<?> getCitiesByCountry(@PathVariable Integer countryId){
        return citiesService.getCitiesByCountry(countryId);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCity(@PathVariable Integer id, @RequestBody CityRes cityDto){
        return citiesService.updateCity(id, cityDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCity(@PathVariable Integer id){
        return citiesService.deleteCity(id);
    }


}
