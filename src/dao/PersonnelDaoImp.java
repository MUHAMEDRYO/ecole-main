package dao;

import config.Connexion;
import model.Personnel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PersonnelDaoImp implements GenericDao<Personnel> {
    private final Connection connection = Connexion.getConnection();

    @Override
    public void add(Personnel entity) {
        String sql = "INSERT INTO utilisateur (username, password, role) VALUES (?, ?, 'ADMIN')";

        try (PreparedStatement ps = connection.prepareStatement(sql, java.sql.Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, getUsername(entity));
            ps.setString(2, entity.getPassword());
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    entity.setId(rs.getInt(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de l'ajout du personnel", e);
        }
    }

    @Override
    public Personnel findById(int id) {
        String sql = "SELECT * FROM utilisateur WHERE id = ? AND role = 'ADMIN'";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapPersonnel(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la recherche du personnel", e);
        }

        return null;
    }

    @Override
    public List<Personnel> findAll() {
        String sql = "SELECT * FROM utilisateur WHERE role = 'ADMIN'";
        List<Personnel> personnels = new ArrayList<>();

        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                personnels.add(mapPersonnel(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors du listage du personnel", e);
        }

        return personnels;
    }

    @Override
    public void update(Personnel entity) {
        String sql = "UPDATE utilisateur SET username = ?, password = ? WHERE id = ? AND role = 'ADMIN'";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, entity.getUsername());
            ps.setString(2, entity.getPassword());
            ps.setInt(3, entity.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la modification du personnel", e);
        }
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM utilisateur WHERE id = ? AND role = 'ADMIN'";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la suppression du personnel", e);
        }
    }

    private Personnel mapPersonnel(ResultSet rs) throws SQLException {
        Personnel personnel = new Personnel();
        personnel.setId(rs.getInt("id"));
        personnel.setUsername(rs.getString("username"));
        personnel.setPassword(rs.getString("password"));
        personnel.setRole(rs.getString("role"));
        return personnel;
    }

    private String getUsername(Personnel entity) {
        if (entity.getUsername() != null && !entity.getUsername().trim().isEmpty()) {
            return entity.getUsername();
        }
        return entity.getEmail();
    }
}
