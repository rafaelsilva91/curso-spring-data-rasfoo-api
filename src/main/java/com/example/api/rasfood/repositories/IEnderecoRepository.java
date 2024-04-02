package com.example.api.rasfood.repositories;

import com.example.api.rasfood.entities.Endereco;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IEnderecoRepository extends JpaRepository<Endereco, Integer> {

    @Query("SELECT e FROM Endereco e WHERE e.cep = :cep")
    List<Endereco> consultarPorCep(@Param("cep") final String cep);

    List<Endereco> findByCep(String value);
}
