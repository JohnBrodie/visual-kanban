/*
 * John Brodie
 * jdb356@drexel.edu
 * CS338:GUI, Assignment 3
 */
import java.awt.GridBagConstraints;
import java.awt.Image;
import java.awt.Insets;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.Comparator;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;


public class Helpers implements Serializable {
	
	private static final long serialVersionUID = 8075614896101399038L;
	public static final boolean DEBUG = true;
	public static long nextID = 0;
	public static int colWidth = 250;
	public static int colHeight = 800;
	public static Board board = null;
	private static Image green = null;
	private static Image red = null;
	private static Image blue = null;
	private static Image yellow = null;
	public static VisualKanban v = null;
	
	public static void loadImages() {
		try {
		    green = ImageIO.read(Helpers.class.getResource("green.png"));
		    red = ImageIO.read(Helpers.class.getResource("red.png"));
		    blue = ImageIO.read(Helpers.class.getResource("blue.png"));
		    yellow = ImageIO.read(Helpers.class.getResource("yellow.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static Image getImageByColor(String color) {
		print_debug("Helper", String.format("Got color %s", color.toString()));
		if(green == null)
			loadImages();
		color = color.toLowerCase();
		if(color.equals("yellow"))
			return yellow;
		else if(color.equals("red"))
			return red;
		else if(color.equals("blue"))
			return blue;
		else 
			return green;
	}
	
	public static String addBreaks(String old) {
		char[] cs = old.toCharArray();
		int step = 16;
		int count = (int) (cs.length / step);
		int limit = count * step + 1;
		for(int i = step -1; i < limit; i+=step) {
			while(i < cs.length) {
				if(Character.isWhitespace(cs[i])) {
					cs[i] = '~';
					break;
				}
				i++;
			}
		}
		String ret = new String(cs);
		ret = ret.replaceAll("~", "<br>");
		return ret;
	}
	
	/**
	 * Customer comparator for column objects.
	 */
    public static Comparator<Column> ColumnComparator = new Comparator<Column>()
    {
        public int compare(Column col1, Column col2)
        {
            return col1.getPosition() - col2.getPosition();
        }
    };
	
	public static long getNextID() {
		nextID++;
		return nextID;
	}
	
	/**
	 * @return Default GridBagConstraints object for this program.
	 */
	public GridBagConstraints getDefaultConstraints() {
		Insets defaultInsets = new Insets(3, 3, 3, 3);
		GridBagConstraints c = new GridBagConstraints();
		c.anchor = GridBagConstraints.NORTHWEST;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridheight = 10;
		c.weightx = 1;
		c.weighty = 1;
    	c.gridx = GridBagConstraints.RELATIVE;
		c.gridy = 0;
		c.insets = defaultInsets;
		return c;
	}
	
	public static void print_debug(String cname, String msg) {
		if(!DEBUG)
			return;
		System.out.println(cname + ": " + msg);
	}

}
