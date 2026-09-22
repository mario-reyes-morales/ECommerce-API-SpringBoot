package upm.app.services;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import upm.app.data.models.User;
import upm.app.data.repositories.UserRepository;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ActiveProfiles("test")
class UserServiceTest {
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;

    @Test
    void testCreate() {
        this.userService.create(User.builder().email("new.user@example.com").name("New User").build());
        assertTrue(userRepository.findByEmail("new.user@example.com").isPresent());
    }

    @Test
    void testCreateConflict() {
        // Mario Reyes already exists in Seeder
        User user = User.builder().email("mario.reyes@alumnos.upm.es").name("Mario Copy").build();
        assertThrows(RuntimeException.class, () -> this.userService.create(user));
    }

    @Test
    void testFindAll() {
        assertTrue(this.userService.findAll().toList().size() >= 2);
    }

}
