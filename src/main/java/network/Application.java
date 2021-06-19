package network;

import static network.util.Logger.log;

public class Application {
    public static void main(String[] args) {
        log(" Client starting...");
        System.out.println("Available commands: \n"
                + "> connect\n"
                + "> send [message]\n"
                + "> disconnect\n"
                + "> delete account\n");
        ApplicationConstructor.construct().run();
        log(" Client finished");
    }
}
