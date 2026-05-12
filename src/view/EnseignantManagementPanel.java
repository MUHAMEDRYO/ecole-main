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
    private JTextField txtNom = new JTextField(10);
    private JTextField txtPrenom = new JTextField(10);
    private JTextField txtEmail = new JTextField(15);
    private JTextField txtGrade = new JTextField(10);
    private JTextField txtSpecialite = new JTextField(10);

    private JLabel lblNom, lblPrenom, lblEmail, lblGrade, lblSpecialite;
    private JButton btnAdd, btnEdit, btnDel, btnRefresh, btnClear;
    private JPanel formPanel, btnPanel;

    public EnseignantManagementPanel() {
        setLayout(new BorderLayout());

        setBorder(BorderFactory.createEmptyBorder(20, 150, 20, 150));

        String[] columns = {"ID", "Nom", "Prénom", "Email", "Grade", "Spécialité"};
        tableModel = new DefaultTableModel(columns, 0);
        table = new JTable(tableModel);
        loadData();

        formPanel = new JPanel(new GridLayout(2, 6, 5, 5));
        formPanel.setBorder(BorderFactory.createTitledBorder("Informations Enseignant"));

        lblNom = new JLabel("Nom:"); formPanel.add(lblNom); formPanel.add(txtNom);
        lblPrenom = new JLabel("Prénom:"); formPanel.add(lblPrenom); formPanel.add(txtPrenom);
        lblEmail = new JLabel("Email:"); formPanel.add(lblEmail); formPanel.add(txtEmail);
        lblGrade = new JLabel("Grade:"); formPanel.add(lblGrade); formPanel.add(txtGrade);
        lblSpecialite = new JLabel("Spécialité:"); formPanel.add(lblSpecialite); formPanel.add(txtSpecialite);

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

        add(new JScrollPane(table), BorderLayout.CENTER);
        add(formPanel, BorderLayout.NORTH);
        add(btnPanel, BorderLayout.SOUTH);

        btnAdd.addActionListener(e -> {
            try {
                Enseignant ens = new Enseignant();
                ens.setNom(txtNom.getText());
                ens.setPrenom(txtPrenom.getText());
                ens.setEmail(txtEmail.getText());
                ens.setGrade(txtGrade.getText());
                ens.setPassword("123456"); 
                controller.addEnseignant(ens);
                JOptionPane.showMessageDialog(this, "Enseignant ajouté avec succès !");
                loadData();
                clearFields();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erreur: " + ex.getMessage());
            }
        });

        btnDel.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                int id = (int) tableModel.getValueAt(row, 0);
                Enseignant ens = controller.findEnseignantById(id);
                controller.deleteEnseignant(ens);
                loadData();
            } else {
                JOptionPane.showMessageDialog(this, "Sélectionnez un enseignant à supprimer");
            }
        });

        btnRefresh.addActionListener(e -> loadData());

        btnClear.addActionListener(e -> clearFields());
        
        translateUI("FR");
    }

    public void translateUI(String lang) {
        if (lang.equals("AR")) {
            formPanel.setBorder(BorderFactory.createTitledBorder("معلومات الأستاذ"));
            lblNom.setText("اللقب:");
            lblPrenom.setText("الاسم:");
            lblEmail.setText("البريد الإلكتروني:");
            lblGrade.setText("الرتبة:");
            lblSpecialite.setText("التخصص:");
            btnAdd.setText("إضافة");
            btnEdit.setText("تعديل");
            btnDel.setText("حذف");
            btnRefresh.setText("تحديث");
            btnClear.setText("مسح");
            tableModel.setColumnIdentifiers(new Object[]{"المعرف", "اللقب", "الاسم", "البريد الإلكتروني", "الرتبة", "التخصص"});
        } else if (lang.equals("EN")) {
            formPanel.setBorder(BorderFactory.createTitledBorder("Teacher Information"));
            lblNom.setText("Last Name:");
            lblPrenom.setText("First Name:");
            lblEmail.setText("Email:");
            lblGrade.setText("Grade:");
            lblSpecialite.setText("Specialty:");
            btnAdd.setText("Add");
            btnEdit.setText("Edit");
            btnDel.setText("Delete");
            btnRefresh.setText("Refresh");
            btnClear.setText("Clear");
            tableModel.setColumnIdentifiers(new Object[]{"ID", "Last Name", "First Name", "Email", "Grade", "Specialty"});
        } else {
            formPanel.setBorder(BorderFactory.createTitledBorder("Informations Enseignant"));
            lblNom.setText("Nom:");
            lblPrenom.setText("Prénom:");
            lblEmail.setText("Email:");
            lblGrade.setText("Grade:");
            lblSpecialite.setText("Spécialité:");
            btnAdd.setText("Ajouter");
            btnEdit.setText("Modifier");
            btnDel.setText("Supprimer");
            btnRefresh.setText("Actualiser");
            btnClear.setText("Vider");
            tableModel.setColumnIdentifiers(new Object[]{"ID", "Nom", "Prénom", "Email", "Grade", "Spécialité"});
        }
        loadData();
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