package com.example.tema2.controller;

import com.example.tema2.dto.TemperatureDto;
import com.example.tema2.service.TemperaturesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

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
    public ResponseEntity<?> getAllTemperatures(@PathVariable(required = false) Double lat,
                                                @PathVariable(required = false) Double lon,
                                                @PathVariable(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
                                                @PathVariable(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate until
                                                ){
        return temperaturesService.getAllTemperatures(lat, lon, from, until);
    }
}
