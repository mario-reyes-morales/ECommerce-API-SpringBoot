package upm.app.services;

import org.springframework.stereotype.Service;
import upm.app.data.models.User;
import upm.app.data.repositories.UserRepository;
import upm.app.services.exceptions.DuplicateException;

import java.util.stream.Stream;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User create(User user) {
        this.userRepository.findByEmail(user.getEmail()).ifPresent(existingUser -> {
            throw new DuplicateException("El correo ya existe, y debiera ser único: " + user.getEmail());
        });
        return this.userRepository.save(user);
    }

    public Stream<User> findAll() {
        return this.userRepository.findAll().stream();
    }

}