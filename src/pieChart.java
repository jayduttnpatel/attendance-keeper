
import java.awt.Color;
import javax.swing.JFrame;
import javax.swing.JPanel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.PieDataset;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.plot.PiePlot3D;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.util.Rotation;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author jaydutt
 */
public class pieChart{
    
    public pieChart(JPanel panel,int present,int absent)
    {
        int persen=(int)((present)*100.0/(present+absent));
        PieDataset data=createPieDataset(persen,100-persen);
        
        JFreeChart chart= createChart(data,"attendance");
        
        ChartPanel chartPanel=new ChartPanel(chart);
        
        chart.setBackgroundPaint(new Color(224,255,255));
        
        chartPanel.setBackground(new Color(224,255,255));
        
        chartPanel.setPreferredSize(new java.awt.Dimension(540,400));
     
        panel.add(chartPanel);
    }
    
    private PieDataset createPieDataset(int present,int absent)
    {
        DefaultPieDataset dataset=new DefaultPieDataset();
        
        dataset.setValue("present",present);
        dataset.setValue("absent",absent);
        
        return dataset;
    }
    
    private JFreeChart createChart(PieDataset dataset,String title)
    {
        JFreeChart chart=ChartFactory.createPieChart3D(title, dataset, true ,true,false);
        
        PiePlot3D plot=(PiePlot3D)chart.getPlot();
        
        plot.setStartAngle(0);
        
        plot.setDirection(Rotation.CLOCKWISE);
        
        plot.setForegroundAlpha(0.8f);
        
        return chart;
    }
    
}
