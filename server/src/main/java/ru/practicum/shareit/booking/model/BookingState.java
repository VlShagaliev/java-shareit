package ru.practicum.shareit.booking.model;

import java.util.Optional;

public enum BookingState {
    ALL,
    CURRENT,
    PAST,
    FUTURE,
    WAITING,
    REJECTED;

    public static Optional<BookingState> fromString(String stateParam) {
        try {
            String upperState = stateParam.trim().toUpperCase();
            return Optional.of(valueOf(upperState));
        } catch (IllegalArgumentException e) {
            return Optional.empty();
        }
    }
}
