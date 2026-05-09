package view;

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
    private Utilisateur currentUser;

    private JMenu menuGestion, menuProf, menuUser, menuLanguage;
    private JMenuItem itemEtudiants, itemProfs, itemPers, itemNotes, itemLogout;
    private JMenuItem langFr, langEn, langAr;

    public MainDashboard(Utilisateur user) {
        this(user, new AuthController());
    }

    public MainDashboard(Utilisateur user, AuthController authController) {
        this.currentUser = user;
        this.authController = authController;

        try {
            backgroundImage = ImageIO.read(new File("src/img/download.jpeg"));
        } catch (IOException e) {
            System.out.println("Erreur: Image introuvable.");
        }

        updateTitle("FR");
        setSize(1100, 750);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel backgroundPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (backgroundImage != null) {
                    g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
                    g.setColor(new Color(0, 0, 0, 90));
                    g.fillRect(0, 0, getWidth(), getHeight());
                }
            }
        };
        setContentPane(backgroundPanel);

        JMenuBar menuBar = new JMenuBar();
        menuBar.setBackground(new Color(33, 37, 41));
        setJMenuBar(menuBar);

        setupMenus(user, menuBar);
        translateUI("FR");

        JPanel wrapper = new JPanel(new GridBagLayout());
        wrapper.setOpaque(false);
        container = new JPanel(new BorderLayout());
        container.setOpaque(false);
        container.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 1.0; gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        wrapper.add(container, gbc);

        initDefaultView(user);
        backgroundPanel.add(wrapper, BorderLayout.CENTER);
    }

    private void setupMenus(Utilisateur user, JMenuBar menuBar) {
        if ("ADMIN".equals(user.getRole())) {
            menuGestion = new JMenu();
            itemEtudiants = new JMenuItem();
            itemProfs = new JMenuItem();
            itemPers = new JMenuItem();
            itemEtudiants.addActionListener(e -> switchView(new EtudiantManagementPanel()));
            itemProfs.addActionListener(e -> switchView(new EnseignantManagementPanel()));
            itemPers.addActionListener(e -> switchView(new PersonnelManagementPanel()));
            menuGestion.add(itemEtudiants); menuGestion.add(itemProfs); menuGestion.add(itemPers);
            menuBar.add(menuGestion);
        }

        if ("ENSEIGNANT".equals(user.getRole())) {
            menuProf = new JMenu();
            itemNotes = new JMenuItem();
            // FIX: Added the specific listener to load the interactive view
            itemNotes.addActionListener(e -> switchView(new EnseignantView(user, this)));
            menuProf.add(itemNotes);
            menuBar.add(menuProf);
        }

        menuUser = new JMenu();
        itemLogout = new JMenuItem();
        itemLogout.addActionListener(e -> {
            this.authController.logout();
            new LoginFrame(this.authController).setVisible(true);
            this.dispose();
        });
        menuUser.add(itemLogout);
        menuBar.add(menuUser);

        menuLanguage = new JMenu("Language");
        langFr = new JMenuItem("Français");
        langEn = new JMenuItem("English");
        langAr = new JMenuItem("العربية");
        langFr.addActionListener(e -> translateUI("FR"));
        langEn.addActionListener(e -> translateUI("EN"));
        langAr.addActionListener(e -> translateUI("AR"));
        menuLanguage.add(langFr); menuLanguage.add(langEn); menuLanguage.add(langAr);
        menuBar.add(menuLanguage);
    }

    private void translateUI(String lang) {
        if (lang.equals("FR")) {
            if (menuGestion != null) {
                menuGestion.setText("Administration");
                itemEtudiants.setText("Gérer les Étudiants");
                itemProfs.setText("Gérer les Enseignants");
                itemPers.setText("Gérer les Personnels");
            }
            if (menuProf != null) {
                menuProf.setText("Espace Enseignant");
                itemNotes.setText("Gestion des Notes");
            }
            menuUser.setText("Compte");
            itemLogout.setText("Déconnexion");
            updateTitle("FR");
        } else if (lang.equals("EN")) {
            if (menuGestion != null) {
                menuGestion.setText("Administration");
                itemEtudiants.setText("Manage Students");
                itemProfs.setText("Manage Teachers");
                itemPers.setText("Manage Staff");
            }
            if (menuProf != null) {
                menuProf.setText("Teacher Space");
                itemNotes.setText("Grades Management");
            }
            menuUser.setText("Account");
            itemLogout.setText("Logout");
            updateTitle("EN");
        } else if (lang.equals("AR")) {
            if (menuGestion != null) {
                menuGestion.setText("الإدارة");
                itemEtudiants.setText("إدارة الطلاب");
                itemProfs.setText("إدارة الأساتذة");
                itemPers.setText("إدارة الموظفين");
            }
            if (menuProf != null) {
                menuProf.setText("فضاء الأستاذ");
                itemNotes.setText("إدارة الأعداد");
            }
            menuUser.setText("الحساب");
            itemLogout.setText("تسجيل الخروج");
            updateTitle("AR");
        }
    }

    private void updateTitle(String lang) {
        String base = lang.equals("AR") ? "لوحة القيادة" : (lang.equals("EN") ? "Dashboard" : "Tableau de Bord");
        setTitle(base + " - " + currentUser.getRole() + " (" + currentUser.getUsername() + ")");
    }

    private void initDefaultView(Utilisateur user) {
        if ("ADMIN".equals(user.getRole())) {
            switchView(new EtudiantManagementPanel());
        } else if ("ENSEIGNANT".equals(user.getRole())) {
            // FIX: Pass 'this' parameter
            switchView(new EnseignantView(user, this));
        } else {
            switchView(new EtudiantView(user));
        }
    }

    public void switchView(JPanel panel) {
        panel.setOpaque(false);
        container.removeAll();
        container.add(panel, BorderLayout.CENTER);
        container.revalidate();
        container.repaint();
    }
}