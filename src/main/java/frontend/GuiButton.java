package frontend;

import javax.swing.*;
import java.awt.*;

public class GuiButton extends JButton {
    private Color pressedBackgroundColor;
    private Color hoverBackgroundColor;

    public GuiButton(String text){
        super(text);
        super.setContentAreaFilled(false);
    }

    public Color getPressedBackgroundColor() {
        return pressedBackgroundColor;
    }

    public void setPressedBackgroundColor(Color pressedBackgroundColor) {
        this.pressedBackgroundColor = pressedBackgroundColor;
    }

    public Color getHoverBackgroundColor() {
        return hoverBackgroundColor;
    }

    public void setHoverBackgroundColor(Color hoverBackgroundColor) {
        this.hoverBackgroundColor = hoverBackgroundColor;
    }

    @Override
    protected void paintComponent(Graphics g){
        if (getModel().isPressed()){
            g.setColor(getPressedBackgroundColor());
        } else if (getModel().isRollover()){
            g.setColor(getHoverBackgroundColor());
        } else {
            g.setColor(getBackground());
        }
        g.fillRect(0, 0, getWidth(), getHeight());
        super.paintComponent(g);
    }

}
