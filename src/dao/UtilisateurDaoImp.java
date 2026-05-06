package dao;

import config.Connexion;
import model.Utilisateur;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UtilisateurDaoImp implements GenericDao<Utilisateur> {
    private final Connection connection = Connexion.getConnection();

    @Override
    public void add(Utilisateur entity) {
        String sql = "INSERT INTO utilisateur (username, password, role) VALUES (?, ?, 'ETUDIANT')";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, entity.getEmail());
            ps.setString(2, entity.getNom());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de l'ajout de l'utilisateur", e);
        }
    }

    @Override
    public Utilisateur findById(int id) {
        String sql = "SELECT * FROM utilisateur WHERE id = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapUtilisateur(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la recherche de l'utilisateur", e);
        }

        return null;
    }

    @Override
    public List<Utilisateur> findAll() {
        String sql = "SELECT * FROM utilisateur";
        List<Utilisateur> utilisateurs = new ArrayList<>();

        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                utilisateurs.add(mapUtilisateur(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors du listage des utilisateurs", e);
        }

        return utilisateurs;
    }

    @Override
    public void update(Utilisateur entity) {
        String sql = "UPDATE utilisateur SET username = ?, password = ? WHERE id = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, entity.getEmail());
            ps.setString(2, entity.getNom());
            ps.setInt(3, entity.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la modification de l'utilisateur", e);
        }
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM utilisateur WHERE id = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la suppression de l'utilisateur", e);
        }
    }

    private Utilisateur mapUtilisateur(ResultSet rs) throws SQLException {
        return new Utilisateur(
                rs.getInt("id"),
                rs.getString("password"),
                rs.getString("role"),
                rs.getString("username")
        );
    }
}
