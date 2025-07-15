package org.example.view;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.AbstractMap;

public class CustomerView {

    public static JPanel createCustomerSearchPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

        // === PANEL TÌM KIẾM ===
        JPanel searchPanel = new JPanel(new GridBagLayout());
        searchPanel.setBackground(UIManager.getColor("Panel.background"));  // đặt màu panel chuẩn
        searchPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(),
                "Tìm kiếm khách hàng",
                TitledBorder.LEFT, TitledBorder.TOP));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 20, 10);
        gbc.anchor = GridBagConstraints.WEST;

        JComboBox<String> cbTieuChi = new JComboBox<>(new String[]{
                "Họ tên", "Mã phòng", "Mã định danh", "Số điện thoại", "Gmail"
        });
        JTextField tfTuKhoa = new JTextField(20);

        gbc.gridx = 0; gbc.gridy = 0;
        searchPanel.add(new JLabel("Tìm theo:"), gbc);

        gbc.gridx = 1;
        searchPanel.add(cbTieuChi, gbc);

        gbc.gridx = 2;
        searchPanel.add(new JLabel("Từ khóa:"), gbc);

        gbc.gridx = 3;
        searchPanel.add(tfTuKhoa, gbc);

        // === BẢNG DỮ LIỆU ===
        String[] columns = {"Họ tên", "Mã phòng", "Mã định danh", "SĐT", "Gmail"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };
        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Danh sách khách hàng"));

        // Dữ liệu mẫu
        Object[][] data = {
                {"Nguyễn Văn A", "P101", "012345678", "0912345678", "a@gmail.com"},
                {"Trần Thị B", "P102", "876543210", "0987654321", "b@gmail.com"},
                {"Lê Văn C", "P103", "1122334455", "0909123456", "c@gmail.com"},
        };
        for (Object[] row : data) model.addRow(row);

        // === PANEL NÚT ===
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnThem = new JButton("Thêm");
        JButton btnXoa = new JButton("Xóa");
        JButton btnSua = new JButton("Sửa");
        JButton btnTimKiem = new JButton("Tìm kiếm");

        btnPanel.add(btnThem);
        btnPanel.add(btnXoa);
        btnPanel.add(btnSua);
        btnPanel.add(btnTimKiem);

        // === TÌM KIẾM ===
        btnTimKiem.addActionListener(e -> {
            String keyword = tfTuKhoa.getText().trim().toLowerCase();
            int colIndex = cbTieuChi.getSelectedIndex();

            model.setRowCount(0);
            for (Object[] row : data) {
                if (row[colIndex].toString().toLowerCase().contains(keyword)) {
                    model.addRow(row);
                }
            }
        });

        // === THÊM ===
        btnThem.addActionListener(e -> showCustomerDialog(panel, model, null));

        // === XÓA ===
        btnXoa.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                int confirm = JOptionPane.showConfirmDialog(panel, "Bạn có chắc muốn xóa dòng đã chọn?", "Xác nhận", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    model.removeRow(selectedRow);
                }
            } else {
                JOptionPane.showMessageDialog(panel, "Vui lòng chọn dòng cần xóa.");
            }
        });

        // === SỬA ===
        btnSua.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                Object[] currentData = new Object[5];
                for (int i = 0; i < 5; i++) currentData[i] = model.getValueAt(selectedRow, i);
                showCustomerDialog(panel, model, new AbstractMap.SimpleEntry<>(selectedRow, currentData));
            } else {
                JOptionPane.showMessageDialog(panel, "Vui lòng chọn dòng cần sửa.");
            }
        });

        panel.add(searchPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(btnPanel, BorderLayout.SOUTH);

        return panel;
    }

    private static void showCustomerDialog(JPanel parent, DefaultTableModel model, AbstractMap.SimpleEntry<Integer, Object[]> editData) {
        JTextField tfHoTen = new JTextField(22);
        JTextField tfMaPhong = new JTextField(22);
        JTextField tfDinhDanh = new JTextField(22);
        JTextField tfSDT = new JTextField(22);
        JTextField tfGmail = new JTextField(22);

        if (editData != null) {
            Object[] d = editData.getValue();
            tfHoTen.setText(d[0].toString());
            tfMaPhong.setText(d[1].toString());
            tfDinhDanh.setText(d[2].toString());
            tfSDT.setText(d[3].toString());
            tfGmail.setText(d[4].toString());
        }

        JPanel inputPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridx = 0; gbc.gridy = 0;

        inputPanel.add(new JLabel("Họ tên:"), gbc); gbc.gridx = 1;
        inputPanel.add(tfHoTen, gbc); gbc.gridy++; gbc.gridx = 0;

        inputPanel.add(new JLabel("Mã phòng:"), gbc); gbc.gridx = 1;
        inputPanel.add(tfMaPhong, gbc); gbc.gridy++; gbc.gridx = 0;

        inputPanel.add(new JLabel("Mã định danh:"), gbc); gbc.gridx = 1;
        inputPanel.add(tfDinhDanh, gbc); gbc.gridy++; gbc.gridx = 0;

        inputPanel.add(new JLabel("Số điện thoại:"), gbc); gbc.gridx = 1;
        inputPanel.add(tfSDT, gbc); gbc.gridy++; gbc.gridx = 0;

        inputPanel.add(new JLabel("Gmail:"), gbc); gbc.gridx = 1;
        inputPanel.add(tfGmail, gbc);

        int result = JOptionPane.showConfirmDialog(parent, inputPanel,
                editData == null ? "Thêm khách hàng" : "Sửa thông tin",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            Object[] newRow = {
                    tfHoTen.getText(), tfMaPhong.getText(), tfDinhDanh.getText(),
                    tfSDT.getText(), tfGmail.getText()
            };

            if (editData == null) model.addRow(newRow);
            else {
                int row = editData.getKey();
                for (int i = 0; i < 5; i++) {
                    model.setValueAt(newRow[i], row, i);
                }
            }
        }
    }
}