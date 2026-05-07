package controller;

import dao.NoteDaoImp;
import model.Note;

import java.util.List;

public class NoteController implements Validation<Note> {
    private final NoteDaoImp dao = new NoteDaoImp();

    public void addNote(Note note) {
        validateNote(note);
        note.calculerMoyenne();
        dao.add(note);
    }

    public Note findNoteById(int id) {
        validateId(id);
        return dao.findById(id);
    }

    public List<Note> findAllNotes() {
        return dao.findAll();
    }

    public void updateNote(Note note) {
        validateNote(note);
        validateId(note.getId());
        note.calculerMoyenne();
        dao.update(note);
    }

    public void deleteNote(Note note) {
        if (note == null) {
            throw new IllegalArgumentException("Note invalide");
        }
        deleteNote(note.getId());
    }

    public void deleteNote(int id) {
        validateId(id);
        dao.delete(id);
    }

    private void validateNote(Note note) {
        if (note == null) {
            throw new IllegalArgumentException("Note invalide");
        }
        if (note.getEtudiant() == null || note.getEtudiant().getId() <= 0) {
            throw new IllegalArgumentException("Etudiant obligatoire");
        }
        if (note.getMatiere() == null || note.getMatiere().getId() <= 0) {
            throw new IllegalArgumentException("Matiere obligatoire");
        }
        validate(note);
    }
    @Override
    public void validate(Note note) {
        if (note.getNoteDs() < 0 || note.getNoteDs() > 20) {
            throw new IllegalArgumentException("La note DS doit etre entre 0 et 20");
        }
        if (note.getNoteExamen() < 0 || note.getNoteExamen() > 20) {
            throw new IllegalArgumentException("La note examen doit etre entre 0 et 20");
        }
    }
    @Override
    public void validateId(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("Id invalide");
        }
    }
}
