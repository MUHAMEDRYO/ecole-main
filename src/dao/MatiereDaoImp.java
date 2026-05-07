package dao;

import config.Connexion;
import model.Matiere;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class MatiereDaoImp implements GenericDao<Matiere> {
    private final Connection connection = Connexion.getConnection();

    @Override
    public void add(Matiere entity) {
        String sql = "INSERT INTO matiere (nom) VALUES (?)";

        try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, entity.getNom());
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    entity.setId(rs.getInt(1));
                }
            }

            saveEnseignantMatiere(entity);
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de l'ajout de la matiere", e);
        }
    }

    @Override
    public Matiere findById(int id) {
        String sql = "SELECT m.*, e.id AS enseignant_id, e.nom AS enseignant_nom, e.prenom AS enseignant_prenom, " +
                "e.email AS enseignant_email, e.specialite AS enseignant_specialite " +
                "FROM matiere m " +
                "LEFT JOIN enseignant_matiere em ON em.matiere_id = m.id " +
                "LEFT JOIN enseignant e ON e.id = em.enseignant_id " +
                "WHERE m.id = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapMatiere(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la recherche de la matiere", e);
        }

        return null;
    }

    @Override
    public List<Matiere> findAll() {
        String sql = "SELECT m.*, e.id AS enseignant_id, e.nom AS enseignant_nom, e.prenom AS enseignant_prenom, " +
                "e.email AS enseignant_email, e.specialite AS enseignant_specialite " +
                "FROM matiere m " +
                "LEFT JOIN enseignant_matiere em ON em.matiere_id = m.id " +
                "LEFT JOIN enseignant e ON e.id = em.enseignant_id";
        List<Matiere> matieres = new ArrayList<>();

        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                matieres.add(mapMatiere(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors du listage des matieres", e);
        }

        return matieres;
    }

    @Override
    public void update(Matiere entity) {
        String sql = "UPDATE matiere SET nom = ? WHERE id = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, entity.getNom());
            ps.setInt(2, entity.getId());
            ps.executeUpdate();

            deleteEnseignantMatiere(entity.getId());
            saveEnseignantMatiere(entity);
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la modification de la matiere", e);
        }
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM matiere WHERE id = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la suppression de la matiere", e);
        }
    }

    private Matiere mapMatiere(ResultSet rs) throws SQLException {
        model.Enseignant enseignant = null;
        int enseignantId = rs.getInt("enseignant_id");
        if (!rs.wasNull()) {
            enseignant = new model.Enseignant(
                    enseignantId,
                    rs.getString("enseignant_nom"),
                    rs.getString("enseignant_prenom"),
                    rs.getString("enseignant_email"),
                    rs.getString("enseignant_specialite")
            );
        }

        return new Matiere(
                rs.getInt("id"),
                rs.getString("nom"),
                enseignant
        );
    }

    private void saveEnseignantMatiere(Matiere entity) throws SQLException {
        if (entity.getEnseignant() == null || entity.getEnseignant().getId() == 0 || entity.getId() == 0) {
            return;
        }

        String sql = "INSERT IGNORE INTO enseignant_matiere (enseignant_id, matiere_id) VALUES (?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, entity.getEnseignant().getId());
            ps.setInt(2, entity.getId());
            ps.executeUpdate();
        }
    }

    private void deleteEnseignantMatiere(int matiereId) throws SQLException {
        String sql = "DELETE FROM enseignant_matiere WHERE matiere_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, matiereId);
            ps.executeUpdate();
        }
    }
}
