package com.example.tema2.service;

import com.example.tema2.dto.CityDto;
import com.example.tema2.dto.CityRes;
import com.example.tema2.dto.IdRes;
import com.example.tema2.entity.Oras;
import com.example.tema2.entity.Tara;
import com.example.tema2.repository.OrasRepository;
import com.example.tema2.repository.TaraRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CitiesService {
    private final OrasRepository orasRepository;
    private final TaraRepository taraRepository;

    @Autowired
    public CitiesService(OrasRepository orasRepository, TaraRepository taraRepository) {
        this.orasRepository = orasRepository;
        this.taraRepository = taraRepository;
    }


    public ResponseEntity<?> addCity(CityDto cityDto) {
        // check if the country with the given id exists
        if (!taraRepository.existsById(cityDto.getIdTara())){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Optional<Tara> orasList = taraRepository.findById(cityDto.getIdTara());
        if (!orasList.isEmpty() &&
                orasList.get().getOrase().stream().anyMatch(oras -> oras.getNumeOras().equals(cityDto.getNume()))){
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

//        if (orasRepository.existsByNumeOras(cityDto.getNume())){
//            return new ResponseEntity<>(HttpStatus.CONFLICT);
//        }

//        if (orasRepository.existsByLongitudineAndLatitudine(cityDto.getLon(), cityDto.getLat())){
//            return new ResponseEntity<>(HttpStatus.CONFLICT);
//        }

        Oras oras = new Oras();
        oras.setNumeOras(cityDto.getNume());
        oras.setLatitudine(cityDto.getLat());
        oras.setLongitudine(cityDto.getLon());
        oras.setTara(taraRepository.findById(cityDto.getIdTara()).get());

        oras = orasRepository.save(oras);
        IdRes idRes = new IdRes();
        idRes.setId(oras.getId());

        return new ResponseEntity<>(idRes, HttpStatus.CREATED);
    }

    public ResponseEntity<?> getAllCities() {
        List<Oras> orasList = orasRepository.findAll();
        List<CityRes> cityResList = new ArrayList<>();

        for (Oras oras: orasList){
            CityRes cityRes = new CityRes();
            cityRes.setId(oras.getId());
            cityRes.setNume(oras.getNumeOras());
            cityRes.setLat(oras.getLatitudine());
            cityRes.setLon(oras.getLongitudine());
            cityRes.setIdTara(oras.getTara().getId());
            cityResList.add(cityRes);
        }

        return new ResponseEntity<>(cityResList, HttpStatus.OK);
    }

    public ResponseEntity<?> getCitiesByCountry(Integer countryId) {
        Optional<Tara> taraOptional = taraRepository.findById(countryId);

        if (taraOptional.isEmpty()){
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK);
        }

        List<Oras> orasList = taraOptional.get().getOrase();
        List<CityRes> cityResList = new ArrayList<>();

        for (Oras oras: orasList){
            CityRes cityRes = new CityRes();
            cityRes.setId(oras.getId());
            cityRes.setNume(oras.getNumeOras());
            cityRes.setLat(oras.getLatitudine());
            cityRes.setLon(oras.getLongitudine());
            cityRes.setIdTara(oras.getTara().getId());
            cityResList.add(cityRes);
        }

        return new ResponseEntity<>(cityResList, HttpStatus.OK);
    }

    public ResponseEntity<?> updateCity(Integer id, CityDto cityDto) {
        Optional<Oras> orasOptional = orasRepository.findById(id);

        if (orasOptional.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Oras oras = orasOptional.get();
        ///TODO CONTINUARE



    }
}
