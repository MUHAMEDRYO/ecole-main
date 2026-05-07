package controller;

import dao.EtudiantDaoImp;
import model.Etudiant;

import java.util.List;

public class EtudiantController {
    private final EtudiantDaoImp dao = new EtudiantDaoImp();

    public void addEtudiant(Etudiant etudiant) {
        validateEtudiant(etudiant);
        dao.add(etudiant);
    }

    public Etudiant findEtudiantById(int id) {
        validateId(id);
        return dao.findById(id);
    }

    public List<Etudiant> findAllEtudiants() {
        return dao.findAll();
    }

    public void updateEtudiant(Etudiant etudiant) {
        validateEtudiant(etudiant);
        validateId(etudiant.getId());
        dao.update(etudiant);
    }

    public void deleteEtudiant(Etudiant etudiant) {
        if (etudiant == null) {
            throw new IllegalArgumentException("Etudiant invalide");
        }
        deleteEtudiant(etudiant.getId());
    }

    public void deleteEtudiant(int id) {
        validateId(id);
        dao.delete(id);
    }

    private void validateEtudiant(Etudiant etudiant) {
        if (etudiant == null) {
            throw new IllegalArgumentException("Etudiant invalide");
        }
        if (etudiant.getNom() == null || etudiant.getNom().trim().isEmpty()) {
            throw new IllegalArgumentException("Le nom est obligatoire");
        }
        if (etudiant.getPrenom() == null || etudiant.getPrenom().trim().isEmpty()) {
            throw new IllegalArgumentException("Le prenom est obligatoire");
        }
        if (etudiant.getEmail() == null || etudiant.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("L'email est obligatoire");
        }
    }

    private void validateId(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("Id invalide");
        }
    }

}
