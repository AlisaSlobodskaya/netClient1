package network;

import network.core.Core;
import network.transport.ListeningToMessagesFromServer;
import network.transport.persistSocket.PersistSocketTransport;
import network.ui.ConsoleUI;

public class ApplicationConstructor {
    public static Core construct() {
        PersistSocketTransport pst = new PersistSocketTransport();
        return new Core(new ConsoleUI(), pst, new ListeningToMessagesFromServer(pst));
    }
}
