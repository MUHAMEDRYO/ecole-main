package model;

public class Note {
    private int id;
    private double valeur;
    private Etudiant etudiant;
    private Matiere matiere;
    private Enseignant enseignant;

    public Note() {}

    public Note(int id, double valeur, Etudiant etudiant, Matiere matiere, Enseignant enseignant) {
        this.id = id;
        this.valeur = valeur;
        this.etudiant = etudiant;
        this.matiere = matiere;
        this.enseignant = enseignant;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getValeur() {
        return valeur;
    }

    public void setValeur(double valeur) {
        this.valeur = valeur;
    }

    public Etudiant getEtudiant() {
        return etudiant;
    }

    public void setEtudiant(Etudiant etudiant) {
        this.etudiant = etudiant;
    }

    public Matiere getMatiere() {
        return matiere;
    }

    public void setMatiere(Matiere matiere) {
        this.matiere = matiere;
    }

    public Enseignant getEnseignant() {
        return enseignant;
    }

    public void setEnseignant(Enseignant enseignant) {
        this.enseignant = enseignant;
    }

    @Override
    public String toString() {
        return "Note [" + matiere.getNom() + "]: " + valeur + " (Etudiant: " + etudiant.getNom() + ")";
    }
}