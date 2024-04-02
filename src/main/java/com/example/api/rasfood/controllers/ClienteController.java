package com.example.api.rasfood.controllers;

import com.example.api.rasfood.domain.entities.Cliente;
import com.example.api.rasfood.domain.entities.ClienteId;
import com.example.api.rasfood.domain.repositories.IClienteRepository;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin("*")
@RestController
@RequestMapping("/clientes")
public class ClienteController {

    private IClienteRepository repository;

    private ObjectMapper objectMapper;

    public ClienteController(IClienteRepository repository, ObjectMapper objectMapper) {
        this.repository = repository;
        this.objectMapper = objectMapper;
    }

    @GetMapping
    public ResponseEntity<List<Cliente>> findAll() {
        List<Cliente> clientes = this.repository.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(clientes);
    }

    @GetMapping("/{email}/{cpf}")
    public ResponseEntity<Cliente> findByCpfEmail(@PathVariable("email") final String email,
                                                  @PathVariable("cpf") final String cpf) {

        ClienteId clienteId = new ClienteId(email, cpf);
        Optional<Cliente> cliente = this.repository.findById(clienteId);

        return cliente.map(value -> ResponseEntity.status(HttpStatus.OK).body(value))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }


    @PatchMapping("/{id}")
    public ResponseEntity<Cliente> update(@PathVariable("id") final String id,
                                             @RequestBody final Cliente cliente) throws JsonMappingException {

        Optional<Cliente> clienteEncontrado = this.repository.findByEmailOrCpf(id);
        if (clienteEncontrado.isPresent()) {
            this.objectMapper.updateValue(clienteEncontrado.get(), cliente);
            Cliente clienteAtualizado = this.repository.save(clienteEncontrado.get());
            return ResponseEntity.status(HttpStatus.OK).body(clienteAtualizado);
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }


}
