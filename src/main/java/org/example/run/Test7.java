//package org.example.run;
//
//import org.example.entity.Person;
//import org.example.service.RequestService;
//
//import java.time.LocalDateTime;
//import java.util.ArrayList;
//import java.util.List;
//
//public class Test7 {
//    public static void main(String[] args) {
//        // Thông tin người dùng
//        String userName = "testuser";
//        String fullName = "Nguyễn Văn A";
//        String email = "nguyenvana@example.com";
//        String phone = "0912345678";
//        String roomId = "R101";
//
//        // Thời gian nhận/trả phòng
//        LocalDateTime checkIn = LocalDateTime.of(2025, 7, 20, 14, 0);
//        LocalDateTime checkOut = LocalDateTime.of(2025, 7, 22, 12, 0);
//
//        // Danh sách người đi cùng
//        List<Person> persons = new ArrayList<>();
//        persons.add(new Person("Nguyễn Văn A", "CCCD", "0123456789"));
//        persons.add(new Person("Trần Thị B", "Hộ chiếu", "B12345678"));
//        persons.add(new Person("Lê Văn C", "Không có", "Chưa xác định"));
//
//
//        // Tổng tiền
//        double amount = 2500000;
//
//        // Gọi phương thức tạo yêu cầu
//        RequestService.createRequest(userName, fullName, email, phone, roomId, checkIn, checkOut, amount, persons);
//    }
//}
