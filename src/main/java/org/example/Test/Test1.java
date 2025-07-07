package org.example.Test;
import java.time.LocalDateTime;


public class Test1 {
    public static void main(String[] args) {

        SetupBooking.createBooking(
                "bookings.xml",
                "R004",
                "Nhat4",
                "R003",
                LocalDateTime.of(2025, 7, 9, 14, 0),
                LocalDateTime.of(2025, 7, 12, 12, 0),
                1000000.0
        );

        SetupBooking.updateBookingStatus("bookings.xml", "Nhat4", "CheckIn");
        SetupBooking.checkBookingStatus("bookings.xml", "rooms.xml");
    }
}
