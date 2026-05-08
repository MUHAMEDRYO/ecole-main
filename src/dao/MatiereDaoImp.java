package dao;

import config.Connexion;
import model.Matiere;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MatiereDaoImp implements GenericDao<Matiere> {
    private final Connection connection = Connexion.getConnection();

    @Override
    public void add(Matiere entity) {
        String sql = "INSERT INTO matiere (nom) VALUES (?)";

        try (PreparedStatement ps = connection.prepareStatement(sql, java.sql.Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, entity.getNom());
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    entity.setId(rs.getInt(1));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de l'ajout de la matiere", e);
        }
    }

    @Override
    public Matiere findById(int id) {
        String sql = "SELECT * FROM matiere WHERE id = ?";

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
        String sql = "SELECT * FROM matiere";
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
        return new Matiere(
                rs.getInt("id"),
                rs.getString("nom"),
                null
            );
    }
}
