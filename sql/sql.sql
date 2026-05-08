-- 1. Fassa5 el base ken mawjouda bech n-nadfou kol chay
DROP DATABASE IF EXISTS school_db;
CREATE DATABASE school_db;
USE school_db;

-- 2. Table: utilisateur (Lazem esmha 'username' kima fil Java)
CREATE TABLE utilisateur (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role ENUM('ADMIN', 'ENSEIGNANT', 'ETUDIANT') NOT NULL
);

-- 3. Table: enseignant
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

-- 4. Table: etudiant
CREATE TABLE etudiant (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nom VARCHAR(100) NOT NULL,
    prenom VARCHAR(100) NOT NULL,
    email VARCHAR(150) UNIQUE NOT NULL,
    utilisateur_id INT,
    CONSTRAINT fk_etudiant_user FOREIGN KEY (utilisateur_id) 
        REFERENCES utilisateur(id) ON DELETE CASCADE
);

-- 5. Table: personnel (Admin)
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

-- 7. Table: note
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

-- 8. Zid el Admin bech t-testi bih
INSERT INTO utilisateur (username, password, role) 
VALUES ('admin', 'admin123', 'ADMIN');

-- Erbat el user m3a el personnel (Admin)
INSERT INTO personnel (nom, prenom, email, utilisateur_id)
VALUES ('Sassi', 'Ayoub', 'admin@isimg.tn', 1);

UPDATE utilisateur 
SET username = 'ayoub', password = 'a' 
WHERE id = 1;