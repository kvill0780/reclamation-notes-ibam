import unittest

from selenium.webdriver.common.by import By
from selenium.webdriver.support import expected_conditions as EC

try:
    from .common import BaseSeleniumTest, UI_BASE_URL
except ImportError:
    from common import BaseSeleniumTest, UI_BASE_URL


class TestDashboardRequiresLogin(BaseSeleniumTest):
    def _assert_redirected_to_login(self, protected_path: str):
        self.driver.get(f"{UI_BASE_URL}{protected_path}")
        self.wait.until(
            lambda d: "/login" in d.current_url.lower()
            and len(d.find_elements(By.CSS_SELECTOR, "input[type='email']")) > 0
        )
        self.assertIn("/login", self.driver.current_url.lower())
        self.assertTrue(
            self.driver.find_element(By.CSS_SELECTOR, "input[type='email']").is_displayed(),
            "Le formulaire de connexion doit etre visible",
        )

    def test_cannot_access_protected_routes_without_login(self):
        protected_routes = [
            "/etudiant/dashboard",
            "/enseignant/dashboard",
            "/scolarite/dashboard",
            "/da/dashboard",
            "/da/periodes",
        ]

        for route in protected_routes:
            with self.subTest(route=route):
                self.open_login_page()
                self._assert_redirected_to_login(route)


if __name__ == "__main__":
    unittest.main(verbosity=2)

