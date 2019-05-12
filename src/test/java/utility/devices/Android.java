package utility.devices;

import com.google.common.collect.ImmutableMap;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidStartScreenRecordingOptions;
import io.appium.java_client.remote.AndroidMobileCapabilityType;
import io.appium.java_client.remote.MobileCapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import utility.Device;

import java.io.File;
import java.net.URL;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class Android extends Device {
    private final String APK_DIR = "\\Users\\dambik\\Documents\\Automation\\damlasekern11\\apk";
    private final String APK_FILE = "n11.apk";
    private final String ANDROID_APP_PACKAGE = "com.dmall.mfandroid";
    private final String APP_ACTIVITY = "com.dmall.mfandroid.activity.base.Splash";
    private final int ADB_TIMEOUT = 60 * 1000;

    public Android(ClientModel model) {
        this.clientModel = model;
    }

    @Override
    public void startDriver() {
        File appDir = new File(APK_DIR);
        File app = new File(appDir, APK_FILE);
        DesiredCapabilities cap = new DesiredCapabilities();
        cap.setCapability(MobileCapabilityType.UDID, clientModel.getUdid());
        cap.setCapability(MobileCapabilityType.DEVICE_NAME, clientModel.name());
        cap.setCapability(MobileCapabilityType.PLATFORM_VERSION, clientModel.getPlatformVersion());
        cap.setCapability(MobileCapabilityType.PLATFORM_NAME, "Android");
        cap.setCapability(AndroidMobileCapabilityType.APP_PACKAGE, ANDROID_APP_PACKAGE);
        cap.setCapability(AndroidMobileCapabilityType.APP_ACTIVITY, APP_ACTIVITY);
        cap.setCapability(AndroidMobileCapabilityType.UNICODE_KEYBOARD, true);
        cap.setCapability(AndroidMobileCapabilityType.RESET_KEYBOARD, true);
        cap.setCapability(MobileCapabilityType.AUTOMATION_NAME, "UiAutomator2");
        cap.setCapability(MobileCapabilityType.NEW_COMMAND_TIMEOUT, NEW_COMMAND_TIMEOUT);
        cap.setCapability(MobileCapabilityType.NO_RESET, true);
        cap.setCapability("adbExecTimeout", ADB_TIMEOUT);
        cap.setCapability(AndroidMobileCapabilityType.SYSTEM_PORT, clientModel.getPort());
        try {
            URL url = new URL(APPIUM_SERVER);
            driver = new AndroidDriver<>(url, cap);
        } catch (Exception e) {
            if (driver == null)
                System.out.println("DEBUG: driver was null");
            System.out.println("Exception in Android.Java at line 54");
            e.printStackTrace();
        }
    }

    @Override
    public void closeApp() {
        List<String> lsArgs = Arrays.asList(ANDROID_APP_PACKAGE);
        Map<String, Object> lsCmd = ImmutableMap.of(
                "command", "am force-stop",
                "args", lsArgs
        );
        switch_to_native_view();
        ((AndroidDriver) driver).executeScript("mobile: shell", lsCmd);
    }
}