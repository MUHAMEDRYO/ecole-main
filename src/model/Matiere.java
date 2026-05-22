package model;

import java.util.ArrayList;
import java.util.List;

public class Matiere {
    private int id;
    private String nom;
    private List<Enseignant> enseignants = new ArrayList<>(); // El profet elli y9arriw el matiere hethi

    public Matiere() {}

    public Matiere(int id, String nom, Enseignant enseignant) {
        this.id = id;
        this.nom = nom;
        setEnseignant(enseignant);

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

    public List<Enseignant> getEnseignants() {
        return enseignants;
    }

    public void setEnseignants(List<Enseignant> enseignants) {
        this.enseignants = enseignants;
    }

    public Enseignant getEnseignant() {
        return enseignants.isEmpty() ? null : enseignants.get(0);
    }

    public void setEnseignant(Enseignant enseignant) {
        enseignants.clear();
        if (enseignant != null) {
            enseignants.add(enseignant);
        }
    }

    @Override
    public String toString() {
        return nom;
    }
}
