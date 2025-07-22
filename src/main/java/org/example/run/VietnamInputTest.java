package org.example.run;

import javax.swing.*;
import java.awt.*;

public class VietnamInputTest {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Test Gõ Tiếng Việt");
        JTextField textField = new JTextField();
        textField.setFont(new Font("Arial", Font.PLAIN, 16));

        frame.setLayout(new BorderLayout());
        frame.add(textField, BorderLayout.CENTER);

        frame.setSize(400, 100);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
