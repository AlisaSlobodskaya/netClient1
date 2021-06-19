package network.transport.persistSocket;

import network.core.Core;
import network.core.Transport;
import network.core.exception.TransportException;
import network.settings.Settings;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class PersistSocketTransport implements Transport {
    private Socket socket;
    private Protocol protocol;

    @Override
    public void connect() {
        try {
            tryConnect();
        } catch (Exception e) {
            throw new TransportException(e);
        }
    }

    private void tryConnect() throws IOException {
        closeSocketIfRequired();
        socket = new Socket();
        socket.connect(Settings.ADDRESS);
        Core.listeningToMessagesFromServer.start();
        protocol = new Protocol();    //new Protocol()
        register();
    }

    private void closeSocketIfRequired() throws IOException {
        if (socket != null && socket.isConnected()) {
            Core.listeningToMessagesFromServer.interrupt();
            socket.close();
        }
    }

    @Override
    public void register() {
        if (socket == null || !socket.isConnected())
            throw new TransportException("connection required");
        try {
            tryRegister();
        } catch (Exception e) {
            throw new TransportException(e);
        }
    }

    private void tryRegister() {
        try {
            Scanner input = new Scanner(System.in);
            System.out.print("Enter your nickname: ");
            String nickname = input.nextLine();
            System.out.print("Enter your password: ");
            String password = input.nextLine();
            tryConverse(protocol.registration(nickname, password));
        } catch (Exception e) {
            throw new TransportException(e);
        }
    }

    @Override
    public void serverListening() {
        if (socket == null || !socket.isConnected())
            throw new TransportException("connection required");
        try {
            tryServerListening();
        } catch (Exception e) {
            throw new TransportException(e);
        }
    }

    private void tryServerListening() throws Exception {
        var in = new BufferedReader(new InputStreamReader(socket.getInputStream(),
                StandardCharsets.UTF_8));
        String msgFromServer = in.readLine();

        if (msgFromServer.equals("connection check")) {
            tryConverse(protocol.sendTrueToWatchdog("true"));
        } else if (msgFromServer.equals("Invalid password. Try again")) {
            System.out.println(msgFromServer);
            Thread.sleep(3000);
            closeSocketIfRequired();
        } else if (msgFromServer.startsWith("20 latest msg: ")) {
            printTwentyLatestMsg(msgFromServer);
        } else {
            System.out.println(msgFromServer);
        }
    }

    private void printTwentyLatestMsg(String msgFromServer) {
        msgFromServer = msgFromServer.substring("20 latest msg: ".length());
        var array = msgFromServer.split(", ");
        for (String msg : array) {
            System.out.println(msg);
        }
    }

    @Override
    public void converse(String message) {
        if (socket == null || !socket.isConnected())
            throw new TransportException("connection required");
        try {
            tryConverse(protocol.sendMessage(message));
        } catch (Exception e) {
            throw new TransportException(e);
        }
    }

    private void tryConverse(char[] arr) throws Exception {
        var out = new PrintWriter(socket.getOutputStream(), true,
                StandardCharsets.UTF_8);

        out.printf(String.valueOf(arr));
    }

    @Override
    public void deleteAccount() {
        if (socket == null || !socket.isConnected())
            throw new TransportException("connection required");
        try {
            tryDeleteAccount();
        } catch (Exception e) {
            throw new TransportException(e);
        }
    }

    private void tryDeleteAccount() {
        try {
            Scanner input = new Scanner(System.in);
            System.out.print("Are you sure? (y/n)  ");
            String answer = input.nextLine();
            if (answer.equals("y") || answer.equals("yes")) {
                System.out.print("Enter your nickname: ");
                String nickname = input.nextLine();
                tryConverse(protocol.deleteAccount(nickname));
                System.out.println("Account deleted successfully! Bye");
                Thread.sleep(3000);
                closeSocketIfRequired();
            }
        } catch (Exception e) {
            throw new TransportException(e);
        }
    }

    @Override
    public void disconnect() {
        try {
            tryDisconnect();
        } catch (Exception e) {
            throw new TransportException(e);
        }
    }

    private void tryDisconnect() throws Exception {
        closeSocketIfRequired();
    }

    public class Protocol {
        private static final char GS = 0x1D;
        private static final char RS = 0x1E;
        private String nickname;

        public char[] registration(String nickname, String password) {
            String output = "T_REGISTER" + GS + nickname + GS + password + RS;
            setNickname(nickname);
            return output.toCharArray();
        }

        public char[] sendMessage(String msg) {
            String output = "T_MESSAGE" + GS + nickname + GS + msg + RS;
            return output.toCharArray();
        }

        public char[] deleteAccount(String nickname) {
            String output = "T_DELETE_ACCOUNT" + GS + nickname + RS;
            return output.toCharArray();
        }

        public char[] sendTrueToWatchdog(String msg) {
            String output = "T_WATCHDOG" + GS + msg + RS;
            return output.toCharArray();
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }
    }
}


