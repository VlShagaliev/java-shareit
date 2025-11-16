package ru.practicum.shareit.booking.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.BookingClient;
import ru.practicum.shareit.booking.dto.BookItemRequestDto;
import ru.practicum.shareit.booking.dto.BookingState;


@Controller
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
@Validated
public class BookingController {
    private final BookingClient bookingClient;

    @PostMapping
    public ResponseEntity<Object> createBooking(@RequestBody @Valid BookItemRequestDto requestDto,
                                                @RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("Запрос на создание бронирования {}, userId={}", requestDto, userId);
        return bookingClient.bookItem(userId, requestDto);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> approveBooking(@PathVariable Long bookingId,
                                                 @RequestParam(value = "approved") Boolean approved,
                                                 @RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Запрос на обновление статуса бронирования");
        return bookingClient.approveBooking(bookingId, userId, approved);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> getBookerBookings(@RequestHeader("X-Sharer-User-Id") long userId,
                                                    @PathVariable Long bookingId) {
        log.info("Запрос на получение бронирования {}, userId={}", bookingId, userId);
        return bookingClient.getBooking(userId, bookingId);
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> getOwnerBookings(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                   @RequestParam(defaultValue = "all") String stateParam) {
        BookingState state = BookingState.from(stateParam)
                .orElseThrow(() -> new IllegalArgumentException("Неизвестное состояние: " + stateParam));
        log.info("Получение списка бронирования всех товаров пользователя id: {} и состояния: {}.", userId, state);
        return bookingClient.getBookingItemByUser(userId, state);
    }

    @GetMapping()
    public ResponseEntity<Object> getAllUserBookings(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                     @RequestParam(defaultValue = "all") String stateParam) {
        BookingState state = BookingState.from(stateParam)
                .orElseThrow(() -> new IllegalArgumentException("Неизвестное состояние: " + stateParam));
        log.info("Запрос на получение бронирования по userId: {} и состоянию: {}.", userId, state);
        return bookingClient.getBookingByUser(userId, state);
    }
}
