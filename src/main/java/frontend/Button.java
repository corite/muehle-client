package frontend;

import logic.entities.Coordinate;
import javax.swing.*;

public class Button extends JButton {
    private Coordinate coordinate;

    public Button(Coordinate coordinate) {
        this.coordinate = coordinate;
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }

    public void setCoordinates(int x, int y){
        this.coordinate = new Coordinate(x, y);
    }
}
