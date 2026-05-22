package view;

import controller.EnseignantController;
import controller.MatiereController;
import model.Enseignant;
import model.Matiere;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class EnseignantManagementPanel extends JPanel {
    private EnseignantController controller = new EnseignantController();
    private MatiereController matiereController = new MatiereController();
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField txtNom = new JTextField(10);
    private JTextField txtPrenom = new JTextField(10);
    private JTextField txtEmail = new JTextField(15);
    private JComboBox<Matiere> cbSpecialite = new JComboBox<>();

    private JLabel lblNom, lblPrenom, lblEmail, lblSpecialite;
    private JButton btnAdd, btnEdit, btnDel, btnRefresh, btnClear;
    private JPanel formPanel, btnPanel;

    public EnseignantManagementPanel() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder("List of Professors"));

        String[] columns = {"ID", "Nom", "Prenom", "Email", "Specialite"};
        tableModel = new DefaultTableModel(columns, 0);
        table = new JTable(tableModel);
        loadMatieres();
        loadData();

        formPanel = new JPanel(new GridLayout(2, 6, 5, 5));


        lblNom = new JLabel("Nom:"); formPanel.add(lblNom); formPanel.add(txtNom);
        lblPrenom = new JLabel("Prenom:"); formPanel.add(lblPrenom); formPanel.add(txtPrenom);
        lblEmail = new JLabel("Email:"); formPanel.add(lblEmail); formPanel.add(txtEmail);
        lblSpecialite = new JLabel("Specialite:"); formPanel.add(lblSpecialite); formPanel.add(cbSpecialite);

        btnPanel = new JPanel();
        btnAdd = new JButton("Ajouter");
        btnEdit = new JButton("Modifier");
        btnDel = new JButton("Supprimer");
        btnRefresh = new JButton("Enregistrer");
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
                Matiere specialite = (Matiere) cbSpecialite.getSelectedItem();
                Enseignant ens = new Enseignant(0, txtNom.getText(), txtPrenom.getText(), txtEmail.getText(), specialite);
                ens.setPassword("123456");
                controller.addEnseignant(ens);
                JOptionPane.showMessageDialog(this, "Enseignant ajoute avec succes !");
                loadData();
                clearFields();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erreur: " + ex.getMessage());
            }
        });

        btnEdit.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                try {
                    int id = (int) tableModel.getValueAt(row, 0);
                    Enseignant ens = controller.findEnseignantById(id);
                    ens.setNom(txtNom.getText());
                    ens.setPrenom(txtPrenom.getText());
                    ens.setEmail(txtEmail.getText());

                    ens.setSpecialite((Matiere) cbSpecialite.getSelectedItem());
                    controller.updateEnseignant(ens);
                    JOptionPane.showMessageDialog(this, "Enseignant modifie avec succes !");
                    loadData();
                    clearFields();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Erreur: " + ex.getMessage());
                }
            } else {
                JOptionPane.showMessageDialog(this, "Selectionnez un enseignant a modifier");
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
                JOptionPane.showMessageDialog(this, "Selectionnez un enseignant a supprimer");
            }
        });

        btnRefresh.addActionListener(e -> {
            loadMatieres();
            loadData();
        });

        btnClear.addActionListener(e -> clearFields());

        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                fillFieldsFromSelectedRow();
            }
        });

        translateUI("EN");
    }

    public void translateUI(String lang) {
        if (lang.equals("AR")) {

            lblNom.setText("Nom:");
            lblPrenom.setText("Prenom:");
            lblEmail.setText("Email:");

            lblSpecialite.setText("Specialite:");
            btnAdd.setText("Ajouter");
            btnEdit.setText("Modifier");
            btnDel.setText("Supprimer");
            btnRefresh.setText("Actualiser");
            btnClear.setText("Vider");
            tableModel.setColumnIdentifiers(new Object[]{"ID", "Nom", "Prenom", "Email", "Specialite"});
        } else if (lang.equals("EN")) {

            lblNom.setText("Last Name:");
            lblPrenom.setText("First Name:");
            lblEmail.setText("Email:");

            lblSpecialite.setText("Specialty:");
            btnAdd.setText("Add");
            btnEdit.setText("Edit");
            btnDel.setText("Delete");
            btnRefresh.setText("Refresh");
            btnClear.setText("Clear");
            tableModel.setColumnIdentifiers(new Object[]{"ID", "Last Name", "First Name", "Email", "Specialty"});
        } else {

            lblNom.setText("Nom:");
            lblPrenom.setText("Prenom:");
            lblEmail.setText("Email:");

            lblSpecialite.setText("Specialite:");
            btnAdd.setText("Ajouter");
            btnEdit.setText("Modifier");
            btnDel.setText("Supprimer");
            btnRefresh.setText("Actualiser");
            btnClear.setText("Vider");
            tableModel.setColumnIdentifiers(new Object[]{"ID", "Nom", "Prenom", "Email", "Specialite"});
        }
        loadData();
    }

    private void loadMatieres() {
        cbSpecialite.removeAllItems();
        List<Matiere> matieres = matiereController.findAllMatieres();
        for (Matiere m : matieres) {
            cbSpecialite.addItem(m);
        }
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
                    e.getSpecialite() == null ? "No Specialty" : e.getSpecialite().getNom()
            });
        }
    }

    private void fillFieldsFromSelectedRow() {

        int row = table.getSelectedRow();
        if (row == -1) {
            return;
        }

        int id = (int) tableModel.getValueAt(row, 0);
        Enseignant enseignant = controller.findEnseignantById(id);
        if (enseignant == null) {
            return;
        }

        txtNom.setText(enseignant.getNom());
        txtPrenom.setText(enseignant.getPrenom());
        txtEmail.setText(enseignant.getEmail());
        selectSpecialite(enseignant.getSpecialite());
    }

    private void selectSpecialite(Matiere specialite) {
        if (specialite == null) {
            cbSpecialite.setSelectedIndex(-1);
            return;
        }

        for (int i = 0; i < cbSpecialite.getItemCount(); i++) {
            Matiere item = cbSpecialite.getItemAt(i);
            if (item.getId() == specialite.getId()) {
                cbSpecialite.setSelectedIndex(i);
                return;
            }
        }
    }

    private void clearFields() {
        txtNom.setText("");
        txtPrenom.setText("");
        txtEmail.setText("");
        if (cbSpecialite.getItemCount() > 0) {
            cbSpecialite.setSelectedIndex(0);
        }
    }
}
