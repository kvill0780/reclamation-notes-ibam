-- Insertion des utilisateurs avec mots de passe hachés (BCrypt pour "password123")
INSERT INTO app_users (nom, prenom, email, password_hash, role, niveau, filiere) VALUES
('Dupont', 'Jean', 'jean.dupont@ibam.ma', '$2a$10$UVrhh0mmjJGHH4DJ89jbxuNKpoErax3Jz4RPCvxMsGGJBd2NwrOOC', 'ROLE_ETUDIANT', 'L3', 'MIAGE'),
('Martin', 'Marie', 'marie.martin@ibam.ma', '$2a$10$UVrhh0mmjJGHH4DJ89jbxuNKpoErax3Jz4RPCvxMsGGJBd2NwrOOC', 'ROLE_ETUDIANT', 'L2', 'CCA'),
('Benali', 'Ahmed', 'ahmed.benali@ibam.ma', '$2a$10$UVrhh0mmjJGHH4DJ89jbxuNKpoErax3Jz4RPCvxMsGGJBd2NwrOOC', 'ROLE_ENSEIGNANT', NULL, NULL),
('Alami', 'Fatima', 'fatima.alami@ibam.ma', '$2a$10$UVrhh0mmjJGHH4DJ89jbxuNKpoErax3Jz4RPCvxMsGGJBd2NwrOOC', 'ROLE_ENSEIGNANT', NULL, NULL),
('GUINKO', 'Ferdinand', 'ferdinand.guinko@ibam.ma', '$2a$10$UVrhh0mmjJGHH4DJ89jbxuNKpoErax3Jz4RPCvxMsGGJBd2NwrOOC', 'ROLE_ENSEIGNANT', NULL, NULL),
('Tazi', 'Omar', 'omar.tazi@ibam.ma', '$2a$10$UVrhh0mmjJGHH4DJ89jbxuNKpoErax3Jz4RPCvxMsGGJBd2NwrOOC', 'ROLE_SCOLARITE', NULL, NULL),
('Bennani', 'Rachid', 'rachid.bennani@ibam.ma', '$2a$10$UVrhh0mmjJGHH4DJ89jbxuNKpoErax3Jz4RPCvxMsGGJBd2NwrOOC', 'ROLE_DA', NULL, NULL),
('TRAORE', 'Yaya', 'yaya.traore@ibam.ma', '$2a$10$UVrhh0mmjJGHH4DJ89jbxuNKpoErax3Jz4RPCvxMsGGJBd2NwrOOC', 'ROLE_ENSEIGNANT', NULL, NULL),
-- Étudiants MIAGE L3
('COMPAORE', 'W. Aubin', 'aubin.compaore@ibam.ma', '$2a$10$UVrhh0mmjJGHH4DJ89jbxuNKpoErax3Jz4RPCvxMsGGJBd2NwrOOC', 'ROLE_ETUDIANT', 'L3', 'MIAGE'),
('CONGOMBO', 'Soumaila', 'soumaila.congombo@ibam.ma', '$2a$10$UVrhh0mmjJGHH4DJ89jbxuNKpoErax3Jz4RPCvxMsGGJBd2NwrOOC', 'ROLE_ETUDIANT', 'L3', 'MIAGE'),
('COULIBALY', 'Wilfried Patrick', 'wilfried.coulibaly@ibam.ma', '$2a$10$UVrhh0mmjJGHH4DJ89jbxuNKpoErax3Jz4RPCvxMsGGJBd2NwrOOC', 'ROLE_ETUDIANT', 'L3', 'MIAGE'),
('DARGA', 'Sokuma Noël', 'noel.darga@ibam.ma', '$2a$10$UVrhh0mmjJGHH4DJ89jbxuNKpoErax3Jz4RPCvxMsGGJBd2NwrOOC', 'ROLE_ETUDIANT', 'L3', 'MIAGE'),
('DARGANI', 'Charlène Olivia', 'charlene.dargani@ibam.ma', '$2a$10$UVrhh0mmjJGHH4DJ89jbxuNKpoErax3Jz4RPCvxMsGGJBd2NwrOOC', 'ROLE_ETUDIANT', 'L3', 'MIAGE'),
('DIALLO', 'Belco', 'belco.diallo@ibam.ma', '$2a$10$UVrhh0mmjJGHH4DJ89jbxuNKpoErax3Jz4RPCvxMsGGJBd2NwrOOC', 'ROLE_ETUDIANT', 'L3', 'MIAGE'),
('DIALLO', 'Tasséré', 'tassere.diallo@ibam.ma', '$2a$10$UVrhh0mmjJGHH4DJ89jbxuNKpoErax3Jz4RPCvxMsGGJBd2NwrOOC', 'ROLE_ETUDIANT', 'L3', 'MIAGE'),
('DIAWARA', 'M. Fadel', 'fadel.diawara@ibam.ma', '$2a$10$UVrhh0mmjJGHH4DJ89jbxuNKpoErax3Jz4RPCvxMsGGJBd2NwrOOC', 'ROLE_ETUDIANT', 'L3', 'MIAGE'),
('DICKO', 'Alou', 'alou.dicko@ibam.ma', '$2a$10$UVrhh0mmjJGHH4DJ89jbxuNKpoErax3Jz4RPCvxMsGGJBd2NwrOOC', 'ROLE_ETUDIANT', 'L3', 'MIAGE'),
('DJEKOMPTE', 'M. Jessica Thérèse', 'jessica.djekompte@ibam.ma', '$2a$10$UVrhh0mmjJGHH4DJ89jbxuNKpoErax3Jz4RPCvxMsGGJBd2NwrOOC', 'ROLE_ETUDIANT', 'L3', 'MIAGE'),
('DRABO', 'N. Kadidia Yasmine', 'yasmine.drabo@ibam.ma', '$2a$10$UVrhh0mmjJGHH4DJ89jbxuNKpoErax3Jz4RPCvxMsGGJBd2NwrOOC', 'ROLE_ETUDIANT', 'L3', 'MIAGE'),
('ILBOUDO', 'B. Jules', 'jules.ilboudo@ibam.ma', '$2a$10$UVrhh0mmjJGHH4DJ89jbxuNKpoErax3Jz4RPCvxMsGGJBd2NwrOOC', 'ROLE_ETUDIANT', 'L3', 'MIAGE'),
('KABORE', 'Alida', 'alida.kabore@ibam.ma', '$2a$10$UVrhh0mmjJGHH4DJ89jbxuNKpoErax3Jz4RPCvxMsGGJBd2NwrOOC', 'ROLE_ETUDIANT', 'L3', 'MIAGE'),
('KANTAGBA', 'L. Oumarou', 'oumarou.kantagba@ibam.ma', '$2a$10$UVrhh0mmjJGHH4DJ89jbxuNKpoErax3Jz4RPCvxMsGGJBd2NwrOOC', 'ROLE_ETUDIANT', 'L3', 'MIAGE'),
('KAMBELE', 'Oumarou', 'oumarou.kambele@ibam.ma', '$2a$10$UVrhh0mmjJGHH4DJ89jbxuNKpoErax3Jz4RPCvxMsGGJBd2NwrOOC', 'ROLE_ETUDIANT', 'L3', 'MIAGE'),
('KIEMDE', 'Lucien', 'lucien.kiemde@ibam.ma', '$2a$10$UVrhh0mmjJGHH4DJ89jbxuNKpoErax3Jz4RPCvxMsGGJBd2NwrOOC', 'ROLE_ETUDIANT', 'L3', 'MIAGE'),
('KONFE', 'Adama', 'adama.konfe@ibam.ma', '$2a$10$UVrhh0mmjJGHH4DJ89jbxuNKpoErax3Jz4RPCvxMsGGJBd2NwrOOC', 'ROLE_ETUDIANT', 'L3', 'MIAGE'),
('NABA', 'Albert', 'albert.naba@ibam.ma', '$2a$10$UVrhh0mmjJGHH4DJ89jbxuNKpoErax3Jz4RPCvxMsGGJBd2NwrOOC', 'ROLE_ETUDIANT', 'L3', 'MIAGE'),
('OUANGA', 'Robert', 'robert.ouanga@ibam.ma', '$2a$10$UVrhh0mmjJGHH4DJ89jbxuNKpoErax3Jz4RPCvxMsGGJBd2NwrOOC', 'ROLE_ETUDIANT', 'L3', 'MIAGE'),
('OUEDRAOGO', 'Elodie', 'elodie.ouedraogo@ibam.ma', '$2a$10$UVrhh0mmjJGHH4DJ89jbxuNKpoErax3Jz4RPCvxMsGGJBd2NwrOOC', 'ROLE_ETUDIANT', 'L3', 'MIAGE'),
('SAVADOGO', 'Souhayouba', 'souhayouba.savadogo@ibam.ma', '$2a$10$UVrhh0mmjJGHH4DJ89jbxuNKpoErax3Jz4RPCvxMsGGJBd2NwrOOC', 'ROLE_ETUDIANT', 'L3', 'MIAGE'),
('SAWADOGO', 'Khaled', 'kaled.sawadogo@ibam.ma', '$2a$10$UVrhh0mmjJGHH4DJ89jbxuNKpoErax3Jz4RPCvxMsGGJBd2NwrOOC', 'ROLE_ETUDIANT', 'L3', 'MIAGE'),
('SAWADOGO', 'Nathanael', 'nathanael.sawadogo@ibam.ma', '$2a$10$UVrhh0mmjJGHH4DJ89jbxuNKpoErax3Jz4RPCvxMsGGJBd2NwrOOC', 'ROLE_ETUDIANT', 'L3', 'MIAGE'),
('SOME', 'Elvis', 'elvis.some@ibam.ma', '$2a$10$UVrhh0mmjJGHH4DJ89jbxuNKpoErax3Jz4RPCvxMsGGJBd2NwrOOC', 'ROLE_ETUDIANT', 'L3', 'MIAGE'),
('SOULAMA', 'Joel', 'joel.soulama@ibam.ma', '$2a$10$UVrhh0mmjJGHH4DJ89jbxuNKpoErax3Jz4RPCvxMsGGJBd2NwrOOC', 'ROLE_ETUDIANT', 'L3', 'MIAGE'),
('SOULI', 'Anicet', 'anicet.souli@ibam.ma', '$2a$10$UVrhh0mmjJGHH4DJ89jbxuNKpoErax3Jz4RPCvxMsGGJBd2NwrOOC', 'ROLE_ETUDIANT', 'L3', 'MIAGE'),
('TRAORE', 'Emmanuel', 'emmanuel.traore@ibam.ma', '$2a$10$UVrhh0mmjJGHH4DJ89jbxuNKpoErax3Jz4RPCvxMsGGJBd2NwrOOC', 'ROLE_ETUDIANT', 'L3', 'MIAGE'),
('TRAORE', 'Sié Donald', 'donald.traore@ibam.ma', '$2a$10$UVrhh0mmjJGHH4DJ89jbxuNKpoErax3Jz4RPCvxMsGGJBd2NwrOOC', 'ROLE_ETUDIANT', 'L3', 'MIAGE'),
('YAMEOGO', 'Hermane', 'hermane.yameogo@ibam.ma', '$2a$10$UVrhh0mmjJGHH4DJ89jbxuNKpoErax3Jz4RPCvxMsGGJBd2NwrOOC', 'ROLE_ETUDIANT', 'L3', 'MIAGE');

-- Insertion des matières IBAM
INSERT INTO matieres (nom, code, description) VALUES
('Comptabilité Générale', 'CG101', 'Principes de base de la comptabilité'),
('Contrôle de Gestion', 'CDG201', 'Techniques de contrôle et pilotage'),
('Audit Interne', 'AUD301', 'Méthodes d''audit et contrôle interne'),
('Marketing Digital', 'MKD201', 'Stratégies marketing numériques'),
('Innovation et Créativité', 'INN301', 'Méthodes d''innovation'),
('Assurance Vie', 'ASV201', 'Produits et techniques d''assurance vie'),
('Banque et Finance', 'BNF301', 'Opérations bancaires et financières'),
('Secrétariat Bilingue', 'SBL101', 'Techniques de secrétariat en français/anglais'),
('Communication Professionnelle', 'COM201', 'Communication en entreprise'),
('Systèmes d''Information', 'SI201', 'Analyse et conception de SI'),
('Gestion de Projet', 'GP301', 'Méthodes de gestion de projet'),
('Base de Données', 'BD201', 'Conception et gestion de bases de données'),
('Techniques de Compilation', 'TC301', 'Compilation et analyse lexicale/syntaxique'),
('Administration Unix', 'UNIX301', 'Administration systèmes Unix/Linux'),
('Analyse et Conception Orientée Objet', 'ACOO301', 'Méthodes UML et conception OO');

-- Insertion des enseignements (qui enseigne quoi)
INSERT INTO enseignements (enseignant_id, matiere_id, filiere, niveau, semestre, annee_academique) VALUES
-- Ahmed enseigne en MIAGE
(3, 10, 'MIAGE', 'L3', 'S5', '2023-2024'),  -- Systèmes d'Information
-- Fatima enseigne en CCA et ABF
(4, 1, 'CCA', 'L2', 'S3', '2023-2024'),   -- Comptabilité Générale
(4, 6, 'ABF', 'L3', 'S6', '2023-2024'),   -- Assurance Vie
-- GUINKO Ferdinand enseigne en MIAGE
(5, 13, 'MIAGE', 'L3', 'S5', '2023-2024'), -- Techniques de Compilation
(5, 14, 'MIAGE', 'L3', 'S5', '2023-2024'), -- Administration Unix
-- TRAORE Yaya enseigne en MIAGE (ID sera 38 en auto-increment)
(8, 12, 'MIAGE', 'L3', 'S5', '2023-2024'), -- Base de Données
(8, 15, 'MIAGE', 'L3', 'S5', '2023-2024'); -- Analyse et Conception Orientée Objet

-- Insertion des notes
INSERT INTO notes (etudiant_id, enseignement_id, valeur) VALUES
-- Notes pour Jean (MIAGE L3)
(1, 1, 12.5),  -- Jean a 12.5 en Systèmes d'Information avec Ahmed
(1, 6, 14.0),  -- Jean a 14.0 en Base de Données avec TRAORE Yaya
(1, 3, 16.5),  -- Jean a 16.5 en Techniques de Compilation avec GUINKO
-- Notes pour Marie (CCA L2)
(2, 2, 15.5),  -- Marie a 15.5 en Comptabilité Générale avec Fatima
-- Notes Techniques de Compilation MIAGE L3 (avec GUINKO Ferdinand)
(8, 3, 11.2),   -- COMPAORE W. Aubin
(9, 3, 12.9),   -- CONGOMBO Soumaila
(10, 3, 12.8),  -- COULIBALY Wilfried Patrick
(11, 3, 10.8),  -- DARGA Sokuma Noël
(12, 3, 8.5),   -- DARGANI Charlène Olivia
(13, 3, 7.9),   -- DIALLO Belco
(14, 3, 12.2),  -- DIALLO Tasséré
(15, 3, 12.8),  -- DIAWARA M. Fadel
(16, 3, 10.4),  -- DICKO Alou
(17, 3, 13.7),  -- DJEKOMPTE M. Jessica Thérèse
(18, 3, 14.7),  -- DRABO N. Kadidia Yasmine
(19, 3, 12.1),  -- ILBOUDO B. Jules
(20, 3, 15.9),  -- KABORE Alida
(21, 3, 10.8),  -- KANTAGBA L. Oumarou
(22, 3, 13.3),  -- KAMBELE Oumarou
(23, 3, 14.2),  -- KIEMDE Lucien
(24, 3, 10.8),  -- KONFE Adama
(25, 3, 13.0),  -- NABA Albert
(26, 3, 13.1),  -- OUANGA Robert
(27, 3, 13.4),  -- OUEDRAOGO Elodie
(28, 3, 10.8),  -- SAVADOGO Souhayouba
(29, 3, 15.8),  -- SAWADOGO Khaled
(30, 3, 10.2),  -- SAWADOGO Nathanael
(31, 3, 5.9),   -- SOME Elvis
(32, 3, 10.9),  -- SOULAMA Joel
(33, 3, 13.0),  -- SOULI Anicet
(34, 3, 13.9),  -- TRAORE Emmanuel
(35, 3, 10.9),  -- TRAORE Sié Donald
(36, 3, 6.5),   -- YAMEOGO Hermane
-- Notes Administration Unix MIAGE L3 (avec GUINKO Ferdinand)
(8, 4, 10.5),   -- COMPAORE W. Aubin
(9, 4, 13.2),   -- CONGOMBO Soumaila
(10, 4, 11.8),  -- COULIBALY Wilfried Patrick
(11, 4, 9.8),   -- DARGA Sokuma Noël
(12, 4, 7.5),   -- DARGANI Charlène Olivia
(13, 4, 8.9),   -- DIALLO Belco
(14, 4, 11.2),  -- DIALLO Tasséré
(15, 4, 13.8),  -- DIAWARA M. Fadel
(16, 4, 9.4),   -- DICKO Alou
(17, 4, 14.7),  -- DJEKOMPTE M. Jessica Thérèse
(18, 4, 15.7),  -- DRABO N. Kadidia Yasmine
(19, 4, 11.1),  -- ILBOUDO B. Jules
(20, 4, 16.9),  -- KABORE Alida
(21, 4, 9.8),   -- KANTAGBA L. Oumarou
(22, 4, 12.3),  -- KAMBELE Oumarou
(23, 4, 15.2),  -- KIEMDE Lucien
(24, 4, 10.8),  -- KONFE Adama
(25, 4, 12.0),  -- NABA Albert
(26, 4, 14.1),  -- OUANGA Robert
(27, 4, 12.4),  -- OUEDRAOGO Elodie
(28, 4, 9.8),   -- SAVADOGO Souhayouba
(29, 4, 16.8),  -- SAWADOGO Khaled
(30, 4, 11.2),  -- SAWADOGO Nathanael
(31, 4, 6.9),   -- SOME Elvis
(32, 4, 9.9),   -- SOULAMA Joel
(33, 4, 14.0),  -- SOULI Anicet
(34, 4, 12.9),  -- TRAORE Emmanuel
(35, 4, 11.9),  -- TRAORE Sié Donald
(36, 4, 7.5),   -- YAMEOGO Hermane
-- Notes Base de Données MIAGE L3 (avec TRAORE Yaya)
(8, 6, 12.2),   -- COMPAORE W. Aubin
(9, 6, 11.9),   -- CONGOMBO Soumaila
(10, 6, 13.8),  -- COULIBALY Wilfried Patrick
(11, 6, 11.8),  -- DARGA Sokuma Noël
(12, 6, 9.5),   -- DARGANI Charlène Olivia
(13, 6, 6.9),   -- DIALLO Belco
(14, 6, 13.2),  -- DIALLO Tasséré
(15, 6, 11.8),  -- DIAWARA M. Fadel
(16, 6, 12.4),  -- DICKO Alou
(17, 6, 12.7),  -- DJEKOMPTE M. Jessica Thérèse
(18, 6, 13.7),  -- DRABO N. Kadidia Yasmine
(19, 6, 13.1),  -- ILBOUDO B. Jules
(20, 6, 14.9),  -- KABORE Alida
(21, 6, 11.8),  -- KANTAGBA L. Oumarou
(22, 6, 14.3),  -- KAMBELE Oumarou
(23, 6, 13.2),  -- KIEMDE Lucien
(24, 6, 12.8),  -- KONFE Adama
(25, 6, 14.0),  -- NABA Albert
(26, 6, 12.1),  -- OUANGA Robert
(27, 6, 14.4),  -- OUEDRAOGO Elodie
(28, 6, 11.8),  -- SAVADOGO Souhayouba
(29, 6, 14.8),  -- SAWADOGO Khaled
(30, 6, 12.2),  -- SAWADOGO Nathanael
(31, 6, 7.9),   -- SOME Elvis
(32, 6, 11.9),  -- SOULAMA Joel
(33, 6, 12.0),  -- SOULI Anicet
(34, 6, 14.9),  -- TRAORE Emmanuel
(35, 6, 12.9),  -- TRAORE Sié Donald
(36, 6, 8.5),   -- YAMEOGO Hermane
-- Notes ACOO MIAGE L3 (avec TRAORE Yaya)
(8, 7, 13.2),   -- COMPAORE W. Aubin
(9, 7, 12.9),   -- CONGOMBO Soumaila
(10, 7, 14.8),  -- COULIBALY Wilfried Patrick
(11, 7, 12.8),  -- DARGA Sokuma Noël
(12, 7, 10.5),  -- DARGANI Charlène Olivia
(13, 7, 8.9),   -- DIALLO Belco
(14, 7, 14.2),  -- DIALLO Tasséré
(15, 7, 13.8),  -- DIAWARA M. Fadel
(16, 7, 11.4),  -- DICKO Alou
(17, 7, 15.7),  -- DJEKOMPTE M. Jessica Thérèse
(18, 7, 16.7),  -- DRABO N. Kadidia Yasmine
(19, 7, 14.1),  -- ILBOUDO B. Jules
(20, 7, 17.9),  -- KABORE Alida
(21, 7, 12.8),  -- KANTAGBA L. Oumarou
(22, 7, 15.3),  -- KAMBELE Oumarou
(23, 7, 16.2),  -- KIEMDE Lucien
(24, 7, 13.8),  -- KONFE Adama
(25, 7, 15.0),  -- NABA Albert
(26, 7, 14.1),  -- OUANGA Robert
(27, 7, 15.4),  -- OUEDRAOGO Elodie
(28, 7, 12.8),  -- SAVADOGO Souhayouba
(29, 7, 17.8),  -- SAWADOGO Khaled
(30, 7, 13.2),  -- SAWADOGO Nathanael
(31, 7, 9.9),   -- SOME Elvis
(32, 7, 12.9),  -- SOULAMA Joel
(33, 7, 14.0),  -- SOULI Anicet
(34, 7, 15.9),  -- TRAORE Emmanuel
(35, 7, 13.9),  -- TRAORE Sié Donald
(36, 7, 10.5);  -- YAMEOGO Hermane

-- Insertion des périodes de réclamation
INSERT INTO periodes_reclamation (nom, date_debut, date_fin, active, createur_id, description, date_creation) VALUES
('Réclamations Semestre 1 - 2024', '2024-01-15 08:00:00', '2024-01-18 17:00:00', true, 7, 'Période de réclamation pour les notes du premier semestre', CURRENT_TIMESTAMP);

-- Insertion des demandes de réclamation
INSERT INTO demandes (etudiant_id, note_id, description, statut, date_creation, date_derniere_action) VALUES
(1, 1, 'Je pense que ma note en Systèmes d''Information est incorrecte. J''ai bien répondu aux questions sur UML.', 'SOUMISE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(1, 3, 'Ma note en Techniques de Compilation me semble trop basse par rapport à mon travail rendu.', 'TRANSMISE_DA', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(2, 4, 'Erreur dans la correction de mon examen de Comptabilité Générale.', 'IMPUTEE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Mise à jour de la demande imputée
UPDATE demandes SET enseignant_impute_id = 4 WHERE id = 3;