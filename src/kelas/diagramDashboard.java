/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package kelas;
import java.awt.Color;
import java.awt.BasicStroke;
import java.awt.geom.Ellipse2D;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.category.DefaultCategoryDataset;

/**
 *
 * @author Sukma Nur
 */
public class diagramDashboard {
  public ChartPanel getDiagramPanel() {

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        // Data dummy
        dataset.addValue(85, "Kehadiran", "Januari");
        dataset.addValue(78, "Kehadiran", "Februari");
        dataset.addValue(90, "Kehadiran", "Maret");
        dataset.addValue(82, "Kehadiran", "April");

        JFreeChart chart = ChartFactory.createLineChart(
            "", "Bulan", "Persentase", dataset
        );

        Color merahTua = new Color(140, 22, 22);
        Color cream = new Color(255, 255, 255);

        CategoryPlot plot = chart.getCategoryPlot();
        plot.setBackgroundPaint(cream);
        plot.setOutlinePaint(null);
        plot.setRangeGridlinePaint(merahTua);
        plot.setDomainGridlinePaint(merahTua);

        LineAndShapeRenderer renderer = new LineAndShapeRenderer();
        renderer.setSeriesPaint(0, merahTua);
        renderer.setSeriesStroke(0, new BasicStroke(3.0f));
        renderer.setSeriesShape(0, new Ellipse2D.Double(-4, -4, 8, 8));
        renderer.setSeriesShapesVisible(0, true);

        plot.setRenderer(renderer);

        return new ChartPanel(chart);
    }

}


