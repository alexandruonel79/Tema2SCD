package com.example.tema2.service;

import com.example.tema2.dto.IdRes;
import com.example.tema2.dto.TemperatureDto;
import com.example.tema2.dto.TemperatureDtoUpdate;
import com.example.tema2.dto.TemperatureRes;
import com.example.tema2.entity.Oras;
import com.example.tema2.entity.Tara;
import com.example.tema2.entity.Temperatura;
import com.example.tema2.repository.OrasRepository;
import com.example.tema2.repository.TaraRepository;
import com.example.tema2.repository.TemperaturaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TemperaturesService {
    private final TemperaturaRepository temperaturaRepository;
    private final OrasRepository orasRepository;
    private final TaraRepository taraRepository;

    @Autowired
    public TemperaturesService(TemperaturaRepository temperaturaRepository, OrasRepository orasRepository,
                               TaraRepository taraRepository) {
        this.temperaturaRepository = temperaturaRepository;
        this.orasRepository = orasRepository;
        this.taraRepository = taraRepository;
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
        temperatura.setOras(oras);
        // save the temperature in the db
        temperatura = temperaturaRepository.save(temperatura);
        IdRes idRes = new IdRes();
        idRes.setId(temperatura.getId());

        return new ResponseEntity<>(idRes, HttpStatus.CREATED);
    }

    public ResponseEntity<?> getAllTemperatures(Double lat, Double lon, LocalDate from, LocalDate until) {
        List<Oras> orasList = orasRepository.findAll();
        List<Temperatura> temperaturaListFilteredLatAndLon = new ArrayList<>();
        for (Oras oras : orasList){
            if (lat != null && lon != null){
                if (oras.getLatitudine().equals(lat) && oras.getLongitudine().equals(lon)){
                    temperaturaListFilteredLatAndLon.addAll(oras.getTemperaturi());
                }
            }
            else if (lat != null){
                if (oras.getLatitudine().equals(lat)){
                    temperaturaListFilteredLatAndLon.addAll(oras.getTemperaturi());
                }
            }
            else if (lon != null){
                if (oras.getLongitudine().equals(lon)){
                    temperaturaListFilteredLatAndLon.addAll(oras.getTemperaturi());
                }
            }
            else {
                temperaturaListFilteredLatAndLon.addAll(oras.getTemperaturi());
            }
        }

        List<Temperatura> filteredList = new ArrayList<>();

        for (Temperatura temp : temperaturaListFilteredLatAndLon){
            LocalDate localDate = temp.getTimestamp().toLocalDateTime().toLocalDate();
            if (from != null && until != null){
                    if (localDate.isAfter(from) && localDate.isBefore(until)){
                        filteredList.add(temp);
                    }
            }
            else if (from != null){
                if (localDate.isAfter(from)){
                    filteredList.add(temp);
                }
            }
            else if (until != null){
                if (localDate.isBefore(until)){
                    filteredList.add(temp);
                }
            }
            else {
                filteredList.add(temp);
            }
        }

        List<TemperatureRes> temperatureResList = new ArrayList<>();
        for (Temperatura temp : filteredList){
            TemperatureRes temperatureRes = new TemperatureRes();
            temperatureRes.setId(temp.getId());
            temperatureRes.setValoare(temp.getValoare());
            temperatureRes.setTimestamp(temp.getTimestamp());
            temperatureResList.add(temperatureRes);
        }

        return new ResponseEntity<>(temperatureResList, HttpStatus.OK);
    }

    public ResponseEntity<?> getAllCitiesTemperatures(LocalDate from, LocalDate until, Integer cityId) {
        Optional<Oras> oras = orasRepository.findById(cityId);
        // not sending 404 or another error
        if (oras.isEmpty()){
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK);
        }

        List<Temperatura> temperaturaList = orasRepository.findById(cityId).get().getTemperaturi();
        List<Temperatura> filteredList = new ArrayList<>();

        for (Temperatura temp : temperaturaList){
            LocalDate localDate = temp.getTimestamp().toLocalDateTime().toLocalDate();

            if (from != null && until != null){
                if (localDate.isAfter(from) && localDate.isBefore(until)){
                    filteredList.add(temp);
                }
            }
            else if (from != null){
                if (localDate.isAfter(from)){
                    filteredList.add(temp);
                }
            }
            else if (until != null){
                if (localDate.isBefore(until)){
                    filteredList.add(temp);
                }
            }
            else {
                filteredList.add(temp);
            }
        }

        List<TemperatureRes> temperatureResList = new ArrayList<>();
        for (Temperatura temp : filteredList){
            TemperatureRes temperatureRes = new TemperatureRes();
            temperatureRes.setId(temp.getId());
            temperatureRes.setValoare(temp.getValoare());
            temperatureRes.setTimestamp(temp.getTimestamp());
            temperatureResList.add(temperatureRes);
        }

        return new ResponseEntity<>(temperatureResList, HttpStatus.OK);
    }

    public ResponseEntity<?> getAllCountriesTemperatures(LocalDate from, LocalDate until, Integer countryId) {
        Optional<Tara> taraOptional = taraRepository.findById(countryId);

        if (taraOptional.isEmpty()){
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK);
        }

        Tara tara = taraOptional.get();
        List<Temperatura> temperaturaList = new ArrayList<>();

        for (Oras oras : tara.getOrase()){
            temperaturaList.addAll(oras.getTemperaturi());
        }

        List<Temperatura> filteredList = new ArrayList<>();

        for (Temperatura temp : temperaturaList){
            LocalDate localDate = temp.getTimestamp().toLocalDateTime().toLocalDate();

            if (from != null && until != null){
                if (localDate.isAfter(from) && localDate.isBefore(until)){
                    filteredList.add(temp);
                }
            }
            else if (from != null){
                if (localDate.isAfter(from)){
                    filteredList.add(temp);
                }
            }
            else if (until != null){
                if (localDate.isBefore(until)){
                    filteredList.add(temp);
                }
            }
            else {
                filteredList.add(temp);
            }
        }
        List<TemperatureRes> temperatureResList = new ArrayList<>();
        for (Temperatura temp : filteredList){
            TemperatureRes temperatureRes = new TemperatureRes();
            temperatureRes.setId(temp.getId());
            temperatureRes.setValoare(temp.getValoare());
            temperatureRes.setTimestamp(temp.getTimestamp());
            temperatureResList.add(temperatureRes);
        }

        return new ResponseEntity<>(temperatureResList, HttpStatus.OK);
    }

    public ResponseEntity<?> updateTemperature(Integer id, TemperatureDtoUpdate temperatureDtoUpdate) {
        if (!id.equals(temperatureDtoUpdate.getId())){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Optional<Temperatura> temperaturaOptional = temperaturaRepository.findById(id);
        Optional<Oras> orasOptional = orasRepository.findById(temperatureDtoUpdate.getId_oras());

        if (temperaturaOptional.isEmpty() || orasOptional.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        List<Temperatura> temperaturaList = temperaturaRepository.findAll();
        // check if a temperature with the same ciy_id and timestamp exist
        for (Temperatura temperatura: temperaturaList){
            if (temperatura.getOras().getId().equals(temperatureDtoUpdate.getId_oras())  &&
                temperatura.getTimestamp().equals(new Timestamp(System.currentTimeMillis()))
            ){
                return new ResponseEntity<>(HttpStatus.CONFLICT);
            }
        }

        // update the temperature
        Temperatura temperatura = temperaturaOptional.get();
        temperatura.setOras(orasOptional.get());
        temperatura.setValoare(temperatureDtoUpdate.getValoare());
        temperaturaRepository.save(temperatura);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    public ResponseEntity<?> deleteTemperature(Integer id) {
        Optional<Temperatura> temperaturaOptional = temperaturaRepository.findById(id);

        if (temperaturaOptional.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        temperaturaRepository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
