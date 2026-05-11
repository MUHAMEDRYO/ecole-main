package controller;

import dao.UtilisateurDaoImp;
import model.Utilisateur;

public class AuthController {
    private final UtilisateurDaoImp dao = new UtilisateurDaoImp(); //final 5ater lazem n5dmou bnafs connection no93douch nbadlou feha
    private Utilisateur currentUser;

    public Utilisateur login(String username, String password) {
        if (username == null || username.trim().isEmpty() ||
                password == null || password.trim().isEmpty()) {
            return null;
        }
        Utilisateur user = dao.findByLogin(username, password);
        if (user != null) {
            this.currentUser = user;
        }
        return user;
    }

    public void logout() {
        currentUser = null;
    }

    public Utilisateur getCurrentUser() {
        return currentUser;
    }

    public boolean isLoggedIn() {
        return currentUser != null;
    }
}
