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

    public EnseignantView(Utilisateur prof) {
        setLayout(new BorderLayout());

        // Header
        JLabel lblWelcome = new JLabel("Espace Enseignant : " + prof.getUsername(), SwingConstants.CENTER);
        lblWelcome.setFont(new Font("Arial", Font.BOLD, 16));
        add(lblWelcome, BorderLayout.NORTH);

        // Table Etudiants
        String[] columns = {"ID", "Nom", "Prénom", "Email"};
        modelEtudiants = new DefaultTableModel(columns, 0);
        tableEtudiants = new JTable(modelEtudiants);
        loadEtudiants();

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBorder(BorderFactory.createTitledBorder("Liste de mes Étudiants"));
        centerPanel.add(new JScrollPane(tableEtudiants), BorderLayout.CENTER);

        add(centerPanel, BorderLayout.CENTER);
    }

    private void loadEtudiants() {
        modelEtudiants.setRowCount(0);
        List<Etudiant> list = etudiantController.findAllEtudiants();
        for (Etudiant e : list) {
            modelEtudiants.addRow(new Object[]{e.getId(), e.getNom(), e.getPrenom(), e.getEmail()});
        }
    }
}
