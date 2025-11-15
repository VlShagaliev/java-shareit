import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class UserDtoTest {


    private Validator validator;

    @BeforeEach
    public void setup() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void testValidUser() {
        UserDto user = new UserDto(null, "Владислав Шагалиев", "vl.shagaliev@ya.ru");

        Set<ConstraintViolation<UserDto>> violations = validator.validate(user);

        assertTrue(violations.isEmpty());
    }
}