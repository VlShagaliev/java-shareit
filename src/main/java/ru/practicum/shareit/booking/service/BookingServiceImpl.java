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
    public List<BookingResponseDto> getOwnerBookings(Long userId) {
        User booker = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));
        return bookingRepository.findAllByBookerId(userId).stream()
                .map(BookingMapper::toBookingResponseDto)
                .toList();
    }

    @Override
    public List<BookingResponseDto> getAllUserBookings(Long userId) {
        return bookingRepository.findAllByBookerId(userId).stream()
                .map(BookingMapper::toBookingResponseDto)
                .toList();
    }

    // Остальные методы реализуем аналогично
}
