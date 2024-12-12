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
import org.springframework.dao.DataIntegrityViolationException;
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
        // check if anything is null
        if (temperatureDto.getIdOras() == null || temperatureDto.getValoare() == null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        // not found error
        if (!orasRepository.existsById(temperatureDto.getIdOras())){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        // get the city with the given id
        Oras oras = orasRepository.findById(temperatureDto.getIdOras()).get();

        Temperatura temperatura = new Temperatura();
        temperatura.setValoare(temperatureDto.getValoare());
        temperatura.setOras(oras);

        try{
            temperatura = temperaturaRepository.save(temperatura);
        }
        catch (DataIntegrityViolationException e){
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        IdRes idRes = new IdRes();
        idRes.setId(temperatura.getId());

        return new ResponseEntity<>(idRes, HttpStatus.CREATED);
    }

    public ResponseEntity<?> getAllTemperatures(Double lat, Double lon, LocalDate from, LocalDate until) {
        List<Oras> orasList = orasRepository.findAll();
        List<Temperatura> temperaturaListFilteredLatAndLon = new ArrayList<>();
        // filter just the latitude and longitude
        // add them in the list if they match
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
        // now filter the list on from and until
        // finally, add them to the filtered list
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
        // format the response
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
        // not specified, send an empty list
        if (cityId == null){
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK);
        }

        Optional<Oras> oras = orasRepository.findById(cityId);
        // not sending 404 or another error
        if (oras.isEmpty()){
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK);
        }

        List<Temperatura> temperaturaList = orasRepository.findById(cityId).get().getTemperaturi();
        List<Temperatura> filteredList = new ArrayList<>();
        // same idea as above
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
        // format the response
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
        // not specified, send an empty list
        if (countryId == null){
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK);
        }

        Optional<Tara> taraOptional = taraRepository.findById(countryId);
        // not sending 404 or another error
        // should be 404
        if (taraOptional.isEmpty()){
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK);
        }

        Tara tara = taraOptional.get();
        List<Temperatura> temperaturaList = new ArrayList<>();
        // add each city's temperatures to the list
        for (Oras oras : tara.getOrase()){
            temperaturaList.addAll(oras.getTemperaturi());
        }

        List<Temperatura> filteredList = new ArrayList<>();
        // filter them using the same idea as above
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
        // format the response
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
        // check if anything is null
        if (temperatureDtoUpdate.getId_oras() == null || temperatureDtoUpdate.getValoare() == null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        // double check the ids
        if (!id.equals(temperatureDtoUpdate.getId())){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Optional<Temperatura> temperaturaOptional = temperaturaRepository.findById(id);
        Optional<Oras> orasOptional = orasRepository.findById(temperatureDtoUpdate.getId_oras());
        // if the temperature or the city mentioned does not exist, return 404
        if (temperaturaOptional.isEmpty() || orasOptional.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        // update the temperature
        Temperatura temperatura = temperaturaOptional.get();
        temperatura.setOras(orasOptional.get());
        temperatura.setValoare(temperatureDtoUpdate.getValoare());
        try{
            temperaturaRepository.save(temperatura);
        }
        catch (DataIntegrityViolationException e){
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    public ResponseEntity<?> deleteTemperature(Integer id) {
        if (id == null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Optional<Temperatura> temperaturaOptional = temperaturaRepository.findById(id);

        if (temperaturaOptional.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        temperaturaRepository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
