/*
 * John Brodie
 * jdb356@drexel.edu
 * CS338:GUI, Assignment 3
 */
import javax.swing.JFrame;


public class VisualKanban extends JFrame {
	private static final long serialVersionUID = -7840323711220038015L;
	
	public VisualKanban() {
	}
	public static void main(String[] args) {
		Helpers.board = new Board();
	}
}