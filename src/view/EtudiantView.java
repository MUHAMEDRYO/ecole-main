package view;

import controller.MatiereController;
import controller.NoteController;
import model.Matiere;
import model.Note;
import model.Utilisateur;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.Font;
import java.util.List;

public class EtudiantView extends JPanel {
    private NoteController noteController = new NoteController();
    private JTable tableNotes;
    private DefaultTableModel modelNotes;
    private final Utilisateur currentEtudiant;

    private JLabel lblInfo;
    private JScrollPane scrollPane;

    public EtudiantView(Utilisateur etudiant) {
        this.currentEtudiant = etudiant;
        setLayout(new BorderLayout());

        // Header
        JPanel topPanel = new JPanel(new BorderLayout());
        lblInfo = new JLabel("", SwingConstants.CENTER);
        lblInfo.setFont(new Font("Arial", Font.BOLD, 16));
        topPanel.add(lblInfo, BorderLayout.CENTER);

        add(topPanel, BorderLayout.NORTH);

        // Table Notes
        String[] columns = {"Matière", "Note DS", "Note Examen", "Moyenne"};
        modelNotes = new DefaultTableModel(columns, 0);
        tableNotes = new JTable(modelNotes);
        loadMyNotes();

        scrollPane = new JScrollPane(tableNotes);
        add(scrollPane, BorderLayout.CENTER);
        
        translateUI("FR"); // Default
    }

    public void translateUI(String lang) {
        if (lang.equals("AR")) {
            lblInfo.setText("ملاحظاتي - الطالب: " + currentEtudiant.getUsername());
            modelNotes.setColumnIdentifiers(new Object[]{"المادة", "ملاحظة DS", "ملاحظة الامتحان", "المعدل"});
        } else if (lang.equals("EN")) {
            lblInfo.setText("My Grades - Student: " + currentEtudiant.getUsername());
            modelNotes.setColumnIdentifiers(new Object[]{"Subject", "DS Grade", "Exam Grade", "Average"});
        } else {
            lblInfo.setText("Mes Notes - Étudiant: " + currentEtudiant.getUsername());
            modelNotes.setColumnIdentifiers(new Object[]{"Matière", "Note DS", "Note Examen", "Moyenne"});
        }
        loadMyNotes();
    }

    private void loadMyNotes() {
        modelNotes.setRowCount(0);
        List<Note> notes = noteController.findAllNotes();
        for (Note n : notes) {
            if (n.getEtudiant() == null || currentEtudiant.getUsername() == null || !currentEtudiant.getUsername().equals(n.getEtudiant().getUsername())) {
                continue;
            }
            modelNotes.addRow(new Object[]{
                    n.getMatiere().getNom(),
                    n.getNoteDs(),
                    n.getNoteExamen(),
                    String.format("%.2f", n.getMoyenne())
            });
        }
    }
    private void showMatières() {
        MatiereController matiereController = new MatiereController();
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
