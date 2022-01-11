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

    public Socket getSocket() {
        return socket;
    }

    public Gui getGui() {
        return gui;
    }

    @Override
    public void run() {
        try {
            while (true) {
                ObjectInputStream ois = new ObjectInputStream(getSocket().getInputStream());

                Object inputObject = ois.readObject();
                //todo: implement connect action
                if (inputObject instanceof InitialResponse) {
                    handleInitialResponse((InitialResponse) inputObject);
                } else if (inputObject instanceof ListPlayersResponse) {
                    handleListPlayersResponse((ListPlayersResponse) inputObject);
                } else if (inputObject instanceof GameResponse) {
                    handleGameResponse((GameResponse) inputObject);
                } else if (inputObject instanceof EndSessionResponse){
                    handleEndSessionResponse((EndSessionResponse) inputObject);
                } else throw new ClassNotFoundException("Read input object not supported");
            }
        } catch (IOException e) {
            logger.error("the connection was closed", e);
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

    public void handleInitialResponse(InitialResponse response) {
        getGui().setPlayer(response.getSelf());


        ListPlayersAction listPlayersAction = new ListPlayersAction(response.getSelf());
        Thread socketWriter = new Thread(new SocketWriter(getGui().getWriterLock(),listPlayersAction,getGui().getOutputStream()));
        socketWriter.start();
    }

    public void handleListPlayersResponse(ListPlayersResponse response) {
        getGui().renderListPlayersResponse(response);
    }

    public void handleGameResponse(GameResponse response) {
        getGui().setLastGameResponse(response);

        getGui().renderGameResponse(response);
    }

    public void handleEndSessionResponse(EndSessionResponse response){
        getGui().renderEndSessionResponse(response);
    }
}
// todo playerlost response popup window
// todo ReconnectAction
