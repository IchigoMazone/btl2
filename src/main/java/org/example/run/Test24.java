package org.example.run;

import org.example.service.PaymentService;

import java.time.LocalDateTime;

public class Test24 {
    public static void main(String[] args) {
        // Tạo payment thử nghiệm
        PaymentService.createPayment(
                "BK00001",               // bookingId
                "Nguyễn Văn A",          // fullName
                "nguyenvana",            // userName
                "Phòng 101",             // room
                1500000.0,               // amount
                "Chuyển khoản",          // method
                "MB12345678",            // code chuyển khoản
                LocalDateTime.of(2025, 7, 25, 14, 0),  // checkIn
                LocalDateTime.of(2025, 7, 28, 12, 0)   // checkOut
        );

        System.out.println("Đã tạo payment mẫu và ghi vào payments.xml");
    }
}
