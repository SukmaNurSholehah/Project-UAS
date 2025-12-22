/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package kelas;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.awt.BasicStroke;
import java.awt.Color;
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
  public ChartPanel getDiagramBulanan() {

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        try {
            koneksi k = new koneksi();
            Connection conn = k.configDB();

            String sql =
                "SELECT DATE_FORMAT(tgl, '%Y-%m') AS bulan, " +
                "ROUND((SUM(hadir) / COUNT(*)) * 100) AS persentase " +
                "FROM absensi_latihan " +
                "GROUP BY DATE_FORMAT(tgl, '%Y-%m') " +
                "ORDER BY bulan";

            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String bulan = rs.getString("bulan");   // contoh: 2025-01
                int persen = rs.getInt("persentase");   // contoh: 82

                dataset.addValue(persen, "Kehadiran", bulan);
            }

            rs.close();
            ps.close();
            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        JFreeChart chart = ChartFactory.createLineChart(
            null,
            "Bulan",
            "Persentase Kehadiran",
            dataset
        );

        Color merahTua = new Color(140, 22, 22);
        Color cream = new Color(255, 245, 230);

        CategoryPlot plot = chart.getCategoryPlot();
        plot.setBackgroundPaint(cream);
        plot.setRangeGridlinePaint(merahTua);

        LineAndShapeRenderer renderer = new LineAndShapeRenderer();
        renderer.setSeriesPaint(0, merahTua);
        renderer.setSeriesStroke(0, new BasicStroke(3.0f));
        renderer.setSeriesShapesVisible(0, true);

        plot.setRenderer(renderer);

        return new ChartPanel(chart);
    }
}


