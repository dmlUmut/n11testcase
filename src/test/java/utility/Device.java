package utility;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import org.junit.Assert;
import org.openqa.selenium.WebDriver;
import stepdefinitions.Stepdef;
import utility.devices.ClientModel;

import java.util.Set;

import static stepdefinitions.Stepdef.sleepms;

public abstract class Device implements Runnable {

    protected final String APPIUM_SERVER = "http://127.0.0.1:4723/wd/hub";
    protected final int NEW_COMMAND_TIMEOUT = 150000;
    public WebDriver driver;
    protected ClientModel clientModel;

    public abstract void startDriver();

    public void switch_to_native_view() {
        Set<String> contextNames = ((AndroidDriver) driver).getContextHandles();
        for (String contextName : contextNames) {
            if (contextName.contains("NATIVE_APP"))
                ((AndroidDriver) driver).context(contextName);
        }
    }

    @Override
    public void run() {
        startDriver();
        sleepms(10000);
    }

    public abstract void closeApp();

    public WebDriver getDriver() {
        return driver;
    }

    public ClientModel getClientModel() {
        return clientModel;
    }

}
