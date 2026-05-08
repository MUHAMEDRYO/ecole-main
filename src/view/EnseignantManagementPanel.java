package view;

import controller.EnseignantController;
import model.Enseignant;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class EnseignantManagementPanel extends JPanel {
    private EnseignantController controller = new EnseignantController();
    private JTable table;
    private DefaultTableModel tableModel;

    // Form fields
    private JTextField txtNom = new JTextField(10);
    private JTextField txtPrenom = new JTextField(10);
    private JTextField txtEmail = new JTextField(15);
    private JTextField txtGrade = new JTextField(10);
    private JTextField txtSpecialite = new JTextField(10);

    public EnseignantManagementPanel() {
        setLayout(new BorderLayout());

        // 1. Table Setup
        String[] columns = {"ID", "Nom", "Prénom", "Email", "Grade", "Spécialité"};
        tableModel = new DefaultTableModel(columns, 0);
        table = new JTable(tableModel);
        loadData();

        // 2. Form Panel
        JPanel formPanel = new JPanel(new GridLayout(2, 6, 5, 5));
        formPanel.setBorder(BorderFactory.createTitledBorder("Informations Enseignant"));

        formPanel.add(new JLabel("Nom:")); formPanel.add(txtNom);
        formPanel.add(new JLabel("Prénom:")); formPanel.add(txtPrenom);
        formPanel.add(new JLabel("Email:")); formPanel.add(txtEmail);
        formPanel.add(new JLabel("Grade:")); formPanel.add(txtGrade);
        formPanel.add(new JLabel("Spécialité:")); formPanel.add(txtSpecialite);

        // 3. Buttons Panel
        JPanel btnPanel = new JPanel();
        JButton btnAdd = new JButton("Ajouter");
        JButton btnEdit = new JButton("Modifier");
        JButton btnDel = new JButton("Supprimer");
        JButton btnSave = new JButton("Enregistrer");
        JButton btnClear = new JButton("Vider");

        btnPanel.add(btnAdd);
        btnPanel.add(btnEdit);
        btnPanel.add(btnDel);
        btnPanel.add(btnSave);
        btnPanel.add(btnClear);

        // 4. Layout Assembly
        add(new JScrollPane(table), BorderLayout.CENTER);
        add(formPanel, BorderLayout.NORTH);
        add(btnPanel, BorderLayout.SOUTH);

        // --- Action Listeners ---

        // Ajouter (Localement dans la table)
        btnAdd.addActionListener(e -> {
            Object[] row = {
                    "", // ID empty for now
                    txtNom.getText(),
                    txtPrenom.getText(),
                    txtEmail.getText(),
                    txtGrade.getText(),
                    txtSpecialite.getText()
            };
            tableModel.addRow(row);
        });

        // Supprimer
        btnDel.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                Object idObj = tableModel.getValueAt(row, 0);
                if (idObj != null && !idObj.toString().isEmpty()) {
                    int id = (int) idObj;
                    controller.deleteEnseignant(controller.findEnseignantById(id));
                }
                tableModel.removeRow(row);
            } else {
                JOptionPane.showMessageDialog(this, "Sélectionnez un enseignant à supprimer");
            }
        });

        // Enregistrer (Sync with Database)
        btnSave.addActionListener(e -> {
            try {
                Enseignant ens = new Enseignant();
                ens.setNom(txtNom.getText());
                ens.setPrenom(txtPrenom.getText());
                ens.setEmail(txtEmail.getText());
                ens.setGrade(txtGrade.getText());
                ens.setPassword("123456"); // Password par défaut

                // Note: Specialite is handled via specific setter if not in constructor
                // ens.setSpecialite(txtSpecialite.getText());

                controller.addEnseignant(ens);
                JOptionPane.showMessageDialog(this, "Enseignant enregistré avec succès !");
                loadData();
                clearFields();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erreur: " + ex.getMessage());
            }
        });

        // Vider les champs
        btnClear.addActionListener(e -> clearFields());
    }

    private void loadData() {
        tableModel.setRowCount(0);
        List<Enseignant> list = controller.findAllEnseignants();
        for (Enseignant e : list) {
            tableModel.addRow(new Object[]{
                    e.getId(),
                    e.getNom(),
                    e.getPrenom(),
                    e.getEmail(),
                    e.getGrade(),
                    "Informatique" // Placeholder for specialty
            });
        }
    }

    private void clearFields() {
        txtNom.setText("");
        txtPrenom.setText("");
        txtEmail.setText("");
        txtGrade.setText("");
        txtSpecialite.setText("");
    }
}