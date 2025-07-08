package org.example.service;

import org.example.entity.Payment;
import org.example.entity.PaymentXML;
import org.example.utils.FileUtils;

import java.io.File;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Lớp xử lý ghi thông tin thanh toán vào file XML.
 */
public class PaymentService {

    /**
     * Tạo mới bản ghi thanh toán và lưu vào file XML.
     *
     * @param bookingId mã booking liên quan
     * @param userName  tên người dùng
     * @param amount    số tiền thanh toán
     * @param method    phương thức thanh toán
     */
    public static void createPayment(String bookingId, String userName, double amount, String method) {
        String path = "payment.xml";
        PaymentXML paymentXML = FileUtils.readFromFile(path, PaymentXML.class);
        if (paymentXML == null) {
            paymentXML = new PaymentXML();
        }

        List<Payment> payments = paymentXML.getPayments();

        Payment payment = new Payment();
        payment.setPaymentId("PMT" + UUID.randomUUID().toString().substring(0, 5).toUpperCase());
        payment.setBookingId(bookingId);
        payment.setUserName(userName);
        payment.setAmount(amount);
        payment.setMethod(method);
        payment.setStatus("Đã thanh toán");
        payment.setPaidAt(LocalDateTime.now());

        payments.add(payment);
        paymentXML.setPayments(payments);

        FileUtils.writeToFile(path, paymentXML);

        System.out.println("Đã ghi payment thành công. Tổng số giao dịch: " + payments.size());
        System.out.println("Đường dẫn file XML: " + new File(path).getAbsolutePath());
    }
}
