package utility;

import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


public class Hooks {
    //Scenario Level Before Hook
    @Before()
    public void before() {

    }

    //Scenario Level After Hook
    @After()
    public void cleanUp(Scenario scenario) {
        if (scenario.isFailed()) {
            ExecutorService service = Executors.newFixedThreadPool(5);
            User.getUsers().forEach((k, v) -> {
                service.execute(() -> {
                    v.getDevice().switch_to_native_view();

                    byte[] screenshot = ((TakesScreenshot) v.getDevice().getDriver()).getScreenshotAs(OutputType.BYTES);
                    scenario.embed(screenshot, "image/png");

                    v.getDevice().closeApp();
                    v.getDevice().startDriver();
                });
            });

            service.shutdown();
            try {
                if (service.awaitTermination(300, TimeUnit.SECONDS)) {
                    service.shutdownNow();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

