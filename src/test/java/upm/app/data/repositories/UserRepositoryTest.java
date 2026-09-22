package upm.app.data.repositories;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import upm.app.data.models.User;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ActiveProfiles("test")
class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Test
    void testFindByEmail() {
        Optional<User> dbUser = userRepository.findByEmail("mario.reyes@alumnos.upm.es");
        assertTrue(dbUser.isPresent());
        assertEquals("Mario Reyes", dbUser.get().getName());
    }

    @Test
    void testFindByEmailNotFound() {
        assertFalse(userRepository.findByEmail("non-existent@email.com").isPresent());
    }

}
