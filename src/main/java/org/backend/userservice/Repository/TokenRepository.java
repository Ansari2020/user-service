package org.backend.userservice.Repository;

import org.backend.userservice.Models.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {
    @Override
    Token save(Token token);
    List<Token>  findByUserEmail(String email);

    Optional<Token> findByValueAndDeleted(String tokenValue, boolean deleted);

    /*

    select * from tokens where value = tokenValue and deleted = false and expiry_date > time.
     */
    Optional<Token> findByValueAndDeletedAndExpiryAtGreaterThan(String tokenValue, boolean deleted, Date currentTime);
}