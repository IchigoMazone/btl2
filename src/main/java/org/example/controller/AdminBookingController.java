package org.example.controller;

import org.example.view.AdminBookingView;
import org.example.view.MainFrameView;
import org.example.action.CheckAdminBooking;
import org.example.entity.SelectedRoomInfo;
import javax.swing.JPanel;

public class AdminBookingController {
    private AdminBookingView view;
    private MainFrameView mainFrame;
    private SelectedRoomInfo selectedRoom;
    private CheckAdminBooking bookingAction;

    public AdminBookingController(AdminBookingView view, MainFrameView mainFrame, SelectedRoomInfo selectedRoom) {
        this.view = view;
        this.mainFrame = mainFrame;
        this.selectedRoom = selectedRoom;
        this.bookingAction = new CheckAdminBooking(this);
        initializeListeners();
    }

    private void initializeListeners() {
        view.getCbLoaiChung().addActionListener(e -> bookingAction.updateMaChungField());
        view.getCbSoNguoi().addActionListener(e -> bookingAction.updateGuestFields());
        for (int i = 1; i < 4; i++) {
            int index = i;
            view.getLoaiCombos()[i].addActionListener(e -> bookingAction.updateMaField(index));
        }
        view.getBtnRandom().addActionListener(e -> bookingAction.handleRandomUsername());
        view.getBtnQuayLai().addActionListener(e -> bookingAction.handleQuayLai());
        view.getBtnXacNhan().addActionListener(e -> bookingAction.handleXacNhan());
    }

    public AdminBookingView getView() {
        return view;
    }

    public MainFrameView getMainFrame() {
        return mainFrame;
    }

    public SelectedRoomInfo getSelectedRoom() {
        return selectedRoom;
    }

    public JPanel getPanel() {
        return view.getPanel();
    }
}