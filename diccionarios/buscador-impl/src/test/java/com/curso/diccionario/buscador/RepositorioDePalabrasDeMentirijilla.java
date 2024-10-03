package com.curso.diccionario.buscador;

import com.curso.diccionario.entity.Idioma;
import com.curso.diccionario.entity.Palabra;
import com.curso.diccionario.repository.RepositorioPalabras;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

//@Repository
//@Primary
public class RepositorioDePalabrasDeMentirijilla implements RepositorioPalabras {
    @Override
    public Optional<Palabra> findByPalabraAndIdioma(String palabra, Idioma idioma) {
        if(idioma == null) throw new NullPointerException();
        if(palabra == null) throw new NullPointerException();
        if(!idioma.equals("ES")) return Optional.empty();
        if(!palabra.equals("manzana")) return Optional.empty();
        return Optional.of(Palabra.builder().palabra("manzana").id(19L).idioma(idioma).build());
    }

    @Override
    public List<Palabra> findAll() {
        return List.of();
    }

    @Override
    public List<Palabra> findAll(Sort sort) {
        return List.of();
    }

    @Override
    public Page<Palabra> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public List<Palabra> findAllById(Iterable<Long> longs) {
        return List.of();
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void deleteById(Long aLong) {

    }

    @Override
    public void delete(Palabra entity) {

    }

    @Override
    public void deleteAllById(Iterable<? extends Long> longs) {

    }

    @Override
    public void deleteAll(Iterable<? extends Palabra> entities) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public <S extends Palabra> S save(S entity) {
        return null;
    }

    @Override
    public <S extends Palabra> List<S> saveAll(Iterable<S> entities) {
        return List.of();
    }

    @Override
    public Optional<Palabra> findById(Long aLong) {
        return Optional.empty();
    }

    @Override
    public boolean existsById(Long aLong) {
        return false;
    }

    @Override
    public void flush() {

    }

    @Override
    public <S extends Palabra> S saveAndFlush(S entity) {
        return null;
    }

    @Override
    public <S extends Palabra> List<S> saveAllAndFlush(Iterable<S> entities) {
        return List.of();
    }

    @Override
    public void deleteAllInBatch(Iterable<Palabra> entities) {

    }

    @Override
    public void deleteAllByIdInBatch(Iterable<Long> longs) {

    }

    @Override
    public void deleteAllInBatch() {

    }

    @Override
    public Palabra getOne(Long aLong) {
        return null;
    }

    @Override
    public Palabra getById(Long aLong) {
        return null;
    }

    @Override
    public Palabra getReferenceById(Long aLong) {
        return null;
    }

    @Override
    public <S extends Palabra> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends Palabra> List<S> findAll(Example<S> example) {
        return List.of();
    }

    @Override
    public <S extends Palabra> List<S> findAll(Example<S> example, Sort sort) {
        return List.of();
    }

    @Override
    public <S extends Palabra> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends Palabra> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends Palabra> boolean exists(Example<S> example) {
        return false;
    }

    @Override
    public <S extends Palabra, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        return null;
    }
}
