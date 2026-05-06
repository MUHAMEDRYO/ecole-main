package model;

public class Enseignant extends Utilisateur {
    private String nom;
    private String prenom;
    private String email;
    private String grade;

    public Enseignant() {
        setRole("ENSEIGNANT");
    }

    public Enseignant(int id, String nom, String prenom, String email, String grade) {
        setId(id);
        setRole("ENSEIGNANT");
        setUsername(email);
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.grade = grade;
    }

    public Enseignant(int id, String nom, String prenom, String email, String role, String grade) {
        setId(id);
        setRole(role);
        setUsername(email);
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.grade = grade;
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

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    @Override
    public String toString() {
        return "Enseignant{" +
                "id=" + getId() +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", email='" + email + '\'' +
                ", role='" + getRole() + '\'' +
                ", grade='" + grade + '\'' +
                '}';
    }
}
