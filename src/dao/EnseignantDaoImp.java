package dao;

import config.Connexion;
import model.Enseignant;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EnseignantDaoImp implements GenericDao<Enseignant> {
    private final Connection connection = Connexion.getConnection();

    @Override
    public void add(Enseignant entity) {
        String sql = "INSERT INTO enseignant (nom, prenom, email, specialite) VALUES (?, ?, ?, ?)";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, entity.getNom());
            ps.setString(2, entity.getPrenom());
            ps.setString(3, entity.getEmail());
            ps.setString(4, entity.getGrade());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de l'ajout de l'enseignant", e);
        }
    }

    @Override
    public Enseignant findById(int id) {
        String sql = "SELECT * FROM enseignant WHERE id = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapEnseignant(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la recherche de l'enseignant", e);
        }

        return null;
    }

    @Override
    public List<Enseignant> findAll() {
        String sql = "SELECT * FROM enseignant";
        List<Enseignant> enseignants = new ArrayList<>();

        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                enseignants.add(mapEnseignant(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors du listage des enseignants", e);
        }

        return enseignants;
    }

    @Override
    public void update(Enseignant entity) {
        String sql = "UPDATE enseignant SET nom = ?, prenom = ?, email = ?, grade = ? WHERE id = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, entity.getNom());
            ps.setString(2, entity.getPrenom());
            ps.setString(3, entity.getEmail());
            ps.setString(4, entity.getGrade());
            ps.setInt(5, entity.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la modification de l'enseignant", e);
        }
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM enseignant WHERE id = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la suppression de l'enseignant", e);
        }
    }

    private Enseignant mapEnseignant(ResultSet rs) throws SQLException {
        return new Enseignant(
                rs.getInt("id"),
                rs.getString("nom"),
                rs.getString("prenom"),
                rs.getString("email"),
                rs.getString("specialite")
        );
    }
}
