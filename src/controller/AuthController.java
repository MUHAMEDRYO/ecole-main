package controller;

import dao.UtilisateurDaoImp;
import model.Utilisateur;

public class AuthController {
    private final UtilisateurDaoImp dao = new UtilisateurDaoImp(); //final 5ater lazem n5dmou bnafs connection no93douch nbadlou feha

    public Utilisateur login(String username, String password) {
        if (username == null || username.trim().isEmpty() ||
                password == null || password.trim().isEmpty()) {
            return null;
        }
        return dao.findByLogin(username, password);
    }

    public void logout() {
        // Later, clear the current session/user here if you store one.
    }

    public String navigate(Utilisateur user) {
        if (user == null || user.getRole() == null) {
            return null;
        }

        switch (user.getRole()) {
            case "ENSEIGNANT":
                return "/views/enseignant.fxml";
            case "ETUDIANT":
                return "/views/etudiant.fxml";
            case "ADMIN":
                return "/views/personnel.fxml";
            default:
                return null;
        }
    }



}
