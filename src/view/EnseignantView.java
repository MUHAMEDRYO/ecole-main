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

    private JMenu menuGérer, menuEspace, menuCompte, menuLanguage;
    private JLabel lblUserProf, lblRoleProf;
    private JPanel notesPanel, profPanel;

    // Changed parentFrame to MainDashboard to access its methods
    public EnseignantView(Utilisateur prof, MainDashboard dashboard) {
        setLayout(new BorderLayout());


        // --- MENU BAR ---
        JMenuBar menuBar = new JMenuBar();
        menuGérer = new JMenu("Gérer les Notes");
        menuEspace = new JMenu("Espace Enseignant");

        // Compte Menu
        menuCompte = new JMenu("Compte");
        menuCompte.add(itemLogout);

        // ACTION: LOGOUT
        itemLogout.addActionListener(e -> {
            dashboard.dispose(); // Close current dashboard
            // Assuming LoginFrame takes an AuthController or similar
            new LoginFrame(new AuthController()).setVisible(true);
        });

        // Language Menu
        menuLanguage = new JMenu("Language");
        menuLanguage.add(langFr);
        menuLanguage.add(langEn);
        menuLanguage.add(langAr);

        // ACTIONS: LANGUAGE SWITCHING
        langFr.addActionListener(e -> dashboard.switchView(new EnseignantView(prof, dashboard))); // Refresh view
        langEn.addActionListener(e -> dashboard.switchView(new EnseignantView(prof, dashboard))); 
        langAr.addActionListener(e -> dashboard.switchView(new EnseignantView(prof, dashboard)));

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
        notesPanel = createNotesPanel();
        profPanel = createProfPanel(prof);

        cards.add(notesPanel, "NOTES");
        cards.add(profPanel, "PROFIL");
        add(cards, BorderLayout.CENTER);
        
        translateUI("FR"); // Default
    }

    public void translateUI(String lang) {
        if (lang.equals("FR")) {
            menuGérer.setText("Gérer les Notes");
            menuEspace.setText("Espace Enseignant");
            menuCompte.setText("Compte");
            itemLogout.setText("Déconnexion");
            notesPanel.setBorder(BorderFactory.createTitledBorder("Liste des Étudiants"));
            profPanel.setBorder(BorderFactory.createTitledBorder("Informations Enseignant"));
            modelEtudiants.setColumnIdentifiers(new Object[]{"ID", "Nom", "Prénom", "Email"});
        } else if (lang.equals("EN")) {
            menuGérer.setText("Manage Grades");
            menuEspace.setText("Teacher Space");
            menuCompte.setText("Account");
            itemLogout.setText("Logout");
            notesPanel.setBorder(BorderFactory.createTitledBorder("Students List"));
            profPanel.setBorder(BorderFactory.createTitledBorder("Teacher Information"));
            modelEtudiants.setColumnIdentifiers(new Object[]{"ID", "Last Name", "First Name", "Email"});
        } else if (lang.equals("AR")) {
            menuGérer.setText("إدارة الأعداد");
            menuEspace.setText("فضاء الأستاذ");
            menuCompte.setText("الحساب");
            itemLogout.setText("تسجيل الخروج");
            notesPanel.setBorder(BorderFactory.createTitledBorder("قائمة الطلاب"));
            profPanel.setBorder(BorderFactory.createTitledBorder("معلومات الأستاذ"));
            modelEtudiants.setColumnIdentifiers(new Object[]{"المعرف", "اللقب", "الاسم", "البريد الإلكتروني"});
        }
        loadEtudiants();
    }

    private JPanel createNotesPanel() {
        JPanel panel = new JPanel(new BorderLayout());

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

    private JPanel createProfPanel(Utilisateur prof) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Informations Enseignant"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);
        lblUserProf = new JLabel("Nom d'utilisateur: " + prof.getUsername());
        panel.add(lblUserProf, gbc);
        gbc.gridy++;
        lblRoleProf = new JLabel("Rôle: " + prof.getRole());
        panel.add(lblRoleProf, gbc);
        return panel;
    }

    private void openEditNotesDialog(int row) {
        int etudiantId = (int) modelEtudiants.getValueAt(row, 0);
        String nom = modelEtudiants.getValueAt(row, 1).toString() + " " + modelEtudiants.getValueAt(row, 2).toString();
        
        JPanel dialogPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        JComboBox<Matiere> cbMatieres = new JComboBox<>();
        List<Matiere> matieres = matiereController.findAllMatieres();
        for (Matiere m : matieres) cbMatieres.addItem(m);

        JTextField txtDS = new JTextField();
        JTextField txtExam = new JTextField();

        dialogPanel.add(new JLabel("Étudiant:"));
        dialogPanel.add(new JLabel(nom));
        dialogPanel.add(new JLabel("Matière:"));
        dialogPanel.add(cbMatieres);
        dialogPanel.add(new JLabel("Note DS:"));
        dialogPanel.add(txtDS);
        dialogPanel.add(new JLabel("Note Examen:"));
        dialogPanel.add(txtExam);

        int result = JOptionPane.showConfirmDialog(null, dialogPanel, "Saisie des Notes", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try {
                controller.NoteController noteController = new controller.NoteController();
                model.Note note = new model.Note();
                note.setEtudiant(etudiantController.findEtudiantById(etudiantId));
                note.setMatiere((Matiere) cbMatieres.getSelectedItem());
                note.setNoteDs(Double.parseDouble(txtDS.getText()));
                note.setNoteExamen(Double.parseDouble(txtExam.getText()));
                
                noteController.addNote(note);
                JOptionPane.showMessageDialog(this, "Note enregistrée avec succès !");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erreur: " + ex.getMessage());
            }
        }
    }

    private void loadEtudiants() {
        modelEtudiants.setRowCount(0);
        List<Etudiant> list = etudiantController.findAllEtudiants();
        for (Etudiant e : list) {
            modelEtudiants.addRow(new Object[]{e.getId(), e.getNom(), e.getPrenom(), e.getEmail()});
        }
    }
}