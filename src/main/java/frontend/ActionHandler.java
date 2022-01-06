/*package frontend;

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

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() instanceof Button) {
            Button button = (Button) e.getSource();
            Player player = gui.getGame().getNextPlayerToMove();
            GamePhase phase = player.getPhase();
            Draw draw = gui.getDraw();

            if (gui.getGame().isNextOperationTake()) {

                //try to perform take operation if the next operation is take and repaint JLabel properly

                try {
                    gui.getGame().takeStone(player, button.getCoordinate());
                    draw.repaint();
                    logger.debug("took Stone from coordinate " + button.getCoordinate());
                } catch (IllegalMoveException ex) {
                    logger.warn("Illegal Take", ex);
                }
            } else {
                try {

                    //try to perform different phase moves and repaint JLabel

                    switch (phase) {

                        case PLACE -> {
                            gui.getGame().placeStone(player, button.getCoordinate());
                            logger.debug("set stone at coordinate " + button.getCoordinate());
                        }

                        case MOVE, FLY -> {
                            if (gui.getTmp() == null || !gui.getGame().getPositionAtCoordinate(button.getCoordinate()).getStoneState().equals(StoneState.NONE)) {
                                gui.setTmp(button);
                                logger.debug("set tmp stone at coordinate " + button.getCoordinate());
                            } else {
                                gui.getGame().moveStone(player, gui.getTmp().getCoordinate(), button.getCoordinate());
                                logger.debug("moved stone from coordinate " + gui.getTmp().getCoordinate() + " to coordinate " + button.getCoordinate());
                                gui.setTmp(null);
                            }
                        }
                    }
                    draw.repaint();
                } catch (GameException ex) {
                    logger.warn("Illegal Move", ex);
                }
            }
        }
    }
}*/
