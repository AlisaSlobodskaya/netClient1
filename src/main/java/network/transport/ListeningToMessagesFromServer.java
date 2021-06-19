package network.transport;

import network.core.Core;
import network.transport.persistSocket.PersistSocketTransport;

public class ListeningToMessagesFromServer extends Thread {
    private final PersistSocketTransport transport;

    public ListeningToMessagesFromServer(PersistSocketTransport transport) {
        this.transport = transport;
    }

    public void run() {
        try {
            runCommandLoop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void runCommandLoop() {
        while (true) {
            transport.serverListening();
        }
    }
}
