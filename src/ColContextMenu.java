import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;


public class ColContextMenu extends JPopupMenu {
	private static final long serialVersionUID = 5940741478382172341L;

	public ColContextMenu(final Column col) {
		JMenuItem addItem = new JMenuItem("Add");
		JMenuItem removeItem = new JMenuItem("Remove");
		JMenuItem editItem = new JMenuItem("Edit");
		JMenuItem title = new JMenuItem(col.getName());
		title.setEnabled(false);
		add(title);
		addSeparator();
		add(addItem);
		add(editItem);
		add(removeItem);
		addItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Helpers.board.showAddColumnDialog(); }});
		editItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Helpers.board.showEditColumnDialog(col); }});
		removeItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Helpers.board.showRemoveColumnDialog(col); }});
	}
}