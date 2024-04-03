package com.example.api.rasfood.domain.repositories.specification;

import com.example.api.rasfood.domain.entities.Cardapio;
import org.springframework.data.jpa.domain.Specification;

public class CardapioSpec {

    public static Specification<Cardapio> nome(String nome){
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(root.get("nome"),"%"+nome+"%");
    }
    public static Specification<Cardapio> descricao(String descricao){
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(root.get("descricao"),"%"+descricao+"%");
    }

    public static Specification<Cardapio> categoria(Integer categoria){
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("categoria"),categoria);
    }

    public static Specification<Cardapio> disponivel(boolean disponivel){
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("disponivel"),disponivel);
    }
}
