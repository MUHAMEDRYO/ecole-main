package view;

import controller.EtudiantController;
import model.Etudiant;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class EtudiantManagementPanel extends JPanel {
    private EtudiantController controller = new EtudiantController();
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField txtNom = new JTextField(10), txtPrenom = new JTextField(10), txtEmail = new JTextField(15);

    public EtudiantManagementPanel() {
        setLayout(new BorderLayout());

        // Table
        String[] columns = {"ID", "Nom", "Prénom", "Email"};
        tableModel = new DefaultTableModel(columns, 0);
        table = new JTable(tableModel);
        loadData();

        // Form
        JPanel formPanel = new JPanel();
        formPanel.add(new JLabel("Nom:")); formPanel.add(txtNom);
        formPanel.add(new JLabel("Prénom:")); formPanel.add(txtPrenom);
        formPanel.add(new JLabel("Email:")); formPanel.add(txtEmail);

        // Buttons [cite: 24]
        JPanel btnPanel = new JPanel();
        JButton btnAdd = new JButton("Ajouter");
        JButton btnEdit = new JButton("Modifier");
        JButton btnDel = new JButton("Supprimer");
        JButton btnSave = new JButton("Enregistrer");
        JButton btnClose = new JButton("Fermer");

        btnPanel.add(btnAdd); btnPanel.add(btnEdit); btnPanel.add(btnDel);
        btnPanel.add(btnSave); btnPanel.add(btnClose);

        add(new JScrollPane(table), BorderLayout.CENTER);
        add(formPanel, BorderLayout.NORTH);
        add(btnPanel, BorderLayout.SOUTH);

        // Action Listeners [cite: 25, 26, 27, 28]
        btnAdd.addActionListener(e -> {
            Etudiant et = new Etudiant(txtNom.getText(), txtPrenom.getText(), txtEmail.getText());
            controller.addEtudiant(et);
            loadData();
        });

        btnDel.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                int id = (int) tableModel.getValueAt(row, 0);
                controller.deleteEtudiant(id);
                loadData();
            }
        });

        btnClose.addActionListener(e -> System.exit(0));
    }

    private void loadData() {
        tableModel.setRowCount(0);
        List<Etudiant> list = controller.findAllEtudiants();
        for (Etudiant et : list) {
            tableModel.addRow(new Object[]{et.getId(), et.getNom(), et.getPrenom(), et.getEmail()});
        }
    }
}