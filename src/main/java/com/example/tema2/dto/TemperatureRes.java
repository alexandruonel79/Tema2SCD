package com.example.tema2.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TemperatureRes {
    private Integer id;
    private Double valoare;
    private Date timestamp;
}
