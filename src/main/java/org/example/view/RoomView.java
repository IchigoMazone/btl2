package org.example.view;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class RoomView {

    public static JPanel createRoomSearchPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

        // === PANEL TÌM KIẾM ===
        JPanel searchPanel = new JPanel(new GridBagLayout());
        searchPanel.setBackground(UIManager.getColor("Panel.background"));
        searchPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(),
                "Tìm kiếm phòng",
                TitledBorder.LEFT, TitledBorder.TOP));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 20, 10);
        gbc.anchor = GridBagConstraints.WEST;

        JComboBox<String> cbTieuChi = new JComboBox<>(new String[]{
                "Tất cả", "Mã phòng", "Số phòng", "Trạng thái phòng"
        });
        JComboBox<String> cbLoaiPhong = new JComboBox<>(new String[]{
                "Tất cả", "Phòng đơn", "Phòng đôi", "Phòng gia đình", "Phòng đặc biệt"
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

        gbc.gridx = 4;
        searchPanel.add(new JLabel("Loại phòng:"), gbc);

        gbc.gridx = 5;
        searchPanel.add(cbLoaiPhong, gbc);

        // === BẢNG DỮ LIỆU ===
        String[] columns = {"Mã phòng", "Số phòng", "Kiểu phòng", "Mô tả", "Trạng thái"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };
        JTable table = new JTable(model);
        table.getColumnModel().getColumn(0).setPreferredWidth(30);
        table.getColumnModel().getColumn(1).setPreferredWidth(30);
        table.getColumnModel().getColumn(2).setPreferredWidth(30);
        table.getColumnModel().getColumn(3).setPreferredWidth(200);
        table.getColumnModel().getColumn(4).setPreferredWidth(50);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Danh sách phòng"));

        // Dữ liệu mẫu
        Object[][] data = {
                {"P101", "101", "Phòng đơn", "Phòng đơn gần cửa sổ", "Đang trống"},
                {"P102", "102", "Phòng đôi", "Phòng đôi có ban công", "Đang sử dụng"},
                {"P201", "201", "Phòng đặc biệt", "Phòng gia đình có tivi lớn và tủ lạnh mini", "Đang trống"},
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
            String loaiPhong = cbLoaiPhong.getSelectedItem().toString();
            int colIndex = cbTieuChi.getSelectedIndex();

            model.setRowCount(0);
            for (Object[] row : data) {
                boolean matchTieuChi = colIndex == 0 || row[colIndex - 1].toString().toLowerCase().contains(keyword);
                boolean matchLoaiPhong = loaiPhong.equals("Tất cả") || row[2].toString().equalsIgnoreCase(loaiPhong);
                if (matchTieuChi && matchLoaiPhong) {
                    model.addRow(row);
                }
            }
        });

        panel.add(searchPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(btnPanel, BorderLayout.SOUTH);

        return panel;
    }
}
