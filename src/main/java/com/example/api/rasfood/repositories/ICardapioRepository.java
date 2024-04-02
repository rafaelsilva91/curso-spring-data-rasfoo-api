package com.example.api.rasfood.repositories;

import com.example.api.rasfood.entities.Cardapio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ICardapioRepository extends JpaRepository<Cardapio, Integer> {


    @Query(value =" SELECT * FROM CARDAPIO C" +
                  " WHERE C.CATEGORIA_ID = ?1" +
                  " AND C.DISPONIVEL = true", nativeQuery = true)
    List<Cardapio> findAllByCategoria(final Integer categoria);

}
