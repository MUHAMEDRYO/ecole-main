package dao;

import config.Connexion;
import model.Etudiant;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class EtudiantDaoImp implements GenericDao<Etudiant> {
    private final Connection connection = Connexion.getConnection();

    @Override
    public void add(Etudiant entity) {
        String utilisateurSql = "INSERT INTO utilisateur (username, password, role) VALUES (?, ?, ?)";
        String etudiantSql = "INSERT INTO etudiant (nom, prenom, email, utilisateur_id) VALUES (?, ?, ?, ?)";

        try {
            connection.setAutoCommit(false);

            // --- FIX: Thabbet fil password wel role 9bal el insertion ---
            String password = (entity.getPassword() != null && !entity.getPassword().isEmpty())
                    ? entity.getPassword() : "123456";
            String role = (entity.getRole() != null) ? entity.getRole() : "ETUDIANT";
            String username = (entity.getUsername() != null && !entity.getUsername().isEmpty())
                    ? entity.getUsername() : entity.getEmail();

            int utilisateurId;
            try (PreparedStatement ps = connection.prepareStatement(utilisateurSql, Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, username);
                ps.setString(2, password);
                ps.setString(3, role);
                ps.executeUpdate();

                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (!rs.next()) {
                        throw new SQLException("Aucun id utilisateur genere");
                    }
                    utilisateurId = rs.getInt(1);
                }
            }

            try (PreparedStatement ps = connection.prepareStatement(etudiantSql, Statement.RETURN_GENERATED_KEYS)) {
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
            throw new RuntimeException("Erreur lors de l'ajout de l'etudiant", e);
        } finally {
            restoreAutoCommit();
        }
    }

    @Override
    public Etudiant findById(int id) {
        String sql = "SELECT e.*, u.username, u.password, u.role FROM etudiant e " +
                "LEFT JOIN utilisateur u ON e.utilisateur_id = u.id WHERE e.id = ?";

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
        String sql = "SELECT e.*, u.username, u.password, u.role FROM etudiant e " +
                "LEFT JOIN utilisateur u ON e.utilisateur_id = u.id";
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
        String etudiantSql = "UPDATE etudiant SET nom = ?, prenom = ?, email = ? WHERE id = ?";
        String utilisateurSql = "UPDATE utilisateur u JOIN etudiant e ON e.utilisateur_id = u.id " +
                "SET u.username = ?, u.password = ?, u.role = ? WHERE e.id = ?";

        try {
            connection.setAutoCommit(false);
            try (PreparedStatement ps = connection.prepareStatement(etudiantSql)) {
                ps.setString(1, entity.getNom());
                ps.setString(2, entity.getPrenom());
                ps.setString(3, entity.getEmail());
                ps.setInt(4, entity.getId());
                ps.executeUpdate();
            }

            try (PreparedStatement userPs = connection.prepareStatement(utilisateurSql)) {
                userPs.setString(1, entity.getEmail());
                userPs.setString(2, entity.getPassword());
                userPs.setString(3, entity.getRole());
                userPs.setInt(4, entity.getId());
                userPs.executeUpdate();
            }
            connection.commit();
        } catch (SQLException e) {
            rollback();
            throw new RuntimeException("Erreur lors de la modification de l'etudiant", e);
        } finally {
            restoreAutoCommit();
        }
    }

    @Override
    public void delete(int id) {
        // Suppression cascade grâce à la FK dans le SQL, mais on supprime l'utilisateur d'abord
        String findUserSql = "SELECT utilisateur_id FROM etudiant WHERE id = ?";
        String deleteUserSql = "DELETE FROM utilisateur WHERE id = ?";

        try {
            int userId = -1;
            try (PreparedStatement ps = connection.prepareStatement(findUserSql)) {
                ps.setInt(1, id);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) userId = rs.getInt("utilisateur_id");
            }

            if (userId != -1) {
                try (PreparedStatement ps = connection.prepareStatement(deleteUserSql)) {
                    ps.setInt(1, userId);
                    ps.executeUpdate();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la suppression", e);
        }
    }

    private Etudiant mapEtudiant(ResultSet rs) throws SQLException {
        Etudiant etudiant = new Etudiant(
                rs.getInt("id"),
                rs.getString("nom"),
                rs.getString("prenom"),
                rs.getString("email")
        );
        etudiant.setUsername(rs.getString("username"));
        etudiant.setPassword(rs.getString("password"));
        etudiant.setRole(rs.getString("role"));
        return etudiant;
    }

    private void rollback() {
        try {
            connection.rollback();
        } catch (SQLException ignored) {}
    }

    private void restoreAutoCommit() {
        try {
            connection.setAutoCommit(true);
        } catch (SQLException ignored) {}
    }
}