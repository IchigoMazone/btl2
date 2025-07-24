//package org.example.service;
//
//import org.example.entity.*;
//import org.example.utils.FileUtils;
//
//import java.time.LocalDateTime;
//import java.util.ArrayList;
//import java.util.List;
//
//public class BookingService {
//
//    // Tạo booking mới
//    public static void createBooking(String filePath,
//                                     String bookingId,
//                                     String requestId,
//                                     String userName,
//                                     String fullName,
//                                     String email,
//                                     String phone,
//                                     String roomId,
//                                     LocalDateTime checkIn,
//                                     LocalDateTime checkOut,
//                                     double amount,
//                                     List<Person> persons) {
//
//        BookingXML bookingXML = FileUtils.readFromFile(filePath, BookingXML.class);
//        if (bookingXML == null) {
//            bookingXML = new BookingXML();
//        }
//
//        List<Booking> bookings = bookingXML.getBookings();
//        if (bookings == null) {
//            bookings = new ArrayList<>();
//        }
//
//        LocalDateTime now = LocalDateTime.now();
//
//        // Khởi tạo lịch sử chỉ 1 trạng thái ban đầu
//        List<HistoryEntry> historyList = new ArrayList<>();
//        historyList.add(new HistoryEntry(now, "Đã tạo booking"));
//
//        // Tạo đối tượng Booking mới với trạng thái ban đầu "Chờ duyệt" hoặc "Đã tạo booking"
//        Booking newBooking = new Booking(
//                bookingId,
//                requestId,
//                userName,
//                fullName,
//                email,
//                phone,
//                roomId,
//                checkIn,
//                checkOut,
//                amount,
//                now,
//                "Đã đặt", // trạng thái ban đầu
//                persons,
//                historyList
//        );
//
//        bookings.add(0, newBooking); // Thêm booking mới lên đầu danh sách
//        bookingXML.setBookings(bookings);
//        FileUtils.writeToFile(filePath, bookingXML);
//
//        System.out.println("Tạo booking thành công: " + bookingId);
//    }
//
//    // Cập nhật trạng thái booking theo bookingId
//    public static void updateBookingStatus(String filePath, String bookingId, String newStatus) {
//        BookingXML bookingXML = FileUtils.readFromFile(filePath, BookingXML.class);
//        if (bookingXML == null || bookingXML.getBookings() == null) {
//            System.out.println("Không có dữ liệu booking.");
//            return;
//        }
//
//        boolean updated = false;
//        for (Booking booking : bookingXML.getBookings()) {
//            if (booking.getBookingId().equals(bookingId)) {
//                booking.setStatus(newStatus);
//                booking.addBookingHistory(newStatus, LocalDateTime.now());
//                updated = true;
//                break;
//            }
//        }
//
//        if (updated) {
//            FileUtils.writeToFile(filePath, bookingXML);
//            System.out.println("Cập nhật trạng thái thành công: " + bookingId);
//        } else {
//            System.out.println("Không tìm thấy booking với ID: " + bookingId);
//        }
//    }
//
//    // Liệt kê danh sách khách đang lưu trú với số điện thoại và email đại diện
//    public static void listCurrentGuests(LocalDateTime currentTime, String filePath) {
//        BookingXML bookingXML = FileUtils.readFromFile(filePath, BookingXML.class);
//        if (bookingXML == null || bookingXML.getBookings() == null) {
//            System.out.println("Không có dữ liệu booking.");
//            return;
//        }
//
//        List<Booking> currentBookings = bookingXML.getBookings().stream()
//                .filter(b -> !b.getCheckIn().isAfter(currentTime) && !b.getCheckOut().isBefore(currentTime))
//                .toList();
//
//        if (currentBookings.isEmpty()) {
//            System.out.println("Không có khách nào đang lưu trú vào thời điểm " + currentTime);
//            return;
//        }
//
//        System.out.println("Danh sách khách đang lưu trú tại " + currentTime + ":");
//        for (Booking booking : currentBookings) {
//            String phone = booking.getPhone();
//            String email = booking.getEmail();
//
//            List<Person> persons = booking.getPersons();
//            for (Person person : persons) {
//                System.out.println("- " + person.getName()
//                        + " | SĐT đại diện: " + phone
//                        + " | Email đại diện: " + email
//                        + " | Phòng: " + booking.getRoomId()
//                        + " | Check-in: " + booking.getCheckIn()
//                        + " | Check-out: " + booking.getCheckOut());
//            }
//        }
//    }
//
//    // Kiểm tra trạng thái hoạt động của các phòng
//    public static void checkBookingStatus(String bookingFilePath, String roomFilePath) {
//        BookingXML bookingXML = FileUtils.readFromFile(bookingFilePath, BookingXML.class);
//        RoomXML roomXML = FileUtils.readFromFile(roomFilePath, RoomXML.class);
//
//        if (roomXML == null || roomXML.getRooms() == null || roomXML.getRooms().isEmpty()) {
//            System.out.println("Không có dữ liệu phòng.");
//            return;
//        }
//
//        List<Room> rooms = roomXML.getRooms();
//        List<Booking> bookings = bookingXML != null && bookingXML.getBookings() != null ? bookingXML.getBookings() : new ArrayList<>();
//        LocalDateTime now = LocalDateTime.now();
//
//        for (Room room : rooms) {
//            boolean isActive = false;
//
//            for (Booking booking : bookings) {
//                if (booking.getRoomId().equals(room.getRoomId())
//                        && "Check-in".equalsIgnoreCase(booking.getStatus())
//                        && !now.isBefore(booking.getCheckIn())
//                        && !now.isAfter(booking.getCheckOut())) {
//                    isActive = true;
//                    break;
//                }
//            }
//
//            System.out.println("Phòng " + room.getRoomId() + ": " + (isActive ? "Đang hoạt động" : "Không hoạt động"));
//        }
//
//        public static Booking getBookingById(String bookingId) {
//            BookingXML bookingXML = FileUtils.readFromFile(BOOKINGS_XML_PATH, BookingXML.class);
//            if (bookingXML == null) {
//                return null;
//            }
//            return bookingXML.getBookings().stream()
//                    .filter(b -> b.getBookingId().equals(bookingId))
//                    .findFirst()
//                    .orElse(null);
//        }
//    }
//}


package org.example.service;

import org.example.entity.*;
import org.example.utils.FileUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class BookingService {
    private static final String BOOKINGS_XML_PATH = "bookings.xml";

    private static final String FILE_PATH = "bookings.xml";

    public void addPerson(String bookingId, Person person) {
        BookingXML bookingXML = FileUtils.readFromFile(FILE_PATH, BookingXML.class);
        if (bookingXML == null || bookingXML.getBookings() == null) {
            throw new RuntimeException("Không thể đọc dữ liệu booking.");
        }

        Booking booking = bookingXML.getBookings().stream()
                .filter(b -> b.getBookingId().equals(bookingId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Không tìm thấy bookingId: " + bookingId));

        booking.getPersons().add(person);

        FileUtils.writeToFile(FILE_PATH, bookingXML); // ⬅ Ghi file sau khi thêm
    }

    public void updatePerson(String bookingId, String oldDocumentCode, String oldFullName, Person updatedPerson) {
        BookingXML bookingXML = FileUtils.readFromFile(FILE_PATH, BookingXML.class);
        if (bookingXML == null || bookingXML.getBookings() == null) {
            throw new RuntimeException("Không thể đọc dữ liệu booking.");
        }

        Booking booking = bookingXML.getBookings().stream()
                .filter(b -> b.getBookingId().equals(bookingId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Không tìm thấy bookingId: " + bookingId));

        List<Person> persons = booking.getPersons();
        for (int i = 0; i < persons.size(); i++) {
            Person p = persons.get(i);
            if (p.getDocumentCode().equals(oldDocumentCode) && p.getFullName().equals(oldFullName)) {
                persons.set(i, updatedPerson);
                FileUtils.writeToFile(FILE_PATH, bookingXML); // ⬅ Ghi file sau khi cập nhật
                return;
            }
        }

        throw new RuntimeException("Không tìm thấy người cần sửa.");
    }

    public void deletePerson(String bookingId, String documentCode, String fullName) {
        BookingXML bookingXML = FileUtils.readFromFile(FILE_PATH, BookingXML.class);
        if (bookingXML == null || bookingXML.getBookings() == null) {
            throw new RuntimeException("Không thể đọc dữ liệu booking.");
        }

        Booking booking = bookingXML.getBookings().stream()
                .filter(b -> b.getBookingId().equals(bookingId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Không tìm thấy bookingId: " + bookingId));

        boolean removed = booking.getPersons().removeIf(
                p -> p.getDocumentCode().equals(documentCode) && p.getFullName().equals(fullName)
        );

        if (!removed) {
            throw new RuntimeException("Không tìm thấy người cần xóa.");
        }

        FileUtils.writeToFile(FILE_PATH, bookingXML); // ⬅ Ghi file sau khi xóa
    }

    // Tạo booking mới
    public static void createBooking(String filePath,
                                     String bookingId,
                                     String requestId,
                                     String userName,
                                     String fullName,
                                     String email,
                                     String phone,
                                     String roomId,
                                     LocalDateTime checkIn,
                                     LocalDateTime checkOut,
                                     double amount,
                                     List<Person> persons) {

        BookingXML bookingXML = FileUtils.readFromFile(filePath, BookingXML.class);
        if (bookingXML == null) {
            bookingXML = new BookingXML();
        }

        List<Booking> bookings = bookingXML.getBookings();
        if (bookings == null) {
            bookings = new ArrayList<>();
        }

        LocalDateTime now = LocalDateTime.now();

        // Khởi tạo lịch sử chỉ 1 trạng thái ban đầu
        List<HistoryEntry> historyList = new ArrayList<>();
        historyList.add(new HistoryEntry(now, "Đã tạo booking"));

        // Tạo đối tượng Booking mới với trạng thái ban đầu "Chờ duyệt" hoặc "Đã tạo booking"
        Booking newBooking = new Booking(
                bookingId,
                requestId,
                userName,
                fullName,
                email,
                phone,
                roomId,
                checkIn,
                checkOut,
                amount,
                now,
                "Đã đặt", // trạng thái ban đầu
                persons,
                historyList
        );

        bookings.add(0, newBooking); // Thêm booking mới lên đầu danh sách
        bookingXML.setBookings(bookings);
        FileUtils.writeToFile(filePath, bookingXML);

        System.out.println("Tạo booking thành công: " + bookingId);
    }

    // Cập nhật trạng thái booking theo bookingId
    public static void updateBookingStatus(String filePath, String bookingId, String newStatus) {
        BookingXML bookingXML = FileUtils.readFromFile(filePath, BookingXML.class);
        if (bookingXML == null || bookingXML.getBookings() == null) {
            System.out.println("Không có dữ liệu booking.");
            return;
        }

        boolean updated = false;
        for (Booking booking : bookingXML.getBookings()) {
            if (booking.getBookingId().equals(bookingId)) {
                booking.setStatus(newStatus);
                booking.addBookingHistory(newStatus, LocalDateTime.now());
                updated = true;
                break;
            }
        }

        if (updated) {
            FileUtils.writeToFile(filePath, bookingXML);
            System.out.println("Cập nhật trạng thái thành công: " + bookingId);
        } else {
            System.out.println("Không tìm thấy booking với ID: " + bookingId);
        }
    }

    // Liệt kê danh sách khách đang lưu trú với số điện thoại và email đại diện
//    public static void listCurrentGuests(LocalDateTime currentTime, String filePath) {
//        BookingXML bookingXML = FileUtils.readFromFile(filePath, BookingXML.class);
//        if (bookingXML == null || bookingXML.getBookings() == null) {
//            System.out.println("Không có dữ liệu booking.");
//            return;
//        }
//
//        List<Booking> currentBookings = bookingXML.getBookings().stream()
//                .filter(b -> !b.getCheckIn().isAfter(currentTime) && !b.getCheckOut().isBefore(currentTime))
//                .toList();
//
//        if (currentBookings.isEmpty()) {
//            System.out.println("Không có khách nào đang lưu trú vào thời điểm " + currentTime);
//            return;
//        }
//
//        System.out.println("Danh sách khách đang lưu trú tại " + currentTime + ":");
//        for (Booking booking : currentBookings) {
//            String phone = booking.getPhone();
//            String email = booking.getEmail();
//
//            List<Person> persons = booking.getPersons();
//            for (Person person : persons) {
//                System.out.println("- " + person.getFullName()
//                        + " | SĐT đại diện: " + phone
//                        + " | Email đại diện: " + email
//                        + " | Phòng: " + booking.getRoomId()
//                        + " | Check-in: " + booking.getCheckIn()
//                        + " | Check-out: " + booking.getCheckOut());
//            }
//        }
//    }

    public static void listCurrentGuests(LocalDateTime currentTime, String filePath) {
        BookingXML bookingXML = FileUtils.readFromFile(filePath, BookingXML.class);
        if (bookingXML == null || bookingXML.getBookings() == null) {
            System.out.println("Không có dữ liệu booking.");
            return;
        }

        List<Booking> currentBookings = bookingXML.getBookings().stream()
                .filter(b -> !b.getCheckIn().isAfter(currentTime) && !b.getCheckOut().isBefore(currentTime))
                .toList();

        if (currentBookings.isEmpty()) {
            System.out.println("Không có khách nào đang lưu trú vào thời điểm " + currentTime);
            return;
        }

        System.out.println("Danh sách khách đang lưu trú tại " + currentTime + ":");
        for (Booking booking : currentBookings) {
            String phone = booking.getPhone();
            String email = booking.getEmail();
            String account = booking.getUserName(); // ⚠️ Cần đảm bảo Booking có field này

            List<Person> persons = booking.getPersons();
            for (Person person : persons) {
                String documentType = person.getDocumentType();
                String documentCode = person.getDocumentCode();

                System.out.println("- " + person.getFullName()
                        + " | Tài khoản: " + (account != null ? account : "Không có")
                        + " | SĐT đại diện: " + phone
                        + " | Email đại diện: " + email
                        + " | Giấy tờ: " + documentType
                        + " | Mã giấy tờ: " + (documentCode != null ? documentCode : "Không có")
                        + " | Phòng: " + booking.getRoomId()
                        + " | Check-in: " + booking.getCheckIn()
                        + " | Check-out: " + booking.getCheckOut());
            }
        }
    }

    public void updateBooking(Booking updatedBooking) {
        BookingXML bookingXML = FileUtils.readFromFile("bookings.xml", BookingXML.class);
        if (bookingXML == null || bookingXML.getBookings() == null) {
            System.out.println("Không có dữ liệu booking.");
            return;
        }

        boolean updated = false;
        for (int i = 0; i < bookingXML.getBookings().size(); i++) {
            Booking booking = bookingXML.getBookings().get(i);
            if (booking.getBookingId().equals(updatedBooking.getBookingId())) {
                bookingXML.getBookings().set(i, updatedBooking);
                updated = true;
                break;
            }
        }

        if (updated) {
            FileUtils.writeToFile("bookings.xml", bookingXML);
            System.out.println("Cập nhật booking thành công: " + updatedBooking.getBookingId());
        } else {
            System.out.println("Không tìm thấy booking với ID: " + updatedBooking.getBookingId());
        }
    }

    public List<Booking> getAllBookings() {
        BookingXML bookingXML = FileUtils.readFromFile("bookings.xml", BookingXML.class);
        if (bookingXML == null || bookingXML.getBookings() == null) {
            return new ArrayList<>();
        }
        return bookingXML.getBookings();
    }


    // Kiểm tra trạng thái hoạt động của các phòng
    public static void checkBookingStatus(String bookingFilePath, String roomFilePath) {
        BookingXML bookingXML = FileUtils.readFromFile(bookingFilePath, BookingXML.class);
        RoomXML roomXML = FileUtils.readFromFile(roomFilePath, RoomXML.class);

        if (roomXML == null || roomXML.getRooms() == null || roomXML.getRooms().isEmpty()) {
            System.out.println("Không có dữ liệu phòng.");
            return;
        }

        List<Room> rooms = roomXML.getRooms();
        List<Booking> bookings = bookingXML != null && bookingXML.getBookings() != null ? bookingXML.getBookings() : new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();

        for (Room room : rooms) {
            boolean isActive = false;

            for (Booking booking : bookings) {
                if (booking.getRoomId().equals(room.getRoomId())
                        && "Check-in".equalsIgnoreCase(booking.getStatus())
                        && !now.isBefore(booking.getCheckIn())
                        && !now.isAfter(booking.getCheckOut())) {
                    isActive = true;
                    break;
                }
            }

            System.out.println("Phòng " + room.getRoomId() + ": " + (isActive ? "Đang hoạt động" : "Không hoạt động"));
        }
    }

    // Lấy thông tin booking theo bookingId
    public static Booking findBookingById(String bookingId) {
        BookingXML bookingXML = FileUtils.readFromFile(BOOKINGS_XML_PATH, BookingXML.class);
        if (bookingXML == null || bookingXML.getBookings() == null) {
            return null;
        }
        return bookingXML.getBookings().stream()
                .filter(b -> b.getBookingId().equals(bookingId))
                .findFirst()
                .orElse(null);
    }

    public static int countActiveRooms(String bookingFilePath, String roomFilePath) {
        BookingXML bookingXML = FileUtils.readFromFile(bookingFilePath, BookingXML.class);
        RoomXML roomXML = FileUtils.readFromFile(roomFilePath, RoomXML.class);

        if (roomXML == null || roomXML.getRooms() == null || roomXML.getRooms().isEmpty()) {
            return 0;
        }

        List<Room> rooms = roomXML.getRooms();
        List<Booking> bookings = bookingXML != null && bookingXML.getBookings() != null ? bookingXML.getBookings() : new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();

        int count = 0;
        for (Room room : rooms) {
            for (Booking booking : bookings) {
                if (booking.getRoomId().equals(room.getRoomId())
                        && "Check-in".equalsIgnoreCase(booking.getStatus())
                        && !now.isBefore(booking.getCheckIn())
                        && !now.isAfter(booking.getCheckOut())) {
                    count++;
                    break;
                }
            }
        }

        return count;
    }

    public static int countEmptyRooms(String bookingFilePath, String roomFilePath) {
        BookingXML bookingXML = FileUtils.readFromFile(bookingFilePath, BookingXML.class);
        RoomXML roomXML = FileUtils.readFromFile(roomFilePath, RoomXML.class);

        if (roomXML == null || roomXML.getRooms() == null) return 0;
        List<Room> rooms = roomXML.getRooms();
        List<Booking> bookings = bookingXML != null && bookingXML.getBookings() != null
                ? bookingXML.getBookings()
                : new ArrayList<>();

        LocalDateTime now = LocalDateTime.now();
        int count = 0;

        for (Room room : rooms) {
            String roomId = room.getRoomId().trim();
            boolean isOccupied = false;

            for (Booking booking : bookings) {
                String bookedRoomId = booking.getRoomId().trim();

                if (roomId.equalsIgnoreCase(bookedRoomId)
                        && !"Check-in".equalsIgnoreCase(booking.getStatus())
                        && !now.isBefore(booking.getCheckIn())
                        && !now.isAfter(booking.getCheckOut())) {
                    isOccupied = true;
                    break;
                }
            }

            if (!isOccupied) {
                count++;
            }
        }

        return count;
    }
}