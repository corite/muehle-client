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
        JComboBox comboBox = (JComboBox) e.getSource();
        Player player = (Player) comboBox.getSelectedItem();
        //todo: @burned: können wir damit den SendRequestButton wegrationalisieren, sodass die anfrage direkt bei der auswahl gesendet wird?
        // Wenn ja wäre das denke Ich cool.
    }
}
