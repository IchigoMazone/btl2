package org.example.Test;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "Booking")
@XmlAccessorType(XmlAccessType.FIELD)
public class Booking {

    private String bookingId;
    private String requestId;
    private String userName;
    private String roomId;

    @XmlJavaTypeAdapter(LocalDateTimeAdapter.class)
    private LocalDateTime checkIn;

    @XmlJavaTypeAdapter(LocalDateTimeAdapter.class)
    private LocalDateTime checkOut;

    @XmlJavaTypeAdapter(LocalDateTimeAdapter.class)
    private LocalDateTime createdAt;

    private String status;
    private double amount;

    @XmlElement(name = "history")
    private List<BookingHistoryEntry> history = new ArrayList<>();

    // Constructor mặc định cần thiết cho JAXB
    public Booking() {}

    public Booking(String bookingId, String requestId, String userName, String roomId,
                   LocalDateTime checkIn, LocalDateTime checkOut, double amount) {
        this.bookingId = bookingId;
        this.requestId = requestId;
        this.userName = userName;
        this.roomId = roomId;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.amount = amount;
        this.createdAt = LocalDateTime.now();
        this.status = "Đã tạo";
        this.history = new ArrayList<>();
        addHistory(status, createdAt);
    }

    // Getters & Setters
    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public List<BookingHistoryEntry> getHistory() {
        return history;
    }

    public void setHistory(List<BookingHistoryEntry> history) {
        this.history = history;
    }

    public void addHistory(String status, LocalDateTime time) {
        if (history == null) history = new ArrayList<>();
        history.add(new BookingHistoryEntry(time, status));
    }
}
