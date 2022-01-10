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
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
        synchronized (gui){
            JComboBox comboBox = (JComboBox) e.getSource();
            Player player = (Player) comboBox.getSelectedItem();
        }
=======
        JComboBox comboBox = (JComboBox) e.getSource();
        Player player = (Player) comboBox.getSelectedItem();
        //todo: @burned: können wir damit den SendRequestButton wegrationalisieren, sodass die anfrage direkt bei der auswahl gesendet wird?
        // Wenn ja wäre das denke Ich cool.
>>>>>>> 6b3076bcc6367262875af541f67ba07f8fad45d3
=======
        JComboBox comboBox = (JComboBox) e.getSource();
        Player player = (Player) comboBox.getSelectedItem();
>>>>>>> parent of 9c40130 (Added some stuff)
=======
        JComboBox comboBox = (JComboBox) e.getSource();
        Player player = (Player) comboBox.getSelectedItem();
>>>>>>> parent of 9c40130 (Added some stuff)
    }
}
