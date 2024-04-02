package com.example.api.rasfood.controllers;

import com.example.api.rasfood.entities.Cardapio;
import com.example.api.rasfood.repositories.ICardapioRepository;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin("*")
@RestController
@RequestMapping("/cardapio")
public class CardapioController {

    private ICardapioRepository repository;
    private ObjectMapper objectMapper;
    public CardapioController(ICardapioRepository repository, ObjectMapper objectMapper) {
        this.repository = repository;
        this.objectMapper = objectMapper;
    }

    @GetMapping
    public ResponseEntity<List<Cardapio>> findAll(){
        List<Cardapio> cardapioList = this.repository.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(cardapioList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cardapio> findById(@PathVariable final Integer id){
        Optional<Cardapio> itemCardapio = this.repository.findById(id);
        return  itemCardapio.map(value -> ResponseEntity.status(HttpStatus.OK).body(itemCardapio.get()))
                .orElseGet(()->ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @GetMapping("/categoria/{categoriaId}/disponivel")
    public ResponseEntity<List<Cardapio>> consultarPorCategoria(@PathVariable final Integer categoriaId){
        List<Cardapio> cardapioList = this.repository.findAllByCategoria(categoriaId);
        return ResponseEntity.status(HttpStatus.OK).body(cardapioList);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Cardapio> update(@PathVariable final Integer id,
                                           @RequestBody final Cardapio cardapio) throws JsonMappingException {

        Optional<Cardapio> itemCardapioEncontrado = this.repository.findById(id);
        if(itemCardapioEncontrado.isPresent()){
            this.objectMapper.updateValue(itemCardapioEncontrado.get(), cardapio);
            Cardapio cardapioAtualizado = this.repository.save(itemCardapioEncontrado.get());
            return ResponseEntity.status(HttpStatus.OK).body(cardapioAtualizado);
        }

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PostMapping("/")
    public ResponseEntity<Cardapio> insert(@RequestBody final Cardapio cardapio){
        Cardapio value  = this.repository.save(cardapio);
        return ResponseEntity.status(HttpStatus.CREATED).body(this.repository.save(value));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable final Integer id){
        Optional<Cardapio> cardapio = this.repository.findById(id);
        if(!cardapio.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Recurso não encontrado!");
        }

        this.repository.deleteById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
