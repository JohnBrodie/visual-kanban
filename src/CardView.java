/*
 * John Brodie
 * jdb356@drexel.edu
 * CS338:GUI, Assignment 3
 */
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragSource;
import java.awt.dnd.DragSourceDragEvent;
import java.awt.dnd.DragSourceDropEvent;
import java.awt.dnd.DragSourceEvent;
import java.awt.dnd.DragSourceListener;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.Serializable;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.TransferHandler;

public class CardView extends JPanel implements Transferable, Serializable,
						DragSourceListener, DragGestureListener, DropTargetListener {
	
	private static final long serialVersionUID = 1154690854045670870L;
	Card card;
	JLabel nameLabel = new JLabel();
	JLabel descriptionLabel = new JLabel();
	final int width = 200;
	final int height = 180;
	final int panelX = 200;
	private String cname = getClass().getName();
	private DragSource source;
	private TransferHandler t;

	/**
	 * Given a card, initialize a view object.
	 * @param card - The model used by this view.
	 */
	public CardView(final Card card) {
		this.card = card;
		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		this.setPreferredSize(new Dimension(width, height));
		this.setMinimumSize(new Dimension(width, height));
		this.setMaximumSize(new Dimension(width, height));
		this.setAlignmentX(CENTER_ALIGNMENT);
		nameLabel.setAlignmentX(CENTER_ALIGNMENT);
		descriptionLabel.setAlignmentX(CENTER_ALIGNMENT);
		descriptionLabel.setHorizontalAlignment(JLabel.CENTER);
		this.add(Box.createRigidArea(new Dimension(5, 50)));
		this.add(nameLabel);
		this.add(descriptionLabel);
		this.add(Box.createVerticalGlue());
		nameLabel.setFont(nameLabel.getFont().deriveFont(16.0f));
		
		this.setOpaque(false);
		setTransferHandlers();
	}
	
	protected void setTransferHandlers() {
		/* Set up Drop handler */
		new DropTarget(this, this);
		setTransferHandler(new CardTransferHandler(card.getColumn()));
		
		/* Set up transfer handler */
		t = new TransferHandler() {
			private static final long serialVersionUID = -6935134217762525672L;

			public Transferable createTransferable(JComponent c) {
				return card.getView();
			}
		};
		this.setTransferHandler(t);
		source = new DragSource();
		source.createDefaultDragGestureRecognizer(this, DnDConstants.ACTION_MOVE,
											this);
		
	}
	
	public void paint(Graphics g) {
		Helpers.print_debug(cname, String.format("paint called on %s", card.getName()));
		Image bg = Helpers.getImageByColor(card.getColor());
		g.drawImage(bg, 0, 0, null);
		super.paint(g);
	}
	
	/**
	 * @return DataFlavor - A JPanel data flavor.
	 */
	public DataFlavor[] getTransferDataFlavors() {
		return new DataFlavor[] {
				new DataFlavor(CardView.class, "JPanel")};
		}

	/* Drag methods */
	
	/**
	 * Called when a drag event is detected.
	 */
	@Override
	public void dragGestureRecognized(DragGestureEvent dge) {
        Helpers.print_debug(cname, "Drag started.");
        Toolkit toolkit = Toolkit.getDefaultToolkit(); // TODO
        toolkit.getBestCursorSize(200, 180);
        Image image = createImage(this);
        Cursor c = toolkit.createCustomCursor(image , new Point(0,0), "img");
        Helpers.board.view.setCursor(c);
		source.startDrag(dge, c,
						 this, this);
		card.getView().setVisible(false);
	}
	public BufferedImage createImage(JPanel panel) {

	    int w = panel.getWidth();
	    int h = panel.getHeight();
	    BufferedImage bi = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
	    Graphics2D g = bi.createGraphics();
	    panel.paint(g);
	    return bi;
	}


	@Override
	public void dragDropEnd(DragSourceDropEvent dsc) {
		boolean status = dsc.getDropSuccess();
        Helpers.board.view.setCursor(Cursor.DEFAULT_CURSOR);
        Helpers.print_debug(cname, String.format("DragDrop ended with status: %s", status));
        if(status)
        	card.getColumn().removeCard(card);
        card.getColumn().update();
	}

	@Override
	public void dragEnter(DragSourceDragEvent arg0) {}

	@Override
	public void dragExit(DragSourceEvent arg0) {}

	@Override
	public void dragOver(DragSourceDragEvent arg0) {}

	@Override
	public void dropActionChanged(DragSourceDragEvent arg0) {}

	@Override
	public Object getTransferData(DataFlavor arg0)
			throws UnsupportedFlavorException, IOException {
		return this;
	}

	@Override
	public boolean isDataFlavorSupported(DataFlavor arg0) {
		return true;
	}
	
	/* Drop Methods */
	@Override
	public void drop(DropTargetDropEvent dtde) {
		Helpers.print_debug(cname, String.format("Drop detected on card at column %s.", card.getColumn().getName()));
		card.getColumn().view.drop(dtde);
	}

	@Override
	public void dragEnter(DropTargetDragEvent arg0) {}

	@Override
	public void dragExit(DropTargetEvent arg0) {}

	@Override
	public void dragOver(DropTargetDragEvent arg0) {}

	@Override
	public void dropActionChanged(DropTargetDragEvent arg0) {}
}
