package org.example.controller;

import org.example.view.AdminSearchView;
import org.example.view.MainFrameView;
import org.example.action.CheckAdminSearch;
import org.example.entity.Room;
import org.example.entity.SelectedRoomInfo;
import org.example.service.RoomFinderService;
import org.example.service.BookingService;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.List;

public class AdminSearchController {
    private AdminSearchView view;
    private MainFrameView mainFrame;
    private RoomFinderService finder;
    private DecimalFormat currencyFormat;
    private CheckAdminSearch searchAction;

    public AdminSearchController(AdminSearchView view, MainFrameView mainFrame) {
        this.view = view;
        this.mainFrame = mainFrame;
        this.finder = new RoomFinderService("rooms.xml", "bookings.xml");
        this.currencyFormat = new DecimalFormat("#,###");
        this.searchAction = new CheckAdminSearch(this);
        initializeListeners();
    }

    private void initializeListeners() {
        view.getBtnSearch().addActionListener(e -> searchAction.handleSearch());
        view.getBtnBook().addActionListener(e -> searchAction.handleBooking());
    }

    public JPanel getPanel() {
        return view.getPanel();
    }

    public AdminSearchView getView() {
        return view;
    }

    public MainFrameView getMainFrame() {
        return mainFrame;
    }

    public RoomFinderService getFinder() {
        return finder;
    }

    public DecimalFormat getCurrencyFormat() {
        return currencyFormat;
    }

    public void displayRooms(List<Room> rooms, LocalDateTime checkIn, LocalDateTime checkOut) {
        DefaultTableModel tableModel = view.getTableModel();
        tableModel.setRowCount(0);
        for (Room room : rooms) {
            tableModel.addRow(new Object[]{
                    room.getRoomId(),
                    room.getDescription(),
                    room.getType(),
                    currencyFormat.format(BookingService.getAmount(checkIn, checkOut, room.getPrice()))
            });
        }
    }

    public SelectedRoomInfo getSelectedRoomInfo() {
        DefaultTableModel tableModel = view.getTableModel();
        JTable table = view.getTable();
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            return null;
        }

        String roomId = tableModel.getValueAt(selectedRow, 0).toString();
        String description = tableModel.getValueAt(selectedRow, 1).toString();
        String type = tableModel.getValueAt(selectedRow, 2).toString();
        String priceStr = tableModel.getValueAt(selectedRow, 3).toString().replace(",", "");
        double price = Double.parseDouble(priceStr);

        LocalDateTime checkIn = LocalDateTime.of(
                view.getDpIn().getDate(),
                java.time.LocalTime.of(view.getCbHourIn().getSelectedIndex(), view.getCbMinIn().getSelectedIndex())
        );

        LocalDateTime checkOut = LocalDateTime.of(
                view.getDpOut().getDate(),
                java.time.LocalTime.of(view.getCbHourOut().getSelectedIndex(), view.getCbMinOut().getSelectedIndex())
        );

        return new SelectedRoomInfo(roomId, description, type, price, checkIn, checkOut);
    }
}