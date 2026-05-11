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

    private JLabel lblNom, lblPrenom, lblEmail;
    private JButton btnAdd, btnEdit, btnDel, btnSave, btnClose;
    private JPanel formPanel, btnPanel;

    public EtudiantManagementPanel() {
        setOpaque(false);
        setLayout(new BorderLayout());

        setBorder(BorderFactory.createEmptyBorder(20, 150, 20, 150));
        // Table
        String[] columns = {"ID", "Nom", "Prénom", "Email"};
        tableModel = new DefaultTableModel(columns, 0);
        table = new JTable(tableModel);
        loadData();

        // Form
        formPanel = new JPanel();
        lblNom = new JLabel("Nom:"); formPanel.add(lblNom); formPanel.add(txtNom);
        lblPrenom = new JLabel("Prénom:"); formPanel.add(lblPrenom); formPanel.add(txtPrenom);
        lblEmail = new JLabel("Email:"); formPanel.add(lblEmail); formPanel.add(txtEmail);

        // Buttons [cite: 24]
        btnPanel = new JPanel();
        btnAdd = new JButton("Ajouter");
        btnEdit = new JButton("Modifier");
        btnDel = new JButton("Supprimer");
        btnSave = new JButton("Enregistrer");
        btnClose = new JButton("Fermer");

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
                Etudiant et = controller.findEtudiantById(id);
                controller.deleteEtudiant(et);
                loadData();
            }
        });

        btnClose.addActionListener(e -> System.exit(0));
        
        translateUI("FR");
    }

    public void translateUI(String lang) {
        if (lang.equals("AR")) {
            lblNom.setText("اللقب:");
            lblPrenom.setText("الاسم:");
            lblEmail.setText("البريد الإلكتروني:");
            btnAdd.setText("إضافة");
            btnEdit.setText("تعديل");
            btnDel.setText("حذف");
            btnSave.setText("حفظ");
            btnClose.setText("إغلاق");
            tableModel.setColumnIdentifiers(new Object[]{"المعرف", "اللقب", "الاسم", "البريد الإلكتروني"});
        } else if (lang.equals("EN")) {
            lblNom.setText("Last Name:");
            lblPrenom.setText("First Name:");
            lblEmail.setText("Email:");
            btnAdd.setText("Add");
            btnEdit.setText("Edit");
            btnDel.setText("Delete");
            btnSave.setText("Save");
            btnClose.setText("Close");
            tableModel.setColumnIdentifiers(new Object[]{"ID", "Last Name", "First Name", "Email"});
        } else {
            lblNom.setText("Nom:");
            lblPrenom.setText("Prénom:");
            lblEmail.setText("Email:");
            btnAdd.setText("Ajouter");
            btnEdit.setText("Modifier");
            btnDel.setText("Supprimer");
            btnSave.setText("Enregistrer");
            btnClose.setText("Fermer");
            tableModel.setColumnIdentifiers(new Object[]{"ID", "Nom", "Prénom", "Email"});
        }
        loadData();
    }

    private void loadData() {
        tableModel.setRowCount(0);
        List<Etudiant> list = controller.findAllEtudiants();
        for (Etudiant et : list) {
            tableModel.addRow(new Object[]{et.getId(), et.getNom(), et.getPrenom(), et.getEmail()});
        }
    }
}