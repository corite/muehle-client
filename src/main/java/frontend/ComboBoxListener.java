package frontend;

import logic.entities.Player;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ComboBoxListener implements ActionListener{
    private final Gui gui;

    public ComboBoxListener(Gui gui){
        this.gui = gui;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        synchronized (gui){
            JComboBox comboBox = (JComboBox) e.getSource();
            Player player = (Player) comboBox.getSelectedItem();
        }
    }
}
