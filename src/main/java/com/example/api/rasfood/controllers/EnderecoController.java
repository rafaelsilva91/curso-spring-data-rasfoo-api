package com.example.api.rasfood.controllers;

import com.example.api.rasfood.entities.Endereco;
import com.example.api.rasfood.repositories.IEnderecoRepository;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin("*")
@RestController
@RequestMapping("/endereco")
public class EnderecoController {

    private IEnderecoRepository repository;

    private ObjectMapper objectMapper;

    public EnderecoController(IEnderecoRepository repository, ObjectMapper objectMapper) {
        this.repository = repository;
        this.objectMapper = objectMapper;
    }

    @GetMapping("/enderecos")
    public ResponseEntity<List<Endereco>> listar() {
        List<Endereco> enderecos = this.repository.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(enderecos);
    }

    @GetMapping("/cep/{value}")
    public ResponseEntity<List<Endereco>> consultarPorCep(@PathVariable final String value){
        List<Endereco> enderecos = this.repository.consultarPorCep(value);
        return ResponseEntity.status(HttpStatus.OK).body(enderecos);
    }

    @GetMapping("/busca/cep/{cep}") //Teste Method
    public ResponseEntity<List<Endereco>> consultarTodosPorCEP(@PathVariable final String cep){
        List<Endereco> enderecos = this.repository.findByCep(cep);
        return ResponseEntity.status(HttpStatus.OK).body(enderecos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Endereco> findById(@PathVariable final Integer id) {
        Optional<Endereco> endereco = this.repository.findById(id);
        return endereco.map(value -> ResponseEntity.status(HttpStatus.OK).body(value))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Endereco> update(@PathVariable final Integer id,
                                           @RequestBody final Endereco endereco) throws JsonMappingException {
        Optional<Endereco> enderecoEncontrado = this.repository.findById(id);
        if (enderecoEncontrado.isPresent()) {
            this.objectMapper.updateValue(enderecoEncontrado.get(), endereco);
            Endereco enderecoAtualizado = this.repository.save(enderecoEncontrado.get());
            return ResponseEntity.status(HttpStatus.OK).body(enderecoAtualizado);
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

}
