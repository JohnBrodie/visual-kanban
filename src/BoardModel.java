/*
 * John Brodie
 * jdb356@drexel.edu
 * CS338:GUI, Assignment 3
 */
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BoardModel implements Serializable {
	
	private static final long serialVersionUID = -8725397024421627704L;
	private List<Column> columns = new ArrayList<Column>();
	private String name;
	private String cname = getClass().getName();
	private Card selectedCard = null;

	/**
	 * Constructor for board model
	 * @param name - name of board
	 */
	public BoardModel(String name) {
		this.name = name;
	}
	
	/**
	 * @return - List of columns sorted by position.
	 */
	public List<Column> getColumns() {
		sortColumns();
		return columns;
	}
	
	public Column getFirstColumn() {
		sortColumns();
		return columns.get(0);
	}
	
	/**
	 * Sort column list.
	 */
	public void sortColumns() {
		Collections.sort(columns, Helpers.ColumnComparator);
	}
	
	/**
	 * Add a column
	 * @param column - column to add
	 */
	public void addColumn(Column column) {
		Helpers.print_debug(cname, String.format("Adding column %s.", column.getName()));
		columns.add(column);
	}
	
	/**
	 * Remove a column
	 * @param column - column to remove
	 */
	public void removeColumn(Column column) {
		columns.remove(column);
	}
	
	/**
	 * Get column by position
	 * @param position - Position of column to return
	 * @return - First column with given position, null if none exist
	 */
	public Column getColumnByPosition(Integer position) {
		Column column = null;
		for(Column col : columns) {
			if(col.getPosition().equals(position)) {
				column = col;
			}
		}
		return column;
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
	 * @param selectedCard the selectedCard to set
	 */
	public void setSelectedCard(Card selectedCard) {
		this.selectedCard = selectedCard;
	}

	/**
	 * @return the selectedCard
	 */
	public Card getSelectedCard() {
		return selectedCard;
	}
}