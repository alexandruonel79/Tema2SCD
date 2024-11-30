package com.example.tema2.repository;

import com.example.tema2.entity.Tara;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TaraRepository extends JpaRepository<Tara, Integer> {
    boolean existsByNumeTara(String numeTara);
    boolean existsByLongitudineAndLatitudine(Double longitudine, Double latitudine);
}
