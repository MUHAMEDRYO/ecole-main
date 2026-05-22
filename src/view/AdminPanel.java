package view;

import model.Utilisateur;

import javax.swing.*;
import java.awt.*;

public class AdminPanel extends JPanel {

    public AdminPanel(Utilisateur user, JFrame frame) {

        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel l = new JLabel("HIGHER INSTITUTE OF COMPUTER SCIENCE AND MATHEMATICS");
        l.setFont(new Font("Century Gothic", Font.BOLD, 26));

        this.add(l);

        JLabel l1 = new JLabel("2025/2026");
        l1.setFont(new Font("Century Gothic", Font.BOLD, 26));
        this.add(l1);
        JLabel footer = new JLabel("System Status: Operational | Connected Securely", SwingConstants.CENTER);
        footer.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        footer.setForeground(new Color(189, 195, 199));
        this.add(footer, BorderLayout.SOUTH);

    }
}
