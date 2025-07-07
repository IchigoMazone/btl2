package org.example.Test;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "Bookings")
@XmlAccessorType(XmlAccessType.FIELD)
public class BookingXML {

    @XmlElement(name = "Booking")
    private List<Booking> bookingList = new ArrayList<>();

    public List<Booking> getBookings() {
        return bookingList;
    }

    public void setBookings(List<Booking> bookingList) {
        this.bookingList = bookingList;
    }
}
