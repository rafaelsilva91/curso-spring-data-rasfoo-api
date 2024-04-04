package com.example.api.rasfood.controllers;

import com.example.api.rasfood.domain.entities.Cardapio;
import com.example.api.rasfood.domain.repositories.ICardapioRepository;
import com.example.api.rasfood.domain.repositories.projection.CardapioProjection;
import com.example.api.rasfood.domain.repositories.specification.CardapioSpec;
import com.example.api.rasfood.dto.CardapioDto;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
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
    public ResponseEntity<List<Cardapio>> findAll(@RequestParam("page") Integer page,
                                                  @RequestParam("size") Integer size,
                                                  @RequestParam(value = "sort", required = false) Sort.Direction sort,
                                                  @RequestParam(value = "property", required = false) String property){

        Pageable pageable = Objects.nonNull(sort)
                            ? PageRequest.of(page,size, Sort.by(sort, property))
                            : PageRequest.of(page,size);

        List<Cardapio> cardapioList = this.repository.findAll(pageable).getContent();
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

    @GetMapping("/descricao/{descricao}/disponivel")
    public ResponseEntity<List<Cardapio>> findAllByDescricao(@PathVariable final String descricao,
                                                           @RequestParam("page") Integer page,
                                                           @RequestParam("size") Integer size){
        Pageable pageable = PageRequest.of(page, size);
        List<Cardapio> cardapioDtoList = this.repository.findAll(
                Specification.where(CardapioSpec.descricao(descricao)
                        .and(CardapioSpec.disponivel(true))),
                pageable).getContent();

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

    @PatchMapping(path = "/{id}/img", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Cardapio> salvarImg(@PathVariable("id") final Integer id,
                                              @RequestPart final MultipartFile file) throws IOException {

        Optional<Cardapio> cardapioEncontrado = this.repository.findById(id);
        if(cardapioEncontrado.isPresent()){
            Cardapio cardapio = cardapioEncontrado.get();
            cardapio.setImg(file.getBytes());

            return ResponseEntity.status(HttpStatus.OK).body(this.repository.save(cardapioEncontrado.get()));
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

}
