package pages;

import org.junit.Assert;
import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import utils.WebDriverUtils;
import java.util.List;

public class SearchPage {
    private static final int SHORT_WAIT_TIME = 5;
    protected WebDriver driver;
    protected WebDriverUtils webDriverUtils;
    @FindBy(name = "q")
    public WebElement searchBox;
    @FindBy(name = "btnK")
    public WebElement searchButton;

    @FindBy(xpath = "//ul[@role='listbox']//li")
    protected List<WebElement> suggestionList;

    @FindBy(xpath = "//a[@id='pnnext']")
    protected WebElement nextButton;

    @FindBy(xpath = "//a[@id='pnprev']")
    protected WebElement previousButton;

    @FindBy(xpath = "//div[@id='search']//div[contains(@class,'g')]")
    protected List<WebElement> searchResults;

    @FindBy(xpath = "//p[@role='heading']")
    protected WebElement invalidSearchMsg;

    public SearchPage(WebDriver driver) {
        this.driver = driver;
        this.webDriverUtils = new WebDriverUtils(driver);
        PageFactory.initElements(driver, this);
    }

    public void enterSearchTerm(String term) {
        searchBox.sendKeys(term);
    }

    public void submitSearch() {
        try {
            webDriverUtils.waitForElementToBeClickable(searchButton);
            searchButton.click();
        } catch (Exception e) {
            System.out.println("Search button click failed. Trying Keys.ENTER. Exception: " + e.getMessage());
            searchBox.sendKeys(Keys.ENTER);
        }
    }

    public void waitForVisibilityOfSuggestionList() {
        for (WebElement suggestion : suggestionList) {
            webDriverUtils.waitForVisibility(suggestion);
        }
    }

    public void selectSuggestion(String expectedSuggestion) {
        boolean suggestionFound = false;
        webDriverUtils.pause(SHORT_WAIT_TIME);
        for (WebElement suggestion : suggestionList) {
            String suggestionText = suggestion.getText();
            if (suggestionText.contains(expectedSuggestion)) {
                suggestion.click();
                suggestionFound = true;
                break;
            }
        }
        Assert.assertTrue("The suggestion '" + expectedSuggestion + "' was not found in the suggestion list", suggestionFound);
    }

    public void clickNextButton() {
        webDriverUtils.waitForElementToBeClickable(nextButton);
        webDriverUtils.scrollToElement(nextButton);
        nextButton.click();
    }

    public void clickPreviousButton() {
        clickNextButton();
        if (webDriverUtils.isElementVisible(previousButton)) {
            webDriverUtils.waitForElementToBeClickable(previousButton);
            webDriverUtils.scrollToElement(previousButton);
            previousButton.click();
        } else {
            throw new NoSuchElementException("Previous button not visible after clicking next");
        }
    }

    public boolean isOnPage(int targetPageNumber) {
        String currentPage = driver.getCurrentUrl();
        int resultsPerPage = 10;
        int expectedStartValue = (targetPageNumber - 1) * resultsPerPage;
        if (currentPage.contains("start=")) {
            String startParam = currentPage.split("start=")[1].split("&")[0];
            int startValue = Integer.parseInt(startParam);
            return startValue == expectedStartValue;
        } else if (targetPageNumber == 1 && !currentPage.contains("start=")) {
            return true;
        }
        return false;
    }

    public boolean isOnNextPage(int expectedStartValue) {
        return isOnPage(expectedStartValue);
    }

    public boolean isOnPreviousPage(int expectedStartValue) {
        return isOnPage(expectedStartValue);
    }

    public int getSearchResultSize() {
        return searchResults.size();
    }
}
