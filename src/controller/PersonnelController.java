package controller;


import dao.PersonnelDaoImp;
import model.Personnel;


import java.util.List;

public class PersonnelController implements Validation<Personnel> {
    private final PersonnelDaoImp dao = new PersonnelDaoImp();

    public void addPersonnel(Personnel p) {
        validate(p);
        dao.add(p);
    }

    public Personnel findPersonnelById(int id) {
        validateId(id);
        return dao.findById(id);
    }

    public List<Personnel> findAllPersonnel() {
        return dao.findAll();
    }

    public void updatePersonnel(Personnel p) {
        validate(p);
        validateId(p.getId());
        dao.update(p);
    }

    public void deletePersonnel(Personnel p) {
        if (p == null) {
            throw new IllegalArgumentException("Personnel invalide");
        }
        deletePersonnel(p.getId());
    }

    public void deletePersonnel(int id) {
        validateId(id);
        dao.delete(id);
    }
    @Override
    public void validate(Personnel p) {
        if (p == null) {
            throw new IllegalArgumentException("Personnel invalide");
        }
        if (p.getNom() == null || p.getNom().trim().isEmpty()) {
            throw new IllegalArgumentException("Le nom est obligatoire");
        }
        if (p.getPrenom() == null || p.getPrenom().trim().isEmpty()) {
            throw new IllegalArgumentException("Le prenom est obligatoire");
        }
        if (p.getEmail() == null || p.getEmail().trim().isEmpty()) {
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
