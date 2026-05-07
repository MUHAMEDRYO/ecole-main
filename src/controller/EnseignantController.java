package controller;

import dao.EnseignantDaoImp;
import model.Enseignant;

import java.util.List;

public class EnseignantController {
    private final EnseignantDaoImp dao = new EnseignantDaoImp();

    public void addEnseignant(Enseignant enseignant) {
        validateEnseignant(enseignant);
        dao.add(enseignant);
    }

    public Enseignant findEnseignantById(int id) {
        validateId(id);
        return dao.findById(id);
    }

    public List<Enseignant> findAllEnseignants() {
        return dao.findAll();
    }

    public void updateEnseignant(Enseignant enseignant) {
        validateEnseignant(enseignant);
        validateId(enseignant.getId());
        dao.update(enseignant);
    }

    public void deleteEnseignant(Enseignant enseignant) {
        if (enseignant == null) {
            throw new IllegalArgumentException("Enseignant invalide");
        }
        validateId(enseignant.getId());
        dao.delete(enseignant.getId());
    }



    private void validateEnseignant(Enseignant enseignant) {
        if (enseignant == null) {
            throw new IllegalArgumentException("Enseignant invalide");
        }
        if (enseignant.getNom() == null || enseignant.getNom().trim().isEmpty()) {
            throw new IllegalArgumentException("Le nom est obligatoire");
        }
        if (enseignant.getPrenom() == null || enseignant.getPrenom().trim().isEmpty()) {
            throw new IllegalArgumentException("Le prenom est obligatoire");
        }
        if (enseignant.getEmail() == null || enseignant.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("L'email est obligatoire");
        }
    }

    private void validateId(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("Id invalide");
        }
    }

}
