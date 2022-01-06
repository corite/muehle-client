package frontend;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

import logic.entities.Player;

public class Gui {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private JComboBox<Player> playerList;
    private Draw draw;
    private final JFrame frame;
    private final Button[]btn = new Button[24];
    private Button tmp = null;
    private Player player;
    private NetworkHandler networkHandler;

    public Gui() {

        //creating window and window settings

        frame = new JFrame("Muehle");
        frame.setBounds(0, 0, 1000, 750);
        frame.getContentPane().setBackground(Color.decode("#FDFD96"));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.setResizable(false);

        //get player name input through popup window, catch empty String or cancel Operation and initialize own Player Object

        try{
            networkHandler = new NetworkHandler(new Socket("localhost", 77777));
        } catch (IOException e) {
            logger.debug("Host was not found");
        }

        String name = null;

        while (name == null || name.equals("")) {
            name = JOptionPane.showInputDialog(frame, "Enter username for Player 1!");
        }

        player = networkHandler.handleInitialAction(name);

        ArrayList<Player> players = networkHandler.handleListPlayersAction(player);

        playerList = new JComboBox<>();
        for (Player player : players){
            playerList.addItem(player);
        }
        playerList.addActionListener();

        //creating JLabel from draw class and draw settings

        draw = new Draw(this);
        draw.setBounds(0, 0, 1000, 750);
        draw.setVisible(true);
        frame.add(draw);
        getDraw().repaint();

        //create buttons
/*
        ArrayList<Coordinate> coordinates = game.getField().nodes().stream()
                .map(Position::getCoordinate)
                .sorted(((o1, o2) -> o1.getY() == o2.getY() ? Integer.compare(o1.getX(), o2.getX()) : -Integer.compare(o1.getY(), o2.getY())))
                //sorts the collection so that the nodes can be traversed row by row from top to bottom
                .collect(Collectors.toCollection(ArrayList::new));

        for (int i=0; i< btn.length; i++){
            btn[i] = new Button(coordinates.get(i));
            btn[i].setVisible(true);
            btn[i].addActionListener(new ActionHandler(this));
            btn[i].setFocusPainted(false);
            btn[i].setContentAreaFilled(false);
            btn[i].setBorder(null);
            frame.add(btn[i]);
        }
        this.placeBtn();
*/
    }
/*
    public Button getBtn(int i){
        return btn[i];
    }
*/
    public Draw getDraw() {
        return draw;
    }

    public JComboBox getComboBox() {
        return playerList;
    }
/*
    public Button getTmp() {
        return tmp;
    }

    public void setTmp(Button tmp) {
        this.tmp = tmp;
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
    }*/
}