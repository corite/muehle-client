package frontend;

import logic.entities.Coordinate;
import logic.entities.StoneState;
import networking.entities.ActionType;
import networking.entities.GameAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import logic.entities.Position;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Collectors;

public class ActionHandler implements ActionListener {


    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final Gui gui;
    private final ObjectOutputStream outputStream;

    public ActionHandler(Gui gui, ObjectOutputStream outputStream) {
        this.gui = gui;
        this.outputStream = outputStream;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (gui.getGameResponse().isYourTurn()){
            if (e.getSource() instanceof Button button) {
                Draw draw = gui.getDraw();

                if (gui.getGameResponse().getNextAction().equals(ActionType.TAKE)) {

                    //Sending Take Operation to Server if Action is Take

                    try {
                        outputStream.writeObject(new GameAction(gui.getPlayer(), ActionType.TAKE, null, button.getCoordinate()));
                        outputStream.flush();
                    } catch (IOException ex) {
                        logger.debug("IO Error", ex);
                    }

                } else if (gui.getGameResponse().getNextAction().equals(ActionType.PLACE)) {

                    //Sending Place Operation to Server if Action is Place

                    try {
                        outputStream.writeObject(new GameAction(gui.getPlayer(), ActionType.PLACE, null, button.getCoordinate()));
                        outputStream.flush();
                    } catch (IOException ex) {
                        logger.debug("IO Error", ex);
                    }

                } else if (gui.getGameResponse().getNextAction().equals(ActionType.MOVE)) {

                    //Sending Move Operation to Server if Action is Move

                    if (gui.getTmp() == null || !getPosition(button.getCoordinate()).getStoneState().equals(StoneState.NONE)) {
                        gui.setTmp(button);
                        logger.debug("set tmp stone at coordinate " + button.getCoordinate());

                    } else {
                        try {
                            outputStream.writeObject(new GameAction(gui.getPlayer(), ActionType.MOVE, gui.getTmp().getCoordinate(), button.getCoordinate()));
                            outputStream.flush();
                            //todo dont forget to reset tmp Button in Response
                        } catch (IOException ex) {
                            logger.debug("IO Error", ex);
                        }
                    }

                } else {
                    logger.debug("Error, unknown Action");
                }
                draw.repaint();
            }
        } else {
            logger.info("Not your turn");
        }
    }

    private Position getPosition(Coordinate coordinate){
        return gui.getGameResponse().getGameField().stream().filter(p -> p.getCoordinate().equals(coordinate)).findFirst().get();
    }

}
