package gov.nist.itl.ssd.similarity.test.reporting;

import javax.swing.JFrame;
import org.jfree.data.xy.*;
import java.io.*;
import java.util.Random;
import org.jfree.chart.*;
import org.jfree.data.statistics.*;
import org.jfree.chart.plot.PlotOrientation;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.IntervalXYDataset;
import org.jfree.data.xy.XYDataItem;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

import fj.data.List;
import fj.data.Array;

public class Graph 
{
	
	public static JComponent createHistogram( Array<Array<Double>> data, String title, String xLabel, String yLabel ) 
	{	
		Array<Double> input = data.get(0);
		Array<Double> output= data.get(1);
		Array<XYDataItem> items = Array.empty();
		XYSeries series = new XYSeries("Data");
		XYSeriesCollection dataset = null;
		XYDataItem item = null;
		int len = input.length();
		double[] values = new double[output.length()];
		for (int i=0; i < len; i++) {
			values[i] = output.get(i).doubleValue();
		}
		
		HistogramDataset hdata = new HistogramDataset();
		hdata.setType(HistogramType.RELATIVE_FREQUENCY);
		hdata.addSeries("Histogram", values, 1025);
		
		final JFreeChart chart = ChartFactory.createHistogram(
	            title,
	            xLabel,
	            yLabel,
	            hdata,
	            PlotOrientation.VERTICAL,
	            true,
	            true,
	            false
	        );

	        ChartPanel chartPanel = new ChartPanel(chart);
	        chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
	        JFrame frame = new JFrame("FrameTitle");
	        frame.setContentPane(chartPanel);
	        frame.pack();
	        //frame.setVisible(true);
	        return (JComponent)frame.getContentPane();
	}	
	
	public static JComponent showHistogram( Array<Array<Double>> data, String title, String xLabel, String yLabel ) 
	{	
		Array<Double> input = data.get(0);
		Array<Double> output= data.get(1);
		Array<XYDataItem> items = Array.empty();
		XYSeries series = new XYSeries("Data");
		XYSeriesCollection dataset = null;
		XYDataItem item = null;
		int len = input.length();
		double[] values = new double[output.length()];
		for (int i=0; i < len; i++) {
			values[i] = output.get(i).doubleValue();
		}
		
		HistogramDataset hdata = new HistogramDataset();
		//hdata.setType(HistogramType.RELATIVE_FREQUENCY);
		hdata.setType(HistogramType.FREQUENCY);
		hdata.addSeries("Histogram", values, 255);
		
		final JFreeChart chart = ChartFactory.createHistogram(
	            title,
	            xLabel,
	            yLabel,
	            hdata,
	            PlotOrientation.VERTICAL,
	            true,
	            true,
	            false
	        );

	        ChartPanel chartPanel = new ChartPanel(chart);
	        chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
	        JFrame frame = new JFrame("FrameTitle");
	        frame.setContentPane(chartPanel);
	        frame.pack();
	        frame.setVisible(true);
	        return (JComponent)frame.getContentPane();
	}	
	
	
	public static void saveAsPNG( String fileName, JFreeChart chart, int width, int height ) {
        try {
        ChartUtilities.saveChartAsPNG(
        		new File(fileName), 
        		chart, 
        		width, 
        		height
        		);
        } catch (IOException e) { }	
	}
	
	
	/*
	 *  Expect input as a list of lists, 
	 *    typically: list(input), list(output).
	 *  Create an x-y graph from that.
	 */
	public static JComponent createGraph( List<List<Double>> data, String title, String xLabel, String yLabel ) 
	{	
		List<Double> input = data.index(0);
		List<Double> output= data.index(1);
		List<XYDataItem> items = List.list();
		XYSeries series = new XYSeries("Data");
		XYSeriesCollection dataset = null;
		XYDataItem item = null;
		int len = input.length();
		for (int i=0; i < len; i++) {
			item = new XYDataItem( input.index(i), output.index(i) );
			series.add(item);
		}
		dataset = new XYSeriesCollection(series);
		final JFreeChart chart = ChartFactory.createXYLineChart(
	            title,
	            xLabel,
	            yLabel,
	            dataset,
	            PlotOrientation.VERTICAL,
	            true,
	            true,
	            false
	        );

	        ChartPanel chartPanel = new ChartPanel(chart);
	        chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
	        JFrame frame = new JFrame("FrameTitle");
	        frame.setContentPane(chartPanel);
	        frame.pack();
	        //frame.setVisible(true);
	        return (JComponent)frame.getContentPane();
	}

	public static JComponent createGraph( Array<Array<Double>> data, String title, String xLabel, String yLabel ) 
	{	
		Array<Double> input = data.get(0);
		Array<Double> output= data.get(1);
		Array<XYDataItem> items = Array.empty();
		XYSeries series = new XYSeries("Data");
		XYSeriesCollection dataset = null;
		XYDataItem item = null;
		int len = input.length();
		for (int i=0; i < len; i++) {
			item = new XYDataItem( input.get(i), output.get(i) );
			series.add(item);
		}
		dataset = new XYSeriesCollection(series);
		final JFreeChart chart = ChartFactory.createXYLineChart(
	            title,
	            xLabel,
	            yLabel,
	            dataset,
	            PlotOrientation.VERTICAL,
	            true,
	            true,
	            false
	        );

	        ChartPanel chartPanel = new ChartPanel(chart);
	        chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
	        JFrame frame = new JFrame("FrameTitle");
	        frame.setContentPane(chartPanel);
	        frame.pack();
	        //frame.setVisible(true);
	        return (JComponent)frame.getContentPane();
	}	
	
	/*
	 * Show graph.
	 */
	
	public static JComponent showGraph( List<List<Double>> data, String title, String xLabel, String yLabel ) 
	{	
		List<Double> input = data.index(0);
		List<Double> output= data.index(1);
		List<XYDataItem> items = List.list();
		XYSeries series = new XYSeries("Data");
		XYSeriesCollection dataset = null;
		XYDataItem item = null;
		int len = input.length();
		for (int i=0; i < len; i++) {
			item = new XYDataItem( input.index(i), output.index(i) );
			series.add(item);
		}
		dataset = new XYSeriesCollection(series);
		final JFreeChart chart = ChartFactory.createXYLineChart(
	            title,
	            xLabel,
	            yLabel,
	            dataset,
	            PlotOrientation.VERTICAL,
	            true,
	            true,
	            false
	        );

	        ChartPanel chartPanel = new ChartPanel(chart);
	        chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
	        JFrame frame = new JFrame("FrameTitle");
	        frame.setContentPane(chartPanel);
	        frame.pack();
	        frame.setVisible(true);
	        return (JComponent)frame.getContentPane();
	}	
	
	public static JComponent showGraph( Array<Array<Double>> data, String title, String xLabel, String yLabel ) 
	{	
		Array<Double> input = data.get(0);
		Array<Double> output= data.get(1);
		Array<XYDataItem> items = Array.empty();
		XYSeries series = new XYSeries("Data");
		XYSeriesCollection dataset = null;
		XYDataItem item = null;
		int len = input.length();
		for (int i=0; i < len; i++) {
			item = new XYDataItem( input.get(i), output.get(i) );
			series.add(item);
		}
		dataset = new XYSeriesCollection(series);
		final JFreeChart chart = ChartFactory.createXYLineChart(
	            title,
	            xLabel,
	            yLabel,
	            dataset,
	            PlotOrientation.VERTICAL,
	            true,
	            true,
	            false
	        );

	        ChartPanel chartPanel = new ChartPanel(chart);
	        chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
	        JFrame frame = new JFrame("FrameTitle");
	        frame.setContentPane(chartPanel);
	        frame.pack();
	        //frame.setVisible(true);
	        return (JComponent)frame.getContentPane();
	}		
	
	public static void showGraph( List<List<Double>> data ) 
	{	
		List<Double> input = data.index(0);
		List<Double> output= data.index(1);
		List<XYDataItem> items = List.list();
		XYSeries series = new XYSeries("Data");
		XYSeriesCollection dataset = null;
		XYDataItem item = null;
		int len = input.length();
		for (int i=0; i < len; i++) {
			item = new XYDataItem( input.index(i), output.index(i) );
			series.add(item);
		}
		dataset = new XYSeriesCollection(series);
		final JFreeChart chart = ChartFactory.createXYLineChart(
	            "XY Series Demo",
	            "X", 
	            "Y", 
	            dataset,
	            PlotOrientation.VERTICAL,
	            true,
	            true,
	            false
	        );

	        final ChartPanel chartPanel = new ChartPanel(chart);
	        chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
	        JFrame frame = new JFrame("FrameTitle");
	        frame.setContentPane(chartPanel);
	        frame.pack();
	        frame.setVisible(true);
	}
	
	public static void showGraph( Array<Array<Double>> data ) 
	{	
		Array<Double> input = data.get(0);
		Array<Double> output= data.get(1);
		Array<XYDataItem> items = Array.empty();
		XYSeries series = new XYSeries("Data");
		XYSeriesCollection dataset = null;
		XYDataItem item = null;
		int len = input.length();
		for (int i=0; i < len; i++) {
			item = new XYDataItem( input.get(i), output.get(i) );
			series.add(item);
		}
		dataset = new XYSeriesCollection(series);
		final JFreeChart chart = ChartFactory.createXYLineChart(
	            "XY Series Demo",
	            "X", 
	            "Y", 
	            dataset,
	            PlotOrientation.VERTICAL,
	            true,
	            true,
	            false
	        );

	        final ChartPanel chartPanel = new ChartPanel(chart);
	        chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
	        JFrame frame = new JFrame("FrameTitle");
	        frame.setContentPane(chartPanel);
	        frame.pack();
	        frame.setVisible(true);
	}	

	/*
	 * Save the graph's image out to file.
	 */
	public static void saveGraphImage(JFreeChart c, String fileName )
	{		    
		try {
			File f = new File( fileName );
            ChartUtilities.saveChartAsPNG(f, c, 500, 270);
		}catch( Exception e ){
			System.out.println("ERROR: " + e.getMessage());
		}
	}	

	public static void saveGraphImage(JComponent c, String dir, String fileName )
	{		
		BufferedImage image = new BufferedImage(c.getWidth(), c.getHeight(), BufferedImage.TYPE_INT_ARGB);
		c.paint(image.getGraphics());       
		try {
			File f = new File( dir + "/" + fileName );
			ImageIO.write(image, "png", f);
		}catch( Exception e ){
			System.out.println("ERROR: " + e.getMessage());
		}
	}	
	
}

class PersistentGraph extends JComponent
{	
	JComponent graphPanel;
	
	public PersistentGraph() {
		super();
	}
	
	public PersistentGraph( JComponent graphPanel ) {
		super();
		this.graphPanel = graphPanel;
	}
	
	public void paint( Graphics g )
	{
		int height = 100;
		int width  = 300;
    }    	
}


	
