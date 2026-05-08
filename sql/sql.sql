-- 1. Create and use the database
DROP DATABASE IF EXISTS school_db;
CREATE DATABASE school_db;
USE school_db;

-- 2. Table: utilisateur (The core login table)
-- Matched with Utilisateur.java and UtilisateurDaoImp.java
CREATE TABLE utilisateur (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role ENUM('ADMIN', 'ENSEIGNANT', 'ETUDIANT') NOT NULL
);

-- 3. Table: enseignant (Linked to utilisateur)
-- Matched with EnseignantDaoImp.java
CREATE TABLE enseignant (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nom VARCHAR(100) NOT NULL,
    prenom VARCHAR(100) NOT NULL,
    email VARCHAR(150) UNIQUE NOT NULL,
    grade VARCHAR(50),
    specialite VARCHAR(100),
    utilisateur_id INT,
    CONSTRAINT fk_enseignant_user FOREIGN KEY (utilisateur_id) 
        REFERENCES utilisateur(id) ON DELETE CASCADE
);

-- 4. Table: etudiant (Linked to utilisateur)
-- Matched with EtudiantDaoImp.java
CREATE TABLE etudiant (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nom VARCHAR(100) NOT NULL,
    prenom VARCHAR(100) NOT NULL,
    email VARCHAR(150) UNIQUE NOT NULL,
    utilisateur_id INT,
    CONSTRAINT fk_etudiant_user FOREIGN KEY (utilisateur_id) 
        REFERENCES utilisateur(id) ON DELETE CASCADE
);

-- 5. Table: personnel (Admins)
CREATE TABLE personnel (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nom VARCHAR(100) NOT NULL,
    prenom VARCHAR(100) NOT NULL,
    email VARCHAR(150) UNIQUE NOT NULL,
    utilisateur_id INT,
    CONSTRAINT fk_personnel_user FOREIGN KEY (utilisateur_id) 
        REFERENCES utilisateur(id) ON DELETE CASCADE
);

-- 6. Table: matiere
CREATE TABLE matiere (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nom VARCHAR(100) NOT NULL
);

-- 7. Join Table: enseignant_matiere
CREATE TABLE enseignant_matiere (
    enseignant_id INT,
    matiere_id INT,
    PRIMARY KEY (enseignant_id, matiere_id),
    FOREIGN KEY (enseignant_id) REFERENCES enseignant(id) ON DELETE CASCADE,
    FOREIGN KEY (matiere_id) REFERENCES matiere(id) ON DELETE CASCADE
);

-- 8. Table: note
-- Columns matched with Note.java calculation logic
CREATE TABLE note (
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


-- 9. INSERT DEFAULT DATA
-- Insert Admin User
INSERT INTO utilisateur (username, password, role) 
VALUES ('admin', 'admin123', 'ADMIN');

-- Link Admin User to Personnel table (assuming ID 1)
INSERT INTO personnel (nom, prenom, email, utilisateur_id)
VALUES ('Sassi', 'Ayoub', 'admin@isimg.tn', 1);

-- Optional: Insert a test Teacher and Student
INSERT INTO utilisateur (username, password, role) VALUES ('prof1', 'prof123', 'ENSEIGNANT');
INSERT INTO enseignant (nom, prenom, email, specialite, utilisateur_id) VALUES ('Hachicha', 'Sofiane', 'sofiane@school.tn', 'Java', 2);

INSERT INTO utilisateur (username, password, role) VALUES ('etu1', 'etu123', 'ETUDIANT');
INSERT INTO etudiant (nom, prenom, email, utilisateur_id) VALUES ('Ben Ahmed', 'Ali', 'ali@student.tn', 3);