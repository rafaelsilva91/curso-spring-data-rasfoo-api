package com.example.api.rasfood.domain.repositories;

import com.example.api.rasfood.domain.entities.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ICategoriaRepository extends JpaRepository <Categoria, Integer> {
}
