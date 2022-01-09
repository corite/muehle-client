package frontend;

import javax.swing.*;
import java.awt.*;

public class Draw extends JLabel {
    private Gui gui;

    public Draw(Gui gui) {
        this.gui = gui;
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
/*
        for (int i=0; i<=23;i++){
            if (gui.getBtn(i) != null) {

                //draw Stones on Button Coordinates

                if (gui.getGame().getPositionAtCoordinate(gui.getBtn(i).getCoordinate()).getStoneState().equals(StoneState.BLACK)) {
                    g.setColor(Color.BLACK);
                    g.fillOval(gui.getBtn(i).getX(), gui.getBtn(i).getY(), 40, 40);
                    g.setColor(Color.WHITE);
                    g.drawOval(gui.getBtn(i).getX()+5, gui.getBtn(i).getY()+5, 30, 30);
                    g.drawOval(gui.getBtn(i).getX()+10, gui.getBtn(i).getY()+10, 20, 20);
                    g.setColor(Color.BLACK);
                } else if (gui.getGame().getPositionAtCoordinate(gui.getBtn(i).getCoordinate()).getStoneState().equals(StoneState.WHITE)) {
                    g.setColor(Color.WHITE);
                    g.fillOval(gui.getBtn(i).getX(), gui.getBtn(i).getY(), 40, 40);
                    g.setColor(Color.BLACK);
                    g.drawOval(gui.getBtn(i).getX()+5, gui.getBtn(i).getY()+5, 30, 30);
                    g.drawOval(gui.getBtn(i).getX()+10, gui.getBtn(i).getY()+10, 20, 20);
                }

                //draw red circle around selected Button during move/fly phase

                if (gui.getTmp() != null){
                    g.setColor(Color.RED);
                    g.drawOval(gui.getTmp().getX(), gui.getTmp().getY(), 40, 40);
                    g.setColor(Color.BLACK);
                }
            }
        }

        //draw String of moving/winning player

        g.setFont(g.getFont().deriveFont(g.getFont().getSize() * 1.4F));
        if (gui.getGame().getPlayer1().getPhase().equals(GamePhase.WON)) {
            g.drawString(getResizedString(gui.getGame().getPlayer1().getName()) + " hat gewonnen.", 700, 50);
        }
        else if (gui.getGame().getPlayer2().getPhase().equals(GamePhase.WON)) {
            g.drawString(getResizedString(gui.getGame().getPlayer2().getName()) + " hat gewonnen.", 700, 50);
        }
        else {
            g.drawString(getResizedString(gui.getGame().getNextPlayerToMove().getName()) + " ist am Zug.", 700, 50);
        }
        g.drawString(getResizedString(gui.getGame().getPlayer1().getName()) + " spielt " + getColorAsString(gui.getGame().getPlayer1().getColor()) + ".", 700, 100);
        g.drawString(getResizedString(gui.getGame().getPlayer2().getName()) + " spielt " + getColorAsString(gui.getGame().getPlayer2().getColor()) + ".", 700, 125);

        //draw remaining Stones of the players

        for (int i=0; i<=8-gui.getGame().getPlayer1().getPlacedStones(); i++){
            g.setColor(Color.WHITE);
            g.fillOval(700, 600-i*50,40,40);
            g.setColor(Color.BLACK);
            g.drawOval(700+5, 600-i*50+5, 30, 30);
            g.drawOval(700+10, 600-i*50+10, 20, 20);
        }

        for (int i=0; i<=8-gui.getGame().getPlayer2().getPlacedStones(); i++){
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
        if (s.equals(StoneState.WHITE)) {
            return "Weiss";
        } else {
            return "Schwarz";
        }*/
    }
}
