@appLogin
Feature: AppLogin

  Scenario: 1 Verify this application can not login with incorrect password
    Given User see login.menu
    When User tap on login.menu
    When User tap on login.signin
    Then User should see login.image displayed
    When User login as "n11testkullanicisi@gmail.com" and "yanlissifre"
    Then User should see login.image displayed

  Scenario: 2 Verify this application can not login with incorrect password
    When User login as "n11testdenemekullanicisi@gmail.com" and "n11sifre"
    Then User should see login.image displayed

  Scenario: 3 Verify this correct user when the application is started
    When User login as "n11testkullanicisi@gmail.com" and "n11sifre"
    When User tap on login.menu
    Then User should not see login.image
    Then User should see user.email displayed