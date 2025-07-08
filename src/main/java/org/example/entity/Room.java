package org.example.entity;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Room")
@XmlAccessorType(XmlAccessType.FIELD)
public class Room {
    private String roomId;
    private String roomNumber;
    private String type;
    private double price;
    private String description;

    public Room() {
    }

    public Room(String roomId, String roomNumber, String type, double price, String description) {
        this.roomId = roomId;
        this.roomNumber = roomNumber;
        this.type = type;
        this.price = price;
        this.description = description;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

