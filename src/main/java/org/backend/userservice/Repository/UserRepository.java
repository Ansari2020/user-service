package org.backend.userservice.Repository;

import org.backend.userservice.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository  extends JpaRepository<User, Long> {

     User save(User user); // if user present then update else create
     Optional<User> findByEmail(String email);

}
