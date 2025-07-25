

package org.example.view;

import org.example.controller.RoomController;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class RoomView {

    public static JPanel createRoomSearchPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(Color.WHITE);

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
                "Trạng thái phòng", "Mã phòng", "Số phòng", "Giá phòng"
        });
        JComboBox<String> cbLoaiPhong = new JComboBox<>(new String[]{
                "Tất cả", "Phòng đơn", "Phòng đôi", "Phòng gia đình", "Phòng đặc biệt"
        });
        JTextField tfTuKhoa = new JTextField(20);
        JComboBox<String> cbTrangThai = new JComboBox<>(new String[]{"Đang trống", "Đang sử dụng"});

        // Panel chứa phần nhập từ khóa: có thể là text hoặc combo
        JPanel keywordPanel = new JPanel(new CardLayout());
        keywordPanel.add(tfTuKhoa, "text");
        keywordPanel.add(cbTrangThai, "combo");

        gbc.gridx = 0; gbc.gridy = 0;
        searchPanel.add(new JLabel("Tìm theo:"), gbc);
        gbc.gridx = 1;
        searchPanel.add(cbTieuChi, gbc);
        gbc.gridx = 2;
        searchPanel.add(new JLabel("Từ khóa:"), gbc);
        gbc.gridx = 3;
        searchPanel.add(keywordPanel, gbc);
        gbc.gridx = 4;
        searchPanel.add(new JLabel("Loại phòng:"), gbc);
        gbc.gridx = 5;
        searchPanel.add(cbLoaiPhong, gbc);

        // === BẢNG DỮ LIỆU ===
        String[] columns = {"Mã phòng", "Số phòng", "Kiểu phòng", "Giá tiền", "Trạng thái", "Mô tả"};
        DefaultTableModel tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };

        JTable table = new JTable(tableModel);
        Font bigFont = new Font("Arial", Font.PLAIN, 14);
        table.setFont(bigFont);
        table.setRowHeight(28);
        table.getTableHeader().setFont(bigFont);
        table.getTableHeader().setResizingAllowed(false);
        table.getTableHeader().setReorderingAllowed(false);

        // Đặt độ rộng cột cố định
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        table.getColumnModel().getColumn(0).setPreferredWidth(100); // Mã phòng
        table.getColumnModel().getColumn(1).setPreferredWidth(100); // Số phòng
        table.getColumnModel().getColumn(2).setPreferredWidth(150); // Kiểu phòng
        table.getColumnModel().getColumn(3).setPreferredWidth(100); // Giá tiền
        table.getColumnModel().getColumn(4).setPreferredWidth(120); // Trạng thái
        table.getColumnModel().getColumn(5).setPreferredWidth(400); // Mô tả

        JScrollPane scrollPane = new JScrollPane(table,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setPreferredSize(new Dimension(850, 250));
        scrollPane.setBorder(BorderFactory.createTitledBorder("Danh sách phòng"));
        scrollPane.getViewport().setBackground(Color.WHITE);

        // === PANEL NÚT ===
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnThem = new JButton("Thêm");
        JButton btnSua = new JButton("Sửa");
        JButton btnXoa = new JButton("Xóa");
        JButton btnTimKiem = new JButton("Tìm kiếm");

        btnPanel.add(btnThem);
        btnPanel.add(btnSua);
        btnPanel.add(btnXoa);
        btnPanel.add(btnTimKiem);

        // === KHỞI TẠO CONTROLLER ===
        RoomController controller;
        controller = new RoomController(tableModel, tfTuKhoa, cbTrangThai, cbTieuChi, cbLoaiPhong, table);

        // === SỰ KIỆN THAY ĐỔI TIÊU CHÍ ===
        cbTieuChi.addActionListener(e -> {
            String selected = cbTieuChi.getSelectedItem().toString();
            CardLayout cl = (CardLayout) keywordPanel.getLayout();
            if (selected.equals("Trạng thái phòng")) {
                cl.show(keywordPanel, "combo");
            } else {
                cl.show(keywordPanel, "text");
                if (selected.equals("Giá phòng")) {
                    tfTuKhoa.setText(""); // Xóa nội dung để tránh lỗi định dạng
                    tfTuKhoa.setToolTipText("Nhập giá (ví dụ: 350000 hoặc 300000-500000)");
                } else {
                    tfTuKhoa.setToolTipText(null);
                }
            }
        });

        // === SỰ KIỆN NÚT BẤM ===
        btnTimKiem.addActionListener(e -> controller.handleSearch());
        btnThem.addActionListener(e -> controller.handleAdd());
        btnSua.addActionListener(e -> controller.handleEdit());
        btnXoa.addActionListener(e -> controller.handleDelete());

        // === LOAD DỮ LIỆU BAN ĐẦU ===
        controller.loadRoomData();

        // === ĐẶT MẶC ĐỊNH ===
        cbTieuChi.setSelectedItem("Trạng thái phòng");
        cbTrangThai.setSelectedItem("Đang trống");
        ((CardLayout) keywordPanel.getLayout()).show(keywordPanel, "combo");

        // === THÊM VÀO PANEL CHÍNH ===
        panel.add(searchPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(btnPanel, BorderLayout.SOUTH);

        return panel;
    }
}


