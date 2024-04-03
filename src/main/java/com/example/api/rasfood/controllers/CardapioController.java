package com.example.api.rasfood.controllers;

import com.example.api.rasfood.domain.entities.Cardapio;
import com.example.api.rasfood.domain.repositories.ICardapioRepository;
import com.example.api.rasfood.domain.repositories.projection.CardapioProjection;
import com.example.api.rasfood.dto.CardapioDto;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
    public ResponseEntity<Page<Cardapio>> findAll(@RequestParam("page") Integer page,
                                                  @RequestParam("size") Integer size){
        Pageable pageable = PageRequest.of(page,size);
        Page<Cardapio> cardapioList = this.repository.findAll(pageable);
        return ResponseEntity.status(HttpStatus.OK).body(cardapioList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cardapio> findById(@PathVariable final Integer id){
        Optional<Cardapio> itemCardapio = this.repository.findById(id);
        return  itemCardapio.map(value -> ResponseEntity.status(HttpStatus.OK).body(itemCardapio.get()))
                .orElseGet(()->ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @GetMapping("/categoria/{categoriaId}/disponivel")
    public ResponseEntity<Page<Cardapio>> consultarPorCategoria(@PathVariable final Integer categoriaId,
                                                                @RequestParam("page") Integer page,
                                                                @RequestParam("size") Integer size){
        Pageable pageable = PageRequest.of(page, size);
        Page<Cardapio> cardapioList = this.repository.findAllByCategoria(categoriaId, pageable);
        return ResponseEntity.status(HttpStatus.OK).body(cardapioList);
    }

    @GetMapping("/categoria/testeNativeQuery/cat={categoriaId}")
    public ResponseEntity<Page<CardapioProjection>> consultarPorCategoriaID(@PathVariable final Integer categoriaId,
                                                                            @RequestParam("page") Integer page,
                                                                            @RequestParam("size") Integer size){
        Pageable pageable = PageRequest.of(page, size);
        Page<CardapioProjection> cardapioList = this.repository.findAllByCategoriasId(categoriaId,pageable);
        return ResponseEntity.status(HttpStatus.OK).body(cardapioList);
    }

    @GetMapping("/nome/{nome}")
    public ResponseEntity<Page<CardapioDto>> findAllByNome(@PathVariable final String nome,
                                                           @RequestParam("page") Integer page,
                                                           @RequestParam("size") Integer size){
        Pageable pageable = PageRequest.of(page, size);
        Page<CardapioDto> cardapioDtoList = this.repository.findAllByNome(nome, pageable);
        return ResponseEntity.status(HttpStatus.OK).body(cardapioDtoList);
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
