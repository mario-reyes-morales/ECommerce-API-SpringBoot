package upm.app.data.models;

import jakarta.validation.Validation;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class UserTest {
    @Test
    void testValidUser() {
        User user = User.builder().email("test@example.com").name("Mario").build();
        Assertions.assertEquals("test@example.com", user.getEmail());
        Assertions.assertEquals("Mario", user.getName());
    }

    @Test
    void testInvalidNameShort() {
        User user = User.builder().email("test@example.com").name("Ma").build();
        Assertions.assertTrue(Validation.buildDefaultValidatorFactory().getValidator().validate(user).size() > 0);
    }

    @Test
    void testInvalidNameLong() {
        User user = User.builder().email("test@example.com").name("ThisNameIsWayTooLongForTheConstraint").build();
        Assertions.assertTrue(Validation.buildDefaultValidatorFactory().getValidator().validate(user).size() > 0);
    }

}
