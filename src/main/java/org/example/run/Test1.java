package org.example.run;

import org.example.service.PaymentService;

public class Test1 {
    public static void main(String[] args) {
        // Thông tin giả lập cho payment
        String bookingId = "BK20250708";
        String fullName = "Nguyen Van A";
        double amount = 2500.0;
        String method = "Credit Card";

        // Gọi hàm tạo payment
        PaymentService.createPayment(bookingId, fullName, amount, method);
    }
}
