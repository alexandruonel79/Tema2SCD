package com.example.tema2.service;

import com.example.tema2.dto.CountryDto;
import com.example.tema2.dto.CountryRes;
import com.example.tema2.dto.IdRes;
import com.example.tema2.entity.Tara;
import com.example.tema2.repository.TaraRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CountriesService {

    private final TaraRepository taraRepository;

    @Autowired
    public CountriesService(TaraRepository taraRepository) {
        this.taraRepository = taraRepository;
    }

    public ResponseEntity<?> addCountry(CountryDto countryDto){
        if (taraRepository.existsByNumeTara(countryDto.getNume())){
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

//        if (taraRepository.existsByLongitudineAndLatitudine(countryDto.getLon(), countryDto.getLat())){
//            return new ResponseEntity<>(HttpStatus.CONFLICT);
//        }

        // create a new entity from the dto
        Tara tara = new Tara();
        tara.setNumeTara(countryDto.getNume());
        tara.setLatitudine(countryDto.getLat());
        tara.setLongitudine(countryDto.getLon());

        // save the country in the db
        tara = taraRepository.save(tara);

        IdRes countryRes = new IdRes();
        countryRes.setId(tara.getId());

        return new ResponseEntity<>(countryRes, HttpStatus.CREATED);
    }

    public ResponseEntity<?> getAllCountries() {
        List<Tara> countriesList = taraRepository.findAll();
        List<CountryRes> countryResList = new ArrayList<>();

        for (Tara country : countriesList) {
            CountryRes countryRes = new CountryRes();
            countryRes.setId(country.getId());
            countryRes.setNume(country.getNumeTara());
            countryRes.setLat(country.getLatitudine());
            countryRes.setLon(country.getLongitudine());
            countryResList.add(countryRes);
        }

        return new ResponseEntity<>(countryResList, HttpStatus.OK);
    }

    public ResponseEntity<?> updateCountry(Integer id, CountryRes countryDto) {
        if (!id.equals(countryDto.getId())){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Optional<Tara> taraOptional = taraRepository.findById(id);
        if (taraOptional.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Tara tara = taraOptional.get();
        tara.setNumeTara(countryDto.getNume());
        tara.setLatitudine(countryDto.getLat());
        tara.setLongitudine(countryDto.getLon());
        taraRepository.save(tara);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    public ResponseEntity<?> deleteCountry(Integer id) {
        // check if it exists
        if(!taraRepository.existsById(id)){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        taraRepository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
