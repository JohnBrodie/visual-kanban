/*
 * John Brodie
 * jdb356@drexel.edu
 * CS338:GUI, Assignment 3
 */
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

public class Board implements Serializable {
	
	private static final long serialVersionUID = 6263645296828285792L;
	private String cname = getClass().getName();
	public BoardView view;
	BoardModel model;
	private StatsController stats;
	private boolean statsInit = false;
	protected JFileChooser fc = new JFileChooser();
	
	public Board() {
		model = new BoardModel("Visual Kanban");
		view = new BoardView(this);
		stats = new StatsController(this);
		Helpers.board = this;
		
		createDefaultColumns();
		/* Show the board's view component. */
		view.CreateGUI();
		setListeners();
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				view.setVisible(true);
			}
		});
	}
	
	/**
	 * Create a card in each column,
	 * called only if debug set.
	 */
	public void test() {
		String desc = "A small, simple task goes here...";
		for(Column col: model.getColumns()) {
			Card car = new Card("Name " + col.getPosition().toString(), desc,
					"red", "name here", "name here");
			col.addCard(car);
		}
	}
	
	public void resetCardDialog() {
		view.addCardTitleField.setText("Default Title");
		view.descriptionArea.setText("Default Description");
		view.creatorField.setText("Default Creator");
		view.assignedField.setText("Default Title");
		view.priorityBox.setSelectedIndex(0);
;
	}
	
	/**
	 * Display the add card dialog.
	 */
	protected void showAddCardDialog() {
		resetCardDialog();
		int result = JOptionPane.showConfirmDialog(null, view.addCardPanel, 
				"Card Details", JOptionPane.OK_CANCEL_OPTION, -1);
		if(result == JOptionPane.OK_OPTION) {
			Helpers.print_debug(cname, String.format("Got OK from add card dialog."));
			Card card = new Card(view.addCardTitleField.getText(), view.descriptionArea.getText(),
					view.priorityBox.getSelectedItem().toString(),
					view.creatorField.getText(), view.assignedField.getText());
			model.getFirstColumn().addCard(card);
		}
	}
	
	protected void showEditColumnDialog() {
		view.removeColumnBox.removeAllItems();
		for(Column col: model.getColumns()) {
			view.removeColumnBox.addItem(col);
		}
		int result = JOptionPane.showConfirmDialog(null, view.removeColumnPanel, 
				"Edit Column", JOptionPane.OK_CANCEL_OPTION);
		if(result == JOptionPane.OK_OPTION) {
			Column col = (Column) view.removeColumnBox.getSelectedItem();
			showEditColumnDialog(col);
		}
	}
	
	protected void showEditColumnDialog(Column col) {
		Integer posi = col.getPosition()+1;
		view.addColumnTitleField.setText(col.getName());
		view.addColumnWipField.setText(col.getWipLimit().toString());
		view.addColumnPosField.setText(posi.toString());
		int result = JOptionPane.showConfirmDialog(null, view.addColumnPanel, 
				"Column Details", JOptionPane.OK_CANCEL_OPTION);
		if(result == JOptionPane.OK_OPTION) {
			Integer wip = 0;
			Integer position = 0;
			String name = view.addColumnTitleField.getText(); 
			try {
				wip = Integer.parseInt(view.addColumnWipField.getText());
				position = Integer.parseInt(view.addColumnPosField.getText());
			} catch(NumberFormatException e) { }
			col.setName(name);
			col.setWipLimit(wip);
			calcColPosition(position);
			col.setPosition(position);
			update();
		}
	}
	
	protected void resetColumnDialog() {
		view.addColumnTitleField.setText("");
		view.addColumnWipField.setText("");
		view.addColumnPosField.setText("");
	}
	
	/**
	 * Display the add column dialog.
	 */
	protected void showAddColumnDialog() {
		this.resetColumnDialog();
		int result = JOptionPane.showConfirmDialog(null, view.addColumnPanel, 
				"Column Details", JOptionPane.OK_CANCEL_OPTION);
		if(result == JOptionPane.OK_OPTION) {
			Helpers.print_debug(cname, String.format("Got OK from add column dialog."));
			Integer wip = 0;
			Integer position = 0;
			String name = view.addColumnTitleField.getText(); 
			try {
				wip = Integer.parseInt(view.addColumnWipField.getText());
				position = Integer.parseInt(view.addColumnPosField.getText());
			} catch(NumberFormatException e) { }
			createColumn(name, position, wip);
		}
	}
	
	protected Card getSelectedCard() {
		return model.getSelectedCard();
	}
	
	protected void setSelectedCard(Card card) {
		model.setSelectedCard(card);
		view.editCardItem.setEnabled(true);
		view.removeCardItem.setEnabled(true);
	}
	
	protected void unselectCard() {
		Card current;
		current = model.getSelectedCard();
		if(current != null) {
			current.getView().setBorder(null);
		}
		model.setSelectedCard(null);
		view.editCardItem.setEnabled(false);
		view.removeCardItem.setEnabled(false);
	}
	
	protected void showStats() {
		if(!statsInit) {
			stats.view.CreateGUI();
			statsInit = true;
		}
		stats.getView().setVisible(true);
	}
	
	/**
	 * Display help dialog.
	 */
	protected void showHelpDialog() {
		String msg = "Use the File menu to add cards, and add/remove columns.\nDrag and Drop the cards as your work progresses.";
		JOptionPane.showConfirmDialog(null, msg,
				"Help", JOptionPane.PLAIN_MESSAGE, JOptionPane.INFORMATION_MESSAGE);
	}
	
	/**
	 * Display rename board dialog.
	 */
	protected void showRenameBoardDialog() {
		int result = JOptionPane.showConfirmDialog(null, view.renameBoardPanel,
				"Rename Board", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
		if(result == JOptionPane.OK_OPTION)
			model.setName(view.renameField.getText());
		view.renameField.setText("");
		update();
	}
	
	protected void showRemoveColumnDialog() {
		view.removeColumnBox.removeAllItems();
		for(Column col: model.getColumns()) {
			view.removeColumnBox.addItem(col);
		}
		int result = JOptionPane.showConfirmDialog(null, view.removeColumnPanel, 
				"Remove Column", JOptionPane.OK_CANCEL_OPTION);
		if(result == JOptionPane.OK_OPTION) {
			Helpers.print_debug(cname, "Got OK to remove column");
			Column col = (Column) view.removeColumnBox.getSelectedItem();
			removeColumn(col);
			update();
		}
	}
	
	protected void showRemoveCardDialog(Card card) {
		String msg = "Are you sure you want to remove this card?";
		int result = JOptionPane.showConfirmDialog(null, msg,
				"Remove Card", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
		if(result == JOptionPane.YES_OPTION)
			card.getColumn().removeCard(card);
	}
	
	protected void showRemoveColumnDialog(Column col) {
		String msg = "Are you sure you want to remove this column?";
		int result = JOptionPane.showConfirmDialog(null, msg,
				"Remove Column", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
		if(result == JOptionPane.YES_OPTION)
			removeColumn(col);
	}
	
	/**
	 * Display exit dialog.
	 */
	protected void showExitDialog() {
		String msg = "Are you sure you want to exit?";
		int result = JOptionPane.showConfirmDialog(null, msg,
				"Exit", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
		if(result == JOptionPane.YES_OPTION)
			System.exit(0);
	}
	
	/**
	 * Display about dialog.
	 */
	protected void showAboutDialog() {
		String msg = "Visual Kanban v1.0\nBy John Brodie";
		JOptionPane.showConfirmDialog(null, msg,
				"About", JOptionPane.PLAIN_MESSAGE, JOptionPane.INFORMATION_MESSAGE);
	}
	
	/**
	 * Display the save dialog.
	 */
	protected void showSaveDialog() {
		int retVal = fc.showSaveDialog(view);
		if(retVal == JFileChooser.APPROVE_OPTION) {
			File file = fc.getSelectedFile();
			FileOutputStream fout = null;
			ObjectOutputStream oos = null;
			try {
				fout = new FileOutputStream(file);
				oos = new ObjectOutputStream(fout);
				oos.writeObject(this.model);
				oos.close();
			} catch (FileNotFoundException e1) { e1.printStackTrace();
			} catch (IOException e) { e.printStackTrace(); }
			JOptionPane.showConfirmDialog(null, "Board has been saved.",
					"Save", JOptionPane.PLAIN_MESSAGE, JOptionPane.INFORMATION_MESSAGE);
		}
	}
	
	public void showLoadDialog() {
		int retVal = fc.showOpenDialog(Helpers.v);
		if(retVal == JFileChooser.APPROVE_OPTION) {
			File file = fc.getSelectedFile();
			FileInputStream fin = null;
			try {
				fin = new FileInputStream(file);
				ObjectInputStream inst = new ObjectInputStream(fin);
				BoardModel loaded = (BoardModel) inst.readObject();
				//Helpers.v.board = new Board();
				//Helpers.v.board.model = loaded;
				model = loaded;
			} catch (FileNotFoundException e2) { e2.printStackTrace();
			} catch (ClassNotFoundException e1) { e1.printStackTrace();
			} catch (IOException e1) { e1.printStackTrace(); }
			for(Column col : this.model.getColumns()) {
				col.setMouseListeners();
				col.getView().setTransferHandlers();
				for(Card card : col.getCards()) {
					System.out.println("** Setting card**" + card.getName());
					card.setListeners();
					card.getView().setTransferHandlers();
				}
			}
			this.update();
		}
	}
	/**
	 * Set up all listeners for the high level Board view.
	 */
	protected void setListeners() {
		view.saveButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				showSaveDialog(); }});
		view.saveItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				showSaveDialog(); }});
		view.loadItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				showLoadDialog(); }});
		view.aboutItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				showAboutDialog(); }});
		view.exitItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				showExitDialog(); }});
		view.exitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				showExitDialog(); }});
		view.addColumnItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				showAddColumnDialog(); }});
		view.editColumnItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				showEditColumnDialog(); }});
		view.addCardItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				showAddCardDialog(); }});
		view.helpItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				showHelpDialog(); }});
		view.statsItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				showStats(); }});
		view.removeColumnItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				showRemoveColumnDialog(); }});
		view.renameBoardItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				showRenameBoardDialog(); }});
		ActionListener details = new detailsListener();
		ActionListener remove = new removeListener();
		view.removeCardItem.addActionListener(remove);
		view.editCardItem.addActionListener(details);
	}
	
	/**
	 * Create default columns and add them to model:
	 */
	private void createDefaultColumns() {
		model.addColumn(new Column("ToDo", 0, 10));
		model.addColumn(new Column("Analysis", 1, 3));
		model.addColumn(new Column("Development", 2, 6));
		model.addColumn(new Column("Review", 3, 3));
		model.addColumn(new Column("Test", 4, 4));
		model.addColumn(new Column("Release Ready", 5, 3));
		model.addColumn(new Column("Done", 999, 0));
		test(); // TODO ?
	}
	
	/**
	 * Recursively move columns up one position to make room
	 * for new column
	 * @param position - Position of new column
	 */
	private void calcColPosition(Integer position) {
		Helpers.print_debug(cname, String.format("Checking position %d.", position));
		Column old = model.getColumnByPosition(position);
		if(old != null) {
			Helpers.print_debug(cname, String.format("Moving old column to %d.", position+1));
			calcColPosition(position+1);
			old.setPosition(position+1);
		}
	}
	
	/**
	 * Create a new column on this board.
	 */
	private void createColumn(String name, Integer position, Integer wipLimit) {
		calcColPosition(position);
		Column column = new Column(name, position, wipLimit);
		model.addColumn(column);
		update();
	}
	
	private Column getNextColumn(Integer position) {
		Column next = null;
		ArrayList<Column> cols = (ArrayList<Column>)model.getColumns();
		cols.trimToSize();
		boolean getnext = false;
		for(Column col: cols) {
			if(getnext) {
				next = col;
				break;
			}
			if(col.getPosition().equals(position))
				getnext = true;
		}
		return next;
	}
	
	private Column getPreviousColumn(Integer position) {
		Column prev = null;
		ArrayList<Column> cols = (ArrayList<Column>)model.getColumns();
		cols.trimToSize();
		for(Column col: cols) {
			if(col.getPosition().equals(position))
				continue;
			prev = col;
		}
		return prev;
	}
	
	private void moveCards(List<Card> cards, Column oldCol, Column newCol) {
		Helpers.print_debug(cname, String.format("Moving cards from col %d to %d.", oldCol.getPosition(), newCol.getPosition()));
		List<Card> tempCards = new ArrayList<Card>();
		tempCards.addAll(cards);
		for(Card card : cards)
			newCol.addCard(card);
		for(Card card : tempCards)
			oldCol.removeCard(card);
	}
	
	/**
	 * Remove a column from the board.
	 * @param column - The column to remove.
	 */
	private void removeColumn(Column column) {
		if(!column.isEmpty()) {
			Column newCol = getNextColumn(column.getPosition());
			Column prevCol = getPreviousColumn(column.getPosition());
			if(newCol != null) {
				moveCards(column.getCards(), column, newCol);
			} else if(prevCol != null) { // if there's a previous col
				moveCards(column.getCards(), column, prevCol);
			} else {
				JOptionPane.showMessageDialog(view, "No column to move cards to!", "Column Error", JOptionPane.ERROR_MESSAGE);
				return;
			}
		}
		model.removeColumn(column);
		this.update();
	}
	
	/**
	 * Get model of board.
	 */
	public BoardModel getModel() {
		return model;
	}
	
	/**
	 * Get name of board.
	 */
	public String getName() {
		return model.getName();
	}
	
	protected void resizeColumns() {
		int numCards = 0;
		for(Column col : model.getColumns()) {
			int temp = col.getCards().size();
			if(temp > numCards)
				numCards = temp;
		}
		int calcd = 150 + (220*numCards);
		if(calcd < view.getHeight())
			calcd = view.getHeight();
		Helpers.print_debug(cname, String.format("Setting colHeight to %d", 150+(220*numCards)));
		Helpers.colHeight = calcd;
	}
	
	/**
	 * Refresh columns/title based on model data, repaint board
	 * Called whenever a column is added through createColumn,
	 * and initially at the end of boardView's createGUI
	 */
	public void update() {
		Helpers.print_debug(cname, "Update called on board view.");
		view.boardNameLabel.setText(getName());
		/* Add Columns */
		view.columnPanel.removeAll();
		int x = 0;
		model.sortColumns();
		for(Column col: model.getColumns()) {
			col.setPosition(x);
			view.columnPanel.add(col.getView());
			x++;
		}
		resizeColumns();
		view.columnPanel.revalidate();
		view.columnPanel.repaint();
		view.getContentPane().repaint();
	}
}