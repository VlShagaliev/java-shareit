import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.dto.BookItemRequestDto;

import java.time.LocalDateTime;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BookingItemRequestDtoTest {

    Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    void testStartIsInPast() {
        BookItemRequestDto bookItemRequestDto = new BookItemRequestDto(null, LocalDateTime.now().minusDays(1),
                LocalDateTime.now().plusDays(5));

        Set<ConstraintViolation<BookItemRequestDto>> violations = validator.validate(bookItemRequestDto);

        assertFalse(violations.isEmpty());
    }

    @Test
    void testValidIsSuccess() {
        LocalDateTime start = LocalDateTime.now().plusHours(1);
        LocalDateTime end = LocalDateTime.now().plusHours(2);
        BookItemRequestDto bookingCreateDto = new BookItemRequestDto(null, start, end);

        Set<ConstraintViolation<BookItemRequestDto>> violations = validator.validate(bookingCreateDto);

        assertTrue(violations.isEmpty());
    }

    @Test
    void testEndThenValidationFails() {
        LocalDateTime dateTime = LocalDateTime.now();
        BookItemRequestDto bookingCreateDto = new BookItemRequestDto(null, dateTime, dateTime);

        Set<ConstraintViolation<BookItemRequestDto>> violations = validator.validate(bookingCreateDto);

        assertFalse(violations.isEmpty());
    }
}
