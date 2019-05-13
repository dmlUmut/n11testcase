@basket
Feature: Basket

  Scenario: 1 Verify the product has been added to the basket
    Given User basket control basket.count
    When User tap on home.search
    When User search "iphone"
    When User search text control "iphone"
    And User close popup
    When User tap on search.firstItem
    And User close popup
    When User add basket item.addBasket
    Then User should see basket.count displayed

  Scenario: 2 Verify the product amount
    When User tap on basket.button
    And User close popup
    When User control amount basket.amount
    When User tap on basket.payment
    Then User should see payment.confirm displayed