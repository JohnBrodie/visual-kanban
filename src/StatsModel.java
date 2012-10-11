/*
 * John Brodie
 * jdb356@drexel.edu
 * CS338:GUI, Assignment 3
 */
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class StatsModel implements Serializable {
	
	private static final long serialVersionUID = 8816168285473383386L;
	public StatsController controller;
	private String cname = getClass().getName();
	
	private int totalCards = 0;
	private List<String> columnNames = new ArrayList<String>();
	private int cardsInQueue = 0;
	private int cardsInProgress= 0;
	private int cardsDone = 0;
	private int totalColumns = 0;
	public int redCards = 0;
	public int blueCards = 0;
	public int yellowCards = 0;
	public int greenCards = 0;
	
	
	public StatsModel(StatsController controller) {
		this.controller = controller;
	}
	
	private void reset() {
		Helpers.print_debug(cname, "Resetting fields");
		totalCards = 0;
		columnNames.clear();
		cardsInQueue = 0;
		cardsInProgress = 0;
		cardsDone = 0;
		totalColumns = 0;
		redCards = 0;
		blueCards = 0;
		yellowCards = 0;
		greenCards = 0;
	}
	
	protected void refresh() {
		reset();
		Helpers.print_debug(cname, "Refreshing fields");
		ArrayList<Column> columns = (ArrayList<Column>) controller.board.getModel().getColumns();
		columns.trimToSize();
		totalColumns = columns.size();
		boolean firstFlag = true;
		for(Column col : columns) {
			ArrayList<Card> cards = (ArrayList<Card>) col.getCards();
			cards.trimToSize();
			for(Card card : cards) {
				String prio = card.getColor().toLowerCase();
				if(prio.equals("green"))
					greenCards++;
				else if(prio.equals("yellow"))
					yellowCards++;
				else if(prio.equals("blue"))
					blueCards++;
				else
					redCards++;
			}
			cardsDone = cards.size();
			totalCards += cardsDone;
			if(firstFlag) {
				cardsInQueue = cards.size();
				firstFlag = false;
			}
			columnNames.add(col.getName());
		}
		cardsInProgress = totalCards - (cardsDone + cardsInQueue);
		Helpers.print_debug(cname, "Refresh complete");
	}
	
	/**
	 * @param totalCards the totalCards to set
	 */
	public void setTotalCards(int totalCards) {
		this.totalCards = totalCards;
	}
	/**
	 * @return the totalCards
	 */
	public int getTotalCards() {
		return totalCards;
	}
	
	/**
	 * @param cardsInQueue the cardsInQueue to set
	 */
	public void setCardsInQueue(int cardsInQueue) {
		this.cardsInQueue = cardsInQueue;
	}
	/**
	 * @return the cardsInQueue
	 */
	public int getCardsInQueue() {
		return cardsInQueue;
	}
	/**
	 * @param cardsInProgress the cardsInProgress to set
	 */
	public void setCardsInProgress(int cardsInProgress) {
		this.cardsInProgress = cardsInProgress;
	}
	/**
	 * @return the cardsInProgress
	 */
	public int getCardsInProgress() {
		return cardsInProgress;
	}
	/**
	 * @param cardsDone the cardsDone to set
	 */
	public void setCardsDone(int cardsDone) {
		this.cardsDone = cardsDone;
	}
	/**
	 * @return the cardsDone
	 */
	public int getCardsDone() {
		return cardsDone;
	}

	/**
	 * @param totalColumns the totalColumns to set
	 */
	public void setTotalColumns(int totalColumns) {
		this.totalColumns = totalColumns;
	}

	/**
	 * @return the totalColumns
	 */
	public int getTotalColumns() {
		return totalColumns;
	}

}
