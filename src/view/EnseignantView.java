package view;

import controller.EtudiantController;
import controller.MatiereController;
import model.Etudiant;
import model.Matiere;
import model.Utilisateur;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class EnseignantView extends JPanel {
    private EtudiantController etudiantController = new EtudiantController();
    private MatiereController matiereController = new MatiereController();
    private JTable tableEtudiants;
    private DefaultTableModel modelEtudiants;

    private CardLayout cardLayout = new CardLayout();
    private JPanel cards = new JPanel(cardLayout);

    public EnseignantView(Utilisateur prof, JFrame parentFrame) {
        setLayout(new BorderLayout());

        // --- CREATING THE MENU BAR (Beside Compte/Language) ---
        JMenuBar menuBar = new JMenuBar();

        // Navigation Menus
        JMenu menuGérer = new JMenu("Gérer les Notes");
        JMenu menuEspace = new JMenu("Espace Enseignant");

        // Keeping your existing items (simulated here)
        JMenu menuCompte = new JMenu("Compte");
        JMenu menuLanguage = new JMenu("Language");

        // Add Click listeners to the Menu Titles
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

        // Attach the menu bar to the parent JFrame
        parentFrame.setJMenuBar(menuBar);

        // --- CONTENT CARDS ---
        JPanel notesPanel = createNotesPanel();
        JPanel profPanel = createProfPanel(prof);

        cards.add(notesPanel, "NOTES");
        cards.add(profPanel, "PROFIL");
        add(cards, BorderLayout.CENTER);
    }

    private JPanel createNotesPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        // Table Etudiants
        String[] columns = {"ID", "Nom", "Prénom", "Email"};
        modelEtudiants = new DefaultTableModel(columns, 0);
        tableEtudiants = new JTable(modelEtudiants);
        loadEtudiants();

        // Clicking a student allows manipulating properties (Note DS, etc.)
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
        JPanel panel = new JPanel(new GridBagLayout()); // Centered layout
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

        int result = JOptionPane.showConfirmDialog(null, dialogPanel,
                "Saisie des Notes", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            // Logic to update student notes via your controllers
            System.out.println("Updating " + nom + " with DS: " + txtDS.getText());
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