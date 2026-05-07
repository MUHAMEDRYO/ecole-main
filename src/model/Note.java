package model;

public class Note {
    private int id;
    private double noteDs;
    private double noteExamen;
    private double moyenne;
    private Etudiant etudiant;
    private Matiere matiere;
    private Enseignant enseignant;

    public Note() {}

    public Note(int id, double noteDs, double noteExamen, double moyenne, Etudiant etudiant, Matiere matiere, Enseignant enseignant) {
        this.id = id;
        this.noteDs = noteDs;
        this.noteExamen = noteExamen;
        this.moyenne = moyenne;
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

    public double getNoteDs() {
        return noteDs;
    }

    public void setNoteDs(double noteDs) {
        this.noteDs = noteDs;
        calculerMoyenne();
    }

    public double getNoteExamen() {
        return noteExamen;
    }

    public void setNoteExamen(double noteExamen) {
        this.noteExamen = noteExamen;
        calculerMoyenne();
    }

    public double getMoyenne() {
        return moyenne;
    }

    public void setMoyenne(double moyenne) {
        this.moyenne = moyenne;
    }

    public void calculerMoyenne() {
        this.moyenne = noteDs *0.3 + noteExamen * 0.7;
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
        return "Note [" + matiere.getNom() + "]: DS=" + noteDs + ", Examen=" + noteExamen + ", Moyenne=" + moyenne + " (Etudiant: " + etudiant.getNom() + ")";
    }
}
