package org.example.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BookingUserFilterService {

    private final String bookingFilePath;

    public BookingUserFilterService(String bookingFilePath) {
        this.bookingFilePath = bookingFilePath;
    }

    // Trả về danh sách tên người dùng có check-in trong khoảng
    public List<String> getUsernamesByCheckInBetween(LocalDateTime from, LocalDateTime to) {
        List<Booking> bookings = getAllBookings();
        return bookings.stream()
                .filter(b -> b.getCheckIn() != null
                        && !b.getCheckIn().isBefore(from)
                        && !b.getCheckIn().isAfter(to))
                .map(Booking::getUserName)
                .distinct()
                .collect(Collectors.toList());
    }

    // Trả về toàn bộ booking từ file
    private List<Booking> getAllBookings() {
        BookingXML bookingXML = XMLUtil.readFromFile(bookingFilePath, BookingXML.class);
        return bookingXML != null ? bookingXML.getBookings() : new ArrayList<>();
    }
}
