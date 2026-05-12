package controller;

import dao.MatiereDaoImp;
import model.Matiere;

import java.util.List;

public class MatiereController implements Validation<Matiere> {
    private final MatiereDaoImp dao = new MatiereDaoImp();

    public void addMatiere(Matiere matiere) {
        validate(matiere);
        dao.add(matiere);
    }

    public Matiere findMatiereById(int id) {
        validateId(id);
        return dao.findById(id);
    }

    public List<Matiere> findAllMatieres() {
        return dao.findAll();
    }

    public void updateMatiere(Matiere matiere) {
        validate(matiere);
        validateId(matiere.getId());
        dao.update(matiere);
    }

    public void deleteMatiere(Matiere matiere) {
        if (matiere == null) {
            throw new IllegalArgumentException("Matiere invalide");
        }
        deleteMatiere(matiere.getId());
    }

    public void deleteMatiere(int id) {
        validateId(id);
        dao.delete(id);
    }
    @Override
    public void validate(Matiere matiere) {
        if (matiere == null) {
            throw new IllegalArgumentException("Matiere invalide");
        }
        if (matiere.getNom() == null || matiere.getNom().trim().isEmpty()) {
            throw new IllegalArgumentException("Le nom de la matiere est obligatoire");
        }
    }
    @Override
    public void validateId(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("Id invalide");
        }
    }
}
