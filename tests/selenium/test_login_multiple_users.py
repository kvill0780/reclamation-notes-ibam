import unittest

try:
    from .common import BaseSeleniumTest
except ImportError:
    from common import BaseSeleniumTest


class TestLoginMultipleUsers(BaseSeleniumTest):
    def _assert_login_success(self, email: str, password: str, expected_path: str):
        self.login(email, password)
        self.wait_dashboard()
        self.assertIn(expected_path, self.driver.current_url.lower())

    def test_login_user_1_etudiant_jean(self):
        self._assert_login_success("jean.dupont@ibam.ma", "password123", "/etudiant/dashboard")

    def test_login_user_2_etudiant_marie(self):
        self._assert_login_success("marie.martin@ibam.ma", "password123", "/etudiant/dashboard")

    def test_login_user_3_enseignant_ahmed(self):
        self._assert_login_success("ahmed.benali@ibam.ma", "password123", "/enseignant/dashboard")

    def test_login_user_4_scolarite_omar(self):
        self._assert_login_success("omar.tazi@ibam.ma", "password123", "/scolarite/dashboard")

    def test_login_user_5_da_rachid(self):
        self._assert_login_success("rachid.bennani@ibam.ma", "password123", "/da/dashboard")


if __name__ == "__main__":
    unittest.main(verbosity=2)
