package stepdefinitions;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import cucumber.api.java.en.Given;
import io.appium.java_client.MobileElement;
import org.junit.Assert;
import org.openqa.selenium.*;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utility.Hooks;
import utility.User;

import java.io.IOException;
import java.util.*;

import static junit.framework.TestCase.assertTrue;
import static junit.framework.TestCase.fail;

public class Stepdef {
    private Hooks hooks;
    private Properties selectors;
    private WebDriver driver;
    private final int TAP_RETRY_COUNT = 4;
    private final int ELEMENT_LOAD_TIMEOUT = 30;

    static String productAmount = "";

    public Stepdef(Properties selectors, Hooks hooks) {
        this.hooks = hooks;
        this.selectors = selectors;

        try {
            selectors.load(Stepdef.class.getResourceAsStream("/selectors/selector.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getPropertyValue(String id) {
        Optional<String> opt = Optional.ofNullable(selectors.getProperty(id));
        if (opt.isPresent())
            return opt.get();
        else
            fail("Error in selectors.properties or Gherkin file ;" + id + " could not be found in selectors.properties!");
        return null;
    }

    private void setDriver(String user) {
        driver = User.getUsers()
                .get(user)
                .getDevice()
                .getDriver();
    }


    public static void sleepms(long time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private WebElement getElement(String sel) {
        waitForElement(sel);
        return driver.findElement(getBy(sel));
    }

    private void tap(String sel) {
        int i = TAP_RETRY_COUNT;
        do {
            try {
                getElement(sel).click();
                break;
            } catch (Exception e) {
                sleepms(1000);
                i--;
                if (i == 0) {
                    throw e;
                }
            }
        } while (i > 0);
    }

    private void waitForElement(String id) {

        try {
            WebDriverWait waitElement = new WebDriverWait(driver, ELEMENT_LOAD_TIMEOUT);
            waitElement
                    .ignoring(org.openqa.selenium.NoSuchElementException.class)
                    .ignoring(ElementNotVisibleException.class)
                    .until(ExpectedConditions
                            .visibilityOfElementLocated(getBy(id)));

            WebDriverWait waitElement2 = new WebDriverWait(driver, ELEMENT_LOAD_TIMEOUT);
            waitElement2
                    .ignoring(NoSuchElementException.class)
                    .ignoring(ElementNotVisibleException.class)
                    .until(ExpectedConditions
                            .elementToBeClickable(getBy(id)));
        } catch (TimeoutException e) {
            Assert.fail(e.getMessage());

        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
    }

    private void type(String sel, String keys) throws Exception {
        waitForElement(sel);
        WebElement el = getElement(sel);
        tap(sel);
        el.clear();
        el.sendKeys(keys);
    }

    private By getBy(String tag) {
        if (tag.substring(0, 1).equals("/"))
            return By.xpath(tag);
        else return By.id(tag);

    }

    @When("^([^\"]*) taps* on ([^\"]*)$")
    public void TapOn(String arg1, String arg2) throws Exception {
        setDriver(arg1);
        tap(getPropertyValue(arg2));
    }

    @When("^([^\"]*) taps* on ([^\"]*) where text \"([^\"]*)\"$")
    public void TapTextOnList(String arg1, String arg2, String arg3) throws Exception {
        setDriver(arg1);
        List<MobileElement> listElements = driver.findElement(getBy(getPropertyValue(arg2)))
                .findElements(By.className("android.support.v7.widget.LinearLayoutCompat"));

        for (MobileElement el : listElements) {
            MobileElement textElement = el.findElementByClassName("android.widget.CheckedTextView");
            if (textElement.getText().equalsIgnoreCase(arg3)) {
                el.click();
                break;
            }
        }
    }

    @When("^([^\"]*) control amount ([^\"]*)$")
    public void controlAmount(String arg1, String arg2) throws Exception {
        setDriver(arg1);
        if ((!driver.findElements(getBy(getPropertyValue(arg2))).isEmpty())) {
            assertTrue(driver.findElement(getBy(getPropertyValue(arg2))).getText().equals(productAmount));

        }
    }

    @When("^([^\"]*) logins* as \"([^\"]*)\" and \"([^\"]*)\"$")
    public void userLoginsAsAnd(String arg1, String username, String password) throws Exception {
        setDriver(arg1);
        type(getPropertyValue("login.email"), username);
        type(getPropertyValue("login.password"), password);
        tap(getPropertyValue("login.submit"));
    }

    @Given("^([^\"]*) tutorial control ([^\"]*)$")
    public void tutorialControl(String arg1, String arg2) {
        setDriver(arg1);
        if ((!driver.findElements(getBy(getPropertyValue(arg2))).isEmpty())) {
            tap(getPropertyValue(arg2));
        }
    }

    @Then("^([^\"]*) should see ([^\"]*) displayed$")
    public void iShouldSeeDisplayed(String arg1, String arg2) {
        setDriver(arg1);
        waitForElement(getPropertyValue(arg2));
    }

    @And("^([^\"]*) press back$")
    public void pressBack(String arg1) {
        setDriver(arg1);
        driver.navigate().back();
    }

    @Then("^([^\"]*) should not see ([^\"]*)$")
    public void iShouldNotSee(String arg1, String arg2) {
        setDriver(arg1);
        assertTrue(driver.findElements(getBy(arg2)).isEmpty());
    }

    @Given("^([^\"]*) basket control ([^\"]*)$")
    public void basketControl(String arg1, String arg2) {
        setDriver(arg1);
        if ((!driver.findElements(getBy(getPropertyValue(arg2))).isEmpty())) {
            tap(getPropertyValue("basket.button"));
            closePopup(arg1);
            tap(getPropertyValue("basket.allDelete"));
            tap(getPropertyValue("basket.shoppingButton"));
        }
    }

    @When("^([^\"]*) search \"([^\"]*)\"$")
    public void userSearch(String arg1, String searchText) throws Exception {
        setDriver(arg1);
        type(getPropertyValue("home.searchText"), searchText);
        tap(getPropertyValue("search.enter"));
    }

    @When("^([^\"]*) search text control \"([^\"]*)\"$")
    public void searchTextControl(String arg1, String arg2) {
        setDriver(arg1);
        waitForElement(getPropertyValue("search.title"));
        assertTrue(driver.findElement(getBy(getPropertyValue("search.title"))).getText().toLowerCase().contains(arg2.toLowerCase()));
    }

    @When("^([^\"]*) add basket ([^\"]*)$")
    public void addBasket(String arg1, String arg2) {
        setDriver(arg1);
        waitForElement(getPropertyValue("item.addBasket"));
        if ((!driver.findElements(getBy(getPropertyValue("item.option"))).isEmpty())) {
            tap(getPropertyValue("item.option"));
            tap(getPropertyValue("item.optionSelected"));
            tap(getPropertyValue("item.optionAdd"));
        }
        MobileElement amountView = driver.findElement(getBy(getPropertyValue("item.amount")));
        productAmount = amountView.getText();
        tap(getPropertyValue("item.addBasket"));
    }

    @When("^([^\"]*) set text \"([^\"]*)\" to ([^\"]*)$")
    public void setText(String arg1, String arg2, String arg3) throws Exception {
        setDriver(arg1);
        type(getPropertyValue(arg3), arg2);
    }

    @And("^([^\"]*) close popup$")
    public void closePopup(String arg1) {
        setDriver(arg1);
        sleepms(1500);
        if ((!driver.findElements(getBy(getPropertyValue("popup.view"))).isEmpty())) {
            tap(getPropertyValue("popup.close"));
        }
    }
}

