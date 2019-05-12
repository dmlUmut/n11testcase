package utility.devices;

public enum ClientModel {
    NEXUS5X ("emulator-5554",8301,"9.0", "Android");

    private final String udid;
    private final int port;
    private final String platformVersion;
    private final String platformName;

    ClientModel(String udid, int port, String platformVersion, String platformName) {
        this.udid = udid;
        this.port = port;
        this.platformVersion = platformVersion;
        this.platformName = platformName;
    }

    public String getPlatformName() {
        return platformName;
    }

    public String getUdid() {
        return udid;
    }

    public int getPort() {
        return port;
    }

    public String getPlatformVersion() {
        return platformVersion;
    }

    public static ClientModel getClientModelByName(String name){
        for (ClientModel c : ClientModel.values()) {
            if (c.name().equalsIgnoreCase(name)){
                return c;
            }
        }
        return null;
    }
}
