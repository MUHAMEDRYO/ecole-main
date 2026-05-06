package model;

public class Enseignant extends Utilisateur {
    private String grade;

    public Enseignant() {}

    public Enseignant(int id, String nom, String prenom, String email, String grade) {
        super(id, nom, prenom, email);
        this.grade = grade;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    @Override
    public String toString() {
        return super.toString() + " [Grade: " + grade + "]";
    }
}