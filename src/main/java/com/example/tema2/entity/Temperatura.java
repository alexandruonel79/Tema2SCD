package com.example.tema2.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;

// • [Temperaturi] : id, valoare, timestamp, id_oras (unic(id_oras, timestamp)).
@Entity
@Table(name = "`Temperaturi`", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"id_oras", "timestamp"})
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Temperatura {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    Double valoare;

    @CreationTimestamp
    @Column
    private Timestamp timestamp;

    @ManyToOne
    @JoinColumn(name = "id_oras", referencedColumnName = "id")
    private Oras oras;
}
