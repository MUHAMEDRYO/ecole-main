package view;

import controller.MatiereController;
import model.Matiere;
import model.Utilisateur;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.List;

public class PersonnelView extends JPanel {
    private MatiereController matiereController = new MatiereController();
    private final Utilisateur currentPersonnel;

    public PersonnelView(Utilisateur personnel) {
        this.currentPersonnel = personnel;
        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel(new BorderLayout());
        JLabel lblInfo = new JLabel("Espace Personnel - " + personnel.getUsername(), SwingConstants.CENTER);
        lblInfo.setFont(new Font("Arial", Font.BOLD, 16));
        topPanel.add(lblInfo, BorderLayout.CENTER);

        JButton btnMatiere = new JButton("Matières");
        btnMatiere.addActionListener(e -> showMatières());
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(btnMatiere);
        topPanel.add(buttonPanel, BorderLayout.EAST);

        add(topPanel, BorderLayout.NORTH);

        JPanel infoPanel = new JPanel(new GridBagLayout());
        infoPanel.setBorder(BorderFactory.createTitledBorder("Mes Informations"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(10, 10, 10, 10);
        
        infoPanel.add(new JLabel("Nom d'utilisateur: " + personnel.getUsername()), gbc);
        gbc.gridy++;
        infoPanel.add(new JLabel("Rôle: " + personnel.getRole()), gbc);

        add(infoPanel, BorderLayout.CENTER);
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
}

