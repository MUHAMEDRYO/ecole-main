package model;

public class Etudiant extends Utilisateur {

    public Etudiant() {}


    public Etudiant(int id, String nom, String prenom, String email) {
        super(id, nom, prenom, email);
    }

    @Override
    public String toString() {
        return "Etudiant: " + super.toString();
    }
//sed bih
}