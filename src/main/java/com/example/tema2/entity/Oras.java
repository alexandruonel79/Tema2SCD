package com.example.tema2.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

// • [Orase] : id, id_tara, nume_oras, latitudine, longitudine (unic(id_tara, nume_oras));
@Entity
@Table(name = "`Orase`", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"id_tara", "nume_oras"})
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Oras {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "nume_oras")
    String numeOras;

    @Column
    Double latitudine;

    @Column
    Double longitudine;

    @ManyToOne
    @JoinColumn(name = "id_tara", referencedColumnName = "id")
    private Tara tara;

    @OneToMany(mappedBy = "oras", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Temperatura> temperaturi;

}
