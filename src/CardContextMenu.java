import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;


public class CardContextMenu extends JPopupMenu {
	private static final long serialVersionUID = 5940741478382172341L;

	public CardContextMenu(final Card card) {
		JMenuItem removeItem = new JMenuItem("Remove");
		JMenuItem editItem = new JMenuItem("Edit");
		JMenuItem title = new JMenuItem(card.getName());
		title.setEnabled(false);
		add(title);
		addSeparator();
		add(editItem);
		add(removeItem);
		editItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				card.getColumn().showCardDetailsDialog(card); }});
		removeItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Helpers.board.showRemoveCardDialog(card); }});
	}
}
