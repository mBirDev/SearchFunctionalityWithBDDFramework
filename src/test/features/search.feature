Feature: Google search functionality

  Background:
    Given user launches the search web application

  Scenario Outline: Search suggestions are displayed and selectable
    When user searches for "<query>"
    And the search suggestion list is displayed
    Then the user should be able to select an option from the suggestion list
    Examples:
      | query    |
      | selenium webdriver|

  Scenario Outline: Valid search returns relevant results
    When user searches for "<query>"
    And clicks on search button
    Then results retrieved should contain "<result>"
    Examples:
      | query    | result   |
      | selenium | selenium |

  Scenario Outline: Special characters in search term
    When user searches for "<specialQuery>"
    And clicks on search button
    Then results retrieved should contain "<expectedResult>"
    Examples:
      | specialQuery | expectedResult |
      | @selenium    | selenium       |
      | selenium!    | selenium       |
      | #testing     | testing        |

  Scenario Outline: User can navigate to the next page of search results
    When user searches for "<query>"
    And clicks on search button
    And the user clicks on the Next button
    Then the user should be navigated to the next page of results
    And the correct number of results should be displayed on each page
    Examples:
      | query    |
      | selenium |

  Scenario Outline: User can navigate back to the previous page of search results
    When user searches for "<query>"
    And clicks on search button
    And the user clicks on the Previous button
    Then the user should be navigated to the previous page of results
    And the correct number of results should be displayed on the previous page
    Examples:
      | query    |
      | selenium |

  Scenario Outline: Search results are displayed in the correct language (Chinese)
    Given the user switches the language to "<languageCode>"
    When user searches for "<query>"
    And clicks on search button
    Then results retrieved should contain "<result>"
    Examples:
      | languageCode | query    |  | result   |
      | zh           | selenium |  | selenium |


