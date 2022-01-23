package frontend;

import logic.entities.Player;
import logic.entities.StoneState;
import logic.entities.User;

import javax.swing.*;
import java.awt.*;

public class Draw extends JLabel {
    private final Gui gui;

    public Draw(Gui gui) {
        this.gui = gui;
    }

    private Gui getGui() {
        return gui;
    }

    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);

        g.setColor(Color.BLACK);

        //outer rectangle

        g.drawRect(50,50, 600,600);

        //middle rectangle

        g.drawRect(150,150, 400,400);

        //inner rectangle

        g.drawRect(250, 250, 200, 200);

        //lines in between

        g.drawLine(50,350,250, 350);
        g.drawLine(450,350,650,350);
        g.drawLine(350,50,350,250);
        g.drawLine(350,450,350,650);

        //filled rectangles on the outer rectangle top and bottom line

        for (int i=0; i<=1;i++){
            for (int j=0; j<=2;j++){
                g.fillRect(50 + j*300-5,50 + i*600-5,10,10);
            }
        }

        //filled rectangles on the middle rectangle top and bottom line

        for (int i=0; i<=1;i++){
            for (int j=0; j<=2;j++){
                g.fillRect(150 + j*200-5,150 + i*400-5,10,10);
            }
        }

        //filled rectangles on the inner rectangle top and bottom line

        for (int i=0; i<=1;i++){
            for (int j=0; j<=2;j++){
                g.fillRect(250 + j*100-5,250 + i*200-5,10,10);
            }
        }

        //filled rectangles on the middle line, left side

        for (int i=0; i<=2;i++){
            g.fillRect(50 + i*100-5,350-5,10,10);
        }

        //filled rectangles on the middle line, right side

        for (int i=0; i<=2;i++){
            g.fillRect(450 + i*100-5,350-5,10,10);
        }

        //iterate through all Buttons

        for (int i=0; i<=23;i++){
            if (getGui().getBtn(i) != null) {

                //draw Stones on Button Coordinates

                if (!getGui().getPosition(getGui().getBtn(i).getCoordinate()).getStoneState().equals(StoneState.NONE)) {
                    Color btnColor = getColor(getGui().getPosition(getGui().getBtn(i).getCoordinate()).getStoneState());
                    g.setColor(btnColor);
                    g.fillOval(getGui().getBtn(i).getX(), getGui().getBtn(i).getY(), 40, 40);
                    g.setColor(getOtherColor(btnColor));
                    g.drawOval(getGui().getBtn(i).getX() + 5, getGui().getBtn(i).getY() + 5, 30, 30);
                    g.drawOval(getGui().getBtn(i).getX() + 10, getGui().getBtn(i).getY() + 10, 20, 20);
                    if (Color.BLACK.equals(btnColor)) {
                        g.setColor(Color.BLACK);
                    }
                }

                //draw red circle around selected Button during move/fly phase

                if (getGui().getTmp() != null){
                    g.setColor(Color.RED);
                    g.drawOval(getGui().getTmp().getX(), getGui().getTmp().getY(), 40, 40);
                    g.setColor(Color.BLACK);
                }
            }
        }

        //draw String of moving/winning player

        g.setFont(g.getFont().deriveFont(g.getFont().getSize() * 1.4F));
        User winningUser = getGui().getWinningUser();
        if (winningUser != null){
            g.drawString(getResizedString(winningUser.toString()) + " hat gewonnen.", 700, 50);
        }
        else if (getGui().getLastGameResponse().getNextPlayerToMove().getUser().equals(getGui().getUser())){
            switch (getGui().getLastGameResponse().getNextAction()){
                case PLACE -> g.drawString("Du musst einen Stein setzen!", 700, 50);
                case TAKE -> g.drawString("Du musst einen Stein nehmen!", 700, 50);
                case MOVE -> g.drawString("Du musst einen Stein bewegen!", 700, 50);
            }
        }else {
            switch (getGui().getLastGameResponse().getNextAction()) {
                case PLACE -> g.drawString("Dein Gegner setzt einen Stein!", 700, 50);
                case TAKE -> g.drawString("Dein Gegner nimmt einen Stein!", 700, 50);
                case MOVE -> g.drawString("Dein Gegner zieht einen Stein!", 700, 50);
            }
        }

        g.drawString(getResizedString(getGui().getUser().toString()) + " spielt " + getColorAsString(getGui().getPlayerFromUser(getGui().getUser()).getColor()) + ".", 700, 100);
        g.drawString(getResizedString(getGui().getOpposingUser().toString()) + " spielt " + getColorAsString(getGui().getPlayerFromUser(getGui().getOpposingUser()).getColor()) + ".", 700, 125);

        //draw remaining Stones of the players

        for (int i=0; i<=8-getPlayerWithColor(StoneState.WHITE).getPlacedStones(); i++){
            g.setColor(Color.WHITE);
            g.fillOval(700, 600-i*50,40,40);
            g.setColor(Color.BLACK);
            g.drawOval(700+5, 600-i*50+5, 30, 30);
            g.drawOval(700+10, 600-i*50+10, 20, 20);
        }

        for (int i=0; i<=8-getPlayerWithColor(StoneState.BLACK).getPlacedStones(); i++){
            g.setColor(Color.BLACK);
            g.fillOval(750, 600-i*50,40,40);
            g.setColor(Color.WHITE);
            g.drawOval(750+5, 600-i*50+5, 30, 30);
            g.drawOval(750+10, 600-i*50+10, 20, 20);
            g.setColor(Color.BLACK);
        }
    }

    private String getResizedString(String a) {
        if (a != null) {
            if (a.length() > 10) {
                return a.substring(0, 10) + "...";
            }else{
                return a;
            }
        }else {
            return "";
        }
    }

    private String getColorAsString(StoneState s) {
        if (StoneState.WHITE.equals(s)) {
            return "Weiss";
        } else if (StoneState.BLACK.equals(s)){
            return "Schwarz";
        } else throw new IllegalArgumentException("unknown StoneState");
    }

    private Color getOtherColor(Color color) {
        return Color.WHITE.equals(color) ? Color.BLACK : Color.WHITE;
    }

    private Color getColor(StoneState stoneState) {
        if (StoneState.WHITE.equals(stoneState)) {
            return Color.WHITE;
        } else if (StoneState.BLACK.equals(stoneState)) {
            return Color.BLACK;
        } else throw new IllegalArgumentException("unknown StoneState");
    }

    private Player getPlayerWithColor(StoneState stoneState){
        if (getGui().getPlayerFromUser(getGui().getUser()).getColor().equals(stoneState)){
            return getGui().getPlayerFromUser(getGui().getUser());
        }else return getGui().getPlayerFromUser(getGui().getOpposingUser());
    }
}
