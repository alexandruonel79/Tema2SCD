package com.example.tema2.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TemperatureDtoUpdate {
    private Integer id;
    private Integer idOras;
    private Double valoare;
}
