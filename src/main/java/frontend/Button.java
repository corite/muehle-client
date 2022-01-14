package frontend;

import logic.entities.Coordinate;
import javax.swing.*;

public class Button extends JButton {
    private final Coordinate coordinate;

    public Button(Coordinate coordinate) {
        this.coordinate = coordinate;
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }

}
