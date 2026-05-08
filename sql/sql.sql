-- Create the database
CREATE DATABASE IF NOT EXISTS school_db;
USE ecole; 

-- 1. Table: utilisateur
-- Based on Utilisateur.java and login logic in AuthController
CREATE TABLE IF NOT EXISTS utilisateur (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role ENUM('ADMIN', 'ENSEIGNANT', 'ETUDIANT') NOT NULL
);

-- 2. Table: enseignant
-- Based on Enseignant.java and EnseignantDaoImp.java
CREATE TABLE IF NOT EXISTS enseignant (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nom VARCHAR(100) NOT NULL,
    prenom VARCHAR(100) NOT NULL,
    email VARCHAR(150) UNIQUE NOT NULL,
    grade VARCHAR(50),
    specialite VARCHAR(100), -- Used in mapEnseignant method
    utilisateur_id INT,
    CONSTRAINT fk_enseignant_user FOREIGN KEY (utilisateur_id) 
        REFERENCES utilisateur(id) ON DELETE CASCADE
);

-- 3. Table: etudiant
-- Based on Etudiant.java and EtudiantDaoImp.java
CREATE TABLE IF NOT EXISTS etudiant (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nom VARCHAR(100) NOT NULL,
    prenom VARCHAR(100) NOT NULL,
    email VARCHAR(150) UNIQUE NOT NULL,
    utilisateur_id INT,
    CONSTRAINT fk_etudiant_user FOREIGN KEY (utilisateur_id) 
        REFERENCES utilisateur(id) ON DELETE CASCADE
);

-- 4. Table: personnel (Admin members)
-- Based on Personnel.java
CREATE TABLE IF NOT EXISTS personnel (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nom VARCHAR(100) NOT NULL,
    prenom VARCHAR(100) NOT NULL,
    email VARCHAR(150) UNIQUE NOT NULL,
    utilisateur_id INT,
    CONSTRAINT fk_personnel_user FOREIGN KEY (utilisateur_id) 
        REFERENCES utilisateur(id) ON DELETE CASCADE
);

-- 5. Table: matiere
-- Based on Matiere.java and MatiereDaoImp.java
CREATE TABLE IF NOT EXISTS matiere (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nom VARCHAR(100) NOT NULL
);

-- 6. Join Table: enseignant_matiere
-- Found in MatiereDaoImp.java logic (saveEnseignantMatiere)
CREATE TABLE IF NOT EXISTS enseignant_matiere (
    enseignant_id INT,
    matiere_id INT,
    PRIMARY KEY (enseignant_id, matiere_id),
    FOREIGN KEY (enseignant_id) REFERENCES enseignant(id) ON DELETE CASCADE,
    FOREIGN KEY (matiere_id) REFERENCES matiere(id) ON DELETE CASCADE
);

-- 7. Table: note
-- Based on Note.java and NoteController.java
CREATE TABLE IF NOT EXISTS note (
    id INT AUTO_INCREMENT PRIMARY KEY,
    note_ds DOUBLE DEFAULT 0,
    note_examen DOUBLE DEFAULT 0,
    moyenne DOUBLE DEFAULT 0,
    etudiant_id INT NOT NULL,
    matiere_id INT NOT NULL,
    enseignant_id INT,
    FOREIGN KEY (etudiant_id) REFERENCES etudiant(id) ON DELETE CASCADE,
    FOREIGN KEY (matiere_id) REFERENCES matiere(id) ON DELETE CASCADE,
    FOREIGN KEY (enseignant_id) REFERENCES enseignant(id) ON DELETE SET NULL
);

-- Initial Data for Testing
INSERT IGNORE INTO utilisateur (username, password, role) VALUES 
('admin', 'a', 'ADMIN');