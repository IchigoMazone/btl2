
package org.example.view;
import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.example.controller.RequestResetController;
import org.example.controller.BookingResetController;
import javax.swing.*;
import java.awt.*;

public class MainFrameView extends JFrame {
    private final CardLayout mainCardLayout;
    private final JPanel mainPanel;

    public static final String LOGIN = "LOGIN";
    public static final String REGISTER = "REGISTER";
    public static final String FORGOT_PASSWORD = "FORGOT_PASSWORD";
    public static final String PASSWORD_RESET = "PASSWORD_RESET";
    public static final String FINISH = "FINISH";
    public static final String ADMIN_CONTAINER = "ADMIN_CONTAINER";
    public static final String USER_CONTAINER = "USER_CONTAINER";

    private LoginView loginPanel;
    private RegisterView registerPanel;
    private ForgotPasswordView forgotPasswordPanel;
    private PasswordResetView passwordResetPanel;
    private FinishView finishPanel;

    private AdminContainerView adminContainerPanel;
    private CustomerContainerView userContainerPanel;
    private String loggedInUsername;

    public MainFrameView() {
        setTitle("Hotel Management System");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);
        setResizable(false);

        RequestResetController controller = new RequestResetController();
        controller.updateExpiredRequests();
        BookingResetController bookingResetController = new BookingResetController();
        bookingResetController.updateAbsentBookings();

        Timer timer = new Timer(60_000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.updateExpiredRequests();
                bookingResetController.updateAbsentBookings();
            }
        });
        timer.start();

        mainCardLayout = new CardLayout();
        mainPanel = new JPanel(mainCardLayout);

        loginPanel = new LoginView(this);
        registerPanel = new RegisterView(this);
        forgotPasswordPanel = new ForgotPasswordView(this);
        passwordResetPanel = new PasswordResetView(this);
        finishPanel = new FinishView(this);

        mainPanel.add(loginPanel, LOGIN);
        mainPanel.add(registerPanel, REGISTER);
        mainPanel.add(forgotPasswordPanel, FORGOT_PASSWORD);
        mainPanel.add(passwordResetPanel, PASSWORD_RESET);
        mainPanel.add(finishPanel, FINISH);

        setContentPane(mainPanel);
        showLoginPanel();
        setVisible(true);
    }

    public void showLoginPanel() {
        loginPanel.resetForm();
        mainCardLayout.show(mainPanel, LOGIN);
    }

    public void showRegisterPanel() {
        registerPanel.resetForm();
        mainCardLayout.show(mainPanel, REGISTER);
    }

    public void showForgotPasswordPanel() {
        forgotPasswordPanel.resetForm();
        mainCardLayout.show(mainPanel, FORGOT_PASSWORD);
    }

    public void showPasswordResetPanel(String username) {
        passwordResetPanel.resetForm();
        passwordResetPanel.setUsername(username);
        mainCardLayout.show(mainPanel, PASSWORD_RESET);
    }

    public void showFinishPanel() {
        mainCardLayout.show(mainPanel, FINISH);
    }

    public void showAdminContainerPanel() {
        if (adminContainerPanel == null) {
            adminContainerPanel = new AdminContainerView(this);
            mainPanel.add(adminContainerPanel, ADMIN_CONTAINER);
        }
        adminContainerPanel.resetToDefault();
        mainCardLayout.show(mainPanel, ADMIN_CONTAINER);
    }

    public void showUserContainerPanel(String username) {
        this.loggedInUsername = username;
        userContainerPanel = new CustomerContainerView(this, username);
        mainPanel.add(userContainerPanel, USER_CONTAINER);
        userContainerPanel.loadData(username);
        userContainerPanel.resetToDefault();
        mainCardLayout.show(mainPanel, USER_CONTAINER);
    }

    public void setCustomerDynamicContent(JPanel panel) {
        if (userContainerPanel != null) {
            userContainerPanel.setMainContent(panel);
            mainCardLayout.show(mainPanel, USER_CONTAINER);
        }
    }

    public void setAdminDynamicContent(JPanel panel) {
        if (adminContainerPanel != null) {
            adminContainerPanel.setMainContent(panel);
            mainCardLayout.show(mainPanel, ADMIN_CONTAINER);
        }
    }

    public String getLoggedInUsername() {
        return loggedInUsername;
    }

    public void setAdminSelectedMenu(String menuName) {
        adminContainerPanel.setSelectedMenu(menuName);
    }

    public void setCustomerSelectedMenu(String menuName) {
        userContainerPanel.setSelectedMenu(menuName);
    }
}

