package com.example.api.rasfood.repositories;

import com.example.api.rasfood.entities.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ICategoriaRepository extends JpaRepository <Categoria, Integer> {
}
