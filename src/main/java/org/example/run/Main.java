package org.example.run;

import com.formdev.flatlaf.FlatLightLaf;
import org.example.view.MainFrameView;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        SwingUtilities.invokeLater(MainFrameView::new);
    }
}
