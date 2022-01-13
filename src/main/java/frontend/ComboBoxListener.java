package frontend;

import logic.entities.Player;
import networking.SocketWriter;
import networking.entities.ConnectAction;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ComboBoxListener implements ActionListener{
    private final Gui gui;

    public ComboBoxListener(Gui gui){
        this.gui = gui;
    }

    public Gui getGui() {
        return gui;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        synchronized (getGui()){
            JComboBox comboBox = (JComboBox) e.getSource();
            Player player = (Player) comboBox.getSelectedItem();
            if (player != null) {
                ConnectAction connectAction = new ConnectAction(getGui().getPlayer(), player);
                Thread socketWriter = new Thread(new SocketWriter(getGui().getWriterLock(), connectAction, getGui().getOutputStream()));
                socketWriter.start();
            }
        }
        //todo: @burned: können wir damit den SendRequestButton wegrationalisieren, sodass die anfrage direkt bei der auswahl gesendet wird?
        // Wenn ja wäre das denke Ich cool.
    }
}
