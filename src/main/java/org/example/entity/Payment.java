//package org.example.entity;
//
//import org.example.utils.LocalDateTimeAdapter;
//
//import javax.xml.bind.annotation.*;
//import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
//import java.time.LocalDateTime;
//
//@XmlRootElement(name = "Payment")
//@XmlAccessorType(XmlAccessType.FIELD)
//public class Payment {
//
//    private String paymentId;
//    private String bookingId;
//    private String fullName;
//    private double amount;
//    private String method;
//    private String status;
//
//    @XmlJavaTypeAdapter(LocalDateTimeAdapter.class)
//    private LocalDateTime paidAt;
//
//    public Payment() {}
//
//    public Payment(String paymentId, String bookingId, String fullName,
//                   double amount, String method, String status, LocalDateTime paidAt) {
//        this.paymentId = paymentId;
//        this.bookingId = bookingId;
//        this.fullName = fullName;
//        this.amount = amount;
//        this.method = method;
//        this.status = status;
//        this.paidAt = paidAt;
//    }
//
//    public String getPaymentId() { return paymentId; }
//    public void setPaymentId(String paymentId) { this.paymentId = paymentId; }
//
//    public String getBookingId() { return bookingId; }
//    public void setBookingId(String bookingId) { this.bookingId = bookingId; }
//
//    public String getFullName() { return fullName; }
//    public void setFullName(String fullName) { this.fullName = fullName; }
//
//    public double getAmount() { return amount; }
//    public void setAmount(double amount) { this.amount = amount; }
//
//    public String getMethod() { return method; }
//    public void setMethod(String method) { this.method = method; }
//
//    public String getStatus() { return status; }
//    public void setStatus(String status) { this.status = status; }
//
//    public LocalDateTime getPaidAt() { return paidAt; }
//    public void setPaidAt(LocalDateTime paidAt) { this.paidAt = paidAt; }
//}


package org.example.entity;

import org.example.utils.LocalDateTimeAdapter;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.time.LocalDateTime;

@XmlRootElement(name = "Payment")
@XmlAccessorType(XmlAccessType.FIELD)
public class Payment {

    private String paymentId;
    private String bookingId;
    private String fullName;
    private String userName;
    private String room;
    private double amount;
    private String method;
    private String code;
    private String status;

    @XmlJavaTypeAdapter(LocalDateTimeAdapter.class)
    private LocalDateTime checkIn;

    @XmlJavaTypeAdapter(LocalDateTimeAdapter.class)
    private LocalDateTime checkOut;

    @XmlJavaTypeAdapter(LocalDateTimeAdapter.class)
    private LocalDateTime paidAt;

    public Payment() {
    }

    public Payment(String paymentId, String bookingId, String fullName, String userName, String room,
                   double amount, String method, String code,
                   LocalDateTime checkIn, LocalDateTime checkOut,
                   String status, LocalDateTime paidAt) {
        this.paymentId = paymentId;
        this.bookingId = bookingId;
        this.fullName = fullName;
        this.userName = userName;
        this.room = room;
        this.amount = amount;
        this.method = method;
        this.code = code;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.status = status;
        this.paidAt = paidAt;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getCheckIn() {
        return checkIn;
    }

    public void setCheckIn(LocalDateTime checkIn) {
        this.checkIn = checkIn;
    }

    public LocalDateTime getCheckOut() {
        return checkOut;
    }

    public void setCheckOut(LocalDateTime checkOut) {
        this.checkOut = checkOut;
    }

    public LocalDateTime getPaidAt() {
        return paidAt;
    }

    public void setPaidAt(LocalDateTime paidAt) {
        this.paidAt = paidAt;
    }
}
