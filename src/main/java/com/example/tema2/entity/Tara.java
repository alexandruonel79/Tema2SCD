package com.example.tema2.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

//    • [Tari] : id, nume_tara, latitudine, longitudine (unic(nume_tara));
@Entity
@Table(name = "`Tari`", uniqueConstraints = {
        @UniqueConstraint(columnNames = "nume_tara")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Tara {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true, name = "nume_tara")
    String numeTara;

    @Column
    Double latitudine;

    @Column
    Double longitudine;

    @OneToMany(mappedBy = "tara", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Oras> orase;
}
