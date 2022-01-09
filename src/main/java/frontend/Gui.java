package frontend;

import logic.entities.StoneState;
import networking.entities.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;

import logic.entities.Player;

public class Gui {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final JComboBox<Player> playerList = new JComboBox<>();
    private final Draw draw = new Draw(this);
    private final JFrame frame = new JFrame("Muehle");
    private final Button[]btn = new Button[24];
    private Button tmp = null;
    private final JButton refreshList = new JButton("Aktualisiere Liste");
    private final JButton confirm = new JButton("Anfrage senden");
    private Player player = null;
    private Socket socket;
    private GameResponse gameResponse;
    private final NetworkHandler networkHandler;
    private ArrayList<Player> players = new ArrayList<>();

    public Gui() throws IOException {

        //creating window and window settings

        this.createFrame();

        //create Socket and Thread for NetworkHandler class
        synchronized (this) {
            socket = new Socket("localhost", 5056);
            networkHandler = new NetworkHandler(socket, this);
            Thread network = new Thread(networkHandler);
            network.start();
        }

        //get player name input through popup window, catch empty String or cancel Operation

        String name = null;

        while (name == null || name.equals("")) {
            name = JOptionPane.showInputDialog(frame, "Enter username for Player 1!");
        }

        //send input name to the Server to receive Player Object
        synchronized (this){
            ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
            outputStream.writeObject(new InitialAction(name));
            outputStream.flush();

            //put Players in Combo Box, to choose one to play against

            createConnectionElements(outputStream);

            //creating JLabel from draw class and draw settings

/*            createLabel();

            //create buttons

            ArrayList<Coordinate> coordinates = gameResponse.getGameField().stream()
                    .map(Position::getCoordinate)
                    .sorted(((o1, o2) -> o1.getY() == o2.getY() ? Integer.compare(o1.getX(), o2.getX()) : -Integer.compare(o1.getY(), o2.getY())))
                    //sorts the collection so that the nodes can be traversed row by row from top to bottom
                    .collect(Collectors.toCollection(ArrayList::new));

            for (int i=0; i< btn.length; i++){
                btn[i] = new Button(coordinates.get(i));
                createButtons(i);
            }
            this.placeBtn();*/
        }
    }
    //todo make only necessary getter/setter public and add missing getters/setters
    public Button getBtn(int i){
        return btn[i];
    }

    public void setPlayers(ArrayList<Player> players) {
        this.players = players;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Draw getDraw() {
        return draw;
    }

    public JComboBox getComboBox() {
        return playerList;
    }

    public Button getTmp() {
        return tmp;
    }

    public void setTmp(Button tmp) {
        this.tmp = tmp;
    }

    public GameResponse getGameResponse() {
        return gameResponse;
    }

    //Button placement

    private void placeBtn() {
        btn[0].setBounds(50-20, 50-20, 40, 40);
        btn[1].setBounds(350-20, 50-20, 40, 40);
        btn[2].setBounds(650-20, 50-20, 40, 40);
        btn[3].setBounds(150-20, 150-20, 40, 40);
        btn[4].setBounds(350-20, 150-20, 40, 40);
        btn[5].setBounds(550-20, 150-20, 40, 40);
        btn[6].setBounds(250-20, 250-20, 40, 40);
        btn[7].setBounds(350-20, 250-20, 40, 40);
        btn[8].setBounds(450-20, 250-20, 40, 40);
        btn[9].setBounds(50-20, 350-20, 40, 40);
        btn[10].setBounds(150-20, 350-20, 40, 40);
        btn[11].setBounds(250-20, 350-20, 40, 40);
        btn[12].setBounds(450-20, 350-20, 40, 40);
        btn[13].setBounds(550-20, 350-20, 40, 40);
        btn[14].setBounds(650-20, 350-20, 40, 40);
        btn[15].setBounds(250-20, 450-20, 40, 40);
        btn[16].setBounds(350-20, 450-20, 40, 40);
        btn[17].setBounds(450-20, 450-20, 40, 40);
        btn[18].setBounds(150-20, 550-20, 40, 40);
        btn[19].setBounds(350-20, 550-20, 40, 40);
        btn[20].setBounds(550-20, 550-20, 40, 40);
        btn[21].setBounds(50-20, 650-20, 40, 40);
        btn[22].setBounds(350-20, 650-20, 40, 40);
        btn[23].setBounds(650-20, 650-20, 40, 40);
    }

    private void createFrame(){
        frame.setBounds(0, 0, 1000, 750);
        frame.setLayout(new FlowLayout());
        frame.getContentPane().setBackground(Color.decode("#FDFD96"));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.setResizable(false);
    }

    private void createLabel(){
        draw.setBounds(0, 0, 1000, 750);
        draw.setVisible(true);
        getDraw().repaint();
    }

    private void createButtons(int i){
        btn[i].setVisible(true);
        try {
            btn[i].addActionListener(new ActionHandler(this, new ObjectOutputStream(socket.getOutputStream())));
        } catch (IOException e) {
            logger.debug("IO Error", e);
        }
        btn[i].setFocusPainted(false);
        btn[i].setContentAreaFilled(false);
        btn[i].setBorder(null);
        frame.add(btn[i]);
    }

    public void handleListPlayers(ListPlayersResponse response){
        playerList.removeAll();
        if (response.getPlayers().isEmpty()){
            JOptionPane.showMessageDialog(frame, "Zurzeit befindet sich kein Spieler in der Warteschlange versuche die Liste in später zu aktualisieren.");
            playerList.addItem(new Player("", 0, StoneState.NONE, OutputStream.nullOutputStream()));
        }else{
            for (Player player : response.getPlayers()){
                playerList.addItem(player);
            }
        }
    }

    private void createConnectionElements(ObjectOutputStream outputStream){
        refreshList.addActionListener(e -> {
            synchronized (this) {
                try {
                    outputStream.writeObject(new ListPlayersAction(player));
                    outputStream.flush();
                } catch (IOException ex) {
                    logger.debug("IO Error", ex);
                }
            }
        });
        refreshList.setPreferredSize(new Dimension(200, 50));
        frame.add(refreshList);

        playerList.addActionListener(new ComboBoxListener(this));
        playerList.setPreferredSize(new Dimension(300,50));
        frame.add(playerList);

        confirm.setPreferredSize(new Dimension(200,50));
        confirm.addActionListener(e -> {
            synchronized (this) {
                if (playerList.getSelectedItem() == null) {
                    JOptionPane.showMessageDialog(frame, "Bitte Wähle einen Spieler aus der Liste aus");
                } else {
                    try {
                        outputStream.writeObject(new ConnectAction(player, (Player) playerList.getSelectedItem()));
                        outputStream.flush();
                        frame.remove(confirm);
                        frame.remove(playerList);
                        frame.remove(refreshList);
                    } catch (IOException ex) {
                        logger.debug("IO Error", ex);
                    }
                }
            }
        });
        frame.add(confirm);
    }
}