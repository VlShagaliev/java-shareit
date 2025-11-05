package ru.practicum.shareit.booking.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.booking.model.Booking;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Transactional
    List<Booking> findAllByBookerId(Long bookerId, Sort newestFirst);

    List<Booking> findAllByItemId(Long itemId);
}
