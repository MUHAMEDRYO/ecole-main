package model;

public class Enseignant extends Utilisateur {
    private String nom;
    private String prenom;
    private String email;
    private Matiere specialite;

    public Enseignant() {
        setRole("ENSEIGNANT");
    }

    public Enseignant(int id, String nom, String prenom, String email) {
        setId(id);
        setRole("ENSEIGNANT");
        setUsername(email);
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;

    }

    public Enseignant(int id, String nom, String prenom, String email,  Matiere specialite) {
        setId(id);
        setRole("ENSEIGNANT");
        setUsername(email);
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;

        this.specialite = specialite;
    }

    public Enseignant(int id, String nom, String prenom, String email, String role) {
        setId(id);
        setRole(role);
        setUsername(email);
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;

    }

    public Enseignant(int id, String nom, String prenom, String email, String role, String grade, Matiere specialite) {
        setId(id);
        setRole(role);
        setUsername(email);
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;

        this.specialite = specialite;
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

    public Matiere getSpecialite() {
        return specialite;
    }

    public void setSpecialite(Matiere specialite) {
        this.specialite = specialite;
    }

    @Override
    public String toString() {
        return "Enseignant{" +
                "id=" + getId() +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", email='" + email + '\'' +
                ", role='" + getRole() + '\'' +
                ", specialite='" + specialite + '\'' +
                '}';
    }
}
