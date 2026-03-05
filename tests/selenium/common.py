"""
Shared Selenium helpers for UI workflows.
"""

from __future__ import annotations

import json
import os
import time
import unittest
from datetime import datetime, timedelta
from pathlib import Path
from typing import Iterable
from urllib import error, request

from selenium import webdriver
from selenium.webdriver.common.by import By
from selenium.webdriver.chrome.options import Options
from selenium.webdriver.support import expected_conditions as EC
from selenium.webdriver.support.ui import WebDriverWait

UI_BASE_URL = os.getenv("UI_BASE_URL", "http://localhost:3000")
API_BASE_URL = os.getenv("API_BASE_URL", "http://localhost:8080")
LOGIN_URL = f"{UI_BASE_URL}/"
WAIT_TIMEOUT = int(os.getenv("SELENIUM_WAIT_TIMEOUT", "12"))

VALID_ETUDIANT_EMAIL = os.getenv("SELENIUM_ETUDIANT_EMAIL", "joel.soulama@ibam.ma")
VALID_ETUDIANT_PASSWORD = os.getenv("SELENIUM_ETUDIANT_PASSWORD", "password123")
VALID_ENSEIGNANT_EMAIL = os.getenv("SELENIUM_ENSEIGNANT_EMAIL", "yaya.traore@ibam.ma")
VALID_ENSEIGNANT_PASSWORD = os.getenv("SELENIUM_ENSEIGNANT_PASSWORD", "password123")
VALID_DA_EMAIL = os.getenv("SELENIUM_DA_EMAIL", "rachid.bennani@ibam.ma")
VALID_DA_PASSWORD = os.getenv("SELENIUM_DA_PASSWORD", "password123")
VALID_SCOLARITE_EMAIL = os.getenv("SELENIUM_SCOLARITE_EMAIL", "omar.tazi@ibam.ma")
VALID_SCOLARITE_PASSWORD = os.getenv("SELENIUM_SCOLARITE_PASSWORD", "password123")

FIXTURE_FILE = Path(__file__).resolve().parent / "fixtures" / "justificatif_test.pdf"
DEFAULT_UPLOAD_CONTENT_TYPE = "application/pdf"

STUDENT_CANDIDATES_FOR_SETUP = [
    "jean.dupont@ibam.ma",
    "marie.martin@ibam.ma",
    "test.bloquage@ibam.ma",
    "aubin.compaore@ibam.ma",
    "soumaila.congombo@ibam.ma",
    "wilfried.coulibaly@ibam.ma",
    "noel.darga@ibam.ma",
    "belco.diallo@ibam.ma",
    "nathanael.sawadogo@ibam.ma",
    "joel.soulama@ibam.ma",
]


def build_driver() -> webdriver.Chrome:
    options = Options()
    headless = os.getenv("SELENIUM_HEADLESS", "0").lower() in {"1", "true", "yes"}
    if headless:
        options.add_argument("--headless=new")
    options.add_argument("--window-size=1400,1000")
    options.add_argument("--no-sandbox")
    options.add_argument("--disable-dev-shm-usage")
    return webdriver.Chrome(options=options)


def _api_call(
    path: str,
    method: str = "GET",
    token: str | None = None,
    payload: dict | None = None,
    raw_data: bytes | None = None,
    extra_headers: dict | None = None,
):
    url = f"{API_BASE_URL}{path}"
    headers = {}
    if token:
        headers["Authorization"] = f"Bearer {token}"
    if extra_headers:
        headers.update(extra_headers)

    data = None
    if raw_data is not None:
        data = raw_data
    elif payload is not None:
        headers["Content-Type"] = "application/json"
        data = json.dumps(payload).encode("utf-8")

    req = request.Request(url=url, data=data, headers=headers, method=method)
    try:
        with request.urlopen(req, timeout=20) as response:
            body = response.read().decode("utf-8")
            if not body:
                return None
            try:
                return json.loads(body)
            except json.JSONDecodeError:
                return body
    except error.HTTPError as exc:
        raw = exc.read().decode("utf-8")
        detail = raw
        try:
            detail_json = json.loads(raw)
            detail = detail_json.get("message") or detail_json
        except Exception:
            pass
        raise RuntimeError(f"API {method} {path} failed ({exc.code}): {detail}") from exc


def api_login(email: str, password: str) -> str:
    data = _api_call(
        "/api/auth/login",
        method="POST",
        payload={"email": email, "password": password},
    )
    token = (data or {}).get("token")
    if not token:
        raise RuntimeError("Token JWT introuvable dans la reponse de login")
    return token


def ensure_active_reclamation_period() -> dict:
    token = api_login(VALID_DA_EMAIL, VALID_DA_PASSWORD)
    current = _api_call("/api/periodes/active", token=token)
    if current and current.get("active"):
        return current

    now = datetime.now()
    start = now - timedelta(seconds=30)
    end = now + timedelta(hours=2)
    payload = {
        "nom": f"Periode Selenium {now.strftime('%Y-%m-%d %H:%M:%S')}",
        "dateDebut": start.strftime("%Y-%m-%dT%H:%M:%S"),
        "dateFin": end.strftime("%Y-%m-%dT%H:%M:%S"),
        "description": "Periode creee automatiquement pour tests Selenium",
    }

    try:
        return _api_call("/api/periodes", method="POST", token=token, payload=payload)
    except RuntimeError:
        # If a parallel run created one, retry active lookup once.
        time.sleep(1)
        current_retry = _api_call("/api/periodes/active", token=token)
        if current_retry and current_retry.get("active"):
            return current_retry
        raise


def _build_multipart_body(fields: dict, files: Iterable[tuple[str, str, str, bytes]]) -> tuple[str, bytes]:
    boundary = f"----SeleniumBoundary{int(time.time() * 1000)}"
    chunks = []

    for key, value in fields.items():
        chunks.extend(
            [
                f"--{boundary}\r\n".encode(),
                f'Content-Disposition: form-data; name="{key}"\r\n\r\n'.encode(),
                f"{value}\r\n".encode(),
            ]
        )

    for field_name, filename, content_type, file_bytes in files:
        chunks.extend(
            [
                f"--{boundary}\r\n".encode(),
                (
                    f'Content-Disposition: form-data; name="{field_name}"; '
                    f'filename="{filename}"\r\n'
                ).encode(),
                f"Content-Type: {content_type}\r\n\r\n".encode(),
                file_bytes,
                b"\r\n",
            ]
        )

    chunks.append(f"--{boundary}--\r\n".encode())
    return boundary, b"".join(chunks)


def _create_reclamation_for_student_note(student_token: str, note_id: int, description: str, note_attendue: float) -> dict:
    file_bytes = FIXTURE_FILE.read_bytes()
    boundary, body = _build_multipart_body(
        fields={
            "noteId": str(note_id),
            "description": description,
            "noteAttendue": str(note_attendue),
        },
        files=[
            (
                "justificatif",
                FIXTURE_FILE.name,
                DEFAULT_UPLOAD_CONTENT_TYPE,
                file_bytes,
            )
        ],
    )
    return _api_call(
        "/api/reclamations",
        method="POST",
        token=student_token,
        raw_data=body,
        extra_headers={"Content-Type": f"multipart/form-data; boundary={boundary}"},
    )


def ensure_imputee_for_enseignant(enseignant_email: str) -> dict:
    ensure_active_reclamation_period()

    da_token = api_login(VALID_DA_EMAIL, VALID_DA_PASSWORD)
    scolarite_token = api_login(VALID_SCOLARITE_EMAIL, VALID_SCOLARITE_PASSWORD)

    enseignants = _api_call("/api/reclamations/enseignants", token=da_token) or []
    enseignant = next((e for e in enseignants if e.get("email") == enseignant_email), None)
    if enseignant is None:
        raise RuntimeError(f"Enseignant introuvable pour email {enseignant_email}")

    teacher_id = enseignant["id"]
    teacher_nom = (enseignant.get("nom") or "").strip().lower()
    teacher_prenom = (enseignant.get("prenom") or "").strip().lower()

    existing_demands = _api_call("/api/reclamations", token=da_token) or []
    for demand in existing_demands:
        if demand.get("statut") == "IMPUTEE" and demand.get("enseignantImputeId") == teacher_id:
            return demand

    created_demande = None
    for student_email in STUDENT_CANDIDATES_FOR_SETUP:
        try:
            student_token = api_login(student_email, VALID_ETUDIANT_PASSWORD)
        except Exception:
            continue

        notes = _api_call("/api/notes/mes-notes", token=student_token) or []
        student_demands = _api_call("/api/reclamations", token=student_token) or []
        existing_note_ids = {d.get("noteId") for d in student_demands}

        yaya_notes = [
            n
            for n in notes
            if (n.get("enseignantNom") or "").strip().lower() == teacher_nom
            and (n.get("enseignantPrenom") or "").strip().lower() == teacher_prenom
            and n.get("id") not in existing_note_ids
        ]
        if not yaya_notes:
            continue

        note = yaya_notes[0]
        created_demande = _create_reclamation_for_student_note(
            student_token=student_token,
            note_id=note["id"],
            description=f"Preparation analyse Selenium {int(time.time())}",
            note_attendue=15.0,
        )
        break

    if created_demande is None:
        raise RuntimeError("Impossible de creer une nouvelle reclamation imputable pour l'enseignant cible")

    demande_id = created_demande["id"]
    _api_call(f"/api/reclamations/{demande_id}/verifier?recevable=true", method="PUT", token=scolarite_token)
    _api_call(f"/api/reclamations/{demande_id}/imputer-auto", method="PUT", token=da_token)

    refreshed = _api_call(f"/api/reclamations/{demande_id}", token=da_token)
    if refreshed.get("statut") != "IMPUTEE":
        raise RuntimeError(f"La demande preparee n'est pas en statut IMPUTEE (statut actuel: {refreshed.get('statut')})")
    return refreshed


def find_student_with_reclaimable_note() -> str:
    ensure_active_reclamation_period()

    for student_email in STUDENT_CANDIDATES_FOR_SETUP:
        try:
            student_token = api_login(student_email, VALID_ETUDIANT_PASSWORD)
        except Exception:
            continue

        notes = _api_call("/api/notes/mes-notes", token=student_token) or []
        if not notes:
            continue

        student_demands = _api_call("/api/reclamations", token=student_token) or []
        existing_note_ids = {d.get("noteId") for d in student_demands}
        reclaimable = [n for n in notes if n.get("id") not in existing_note_ids]
        if reclaimable:
            return student_email

    raise RuntimeError("Aucun etudiant avec note reclamable disponible")


class BaseSeleniumTest(unittest.TestCase):
    def setUp(self):
        self.driver = build_driver()
        self.wait = WebDriverWait(self.driver, WAIT_TIMEOUT)
        self.open_login_page()

    def tearDown(self):
        self.driver.quit()

    def open_login_page(self):
        self.driver.get(LOGIN_URL)
        self.wait.until(EC.presence_of_element_located((By.CSS_SELECTOR, "input[type='email']")))
        self.driver.execute_script("window.localStorage.clear(); window.sessionStorage.clear();")
        self.driver.delete_all_cookies()
        self.driver.get(LOGIN_URL)

    def login(self, email: str, password: str):
        email_input = self.wait.until(EC.presence_of_element_located((By.CSS_SELECTOR, "input[type='email']")))
        password_input = self.driver.find_element(By.CSS_SELECTOR, "input[type='password']")
        submit = self.driver.find_element(By.CSS_SELECTOR, "button[type='submit']")

        email_input.clear()
        email_input.send_keys(email)
        password_input.clear()
        password_input.send_keys(password)
        submit.click()

    def wait_dashboard(self):
        self.wait.until(EC.url_contains("dashboard"))

    def click_tab_contains(self, text_part: str):
        tab_buttons = self.wait.until(EC.presence_of_all_elements_located((By.CSS_SELECTOR, ".tabs button")))
        target = None
        for button in tab_buttons:
            if text_part.lower() in button.text.lower():
                target = button
                break
        if target is None:
            raise AssertionError(f"Onglet introuvable avec le texte: {text_part}")
        target.click()

    def logout(self):
        logout_btn = self.wait.until(
            EC.element_to_be_clickable(
                (By.XPATH, "//button[contains(., 'Déconnexion') or contains(., 'Deconnexion')]")
            )
        )
        logout_btn.click()
        self.wait.until(EC.presence_of_element_located((By.CSS_SELECTOR, "input[type='email']")))
