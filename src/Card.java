/*
 * John Brodie
 * jdb356@drexel.edu
 * CS338:GUI, Assignment 3
 */
import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.Serializable;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.SwingUtilities;


public class Card implements Serializable {
	
	private static final long serialVersionUID = -2136603309450729910L;
	CardView view;
	private String name = "Default Name";
	private String description = "Default Description";
	private String color = "green";
	private long id;
	private String creator = "Default Creator";
	private String assigned;
	private Date createdDate;
	private Column column;
	private String cname = getClass().getName();
	
	public Card(String name, String description, String color, String creator, String assigned) {
		Helpers.print_debug(cname, "Creating new card with details.");
		setId(Helpers.getNextID());
		view = new CardView(this);
		this.name = name;
		this.description = description;
		this.color = color;
		this.creator = creator;
		this.assigned = assigned;
		this.createdDate = new Date();
		view.setToolTipText("Created: " + this.createdDate.toString());
		setListeners();
	}
	
	@Deprecated
	public Card() {
		Helpers.print_debug(cname, "Creating new card.");
		setId(Helpers.getNextID());
		view = new CardView(this);
		setListeners();
	}
	
	public CardView getView() {
		update();
		return view;
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
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * @param color the color to set
	 */
	public void setColor(String color) {
		this.color = color;
	}
	/**
	 * @return the color
	 */
	public String getColor() {
		return color;
	}
	/**
	 * @param creator the creator to set
	 */
	public void setCreator(String creator) {
		this.creator = creator;
	}
	/**
	 * @return the creator
	 */
	public String getCreator() {
		return creator;
	}
	/**
	 * @param assigned the assigned to set
	 */
	public void setAssigned(String assigned) {
		this.assigned = assigned;
	}
	/**
	 * @return the assigned
	 */
	public String getAssigned() {
		return assigned;
	}

	/**
	 * @param column the column to set
	 */
	public void setColumn(Column column) {
		this.column = column;
	}

	/**
	 * @return the column
	 */
	public Column getColumn() {
		return column;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}
	
	/**
	 * Customer MouseAdapter 
	 */
	public class SelectCardListener extends MouseAdapter {
		private Card card = null;
		public SelectCardListener(Card card) {
			this.card = card;
		}
			public void mouseClicked(MouseEvent e) {
				Helpers.print_debug(cname, "Mouse click detected");
				Helpers.board.unselectCard();
				Helpers.board.setSelectedCard(this.card);
				card.getView().setBorder(BorderFactory.createLineBorder(Color.black));
				if(e.isPopupTrigger() || SwingUtilities.isRightMouseButton(e))
					showMenu(e);
			}
			public void mouseReleased(MouseEvent e) {
				if(e.isPopupTrigger() || SwingUtilities.isRightMouseButton(e))
					showMenu(e);
			}
			
			private void showMenu(MouseEvent e) {
				CardContextMenu menu = new CardContextMenu(card);
				menu.show(e.getComponent(), e.getX(), e.getY());
			}
	}
	
	protected void setMouseListeners() {
		MouseAdapter selectListen = new SelectCardListener(this);
		view.addMouseListener(selectListen);
	}
	
	protected void setListeners() {
		setMouseListeners();
	}
	
	/**
	 * Update view attributes and repaint the view.
	 */
	public void update() {
		Helpers.print_debug(cname, String.format("Update called on card %s", getName()));
		String desc = String.format("<html><align=center>%s</align></html>",
				Helpers.addBreaks(getDescription()));
		view.nameLabel.setText(getName());
		view.descriptionLabel.setText(desc);
		view.setVisible(true);
		if(this.view.getMouseListeners().length < 4)
			setMouseListeners();
		view.revalidate();
		view.repaint();
	}

	/**
	 * @param createdDate the createdDate to set
	 */
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	/**
	 * @return the createdDate
	 */
	public Date getCreatedDate() {
		return createdDate;
	}
}
