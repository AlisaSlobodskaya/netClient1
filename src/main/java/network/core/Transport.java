package network.core;

public interface Transport {
    void connect();

    void deleteAccount();

    void converse(String message);

    void disconnect();

    void register();

    void serverListening();
}
