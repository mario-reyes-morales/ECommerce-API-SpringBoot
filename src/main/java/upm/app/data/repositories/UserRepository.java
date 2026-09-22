package upm.app.data.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import upm.app.data.models.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);
}