package dao;

import config.Connexion;
import model.Enseignant;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class EnseignantDaoImp implements GenericDao<Enseignant> {
    private final Connection connection = Connexion.getConnection();

    @Override
    public void add(Enseignant entity) {
        String utilisateurSql = "INSERT INTO utilisateur (username, password, role) VALUES (?, ?, ?)";
        String enseignantSql = "INSERT INTO enseignant (nom, prenom, email, specialite, utilisateur_id) VALUES (?, ?, ?, ?, ?)";

        try {
            connection.setAutoCommit(false);

            int utilisateurId;
            try (PreparedStatement ps = connection.prepareStatement(utilisateurSql, Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, entity.getEmail());
                ps.setString(2, entity.getPassword());
                ps.setString(3, entity.getRole());
                ps.executeUpdate();

                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (!rs.next()) {
                        throw new SQLException("Aucun id utilisateur genere");
                    }
                    utilisateurId = rs.getInt(1);
                }
            }

            try (PreparedStatement ps = connection.prepareStatement(enseignantSql, Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, entity.getNom());
                ps.setString(2, entity.getPrenom());
                ps.setString(3, entity.getEmail());
                ps.setString(4, entity.getGrade());
                ps.setInt(5, utilisateurId);
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
            throw new RuntimeException("Erreur lors de l'ajout de l'enseignant", e);
        } finally {
            restoreAutoCommit();
        }
    }

    @Override
    public Enseignant findById(int id) {
        String sql = "SELECT e.*, u.username, u.password, u.role FROM enseignant e " +
                "LEFT JOIN utilisateur u ON e.utilisateur_id = u.id WHERE e.id = ?";

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
        String sql = "SELECT e.*, u.username, u.password, u.role FROM enseignant e " +
                "LEFT JOIN utilisateur u ON e.utilisateur_id = u.id";
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
        String enseignantSql = "UPDATE enseignant SET nom = ?, prenom = ?, email = ?, specialite = ? WHERE id = ?";
        String utilisateurSql = "UPDATE utilisateur u JOIN enseignant e ON e.utilisateur_id = u.id " +
                "SET u.username = ?, u.password = ?, u.role = ? WHERE e.id = ?";

        try (PreparedStatement ps = connection.prepareStatement(enseignantSql)) {
            ps.setString(1, entity.getNom());
            ps.setString(2, entity.getPrenom());
            ps.setString(3, entity.getEmail());
            ps.setString(4, entity.getGrade());
            ps.setInt(5, entity.getId());
            ps.executeUpdate();

            try (PreparedStatement userPs = connection.prepareStatement(utilisateurSql)) {
                userPs.setString(1, entity.getEmail());
                userPs.setString(2, entity.getPassword());
                userPs.setString(3, entity.getRole());
                userPs.setInt(4, entity.getId());
                userPs.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la modification de l'enseignant", e);
        }
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE u, e FROM enseignant e LEFT JOIN utilisateur u ON e.utilisateur_id = u.id WHERE e.id = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la suppression de l'enseignant", e);
        }
    }

    private Enseignant mapEnseignant(ResultSet rs) throws SQLException {
        Enseignant enseignant = new Enseignant(
                rs.getInt("id"),
                rs.getString("nom"),
                rs.getString("prenom"),
                rs.getString("email"),
                rs.getString("specialite")
        );
        enseignant.setUsername(getStringIfExists(rs, "username", enseignant.getEmail()));
        enseignant.setPassword(getStringIfExists(rs, "password", null));
        enseignant.setRole(getStringIfExists(rs, "role", "ENSEIGNANT"));
        return enseignant;
    }

    private String getStringIfExists(ResultSet rs, String column, String defaultValue) {
        try {
            String value = rs.getString(column);
            return value == null ? defaultValue : value;
        } catch (SQLException e) {
            return defaultValue;
        }
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
