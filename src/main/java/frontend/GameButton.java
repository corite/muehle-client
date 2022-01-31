package frontend;

import logic.entities.Coordinate;
import javax.swing.*;

public class GameButton extends JButton {
    private final Coordinate coordinate;

    public GameButton(Coordinate coordinate) {
        this.coordinate = coordinate;
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }

}
