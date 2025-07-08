package org.example.Test;

import java.io.File;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class SetupPayment {

    public static void createPayment(String bookingId, String userName, double amount, String method) {
        String path = "payments.xml";
        PaymentXML paymentXML = XMLUtil.readFromFile(path, PaymentXML.class);
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
        paymentXML.setPayments(payments); // QUAN TRỌNG!!!

        XMLUtil.writeToFile(path, paymentXML);

        System.out.println("✅ Đã ghi payment. Tổng số: " + payments.size());
        System.out.println("🗂 File XML: " + new File(path).getAbsolutePath());
    }
}
