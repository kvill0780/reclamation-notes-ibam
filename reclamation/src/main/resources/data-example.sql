-- Exemple de données pour le système de réclamation IBAM
-- Remplacez les mots de passe par des hash BCrypt sécurisés

-- Insertion des utilisateurs (mot de passe: "password123")
INSERT INTO app_users (nom, prenom, email, password_hash, role, niveau, filiere) VALUES
('Dupont', 'Jean', 'jean.dupont@ibam.ma', '$2a$10$EXAMPLE_HASH_HERE', 'ROLE_ETUDIANT', 'L3', 'MIAGE'),
('Martin', 'Marie', 'marie.martin@ibam.ma', '$2a$10$EXAMPLE_HASH_HERE', 'ROLE_ETUDIANT', 'L2', 'CCA'),
('Benali', 'Ahmed', 'ahmed.benali@ibam.ma', '$2a$10$EXAMPLE_HASH_HERE', 'ROLE_ENSEIGNANT', NULL, NULL),
('Tazi', 'Omar', 'omar.tazi@ibam.ma', '$2a$10$EXAMPLE_HASH_HERE', 'ROLE_SCOLARITE', NULL, NULL),
('Bennani', 'Rachid', 'rachid.bennani@ibam.ma', '$2a$10$EXAMPLE_HASH_HERE', 'ROLE_DA', NULL, NULL);

-- Insertion des matières
INSERT INTO matieres (nom, code, description) VALUES
('Comptabilité Générale', 'CG101', 'Principes de base de la comptabilité'),
('Systèmes d''Information', 'SI201', 'Analyse et conception de SI'),
('Techniques de Compilation', 'TC301', 'Compilation et analyse lexicale/syntaxique');

-- Insertion des enseignements
INSERT INTO enseignements (enseignant_id, matiere_id, filiere, niveau, semestre, annee_academique) VALUES
(3, 2, 'MIAGE', 'L3', 'S5', '2023-2024'),
(3, 3, 'MIAGE', 'L3', 'S5', '2023-2024');

-- Insertion des notes d'exemple
INSERT INTO notes (etudiant_id, enseignement_id, valeur) VALUES
(1, 1, 12.5),
(1, 2, 16.5);

-- Insertion d'une période de réclamation
INSERT INTO periodes_reclamation (nom, date_debut, date_fin, active, createur_id, description, date_creation) VALUES
('Réclamations Semestre 1 - 2024', '2024-01-15 08:00:00', '2024-01-18 17:00:00', true, 5, 'Période de réclamation pour les notes du premier semestre', CURRENT_TIMESTAMP);