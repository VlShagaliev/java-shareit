import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.ShareItServer;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.service.BookingServiceImpl;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest(classes = ShareItServer.class)
@AutoConfigureMockMvc
class BookingControllerTest {

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private BookingServiceImpl bookingService;

    @Autowired
    private MockMvc mockMvc;

    private BookingDto bookingDto;
    private BookingResponseDto bookingCreateDto;
    private ItemDto itemDto;
    private UserDto userDto;

    @BeforeEach
    void setup() {
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = LocalDateTime.now().plusDays(1);

        userDto = new UserDto(1L, "Владислав Шагалиев", "vl.shagaliev@ya.ru");
        itemDto = new ItemDto(1L, "Перфоратор", "Ударный перфоратор", true, userDto.getId(),
                1L, new Booking(), new Booking(), List.of());

        bookingDto = new BookingDto(null, start, end, itemDto.getId(), userDto.getId(), Status.WAITING);
        bookingCreateDto = new BookingResponseDto(1L, start, end, ItemMapper.toItem(itemDto, UserMapper.toUser(userDto), null), UserMapper.toUser(userDto), Status.WAITING);

    }

    @Test
    void add() throws Exception {
        when(bookingService.createBooking(any(), any())).thenReturn(bookingCreateDto);

        mockMvc.perform(post("/bookings")
                        .content(mapper.writeValueAsString(bookingDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", Matchers.is(bookingCreateDto.getId()), Long.class));
    }

    @Test
    void approveBooking() throws Exception {
        when(bookingService.approveBooking(any(), any(), any())).thenReturn(bookingCreateDto);

        mockMvc.perform(patch("/bookings/{bookingId}", 1)
                        .param("approved", String.valueOf(true))
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", Matchers.is(bookingCreateDto.getId()), Long.class));
    }

    @Test
    void getBookingById() throws Exception {
        when(bookingService.getBookingById(any())).thenReturn(bookingCreateDto);

        mockMvc.perform(get("/bookings/{bookingId}", 1)
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", Matchers.is(bookingCreateDto.getId()), Long.class));
    }

    @Test
    void getBookingByUser() throws Exception {
        when(bookingService.getAllUserBookings(any(), any())).thenReturn(List.of(bookingCreateDto));

        mockMvc.perform(get("/bookings")
                        .param("state", "ALL")
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThan(0))))
                .andExpect(jsonPath("$.[0].id", Matchers.is(bookingCreateDto.getId()), Long.class));
    }

    @Test
    void getBookingItemByOtherUser() throws Exception {
        when(bookingService.getOwnerBookings(any(), any())).thenReturn(List.of(bookingCreateDto));

        mockMvc.perform(get("/bookings/owner")
                        .param("state", "ALL")
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThan(0))))
                .andExpect(jsonPath("$.[0].id", Matchers.is(bookingCreateDto.getId()), Long.class));
    }

}