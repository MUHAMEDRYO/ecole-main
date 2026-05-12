package view;

import controller.MatiereController;
import controller.EnseignantController;
import model.Matiere;
import model.Enseignant;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class MatiereManagementPanel extends JPanel {
    private MatiereController controller = new MatiereController();
    private EnseignantController enseignantController = new EnseignantController();
    private JTable table;
    private DefaultTableModel tableModel;

    private JTextField txtNom = new JTextField(15);
    private JComboBox<Enseignant> cbEnseignants = new JComboBox<>();

    private JLabel lblNom, lblEnseignant;
    private JButton btnAdd, btnDel, btnRefresh, btnClear;
    private JPanel formPanel, btnPanel;

    public MatiereManagementPanel() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(20, 150, 20, 150));


        String[] columns = {"ID", "Nom", "Enseignant"};
        tableModel = new DefaultTableModel(columns, 0);
        table = new JTable(tableModel);
        loadData();


        formPanel = new JPanel(new GridLayout(2, 2, 5, 5));
        formPanel.setBorder(BorderFactory.createTitledBorder("Informations Matière"));

        lblNom = new JLabel("Nom:"); formPanel.add(lblNom); formPanel.add(txtNom);
        lblEnseignant = new JLabel("Enseignant:"); formPanel.add(lblEnseignant); formPanel.add(cbEnseignants);
        loadEnseignants();

        btnPanel = new JPanel();
        btnAdd = new JButton("Ajouter");
        btnDel = new JButton("Supprimer");
        btnRefresh = new JButton("Enregistrer");
        btnClear = new JButton("Vider");

        btnPanel.add(btnAdd);
        btnPanel.add(btnDel);
        btnPanel.add(btnRefresh);
        btnPanel.add(btnClear);

        add(new JScrollPane(table), BorderLayout.CENTER);
        add(formPanel, BorderLayout.NORTH);
        add(btnPanel, BorderLayout.SOUTH);


        btnAdd.addActionListener(e -> {
            try {
                Matiere m = new Matiere();
                m.setNom(txtNom.getText());
                m.setEnseignant((Enseignant) cbEnseignants.getSelectedItem());
                controller.addMatiere(m);
                JOptionPane.showMessageDialog(this, "Matière ajoutée avec succès !");
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
                Matiere m = controller.findMatiereById(id);
                controller.deleteMatiere(m);
                loadData();
            }
        });

        btnRefresh.addActionListener(e -> {
            loadData();
            loadEnseignants();
        });

        btnClear.addActionListener(e -> clearFields());

        translateUI("FR");
    }

    private void loadEnseignants() {
        cbEnseignants.removeAllItems();
        List<Enseignant> list = enseignantController.findAllEnseignants();
        for (Enseignant e : list) {
            cbEnseignants.addItem(e);
        }
    }

    public void translateUI(String lang) {
        if (lang.equals("AR")) {
            formPanel.setBorder(BorderFactory.createTitledBorder("معلومات المادة"));
            lblNom.setText("الاسم:");
            lblEnseignant.setText("الأستاذ:");
            btnAdd.setText("إضافة");
            btnDel.setText("حذف");
            btnRefresh.setText("تحديث");
            btnClear.setText("مسح");
            tableModel.setColumnIdentifiers(new Object[]{"المعرف", "الاسم", "الأستاذ"});
        } else if (lang.equals("EN")) {
            formPanel.setBorder(BorderFactory.createTitledBorder("Subject Information"));
            lblNom.setText("Name:");
            lblEnseignant.setText("Teacher:");
            btnAdd.setText("Add");
            btnDel.setText("Delete");
            btnRefresh.setText("Refresh");
            btnClear.setText("Clear");
            tableModel.setColumnIdentifiers(new Object[]{"ID", "Name", "Teacher"});
        } else {
            formPanel.setBorder(BorderFactory.createTitledBorder("Informations Matière"));
            lblNom.setText("Nom:");
            lblEnseignant.setText("Enseignant:");
            btnAdd.setText("Ajouter");
            btnDel.setText("Supprimer");
            btnRefresh.setText("Actualiser");
            btnClear.setText("Vider");
            tableModel.setColumnIdentifiers(new Object[]{"ID", "Nom", "Enseignant"});
        }
        loadData();
    }

    private void loadData() {
        tableModel.setRowCount(0);
        List<Matiere> list = controller.findAllMatieres();
        for (Matiere m : list) {
            String profName = (m.getEnseignant() != null) ? m.getEnseignant().getNom() + " " + m.getEnseignant().getPrenom() : "N/A";
            tableModel.addRow(new Object[]{m.getId(), m.getNom(), profName});
        }
    }

    private void clearFields() {
        txtNom.setText("");
        if (cbEnseignants.getItemCount() > 0) cbEnseignants.setSelectedIndex(0);
    }
}
