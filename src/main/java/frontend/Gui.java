package frontend;

import logic.entities.*;
import networking.SocketReader;
import networking.SocketWriter;
import networking.entities.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class Gui {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final JComboBox<User> userList = new JComboBox<>();
    private final Draw draw = new Draw(this);
    private final JFrame frame = new JFrame("Muehle");
    private final Button[] buttons = new Button[24];
    private Button tmp = null;

    private User user = null;
    private GameResponse lastGameResponse;

    private OutputStream outputStream;
    private final Object writerLock = new Object();

    private boolean isListPlayersScreenEnabled;
    private boolean isGameScreenEnabled;



    public Gui() throws IOException {

        //creating window and window settings

        this.createFrame();

        //create Socket and Thread for NetworkHandler class

        this.establishConnectionWithServer();


        //get player name input through popup window, catch empty String or cancel Operation

        this.readNameAndSendInitialAction();

    }

    public GameResponse getLastGameResponse() {
        return lastGameResponse;
    }

    public void setLastGameResponse(GameResponse lastGameResponse) {
        this.lastGameResponse = lastGameResponse;
        if (getUser().toString().equals(lastGameResponse.getNextPlayerToMove().getUser().toString())){
            setUser(lastGameResponse.getNextPlayerToMove().getUser());
        }else setUser(lastGameResponse.getOtherPlayer().getUser());
    }

    private boolean isListPlayersScreenEnabled() {
        return isListPlayersScreenEnabled;
    }

    private void setListPlayersScreenEnabled(boolean listPlayersScreenEnabled) {
        isListPlayersScreenEnabled = listPlayersScreenEnabled;
    }

    private JComboBox<User> getUserList() {
        return userList;
    }

    public JFrame getFrame() {
        return frame;
    }

    private Button[] getButtons() {
        return buttons;
    }

    public OutputStream getOutputStream() {
        return outputStream;
    }

    private void setOutputStream(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    public Object getWriterLock() {
        return writerLock;
    }

    public Button getBtn(int i){
        return buttons[i];
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Draw getDraw() {
        return draw;
    }

    public Button getTmp() {
        return tmp;
    }

    public void setTmp(Button tmp) {
        this.tmp = tmp;
    }

    private boolean isGameScreenEnabled() {
        return isGameScreenEnabled;
    }

    private void setGameScreenEnabled(boolean isGameScreenEnabled) {
        this.isGameScreenEnabled = isGameScreenEnabled;
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
        getFrame().getContentPane().setBackground(Color.decode("#2b2b2b"));
        getFrame().setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getFrame().setVisible(true);
        getFrame().setResizable(false);
    }

    private void createLabel(){
        getDraw().setBounds(0, 0, 1000, 750);
        getDraw().setVisible(true);
        getDraw().repaint();
    }

    private void createButton(int i){
        getButtons()[i].setVisible(true);

        getButtons()[i].addActionListener(new ActionHandler(this));

        getButtons()[i].setFocusPainted(false);
        getButtons()[i].setContentAreaFilled(false);
        getButtons()[i].setBorder(null);
        getFrame().add(getButtons()[i]);
    }

    private void createButtons(){
        ArrayList<Coordinate> coordinates = getLastGameResponse().getGameField().stream()
                .map(Position::getCoordinate)
                .sorted(((o1, o2) -> o1.getY() == o2.getY() ? Integer.compare(o1.getX(), o2.getX()) : -Integer.compare(o1.getY(), o2.getY())))
                //sorts the collection so that the nodes can be traversed row by row from top to bottom
                .collect(Collectors.toCollection(ArrayList::new));

        for (int i=0; i< getButtons().length; i++){
            getButtons()[i] = new Button(coordinates.get(i));
            this.createButton(i);
        }
        this.placeBtn();
    }

    public synchronized void renderListUsersResponse(ListUsersResponse response){
        logger.debug("rendering ListPlayersResponse");
        if (!isListPlayersScreenEnabled()) {
            logger.debug("instantiating all UI-Objects for ListPlayersResponse once");

            //render refresh button
            JButton refreshListButton = new JButton("Aktualisiere Liste");
            refreshListButton.addActionListener(e -> {
                ListUsersAction listUsersAction = new ListUsersAction(getUser());
                Thread socketWriter = new Thread(new SocketWriter(getWriterLock(), listUsersAction, getOutputStream()));
                socketWriter.start();
                logger.debug("sending ListUsersAction");
            });
            refreshListButton.setPreferredSize(new Dimension(200, 50));
            getFrame().add(refreshListButton);

            //render player list
            getUserList().setPreferredSize(new Dimension(300, 50));
            getFrame().add(getUserList());

            //render connect button
            JButton connectButton = new JButton("Anfrage senden");
            connectButton.addActionListener(e -> {
                ConnectAction connectAction = new ConnectAction(getUser(), (User) getUserList().getSelectedItem());
                Thread socketWriter = new Thread(new SocketWriter(getWriterLock(), connectAction, getOutputStream()));
                socketWriter.start();
                logger.debug("sending ConnectAction");
            });
            connectButton.setPreferredSize(new Dimension(200, 50));
            getFrame().add(connectButton);

            setListPlayersScreenEnabled(true);
            setGameScreenEnabled(false);
        }

        //if Player Screen is already enabled refresh ComboBox
        getUserList().removeAllItems();
        if (response.getUsers().isEmpty()){
            JOptionPane.showMessageDialog(getFrame(), "Zurzeit befindet sich kein Spieler in der Warteschlange versuche die Liste in später zu aktualisieren.");
            getUserList().addItem(null);
        } else {
            for (User user : response.getUsers()){
                getUserList().addItem(user);
            }
        }
    }

    private void establishConnectionWithServer() throws IOException{
        Socket socket = new Socket("localhost", 5056);

        this.setOutputStream(socket.getOutputStream());

        Thread socketReader = new Thread(new SocketReader(socket, this));
        socketReader.start();
    }

    public void loginFailedAction(String message){
        JOptionPane.showMessageDialog(getFrame() ,message);
    }

    public synchronized void readNameAndSendInitialAction() {
        JTextField username = new JTextField(15);
        JPasswordField password = new JPasswordField(15);
        JCheckBox register = new JCheckBox();

        JPanel registration = new JPanel(new GridLayout(0, 2));
        registration.add(new JLabel("Username"));
        registration.add(username);
        registration.add(new JLabel("Passwort"));
        registration.add(password);
        registration.add(new JLabel("Erste Registrierung?"));
        registration.add(register);
        int result = -1;
        while (result != 0) {
            result = JOptionPane.showConfirmDialog(getFrame(), registration, "Einloggen oder Registrieren", JOptionPane.OK_CANCEL_OPTION);
        }
        RegisterLoginUserAction loginUserAction = new RegisterLoginUserAction(username.getText(), new String(password.getPassword()), register.isSelected());

        //send message in new thread, this thread also handles the synchronisation of the output stream
        Thread socketWriter = new Thread(new SocketWriter(getWriterLock(),loginUserAction,getOutputStream()));
        socketWriter.start();
        logger.debug("Initial Action was sent to server");
    }

    public synchronized void renderGameResponse(GameResponse response) {
        logger.debug("rendering GameResponse");

        setLastGameResponse(response);

        if (!isGameScreenEnabled()) {
            logger.debug("instantiating all UI Objects for game screen once");
            getFrame().setTitle(getUser() + " spielt Muehle gegen " + getOpposingUser());

            getFrame().getContentPane().removeAll();

            // create Buttons only possible, after GameResponse was received

            this.createButtons();

            //creating JLabel from draw class and draw settings

            this.createLabel();

            getFrame().add(getDraw());

            for (int i = 0; i < getButtons().length; i++) {
                getFrame().add(getBtn(i));
            }

            placeEndSessionButton();

            getFrame().repaint();
            setGameScreenEnabled(true);
            setListPlayersScreenEnabled(false);
        }
        else {
            //if Player wins Game show Popup window to close client/return to main menu
            if (getLastGameResponse().getNextPlayerToMove().getPhase().equals(GamePhase.WON) || getLastGameResponse().getOtherPlayer().getPhase().equals(GamePhase.WON)){
                Object[] options = {"Zurueck zur Spielersuche", "Client schliessen"};
                int result = JOptionPane.showOptionDialog(getFrame(), getWinningUser() + " hat gewonnen!", "Spiel wurde beendet", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, null);
                renderPopupReturnCloseDialog(result);
            }
            getDraw().repaint();
        }

    }

    private synchronized void placeEndSessionButton(){
        JButton endSession = new JButton("Spiel Beenden");
        endSession.setBounds(770, 650, 200, 50);
        endSession.addActionListener(e -> {
            EndSessionAction endSessionAction = new EndSessionAction(getUser());
            Thread endSessionWriter = new Thread(new SocketWriter(getWriterLock(), endSessionAction, getOutputStream()));
            endSessionWriter.start();
            logger.debug("sending EndSessionAction");
        });
        getFrame().add(endSession);
    }

    public synchronized void renderEndGameResponse(EndGameResponse response){
        if (!isListPlayersScreenEnabled()) {
            logger.debug("rendering EndSessionResponse");
            if (!response.getMessage().contains(getUser().toString())) {
                JOptionPane.showMessageDialog(getFrame(), response.getMessage());
            }
            getFrame().setTitle(getUser() + " spielt Muehle");
            getFrame().getContentPane().removeAll();
            getFrame().repaint();
            ListUsersAction listPlayersAction = new ListUsersAction(getUser());
            Thread listPlayerWriter = new Thread(new SocketWriter(getWriterLock(), listPlayersAction, getOutputStream()));
            listPlayerWriter.start();
            logger.debug("sending ListPlayersAction");
        }
    }

    public synchronized void renderDisconnectResponse(DisconnectResponse response){
        //if player disconnects from game show popup window to close client/return to main menu
        Object[] options = {"Zurueck zur Spielersuche", "Client schliessen"};
        int result = JOptionPane.showOptionDialog(getFrame(), response.getDisconnectedPlayer() + " hat das Spiel verlassen!", "Spiel wurde beendet", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, null);
        renderPopupReturnCloseDialog(result);
    }

    public Position getPosition(Coordinate coordinate){
        return getLastGameResponse().getGameField().stream().filter(p -> p.getCoordinate().equals(coordinate)).findFirst().get();
    }

    public User getOpposingUser(){
        if (getLastGameResponse().getNextPlayerToMove().getUser().equals(getUser())){
            return getLastGameResponse().getOtherPlayer().getUser();
        } else return getLastGameResponse().getNextPlayerToMove().getUser();
    }

    public User getWinningUser(){
        if (getLastGameResponse().getNextPlayerToMove().getPhase().equals(GamePhase.WON)){
            return getLastGameResponse().getNextPlayerToMove().getUser();
        } else if (getLastGameResponse().getOtherPlayer().getPhase().equals(GamePhase.WON)){
            return getLastGameResponse().getOtherPlayer().getUser();
        }else{
            return null;
        }
    }

    public Player getPlayerFromUser(User user){
        if (getLastGameResponse().getNextPlayerToMove().getUser().equals(user)){
            return getLastGameResponse().getNextPlayerToMove();
        }else return getLastGameResponse().getOtherPlayer();
    }

    private synchronized void renderPopupReturnCloseDialog(int result){
        //handle input of the win game/disconnect popup
        if (result == JOptionPane.YES_OPTION){
            EndSessionAction endSessionAction = new EndSessionAction(getUser());
            Thread endSessionWriter = new Thread(new SocketWriter(getWriterLock(), endSessionAction, getOutputStream()));
            endSessionWriter.start();
            logger.debug("sending EndSessionAction");
        } else if (result == JOptionPane.NO_OPTION){
            System.exit(0);
        }
    }

}
