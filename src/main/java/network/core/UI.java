package network.core;

import network.core.command.Command;

public interface UI {
    Command getCommand();

    void showMessage(String message);
}
