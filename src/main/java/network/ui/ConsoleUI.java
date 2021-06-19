package network.ui;

import network.core.UI;
import network.core.command.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class ConsoleUI implements UI {
    private static BufferedReader reader;

    /**
     * Processing commands from the client from the console.
     * <p>
     * Available commands:
     * connect
     * send [message]
     * disconnect
     * delete account
     */
    @Override
    public Command getCommand() {
        Command command = null;
        while (command == null) {
            try {
                reader = new BufferedReader(new InputStreamReader(System.in));
                String input = reader.readLine();
                command = parseCommand(input);
            } catch (Exception e) {
                System.out.println(e);
            }
        }
        return command;
    }

    private Command parseCommand(String line) {
        line = line.strip();
        if (line.equals("connect")) {
            return parseConnect(line);
        } else if (line.startsWith("send ")) {
            return parseSend(line);
        } else if (line.startsWith("delete account")) {
            return parseDeleteAcc(line);
        } else if (line.equals("disconnect")) {
            return parseDisconnect(line);
        } else {
            System.out.println("unknown command");
            return null;
        }
    }

    private Connect parseConnect(String line) {
        return new Connect();
    }

    private Send parseSend(String line) {
        line = line.substring("send ".length());
        return new Send(line);
    }

    private DeleteAccount parseDeleteAcc(String line) {
        return new DeleteAccount();
    }

    private Disconnect parseDisconnect(String line) {
        return new Disconnect();
    }

    @Override
    public void showMessage(String message) {
        System.out.println(message);
    }
}
