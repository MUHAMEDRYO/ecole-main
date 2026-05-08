package view;

import com.mysql.cj.Session;
import controller.AuthController;
import model.Utilisateur;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class MainDashboard extends JFrame {
    private JPanel container;
    private AuthController authController;
    private Image backgroundImage;

    public MainDashboard(Utilisateur user) {
        this(user, new AuthController());
    }

    public MainDashboard(Utilisateur user, AuthController authController) {
        this.authController = authController;

        // 1. Chargement mta3 el background
        try {
            backgroundImage = ImageIO.read(new File("src/img/download.jpeg"));
        } catch (IOException e) {
            System.out.println("Erreur: Malqitech el taswira fi src/img/");
        }

        setTitle("Tableau de Bord - " + user.getRole() + " (" + user.getUsername() + ")");
        setSize(1100, 750);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // 2. Background Panel elli yersem el taswira w fih Overlay
        JPanel backgroundPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (backgroundImage != null) {
                    g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
                    // Overlay ghaméq chwaya bech el khedma wel ktiba t-ban wadha
                    g.setColor(new Color(0, 0, 0, 90));
                    g.fillRect(0, 0, getWidth(), getHeight());
                }
            }
        };
        setContentPane(backgroundPanel);

        // 3. Menu Bar Design
        JMenuBar menuBar = new JMenuBar();
        menuBar.setBackground(new Color(33, 37, 41)); // Dark color professional
        UIManager.put("Menu.foreground", Color.BLACK);
        setJMenuBar(menuBar);

        // --- Setup Menus ---
        setupMenus(user, menuBar);

        // 4. Container Panel: Hetha elli bech i-hiz el Tables
        // Nesta3mlou GridBagLayout hna bech n-najmou n-centeriw el khedma
        JPanel wrapper = new JPanel(new GridBagLayout());
        wrapper.setOpaque(false); // Transparent bech n-choufou el background

        container = new JPanel(new BorderLayout());
        container.setOpaque(false);

        // Zidna EmptyBorder kbir lel container bech el Tables ma-yaklouch el blasa lkol
        container.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // N-centeriw el container west el wrapper
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        wrapper.add(container, gbc);

        initDefaultView(user);
        backgroundPanel.add(wrapper, BorderLayout.CENTER);
    }

    private void setupMenus(Utilisateur user, JMenuBar menuBar) {
        if ("ADMIN".equals(user.getRole())) {
            JMenu menuGestion = new JMenu("Administration");
            JMenuItem itemEtudiants = new JMenuItem("Gérer les Étudiants");
            JMenuItem itemProfs = new JMenuItem("Gérer les Enseignants");
            itemEtudiants.addActionListener(e -> switchView(new EtudiantManagementPanel()));
            itemProfs.addActionListener(e -> switchView(new EnseignantManagementPanel()));
            menuGestion.add(itemEtudiants); menuGestion.add(itemProfs);
            menuBar.add(menuGestion);
        }

        if ("ENSEIGNANT".equals(user.getRole())) {
            JMenu menuProf = new JMenu("Espace Enseignant");
            JMenuItem itemNotes = new JMenuItem("Gestion des Notes");
            itemNotes.addActionListener(e -> switchView(new NoteManagementPanel()));
            menuProf.add(itemNotes);
            menuBar.add(menuProf);
        }

        JMenu menuUser = new JMenu("Account");
        JMenuItem itemLogout = new JMenuItem("Déconnexion");
        itemLogout.addActionListener(e -> {
            this.authController.logout();
            new LoginFrame(this.authController).setVisible(true);
            this.dispose();
        });
        menuUser.add(itemLogout);
        menuBar.add(menuUser);
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
        // Kol panel n-7ottouh lazem i-koun transparent
        panel.setOpaque(false);
        container.removeAll();
        container.add(panel, BorderLayout.CENTER);
        container.revalidate();
        container.repaint();
    }
}
