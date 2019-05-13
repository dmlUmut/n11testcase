@appLogout
Feature: AppLogout

  Scenario: 1 Verify the user logged out
    When User tap on actionbar.leftMenu
    When User tap on user.email
    When User tap on menu.list where text "Hesap Ayarlarım"
    When User tap on account.logout
    When User tap on account.popupLogout
    When User tap on actionbar.leftMenu
    Then User should see login.signin displayed

