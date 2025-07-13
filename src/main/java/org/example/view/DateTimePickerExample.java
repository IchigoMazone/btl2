//import com.formdev.flatlaf.FlatLightLaf;
//import com.github.lgooddatepicker.components.DatePicker;
//import com.github.lgooddatepicker.components.DatePickerSettings;
//
//import javax.swing.*;
//import java.awt.*;
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.time.LocalTime;
//
//public class DateTimePickerExample extends JFrame {
//
//    private DatePicker dpCheckIn, dpCheckOut;
//    private JComboBox<Integer> cbCheckInHour, cbCheckInMinute, cbCheckOutHour, cbCheckOutMinute;
//
//    public DateTimePickerExample() {
//        setTitle("Tách riêng ngày và giờ nhập");
//        setSize(600, 180);
//        setLocationRelativeTo(null);
//        setDefaultCloseOperation(EXIT_ON_CLOSE);
//
//        // Mỗi DatePicker có DatePickerSettings riêng
//        DatePickerSettings dateSettingsCheckIn = new DatePickerSettings();
//        dateSettingsCheckIn.setFormatForDatesCommonEra("dd-MM-yyyy");
//        dpCheckIn = new DatePicker(dateSettingsCheckIn);
//
//        DatePickerSettings dateSettingsCheckOut = new DatePickerSettings();
//        dateSettingsCheckOut.setFormatForDatesCommonEra("dd-MM-yyyy");
//        dpCheckOut = new DatePicker(dateSettingsCheckOut);
//
//        // Tạo array giờ và phút
//        Integer[] hours = new Integer[24];
//        for (int i = 0; i < 24; i++) {
//            hours[i] = i;
//        }
//        Integer[] minutes = new Integer[60];
//        for (int i = 0; i < 60; i++) {
//            minutes[i] = i;
//        }
//
//        // Tạo combobox giờ và phút, set max 10 dòng hiển thị dropdown
//        cbCheckInHour = new JComboBox<>(hours);
//        cbCheckInHour.setMaximumRowCount(10);
//        cbCheckOutHour = new JComboBox<>(hours);
//        cbCheckOutHour.setMaximumRowCount(10);
//
//        cbCheckInMinute = new JComboBox<>(minutes);
//        cbCheckInMinute.setMaximumRowCount(10);
//        cbCheckOutMinute = new JComboBox<>(minutes);
//        cbCheckOutMinute.setMaximumRowCount(10);
//
//        // Đặt kích thước hợp lý cho combobox
//        Dimension comboSize = new Dimension(60, cbCheckInHour.getPreferredSize().height);
//        cbCheckInHour.setPreferredSize(comboSize);
//        cbCheckOutHour.setPreferredSize(comboSize);
//        cbCheckInMinute.setPreferredSize(comboSize);
//        cbCheckOutMinute.setPreferredSize(comboSize);
//
//        JPanel panel = new JPanel(new GridBagLayout());
//        GridBagConstraints gbc = new GridBagConstraints();
//        gbc.insets = new Insets(5,10,5,10);
//        gbc.anchor = GridBagConstraints.WEST;
//
//        // Ngày đến
//        gbc.gridx = 0; gbc.gridy = 0;
//        panel.add(new JLabel("Ngày đến:"), gbc);
//        gbc.gridx = 1;
//        panel.add(dpCheckIn, gbc);
//        gbc.gridx = 2;
//        panel.add(new JLabel("Giờ:"), gbc);
//        gbc.gridx = 3;
//        panel.add(cbCheckInHour, gbc);
//        gbc.gridx = 4;
//        panel.add(new JLabel("Phút:"), gbc);
//        gbc.gridx = 5;
//        panel.add(cbCheckInMinute, gbc);
//
//        // Ngày đi
//        gbc.gridx = 0; gbc.gridy = 1;
//        panel.add(new JLabel("Ngày đi:"), gbc);
//        gbc.gridx = 1;
//        panel.add(dpCheckOut, gbc);
//        gbc.gridx = 2;
//        panel.add(new JLabel("Giờ:"), gbc);
//        gbc.gridx = 3;
//        panel.add(cbCheckOutHour, gbc);
//        gbc.gridx = 4;
//        panel.add(new JLabel("Phút:"), gbc);
//        gbc.gridx = 5;
//        panel.add(cbCheckOutMinute, gbc);
//
//        JButton btnGet = new JButton("Lấy ngày giờ");
//        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 6;
//        gbc.anchor = GridBagConstraints.CENTER;
//        panel.add(btnGet, gbc);
//
//        btnGet.addActionListener(e -> {
//            LocalDate checkInDate = dpCheckIn.getDate();
//            LocalDate checkOutDate = dpCheckOut.getDate();
//
//            Integer checkInH = (Integer) cbCheckInHour.getSelectedItem();
//            Integer checkInM = (Integer) cbCheckInMinute.getSelectedItem();
//            Integer checkOutH = (Integer) cbCheckOutHour.getSelectedItem();
//            Integer checkOutM = (Integer) cbCheckOutMinute.getSelectedItem();
//
//            if(checkInDate == null || checkOutDate == null) {
//                JOptionPane.showMessageDialog(this, "Vui lòng chọn đầy đủ ngày đến và ngày đi");
//                return;
//            }
//            if (checkInH == null || checkInM == null || checkOutH == null || checkOutM == null) {
//                JOptionPane.showMessageDialog(this, "Vui lòng chọn đầy đủ giờ và phút");
//                return;
//            }
//
//            LocalDateTime checkInDateTime = LocalDateTime.of(checkInDate, LocalTime.of(checkInH, checkInM));
//            LocalDateTime checkOutDateTime = LocalDateTime.of(checkOutDate, LocalTime.of(checkOutH, checkOutM));
//
//            if(checkOutDateTime.isBefore(checkInDateTime)) {
//                JOptionPane.showMessageDialog(this, "Ngày giờ đi phải sau ngày giờ đến!");
//                return;
//            }
//
//            JOptionPane.showMessageDialog(this,
//                    "Ngày giờ đến: " + checkInDateTime + "\n" +
//                            "Ngày giờ đi: " + checkOutDateTime);
//        });
//
//        add(panel);
//    }
//
//    public static void main(String[] args) {
//        try {
//            UIManager.setLookAndFeel(new FlatLightLaf());
//        } catch(Exception ignored) {}
//
//        SwingUtilities.invokeLater(() -> {
//            DateTimePickerExample frame = new DateTimePickerExample();
//            frame.setVisible(true);
//        });
//    }
//}
