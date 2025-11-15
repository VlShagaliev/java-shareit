import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.service.UserServiceImpl;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private User user;
    private User updatedUser;
    private UserDto createDto;
    private UserDto updateDto;

    @BeforeEach
    void setUp() {
        user = new User(1L, "Владислав Шагалиев", "vl.shagaliev@ya.ru");
        updatedUser = new User(1L, "Обновленное имя", "updated@ya.ru");
        createDto = new UserDto(null, "Владислав Шагалиев", "vl.shagaliev@ya.ru");
        updateDto = new UserDto(null, "Обновленное имя", "updated@ya.ru");
    }

    @Test
    void testCreateNewUserSuccessfully() {
        when(userRepository.save(any())).thenReturn(user);

        UserDto result = userService.createUser(createDto);
        assertEquals(user.getId(), result.getId());
        assertEquals(user.getName(), result.getName());
        assertEquals(user.getEmail(), result.getEmail());
    }

    @Test
    void testUpdateUserSuccessfully() {
        when(userRepository.save(any())).thenReturn(updatedUser);
        when(userRepository.getReferenceById(1L)).thenReturn(user);

        userService.createUser(createDto);
        UserDto updateResult = userService.updateUser(updateDto, 1L);

        assertEquals(updateResult.getId(), updatedUser.getId());
        assertEquals(updateResult.getName(), updatedUser.getName());
        assertEquals(updateResult.getEmail(), updatedUser.getEmail());
    }

    @Test
    void shouldGetUserById() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        UserDto result = userService.getUserById(1L);

        assertEquals(1L, result.getId());
        assertEquals(user.getName(), result.getName());
        assertEquals(user.getEmail(), result.getEmail());
    }
}