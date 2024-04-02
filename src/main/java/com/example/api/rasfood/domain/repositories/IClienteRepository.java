package com.example.api.rasfood.domain.repositories;

import com.example.api.rasfood.domain.entities.Cliente;
import com.example.api.rasfood.domain.entities.ClienteId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IClienteRepository extends JpaRepository<Cliente, ClienteId> {

    @Query(value = "SELECT c FROM Cliente c " +
            "WHERE c.clienteId.email = :id " +
            "OR c.clienteId.cpf = :id")
    public Optional<Cliente> findByEmailOrCpf(@Param("id") final String id);
}
