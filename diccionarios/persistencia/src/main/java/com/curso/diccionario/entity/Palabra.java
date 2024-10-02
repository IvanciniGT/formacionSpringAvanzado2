package com.curso.diccionario.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty; // Gracias a la dependencia de validations
import java.util.List;

@Entity
@Table( name="palabras" )
@Data
@SuperBuilder
@NoArgsConstructor
public class Palabra {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column( nullable=false, length = 50 ) // IF en JAVA
    @NotEmpty( message = "La palabra no puede estar vacía")
    private String palabra;

    @ManyToOne
    private Idioma idioma;

    @OneToMany(mappedBy = "palabra")
    private List<Significado> significados;
}
