package com.example.tema2.controller;

import com.example.tema2.dto.CountryDto;
import com.example.tema2.dto.CountryRes;
import com.example.tema2.service.CountriesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/countries")
public class CountriesController {

    private final CountriesService countriesService;

    @Autowired
    public CountriesController(CountriesService countriesService) {
        this.countriesService = countriesService;
    }

    @PostMapping
    public ResponseEntity<?> addCountry(@RequestBody CountryDto countryDto){
        return countriesService.addCountry(countryDto);
    }

    @GetMapping()
    public ResponseEntity<?> getAllCountries(){
        return countriesService.getAllCountries();
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCountry(@PathVariable Integer id, @RequestBody CountryRes countryDto){
        return countriesService.updateCountry(id, countryDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCountry(@PathVariable Integer id){
        return countriesService.deleteCountry(id);
    }

}
