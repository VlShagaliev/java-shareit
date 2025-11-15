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
import ru.practicum.shareit.booking.BookingClient;
import ru.practicum.shareit.booking.dto.BookItemRequestDto;
import ru.practicum.shareit.booking.dto.BookingState;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = ShareItGateway.class)
@AutoConfigureMockMvc
class BookingControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BookingClient bookingClient;

    @Autowired
    private MockMvc mockMvc;

    private BookItemRequestDto bookingDto;
    private UserDto userDto;
    private ItemDto itemDto;
    private BookItemRequestDto bookingCreateDto;

    @BeforeEach
    void setUp() {
        LocalDateTime start = LocalDateTime.now().plusDays(1);
        LocalDateTime end = LocalDateTime.now().plusDays(2);

        userDto = new UserDto(1L, "Владислав Шагалиев", "vl.shagaliev@ya.ru");
        itemDto = new ItemDto("Перфоратор", "Ударный перфоратор", true, userDto.getId(),
                1L);

        bookingDto = new BookItemRequestDto(1L, start, end);
        bookingCreateDto = new BookItemRequestDto(1L, start, end);
    }

    @Test
    void addBooking() throws Exception {
        String bookingJson = objectMapper.writeValueAsString(bookingCreateDto);
        ResponseEntity<Object> response = new ResponseEntity<>("", HttpStatus.OK);

        when(bookingClient.bookItem(1L, bookingCreateDto)).thenReturn(response);

        mockMvc.perform(post("/bookings")
                        .content(bookingJson)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk());
    }

    @Test
    void approveBooking() throws Exception {

        mockMvc.perform(patch("/bookings/{bookingId}?approved={approved}", 1, true)
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk());

        verify(bookingClient).approveBooking(1L, 1L, true);
    }

    @Test
    void getBookingById() throws Exception {
        mockMvc.perform(get("/bookings/{bookingId}", 1)
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk());

        verify(bookingClient).getBooking(1L, 1L);
    }

    @Test
    void getBookingByUser() throws Exception {
        mockMvc.perform(get("/bookings?state={state}", BookingState.ALL)
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk());

        verify(bookingClient).getBookingByUser(any(), any());
    }

    @Test
    void getBookingItemByUser() throws Exception {
        mockMvc.perform(get("/bookings/owner?state={state}", BookingState.ALL)
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk());

        verify(bookingClient).getBookingItemByUser(any(), any());
    }
}