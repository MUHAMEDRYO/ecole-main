package view;

import model.Utilisateur;
import javax.swing.*;
import java.awt.*;

public class MainDashboard extends JFrame {
    private JPanel container; // El blasa win bech nbadlou el views

    public MainDashboard(Utilisateur user) {
        setTitle("Tableau de Bord - " + user.getRole() + " (" + user.getUsername() + ")");
        setSize(1000, 700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // 1. Menu Bar
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        // --- Menu Admin (Personnel) ---
        if ("ADMIN".equals(user.getRole())) {
            JMenu menuGestion = new JMenu("Administration");
            JMenuItem itemEtudiants = new JMenuItem("Gérer les Étudiants");
            JMenuItem itemProfs = new JMenuItem("Gérer les Enseignants");

            itemEtudiants.addActionListener(e -> switchView(new EtudiantManagementPanel()));
            // Itha 3andek EnseignantManagementPanel, zidou hna

            menuGestion.add(itemEtudiants);
            menuGestion.add(itemProfs);
            menuBar.add(menuGestion);
        }

        // --- Menu Enseignant ---
        if ("ENSEIGNANT".equals(user.getRole())) {
            JMenu menuProf = new JMenu("Espace Enseignant");
            JMenuItem itemListe = new JMenuItem("Mes Étudiants");
            JMenuItem itemNotes = new JMenuItem("Saisir les Notes");

            itemListe.addActionListener(e -> switchView(new EnseignantView(user)));
            itemNotes.addActionListener(e -> switchView(new NoteManagementPanel()));

            menuProf.add(itemListe);
            menuProf.add(itemNotes);
            menuBar.add(menuProf);
        }

        // --- Menu Étudiant ---
        if ("ETUDIANT".equals(user.getRole())) {
            JMenu menuEtu = new JMenu("Mon Espace");
            JMenuItem itemMesNotes = new JMenuItem("Consulter mes Notes");

            itemMesNotes.addActionListener(e -> switchView(new EtudiantView(user)));

            menuEtu.add(itemMesNotes);
            menuBar.add(menuEtu);
        }

        // --- Menu Deconnexion ---
        JMenu menuUser = new JMenu("Compte");
        JMenuItem itemLogout = new JMenuItem("Déconnexion");
        itemLogout.addActionListener(e -> {
            new LoginFrame().setVisible(true);
            this.dispose();
        });
        menuUser.add(itemLogout);
        menuBar.add(menuUser);

        // 2. Container Panel (Win n-affichiw el views)
        container = new JPanel(new BorderLayout());
        container.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Affichage par défaut selon le role
        initDefaultView(user);

        add(container, BorderLayout.CENTER);
    }

    private void initDefaultView(Utilisateur user) {
        if ("ADMIN".equals(user.getRole())) {
            switchView(new EtudiantManagementPanel());
        } else if ("ENSEIGNANT".equals(user.getRole())) {
            switchView(new EnseignantView(user));
        } else {
            switchView(new EtudiantView(user));
        }
    }

    public void switchView(JPanel panel) {
        container.removeAll();
        container.add(panel, BorderLayout.CENTER);
        container.revalidate();
        container.repaint();
    }
}