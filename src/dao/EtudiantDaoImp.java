package dao;

import config.Connexion;
import model.Etudiant;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EtudiantDaoImp implements GenericDao<Etudiant> {
    private final Connection connection = Connexion.getConnection();

    @Override
    public void add(Etudiant entity) {
        String sql = "INSERT INTO etudiant (nom, prenom, email) VALUES (?, ?, ?)";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, entity.getNom());
            ps.setString(2, entity.getPrenom());
            ps.setString(3, entity.getEmail());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de l'ajout de l'etudiant", e);
        }
    }

    @Override
    public Etudiant findById(int id) {
        String sql = "SELECT * FROM etudiant WHERE id = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapEtudiant(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la recherche de l'etudiant", e);
        }

        return null;
    }

    @Override
    public List<Etudiant> findAll() {
        String sql = "SELECT * FROM etudiant";
        List<Etudiant> etudiants = new ArrayList<>();

        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                etudiants.add(mapEtudiant(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors du listage des etudiants", e);
        }

        return etudiants;
    }

    @Override
    public void update(Etudiant entity) {
        String sql = "UPDATE etudiant SET nom = ?, prenom = ?, email = ? WHERE id = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, entity.getNom());
            ps.setString(2, entity.getPrenom());
            ps.setString(3, entity.getEmail());
            ps.setInt(4, entity.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la modification de l'etudiant", e);
        }
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM etudiant WHERE id = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la suppression de l'etudiant", e);
        }
    }

    private Etudiant mapEtudiant(ResultSet rs) throws SQLException {
        return new Etudiant(
                rs.getInt("id"),
                rs.getString("nom"),
                rs.getString("prenom"),
                rs.getString("email")
        );
    }
}
