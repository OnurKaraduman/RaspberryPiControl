package model;

import java.net.InetAddress;

public class Client {

    //<editor-fold desc="members">
    private int clientID;
    private int clientPort;
    private InetAddress clientIPAddress;
    private int authority;

    //</editor-fold>

    //<editor-fold desc="properties">
    public int getClientID() {
        return clientID;
    }

    public void setClientID(int clientID) {
        this.clientID = clientID;
    }

    public int getClientPort() {
        return clientPort;
    }

    public void setClientPort(int clientPort) {
        this.clientPort = clientPort;
    }

    public InetAddress getClientIPAddress() {
        return clientIPAddress;
    }

    public void setClientIPAddress(InetAddress clientIPAddress) {
        this.clientIPAddress = clientIPAddress;
    }

    public int getAuthority() {
        return authority;
    }

    public void setAuthority(int authority) {
        this.authority = authority;
    }
    //</editor-fold>

}
