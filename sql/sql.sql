CREATE DATABASE IF NOT EXISTS school_db;
USE school_db;

CREATE TABLE IF NOT EXISTS utilisateur (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role ENUM('ADMIN', 'ENSEIGNANT', 'ETUDIANT') NOT NULL
);

CREATE TABLE IF NOT EXISTS enseignant (
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

CREATE TABLE IF NOT EXISTS etudiant (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nom VARCHAR(100) NOT NULL,
    prenom VARCHAR(100) NOT NULL,
    email VARCHAR(150) UNIQUE NOT NULL,
    utilisateur_id INT,
    CONSTRAINT fk_etudiant_user FOREIGN KEY (utilisateur_id) 
        REFERENCES utilisateur(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS personnel (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nom VARCHAR(100) NOT NULL,
    prenom VARCHAR(100) NOT NULL,
    email VARCHAR(150) UNIQUE NOT NULL,
    utilisateur_id INT,
    CONSTRAINT fk_personnel_user FOREIGN KEY (utilisateur_id) 
        REFERENCES utilisateur(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS matiere (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nom VARCHAR(100) NOT NULL
);

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

-- 1. Nettoyage (Remove Mohamed if exists as student or teacher to avoid conflicts)
DELETE FROM utilisateur WHERE username = 'mohamed' AND role != 'ADMIN';

-- 2. Insert Admins
INSERT IGNORE INTO utilisateur (id, username, password, role) VALUES (1, 'ayoub', 'a', 'ADMIN');
INSERT IGNORE INTO utilisateur (id, username, password, role) VALUES (2, 'mohamed', 'a', 'ADMIN');
INSERT IGNORE INTO personnel (id, nom, prenom, email, utilisateur_id) VALUES (1, 'rayen', 'bn', 'rayen@school.tn', 1);

-- 3. Insert 5 Enseignants (All Informatique)
-- First create their user accounts
INSERT IGNORE INTO utilisateur (id, username, password, role) VALUES 
(3, 'prof1', 'pass123', 'ENSEIGNANT'),
(4, 'prof2', 'pass123', 'ENSEIGNANT'),
(5, 'prof3', 'pass123', 'ENSEIGNANT'),
(6, 'prof4', 'pass123', 'ENSEIGNANT'),
(7, 'prof5', 'pass123', 'ENSEIGNANT');

-- Then link to enseignant table with Informatique specialty
INSERT IGNORE INTO enseignant (id, nom, prenom, email, grade, specialite, utilisateur_id) VALUES 
(1, 'Trabelsi', 'Mohamed', 'm.trabelsi@isimg.tn', ' ', 'analyse', 3),
(2, 'Ben Ali', 'Sami', 's.benali@isimg.tn', '', 'algebre', 4),
(3, 'Ghorbel', 'Leila', 'l.ghorbel@isimg.tn', ' de Conférence', 'python', 5),
(4, 'Jallouli', 'Ahmed', 'a.jallouli@isimg.tn', '', 'resau', 6),
(5, 'Mansour', 'Sonia', 's.mansour@isimg.tn', '', 'java', 7);

-- 4. Insert 20 Etudiants
INSERT IGNORE INTO utilisateur (id, username, password, role) VALUES 
(8, 'std1', 'p', 'ETUDIANT'), (9, 'std2', 'p', 'ETUDIANT'), (10, 'std3', 'p', 'ETUDIANT'), (11, 'std4', 'p', 'ETUDIANT'), 
(12, 'std5', 'p', 'ETUDIANT'), (13, 'std6', 'p', 'ETUDIANT'), (14, 'std7', 'p', 'ETUDIANT'), (15, 'std8', 'p', 'ETUDIANT'), 
(16, 'std9', 'p', 'ETUDIANT'), (17, 'std10', 'p', 'ETUDIANT'), (18, 'std11', 'p', 'ETUDIANT'), (19, 'std12', 'p', 'ETUDIANT'), 
(20, 'std13', 'p', 'ETUDIANT'), (21, 'std14', 'p', 'ETUDIANT'), (22, 'std15', 'p', 'ETUDIANT'), (23, 'std16', 'p', 'ETUDIANT'), 
(24, 'std17', 'p', 'ETUDIANT'), (25, 'std18', 'p', 'ETUDIANT'), (26, 'std19', 'p', 'ETUDIANT'), (27, 'std20', 'p', 'ETUDIANT');

INSERT IGNORE INTO etudiant (id, nom, prenom, email, utilisateur_id) VALUES 
(1, 'Abidi', 'Ali', 'ali.abidi@isimg.tn', 8),
(2, 'Bouaziz', 'Fatma', 'fatma.b@isimg.tn', 9),
(3, 'Chaari', 'Yassine', 'yassine.c@isimg.tn', 10),
(4, 'Dali', 'Amira', 'amira.d@isimg.tn', 11),
(5, 'Ellouze', 'Omar', 'omar.e@isimg.tn', 12),
(6, 'Feki', 'Rim', 'rim.f@isimg.tn', 13),
(7, 'Gargouri', 'Hassen', 'hassen.g@isimg.tn', 14),
(8, 'Hammami', 'Sarra', 'sarra.h@isimg.tn', 15),
(9, 'Ismail', 'Khaled', 'khaled.i@isimg.tn', 16),
(10, 'Jarraya', 'Meryem', 'meryem.j@isimg.tn', 17),
(11, 'Karray', 'Tarek', 'tarek.k@isimg.tn', 18),
(12, 'Loulou', 'Ines', 'ines.l@isimg.tn', 19),
(13, 'Mezghani', 'Walid', 'walid.m@isimg.tn', 20),
(14, 'Najar', 'Olfa', 'olfa.n@isimg.tn', 21),
(15, 'Ouali', 'Zied', 'zied.o@isimg.tn', 22),
(16, 'Rekik', 'Hela', 'hela.r@isimg.tn', 23),
(17, 'Sellami', 'Fedi', 'fedi.s@isimg.tn', 24),
(18, 'Turki', 'Salma', 'salma.t@isimg.tn', 25),
(19, 'Youssef', 'Anis', 'anis.y@isimg.tn', 26),
(20, 'Zghal', 'Nour', 'nour.z@isimg.tn', 27);

-- 5. Sample Matieres
INSERT IGNORE INTO matiere (id, nom) VALUES (1, 'Java'), (2, 'Base de Données'), (3, 'algebre');