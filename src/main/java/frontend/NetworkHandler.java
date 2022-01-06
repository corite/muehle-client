package frontend;

import logic.entities.Player;

import networking.entities.InitialAction;
import networking.entities.InitialResponse;
import networking.entities.ListPlayersAction;
import networking.entities.ListPlayersResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class NetworkHandler {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private Socket socket;
    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;

    public NetworkHandler(Socket socket){
        this.socket = socket;
        try{
            this.outputStream = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            logger.debug("Fehler beim outputstream");
        }
        try{
            this.inputStream = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            logger.debug("Fehler beim inputstream");
        }
    }

    public Player handleInitialAction(String name){
        InitialResponse response = null;
        InitialAction initialAction = new InitialAction(name);
        try{
            outputStream.writeObject(initialAction);
        } catch (IOException e){
            logger.debug("Fehler beim übertragen des Objekts");
        }
        try{
            response = (InitialResponse) inputStream.readObject();
        } catch (IOException e){
            logger.debug("Fehler beim lesen des Objekts");
        } catch (ClassNotFoundException ex){
            logger.debug("InitialResponse class was not found");
        }
        if (response.getSelf() == null){
            logger.debug("Error, player should not be null");
        }
        return response.getSelf();
    }

    public ArrayList<Player> handleListPlayersAction(Player player){
        ListPlayersResponse response = null;
        ListPlayersAction listPlayersAction = new ListPlayersAction(player);
        try{
            outputStream.writeObject(listPlayersAction);
        } catch (IOException e){
            logger.debug("Fehler beim übertragen des Objekts");
        }
        try{
            response = (ListPlayersResponse) inputStream.readObject();
        } catch(IOException e) {
            logger.debug("Fehler beim lesen des Objekts");
        } catch (ClassNotFoundException ex){
            logger.debug("ListPlayersResponse class was not found");
        }
        if (response.getPlayers() == null){
            logger.debug("Error, player should not be null");
        }
        return response.getPlayers();
    }
}
