import unittest

from selenium.webdriver.common.by import By
from selenium.webdriver.support import expected_conditions as EC

try:
    from .common import BaseSeleniumTest
except ImportError:
    from common import BaseSeleniumTest

INVALID_PASSWORD_TEST_EMAIL = "test.bloquage@ibam.ma"


class TestLoginInvalidCredentials(BaseSeleniumTest):
    def _assert_login_rejected_with_invalid_credentials_message(self):
        self.assertNotIn("dashboard", self.driver.current_url.lower())
        error_box = self.wait.until(EC.visibility_of_element_located((By.CSS_SELECTOR, ".error")))
        message = error_box.text.strip().lower()
        self.assertTrue(message, "Le message d'erreur doit etre affiche")
        self.assertTrue(
            ("identifiant" in message and "invalide" in message)
            or ("mot de passe" in message and "invalide" in message)
            or ("compte" in message and ("désactivé" in message or "desactive" in message)),
            f"Message inattendu: {message}"
        )

    def test_login_with_invalid_password_shows_error(self):
        self.login(INVALID_PASSWORD_TEST_EMAIL, "motdepasse_invalide")
        self._assert_login_rejected_with_invalid_credentials_message()

    def test_login_with_unknown_email_shows_error(self):
        self.login("inconnu+selenium@ibam.ma", "password123")
        self._assert_login_rejected_with_invalid_credentials_message()


if __name__ == "__main__":
    unittest.main(verbosity=2)
