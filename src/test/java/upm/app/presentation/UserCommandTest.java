package upm.app.presentation;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@SpringBootTest
@ActiveProfiles("test")
class UserCommandTest {
    @Autowired
    private UserCommand userCommand;

    @Test
    void testListUsersOk() {
        assertDoesNotThrow(() -> userCommand.listUsers());
    }
}
