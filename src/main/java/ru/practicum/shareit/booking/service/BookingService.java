package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;

import java.util.List;

public interface BookingService {
    BookingResponseDto createBooking(BookingDto bookingDto, Long userId);

    BookingResponseDto approveBooking(Long bookingId, Boolean approved, Long userId);

    BookingResponseDto getBookingById(Long bookingId);

    List<BookingResponseDto> getOwnerBookings(Long userId);

    List<BookingResponseDto> getAllUserBookings(Long userId);
}