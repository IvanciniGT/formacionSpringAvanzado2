package com.curso.diccionario.repository;

import com.curso.diccionario.entity.Significado;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RepositorioSignificados extends JpaRepository<Significado, Long> {
}
