import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.ShareItGateway;
import ru.practicum.shareit.user.UserClient;
import ru.practicum.shareit.user.dto.UserDto;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = ShareItGateway.class)
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserClient userClient;

    @Autowired
    private MockMvc mockMvc;

    UserDto userDto;

    @BeforeEach
    void setup() {
        userDto = new UserDto(1L, "Владислав Шагалиев", "vl.shagaliev@ya.ru");
    }

    @Test
    void addUser() throws Exception {
        String userJson = objectMapper.writeValueAsString(userDto);
        ResponseEntity<Object> response = new ResponseEntity<>(userJson, HttpStatus.OK);

        when(userClient.createUser(any())).thenReturn(response);

        mockMvc.perform(post("/users")
                        .content(objectMapper.writeValueAsString(userDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(userDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(userDto.getName())))
                .andExpect(jsonPath("$.email", is(userDto.getEmail())))
                .andReturn()
                .getResponse()
                .getContentAsString();
    }

    @Test
    void updateUser() throws Exception {
        String userJson = objectMapper.writeValueAsString(userDto);
        ResponseEntity<Object> response = new ResponseEntity<>(userJson, HttpStatus.OK);

        when(userClient.updateUser(any(), any())).thenReturn(response);

        mockMvc.perform(patch("/users/{id}", 1)
                        .content(objectMapper.writeValueAsString(userDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(userDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(userDto.getName())))
                .andExpect(jsonPath("$.email", is(userDto.getEmail())))
                .andReturn()
                .getResponse()
                .getContentAsString();
    }

    @Test
    void deleteUser() throws Exception {
        mockMvc.perform(delete("/users/{id}", 1))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void getUserById() throws Exception {
        String userJson = objectMapper.writeValueAsString(userDto);
        ResponseEntity<Object> response = new ResponseEntity<>(userJson, HttpStatus.OK);

        when(userClient.getUserById(1L)).thenReturn(response);

        mockMvc.perform(get("/users/{userId}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(userDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(userDto.getName())))
                .andExpect(jsonPath("$.email", is(userDto.getEmail())));
    }

}