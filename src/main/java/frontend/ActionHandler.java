package frontend;

import logic.entities.StoneState;
import networking.SocketWriter;
import networking.entities.ActionType;
import networking.entities.GameAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class ActionHandler implements ActionListener {


    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final Gui gui;

    public ActionHandler(Gui gui) {
        this.gui = gui;
    }

    private Gui getGui() {
        return gui;
    }

    @Override
    public synchronized void actionPerformed(ActionEvent e) {
        if (getGui().getLastGameResponse().getNextPlayerToMove().getUser().equals(getGui().getUser())){
            if (e.getSource() instanceof GameButton gameButton) {
                Draw draw = getGui().getDraw();

                if (getGui().getLastGameResponse().getNextAction().equals(ActionType.TAKE)) {

                    //Sending Take Operation to Server if Action is Take
                    GameAction gameAction = new GameAction(getGui().getPlayerFromUser(getGui().getUser()), ActionType.TAKE, gameButton.getCoordinate());
                    Thread socketWriter = new Thread(new SocketWriter(getGui().getWriterLock(), gameAction,getGui().getOutputStream()));
                    socketWriter.start();

                } else if (getGui().getLastGameResponse().getNextAction().equals(ActionType.PLACE)) {

                    //Sending Place Operation to Server if Action is Place

                    GameAction gameAction = new GameAction(getGui().getPlayerFromUser(getGui().getUser()), ActionType.PLACE, gameButton.getCoordinate());
                    Thread socketWriter = new Thread(new SocketWriter(getGui().getWriterLock(), gameAction,getGui().getOutputStream()));
                    socketWriter.start();

                } else if (getGui().getLastGameResponse().getNextAction().equals(ActionType.MOVE)) {

                    //Sending Move Operation to Server if Action is Move

                    if ((getGui().getTmp() == null && !getGui().getPosition(gameButton.getCoordinate()).getStoneState().equals(StoneState.NONE)) || getGui().getTmp() != null && !getGui().getPosition(gameButton.getCoordinate()).getStoneState().equals(StoneState.NONE)) {

                        getGui().setTmp(gameButton);
                        logger.debug("set tmp stone at coordinate " + gameButton.getCoordinate());

                    } else if (getGui().getTmp() != null && getGui().getPosition(gameButton.getCoordinate()).getStoneState().equals(StoneState.NONE)){
                        GameAction gameAction = new GameAction(getGui().getPlayerFromUser(getGui().getUser()), ActionType.MOVE, getGui().getTmp().getCoordinate(), gameButton.getCoordinate());
                        Thread socketWriter = new Thread(new SocketWriter(getGui().getWriterLock(), gameAction,getGui().getOutputStream()));
                        socketWriter.start();

                        getGui().setTmp(null);
                    }

                } else {
                    logger.error("unknown Action '{}'",getGui().getLastGameResponse().getNextAction());
                }
                draw.repaint();
            }
        } else {
            logger.warn("not your turn");
        }
    }



}
