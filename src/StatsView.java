/*
 * John Brodie
 * jdb356@drexel.edu
 * CS338:GUI, Assignment 3
 */
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.Serializable;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.jfree.chart.ChartPanel;


public class StatsView extends JFrame implements Serializable {
	
	private static final long serialVersionUID = -9197648191136657682L;
	public StatsController controller;
	
	JPanel centerPanel = new JPanel();
	
	/* Title Panel */
	JPanel titlePanel = new JPanel();
	JButton refreshButton = new JButton("Refresh");
	private String[] chartOptions = {"Card Types", "Card Priorities"};
	JComboBox selectBox = new JComboBox(chartOptions);
	/* Type Panel */
	ChartPanel typeChartPanel;
	/* Priority Panel */
	ChartPanel prioChartPanel;
	
	
	public StatsView(StatsController controller) {
		this.controller = controller;
		this.setLayout(new BorderLayout());
		
		titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.LINE_AXIS));
		this.setTitle("Visual KanBan Stats Version 1.0");
		/* public domain icon from http://bit.ly/NRxwCf */
		this.setIconImage(Toolkit.getDefaultToolkit()
						  .getImage("k.png"));
		
	}
	
	public void CreateGUI() {
		centerPanel.setPreferredSize(new Dimension(800, 450));
		/* Title Panel */
		titlePanel.add(refreshButton);
		titlePanel.add(Box.createHorizontalGlue());
		titlePanel.add(new JLabel("Chart Type: "));
		titlePanel.add(selectBox);
		this.add(titlePanel, BorderLayout.NORTH);
		
		/* Type Panel */
		typeChartPanel = new ChartPanel(controller.createTypeChart());
		centerPanel.add(typeChartPanel);
		this.add(centerPanel, BorderLayout.CENTER);
		
		/* Priority Panel */
		prioChartPanel = new ChartPanel(controller.createPrioChart());
		
		pack();
	}
	
	protected void update() {
		
	}
	
}
