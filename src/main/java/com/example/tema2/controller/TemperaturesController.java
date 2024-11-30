package com.example.tema2.controller;

import com.example.tema2.dto.TemperatureDto;
import com.example.tema2.service.TemperaturesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
