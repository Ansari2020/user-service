package org.backend.userservice.Configuration.Repository;

import java.util.Optional;


import org.backend.userservice.Configuration.Models.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends JpaRepository<Client, String> {
    Optional<Client> findByClientId(String clientId);
}