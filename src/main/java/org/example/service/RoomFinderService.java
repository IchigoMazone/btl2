
package org.example.service;
import org.example.entity.*;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.List;
import java.text.DecimalFormatSymbols;
import org.example.entity.BookingXML;
import org.example.utils.FileUtils;
import java.time.YearMonth;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Map;
import org.example.entity.Booking;
import org.example.entity.Room;
import java.util.HashMap;

public class RoomFinderService {

    private final String roomFilePath;
    private final String bookingFilePath;

    public RoomFinderService(String roomFilePath, String bookingFilePath) {
        this.roomFilePath = roomFilePath;
        this.bookingFilePath = bookingFilePath;
    }

    private static List<Room> layTatCaPhong() {
        RoomXML roomXML = FileUtils.readFromFile("rooms.xml", RoomXML.class);
        return (roomXML != null && roomXML.getRooms() != null) ? roomXML.getRooms() : new ArrayList<>();
    }

    private static List<Booking> layTatCaBooking() {
        BookingXML bookingXML = FileUtils.readFromFile("bookings.xml", BookingXML.class);
        return (bookingXML != null && bookingXML.getBookings() != null) ? bookingXML.getBookings() : new ArrayList<>();
    }

    private static List<Request> getAllRequest() {
        RequestXML requestXML = FileUtils.readFromFile("requests.xml", RequestXML.class);
        return (requestXML != null && requestXML.getRequests() != null) ? requestXML.getRequests() : new ArrayList<>();
    }

    public static int countAvailableRoomsAt(LocalDateTime currentTime) {
        List<Room> allRooms = layTatCaPhong();
        List<Booking> allBookings = layTatCaBooking();

        int count = 0;

        for (Room room : allRooms) {
            boolean isAvailable = true;

            for (Booking booking : allBookings) {
                if (!booking.getRoomId().equals(room.getRoomId())) continue;

                String status = booking.getStatus();
                if (status.equalsIgnoreCase("Đã đặt") ||
                        status.equalsIgnoreCase("Đã bị hủy") ||
                        status.equalsIgnoreCase("Check-out") ||
                        status.equalsIgnoreCase("Vắng mặt")  ||
                        status.equalsIgnoreCase("Chờ thanh toán")
                ) {
                    continue;
                }

                if (status.equalsIgnoreCase("Check-in")) {
                    isAvailable = false;
                    break;
                }
            }

            if (isAvailable) {
                count++;
            }
        }

        return count;
    }

    public List<Room> findAvailableRooms(LocalDateTime checkIn, LocalDateTime checkOut, String roomType) {
        List<Room> allRooms = layTatCaPhong();
        List<Booking> allBookings = layTatCaBooking();

        List<Room> availableRooms = new ArrayList<>();

        for (Room room : allRooms) {
            if (roomType != null && !roomType.equalsIgnoreCase("Tất cả")
                    && !room.getType().equalsIgnoreCase(roomType)) {
                continue;
            }

            boolean isAvailable = true;

            for (Booking booking : allBookings) {
                if (!booking.getRoomId().equals(room.getRoomId())) continue;

                String status = booking.getStatus();
                if (status.equalsIgnoreCase("Check-out") ||
                        status.equalsIgnoreCase("Đã bị hủy") ||
                        status.equalsIgnoreCase("Gửi yêu cầu") ||
                        status.equalsIgnoreCase("Vắng mặt") ||
                        status.equalsIgnoreCase("Đã đọc")) {
                    continue;
                }

                if (booking.getCheckOut().isBefore(checkIn) || booking.getCheckOut().isEqual(checkIn)) {
                    continue;
                }

                if (isOverlapping(booking.getCheckIn(), booking.getCheckOut(), checkIn, checkOut)) {
                    isAvailable = false;
                    break;
                }
            }

            if (isAvailable) {
                availableRooms.add(room);
            }
        }

        return availableRooms;
    }

    private boolean isOverlapping(LocalDateTime aStart, LocalDateTime aEnd,
                                  LocalDateTime bStart, LocalDateTime bEnd) {
        return !(aEnd.isBefore(bStart) || aStart.isAfter(bEnd));
    }

    public static int countActiveRoomsAt(LocalDateTime time) {
        List<Booking> allBookings = layTatCaBooking();

        int count = 0;
        for (Booking booking : allBookings) {
            String status = booking.getStatus();

            if ("Check-in".equalsIgnoreCase(status)) {
                count++;
            }
        }

        return count;
    }

    public static int getNumberOfActiveCustomersNow(BookingXML bookingXML) {
        LocalDateTime now = LocalDateTime.now();

        return bookingXML.getBookings().stream()
                .filter(b -> "Check-in".equalsIgnoreCase(b.getStatus()))
                .mapToInt(b -> b.getPersons() != null ? b.getPersons().size() : 0)
                .sum();
    }

    public static int countTodayCheckinRooms(BookingXML bookingXML) {
        LocalDate today = LocalDate.now();

        return (int) bookingXML.getBookings().stream()
                .filter(booking -> booking.getHistory() != null)
                .filter(booking -> booking.getHistory().stream()
                        .anyMatch(history ->
                                "Check-in".equalsIgnoreCase(history.getStatus()) &&
                                        history.getTimestamp().toLocalDate().isEqual(today)
                        )
                )
                .count();
    }

    public static int countTodayCheckoutRooms(BookingXML bookingXML) {
        LocalDate today = LocalDate.now();

        return (int) bookingXML.getBookings().stream()
                .filter(booking -> booking.getHistory() != null)
                .filter(booking -> booking.getHistory().stream()
                        .anyMatch(history ->
                                "Check-out".equalsIgnoreCase(history.getStatus()) &&
                                        history.getTimestamp().toLocalDate().isEqual(today)
                        )
                )
                .count();
    }

    public static String getFormattedTodayRevenue(BookingXML bookingXML) {
        LocalDate today = LocalDate.now();

        double total = bookingXML.getBookings().stream()
                .filter(booking -> booking.getHistory() != null)
                .filter(booking -> booking.getHistory().stream()
                        .anyMatch(history ->
                                "Check-out".equalsIgnoreCase(history.getStatus()) &&
                                        history.getTimestamp().toLocalDate().isEqual(today)
                        )
                )
                .mapToDouble(Booking::getAmount)
                .sum();

        DecimalFormat df = new DecimalFormat("#,###");
        return df.format(total);
    }

    public static int countTodayCreatedBookings(BookingXML bookingXML) {
        LocalDate today = LocalDate.now();

        return (int) bookingXML.getBookings().stream()
                .filter(booking -> booking.getHistory() != null)
                .filter(booking -> booking.getHistory().stream()
                        .anyMatch(history ->
                                "Đã tạo booking".equalsIgnoreCase(history.getStatus()) &&
                                        history.getTimestamp().toLocalDate().isEqual(today)
                        ))
                .count();
    }

    public static int countPendingRequests(RequestXML requestXML) {
        if (requestXML == null || requestXML.getRequests() == null) return 0;

        List<Request> allRequest = getAllRequest();
        int count = 0;

        for (Request r : allRequest) {
            if ("Gửi yêu cầu".equalsIgnoreCase(r.getStatus()) ||
                     "Đã đọc".equalsIgnoreCase(r.getStatus()) ||
            "Gửi yêu cầu hủy".equalsIgnoreCase(r.getStatus())) {
                count++;
            }
        }
        return count;
    }

    public static String getFormattedMonthlyRevenue(BookingXML bookingXML) {
        LocalDate today = LocalDate.now();
        LocalDate startOfMonth = today.withDayOfMonth(1);

        double total = bookingXML.getBookings().stream()
                .filter(booking -> booking.getHistory() != null)
                .filter(booking -> booking.getHistory().stream()
                        .anyMatch(history ->
                                "Check-out".equalsIgnoreCase(history.getStatus()) &&
                                        !history.getTimestamp().toLocalDate().isBefore(startOfMonth) &&
                                        !history.getTimestamp().toLocalDate().isAfter(today)
                        )
                )
                .mapToDouble(Booking::getAmount)
                .sum();

        DecimalFormat df = new DecimalFormat("#,###");
        return df.format(total);
    }

    public static int getTotalMonthlyCustomers(BookingXML bookingXML) {
        LocalDate today = LocalDate.now();
        LocalDate startOfMonth = today.withDayOfMonth(1);

        return bookingXML.getBookings().stream()
                .filter(booking -> booking.getHistory() != null)
                .filter(booking -> booking.getHistory().stream()
                        .anyMatch(history ->
                                "Check-out".equalsIgnoreCase(history.getStatus()) &&
                                        !history.getTimestamp().toLocalDate().isBefore(startOfMonth) &&
                                        !history.getTimestamp().toLocalDate().isAfter(today)
                        )
                )
                .mapToInt(booking -> booking.getPersons() != null ? booking.getPersons().size() : 0)
                .sum();
    }

    public static String getMonthlyCustomerCountFormatted(BookingXML bookingXML) {
        List<Booking> allBookings = bookingXML.getBookings();

        LocalDate today = LocalDate.now();
        int currentMonth = today.getMonthValue();
        int currentYear = today.getYear();

        int countCheckOut = 0;
        int countOthers = 0;

        for (Booking b : allBookings) {
            if (b.getCheckOut() != null) {
                LocalDate checkOutDate = b.getCheckOut().toLocalDate();
                if (checkOutDate.getMonthValue() == currentMonth && checkOutDate.getYear() == currentYear) {
                    String status = b.getStatus();
                    if ("Check-out".equalsIgnoreCase(status)) {
                        countCheckOut++;
                    }
                    if (!"Check-in".equalsIgnoreCase(status) && !"Đã đặt".equalsIgnoreCase(status)) {
                        countOthers++;
                    }
                }
            }
        }

        double rate = countOthers > 0 ? (double) countCheckOut / countOthers * 100 : 0.0;

        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setDecimalSeparator(',');

        DecimalFormat df = new DecimalFormat("#0.00", symbols);

        return df.format(rate) + "%";
    }


    public static String getMaxGuestCountInOneDay(BookingXML bookingXML) {
        List<Booking> bookings = bookingXML.getBookings();
        Map<LocalDate, Integer> guestCountPerDay = new HashMap<>();
        LocalDateTime now = LocalDateTime.now();

        YearMonth currentMonth = YearMonth.from(now);
        LocalDate startOfMonth = currentMonth.atDay(1);
        LocalDate endOfMonth = currentMonth.atEndOfMonth();

        for (Booking booking : bookings) {
            String status = booking.getStatus();

            if (status != null && !status.equalsIgnoreCase("Đã bị hủy")
                    && !status.equalsIgnoreCase("Vắng mặt")) {
                LocalDateTime checkIn = booking.getCheckIn();
                LocalDateTime checkOut = booking.getCheckOut();

                if (checkIn != null && checkOut != null) {
                    LocalDate checkInDate = checkIn.toLocalDate();
                    LocalDate checkOutDate = checkOut.toLocalDate();

                    // Giới hạn trong tháng cần xét
                    LocalDate startDate = checkInDate.isBefore(startOfMonth) ? startOfMonth : checkInDate;
                    LocalDate endDate = checkOutDate.isAfter(endOfMonth) ? endOfMonth : checkOutDate;

                    for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
                        int guests = booking.getNumberOfGuests();
                        guestCountPerDay.put(date, guestCountPerDay.getOrDefault(date, 0) + guests);
                    }
                }
            }
        }

        int max = guestCountPerDay.values().stream().max(Integer::compareTo).orElse(0);
        return String.valueOf(max);
    }

}

