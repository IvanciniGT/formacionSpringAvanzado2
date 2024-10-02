package com.curso.diccionario.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
// esta importación sería jakarta en lugar de javax

// JPA: Estándar que se enmarca dentro de JEE (Antiguo J2EE)
// JEE: Colección de estándares que controlan cómo montar apps Java: JDBC, JMS, JPA
// Java Enterprise Edition -> Jakarta Enterprise Edition
@Entity // Anotación de JPA indicando que este objeto es persistible
@Table( name="idiomas" )
@Data // Me incluye getters, setters, toString bonito
@SuperBuilder // Ofrecerme un patrón builder()
/*
Sin patrón builder, un idioma lo creo como:
    Idioma miIdioma = new Idioma();
    miIdioma.setId(17L);
    miIdioma.setNombre("ES");

Con builder:
    Idioma miIdioma= Idioma.builder().nombre("ES").id(17L).build();
*/
@NoArgsConstructor // Me crea constructor vacio
public class Idioma {
/*
    public Idioma(){} // @NoArgsConstructor
    public Idioma(Long id, String nombre){
        this.id = id;
        this.nombre = nombre;
    } // @AllArgsConstructor
    // @RequiredArgsConstructor sería como el All, pero solo mete los campos declarados final
*/

    @Id // Toda entidad necesita un ID... De hecho es la diferencia con respecto a los DTOs
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Generar ID en automático
    private Long id;

    @Column( nullable=false, length = 5 )
    private String nombre;

}
