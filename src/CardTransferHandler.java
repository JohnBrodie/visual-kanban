/*
 * John Brodie
 * jdb356@drexel.edu
 * CS338:GUI, Assignment 3
 */
import java.awt.Point;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

import javax.swing.JComponent;
import javax.swing.TransferHandler;

class CardTransferHandler extends TransferHandler{

	String cname = getClass().getName();
	private static final long serialVersionUID = 6888034833673715132L;
	Column column;
	
	public CardTransferHandler(Column column) {
		this.column = column;
	}

	/**
	 * Test for a valid JPanel data flavor.
	 */
	public boolean canImport(JComponent c, DataFlavor[] f){
		Helpers.print_debug(cname, "Check if data is importable.");
		DataFlavor temp = new DataFlavor(CardView.class, "JPanel");
		for(DataFlavor d : f){
			if(d.equals(temp))
				return true;
		}
		return false;
	}

	/**
	 * Import the card data into the new column.
	 */
	public boolean importData(JComponent comp, Transferable t, Point p){
		Helpers.print_debug(cname, "Importing data.");
		try {
			CardView tempView = (CardView)t.getTransferData(
					new DataFlavor(CardView.class, "JPanel"));
			column.addCard(tempView.card);

		} catch (UnsupportedFlavorException ex) {
			System.out.println(ex.getStackTrace());
		} catch (IOException ex) {
			System.out.println(ex.getStackTrace());
		}
		return true;
	}
}