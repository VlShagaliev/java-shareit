import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import ru.practicum.shareit.user.dto.UserRequestDto;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class UserRequestDtoTest {


    private Validator validator;

    @BeforeEach
    public void setup() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void testValidUser() {
        UserRequestDto user = new UserRequestDto("Владислав Шагалиев", "vl.shagaliev@ya.ru");

        Set<ConstraintViolation<UserRequestDto>> violations = validator.validate(user);

        assertTrue(violations.isEmpty());
    }

    @Test
    public void testInvalidWithEmptyName() {

        UserRequestDto user = new UserRequestDto("", "vl.shagaliev@ya.ru");

        Set<ConstraintViolation<UserRequestDto>> violations = validator.validate(user);

        assertFalse(violations.isEmpty());
        assertEquals("Имя должно быть указано.", violations.iterator().next().getMessage());
    }

    @Test
    public void testInvalidEmailAddress() {
        UserRequestDto user = new UserRequestDto("Владислав Шагалиев", "vl.shagaliev ya.ru");

        Set<ConstraintViolation<UserRequestDto>> violations = validator.validate(user);

        assertFalse(violations.isEmpty());
        assertEquals("Электронный адрес некорректен.", violations.iterator().next().getMessage());
    }

    @Test
    public void testMultipleViolations() {
        UserRequestDto user = new UserRequestDto("", "");

        Set<ConstraintViolation<UserRequestDto>> violations = validator.validate(user);

        assertEquals(2, violations.size());
        for (ConstraintViolation<?> violation : violations) {
            if ("Имя должно быть указано.".equals(violation.getMessage())) {
                continue;
            } else if ("Электронный адрес должен быть указан.".equals(violation.getMessage())) {
                continue;
            } else
                fail("Неизвестная ошибка.");
        }
    }
}