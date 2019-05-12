package stepdefinitions;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import cucumber.api.java.en.Given;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.openqa.selenium.*;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utility.Device;
import utility.Hooks;
import utility.User;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.*;
import static junit.framework.TestCase.assertTrue;
import static junit.framework.TestCase.fail;

public class Stepdef {
    private Hooks hooks;
    private Properties selectors;
    private WebDriver driver;
    private final int TAP_RETRY_COUNT = 4;
    private final int ELEMENT_LOAD_TIMEOUT = 30;
    private static final Logger LOGGER = LogManager
            .getLogger(MethodHandles.lookup().lookupClass());


    public Stepdef(Properties selectors,Hooks hooks) {
        this.hooks=hooks;
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
                LOGGER.debug(e.getMessage());
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

    private By getBy(String tag){
        if (tag.contains("//"))
            return By.xpath(tag);
        else return By.id(tag);

    }

    @When("^([^\"]*) taps* on ([^\"]*)$")
    public void TapOn(String arg1, String arg2) throws Exception {
        setDriver(arg1);
        tap(getPropertyValue(arg2));
    }


    @When("^([^\"]*) logins* as \"([^\"]*)\" and \"([^\"]*)\"$")
    public void userLoginsAsAnd(String arg0, String username, String password) throws Exception {
        setDriver(arg0);
        type(getPropertyValue("login.email"), username);
        type(getPropertyValue("login.password"), password);
        tap(getPropertyValue("login.submit"));
    }

    @Then("^([^\"]*) should see ([^\"]*) displayed$")
    public void iShouldSeeDisplayed(String arg1, String arg2) {
        setDriver(arg1);
        waitForElement(getPropertyValue(arg2));
    }

    @Given("^([^\"]*) see ([^\"]*)$")
    public void iSee(String arg1, String arg2) {
        setDriver(arg1);
        waitForElement(getPropertyValue(arg2));
    }

    @Then("^([^\"]*) should not see ([^\"]*)$")
    public void iShouldNotSee(String arg0, String arg1) {
        setDriver(arg0);
        assertTrue(driver.findElements(getBy(arg1)).isEmpty());
    }
}
