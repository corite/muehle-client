package frontend;

import logic.entities.Coordinate;
import logic.entities.StoneState;
import networking.SocketWriter;
import networking.entities.ActionType;
import networking.entities.GameAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import logic.entities.Position;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class ActionHandler implements ActionListener {


    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final Gui gui;

    public ActionHandler(Gui gui) {
        this.gui = gui;
    }

    public Gui getGui() {
        return gui;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (getGui().getLastGameResponse().getNextPlayerToMove().equals(getGui().getPlayer())){
            if (e.getSource() instanceof Button button) {
                Draw draw = getGui().getDraw();

                if (getGui().getLastGameResponse().getNextAction().equals(ActionType.TAKE)) {

                    //Sending Take Operation to Server if Action is Take
                    GameAction gameAction = new GameAction(getGui().getPlayer(), ActionType.TAKE, button.getCoordinate());
                    Thread socketWriter = new Thread(new SocketWriter(getGui().getWriterLock(), gameAction,getGui().getOutputStream()));
                    socketWriter.start();

                } else if (getGui().getLastGameResponse().getNextAction().equals(ActionType.PLACE)) {

                    //Sending Place Operation to Server if Action is Place

                    GameAction gameAction = new GameAction(getGui().getPlayer(), ActionType.PLACE, button.getCoordinate());
                    Thread socketWriter = new Thread(new SocketWriter(getGui().getWriterLock(), gameAction,getGui().getOutputStream()));
                    socketWriter.start();

                } else if (getGui().getLastGameResponse().getNextAction().equals(ActionType.MOVE)) {

                    //Sending Move Operation to Server if Action is Move

                    if ((getGui().getTmp() == null && !getGui().getPosition(button.getCoordinate()).getStoneState().equals(StoneState.NONE)) || getGui().getTmp() != null && !getGui().getPosition(button.getCoordinate()).getStoneState().equals(StoneState.NONE)) {

                        getGui().setTmp(button);
                        logger.debug("set tmp stone at coordinate " + button.getCoordinate());

                    } else if (getGui().getTmp() != null && getGui().getPosition(button.getCoordinate()).getStoneState().equals(StoneState.NONE)){
                        GameAction gameAction = new GameAction(getGui().getPlayer(), ActionType.MOVE, getGui().getTmp().getCoordinate(), button.getCoordinate());
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
