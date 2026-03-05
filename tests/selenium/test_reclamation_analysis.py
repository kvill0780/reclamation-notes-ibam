import time
import unittest

from selenium.webdriver.common.by import By
from selenium.webdriver.support import expected_conditions as EC

try:
    from .common import (
        BaseSeleniumTest,
        VALID_ENSEIGNANT_EMAIL,
        VALID_ENSEIGNANT_PASSWORD,
        ensure_imputee_for_enseignant,
    )
except ImportError:
    from common import (
        BaseSeleniumTest,
        VALID_ENSEIGNANT_EMAIL,
        VALID_ENSEIGNANT_PASSWORD,
        ensure_imputee_for_enseignant,
    )


class TestReclamationAnalysis(BaseSeleniumTest):
    def _open_imputee_modal(self):
        ensure_imputee_for_enseignant(VALID_ENSEIGNANT_EMAIL)

        self.login(VALID_ENSEIGNANT_EMAIL, VALID_ENSEIGNANT_PASSWORD)
        self.wait_dashboard()

        self.click_tab_contains("analyser")
        self.wait.until(EC.presence_of_element_located((By.CSS_SELECTOR, ".reclamations-list")))

        if self.driver.find_elements(By.XPATH, "//*[contains(@class, 'empty') and contains(., 'Aucune')]"):
            self.skipTest("Aucune reclamation imputee disponible pour analyse")

        cards = self.driver.find_elements(By.CSS_SELECTOR, ".reclamation-card")
        if not cards:
            self.skipTest("Aucune carte de reclamation disponible")

        # Click the first card content to open detail modal.
        cards[0].find_element(By.CSS_SELECTOR, ".card-content").click()
        self.wait.until(EC.visibility_of_element_located((By.CSS_SELECTOR, ".modal")))
        return self.driver.find_element(By.CSS_SELECTOR, ".modal")

    def test_analysis_buttons_require_comment(self):
        modal = self._open_imputee_modal()

        reject_btn = modal.find_element(By.XPATH, ".//button[normalize-space()='Refuser']")
        accept_btn = modal.find_element(By.XPATH, ".//button[contains(@class, 'btn-primary') and normalize-space()='Accepter']")
        self.assertFalse(reject_btn.is_enabled(), "Refuser doit etre desactive sans commentaire")
        self.assertFalse(accept_btn.is_enabled(), "Accepter doit etre desactive sans commentaire")

        comment_input = self.wait.until(
            EC.presence_of_element_located((By.CSS_SELECTOR, ".modal input[placeholder*='Commentaire']"))
        )
        comment_input.clear()
        comment_input.send_keys(f"Commentaire obligatoire Selenium {int(time.time())}")
        self.wait.until(lambda d: reject_btn.is_enabled() and accept_btn.is_enabled())

    def test_enseignant_can_accept_imputee_reclamation(self):
        modal = self._open_imputee_modal()

        comment_input = self.wait.until(
            EC.presence_of_element_located((By.CSS_SELECTOR, ".modal input[placeholder*='Commentaire']"))
        )
        comment_input.clear()
        comment_input.send_keys(f"Acceptation Selenium {int(time.time())}")

        accept_btn = modal.find_element(
            By.XPATH,
            ".//button[contains(@class,'btn-primary') and contains(., 'Accepter')]",
        )
        self.wait.until(lambda d: accept_btn.is_enabled())
        accept_btn.click()

        self.wait.until(EC.invisibility_of_element_located((By.CSS_SELECTOR, ".modal-overlay")))
        success_box = self.wait.until(
            EC.visibility_of_element_located((By.CSS_SELECTOR, ".notification-success"))
        )
        self.assertTrue(success_box.text.strip(), "Une notification de succes est attendue")

    def test_enseignant_can_refuse_imputee_reclamation(self):
        modal = self._open_imputee_modal()

        comment_input = self.wait.until(
            EC.presence_of_element_located((By.CSS_SELECTOR, ".modal input[placeholder*='Commentaire']"))
        )
        comment_input.clear()
        comment_input.send_keys(f"Refus Selenium {int(time.time())}")

        reject_btn = modal.find_element(By.XPATH, ".//button[normalize-space()='Refuser']")
        self.wait.until(lambda d: reject_btn.is_enabled())
        reject_btn.click()

        self.wait.until(EC.invisibility_of_element_located((By.CSS_SELECTOR, ".modal-overlay")))
        success_box = self.wait.until(
            EC.visibility_of_element_located((By.CSS_SELECTOR, ".notification-success"))
        )
        self.assertTrue(success_box.text.strip(), "Une notification de succes est attendue")


if __name__ == "__main__":
    unittest.main(verbosity=2)
