package networking;

import frontend.Gui;
import networking.entities.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

public class SocketReader implements Runnable {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final Socket socket;
    private final Gui gui;

    public SocketReader(Socket socket, Gui gui) {
        this.socket = socket;
        this.gui = gui;
    }

    private Socket getSocket() {
        return socket;
    }

    private Gui getGui() {
        return gui;
    }

    @Override
    public void run() {
        try {
            while (true) {
                try {
                    ObjectInputStream ois = new ObjectInputStream(getSocket().getInputStream());

                    Object inputObject = ois.readObject();
                    if (inputObject instanceof RegisterLoginUserResponse) {
                        handleRegisterLoginUserResponse((RegisterLoginUserResponse) inputObject);
                    } else if (inputObject instanceof ListUsersResponse) {
                        handleListUsersResponse((ListUsersResponse) inputObject);
                    } else if (inputObject instanceof GameResponse) {
                        handleGameResponse((GameResponse) inputObject);
                    } else if (inputObject instanceof EndGameResponse) {
                        handleEndGameResponse((EndGameResponse) inputObject);
                    } else if (inputObject instanceof DisconnectResponse){
                        handleDisconnectResponse((DisconnectResponse) inputObject);
                    } else throw new ClassNotFoundException("Read input object not supported");
                } catch (IOException e) {
                    logger.error("the connection was closed", e);
                }
            }
        } catch (ClassNotFoundException e) {
            logger.error("(de)serialization failed", e);
        } finally {
            try {
                getSocket().close();
            } catch (IOException e) {
                logger.error("failed to close socket", e);
            }
            Thread.currentThread().interrupt();
        }
    }

    private void handleRegisterLoginUserResponse(RegisterLoginUserResponse response) {
        getGui().renderRegisterLoginUserResponse(response);
    }

    private void handleListUsersResponse(ListUsersResponse response) {
        getGui().renderListUsersResponse(response);
    }

    private void handleGameResponse(GameResponse response) {
        getGui().setLastGameResponse(response);

        getGui().renderGameResponse(response);
    }

    private void handleEndGameResponse(EndGameResponse response){
        getGui().renderEndGameResponse(response);
    }

    private void handleDisconnectResponse(DisconnectResponse response){
        getGui().renderDisconnectResponse(response);
    }
}
