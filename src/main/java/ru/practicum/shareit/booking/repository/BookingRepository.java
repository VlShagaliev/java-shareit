package ru.practicum.shareit.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.model.Booking;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findAllByBookerId(Long bookerId);

    List<Booking> findAllByItemId(Long itemId);

    @Query("SELECT b FROM Booking b " +
            "WHERE b.item.id = :itemId " +
            "  AND b.end < :now " +
            "ORDER BY b.start DESC")
    List<Booking> findPastBookingsForItem(Long itemId, LocalDateTime now);


    @Query("SELECT b FROM Booking b " +
            "WHERE b.item.id = :itemId " +
            "  AND b.start > :now " +
            "ORDER BY b.start ASC")
    List<Booking> findFutureBookingsForItem(Long itemId, LocalDateTime now);
}
