package frontend;

import logic.entities.Coordinate;
import logic.entities.Position;
import networking.SocketReader;
import networking.SocketWriter;
import networking.entities.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

import logic.entities.Player;

public class Gui {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final JComboBox<Player> playerList = new JComboBox<>();
    private final Draw draw = new Draw(this);
    private final JFrame frame = new JFrame("Muehle");
    private final Button[] buttons = new Button[24];
    private Button tmp = null;

    private Player player = null;
    private GameResponse lastGameResponse;

    private OutputStream outputStream;
    private final Object writerLock = new Object();

    private boolean isListPlayersScreenEnabled;



    public Gui() throws IOException {

        //creating window and window settings

        this.createFrame();

        //create Socket and Thread for NetworkHandler class

        this.establishConnectionWithServer();


        //get player name input through popup window, catch empty String or cancel Operation

        this.readNameAndSendInitialAction();




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

    public GameResponse getLastGameResponse() {
        return lastGameResponse;
    }

    public void setLastGameResponse(GameResponse lastGameResponse) {
        this.lastGameResponse = lastGameResponse;
    }

    public boolean isListPlayersScreenEnabled() {
        return isListPlayersScreenEnabled;
    }

    public void setListPlayersScreenEnabled(boolean listPlayersScreenEnabled) {
        isListPlayersScreenEnabled = listPlayersScreenEnabled;
    }

    public JComboBox<Player> getPlayerList() {
        return playerList;
    }

    public JFrame getFrame() {
        return frame;
    }

    public Button[] getButtons() {
        return buttons;
    }

    public OutputStream getOutputStream() {
        return outputStream;
    }

    public void setOutputStream(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    public Object getWriterLock() {
        return writerLock;
    }

    public Button getBtn(int i){
        return buttons[i];
    }

    public Player getPlayer() {
        return player;
    }

    public synchronized void setPlayer(Player player) {
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


    //Button placement

    private void placeBtn() {
        getButtons()[0].setBounds(50-20, 50-20, 40, 40);
        getButtons()[1].setBounds(350-20, 50-20, 40, 40);
        getButtons()[2].setBounds(650-20, 50-20, 40, 40);
        getButtons()[3].setBounds(150-20, 150-20, 40, 40);
        getButtons()[4].setBounds(350-20, 150-20, 40, 40);
        getButtons()[5].setBounds(550-20, 150-20, 40, 40);
        getButtons()[6].setBounds(250-20, 250-20, 40, 40);
        getButtons()[7].setBounds(350-20, 250-20, 40, 40);
        getButtons()[8].setBounds(450-20, 250-20, 40, 40);
        getButtons()[9].setBounds(50-20, 350-20, 40, 40);
        getButtons()[10].setBounds(150-20, 350-20, 40, 40);
        getButtons()[11].setBounds(250-20, 350-20, 40, 40);
        getButtons()[12].setBounds(450-20, 350-20, 40, 40);
        getButtons()[13].setBounds(550-20, 350-20, 40, 40);
        getButtons()[14].setBounds(650-20, 350-20, 40, 40);
        getButtons()[15].setBounds(250-20, 450-20, 40, 40);
        getButtons()[16].setBounds(350-20, 450-20, 40, 40);
        getButtons()[17].setBounds(450-20, 450-20, 40, 40);
        getButtons()[18].setBounds(150-20, 550-20, 40, 40);
        getButtons()[19].setBounds(350-20, 550-20, 40, 40);
        getButtons()[20].setBounds(550-20, 550-20, 40, 40);
        getButtons()[21].setBounds(50-20, 650-20, 40, 40);
        getButtons()[22].setBounds(350-20, 650-20, 40, 40);
        getButtons()[23].setBounds(650-20, 650-20, 40, 40);
    }

    private void createFrame(){
        getFrame().setBounds(0, 0, 1000, 750);
        getFrame().setLayout(new FlowLayout());
        getFrame().getContentPane().setBackground(Color.decode("#FDFD96"));
        getFrame().setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getFrame().setVisible(true);
        getFrame().setResizable(false);
    }

    private void createLabel(){
        getDraw().setBounds(0, 0, 1000, 750);
        getDraw().setVisible(true);
        getDraw().repaint();
    }

    private void createButtons(int i){
        getButtons()[i].setVisible(true);

        getButtons()[i].addActionListener(new ActionHandler(this));

        getButtons()[i].setFocusPainted(false);
        getButtons()[i].setContentAreaFilled(false);
        getButtons()[i].setBorder(null);
        getFrame().add(getButtons()[i]);
    }

    public synchronized void renderListPlayersResponse(ListPlayersResponse response){
        logger.debug("rendering ListPlayersResponse");
        //put Players in Combo Box, to choose one to play against
        if (!isListPlayersScreenEnabled()) {
            logger.debug("instantiating all UI-Objects for ListPlayersResponse once");

            //render refresh button
            JButton refreshListButton = new JButton("Aktualisiere Liste");
            refreshListButton.addActionListener(e -> {
                ListPlayersAction listPlayersAction = new ListPlayersAction(getPlayer());
                Thread socketWriter = new Thread(new SocketWriter(getWriterLock(), listPlayersAction, getOutputStream()));
                socketWriter.start();
            });
            refreshListButton.setPreferredSize(new Dimension(200, 50));
            getFrame().add(refreshListButton);

            //render player list
            getPlayerList().addActionListener(new ComboBoxListener(this));
            getPlayerList().setPreferredSize(new Dimension(300, 50));
            getFrame().add(getPlayerList());

            //render send request button
            JButton sendRequestButton = new JButton("Send request");
            sendRequestButton.setPreferredSize(new Dimension(200,50));
            sendRequestButton.addActionListener(e -> {
                if (getPlayerList().getSelectedItem() == null){
                    synchronized (this) {
                        JOptionPane.showMessageDialog(getFrame(), "Bitte Wähle einen Spieler aus der Liste aus");
                    }
                } else {
                    ConnectAction connectAction = new ConnectAction(getPlayer(), (Player) getPlayerList().getSelectedItem());
                    Thread socketWriter = new Thread(new SocketWriter(getWriterLock(),connectAction, getOutputStream()));
                    socketWriter.start();
                }
            });
            getFrame().add(sendRequestButton);
            setListPlayersScreenEnabled(true);
        }


        getPlayerList().removeAllItems();
        if (response.getPlayers().isEmpty()){
            JOptionPane.showMessageDialog(getFrame(), "Zurzeit befindet sich kein Spieler in der Warteschlange versuche die Liste in später zu aktualisieren.");
            getPlayerList().addItem(null);
        } else {
            for (Player player : response.getPlayers()){
                getPlayerList().addItem(player);
            }
        }
    }

    private void establishConnectionWithServer() throws IOException{
        Socket socket = new Socket("localhost", 5056);

        this.setOutputStream(socket.getOutputStream());

        Thread socketReader = new Thread(new SocketReader(socket, this));
        socketReader.start();
    }

    private synchronized void readNameAndSendInitialAction() throws IOException{
        String name = null;

        while (name == null || name.equals("")) {
            name = JOptionPane.showInputDialog(getFrame(), "Enter username for Player 1!");
        }
        InitialAction initialAction = new InitialAction(name);

        //send message in new thread, this thread also handles the synchronisation of the output stream
        Thread socketWriter = new Thread(new SocketWriter(getWriterLock(),initialAction,getOutputStream()));
        socketWriter.start();
    }

    public synchronized void renderGameResponse(GameResponse response) {
        logger.debug("rendering GameResponse");

        //todo: do shit
    }

    public Position getPosition(Coordinate coordinate){
        return getLastGameResponse().getGameField().stream().filter(p -> p.getCoordinate().equals(coordinate)).findFirst().get();
    }
}