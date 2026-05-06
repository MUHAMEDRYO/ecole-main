package dao;

import config.Connexion;
import model.Etudiant;
import model.Matiere;
import model.Note;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class NoteDaoImp implements GenericDao<Note> {
    private final Connection connection = Connexion.getConnection();

    @Override
    public void add(Note entity) {
        String sql = "INSERT INTO note (etudiant_id, matiere_id, note_ds, note_exam, note_finale) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, entity.getEtudiant().getId());
            ps.setInt(2, entity.getMatiere().getId());
            ps.setDouble(3, entity.getNoteDs());
            ps.setDouble(4, entity.getNoteExamen());
            ps.setDouble(5, entity.getMoyenne());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de l'ajout de la note", e);
        }
    }

    @Override
    public Note findById(int id) {
        String sql = "SELECT * FROM note WHERE id = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapNote(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la recherche de la note", e);
        }

        return null;
    }

    @Override
    public List<Note> findAll() {
        String sql = "SELECT * FROM note";
        List<Note> notes = new ArrayList<>();

        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                notes.add(mapNote(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors du listage des notes", e);
        }

        return notes;
    }

    @Override
    public void update(Note entity) {
        String sql = "UPDATE note SET etudiant_id = ?, matiere_id = ?, note_ds = ?, note_exam = ?, note_finale = ? WHERE id = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, entity.getEtudiant().getId());
            ps.setInt(2, entity.getMatiere().getId());
            ps.setDouble(3, entity.getNoteDs());
            ps.setDouble(4, entity.getNoteExamen());
            ps.setDouble(5, entity.getMoyenne());
            ps.setInt(6, entity.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la modification de la note", e);
        }
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM note WHERE id = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la suppression de la note", e);
        }
    }

    private Note mapNote(ResultSet rs) throws SQLException {
        Etudiant etudiant = new Etudiant();
        etudiant.setId(rs.getInt("etudiant_id"));

        Matiere matiere = new Matiere();
        matiere.setId(rs.getInt("matiere_id"));

        return new Note(
                rs.getInt("id"),
                rs.getDouble("note_ds"),
                rs.getDouble("note_exam"),
                rs.getDouble("note_finale"),
                etudiant,
                matiere,
                null
        );
    }
}
