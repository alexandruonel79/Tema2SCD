package com.example.tema2.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CityRes {
    private Integer id;
    private Integer idTara;
    private String nume;
    private Double lat;
    private Double lon;
}
