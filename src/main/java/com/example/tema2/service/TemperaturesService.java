package com.example.tema2.service;

import com.example.tema2.dto.IdRes;
import com.example.tema2.dto.TemperatureDto;
import com.example.tema2.entity.Oras;
import com.example.tema2.entity.Temperatura;
import com.example.tema2.repository.OrasRepository;
import com.example.tema2.repository.TemperaturaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

@Service
public class TemperaturesService {
    private final TemperaturaRepository temperaturaRepository;
    private final OrasRepository orasRepository;

    @Autowired
    public TemperaturesService(TemperaturaRepository temperaturaRepository, OrasRepository orasRepository) {
        this.temperaturaRepository = temperaturaRepository;
        this.orasRepository = orasRepository;
    }


    public ResponseEntity<?> addTemperature(TemperatureDto temperatureDto) {
        // check if the city with give id exists
        if (!orasRepository.existsById(temperatureDto.getId_oras())){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        // get the city with the given id
        Oras oras = orasRepository.findById(temperatureDto.getId_oras()).get();

        // check if the temperature already exists, in the same city, with the same timestamp
        for (Temperatura temp : oras.getTemperaturi()){
            if (temp.getValoare().equals(temperatureDto.getValoare()) &&
            temp.getTimestamp().equals(new Timestamp(System.currentTimeMillis()))){
                return new ResponseEntity<>(HttpStatus.CONFLICT);
            }
        }

        // create a new entity from the dto
        Temperatura temperatura = new Temperatura();
        temperatura.setValoare(temperatureDto.getValoare());
        // save the temperature in the db
        temperatura = temperaturaRepository.save(temperatura);
        IdRes idRes = new IdRes();
        idRes.setId(temperatura.getId());
        return new ResponseEntity<>(idRes, HttpStatus.CREATED);
    }
}
