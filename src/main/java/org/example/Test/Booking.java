//package org.example.Test;
//
//import javax.xml.bind.annotation.*;
//import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
//import java.time.LocalDateTime;
//import java.util.ArrayList;
//import java.util.List;
//
//@XmlAccessorType(XmlAccessType.FIELD)
//@XmlRootElement(name = "Booking")
//@XmlType(propOrder = {
//        "bookingId",
//        "requestId",
//        "userName",
//        "fullName",
//        "email",
//        "phone",
//        "roomId",
//        "checkIn",
//        "checkOut",
//        "createdAt",
//        "status",
//        "amount",
//        "companions",
//        "history"
//})
//public class Booking extends Request {
//
//    @XmlElement(name = "bookingId")
//    private String bookingId;
//
//    @XmlElement(name = "requestId")
//    private String requestId;
//
//    @XmlElement(name = "userName")
//    private String userName;
//
//    @XmlElement(name = "fullName")
//    private String fullName;
//
//    @XmlElement(name = "email")
//    private String email;
//
//    @XmlElement(name = "phone")
//    private String phone;
//
//    @XmlElement(name = "roomId")
//    private String roomId;
//
//    @XmlJavaTypeAdapter(LocalDateTimeAdapter.class)
//    private LocalDateTime checkIn;
//
//    @XmlJavaTypeAdapter(LocalDateTimeAdapter.class)
//    private LocalDateTime checkOut;
//
//    @XmlJavaTypeAdapter(LocalDateTimeAdapter.class)
//    private LocalDateTime createdAt;
//
//    @XmlElement(name = "status")
//    private String status;
//
//    @XmlElement(name = "amount")
//    private double amount;
//
//    @XmlElement(name = "companion")
//    private List<Person> companions = new ArrayList<>();
//
//    @XmlElement(name = "history")
//    private List<HistoryEntry> history = new ArrayList<>();
//
//    public Booking() {
//        super();
//    }
//
//    public Booking(String bookingId, String requestId, String userName, String fullName,
//                   String email, String phone, String roomId,
//                   LocalDateTime checkIn, LocalDateTime checkOut, double amount,
//                   List<Person> companions) {
//        super(requestId, userName, fullName, email, phone, roomId, checkIn, checkOut, "Đã tạo");
//        this.bookingId = bookingId;
//        this.amount = amount;
//        this.companions = companions != null ? companions : new ArrayList<>();
//        this.createdAt = LocalDateTime.now();
//        this.history = new ArrayList<>();
//        this.addRequestHistory("Đã tạo", this.createdAt);
//    }
//
//    // Getters & Setters
//    public String getBookingId() {
//        return bookingId;
//    }
//
//    public void setBookingId(String bookingId) {
//        this.bookingId = bookingId;
//    }
//
//    public String getRequestId() {
//        return requestId;
//    }
//
//    public void setRequestId(String requestId) {
//        this.requestId = requestId;
//    }
//
//    @Override
//    public String getUserName() {
//        return userName;
//    }
//
//    @Override
//    public void setUserName(String userName) {
//        this.userName = userName;
//    }
//
//    @Override
//    public String getFullName() {
//        return fullName;
//    }
//
//    @Override
//    public void setFullName(String fullName) {
//        this.fullName = fullName;
//    }
//
//    @Override
//    public String getEmail() {
//        return email;
//    }
//
//    @Override
//    public void setEmail(String email) {
//        this.email = email;
//    }
//
//    @Override
//    public String getPhone() {
//        return phone;
//    }
//
//    @Override
//    public void setPhone(String phone) {
//        this.phone = phone;
//    }
//
//    @Override
//    public String getRoomId() {
//        return roomId;
//    }
//
//    @Override
//    public void setRoomId(String roomId) {
//        this.roomId = roomId;
//    }
//
//    public LocalDateTime getCheckIn() {
//        return checkIn;
//    }
//
//    public void setCheckIn(LocalDateTime checkIn) {
//        this.checkIn = checkIn;
//    }
//
//    public LocalDateTime getCheckOut() {
//        return checkOut;
//    }
//
//    public void setCheckOut(LocalDateTime checkOut) {
//        this.checkOut = checkOut;
//    }
//
//    public LocalDateTime getCreatedAt() {
//        return createdAt;
//    }
//
//    public void setCreatedAt(LocalDateTime createdAt) {
//        this.createdAt = createdAt;
//    }
//
//    @Override
//    public String getStatus() {
//        return status;
//    }
//
//    @Override
//    public void setStatus(String status) {
//        this.status = status;
//    }
//
//    public double getAmount() {
//        return amount;
//    }
//
//    public void setAmount(double amount) {
//        this.amount = amount;
//    }
//
//    public List<Person> getCompanions() {
//        return companions;
//    }
//
//    public void setCompanions(List<Person> companions) {
//        this.companions = companions;
//    }
//
//    public List<HistoryEntry> getHistory() {
//        return history;
//    }
//
//    public void setHistory(List<HistoryEntry> history) {
//        this.history = history;
//    }
//
//    public void addBookingHistory(String status, LocalDateTime time) {
//        this.addRequestHistory(status, time);
//    }
//}
