package org.example.entity;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SelectedRoomInfo {
    private String roomId;
    private String description;
    private String type;
    private double price;
    private LocalDateTime checkIn;
    private LocalDateTime checkOut;

    public SelectedRoomInfo() {
    }

    public SelectedRoomInfo(String roomId, String description, String type,
                            double price, LocalDateTime checkIn, LocalDateTime checkOut) {
        this.roomId = roomId;
        this.description = description;
        this.type = type;
        this.price = price;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

//    public String getCheckIn() {
//        return checkIn != null
//                ? checkIn.withSecond(0).withNano(0).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
//                : null;
//    }
//
//
//    public String getCheckOut() {
//        return checkIn != null
//                ? checkIn.withSecond(0).withNano(0).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
//                : null;
//    }

    public LocalDateTime getCheckIn() {
        return checkIn != null ? checkIn.withSecond(0).withNano(0) : null;
    }

    public LocalDateTime getCheckOut() {
        return checkOut != null ? checkOut.withSecond(0).withNano(0) : null;
    }


    public void setCheckOut(LocalDateTime checkOut) {
        this.checkOut = checkOut;
    }

    @Override
    public String toString() {
        return "SelectedRoomInfo{" +
                "roomId='" + roomId + '\'' +
                ", description='" + description + '\'' +
                ", type='" + type + '\'' +
                ", price=" + price +
                ", checkIn=" + checkIn +
                ", checkOut=" + checkOut +
                '}';
    }
}
