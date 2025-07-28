package org.example.action;

import org.example.controller.RequestController;
import org.example.entity.*;
import org.example.view.RequestView;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CheckRequest extends DefaultCellEditor {
    private static final DateTimeFormatter DETAILED_DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private final JButton button;
    private final DefaultTableModel model;
    private final List<Request> requests;
    private final JTable table;
    private final RequestController controller;
    private int selectedRow;
    private String currentValue;

    public CheckRequest(JCheckBox checkBox, DefaultTableModel model, List<Request> requests, JTable table, RequestController controller) {
        super(checkBox);
        this.model = model;
        this.requests = requests;
        this.table = table;
        this.controller = controller;

        button = new JButton();
        RequestView.styleButton(button);

        button.addActionListener(e -> {
            if (selectedRow >= 0 && selectedRow < requests.size()) {
                if (table.isEditing()) {
                    table.getCellEditor().stopCellEditing();
                }
                Request r = requests.get(selectedRow);
                showDetailDialog(r);
            }
        });
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        selectedRow = row;
        currentValue = value == null ? "" : value.toString();
        button.setText(currentValue);
        button.setForeground(currentValue.contains("[Đã đọc]") ? Color.GRAY : new Color(33, 99, 255));
        return button;
    }

    @Override
    public Object getCellEditorValue() {
        return currentValue;
    }

    private void reloadTable() {
        List<Request> updated = controller.getFilteredRequests();
        if (updated == null) {
            JOptionPane.showMessageDialog(null, "Không thể đọc dữ liệu yêu cầu!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        requests.clear();
        requests.addAll(updated);
        controller.loadTableData(requests, model);
    }

    private void showDetailDialog(Request r) {
        List<Person> people = r.getPersons() != null ? r.getPersons() : new ArrayList<>();
        Person daiDien = people.isEmpty() ? new Person("Không rõ", "", "") : people.get(0);
        int soNguoi = people.size();

        StringBuilder danhSach = new StringBuilder();
        for (Person p : people) {
            danhSach.append("- ").append(p.getFullName())
                    .append(" [").append(p.getDocumentType()).append(": ").append(p.getDocumentCode()).append("]\n");
        }

        String chiTiet = String.format("""
                THÔNG TIN ĐẶT PHÒNG

                Người dùng: %s
                Người đại diện: %s
                CCCD: %s
                Gmail: %s
                SĐT: %s

                Số người: %d

                Danh sách khách:
                %s
                Phòng: %s
                Giá: %,.0f VND

                Check-in: %s
                Check-out: %s
                Tạo yêu cầu: %s
                Trạng thái: %s
                """,
                Objects.requireNonNullElse(r.getUserName(), "Không rõ"),
                daiDien.getFullName(), daiDien.getDocumentCode(),
                Objects.requireNonNullElse(r.getEmail(), "Không rõ"),
                Objects.requireNonNullElse(r.getPhone(), "Không rõ"),
                soNguoi, danhSach,
                Objects.requireNonNullElse(r.getRoomId(), "Không rõ"), r.getAmount(),
                r.getCheckIn().format(DETAILED_DATE_FORMATTER),
                r.getCheckOut().format(DETAILED_DATE_FORMATTER),
                r.getSubmittedAt().format(DETAILED_DATE_FORMATTER),
                r.getStatus()
        );

        JTextArea textArea = new JTextArea(chiTiet);
        textArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        textArea.setEditable(false);
        textArea.setMargin(new Insets(10, 10, 10, 10));
        textArea.setPreferredSize(new Dimension(500, 450));

        JPanel content = new JPanel(new BorderLayout());
        content.add(new JScrollPane(textArea), BorderLayout.CENTER);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnQuayLai = new JButton("Quay lại");
        JButton btnCancel = new JButton("Hủy");
        JButton btnConfirm = new JButton("Xác nhận");

        btnQuayLai.addActionListener(e -> {
            if (!"Đã đọc".equalsIgnoreCase(r.getStatus())) {
                controller.updateRequestStatus(r.getRequestId(), "Đã đọc");
                r.setStatus("Đã đọc");
                if (selectedRow < model.getRowCount()) {
                    String msg = String.format("%s đã gửi yêu cầu đặt phòng lúc %s [Đã đọc]",
                            Objects.requireNonNullElse(r.getUserName(), "Không rõ"),
                            r.getSubmittedAt().format(DateTimeFormatter.ofPattern("HH:mm dd-MM-yyyy")));
                    model.setValueAt(msg, selectedRow, 0);
                }
            }
            SwingUtilities.getWindowAncestor(content).dispose();
            SwingUtilities.invokeLater(this::reloadTable);
        });

        btnCancel.addActionListener(e -> {
            if (!"Gửi yêu cầu".equalsIgnoreCase(r.getStatus()) &&
                    !"Đã đọc".equalsIgnoreCase(r.getStatus()) &&
                    !"Gửi yêu cầu hủy".equalsIgnoreCase(r.getStatus())) {
                JOptionPane.showMessageDialog(null, "Yêu cầu đã xử lý. Không thể hủy.", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                return;
            }
            int choice = JOptionPane.showConfirmDialog(content,
                    "Bạn có chắc chắn muốn hủy yêu cầu này?",
                    "Xác nhận hủy", JOptionPane.YES_NO_OPTION);
            if (choice == JOptionPane.YES_OPTION) {
                String bookingId = "BK00000001";
                if ("Gửi yêu cầu hủy".equalsIgnoreCase(r.getStatus())) {
                    bookingId = controller.getBookingIdForCancelRequest(r.getRequestId());
                    if (bookingId == null) {
                        JOptionPane.showMessageDialog(null, "Không tìm thấy booking liên quan để hủy.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }

                if ("Gửi yêu cầu".equalsIgnoreCase(r.getStatus())) {
                    controller.updateRequestStatus(r.getRequestId(), "Đã bị hủy");
                    controller.createNotification(
                            bookingId, r.getRequestId(), Objects.requireNonNullElse(r.getUserName(), "Không rõ"),
                            "Đã bị hủy", "Đã gửi"
                    );
                    JOptionPane.showMessageDialog(null, "Bạn đã hủy yêu cầu.", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                } else if ("Gửi yêu cầu hủy".equalsIgnoreCase(r.getStatus())) {
                    controller.updateRequestStatus(r.getRequestId(), "Đã bị hủy");
                    controller.createNotification(
                            bookingId, r.getRequestId(), Objects.requireNonNullElse(r.getUserName(), "Không rõ"),
                            "Không được hủy", "Đã gửi"
                    );
                    JOptionPane.showMessageDialog(null, "Bạn đã hủy yêu cầu.", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                } else if ("Đã đọc".equalsIgnoreCase(r.getStatus())) {
                    List<HistoryEntry> histories = r.getHistory();
                    if (histories != null && !histories.isEmpty()) {
                        HistoryEntry latestHistory = histories.get(histories.size() - 1);
                        if (latestHistory.getStatus() != null) {
                            if (histories.size() > 1) {
                                HistoryEntry previous = histories.get(histories.size() - 2);
                                String previousStatus = previous.getStatus() != null ? previous.getStatus().trim() : "";
                                if ("Gửi yêu cầu".equalsIgnoreCase(previousStatus)) {
                                    controller.updateRequestStatus(r.getRequestId(), "Đã bị hủy");
                                    controller.createNotification(
                                            bookingId, r.getRequestId(), Objects.requireNonNullElse(r.getUserName(), "Không rõ"),
                                            "Đã bị hủy", "Đã gửi"
                                    );
                                    JOptionPane.showMessageDialog(null, "Bạn đã hủy yêu cầu.", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                                } else if ("Gửi yêu cầu hủy".equalsIgnoreCase(previousStatus)) {
                                    controller.updateRequestStatus(r.getRequestId(), "Đã bị hủy");
                                    controller.createNotification(
                                            bookingId, r.getRequestId(), Objects.requireNonNullElse(r.getUserName(), "Không rõ"),
                                            "Không được hủy", "Đã gửi"
                                    );
                                    JOptionPane.showMessageDialog(null, "Bạn đã hủy yêu cầu.", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                                }
                            }
                        }
                    }
                }
                SwingUtilities.getWindowAncestor(content).dispose();
                SwingUtilities.invokeLater(this::reloadTable);
            }
        });

        btnConfirm.addActionListener(e -> {
            if (!"Gửi yêu cầu".equalsIgnoreCase(r.getStatus()) &&
                    !"Đã đọc".equalsIgnoreCase(r.getStatus()) &&
                    !"Gửi yêu cầu hủy".equalsIgnoreCase(r.getStatus())) {
                JOptionPane.showMessageDialog(null, "Yêu cầu đã xử lý. Không thể xác nhận.", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                return;
            }
            int choice = JOptionPane.showConfirmDialog(content,
                    "Bạn có chắc chắn muốn xác nhận yêu cầu này?",
                    "Xác nhận thông báo", JOptionPane.YES_NO_OPTION);
            if (choice == JOptionPane.YES_OPTION) {
                if ("Gửi yêu cầu hủy".equalsIgnoreCase(r.getStatus())) {
                    String bookingId = controller.getBookingIdForCancelRequest(r.getRequestId());
                    if (bookingId == null) {
                        JOptionPane.showMessageDialog(null, "Không tìm thấy booking liên quan để xác nhận hủy.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    controller.updateRequestStatus(r.getRequestId(), "Đã bị hủy");
                    controller.updateBookingStatus(bookingId, "Đã bị hủy");
                    controller.createNotification(
                            bookingId, r.getRequestId(), Objects.requireNonNullElse(r.getUserName(), "Không rõ"),
                            "Đã được hủy", "Đã gửi"
                    );
                    JOptionPane.showMessageDialog(null, "Bạn đã xác nhận hủy yêu cầu.", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                } else if ("Gửi yêu cầu".equalsIgnoreCase(r.getStatus())) {
                    String newBookingId = controller.generateBookingId();
                    controller.updateRequestStatus(r.getRequestId(), "Đã được duyệt");
                    try {
                        controller.createBooking(
                                newBookingId,
                                r,
                                daiDien.getFullName(),
                                r.getPersons() != null ? r.getPersons() : new ArrayList<>()
                        );
                        controller.createNotification(
                                newBookingId, r.getRequestId(), Objects.requireNonNullElse(r.getUserName(), "Không rõ"),
                                "Đã được duyệt", "Đã gửi"
                        );
                        JOptionPane.showMessageDialog(null, "Bạn đã xác nhận yêu cầu và tạo booking thành công.", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(null, "Lỗi khi tạo booking: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                    }
                } else if ("Đã đọc".equalsIgnoreCase(r.getStatus())) {
                    List<HistoryEntry> histories = r.getHistory();
                    if (histories != null && !histories.isEmpty()) {
                        HistoryEntry latestHistory = histories.get(histories.size() - 1);
                        if (latestHistory.getStatus() != null) {
                            if (histories.size() > 1) {
                                HistoryEntry previous = histories.get(histories.size() - 2);
                                String previousStatus = previous.getStatus() != null ? previous.getStatus().trim() : "";
                                if ("Gửi yêu cầu".equalsIgnoreCase(previousStatus)) {
                                    String bookingId = controller.getBookingIdForCancelRequest(r.getRequestId());
                                    if (bookingId == null) {
                                        JOptionPane.showMessageDialog(null, "Không tìm thấy booking liên quan để xác nhận hủy.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                                        return;
                                    }
                                    controller.updateRequestStatus(r.getRequestId(), "Đã bị hủy");
                                    controller.updateBookingStatus(bookingId, "Đã bị hủy");
                                    controller.createNotification(
                                            bookingId, r.getRequestId(), Objects.requireNonNullElse(r.getUserName(), "Không rõ"),
                                            "Đã được hủy", "Đã gửi"
                                    );
                                    JOptionPane.showMessageDialog(null, "Bạn đã xác nhận hủy yêu cầu.", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                                } else if ("Gửi yêu cầu hủy".equalsIgnoreCase(previousStatus)) {
                                    String newBookingId = controller.generateBookingId();
                                    controller.updateRequestStatus(r.getRequestId(), "Đã được duyệt");
                                    try {
                                        controller.createBooking(
                                                newBookingId,
                                                r,
                                                daiDien.getFullName(),
                                                r.getPersons() != null ? r.getPersons() : new ArrayList<>()
                                        );
                                        controller.createNotification(
                                                newBookingId, r.getRequestId(), Objects.requireNonNullElse(r.getUserName(), "Không rõ"),
                                                "Đã được duyệt", "Đã gửi"
                                        );
                                        JOptionPane.showMessageDialog(null, "Bạn đã xác nhận yêu cầu và tạo booking thành công.", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                                    } catch (Exception ex) {
                                        JOptionPane.showMessageDialog(null, "Lỗi khi tạo booking: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                                    }
                                }
                            }
                        }
                    }
                }
                SwingUtilities.getWindowAncestor(content).dispose();
                SwingUtilities.invokeLater(this::reloadTable);
            }
        });

        buttons.add(btnQuayLai);
        buttons.add(btnCancel);
        buttons.add(btnConfirm);
        content.add(buttons, BorderLayout.SOUTH);

        JDialog dialog = new JDialog((Frame) null, "Chi tiết yêu cầu", true);
        dialog.setContentPane(content);
        dialog.setSize(550, 460);
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
    }
}