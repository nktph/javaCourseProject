package sample;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Connector {
    public Scanner fromServer;
    public PrintWriter toServer;
    public Socket clientSocket;
    private static Connector instance;

    private Connector() {
        try {
            clientSocket = new Socket();
            clientSocket.connect(new InetSocketAddress(InetAddress.getByName("127.0.0.1"), 4848));
            fromServer = new Scanner(clientSocket.getInputStream());
            toServer = new PrintWriter(clientSocket.getOutputStream(), true);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static Connector getInstance() throws IOException {
        if (instance == null)
        {
            instance = new Connector();
        }
        return instance;
    }

    public void commitSuicide() {
        try {
            clientSocket.close();
            instance = null;
            System.out.println("Соединение закрыто, поток остановлен");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
