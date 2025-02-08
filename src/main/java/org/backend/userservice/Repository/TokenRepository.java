package org.backend.userservice.Repository;

import jakarta.el.ELManager;
import org.backend.userservice.Models.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {
    Token save(Token token);
    List<Token> findByUserEmail(String email);


    Optional<Token> findByValueAndDeleted(String value, boolean deleted);
    Optional<Token> findByValueAndDeletedAndExpiryAtGreaterThan(String tokenValue, boolean deleted, Date CurrentDate);

}
