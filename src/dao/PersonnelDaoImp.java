package dao;

import config.Connexion;
import model.Personnel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class PersonnelDaoImp implements GenericDao<Personnel> {
    private final Connection connection = Connexion.getConnection();

    @Override
    public void add(Personnel entity) {
        String utilisateurSql = "INSERT INTO utilisateur (username, password, role) VALUES (?, ?, 'ADMIN')";
        String personnelSql = "INSERT INTO personnel (nom, prenom, email, utilisateur_id) VALUES (?, ?, ?, ?)";

        try {
            connection.setAutoCommit(false);

            int utilisateurId;
            try (PreparedStatement ps = connection.prepareStatement(utilisateurSql, Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, getUsername(entity));
                ps.setString(2, entity.getPassword());
                ps.executeUpdate();

                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (!rs.next()) {
                        throw new SQLException("Aucun id utilisateur genere");
                    }
                    utilisateurId = rs.getInt(1);
                }
            }

            try (PreparedStatement ps = connection.prepareStatement(personnelSql, Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, entity.getNom());
                ps.setString(2, entity.getPrenom());
                ps.setString(3, entity.getEmail());
                ps.setInt(4, utilisateurId);
                ps.executeUpdate();

                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        entity.setId(rs.getInt(1));
                    }
                }
            }

            connection.commit();
        } catch (SQLException e) {
            rollback();
            throw new RuntimeException("Erreur lors de l'ajout du personnel", e);
        } finally {
            restoreAutoCommit();
        }
    }

    @Override
    public Personnel findById(int id) {
        String sql = "SELECT p.*, u.username, u.password, u.role " +
                "FROM personnel p LEFT JOIN utilisateur u ON p.utilisateur_id = u.id " +
                "WHERE p.id = ?";

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
        String sql = "SELECT p.*, u.username, u.password, u.role " +
                "FROM personnel p LEFT JOIN utilisateur u ON p.utilisateur_id = u.id";
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
        String personnelSql = "UPDATE personnel SET nom = ?, prenom = ?, email = ? WHERE id = ?";
        String utilisateurSql = "UPDATE utilisateur u JOIN personnel p ON p.utilisateur_id = u.id " +
                "SET u.username = ?, u.password = ? WHERE p.id = ?";

        try (PreparedStatement ps = connection.prepareStatement(personnelSql)) {
            ps.setString(1, entity.getNom());
            ps.setString(2, entity.getPrenom());
            ps.setString(3, entity.getEmail());
            ps.setInt(4, entity.getId());
            ps.executeUpdate();

            try (PreparedStatement userPs = connection.prepareStatement(utilisateurSql)) {
                userPs.setString(1, getUsername(entity));
                userPs.setString(2, entity.getPassword());
                userPs.setInt(3, entity.getId());
                userPs.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la modification du personnel", e);
        }
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE u, p FROM personnel p LEFT JOIN utilisateur u ON p.utilisateur_id = u.id WHERE p.id = ?";

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
        personnel.setNom(rs.getString("nom"));
        personnel.setPrenom(rs.getString("prenom"));
        personnel.setEmail(rs.getString("email"));
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

    private void rollback() {
        try {
            connection.rollback();
        } catch (SQLException ignored) {
        }
    }

    private void restoreAutoCommit() {
        try {
            connection.setAutoCommit(true);
        } catch (SQLException ignored) {
        }
    }
}
