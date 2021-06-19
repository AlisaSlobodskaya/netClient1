package network.core.command;

public class Send extends Command {
    private final String message;

    public Send(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "Send{" +
                "message='" + message + '\'' +
                '}';
    }
}
