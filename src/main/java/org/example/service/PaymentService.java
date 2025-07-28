
package org.example.service;

import org.example.entity.Booking;
import org.example.entity.BookingXML;
import org.example.entity.Payment;
import org.example.entity.PaymentXML;
import org.example.utils.FileUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PaymentService {
    public static void createPayment(String bookingId, String fullName, String userName, String room,
                                     double amount, String method, String code,
                                     LocalDateTime checkIn, LocalDateTime checkOut) {

        String path = "payments.xml";
        PaymentXML paymentXML = FileUtils.readFromFile(path, PaymentXML.class);
        if (paymentXML == null) {
            paymentXML = new PaymentXML();
        }

        List<Payment> payments = paymentXML.getPayments();
        if (payments == null) {
            payments = new ArrayList<>();
        }

        String paymentId = "PMT" + UUID.randomUUID().toString().substring(0, 5).toUpperCase();
        LocalDateTime paidAt = LocalDateTime.now();

        Payment payment = new Payment(
                paymentId,
                bookingId,
                fullName,
                userName,
                room,
                amount,
                method,
                code,
                checkIn,
                checkOut,
                "Đã thanh toán",
                paidAt
        );

        payments.add(0, payment);
        paymentXML.setPayments(payments);
        FileUtils.writeToFile(path, paymentXML);
    }

    public static void updatePaymentStatus(String filePath, String paymentId, String newStatus) {
        PaymentXML paymentXML = FileUtils.readFromFile(filePath, PaymentXML.class);
        if (paymentXML == null || paymentXML.getPayments() == null) {
            System.out.println("Không có dữ liệu booking.");
            return;
        }

        boolean updated = false;
        for (Payment payment : paymentXML.getPayments()) {
            if (payment.getPaymentId().equals(paymentId)) {
                payment.setStatus(newStatus);
                updated = true;
                break;
            }
        }

        if (updated) {
            FileUtils.writeToFile(filePath, paymentXML);
            System.out.println("Cập nhật trạng thái thành công: " + paymentId);
        } else {
            System.out.println("Không tìm thấy booking với ID: " + paymentId);
        }
    }
}
