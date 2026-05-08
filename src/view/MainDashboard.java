package view;

import model.Utilisateur;
import javax.swing.*;
import java.awt.*;

public class MainDashboard extends JFrame {
    public MainDashboard(Utilisateur user) {
        setTitle("Tableau de Bord - " + user.getRole());
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        // Common menu for Admin/Personnel
        if ("ADMIN".equals(user.getRole().toString())) {
            JMenu menuGestion = new JMenu("Gestion");
            JMenuItem itemEtudiants = new JMenuItem("Gérer Étudiants");
            JMenuItem itemProfs = new JMenuItem("Gérer Enseignants");

            itemEtudiants.addActionListener(e -> switchView(new EtudiantManagementPanel()));
            menuGestion.add(itemEtudiants);
            menuGestion.add(itemProfs);
            menuBar.add(menuGestion);
        }

        // Teacher specific menu
        if ("ENSEIGNANT".equals(user.getRole().toString())) {
            JMenu menuNotes = new JMenu("Notes");
            JMenuItem itemSaisir = new JMenuItem("Gestion des Notes");
            menuNotes.add(itemSaisir);
            menuBar.add(menuNotes);
        }

        add(new JLabel("Bienvenue, " + user.getUsername(), SwingConstants.CENTER), BorderLayout.CENTER);
    }

    private void switchView(JPanel panel) {
        getContentPane().removeAll();
        add(panel);
        revalidate();
        repaint();
    }
}