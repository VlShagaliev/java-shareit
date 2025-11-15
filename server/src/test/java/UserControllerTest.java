import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.ShareItServer;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserServiceImpl;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = ShareItServer.class)
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private UserServiceImpl userService;

    @Autowired
    private MockMvc mockMvc;

    private UserDto userDto;
    private UserDto userUpdateDto;
    private UserDto userCreatedDto;
    private UserDto userUpdatedDto;

    @BeforeEach
    void setup() {
        userDto = new UserDto(null, "Владислав Шагалиев", "vl.shagaliev@ya.ru");
        userUpdateDto = new UserDto(null, "Обновленное имя", "updated@ya.ru");
        userCreatedDto = new UserDto(1L, "Владислав Шагалиев", "vl.shagaliev@ya.ru");
        userUpdatedDto = new UserDto(1L, "Обновленное имя", "updated@ya.ru");
    }

    @Test
    void addUser() throws Exception {
        when(userService.createUser(any(UserDto.class))).thenReturn(userCreatedDto);

        mockMvc.perform(post("/users")
                        .content(mapper.writeValueAsString(userDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(userCreatedDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(userCreatedDto.getName())))
                .andExpect(jsonPath("$.email", is(userCreatedDto.getEmail())));
    }

    @Test
    void updateUser() throws Exception {
        when(userService.updateUser(userUpdateDto, 1L)).thenReturn(userUpdatedDto);

        mockMvc.perform(patch("/users/{id}", 1L)
                        .content(mapper.writeValueAsString(userUpdateDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void deleteUser() throws Exception {
        mockMvc.perform(delete("/users/{userId}", 1L))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void getUserById() throws Exception {
        when(userService.getUserById(1L)).thenReturn(userDto);

        mockMvc.perform(get("/users/{userId}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(userDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(userDto.getName())))
                .andExpect(jsonPath("$.email", is(userDto.getEmail())));
    }
}