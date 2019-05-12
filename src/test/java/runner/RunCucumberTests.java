package runner;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import utility.Device;
import utility.User;
import utility.devices.Android;
import utility.devices.ClientModel;

import java.lang.invoke.MethodHandles;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static utility.devices.ClientModel.NEXUS5X;

@RunWith(Cucumber.class)
@CucumberOptions(

        /*
        *Run only a single feature set or single scenario
        */
        tags = {"@appLogin"},

        features = {"classpath:features"},
        glue = {"stepdefinitions", "utility"},
        plugin = {"pretty", "html:target/cucumber-reports", "json:target/cucumber-reports/login.json", "rerun:target/rerun/rerun.txt"})

public class RunCucumberTests {
    private static final Logger LOGGER = LogManager
            .getLogger(MethodHandles.lookup().lookupClass());
    static Object[][] userArray = {
            {"User", NEXUS5X}
    };


    @BeforeClass
    public static void setup() {
        ExecutorService service = Executors.newFixedThreadPool(5);

        for (Object[] s : userArray) {
            Device device = null;
            String user = (String) s[0];
            ClientModel clientModel = (ClientModel) s[1];
            User.users.put(user, new User(user));

            if (User.getUsers().get(user).getDevice() == null) {
                if (clientModel.getPlatformName().equalsIgnoreCase("android")) {
                    device = new Android(clientModel);
                    service.execute(device);
                }
            }

            User.getUsers()
                    .get(user)
                    .setDevice(device);
        }

        service.shutdown();
        try {
            if (service.awaitTermination(300, TimeUnit.SECONDS)) {
                service.shutdownNow();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    @AfterClass()
    public static void teardown() {
        User.getUsers().forEach((k, v) -> {
            if (v.getDevice().getDriver() != null)
                v.getDevice().getDriver().quit();
        });
    }
}
