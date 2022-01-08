package frontend;

import networking.entities.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

public class NetworkHandler implements Runnable {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final Socket socket;
    private final Gui gui;

    public NetworkHandler(Socket socket, Gui gui){
        this.socket = socket;
        this.gui = gui;
    }

    @Override
    public void run(){
        try {
            while (true) {
                ObjectInputStream ois = new ObjectInputStream(getSocket().getInputStream());

                Object inputObject = ois.readObject();
                //todo: implement connect action
                if (inputObject instanceof InitialResponse) {
                    handleInitialAction((InitialResponse) inputObject);
                } else if (inputObject instanceof ListPlayersResponse) {
                    handleListPlayersAction((ListPlayersResponse) inputObject);
                } else if (inputObject instanceof GameResponse) {
                    handleGameAction((GameResponse) inputObject);
                } else throw new ClassNotFoundException("Read input object not supported");
            }
        } catch (IOException e) {
            logger.error("the connection was closed",e);
        } catch (ClassNotFoundException e) {
            logger.error("(de)serialization failed",e);
        } finally {
            try {
                getSocket().close();
            } catch (IOException e) {
                logger.error("failed to close socket",e);
            }
            Thread.currentThread().interrupt();
        }
    }

    public void handleInitialAction(InitialResponse response) {
        gui.setPlayer(response.getSelf());
    }

    public void handleListPlayersAction(ListPlayersResponse response){
        gui.setPlayers(response.getPlayers());
    }

    public void handleGameAction(GameResponse response){

    }

    public Socket getSocket() {
        return socket;
    }
}
