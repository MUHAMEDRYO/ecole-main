package view;

import controller.AuthController;
import controller.EtudiantController;
import controller.MatiereController;
import model.Etudiant;
import model.Matiere;
import model.Utilisateur;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.util.List;

public class EnseignantView extends JPanel {
    private EtudiantController etudiantController = new EtudiantController();
    private MatiereController matiereController = new MatiereController();
    private JTable tableEtudiants;
    private DefaultTableModel modelEtudiants;

    private JMenuItem itemLogout = new JMenuItem("Déconnexion");
    private JMenuItem langFr = new JMenuItem("Français");
    private JMenuItem langEn = new JMenuItem("English");
    private JMenuItem langAr = new JMenuItem("العربية");

    private CardLayout cardLayout = new CardLayout();
    private JPanel cards = new JPanel(cardLayout);

    // Changed parentFrame to MainDashboard to access its methods
    public EnseignantView(Utilisateur prof, MainDashboard dashboard) {
        setLayout(new BorderLayout());


        // --- MENU BAR ---
        JMenuBar menuBar = new JMenuBar();
        JMenu menuGérer = new JMenu("Gérer les Notes");
        JMenu menuEspace = new JMenu("Espace Enseignant");

        // Compte Menu
        JMenu menuCompte = new JMenu("Compte");
        menuCompte.add(itemLogout);

        // ACTION: LOGOUT
        itemLogout.addActionListener(e -> {
            dashboard.dispose(); // Close current dashboard
            // Assuming LoginFrame takes an AuthController or similar
            new LoginFrame(new AuthController()).setVisible(true);
        });

        // Language Menu
        JMenu menuLanguage = new JMenu("Language");
        menuLanguage.add(langFr);
        menuLanguage.add(langEn);
        menuLanguage.add(langAr);

        // ACTIONS: LANGUAGE SWITCHING
        langFr.addActionListener(e -> translateUI("FR", menuGérer, menuEspace, menuCompte));
        langEn.addActionListener(e -> translateUI("EN", menuGérer, menuEspace, menuCompte));
        langAr.addActionListener(e -> translateUI("AR", menuGérer, menuEspace, menuCompte));

        menuGérer.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cardLayout.show(cards, "NOTES");
            }
        });

        menuEspace.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cardLayout.show(cards, "PROFIL");
            }
        });

        menuBar.add(menuGérer);
        menuBar.add(menuEspace);
        menuBar.add(menuCompte);
        menuBar.add(menuLanguage);

        dashboard.setJMenuBar(menuBar);

        // --- CONTENT ---
        JPanel notesPanel = createNotesPanel();
        JPanel profPanel = createProfPanel(prof);

        cards.add(notesPanel, "NOTES");
        cards.add(profPanel, "PROFIL");
        add(cards, BorderLayout.CENTER);
    }

    private void translateUI(String lang, JMenu m1, JMenu m2, JMenu m3) {
        if (lang.equals("FR")) {
            m1.setText("Gérer les Notes");
            m2.setText("Espace Enseignant");
            m3.setText("Compte");
            itemLogout.setText("Déconnexion");
        } else if (lang.equals("EN")) {
            m1.setText("Manage Grades");
            m2.setText("Teacher Space");
            m3.setText("Account");
            itemLogout.setText("Logout");
        } else if (lang.equals("AR")) {
            m1.setText("إدارة الأعداد");
            m2.setText("فضاء الأستاذ");
            m3.setText("الحساب");
            itemLogout.setText("تسجيل الخروج");
        }
    }

    private JPanel createNotesPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        // Header with Matières button
        JPanel topPanel = new JPanel(new BorderLayout());
        JButton btnMatiere = new JButton("Matières");
        btnMatiere.addActionListener(e -> showMatières());
        topPanel.add(btnMatiere, BorderLayout.EAST);
        panel.add(topPanel, BorderLayout.NORTH);

        String[] columns = {"ID", "Nom", "Prénom", "Email"};
        modelEtudiants = new DefaultTableModel(columns, 0);
        tableEtudiants = new JTable(modelEtudiants);
        loadEtudiants();

        tableEtudiants.getSelectionModel().addListSelectionListener(event -> {
            if (!event.getValueIsAdjusting() && tableEtudiants.getSelectedRow() != -1) {
                openEditNotesDialog(tableEtudiants.getSelectedRow());
            }
        });

        panel.add(new JScrollPane(tableEtudiants), BorderLayout.CENTER);
        panel.setBorder(BorderFactory.createTitledBorder("Liste des Étudiants"));
        return panel;
    }

    private void showMatières() {
        List<Matiere> matieres = matiereController.findAllMatieres();
        String[] columns = {"ID", "Nom", "Enseignant"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);

        for (Matiere m : matieres) {
            String profName = (m.getEnseignant() != null) ? m.getEnseignant().getNom() + " " + m.getEnseignant().getPrenom() : "N/A";
            model.addRow(new Object[]{m.getId(), m.getNom(), profName});
        }

        JTable table = new JTable(model);
        JOptionPane.showMessageDialog(this, new JScrollPane(table), "Liste des Matières", JOptionPane.INFORMATION_MESSAGE);
    }

    private JPanel createProfPanel(Utilisateur prof) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Informations Enseignant"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);
        panel.add(new JLabel("Nom d'utilisateur: " + prof.getUsername()), gbc);
        gbc.gridy++;
        panel.add(new JLabel("Rôle: " + prof.getRole()), gbc);
        return panel;
    }

    private void openEditNotesDialog(int row) {
        String nom = modelEtudiants.getValueAt(row, 1).toString();
        JPanel dialogPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        JTextField txtDS = new JTextField();
        JTextField txtExam = new JTextField();
        dialogPanel.add(new JLabel("Étudiant:"));
        dialogPanel.add(new JLabel(nom));
        dialogPanel.add(new JLabel("Note DS:"));
        dialogPanel.add(txtDS);
        dialogPanel.add(new JLabel("Note Examen:"));
        dialogPanel.add(txtExam);
        JOptionPane.showConfirmDialog(null, dialogPanel, "Saisie des Notes", JOptionPane.OK_CANCEL_OPTION);
    }

    private void loadEtudiants() {
        modelEtudiants.setRowCount(0);
        List<Etudiant> list = etudiantController.findAllEtudiants();
        for (Etudiant e : list) {
            modelEtudiants.addRow(new Object[]{e.getId(), e.getNom(), e.getPrenom(), e.getEmail()});
        }
    }
}