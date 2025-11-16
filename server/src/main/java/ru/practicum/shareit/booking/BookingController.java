package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.service.BookingService;

import java.util.List;

/**
 * TODO Sprint add-bookings.
 */
@RestController
@RequestMapping("/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    public ResponseEntity<BookingResponseDto> createBooking(@RequestBody BookingDto bookingDto, @RequestHeader("X-Sharer-User-Id") Long userId) {
        BookingResponseDto savedBooking = bookingService.createBooking(bookingDto, userId);
        return ResponseEntity.ok(savedBooking);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<BookingResponseDto> approveBooking(@PathVariable Long bookingId,
                                                             @RequestParam(value = "approved") Boolean approved,
                                                             @RequestHeader("X-Sharer-User-Id") Long userId) {
        BookingResponseDto approvedBooking = bookingService.approveBooking(bookingId, approved, userId);
        return ResponseEntity.ok(approvedBooking);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<BookingResponseDto> getBookerBookings(@PathVariable Long bookingId) {
        BookingResponseDto bookings = bookingService.getBookingById(bookingId);
        return ResponseEntity.ok(bookings);
    }

    @GetMapping("/owner")
    public ResponseEntity<List<BookingResponseDto>> getOwnerBookings(@RequestHeader("X-Sharer-User-Id") Long userId, @RequestParam(name = "state", defaultValue = "all") String stateParam) {
        BookingState state = BookingState.fromString(stateParam).orElseThrow(() -> new IllegalArgumentException("Неизвестный параметр state: " + stateParam));
        List<BookingResponseDto> bookings = bookingService.getOwnerBookings(userId, state);
        return ResponseEntity.ok(bookings);
    }

    @GetMapping
    public ResponseEntity<List<BookingResponseDto>> getAllUserBookings(@RequestHeader("X-Sharer-User-Id") Long userId, @RequestParam(name = "state", defaultValue = "all") String stateParam) {
        BookingState state = BookingState.fromString(stateParam).orElseThrow(() -> new IllegalArgumentException("Неизвестный параметр state: " + stateParam));
        List<BookingResponseDto> bookings = bookingService.getAllUserBookings(userId, state);
        return ResponseEntity.ok(bookings);
    }
}