import unittest

try:
    from .common import BaseSeleniumTest, VALID_ETUDIANT_EMAIL, VALID_ETUDIANT_PASSWORD
except ImportError:
    from common import BaseSeleniumTest, VALID_ETUDIANT_EMAIL, VALID_ETUDIANT_PASSWORD


class TestLoginValidCredentials(BaseSeleniumTest):
    def test_login_with_valid_credentials_redirects_to_dashboard(self):
        self.login(VALID_ETUDIANT_EMAIL, VALID_ETUDIANT_PASSWORD)
        self.wait_dashboard()
        self.assertIn("dashboard", self.driver.current_url.lower())


if __name__ == "__main__":
    unittest.main(verbosity=2)
