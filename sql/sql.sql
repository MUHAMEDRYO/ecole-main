-- School Management System Database Schema
CREATE DATABASE IF NOT EXISTS school_db;
USE school_db;

-- Users table (login)
CREATE TABLE IF NOT EXISTS utilisateur (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role ENUM('ADMIN', 'ENSEIGNANT', 'ETUDIANT') NOT NULL
);

-- Students
CREATE TABLE IF NOT EXISTS etudiant (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nom VARCHAR(100) NOT NULL,
    prenom VARCHAR(100) NOT NULL,
    date_naissance DATE,
    email VARCHAR(150),
    telephone VARCHAR(20),
    utilisateur_id INT,
    FOREIGN KEY (utilisateur_id) REFERENCES utilisateur(id) ON DELETE SET NULL
);

-- Teachers
CREATE TABLE IF NOT EXISTS enseignant (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nom VARCHAR(100) NOT NULL,
    prenom VARCHAR(100) NOT NULL,
    email VARCHAR(150),
    telephone VARCHAR(20),
    specialite VARCHAR(100),
    utilisateur_id INT,
    FOREIGN KEY (utilisateur_id) REFERENCES utilisateur(id) ON DELETE SET NULL
);

-- Subjects
CREATE TABLE IF NOT EXISTS matiere (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nom VARCHAR(100) NOT NULL,
    description VARCHAR(255),
    credits INT DEFAULT 3
);

-- Teacher-Subject assignment
CREATE TABLE IF NOT EXISTS enseignant_matiere (
    enseignant_id INT,
    matiere_id INT,
    PRIMARY KEY (enseignant_id, matiere_id),
    FOREIGN KEY (enseignant_id) REFERENCES enseignant(id) ON DELETE CASCADE,
    FOREIGN KEY (matiere_id) REFERENCES matiere(id) ON DELETE CASCADE
);

-- Student-Subject enrollment
CREATE TABLE IF NOT EXISTS etudiant_matiere (
    etudiant_id INT,
    matiere_id INT,
    PRIMARY KEY (etudiant_id, matiere_id),
    FOREIGN KEY (etudiant_id) REFERENCES etudiant(id) ON DELETE CASCADE,
    FOREIGN KEY (matiere_id) REFERENCES matiere(id) ON DELETE CASCADE
);

-- Grades
CREATE TABLE IF NOT EXISTS note (
    id INT AUTO_INCREMENT PRIMARY KEY,
    etudiant_id INT NOT NULL,
    matiere_id INT NOT NULL,
    note_ds DOUBLE,
    note_exam DOUBLE,
    note_finale DOUBLE,
    FOREIGN KEY (etudiant_id) REFERENCES etudiant(id) ON DELETE CASCADE,
    FOREIGN KEY (matiere_id) REFERENCES matiere(id) ON DELETE CASCADE
);

-- Default admin account (password: admin123)
INSERT IGNORE INTO utilisateur (username, password, role) VALUES
('admin', 'admin123', 'ADMIN'),
('prof1', 'prof123', 'ENSEIGNANT'),
('etu1', 'etu123', 'ETUDIANT');

-- Sample data
INSERT IGNORE INTO enseignant (nom, prenom, email, specialite, utilisateur_id) VALUES
('Hachicha', 'Sofiane', 'sofiane@school.tn', 'Programmation', 2);

INSERT IGNORE INTO etudiant (nom, prenom, email, utilisateur_id) VALUES
('Ben Ali', 'Mohamed', 'mohamed@email.tn', 3);

INSERT IGNORE INTO matiere (nom, description, credits) VALUES
('Programmation OO', 'Java et POO', 4),
('Bases de données', 'MySQL et SQL', 3),
('Algorithmes', 'Structures de données', 3);
