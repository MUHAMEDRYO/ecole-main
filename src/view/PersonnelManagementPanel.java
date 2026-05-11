package view;

import controller.PersonnelController;

import model.Personnel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class PersonnelManagementPanel extends JPanel {
    private PersonnelController controller = new PersonnelController();
    private JTable table;
    private DefaultTableModel tableModel;

    // Form fields
    private JTextField txtNom = new JTextField(10);
    private JTextField txtPrenom = new JTextField(10);
    private JTextField txtEmail = new JTextField(15);


    private JLabel lblNom, lblPrenom, lblEmail;
    private JButton btnAdd, btnEdit, btnDel, btnRefresh, btnClear;
    private JPanel formPanel, btnPanel;

    public PersonnelManagementPanel() {
        setLayout(new BorderLayout());

        setBorder(BorderFactory.createEmptyBorder(20, 150, 20, 150));

        // 1. Table Setup
        String[] columns = {"ID", "Nom", "Prénom", "Email"};
        tableModel = new DefaultTableModel(columns, 0);
        table = new JTable(tableModel);
        loadData();

        // 2. Form Panel
        formPanel = new JPanel(new GridLayout(2, 6, 5, 5));
        formPanel.setBorder(BorderFactory.createTitledBorder("Informations Personnel"));

        lblNom = new JLabel("Nom:"); formPanel.add(lblNom); formPanel.add(txtNom);
        lblPrenom = new JLabel("Prénom:"); formPanel.add(lblPrenom); formPanel.add(txtPrenom);
        lblEmail = new JLabel("Email:"); formPanel.add(lblEmail); formPanel.add(txtEmail);


        // 3. Buttons Panel
        btnPanel = new JPanel();
        btnAdd = new JButton("Ajouter");
        btnEdit = new JButton("Modifier");
        btnDel = new JButton("Supprimer");
        btnRefresh = new JButton("Actualiser");
        btnClear = new JButton("Vider");

        btnPanel.add(btnAdd);
        btnPanel.add(btnEdit);
        btnPanel.add(btnDel);
        btnPanel.add(btnRefresh);
        btnPanel.add(btnClear);

        // 4. Layout Assembly
        add(new JScrollPane(table), BorderLayout.CENTER);
        add(formPanel, BorderLayout.NORTH);
        add(btnPanel, BorderLayout.SOUTH);

        // --- Action Listeners ---

        // Ajouter (Directement en DB pour avoir l'ID)
        btnAdd.addActionListener(e -> {
            try {
                Personnel ens = new Personnel();
                ens.setNom(txtNom.getText());
                ens.setPrenom(txtPrenom.getText());
                ens.setEmail(txtEmail.getText());
                ens.setPassword("123456"); 
                controller.addPersonnel(ens);
                JOptionPane.showMessageDialog(this, "Personnel ajouté avec succès !");
                loadData();
                clearFields();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erreur: " + ex.getMessage());
            }
        });

        // Supprimer
        btnDel.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                int id = (int) tableModel.getValueAt(row, 0);
                controller.deletePersonnel(id);
                loadData();
            } else {
                JOptionPane.showMessageDialog(this, "Sélectionnez un personnel à supprimer");
            }
        });

        // Actualiser
        btnRefresh.addActionListener(e -> loadData());

        // Vider les champs
        btnClear.addActionListener(e -> clearFields());
        
        translateUI("FR");
    }

    public void translateUI(String lang) {
        if (lang.equals("AR")) {
            formPanel.setBorder(BorderFactory.createTitledBorder("معلومات الموظفين"));
            lblNom.setText("اللقب:");
            lblPrenom.setText("الاسم:");
            lblEmail.setText("البريد الإلكتروني:");
            btnAdd.setText("إضافة");
            btnEdit.setText("تعديل");
            btnDel.setText("حذف");
            btnRefresh.setText("تحديث");
            btnClear.setText("مسح");
            tableModel.setColumnIdentifiers(new Object[]{"المعرف", "اللقب", "الاسم", "البريد الإلكتروني"});
        } else if (lang.equals("EN")) {
            formPanel.setBorder(BorderFactory.createTitledBorder("Staff Information"));
            lblNom.setText("Last Name:");
            lblPrenom.setText("First Name:");
            lblEmail.setText("Email:");
            btnAdd.setText("Add");
            btnEdit.setText("Edit");
            btnDel.setText("Delete");
            btnRefresh.setText("Refresh");
            btnClear.setText("Clear");
            tableModel.setColumnIdentifiers(new Object[]{"ID", "Last Name", "First Name", "Email"});
        } else {
            formPanel.setBorder(BorderFactory.createTitledBorder("Informations Personnel"));
            lblNom.setText("Nom:");
            lblPrenom.setText("Prénom:");
            lblEmail.setText("Email:");
            btnAdd.setText("Ajouter");
            btnEdit.setText("Modifier");
            btnDel.setText("Supprimer");
            btnRefresh.setText("Actualiser");
            btnClear.setText("Vider");
            tableModel.setColumnIdentifiers(new Object[]{"ID", "Nom", "Prénom", "Email"});
        }
        loadData();
    }

    private void loadData() {
        tableModel.setRowCount(0);
        List<Personnel> list = controller.findAllPersonnel();
        for (Personnel e : list) {
            tableModel.addRow(new Object[]{
                    e.getId(),
                    e.getNom(),
                    e.getPrenom(),
                    e.getEmail(),

            });
        }
    }

    private void clearFields() {
        txtNom.setText("");
        txtPrenom.setText("");
        txtEmail.setText("");

    }
}

