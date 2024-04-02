package com.example.api.rasfood.controllers;

import com.example.api.rasfood.entities.Categoria;
import com.example.api.rasfood.repositories.ICategoriaRepository;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin("*")
@RestController
@RequestMapping("/categoria")
public class CategoriaController {

    private ICategoriaRepository repository;
    private ObjectMapper objectMapper;

    public CategoriaController(ICategoriaRepository repository, ObjectMapper objectMapper) {
        this.repository = repository;
        this.objectMapper = objectMapper;
    }

    @GetMapping
    public ResponseEntity<List<Categoria>> findAll() {
        List<Categoria> list = this.repository.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Categoria> findById(@PathVariable final Integer id){
        Optional<Categoria> categoria = this.repository.findById(id);
        return categoria.map(value -> ResponseEntity.status(HttpStatus.OK).body(value))
                .orElseGet(()->ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Categoria> update(@PathVariable final Integer id,
                                            @RequestBody Categoria categoria) throws JsonMappingException {

        Optional<Categoria> categoriaEncontrada = this.repository.findById(id);
        if(categoriaEncontrada.isPresent()){
            this.objectMapper.updateValue(categoriaEncontrada.get(), categoria);
            Categoria categoriaAtualizada = this.repository.save(categoriaEncontrada.get());
            return ResponseEntity.status(HttpStatus.OK).body(categoriaAtualizada);
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable final Integer id){
        Optional<Categoria> categoria = this.repository.findById(id);
        if (!categoria.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Recurso não encontrado");
        }

        this.repository.deleteById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PostMapping("/")
    public ResponseEntity<Categoria> insert(@RequestBody final Categoria categoria){
        Categoria value = this.repository.save(categoria);
        return ResponseEntity.status(HttpStatus.OK).body(value);
    }

}
