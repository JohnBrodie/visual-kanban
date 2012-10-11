/*
 * John Brodie
 * jdb356@drexel.edu
 * CS338:GUI, Assignment 3
 */
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;

import javax.swing.JOptionPane;

public class removeListener implements ActionListener, Serializable {
	private static final long serialVersionUID = -1232414858310999521L;
	public void actionPerformed(ActionEvent e) {
		Card card = Helpers.board.getModel().getSelectedCard();
		if(card == null) {
			String msg = "Please click a card to select it before using this option.";
			JOptionPane.showConfirmDialog(null, msg,
					"Select a Card", JOptionPane.PLAIN_MESSAGE, JOptionPane.WARNING_MESSAGE);
			return;
		}
		card.getColumn().showCardRemoveDialog(card); 
	}
}
