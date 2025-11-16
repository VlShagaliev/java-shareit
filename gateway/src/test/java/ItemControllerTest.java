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
import ru.practicum.shareit.item.ItemClient;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = ShareItGateway.class)
@AutoConfigureMockMvc
class ItemControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ItemClient itemClient;

    @Autowired
    private MockMvc mockMvc;

    private ItemDto itemDto;
    private ItemDto itemCreateDto;
    private ItemDto itemUpdateDto;
    private UserDto userDto;
    private CommentDto commentDto;
    private CommentDto commentCreateDto;


    @BeforeEach
    void setUp() {
        userDto = new UserDto(1L, "Шагалиев Владислав", "vl.shagaliev@ya.ru");
        itemDto = new ItemDto("Перфоратор", "Ударный перфоратор", true, userDto.getId(),
                1L);
        itemCreateDto = new ItemDto("Перфоратор", "Ударный перфоратор",
                true, 1L, 1L);
        itemUpdateDto = new ItemDto("Перфоратор", "Ударный перфоратор",
                true, 1L, 1L);
        commentDto = new CommentDto(1L, 1L, "Все отлично, достойный комплект в придачу.",
                "Бузмаков Иван", LocalDateTime.now());
        commentCreateDto = new CommentDto(null, 1L, "Все отлично, достойный комплект в придачу.", userDto.getName(), LocalDateTime.now());
    }

    @Test
    void addItem() throws Exception {
        String itemJson = objectMapper.writeValueAsString(itemDto);
        ResponseEntity<Object> response = new ResponseEntity<>(itemJson, HttpStatus.OK);

        when(itemClient.addItem(any(), any())).thenReturn(response);

        mockMvc.perform(post("/items")
                        .content(objectMapper.writeValueAsString(itemCreateDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
    }

    @Test
    void updateItem() throws Exception {
        String itemJson = objectMapper.writeValueAsString(itemDto);
        ResponseEntity<Object> response = new ResponseEntity<>(itemJson, HttpStatus.OK);

        when(itemClient.updateItemById(any(), any(), any())).thenReturn(response);

        mockMvc.perform(patch("/items/{Id}", 1)
                        .content(objectMapper.writeValueAsString(itemUpdateDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
    }

    @Test
    void getItemById() throws Exception {
        mockMvc.perform(get("/items/{Id}", 1)
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk());

        verify(itemClient).getItemById(any());
    }

    @Test
    void getItems() throws Exception {
        mockMvc.perform(get("/items", 1)
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk());

        verify(itemClient).getItemsByUserId(any());

    }

    @Test
    void findItems() throws Exception {
        mockMvc.perform(get("/items/search")
                        .header("X-Sharer-User-Id", 1)
                        .param("text", "drill"))
                .andExpect(status().isOk());

        verify(itemClient).getItemsBySearchName(any());
    }

    @Test
    void addComment() throws Exception {
        String commentJson = objectMapper.writeValueAsString(commentDto);
        ResponseEntity<Object> response = new ResponseEntity<>(commentJson, HttpStatus.OK);

        when(itemClient.addComment(any(), any(), any())).thenReturn(response);

        String content = mockMvc.perform(post("/items/{id}/comment", 1)
                        .content(objectMapper.writeValueAsString(commentCreateDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
    }
}