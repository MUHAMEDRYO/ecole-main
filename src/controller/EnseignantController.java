package controller;

import dao.EnseignantDaoImp;
import model.Enseignant;

import java.util.List;

public class EnseignantController implements Validation<Enseignant> {
    private final EnseignantDaoImp dao = new EnseignantDaoImp();

    public void addEnseignant(Enseignant enseignant) {
        validate(enseignant);
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
        validate(enseignant);
        validateId(enseignant.getId());
        dao.update(enseignant);
    }

    public void deleteEnseignant(Enseignant enseignant) {
        if (enseignant == null) {
            throw new IllegalArgumentException("Enseignant invalide");
        }
        deleteEnseignant(enseignant.getId());
    }

    public void deleteEnseignant(int id) {
        validateId(id);
        dao.delete(id);
    }


    @Override
    public void validate(Enseignant enseignant) {
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
    @Override
    public void validateId(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("Id invalide");
        }
    }

}
