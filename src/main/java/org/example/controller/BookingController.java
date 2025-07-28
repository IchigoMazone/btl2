package org.example.controller;

import org.example.view.BookingView;
import org.example.action.CheckBooking;
import javax.swing.JPanel;

public class BookingController {
    private BookingView view;
    private CheckBooking bookingAction;

    public BookingController(BookingView view) {
        this.view = view;
        this.bookingAction = new CheckBooking(this);
        initializeListeners();
    }

    private void initializeListeners() {
        view.getSearchBtn().addActionListener(e -> bookingAction.handleSearch());
        view.getUpdateBtn().addActionListener(e -> bookingAction.handleUpdate());
    }

    public BookingView getView() {
        return view;
    }

    public JPanel getPanel() {
        return view.getPanel();
    }
}