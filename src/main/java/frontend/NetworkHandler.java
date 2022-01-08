package frontend;

import logic.entities.Player;

import networking.entities.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class NetworkHandler implements Runnable {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private Socket socket;
    private ActionStatus actionStatus = ActionStatus.NONE;
    private Gui gui;

    public NetworkHandler(Socket socket, Gui gui){
        this.socket = socket;
        this.gui = gui;
    }

    @Override
    public void run(){
        try {
            while (true) {
                ObjectInputStream ois = new ObjectInputStream(getClientSocket().getInputStream());

                Object inputObject = ois.readObject();
                //todo: implement connect action
                if (inputObject instanceof InitialAction) {
                    handleInitialAction((InitialAction) inputObject);
                } else if (inputObject instanceof ListPlayersAction) {
                    handleListPlayersAction((ListPlayersAction) inputObject);
                } else if (inputObject instanceof GameAction) {
                    handleGameAction((GameAction) inputObject);
                } else  if (inputObject instanceof ReconnectAction) {
                    handleReconnectAction((ReconnectAction) inputObject);
                } else throw new ClassNotFoundException("Read input object not supported");
            }
        } catch (IOException e) {
            logger.error("the connection was closed",e);
        } catch (ClassNotFoundException e) {
            logger.error("(de)serialization failed",e);
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                logger.error("failed to close socket",e);
            }
            Thread.currentThread().interrupt();
        }
    }

    public Player handleInitialAction(String name) {
        InitialAction initialAction = new InitialAction(name);
        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
            outputStream.writeObject(initialAction);
        } catch (IOException e) {
            logger.debug("IO Error", e);
            return null;
        }
        InitialResponse response;
        try {
            ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
            response = (InitialResponse) inputStream.readObject();
        } catch (IOException e) {
            logger.debug("Fehler beim lesen des Objekts", e);
            return null;
        } catch (ClassNotFoundException ex) {
            logger.debug("InitialResponse class was not found", ex);
            return null;
        }
        return response.getSelf();
    }

    public ArrayList<Player> handleListPlayersAction(Player player){
        ListPlayersAction listPlayersAction = new ListPlayersAction(player);
        try{
            ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
            outputStream.writeObject(listPlayersAction);
        } catch (IOException e){
            logger.debug("Fehler beim übertragen des Objekts",e);
            return null;
        }
        ListPlayersResponse response;
        try{
            ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
            response = (ListPlayersResponse) inputStream.readObject();
        } catch(IOException e) {
            logger.debug("Fehler beim lesen des Objekts",e);
            return null;
        } catch (ClassNotFoundException ex){
            logger.debug("ListPlayersResponse class was not found",ex);
            return null;
        }
        return response.getPlayers();
    }

    public Socket getSocket() {
        return socket;
    }

    public void setActionStatus(ActionStatus actionStatus) {
        this.actionStatus = actionStatus;
    }
}
