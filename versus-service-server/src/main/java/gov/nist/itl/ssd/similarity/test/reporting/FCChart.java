package gov.nist.itl.ssd.similarity.test.reporting;

import java.awt.Font;
import java.text.DecimalFormat;

import javax.swing.JComponent;
import javax.swing.JFrame;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.labels.StandardCategoryToolTipGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.CombinedDomainCategoryPlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

import fj.data.Array;

import gov.nist.itl.ssd.similarity.test.execution.function.Arity5Input;

public class FCChart
{
	
	 protected Arity5Input<Double,Double,Double,Double,Double> data;
	
	 public FCChart(String title, Array<Double> values ) 
	 {	        
	        data = new Arity5Input<Double,Double,Double,Double,Double>();
	        data.i1( values.get(0) );
	        data.i2( values.get(1) );
	        data.i3( values.get(2) );
	        data.i4( values.get(3) );
	        data.i5( values.get(4) );
	        
	    }
	 
	 public Arity5Input<Double,Double,Double,Double,Double> data(){ return data; }
	 public void data( Arity5Input<Double,Double,Double,Double,Double> v ){ this.data=v;}
	 
	 public CategoryDataset createDataset() {

	     final DefaultCategoryDataset result = new DefaultCategoryDataset();

	        // column keys...
	        final String type1 = "HW";
	        final String type2 = "SW";
	        final String type3 = "Image";
	        final String type4 = "Math";
	        final String type5 = "Singularities";

	        result.addValue(data.i1(), "", type1);
	        result.addValue(data.i2(), "", type2);
	        result.addValue(data.i3(), "", type3);
	        result.addValue(data.i4(), "", type4);
	        result.addValue(data.i5(), "", type5);
	        return result;
	    }


	   public JFreeChart createChart() 
	   {
	        final CategoryDataset dataset = createDataset();
	        final NumberAxis rangeAxis = new NumberAxis("Frequency");
	        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
	        final BarRenderer renderer = new BarRenderer();
	        renderer.setBaseToolTipGenerator(new StandardCategoryToolTipGenerator());
	        renderer.setItemLabelGenerator( new StandardCategoryItemLabelGenerator("{2}", new DecimalFormat("##")));
	        renderer.setItemLabelsVisible(true);
	        final CategoryPlot subplot = new CategoryPlot(dataset, null, rangeAxis, renderer);
	        subplot.setDomainGridlinesVisible(true);
	        final CategoryAxis domainAxis = new CategoryAxis("Category");
	        final CombinedDomainCategoryPlot plot = new CombinedDomainCategoryPlot(domainAxis);
	        plot.add(subplot, 1);
	        
	        final JFreeChart result = new JFreeChart(
	            "Fault Category Chart",
	            new Font("SansSerif", Font.BOLD, 12),
	            plot,
	            true
	        );
	  //      result.getLegend().setAnchor(Legend.SOUTH);
	        return result;
	    }
	   
}
