/*
 * John Brodie
 * jdb356@drexel.edu
 * CS338:GUI, Assignment 3
 */
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

public class Column implements Serializable {
	
	private static final long serialVersionUID = 3201072090934008159L;
	private String name = "Default Name";
	private Integer wipLimit = 0;
	private List<Card> cards = new ArrayList<Card>();
	UUID id = UUID.randomUUID();
	private Integer position;
	private String cname = getClass().getName();
	public ColumnView view;
	
	/**
	 * Column constructor
	 * @param name - name of column
	 * @param position - position for this column
	 */
	public Column(String name, Integer position, Integer wipLimit) {
		this.name = name;
		this.position = position;
		this.wipLimit = wipLimit;
		this.view = new ColumnView(this);
		setMouseListeners();
	}
	
	public boolean isEmpty() {
		return cards.isEmpty();
	}
	
	public String toString() {
		return name;
	}
	
	/**
	 * Get card by id
	 */
	public Card getCardByID(long cardID) {
		if(!cards.isEmpty())
			for(Card car : cards) {
				if(car.getId() == cardID)
					return car;
			}
		return null;
	}
	
	/**
	 * Get list of cards in column.
	 * @return cards - List of type card.
	 */
	public List<Card> getCards() {
		return cards;
	}

	/**
	 * Add a card to this column
	 * @param card - card to add
	 */
	public void addCard(final Card card) {
        Helpers.print_debug(cname, String.format("Adding card %s.", card.getName()));
		card.setColumn(this);
		cards.add(card);
		Helpers.board.update();
		update();
	}
	
	protected void showCardRemoveDialog(Card card) {
		Helpers.print_debug(cname, "Showing card remove dialog");
		String msg = "Are you sure you want to remove this card?";
		int result = JOptionPane.showConfirmDialog(null, msg,
				"Exit", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
		if(result == JOptionPane.YES_OPTION)
			card.getColumn().removeCard(card);
	}
	
	protected void showCardDetailsDialog(Card card) {
		Helpers.print_debug(cname, "Showing card details");
		BoardView bv = Helpers.board.view;
		bv.addCardTitleField.setText(card.getName());
		bv.descriptionArea.setText(card.getDescription());
		bv.assignedField.setText(card.getAssigned());
		bv.creatorField.setText(card.getCreator());
		bv.priorityBox.setSelectedItem(card.getColor().toString());
		DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
		bv.createdField.setText(df.format(card.getCreatedDate()));
		int result = JOptionPane.showConfirmDialog(null, bv.addCardPanel, 
				"Card Details", JOptionPane.OK_CANCEL_OPTION, -1);
		if(result == JOptionPane.OK_OPTION) {
			card.setName(bv.addCardTitleField.getText());
			card.setDescription(bv.descriptionArea.getText());
			card.setAssigned(bv.assignedField.getText());
			card.setCreator(bv.creatorField.getText());
			card.setColor(bv.priorityBox.getSelectedItem().toString());
		}
		Helpers.board.resetCardDialog();
		update();
	}
		
	/**
	 * Remove a card from this column
	 * @param card - card to remove
	 */
	public void removeCard(Card card) {
        Helpers.print_debug(cname, String.format("Removing card %s.", card.getName()));
		cards.remove(card);
		Helpers.board.update();
		update();
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param wipLimit the wipLimit to set
	 */
	public void setWipLimit(int wipLimit) {
		this.wipLimit = wipLimit;
	}

	/**
	 * @return the wipLimit
	 */
	public Integer getWipLimit() {
		return wipLimit;
	}
	
	/**
	 * @return Stringified WIP limit
	 */
	public String getWipString() {
		int numCards = 0;
		String wip = wipLimit.toString();
		if(!cards.isEmpty())
			numCards = cards.size();
		if(wipLimit.equals(0))
			wip = "\u221e"; // Unicode "infinity" symbol
		return String.format("%d/%s", numCards, wip);
	}

	/**
	 * @param position the position to set
	 */
	public void setPosition(int position) {
		this.position = position;
	}

	/**
	 * @return the position
	 */
	public Integer getPosition() {
		return position;
	}
	
	/**
	 * Update view attributes and repaint the view.
	 */
	public void update() {
		Helpers.print_debug(cname, "Update called.");
		view.nameLabel.setText(getName());
		view.wipLabel.setText(getWipString());
		view.removeAll();
		view.headerPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
		view.add(view.headerPanel, Component.CENTER_ALIGNMENT);
		view.add(Box.createRigidArea(new Dimension(5,10)));
		view.setBorder(BorderFactory.createLineBorder(Color.black));
		List<Card> cardList = getCards();
		if(!cardList.isEmpty()) {
			for(Card card : getCards()) {
				card.update();
				view.add(card.getView(), Component.CENTER_ALIGNMENT);
			}
		}
		view.headerPanel.revalidate();
		view.headerPanel.repaint();
		view.revalidate();
		view.repaint();
	}

	/**
	 * Customer MouseAdapter to deselect card on column click.
	 */
	public class SelectColListener extends MouseAdapter {
		private Column col = null;
		public SelectColListener(Column col) {
			this.col = col;
		}
			public void mouseClicked(MouseEvent e) {
				Helpers.print_debug(cname, "Mouse click detected");
				Helpers.board.unselectCard();
				if(e.isPopupTrigger() || SwingUtilities.isRightMouseButton(e))
					showMenu(e);
			}
			
			public void mouseReleased(MouseEvent e) {
				if(e.isPopupTrigger() || SwingUtilities.isRightMouseButton(e))
					showMenu(e);
			}
			
			private void showMenu(MouseEvent e) {
				ColContextMenu menu = new ColContextMenu(col);
				menu.show(e.getComponent(), e.getX(), e.getY());
			}
	}
	
	public void setMouseListeners() {
		MouseAdapter selectListen = new SelectColListener(this);
		view.addMouseListener(selectListen);
	}
	
	/**
	 * Update then return the JPanel representation of this view.
	 * @return columnView view
	 */
	public ColumnView getView() {
		update();
		return view;
	}
}