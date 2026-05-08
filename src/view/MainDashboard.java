package view;

import controller.AuthController;
import model.Utilisateur;
import javax.swing.*;
import java.awt.*;

// Hethi el fenétre el kbira elli t-hal ba3d el login
public class MainDashboard extends JFrame {
    private JPanel container; // Hetha el "Cadre" el faragh elli bech n-badlou fih el views
    private AuthController authController;

    // Constructeur sghir
    public MainDashboard(Utilisateur user) {
        this(user, new AuthController());
    }

    // El constructeur el kbir elli yabda fih kol chay
    public MainDashboard(Utilisateur user, AuthController authController) {
        this.authController = authController;

        // Na3tiw title lel fenétre fih el Role wel Username
        setTitle("Tableau de Bord - " + user.getRole() + " (" + user.getUsername() + ")");
        setSize(1000, 700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout()); // Layout bech n-nadhmou el menu wel west

        // 1. Menu Bar (El listerat elli mel fouq)
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        // --- Menu Admin: I-choufou ken el ADMIN ---
        if ("ADMIN".equals(user.getRole())) {
            JMenu menuGestion = new JMenu("Administration");
            JMenuItem itemEtudiants = new JMenuItem("Gérer les Étudiants");
            JMenuItem itemProfs = new JMenuItem("Gérer les Enseignants");

            // Ki nenzel 3la Gérer Etudiants, el view t-tbadel lel panel mte3hom
            itemEtudiants.addActionListener(e -> switchView(new EtudiantManagementPanel()));
            itemProfs.addActionListener(e -> switchView(new EnseignantManagementPanel()));

            menuGestion.add(itemEtudiants);
            menuGestion.add(itemProfs);
            menuBar.add(menuGestion);
        }

        // --- Menu Enseignant: I-choufou ken el PROF ---
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

        // --- Menu Étudiant: I-choufou ken el ETUDIANT ---
        if ("ETUDIANT".equals(user.getRole())) {
            JMenu menuEtu = new JMenu("Mon Espace");
            JMenuItem itemMesNotes = new JMenuItem("Consulter mes Notes");

            itemMesNotes.addActionListener(e -> switchView(new EtudiantView(user)));

            menuEtu.add(itemMesNotes);
            menuBar.add(menuEtu);
        }

        // --- Menu Deconnexion: Mawjoud 3and el ness el kol ---
        JMenu menuUser = new JMenu("Compte");
        JMenuItem itemLogout = new JMenuItem("Déconnexion");
        itemLogout.addActionListener(e -> {
            this.authController.logout(); // Na7iw el session
            new LoginFrame(this.authController).setVisible(true); // Narj3ou lel login
            this.dispose(); // N-sakkrou el Dashboard
        });
        menuUser.add(itemLogout);
        menuBar.add(menuUser);

        // 2. Container Panel (El blasa win n-affichiw el panels)
        container = new JPanel(new BorderLayout());
        container.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // N-7ellou el panel mta3 el bideya 3la 7asb chkoun d5al
        initDefaultView(user);

        add(container, BorderLayout.CENTER);
    }

    // Fonction t-ikhtar awel view t-dh'hor 3la 7asb el Role
    private void initDefaultView(Utilisateur user) {
        if ("ADMIN".equals(user.getRole())) {
            switchView(new EtudiantManagementPanel());
        } else if ("ENSEIGNANT".equals(user.getRole())) {
            switchView(new EnseignantView(user));
        } else {
            switchView(new EtudiantView(user));
        }
    }

    // Hethi aham fonction: Hiya elli tfassa5 chnowa maktoub fil west w t-7ot el panel el jdid
    public void switchView(JPanel panel) {
        container.removeAll(); // Fassa5 el panel el 9dim
        container.add(panel, BorderLayout.CENTER); // Zid el panel el jdid
        container.revalidate(); // 3awed 7seb el kobar (refresh)
        container.repaint();    // 3awed r-som el interface
    }
}