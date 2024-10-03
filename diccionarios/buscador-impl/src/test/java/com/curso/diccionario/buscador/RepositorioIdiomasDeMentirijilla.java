package com.curso.diccionario.buscador;

import com.curso.diccionario.entity.Idioma;
import com.curso.diccionario.repository.RepositorioIdiomas;
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
public class RepositorioIdiomasDeMentirijilla implements RepositorioIdiomas {
    @Override
    public Optional<Idioma> findByNombre(String idioma) {
        if(idioma == null) throw new NullPointerException();
        if(idioma.equals("ES")) return Optional.of(Idioma.builder().nombre("ES").id(17L).build());
        return Optional.empty();
    }

    @Override
    public Optional<List<Idioma>> findByNombreStartingWith(String prefijo) {
        return Optional.empty();
    }

    @Override
    public List<Idioma> findAll() {
        return List.of();
    }

    @Override
    public List<Idioma> findAll(Sort sort) {
        return List.of();
    }

    @Override
    public Page<Idioma> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public List<Idioma> findAllById(Iterable<Long> longs) {
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
    public void delete(Idioma entity) {

    }

    @Override
    public void deleteAllById(Iterable<? extends Long> longs) {

    }

    @Override
    public void deleteAll(Iterable<? extends Idioma> entities) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public <S extends Idioma> S save(S entity) {
        return null;
    }

    @Override
    public <S extends Idioma> List<S> saveAll(Iterable<S> entities) {
        return List.of();
    }

    @Override
    public Optional<Idioma> findById(Long aLong) {
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
    public <S extends Idioma> S saveAndFlush(S entity) {
        return null;
    }

    @Override
    public <S extends Idioma> List<S> saveAllAndFlush(Iterable<S> entities) {
        return List.of();
    }

    @Override
    public void deleteAllInBatch(Iterable<Idioma> entities) {

    }

    @Override
    public void deleteAllByIdInBatch(Iterable<Long> longs) {

    }

    @Override
    public void deleteAllInBatch() {

    }

    @Override
    public Idioma getOne(Long aLong) {
        return null;
    }

    @Override
    public Idioma getById(Long aLong) {
        return null;
    }

    @Override
    public Idioma getReferenceById(Long aLong) {
        return null;
    }

    @Override
    public <S extends Idioma> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends Idioma> List<S> findAll(Example<S> example) {
        return List.of();
    }

    @Override
    public <S extends Idioma> List<S> findAll(Example<S> example, Sort sort) {
        return List.of();
    }

    @Override
    public <S extends Idioma> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends Idioma> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends Idioma> boolean exists(Example<S> example) {
        return false;
    }

    @Override
    public <S extends Idioma, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        return null;
    }
}
