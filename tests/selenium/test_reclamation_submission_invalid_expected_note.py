import unittest

from selenium.webdriver.common.by import By
from selenium.webdriver.support import expected_conditions as EC

try:
    from .common import (
        BaseSeleniumTest,
        FIXTURE_FILE,
        VALID_ETUDIANT_PASSWORD,
        ensure_active_reclamation_period,
        find_student_with_reclaimable_note,
    )
except ImportError:
    from common import (
        BaseSeleniumTest,
        FIXTURE_FILE,
        VALID_ETUDIANT_PASSWORD,
        ensure_active_reclamation_period,
        find_student_with_reclaimable_note,
    )


class TestReclamationSubmissionInvalidExpectedNote(BaseSeleniumTest):
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

    def test_invalid_expected_note_values_show_error_and_block_submission(self):
        form = self._open_submission_modal()

        description = form.find_element(By.CSS_SELECTOR, "textarea")
        description.clear()
        description.send_keys("Test Selenium - note attendue invalide")

        file_input = form.find_element(By.CSS_SELECTOR, "input[type='file']")
        file_input.send_keys(str(FIXTURE_FILE.resolve()))

        expected_note = form.find_element(By.CSS_SELECTOR, "input[type='number']")
        submit_btn = form.find_element(By.XPATH, ".//button[contains(., 'Soumettre la reclamation')]")

        invalid_values = ["-1", "20.5", "21", "100", "999.99"]
        for value in invalid_values:
            with self.subTest(note_attendue=value):
                expected_note.clear()
                expected_note.send_keys(value)

                error = self.wait.until(
                    EC.visibility_of_element_located(
                        (By.XPATH, "//p[contains(@class,'field-error') and contains(., 'comprise entre 0 et 20')]")
                    )
                )
                self.assertTrue(error.is_displayed(), f"Erreur attendue pour la valeur {value}")
                self.assertFalse(submit_btn.is_enabled(), f"Soumission doit etre bloquee pour {value}")


if __name__ == "__main__":
    unittest.main(verbosity=2)

