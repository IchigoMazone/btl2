package org.example.view;

import org.example.service.RoomFinderService;
import org.kordamp.ikonli.materialdesign.MaterialDesign;
import org.kordamp.ikonli.swing.FontIcon;
import org.example.entity.BookingXML;
import org.example.utils.FileUtils;

import javax.swing.*;
import java.awt.*;

public class StatisticalView {
    public static JPanel createDashboardPanel() {
        JPanel mainContent = new JPanel(new BorderLayout());
        mainContent.setPreferredSize(new Dimension(950, 800));
        mainContent.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1, true),
                "Thống kê tháng"
        ));
        mainContent.setBackground(UIManager.getColor("Panel.background"));

        JPanel grid = new JPanel(new GridLayout(2, 2, 20, 20));
        grid.setBackground(new Color(245, 245, 245));
        grid.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        BookingXML bookingXML = FileUtils.readFromFile("bookings.xml", BookingXML.class);
        int luotKhach = RoomFinderService.getTotalMonthlyCustomers(bookingXML);

        String doanhThu = RoomFinderService.getFormattedMonthlyRevenue(bookingXML);
        String luotKhachx = String.valueOf(luotKhach);
        String totalGuests = RoomFinderService.getMonthlyCustomerCountFormatted(bookingXML);
        String maxGuests = RoomFinderService.getMaxGuestCountInOneDay(bookingXML);

        grid.add(createStatCard(MaterialDesign.MDI_CALENDAR_RANGE, "Doanh thu tháng này", doanhThu, new Color(0, 123, 255)));
        grid.add(createStatCard(MaterialDesign.MDI_ACCOUNT_MULTIPLE, "Lượt khách tháng", luotKhachx, new Color(40, 167, 69)));
        grid.add(createStatCard(MaterialDesign.MDI_CHART_LINE, "Tỉ lệ đặt phòng", totalGuests, new Color(255, 193, 7)));
        grid.add(createStatCard(MaterialDesign.MDI_CHART_ARC, "Lượt khách cao nhất", maxGuests, new Color(220, 53, 69)));

        mainContent.add(grid, BorderLayout.CENTER);

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(Color.WHITE);
        wrapper.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        wrapper.add(mainContent, BorderLayout.CENTER);

        return wrapper;
    }

    private static JPanel createStatCard(MaterialDesign iconCode, String label, String value, Color accentColor) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.putClientProperty("JComponent.roundRect", true);
        card.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel iconLabel = new JLabel(FontIcon.of(iconCode, 36, accentColor));
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel titleLabel = new JLabel(label);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        titleLabel.setForeground(new Color(60, 60, 60));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Segoe UI", Font.PLAIN, 22));
        valueLabel.setForeground(accentColor);
        valueLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        card.add(iconLabel);
        card.add(Box.createVerticalStrut(8));
        card.add(titleLabel);
        card.add(valueLabel);

        return card;
    }
}