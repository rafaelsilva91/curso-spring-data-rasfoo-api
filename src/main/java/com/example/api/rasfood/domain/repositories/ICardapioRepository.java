package com.example.api.rasfood.domain.repositories;

import com.example.api.rasfood.domain.entities.Cardapio;
import com.example.api.rasfood.domain.repositories.projection.CardapioProjection;
import com.example.api.rasfood.dto.CardapioDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ICardapioRepository extends JpaRepository<Cardapio, Integer>, PagingAndSortingRepository<Cardapio, Integer> {


    @Query(value =" SELECT * FROM CARDAPIO C" +
                  " WHERE C.CATEGORIA_ID = ?1" +
                  " AND C.DISPONIVEL = true", nativeQuery = true, countQuery = "SELECT count(*) FROM cardapio")
    Page<Cardapio> findAllByCategoria(final Integer categoria, final Pageable pageable);

    @Query("SELECT new com.example.api.rasfood.dto.CardapioDto(" +
            " c.nome, " +
            " c.descricao, " +
            " c.valor, " +
            " c.categoria.nome) " +
            " c FROM Cardapio c " +
            " WHERE c.nome LIKE %:nome% " +
            " AND c.disponivel = true")
    Page<CardapioDto> findAllByNome(final String nome, final Pageable pageable);

    //Usando native_query
    @Query(value = "SELECT" +
            " c.nome as nome," +
            " c.descricao as descricao," +
            " c.valor as valor," +
            " cat.nome as nomeCategoria" +
            " FROM Cardapio c" +
            " INNER JOIN Categorias cat" +
            " ON c.categoria_id = cat.id" +
            " WHERE 1 = 1" +
            " AND c.disponivel = true" +
            " AND c.categoria_id = ?1", nativeQuery = true, countQuery = "SELECT count(*) FROM cardapio")
    Page<CardapioProjection> findAllByCategoriasId(final Integer categoria, final Pageable pageable);

}
