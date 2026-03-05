# Tests - Plateforme Réclamations IBAM

## Prérequis

- Python 3.9+
- Google Chrome installé
- Selenium (Selenium Manager gère ChromeDriver automatiquement)

```bash
pip install selenium requests
```

Avant exécution:

- Backend actif sur `http://localhost:8080`
- Frontend actif sur `http://localhost:3000`

## Variables d'environnement utiles

```bash
export UI_BASE_URL=http://localhost:3000
export API_BASE_URL=http://localhost:8080
export SELENIUM_HEADLESS=1
```

Variables de comptes (optionnel):

- `SELENIUM_ETUDIANT_EMAIL` / `SELENIUM_ETUDIANT_PASSWORD`
- `SELENIUM_ENSEIGNANT_EMAIL` / `SELENIUM_ENSEIGNANT_PASSWORD`
- `SELENIUM_DA_EMAIL` / `SELENIUM_DA_PASSWORD`
- `SELENIUM_SCOLARITE_EMAIL` / `SELENIUM_SCOLARITE_PASSWORD`

## Suite Selenium organisée par scénario

```bash
# depuis la racine du projet
python3 -m unittest discover -s tests/selenium -p "test_*.py" -v

# fichiers individuels
python3 tests/selenium/test_login_valid_credentials.py
python3 tests/selenium/test_login_invalid_credentials.py
python3 tests/selenium/test_login_multiple_users.py
python3 tests/selenium/test_dashboard_requires_login.py
python3 tests/selenium/test_reclamation_submission.py
python3 tests/selenium/test_reclamation_submission_invalid_expected_note.py
python3 tests/selenium/test_reclamation_analysis.py
```

## Ce que couvrent les scénarios Selenium

- Login valide
- Login invalide (mot de passe ou email inconnu)
- Login multi-utilisateurs (5 comptes)
- Protection des routes dashboard sans authentification
- Soumission de réclamation (cas nominal + validations UI)
- Soumission avec notes attendues invalides
- Analyse enseignant (commentaire obligatoire, acceptation, refus)

## Particularités des helpers (`tests/selenium/common.py`)

- Création automatique d’une période active si nécessaire
- Sélection automatique d’un étudiant avec note réclamable
- Préparation automatique d’une réclamation `IMPUTEE` pour les tests d’analyse

## Scripts TP existants

```bash
python3 tests/exercice1_test_fonctionnel.py
python3 tests/exercice2_test_charge.py
python3 tests/exercice3_test_performance.py
python3 tests/exercice6_test_unitaire.py
python3 tests/test_api_validation_note.py
python3 tests/test_validation_note_selenium.py
```

## Arborescence

```text
tests/
├── README.md
├── exercice1_test_fonctionnel.py
├── exercice2_test_charge.py
├── exercice3_test_performance.py
├── exercice6_test_unitaire.py
├── test_api_validation_note.py
├── test_validation_note_selenium.py
├── selenium/
│   ├── common.py
│   ├── fixtures/
│   │   └── justificatif_test.pdf
│   ├── test_login_valid_credentials.py
│   ├── test_login_invalid_credentials.py
│   ├── test_login_multiple_users.py
│   ├── test_dashboard_requires_login.py
│   ├── test_reclamation_submission.py
│   ├── test_reclamation_submission_invalid_expected_note.py
│   └── test_reclamation_analysis.py
└── resultats/
```
