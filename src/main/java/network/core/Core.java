package network.core;

import network.core.command.*;
import network.core.exception.UnexpectedCommandException;
import network.transport.ListeningToMessagesFromServer;

/**
 * Client business logic.
 */
public class Core implements Runnable {
    private final UI ui;
    private final Transport transport;
    public static ListeningToMessagesFromServer listeningToMessagesFromServer;

    public Core(UI userInterface, Transport transport, ListeningToMessagesFromServer listeningToMessagesFromServer) {
        this.ui = userInterface;
        this.transport = transport;
        this.listeningToMessagesFromServer = listeningToMessagesFromServer;
    }

    @Override
    public void run() {
        try {
            runCommandLoop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void runCommandLoop() {
        Command command;
        do {
            command = ui.getCommand();
            perform(command);
        }
        while (command.getClass() != Disconnect.class);
    }

    private void perform(Command command) {
        try {
            performSelection(command);
        } catch (Exception e) {
            ui.showMessage(e.getMessage());
        }
    }

    private void performSelection(Command command) {
        if (command.getClass() == Connect.class) {
            performConnect((Connect) command);
        } else if (command.getClass() == Send.class) {
            performSend((Send) command);
        } else if (command.getClass() == DeleteAccount.class) {
            performDeleteAcc((DeleteAccount) command);
        } else if (command.getClass() == Disconnect.class) {
            performDisconnect((Disconnect) command);
        } else {
            throw new UnexpectedCommandException("Unexpected " + command.getClass().getName());
        }
    }

    private void performDeleteAcc(DeleteAccount command) {
        transport.deleteAccount();
    }

    private void performConnect(Connect command) {
        transport.connect();
    }

    private void performSend(Send command) {
        transport.converse(command.getMessage());
    }

    private void performDisconnect(Disconnect command) {
        transport.disconnect();
    }
}
