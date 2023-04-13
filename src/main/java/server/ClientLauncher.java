package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ClientLauncher {
    protected static Socket socket;

    public static final Integer port=1337;

    public static final String address="localhost";


public static void closeSocket() throws IOException {
        socket.close();
}
    public static Socket getSocket() {
        return socket;
    }
    public static void main(String[] args) {
      Client client;


        try {
            client = new Client(port);
            System.out.println("Client is running...");
            client.prepTransfer();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void setConnection() throws IOException {
socket = new Socket(address, (port));



    }
}
