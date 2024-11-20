Feature: Login Validation for HRMs Portal

  Background:
     #Given user is able to access HRMS portal

  @invalidLogin
  Scenario Outline: Login with an incomplete or incorrect credentials
    When user attempts to login with an incorrect or invalid "<userName>" or "<password>"
    And user clicks on login button
    Then user sees clearly visible error message "<errorMessage>"

    Examples:
      | userName | password      | errorMessage             |
      |          | Hum@nhrm123   | Username cannot be empty |
      | admin    |               | Password is empty        |
      | admi     | Hum@nhrm123   | Invalid credentials      |
      | admin    | Hum@nhrm12345 | Invalid credentials      |

  @validLogin
  Scenario Outline: Successful login after invalid attempt
    When user attempts to login with an incorrect or invalid "<userName>" or "<password>"
    And user clicks on login button
    Then user sees clearly visible error message "<errorMessage>"
    And user is allowed to enter valid credentials
    Examples:
      | userName | password    | errorMessage        |
      | admi     | Hum@nhrm123 | Invalid credentials |








