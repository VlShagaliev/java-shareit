package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exceptions.NoRightsException;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.UnavailableException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Override
    public BookingResponseDto createBooking(BookingDto bookingDto, Long userId) {
        Item item = itemRepository.findById(bookingDto.getItemId())
                .orElseThrow(() -> new NotFoundException("Предмет не найден"));
        User booker = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));
        if (item.getAvailable()) {
            Booking booking = BookingMapper.toBooking(bookingDto, item, booker);
            booking.setStatus(Status.WAITING);
            Booking savedBooking = bookingRepository.save(booking);
            return BookingMapper.toBookingResponseDto(savedBooking);
        } else {
            throw new UnavailableException("Предмет недоступен");
        }
    }

    @Override
    public BookingResponseDto approveBooking(Long bookingId, Boolean approved, Long userId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Запрос на найден"));
        if (!booking.getItem().getOwner().getId().equals(userId)) {
            throw new NoRightsException("У пользователя нет прав");
        }
        if (approved) {
            booking.setStatus(Status.APPROVED);
        } else {
            booking.setStatus(Status.REJECTED);
        }
        Booking updatedBooking = bookingRepository.save(booking);
        return BookingMapper.toBookingResponseDto(updatedBooking);
    }

    @Override
    public BookingResponseDto getBookingById(Long bookingId) {
        return BookingMapper.toBookingResponseDto(bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Запрос на найден")));
    }

    @Override
    public List<BookingResponseDto> getOwnerBookings(Long userId, String stateParam) {
        User booker = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));

        List<Booking> bookings = bookingRepository.findAll().stream()
                .filter(booking -> booking.getItem().getOwner().getId().equals(userId))
                .toList();
        bookings.sort(Comparator.comparing(Booking::getStart).reversed());
        return filteredByStateParam(bookings, stateParam);
    }

    @Override
    public List<BookingResponseDto> getAllUserBookings(Long userId, String stateParam) {
        List<Booking> bookings = bookingRepository.findAllByBookerId(userId).stream()
                .toList();
        return filteredByStateParam(bookings, stateParam);
    }

    private List<BookingResponseDto> filteredByStateParam(List<Booking> bookings, String stateParam) {
        LocalDateTime now = LocalDateTime.now();
        return switch (stateParam) {
            case "CURRENT" -> bookings.stream()
                    .filter(booking -> booking.getStart().isBefore(now) && booking.getEnd().isAfter(now))
                    .map(BookingMapper::toBookingResponseDto)
                    .toList();
            case "PAST" -> bookings.stream()
                    .filter(booking -> booking.getEnd().isBefore(now))
                    .map(BookingMapper::toBookingResponseDto)
                    .toList();
            case "FUTURE" -> bookings.stream()
                    .filter(booking -> booking.getStart().isAfter(now))
                    .map(BookingMapper::toBookingResponseDto)
                    .toList();
            case "WAITING" -> bookings.stream()
                    .filter(booking -> booking.getStatus().equals(Status.WAITING) && booking.getStart().isAfter(now))
                    .map(BookingMapper::toBookingResponseDto)
                    .toList();
            case "REJECTED" -> bookings.stream()
                    .filter(booking -> booking.getStatus().equals(Status.REJECTED))
                    .map(BookingMapper::toBookingResponseDto)
                    .toList();
            default -> bookings.stream()
                    .map(BookingMapper::toBookingResponseDto)
                    .toList();
        };
    }
}
