package me.vincevan.myremoteapp.model;

public class SavedHostItem {

    private String hostName;
    private String hostAddress;

    public SavedHostItem(String name, String address){
        this.hostName = name;
        this.hostAddress = address;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public String getHostAddress() {
        return hostAddress;
    }

    public void setHostAddress(String hostAddress) {
        this.hostAddress = hostAddress;
    }

}
