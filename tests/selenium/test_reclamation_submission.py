import time
import unittest

from selenium.webdriver.common.by import By
from selenium.webdriver.support import expected_conditions as EC

try:
    from .common import (
        BaseSeleniumTest,
        FIXTURE_FILE,
        VALID_ETUDIANT_PASSWORD,
        find_student_with_reclaimable_note,
        ensure_active_reclamation_period,
    )
except ImportError:
    from common import (
        BaseSeleniumTest,
        FIXTURE_FILE,
        VALID_ETUDIANT_PASSWORD,
        find_student_with_reclaimable_note,
        ensure_active_reclamation_period,
    )


class TestReclamationSubmission(BaseSeleniumTest):
    @classmethod
    def setUpClass(cls):
        super().setUpClass()
        ensure_active_reclamation_period()
        try:
            cls.student_email = find_student_with_reclaimable_note()
        except RuntimeError as exc:
            raise unittest.SkipTest(str(exc))

    def _open_submission_modal(self):
        self.login(self.student_email, VALID_ETUDIANT_PASSWORD)
        self.wait_dashboard()

        self.click_tab_contains("Mes notes")
        self.wait.until(EC.presence_of_element_located((By.CSS_SELECTOR, ".notes-table")))

        reclaim_buttons = self.driver.find_elements(
            By.XPATH, "//button[normalize-space()='Reclamer' or normalize-space()='Réclamer']"
        )
        enabled_buttons = [btn for btn in reclaim_buttons if btn.is_enabled()]
        if not enabled_buttons:
            self.skipTest("Aucune note reclamable disponible pour ce compte")

        enabled_buttons[0].click()
        self.wait.until(EC.visibility_of_element_located((By.XPATH, "//h3[contains(., 'Nouvelle réclamation')]")))
        return self.driver.find_element(By.CSS_SELECTOR, ".reclamation-create-form")

    def test_submit_button_disabled_when_form_is_empty(self):
        form = self._open_submission_modal()
        submit_btn = form.find_element(By.XPATH, ".//button[contains(., 'Soumettre la reclamation')]")
        self.assertFalse(submit_btn.is_enabled(), "Le bouton doit etre desactive tant que le formulaire est incomplet")

    def test_invalid_expected_note_shows_validation_error(self):
        form = self._open_submission_modal()

        description = form.find_element(By.CSS_SELECTOR, "textarea")
        description.clear()
        description.send_keys(f"Validation Selenium {int(time.time())}")

        expected_note = form.find_element(By.CSS_SELECTOR, "input[type='number']")
        expected_note.clear()
        expected_note.send_keys("25")

        error = self.wait.until(
            EC.visibility_of_element_located(
                (By.XPATH, "//p[contains(@class,'field-error') and contains(., 'comprise entre 0 et 20')]")
            )
        )
        self.assertTrue(error.is_displayed(), "Le message de validation de note doit etre visible")
        submit_btn = form.find_element(By.XPATH, ".//button[contains(., 'Soumettre la reclamation')]")
        self.assertFalse(submit_btn.is_enabled(), "Le bouton doit rester desactive avec une note invalide")

    def test_submit_requires_attachment_even_with_valid_fields(self):
        form = self._open_submission_modal()

        description = form.find_element(By.CSS_SELECTOR, "textarea")
        description.clear()
        description.send_keys(f"Piece jointe requise Selenium {int(time.time())}")

        expected_note = form.find_element(By.CSS_SELECTOR, "input[type='number']")
        expected_note.clear()
        expected_note.send_keys("15")

        submit_btn = form.find_element(By.XPATH, ".//button[contains(., 'Soumettre la reclamation')]")
        self.assertFalse(
            submit_btn.is_enabled(),
            "Le bouton doit rester desactive si aucun justificatif n'est selectionne",
        )

    def test_student_can_submit_new_reclamation(self):
        form = self._open_submission_modal()

        description = form.find_element(By.CSS_SELECTOR, "textarea")
        description.clear()
        description.send_keys(f"Soumission Selenium {int(time.time())}")

        expected_note = form.find_element(By.CSS_SELECTOR, "input[type='number']")
        expected_note.clear()
        expected_note.send_keys("15")

        file_input = form.find_element(By.CSS_SELECTOR, "input[type='file']")
        file_input.send_keys(str(FIXTURE_FILE.resolve()))

        submit_btn = form.find_element(By.XPATH, ".//button[contains(., 'Soumettre la reclamation')]")
        self.wait.until(lambda d: submit_btn.is_enabled())
        submit_btn.click()

        self.wait.until(EC.invisibility_of_element_located((By.CSS_SELECTOR, ".modal-overlay")))
        success_box = self.wait.until(
            EC.visibility_of_element_located((By.CSS_SELECTOR, ".notification-success"))
        )
        self.assertTrue(success_box.text.strip(), "Une notification de succes est attendue")


if __name__ == "__main__":
    unittest.main(verbosity=2)
