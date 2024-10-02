package com.curso.diccionario.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

@Entity
@Table( name="significados" )
@Data
@SuperBuilder
@NoArgsConstructor
public class Significado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column( nullable=false, length = 500 )
    @NotEmpty( message = "El significado no puede estar vacío")
    private String significado;
}