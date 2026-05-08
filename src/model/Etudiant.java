package model;

public class Etudiant extends Utilisateur {
    private String nom;
    private String prenom;
    private String email;

    public Etudiant() {
        setRole("ETUDIANT");
    }
    public Etudiant(String nom, String prenom, String email) {
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
    }

    public Etudiant(int id, String nom, String prenom, String email) {
        setId(id);
        setRole("ETUDIANT");
        setUsername(email);
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
    }

    public Etudiant(int id, String nom, String prenom, String email, String role) {
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

    @Override
    public String toString() {
        return "Etudiant{" +
                "id=" + getId() +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", email='" + email + '\'' +
                ", role='" + getRole() + '\'' +
                '}';
    }
}
