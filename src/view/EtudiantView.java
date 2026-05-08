package view;

import controller.NoteController;
import model.Note;
import model.Utilisateur;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class EtudiantView extends JPanel {
    private NoteController noteController = new NoteController();
    private JTable tableNotes;
    private DefaultTableModel modelNotes;
    private final Utilisateur currentEtudiant;

    public EtudiantView(Utilisateur etudiant) {
        this.currentEtudiant = etudiant;
        setLayout(new BorderLayout());

        // Header
        JLabel lblInfo = new JLabel("Mes Notes - Étudiant: " + etudiant.getUsername(), SwingConstants.CENTER);
        lblInfo.setFont(new Font("Arial", Font.BOLD, 16));
        add(lblInfo, BorderLayout.NORTH);

        // Table Notes
        String[] columns = {"Matière", "Note DS", "Note Examen", "Moyenne"};
        modelNotes = new DefaultTableModel(columns, 0);
        tableNotes = new JTable(modelNotes);
        loadMyNotes();

        add(new JScrollPane(tableNotes), BorderLayout.CENTER);
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
}
