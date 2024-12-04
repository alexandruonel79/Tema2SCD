package com.example.tema2.controller;

import com.example.tema2.dto.TemperatureDto;
import com.example.tema2.dto.TemperatureDtoUpdate;
import com.example.tema2.entity.Temperatura;
import com.example.tema2.service.TemperaturesService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Optional;

@RestController
@RequestMapping("/api/temperatures")
public class TemperaturesController {

    private final TemperaturesService temperaturesService;

    @Autowired
    public TemperaturesController(TemperaturesService temperaturesService) {
        this.temperaturesService = temperaturesService;
    }

    @PostMapping
    public ResponseEntity<?> addTemperature(@RequestBody TemperatureDto temperatureDto){
        return temperaturesService.addTemperature(temperatureDto);
    }

    @GetMapping
    public ResponseEntity<?> getAllTemperatures(@RequestParam(required = false) Double lat,
                                                @RequestParam(required = false) Double lon,
                                                @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
                                                @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate until
                                                ){
        return temperaturesService.getAllTemperatures(lat, lon, from, until);
    }

    @GetMapping("/cities/{cityId}")
    public ResponseEntity<?> getAllCitiesTemperatures(@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
                                                      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate until,
                                                      @PathVariable Integer cityId
    ){
        return temperaturesService.getAllCitiesTemperatures(from, until, cityId);
    }

    @GetMapping("/cities/")
    public ResponseEntity<?> getAllCitiesTemperaturesWithoutId(@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
                                                      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate until){
        return temperaturesService.getAllCitiesTemperatures(from, until, null);
    }

    @GetMapping("/countries/{countryId}")
    public ResponseEntity<?> getAllCountriesTemperatures(@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
                                                         @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate until,
                                                         @PathVariable Integer countryId
    ){
        return temperaturesService.getAllCountriesTemperatures(from, until, countryId);
    }

    @GetMapping("/countries/")
    public ResponseEntity<?> getAllCountriesTemperaturesWithoutId(@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
                                                         @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate until){
        return temperaturesService.getAllCountriesTemperatures(from, until, null);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateTemperature(@PathVariable Integer id, @RequestBody TemperatureDtoUpdate temperatureDto){
        return temperaturesService.updateTemperature(id, temperatureDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTemperature(@PathVariable Integer id){
        return temperaturesService.deleteTemperature(id);
    }

}
