package utility;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class User {

    public String username;
    public Device device;

    public static HashMap<String, User> users = new HashMap<>();

    public User(String username) {
        this.username = username;
    }

    public void setDevice(Device device) {
        this.device = device;
    }

    public Device getDevice() {
        return device;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public User(String userName, Device device){

    }

    public static Map<String, User> getUsers() {
        return Collections.unmodifiableMap(users);
    }
}