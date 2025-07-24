//package org.example.service;
//
//import org.example.entity.Payment;
//import org.example.entity.PaymentXML;
//import org.example.utils.FileUtils;
//
//import java.io.File;
//import java.time.LocalDateTime;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.UUID;
//
///**
// * Lớp xử lý ghi thông tin thanh toán vào file XML.
// */
//public class PaymentService {
//
//    /**
//     * Tạo mới bản ghi thanh toán và lưu vào file XML.
//     *
//     * @param bookingId mã booking liên quan
//     * @param fullName  tên đầy đủ người đại diện
//     * @param amount    số tiền thanh toán
//     * @param method    phương thức thanh toán
//     */
//    public static void createPayment(String bookingId, String fullName, double amount, String method) {
//        String path = "payments.xml";
//        PaymentXML paymentXML = FileUtils.readFromFile(path, PaymentXML.class);
//        if (paymentXML == null) {
//            paymentXML = new PaymentXML();
//        }
//
//        List<Payment> payments = paymentXML.getPayments();
//        if (payments == null) {
//            payments = new ArrayList<>();
//        }
//
//        String paymentId = "PMT" + UUID.randomUUID().toString().substring(0, 5).toUpperCase();
//        LocalDateTime paidAt = LocalDateTime.now();
//
//        Payment payment = new Payment(
//                paymentId,
//                bookingId,
//                fullName,
//                amount,
//                method,
//                "Đã thanh toán",
//                paidAt
//        );
//
//        // ✅ Thêm bản ghi mới lên đầu danh sách
//        payments.add(0, payment);
//        paymentXML.setPayments(payments);
//
//        FileUtils.writeToFile(path, paymentXML);
//    }
//}

package org.example.service;

import org.example.entity.Payment;
import org.example.entity.PaymentXML;
import org.example.utils.FileUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Service xử lý lưu thông tin thanh toán vào file XML.
 */
public class PaymentService {

    /**
     * Tạo mới bản ghi thanh toán và lưu vào file XML.
     *
     * @param bookingId mã đặt phòng
     * @param fullName  họ tên người thanh toán
     * @param userName  tên tài khoản người dùng
     * @param room      mã phòng hoặc tên phòng
     * @param amount    số tiền đã thanh toán
     * @param method    phương thức thanh toán (ví dụ: Chuyển khoản, Tiền mặt,...)
     * @param code      mã giao dịch/tham chiếu thanh toán
     * @param checkIn   thời gian nhận phòng (LocalDateTime)
     * @param checkOut  thời gian trả phòng (LocalDateTime)
     */
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
}
