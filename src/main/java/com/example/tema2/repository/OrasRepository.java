package com.example.tema2.repository;

import com.example.tema2.entity.Oras;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrasRepository extends JpaRepository<Oras, Integer> {
}
