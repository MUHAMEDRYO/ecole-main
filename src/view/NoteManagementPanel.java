package view;

import controller.EtudiantController;
import controller.MatiereController;
import controller.NoteController;
import model.Etudiant;
import model.Matiere;
import model.Note;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class NoteManagementPanel extends JPanel {
    private NoteController noteController = new NoteController();
    private EtudiantController etudiantController = new EtudiantController();
    private MatiereController matiereController = new MatiereController();

    private JTable table;
    private DefaultTableModel tableModel;

    private JComboBox<Etudiant> cbEtudiants = new JComboBox<>();
    private JComboBox<Matiere> cbMatieres = new JComboBox<>();
    private JTextField txtNoteDS = new JTextField(5);
    private JTextField txtNoteExamen = new JTextField(5);

    private JLabel lblEtudiant, lblMatiere, lblNoteDS, lblNoteExamen;
    private JButton btnAdd, btnSave, btnDelete;
    private JPanel formPanel, btnPanel;

    public NoteManagementPanel() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(20, 150, 20, 150));

        formPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        formPanel.setBorder(BorderFactory.createTitledBorder("Saisie des Notes"));

        loadComboBoxData();

        lblEtudiant = new JLabel("Étudiant:"); formPanel.add(lblEtudiant); formPanel.add(cbEtudiants);
        lblMatiere = new JLabel("Matière:"); formPanel.add(lblMatiere); formPanel.add(cbMatieres);
        lblNoteDS = new JLabel("Note DS:"); formPanel.add(lblNoteDS); formPanel.add(txtNoteDS);
        lblNoteExamen = new JLabel("Note Examen:"); formPanel.add(lblNoteExamen); formPanel.add(txtNoteExamen);


        String[] columns = {"ID", "Étudiant", "Matière", "Note DS", "Note Examen", "Moyenne"};
        tableModel = new DefaultTableModel(columns, 0);
        table = new JTable(tableModel);
        loadNotesTable();

        // 3. Buttons Panel
        btnPanel = new JPanel();
        btnAdd = new JButton("Calculer & Ajouter");
        btnSave = new JButton("Enregistrer dans DB");
        btnDelete = new JButton("Supprimer");

        btnPanel.add(btnAdd);
        btnPanel.add(btnSave);
        btnPanel.add(btnDelete);

        // Layout Assembly
        add(formPanel, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
        add(btnPanel, BorderLayout.SOUTH);




        btnAdd.addActionListener(e -> {
            try {
                double ds = Double.parseDouble(txtNoteDS.getText());
                double exam = Double.parseDouble(txtNoteExamen.getText());
                double moy = (ds * 0.3) + (exam * 0.7); // La formule mta3ek

                Etudiant selectedEtu = (Etudiant) cbEtudiants.getSelectedItem();
                Matiere selectedMat = (Matiere) cbMatieres.getSelectedItem();

                if (selectedEtu == null || selectedMat == null) {
                    JOptionPane.showMessageDialog(this, "Veuillez choisir un etudiant et une matiere.");
                    return;
                }

                Object[] row = {"", selectedEtu.getNom(), selectedMat.getNom(), ds, exam, String.format("%.2f", moy)};
                tableModel.addRow(row);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Veuillez saisir des notes valides (ex: 15.5)");
            }
        });

        // Button: Enregistrer (Database Sync)
        btnSave.addActionListener(e -> {
            try {
                Etudiant selectedEtu = (Etudiant) cbEtudiants.getSelectedItem();
                Matiere selectedMat = (Matiere) cbMatieres.getSelectedItem();

                if (selectedEtu == null || selectedMat == null) {
                    JOptionPane.showMessageDialog(this, "Veuillez choisir un etudiant et une matiere.");
                    return;
                }

                Note n = new Note();
                n.setEtudiant(selectedEtu);
                n.setMatiere(selectedMat);
                n.setNoteDs(Double.parseDouble(txtNoteDS.getText()));
                n.setNoteExamen(Double.parseDouble(txtNoteExamen.getText()));

                noteController.addNote(n); // Hna el controller ycalculi el moyenne zeda
                JOptionPane.showMessageDialog(this, "Note enregistrée avec succès !");
                loadNotesTable();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erreur lors de l'enregistrement: " + ex.getMessage());
            }
        });

        btnDelete.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                int id = (int) tableModel.getValueAt(row, 0);
                Note n = noteController.findNoteById(id);
                noteController.deleteNote(n);
                loadNotesTable();
            }
        });
        
        translateUI("FR");
    }

    public void translateUI(String lang) {
        if (lang.equals("AR")) {
            formPanel.setBorder(BorderFactory.createTitledBorder("إدخال الأعداد"));
            lblEtudiant.setText("الطالب:");
            lblMatiere.setText("المادة:");
            lblNoteDS.setText("ملاحظة DS:");
            lblNoteExamen.setText("ملاحظة الامتحان:");
            btnAdd.setText("احسب وأضف");
            btnSave.setText("حفظ في قاعدة البيانات");
            btnDelete.setText("حذف");
            tableModel.setColumnIdentifiers(new Object[]{"المعرف", "الطالب", "المادة", "ملاحظة DS", "ملاحظة الامتحان", "المعدل"});
        } else if (lang.equals("EN")) {
            formPanel.setBorder(BorderFactory.createTitledBorder("Enter Grades"));
            lblEtudiant.setText("Student:");
            lblMatiere.setText("Subject:");
            lblNoteDS.setText("DS Grade:");
            lblNoteExamen.setText("Exam Grade:");
            btnAdd.setText("Calculate & Add");
            btnSave.setText("Save to DB");
            btnDelete.setText("Delete");
            tableModel.setColumnIdentifiers(new Object[]{"ID", "Student", "Subject", "DS Grade", "Exam Grade", "Average"});
        } else {
            formPanel.setBorder(BorderFactory.createTitledBorder("Saisie des Notes"));
            lblEtudiant.setText("Étudiant:");
            lblMatiere.setText("Matière:");
            lblNoteDS.setText("Note DS:");
            lblNoteExamen.setText("Note Examen:");
            btnAdd.setText("Calculer & Ajouter");
            btnSave.setText("Enregistrer dans DB");
            btnDelete.setText("Supprimer");
            tableModel.setColumnIdentifiers(new Object[]{"ID", "Étudiant", "Matière", "Note DS", "Note Examen", "Moyenne"});
        }
        loadNotesTable();
    }

    private void loadComboBoxData() {
        List<Etudiant> etudiants = etudiantController.findAllEtudiants();
        for (Etudiant e : etudiants) cbEtudiants.addItem(e);

        List<Matiere> matieres = matiereController.findAllMatieres();
        for (Matiere m : matieres) cbMatieres.addItem(m);
    }

    private void loadNotesTable() {
        tableModel.setRowCount(0);
        List<Note> notes = noteController.findAllNotes();
        for (Note n : notes) {
            tableModel.addRow(new Object[]{
                    n.getId(),
                    n.getEtudiant().getNom(),
                    n.getMatiere().getNom(),
                    n.getNoteDs(),
                    n.getNoteExamen(),
                    n.getMoyenne()
            });
        }
    }
}