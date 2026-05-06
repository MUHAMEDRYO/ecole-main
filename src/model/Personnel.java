package model;

public class Personnel extends Utilisateur {
    private String nom;
    private String prenom;
    private String email;

    public Personnel() {
        setRole("ADMIN");
    }

    public Personnel(int id, String nom, String prenom, String email) {
        setId(id);
        setRole("ADMIN");
        setUsername(email);
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
    }

    public Personnel(int id, String nom, String prenom, String email, String role) {
        setId(id);
        setRole(role);
        setUsername(email);
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
        setUsername(email);
    }
}
