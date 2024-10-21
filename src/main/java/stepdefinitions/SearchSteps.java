package stepdefinitions;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import pages.SearchPage;
import org.junit.Assert;
import java.util.concurrent.TimeUnit;

public class SearchSteps {
    private WebDriver driver;
    private SearchPage searchPage;
    private static final String GOOGLE_URL = "https://www.google.com";
    private static final int NEXT_PAGE_VALUE = 2;
    private static final int PREVIOUS_PAGE_VALUE = 1;
    private String languageCode = "en"; // Default to English
    @Before
    public void setUpBrowser() {
        setupChromeDriverWithLanguage(languageCode);
    }
    private void setupChromeDriverWithLanguage(String languageCode) {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--lang=" + languageCode);
        driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        driver.manage().window().maximize();
        searchPage = new SearchPage(driver);
    }

    @Given("user launches the search web application")
    public void user_launches_the_search_web_application() {
        driver.get(GOOGLE_URL);
    }
    @When("user searches for {string}")
    public void user_searches_for(String searchTerm) {
        searchPage.enterSearchTerm(searchTerm);
        Assert.assertTrue("Search box should contain the query",
                searchPage.searchBox.getAttribute("value").equals(searchTerm));
    }
    @When("clicks on search button")
    public void clicks_on_search_button() {
        searchPage.submitSearch();
    }
    @Then("results retrieved should contain {string}")
    public void results_retrieved_should_contain(String expectedResult) {
        boolean isResultPresent = driver.getPageSource().contains(expectedResult);
        Assert.assertTrue("Expected result not found in search results.", isResultPresent);
    }
    @And("the search suggestion list is displayed")
    public void searchSuggestionListIsDisplayed() {
        searchPage.waitForVisibilityOfSuggestionList();
    }
    @Then("the user should be able to select an option from the suggestion list")
    public void userSelectsOptionFromSuggestionList() {
        String expectedSuggestion = "selenium webdriver";
        searchPage.selectSuggestion(expectedSuggestion);

        Assert.assertTrue("Search box should contain the selected suggestion",
                searchPage.searchBox.getAttribute("value").contains(expectedSuggestion));
    }
    @When("the user clicks on the Next button")
    public void the_user_clicks_on_the_next_button() {
            searchPage.clickNextButton();
    }
    @When("the user clicks on the Previous button")
    public void the_user_clicks_on_the_previous_button() {
        searchPage.clickPreviousButton();
    }
    @Then("the user should be navigated to the next page of results")
    public void userNavigatesToNextPage() {
        Assert.assertTrue("User should be navigated to the next page",
                searchPage.isOnNextPage(NEXT_PAGE_VALUE));
    }
    @Then("the user should be navigated to the previous page of results")
    public void userNavigatesToPreviousPage() {
        Assert.assertTrue("User should be navigated to the previous page",
                searchPage.isOnPreviousPage(PREVIOUS_PAGE_VALUE));
    }
    @Then("the correct number of results should be displayed on each page")
    public void verifyCorrectNumberOfResultsDisplayed() {
        Assert.assertTrue("No results should be displayed", searchPage.getSearchResultSize() > 0);
    }
    @Then("the correct number of results should be displayed on the previous page")
    public void the_correct_number_of_results_should_be_displayed_on_the_previous_page() {
        Assert.assertTrue("No results should be displayed on the previous page", searchPage.getSearchResultSize() > 0);
    }
    // Localization Test Steps
    @Given("the user switches the language to {string}")
    public void userSwitchesLanguage(String langCode) {
        this.languageCode = langCode;
        // Restart browser with the new language
        closeBrowser();
        setUpBrowser();
        driver.get(GOOGLE_URL);
    }
    @After
    public void closeBrowser() {
        if (driver != null) {
            driver.quit();
        }
    }
}
