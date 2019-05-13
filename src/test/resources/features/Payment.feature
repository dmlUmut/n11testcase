@payment
Feature: Payment

  Scenario: 1 Verify the user selected credit card
    When User tap on payment.creditCard
    When User set text "Damla Seker" to creditCard.name
    When User set text "3456789612354556" to creditCard.cardNo
    When User tap on creditCard.exDate
    When User tap on creditCard.exDateSubmit
    When User set text "111" to creditCard.cvc
    When User tap on creditCard.contract
    When User tap on actionbar.leftMenu
    When User tap on actionbar.leftMenu
    When User tap on actionbar.leftMenu
    When User tap on actionbar.leftMenu
    When User should see home.banner displayed