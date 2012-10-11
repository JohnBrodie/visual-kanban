/*
 * John Brodie
 * jdb356@drexel.edu
 * CS338:GUI, Assignment 3
 */
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.io.IOException;
import java.io.Serializable;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class ColumnView extends JPanel implements DropTargetListener,
												  Serializable {
	
	private static final long serialVersionUID = 105130335730011237L;
	Column column;
	JLabel nameLabel = new JLabel("Default Name");
	JLabel wipLabel = new JLabel("0/inf.");
	JPanel headerPanel = new JPanel();
	Color bgColor = Color.white;
	private String cname = getClass().getName();
	final int width = 250;
	final int height = 800;

	/**
	 * Given a card, initialize a view object.
	 * @param card - The model used by this view.
	 */
	public ColumnView(Column column) {
		this.column = column;
		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		this.setBackground(bgColor);
		this.setAlignmentX(Box.CENTER_ALIGNMENT);
		wipLabel.setAlignmentX(CENTER_ALIGNMENT);
		nameLabel.setAlignmentX(CENTER_ALIGNMENT);
		headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.PAGE_AXIS));
		headerPanel.add(nameLabel);
		headerPanel.add(wipLabel);
		headerPanel.setBackground(bgColor);
		this.add(headerPanel);
		nameLabel.setFont(nameLabel.getFont().deriveFont(20.0f));
		setTransferHandlers();
	}
	
	protected void setTransferHandlers() {
		new DropTarget(this, this);
		setTransferHandler(new CardTransferHandler(column));
	}
	
	public Dimension getPreferredSize() {
		Helpers.board.resizeColumns();
		return new Dimension(Helpers.colWidth, Helpers.colHeight);
	}
	
    /* DropTargetListener methods */
	@Override
	public void drop(DropTargetDropEvent dtde) {
		Helpers.print_debug(cname, String.format("Drop detected on column %s.", column.getName()));
		try {
			Point loc = dtde.getLocation();
			Transferable t = dtde.getTransferable();
			DataFlavor[] d = t.getTransferDataFlavors();
			t.getTransferData(d[0]);
			if(getTransferHandler().canImport(this, d)) {
				((CardTransferHandler)getTransferHandler()).importData(this,
						(CardView)t.getTransferData(d[0]), loc);
			}
			else return;
		} catch (UnsupportedFlavorException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			dtde.dropComplete(true);
			repaint();
		}
	}
	
	@Override
	public void dropActionChanged(DropTargetDragEvent arg0) {}

	@Override
	public void dragEnter(DropTargetDragEvent arg0) {}

	@Override
	public void dragExit(DropTargetEvent arg0) {}

	@Override
	public void dragOver(DropTargetDragEvent arg0) {}
}