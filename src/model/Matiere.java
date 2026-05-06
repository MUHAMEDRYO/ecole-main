package model;

public class Matiere {
    private int id;
    private String nom;
    private Enseignant enseignant; // El prof elli i9arri el matiere hethi

    public Matiere() {}

    public Matiere(int id, String nom, Enseignant enseignant) {
        this.id = id;
        this.nom = nom;
        this.enseignant = enseignant;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public Enseignant getEnseignant() {
        return enseignant;
    }

    public void setEnseignant(Enseignant enseignant) {
        this.enseignant = enseignant;
    }

    @Override
    public String toString() {
        return "Matiere: " + nom + " (Prof: " + enseignant.getNom() + ")";
    }
}