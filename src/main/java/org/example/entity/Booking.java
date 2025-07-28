package org.example.entity;

import org.example.utils.LocalDateTimeAdapter;
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
    private String fullName;
    private String email;
    private String phone;
    private String roomId;

    @XmlJavaTypeAdapter(LocalDateTimeAdapter.class)
    private LocalDateTime checkIn;

    @XmlJavaTypeAdapter(LocalDateTimeAdapter.class)
    private LocalDateTime checkOut;

    private double amount;

    @XmlJavaTypeAdapter(LocalDateTimeAdapter.class)
    private LocalDateTime submittedAt;

    private String status;

    @XmlElement(name = "Person")
    private List<Person> persons = new ArrayList<>();

    @XmlElement(name = "History")
    private List<HistoryEntry> history = new ArrayList<>();

    public Booking() {
    }

    public Booking(
            String bookingId,
            String requestId,
            String userName,
            String fullName,
            String email,
            String phone,
            String roomId,
            LocalDateTime checkIn,
            LocalDateTime checkOut,
            double amount,
            LocalDateTime submittedAt,
            String status,
            List<Person> persons,
            List<HistoryEntry> history
            ) {

        this.bookingId = bookingId;
        this.requestId = requestId;
        this.userName = userName;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.roomId = roomId;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.amount = amount;
        this.submittedAt = submittedAt;
        this.status = status;
        if (persons != null) this.persons = persons;
        if (history != null) this.history = history;
    }

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

    public String getFullName() {
        return fullName;
    }
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
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

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public LocalDateTime getSubmittedAt() {
        return submittedAt;
    }
    public void setSubmittedAt(LocalDateTime submittedAt) {
        this.submittedAt = submittedAt;
    }

    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }

    public List<Person> getPersons() {
        return persons;
    }
    public void setPersons(List<Person> persons) {
        this.persons = persons;
    }

    public List<HistoryEntry> getHistory() {
        return history;
    }
    public void setHistory(List<HistoryEntry> history) {
        this.history = history;
    }

    public void addBookingHistory(String status, LocalDateTime timestamp) {
        if (history == null) {
            history = new ArrayList<>();
        }
        history.add(new HistoryEntry(timestamp, status));
    }

    public int getNumberOfGuests() {
        return persons != null ? persons.size() : 0;
    }

}
