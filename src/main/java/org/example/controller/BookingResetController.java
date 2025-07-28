package org.example.controller;

import org.example.entity.Booking;
import org.example.entity.BookingXML;
import org.example.service.BookingService;
import org.example.service.NotificationService;
import org.example.utils.FileUtils;

import java.time.LocalDateTime;
import java.util.List;

public class BookingResetController {

    public static void updateAbsentBookings() {

        BookingXML bookingXML = FileUtils.readFromFile("bookings.xml", BookingXML.class);
        List<Booking> allBookings = bookingXML.getBookings();
        LocalDateTime now = LocalDateTime.now();

        for (Booking booking : allBookings) {
            String status = booking.getStatus();
            LocalDateTime checkIn = booking.getCheckIn();
            LocalDateTime checkOut = booking.getCheckOut();


            if ("Đã đặt".equalsIgnoreCase(status) && checkIn != null) {
                if (now.isAfter(checkIn.plusHours(1))) {
                    BookingService.updateBookingStatus("bookings.xml", booking.getBookingId(), "Vắng mặt");
                    NotificationService.createNotification(
                            booking.getBookingId(),
                            booking.getRequestId(),
                            booking.getUserName(),
                            "Vắng mặt",
                            "Đã gửi"
                    );
                }
            }

            if ("Check-in".equalsIgnoreCase(status) && checkOut != null) {
                if (!now.isBefore(checkOut)) {
                    BookingService.updateBookingStatus("bookings.xml", booking.getBookingId(), "Chờ thanh toán");
                    NotificationService.createNotification(
                            booking.getBookingId(),
                            booking.getRequestId(),
                            booking.getUserName(),
                            "Check-out",
                            "Đã gửi"
                    );
                }
            }
        }
    }
}
