/*
 * John Brodie
 * jdb356@drexel.edu
 * CS338:GUI, Assignment 3
 */
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot3D;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.util.Rotation;


public class StatsController implements Serializable {
	
	private static final long serialVersionUID = -6336013147685600789L;
	private String cname = getClass().getName();
	public StatsView view;
	public StatsModel model;
	public Board board;
	public DefaultPieDataset typeDataset;
	public DefaultPieDataset prioDataset;
	
	public StatsController(Board board) {
		this.board = board;
		view = new StatsView(this);
		model = new StatsModel(this);
		typeDataset = new DefaultPieDataset();
		prioDataset = new DefaultPieDataset();
		setListeners();
	}
	
	public StatsView getView() {
		return view;
	}
	
	private void updateView() {
		model.refresh();
		view.update();
	}
	
	public void setChart(ChartPanel chart) {
		view.centerPanel.removeAll();
		view.centerPanel.add(chart);
		view.centerPanel.revalidate();
		view.centerPanel.repaint();
		
	}
	
	public void setListeners() {
		view.selectBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String type = (String) view.selectBox.getSelectedItem();
				Helpers.print_debug(cname, String.format("Switched to: %s", type));
				type = type.toLowerCase();
				if(type.equals("card types")) {
					updateTypeChart();
					setChart(view.typeChartPanel);
				} else if(type.equals("card priorities")) {
					updatePrioChart();
					setChart(view.prioChartPanel);
				}
				}});
		
		view.refreshButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String type = (String) view.selectBox.getSelectedItem();
				Helpers.print_debug(cname, String.format("Refresh requested on: %s", type));
				type = type.toLowerCase();
				if(type.equals("card types")) {
					updateTypeChart();
				} else if(type.equals("card priorities")) {
					updatePrioChart();
				}
				}});
	}
	
	public void updateTypeDataset() {
		Helpers.print_debug(cname, "Updating Type Dataset");
		typeDataset.setValue("In Queue", model.getCardsInQueue());
		typeDataset.setValue("In Progress", model.getCardsInProgress());
		typeDataset.setValue("Done", model.getCardsDone());
	}
	
	public void updatePrioDataset() {
		Helpers.print_debug(cname, "Updating Prio Dataset");
		prioDataset.setValue("Red", model.redCards);
		prioDataset.setValue("Blue", model.blueCards);
		prioDataset.setValue("Green", model.greenCards);
		prioDataset.setValue("Yellow", model.yellowCards);
	}
	
	public void updateTypeChart() {
		JFreeChart c = createTypeChart();
		view.typeChartPanel.removeAll();
		view.typeChartPanel.add(new ChartPanel(c));
		
	}
	
	public void updatePrioChart() {
		JFreeChart c = createPrioChart();
		view.prioChartPanel.removeAll();
		view.prioChartPanel.add(new ChartPanel(c));
	}
	
	public JFreeChart createPrioChart() {
		model.refresh();
		String title = "Cards per Priority";
		updatePrioDataset();
		JFreeChart chart = ChartFactory.createPieChart3D(title,
				prioDataset, true, true, false);
		PiePlot3D plot = (PiePlot3D) chart.getPlot();
		plot.setStartAngle(290);
		plot.setDirection(Rotation.CLOCKWISE);
		plot.setForegroundAlpha(0.5f);
		
		return chart;
	}
	
	public JFreeChart createTypeChart() {
		model.refresh();
		String title = "Cards per Category";
		updateTypeDataset();
		JFreeChart chart = ChartFactory.createPieChart3D(title,
				typeDataset, true, true, false);
		PiePlot3D plot = (PiePlot3D) chart.getPlot();
		plot.setStartAngle(290);
		plot.setDirection(Rotation.CLOCKWISE);
		plot.setForegroundAlpha(0.5f);
		
		return chart;
	}
	
	
}