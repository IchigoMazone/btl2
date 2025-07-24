//////package org.example.service;
//////
//////import org.example.entity.Room;
//////import org.example.entity.RoomXML;
//////import org.example.entity.Booking;
//////import org.example.entity.BookingXML;
//////import org.example.utils.FileUtils;
//////
//////import java.time.LocalDateTime;
//////import java.util.ArrayList;
//////import java.util.List;
//////
///////**
////// * Service xử lý nghiệp vụ tìm phòng trống theo yêu cầu.
////// */
//////public class RoomFinderService {
//////
//////    private final String roomFilePath;
//////    private final String bookingFilePath;
//////
//////    public RoomFinderService(String roomFilePath, String bookingFilePath) {
//////        this.roomFilePath = roomFilePath;
//////        this.bookingFilePath = bookingFilePath;
//////    }
//////
//////    /**
//////     * Tìm danh sách phòng trống theo khoảng thời gian và loại phòng.
//////     * @param checkIn  thời gian nhận phòng
//////     * @param checkOut thời gian trả phòng
//////     * @param roomType loại phòng cần tìm
//////     * @return danh sách phòng trống phù hợp
//////     */
//////    public List<Room> findAvailableRooms(LocalDateTime checkIn, LocalDateTime checkOut, String roomType) {
//////        List<Room> allRooms = getAllRooms();
//////        List<Booking> allBookings = getAllBookings();
//////
//////        List<Room> availableRooms = new ArrayList<>();
//////
//////        for (Room room : allRooms) {
//////            // Bỏ qua phòng không đúng loại
//////            if (!room.getType().equalsIgnoreCase(roomType)) {
//////                continue;
//////            }
//////
//////            boolean isAvailable = true;
//////            for (Booking booking : allBookings) {
//////                if (!booking.getRoomId().equals(room.getRoomId())) {
//////                    continue;
//////                }
//////
//////                // Nếu thời gian booking trùng hoặc chồng lên khoảng thời gian cần tìm
//////                if (isOverlapping(booking.getCheckIn(), booking.getCheckOut(), checkIn, checkOut)) {
//////                    isAvailable = false;
//////                    break;
//////                }
//////            }
//////
//////            if (isAvailable) {
//////                availableRooms.add(room);
//////            }
//////        }
//////
//////        return availableRooms;
//////    }
//////
//////    /**
//////     * Kiểm tra hai khoảng thời gian có chồng lấp nhau hay không.
//////     * @param aStart thời gian bắt đầu khoảng A
//////     * @param aEnd   thời gian kết thúc khoảng A
//////     * @param bStart thời gian bắt đầu khoảng B
//////     * @param bEnd   thời gian kết thúc khoảng B
//////     * @return true nếu chồng lấp, false nếu không
//////     */
//////    private boolean isOverlapping(LocalDateTime aStart, LocalDateTime aEnd,
//////                                  LocalDateTime bStart, LocalDateTime bEnd) {
//////        // aEnd trước bStart hoặc aStart sau bEnd => không chồng lấp
//////        return !(aEnd.isBefore(bStart) || aStart.isAfter(bEnd));
//////    }
//////
//////    /**
//////     * Đọc tất cả phòng từ file XML.
//////     * @return danh sách phòng
//////     */
//////    private List<Room> getAllRooms() {
//////        RoomXML roomXML = FileUtils.readFromFile(roomFilePath, RoomXML.class);
//////        if (roomXML == null) {
//////            return new ArrayList<>();
//////        }
//////        return roomXML.getRooms();
//////    }
//////
//////    /**
//////     * Đọc tất cả booking từ file XML.
//////     * @return danh sách booking
//////     */
//////    private List<Booking> getAllBookings() {
//////        BookingXML bookingXML = FileUtils.readFromFile(bookingFilePath, BookingXML.class);
//////        if (bookingXML == null) {
//////            return new ArrayList<>();
//////        }
//////        return bookingXML.getBookings();
//////    }
//////}
////
////package org.example.service;
////
////import org.example.entity.Room;
////import org.example.entity.RoomXML;
////import org.example.entity.Booking;
////import org.example.entity.BookingXML;
////import org.example.utils.FileUtils;
////
////import java.time.LocalDateTime;
////import java.util.ArrayList;
////import java.util.List;
////
/////**
//// * Service xử lý nghiệp vụ tìm phòng trống theo yêu cầu.
//// */
////public class RoomFinderService {
////
////    private final String roomFilePath;
////    private final String bookingFilePath;
////
////    public RoomFinderService(String roomFilePath, String bookingFilePath) {
////        this.roomFilePath = roomFilePath;
////        this.bookingFilePath = bookingFilePath;
////    }
////
////    /**
////     * Tìm danh sách phòng trống theo khoảng thời gian và loại phòng.
////     * Nếu roomType là null hoặc \"Tất cả\" thì không lọc loại phòng.
////     *
////     * @param checkIn  thời gian nhận phòng
////     * @param checkOut thời gian trả phòng
////     * @param roomType loại phòng cần tìm
////     * @return danh sách phòng trống phù hợp
////     */
////    public List<Room> findAvailableRooms(LocalDateTime checkIn, LocalDateTime checkOut, String roomType) {
////        List<Room> allRooms = getAllRooms();
////        List<Booking> allBookings = getAllBookings();
////
////        List<Room> availableRooms = new ArrayList<>();
////
////        for (Room room : allRooms) {
////            // Bỏ qua nếu loại phòng không phù hợp (nếu có yêu cầu loại phòng cụ thể)
////            if (roomType != null && !roomType.equalsIgnoreCase("Tất cả")
////                    && !room.getType().equalsIgnoreCase(roomType)) {
////                continue;
////            }
////
////            boolean isAvailable = true;
////            for (Booking booking : allBookings) {
////                if (!booking.getRoomId().equals(room.getRoomId())) {
////                    continue;
////                }
////
////                // Nếu thời gian booking trùng hoặc chồng lên khoảng thời gian cần tìm
////                if (isOverlapping(booking.getCheckIn(), booking.getCheckOut(), checkIn, checkOut)) {
////                    isAvailable = false;
////                    break;
////                }
////            }
////
////            if (isAvailable) {
////                availableRooms.add(room);
////            }
////        }
////
////        return availableRooms;
////    }
////
////    /**
////     * Kiểm tra hai khoảng thời gian có chồng lấp nhau hay không.
////     */
////    private boolean isOverlapping(LocalDateTime aStart, LocalDateTime aEnd,
////                                  LocalDateTime bStart, LocalDateTime bEnd) {
////        return !(aEnd.isBefore(bStart) || aStart.isAfter(bEnd));
////    }
////
////    /**
////     * Đọc tất cả phòng từ file XML.
////     */
////    private List<Room> getAllRooms() {
////        RoomXML roomXML = FileUtils.readFromFile(roomFilePath, RoomXML.class);
////        return (roomXML != null && roomXML.getRooms() != null) ? roomXML.getRooms() : new ArrayList<>();
////    }
////
////    /**
////     * Đọc tất cả booking từ file XML.
////     */
////    private List<Booking> getAllBookings() {
////        BookingXML bookingXML = FileUtils.readFromFile(bookingFilePath, BookingXML.class);
////        return (bookingXML != null && bookingXML.getBookings() != null) ? bookingXML.getBookings() : new ArrayList<>();
////    }
////}
//
//
//package org.example.service;
//
//import org.example.entity.Room;
//import org.example.entity.RoomXML;
//import org.example.entity.Booking;
//import org.example.entity.BookingXML;
//import org.example.utils.FileUtils;
//
//import java.time.LocalDateTime;
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * Service xử lý nghiệp vụ tìm phòng trống theo yêu cầu.
// */
//public class RoomFinderService {
//
//    private static final String roomFilePath;
//    private static final String bookingFilePath;
//
//    public RoomFinderService(String roomFilePath, String bookingFilePath) {
//        this.roomFilePath = roomFilePath;
//        this.bookingFilePath = bookingFilePath;
//    }
//
//
//    // ✅ Đọc danh sách phòng từ file XML
//    private List<Room> layTatCaPhong() {
//        RoomXML roomXML = FileUtils.readFromFile(roomFilePath, RoomXML.class);
//        return (roomXML != null && roomXML.getRooms() != null) ? roomXML.getRooms() : new ArrayList<>();
//    }
//
//    // ✅ Đọc danh sách booking từ file XML
//    private List<Booking> layTatCaBooking() {
//        BookingXML bookingXML = FileUtils.readFromFile(bookingFilePath, BookingXML.class);
//        return (bookingXML != null && bookingXML.getBookings() != null) ? bookingXML.getBookings() : new ArrayList<>();
//    }
//
//    // ✅ Đếm số phòng trống tại một thời điểm
//    public int countAvailableRoomsAt(LocalDateTime currentTime) {
//        List<Room> allRooms = getAllRooms();
//        List<Booking> allBookings = getAllBookings();
//
//        int count = 0;
//
//        for (Room room : allRooms) {
//            boolean isAvailable = true;
//
//            for (Booking booking : allBookings) {
//                // Nếu không phải booking của phòng này, bỏ qua
//                if (!booking.getRoomId().equals(room.getRoomId())) {
//                    continue;
//                }
//
//                // Bỏ qua các booking không ảnh hưởng đến tình trạng phòng
//                String status = booking.getStatus();
//                if (status.equalsIgnoreCase("Gửi yêu cầu") ||
//                        status.equalsIgnoreCase("Đã đọc") ||
//                        status.equalsIgnoreCase("Đã bị hủy") ||
//                        status.equalsIgnoreCase("Check-out")) {
//                    continue;
//                }
//
//                // Kiểm tra xem thời điểm hiện tại có nằm trong khoảng đặt phòng hay không
//                LocalDateTime checkIn = booking.getCheckIn();
//                LocalDateTime checkOut = booking.getCheckOut();
//
//                if ((currentTime.isEqual(checkIn) || currentTime.isAfter(checkIn)) &&
//                        currentTime.isBefore(checkOut)) {
//                    isAvailable = false;
//                    break;
//                }
//            }
//
//            if (isAvailable) {
//                count++;
//            }
//        }
//
//        return count;
//    }
//
//
//    /**
//     * Tìm danh sách phòng trống theo khoảng thời gian và loại phòng.
//     * Nếu roomType là null hoặc "Tất cả" thì không lọc loại phòng.
//     * Các phòng có booking với trạng thái "Gửi yêu cầu", "Đã đọc" hoặc "Đã bị hủy" vẫn được coi là trống.
//     *
//     * @param checkIn  thời gian nhận phòng
//     * @param checkOut thời gian trả phòng
//     * @param roomType loại phòng cần tìm
//     * @return danh sách phòng trống phù hợp
//     */
////    public List<Room> findAvailableRooms(LocalDateTime checkIn, LocalDateTime checkOut, String roomType) {
////        List<Room> allRooms = getAllRooms();
////        List<Booking> allBookings = getAllBookings();
////
////        List<Room> availableRooms = new ArrayList<>();
////
////        for (Room room : allRooms) {
////            // Bỏ qua nếu loại phòng không phù hợp (nếu có yêu cầu loại phòng cụ thể)
////            if (roomType != null && !roomType.equalsIgnoreCase("Tất cả")
////                    && !room.getType().equalsIgnoreCase(roomType)) {
////                continue;
////            }
////
////            boolean isAvailable = true;
////            for (Booking booking : allBookings) {
////                if (!booking.getRoomId().equals(room.getRoomId())) {
////                    continue;
////                }
////
////                // Chỉ kiểm tra các booking có trạng thái không phải "Gửi yêu cầu", "Đã đọc" hoặc "Đã bị hủy"
////                if (!booking.getStatus().equalsIgnoreCase("Gửi yêu cầu") &&
////                        !booking.getStatus().equalsIgnoreCase("Đã đọc") &&
////                        !booking.getStatus().equalsIgnoreCase("Đã bị hủy")) {
////                    // Nếu thời gian booking trùng hoặc chồng lên khoảng thời gian cần tìm
////                    if (isOverlapping(booking.getCheckIn(), booking.getCheckOut(), checkIn, checkOut)) {
////                        isAvailable = false;
////                        break;
////                    }
////                }
////            }
////
////            if (isAvailable) {
////                availableRooms.add(room);
////            }
////        }
////
////        return availableRooms;
////    }
//
//    public List<Room> findAvailableRooms(LocalDateTime checkIn, LocalDateTime checkOut, String roomType) {
//        List<Room> allRooms = getAllRooms();
//        List<Booking> allBookings = getAllBookings();
//
//        List<Room> availableRooms = new ArrayList<>();
//
//        for (Room room : allRooms) {
//            if (roomType != null && !roomType.equalsIgnoreCase("Tất cả")
//                    && !room.getType().equalsIgnoreCase(roomType)) {
//                continue;
//            }
//
//            boolean isAvailable = true;
//            for (Booking booking : allBookings) {
//                if (!booking.getRoomId().equals(room.getRoomId())) {
//                    continue;
//                }
//
//                // BỎ QUA các trạng thái không ảnh hưởng
//                if (booking.getStatus().equalsIgnoreCase("Check-out") ||
//                        booking.getStatus().equalsIgnoreCase("Đã bị hủy") ||
//                        booking.getStatus().equalsIgnoreCase("Gửi yêu cầu") ||
//                        booking.getStatus().equalsIgnoreCase("Đã đọc")) {
//                    continue;
//                }
//
//                // BỎ QUA nếu booking kết thúc trước hoặc ngay thời điểm cần check-in
//                if (booking.getCheckOut().isBefore(checkIn) || booking.getCheckOut().isEqual(checkIn)) {
//                    continue;
//                }
//
//                if (isOverlapping(booking.getCheckIn(), booking.getCheckOut(), checkIn, checkOut)) {
//                    isAvailable = false;
//                    break;
//                }
//            }
//
//            if (isAvailable) {
//                availableRooms.add(room);
//            }
//        }
//
//        return availableRooms;
//    }
//
//    /**
//     * Kiểm tra hai khoảng thời gian có chồng lấp nhau hay không.
//     */
//    private boolean isOverlapping(LocalDateTime aStart, LocalDateTime aEnd,
//                                  LocalDateTime bStart, LocalDateTime bEnd) {
//        return !(aEnd.isBefore(bStart) || aStart.isAfter(bEnd));
//    }
//
//    /**
//     * Đọc tất cả phòng từ file XML.
//     */
//    private List<Room> getAllRooms() {
//        RoomXML roomXML = FileUtils.readFromFile(roomFilePath, RoomXML.class);
//        return (roomXML != null && roomXML.getRooms() != null) ? roomXML.getRooms() : new ArrayList<>();
//    }
//
//    /**
//     * Đọc tất cả booking từ file XML.
//     */
//    private List<Booking> getAllBookings() {
//        BookingXML bookingXML = FileUtils.readFromFile(bookingFilePath, BookingXML.class);
//        return (bookingXML != null && bookingXML.getBookings() != null) ? bookingXML.getBookings() : new ArrayList<>();
//    }
//}
//
//


package org.example.service;

import org.example.entity.BookingXML;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.List;
import org.example.entity.Room;
import org.example.entity.RoomXML;
import org.example.entity.Booking;
import org.example.entity.BookingXML;
import org.example.utils.FileUtils;
import java.text.NumberFormat;         // Để định dạng số có dấu phẩy
import java.time.LocalDate;            // Để lấy ngày hôm nay và so sánh
import java.util.Locale;
import org.example.entity.RequestXML;
import org.example.entity.HistoryEntry;
import java.time.LocalDateTime;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.time.LocalDate;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.Comparator;
import java.util.Map.Entry;

import javax.swing.JOptionPane;

import org.example.entity.Booking;
import org.example.entity.Room;
import org.example.entity.BookingXML;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



/**
 * Service xử lý nghiệp vụ tìm phòng trống theo yêu cầu.
 */
public class RoomFinderService {

    private final String roomFilePath;
    private final String bookingFilePath;

    public RoomFinderService(String roomFilePath, String bookingFilePath) {
        this.roomFilePath = roomFilePath;
        this.bookingFilePath = bookingFilePath;
    }

    // ✅ Đọc danh sách phòng từ file XML
    private static List<Room> layTatCaPhong() {
        RoomXML roomXML = FileUtils.readFromFile("rooms.xml", RoomXML.class);
        return (roomXML != null && roomXML.getRooms() != null) ? roomXML.getRooms() : new ArrayList<>();
    }

    // ✅ Đọc danh sách booking từ file XML
    private static List<Booking> layTatCaBooking() {
        BookingXML bookingXML = FileUtils.readFromFile("bookings.xml", BookingXML.class);
        return (bookingXML != null && bookingXML.getBookings() != null) ? bookingXML.getBookings() : new ArrayList<>();
    }

    // ✅ Đếm số phòng trống tại một thời điểm
    public static int countAvailableRoomsAt(LocalDateTime currentTime) {
        List<Room> allRooms = layTatCaPhong();
        List<Booking> allBookings = layTatCaBooking();

        int count = 0;

        for (Room room : allRooms) {
            boolean isAvailable = true;

            for (Booking booking : allBookings) {
                if (!booking.getRoomId().equals(room.getRoomId())) continue;

                String status = booking.getStatus();
                if (status.equalsIgnoreCase("Gửi yêu cầu") ||
                        status.equalsIgnoreCase("Đã đọc") ||
                        status.equalsIgnoreCase("Đã bị hủy") ||
                        status.equalsIgnoreCase("Check-out")) {
                    continue;
                }

                LocalDateTime checkIn = booking.getCheckIn();
                LocalDateTime checkOut = booking.getCheckOut();

                if ((currentTime.isEqual(checkIn) || currentTime.isAfter(checkIn)) &&
                        currentTime.isBefore(checkOut)) {
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

    /**
     * Tìm danh sách phòng trống theo khoảng thời gian và loại phòng.
     * Nếu roomType là null hoặc "Tất cả" thì không lọc loại phòng.
     * Các phòng có booking với trạng thái "Gửi yêu cầu", "Đã đọc" hoặc "Đã bị hủy" vẫn được coi là trống.
     */
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

    /**
     * Kiểm tra hai khoảng thời gian có chồng lấp nhau hay không.
     */
    private boolean isOverlapping(LocalDateTime aStart, LocalDateTime aEnd,
                                  LocalDateTime bStart, LocalDateTime bEnd) {
        return !(aEnd.isBefore(bStart) || aStart.isAfter(bEnd));
    }

    public static int countActiveRoomsAt(LocalDateTime time) {
        List<Booking> allBookings = layTatCaBooking();  // Lấy tất cả các đơn đặt phòng

        int count = 0;
        for (Booking booking : allBookings) {
            String status = booking.getStatus();
            LocalDateTime checkIn = booking.getCheckIn();
            LocalDateTime checkOut = booking.getCheckOut();

            if ("check-in".equalsIgnoreCase(status)
                    && (time.isEqual(checkIn) || time.isAfter(checkIn))
                    && (time.isBefore(checkOut) || time.isEqual(checkOut))) {
                count++;
            }
        }

        return count;
    }

    public static int getNumberOfActiveCustomersNow(BookingXML bookingXML) {
        LocalDateTime now = LocalDateTime.now();

        return bookingXML.getBookings().stream()
                .filter(b -> "Check-in".equalsIgnoreCase(b.getStatus()))
                .filter(b -> !b.getCheckIn().isAfter(now) && b.getCheckOut().isAfter(now))
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

        DecimalFormat df = new DecimalFormat("#,###"); // phân cách hàng nghìn bằng dấu ,
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

//    public static int countPendingRequests(RequestXML requestXML) {
//        if (requestXML == null || requestXML.getRequests() == null) return 0;
//
//        LocalDateTime now = LocalDateTime.now();
//
//        return (int) requestXML.getRequests().stream()
//                .filter(request -> {
//                    List<HistoryEntry> histories = request.getHistory();
//                    if (histories == null || histories.isEmpty()) return false;
//
//                    // Nếu có bất kỳ status nào không phải "Gửi yêu cầu" hoặc "Đã đọc" thì loại bỏ
//                    for (HistoryEntry history : histories) {
//                        String status = history.getStatus().trim().toLowerCase();
//                        if (!status.equals("Gửi yêu cầu") && !status.equals("Đã đọc") && !status.equals("Gửi yêu cầu hủy")) {
//                            return false;
//                        }
//                    }
//
//                    // Kiểm tra còn ít nhất 4 tiếng đến check-in
////                    LocalDateTime checkInTime = request.getCheckIn();
////                    if (checkInTime == null) return false;
////
////                    return now.isBefore(checkInTime.minusHours(4));
//                })
//                .count();
//    }

public static int countPendingRequests(RequestXML requestXML) {
    if (requestXML == null || requestXML.getRequests() == null) return 0;

    return (int) requestXML.getRequests().stream()
            .filter(request -> {
                List<HistoryEntry> histories = request.getHistory();
                if (histories == null || histories.isEmpty()) return false;

                // Nếu có bất kỳ status nào không phải "Gửi yêu cầu", "Đã đọc" hoặc "Gửi yêu cầu hủy" thì loại bỏ
                for (HistoryEntry history : histories) {
                    String status = history.getStatus().trim().toLowerCase();
                    if (!status.equals("gửi yêu cầu") &&
                            !status.equals("đã đọc") &&
                            !status.equals("gửi yêu cầu hủy")) {
                        return false;
                    }
                }

                return true; // giữ lại nếu qua được hết các điều kiện trên
            })
            .count();
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
        LocalDate today = LocalDate.now();
        int currentMonth = today.getMonthValue();
        int currentYear = today.getYear();

        List<Booking> allBookings = bookingXML.getBookings();
        long totalBookings = allBookings.size();

        long checkedInBookingsThisMonth = allBookings.stream()
                .filter(b -> b.getCheckIn() != null &&
                        b.getCheckIn().getMonthValue() == currentMonth &&
                        b.getCheckIn().getYear() == currentYear)
                .count();

        double rate = totalBookings > 0 ? (double) checkedInBookingsThisMonth / totalBookings * 100 : 0.0;

        DecimalFormat df = new DecimalFormat("#0.0");
        return df.format(rate) + "%";
    }


    public static String getMaxGuestCountInOneDay(BookingXML bookingXML) {
        List<Booking> bookings = bookingXML.getBookings();
        Map<LocalDate, Integer> guestCountPerDay = new HashMap<>();

        for (Booking booking : bookings) {
            if (booking.getCheckIn() != null) {
                LocalDate date = booking.getCheckIn().toLocalDate();
                int guests = booking.getNumberOfGuests(); // lấy số khách từ thuộc tính
                guestCountPerDay.put(date, guestCountPerDay.getOrDefault(date, 0) + guests);
            }
        }

        int max = 0;
        for (int count : guestCountPerDay.values()) {
            if (count > max) max = count;
        }

        return String.valueOf(max); // ép kiểu thành String
    }

}

