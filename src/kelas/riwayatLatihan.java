/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package kelas;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import java.awt.Color;
import java.awt.Font;
import java.sql.SQLException;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author Sukma Nur
 */
public class riwayatLatihan extends koneksi {

    private Date tglLatihan;
    private String lokasiLatihan, keteranganLatihan;

    private final Connection koneksi;
    private PreparedStatement ps;
    private Statement st;
    private ResultSet rs;
    private String query;

    public riwayatLatihan() {
        koneksi = super.configDB();
    }

    public Date getTglLatihan() {
        return tglLatihan;
    }

    public void setTglLatihan(Date tglLatihan) {
        this.tglLatihan = tglLatihan;
    }

    public String getLokasiLatihan() {
        return lokasiLatihan;
    }

    public void setLokasiLatihan(String lokasiLatihan) {
        this.lokasiLatihan = lokasiLatihan;
    }

    public String getKeteranganLatihan() {
        return keteranganLatihan;
    }

    public void setKeteranganLatihan(String keteranganLatihan) {
        this.keteranganLatihan = keteranganLatihan;
    }

    public void tampilRiwayatLatihan(JTable tabel) {
        DefaultTableModel model = new DefaultTableModel();

        model.addColumn("ID");
        model.addColumn("Tanggal");
        model.addColumn("Lokasi");
        model.addColumn("Pelatih");
        model.addColumn("Keterangan");

        try {
            query = "SELECT ID_jadwal, tgl, lokasi, pelatih, keterangan FROM jadwal_latihan";
            ps = koneksi.prepareStatement(query);
            rs = ps.executeQuery();

            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getString("ID_jadwal"),
                    rs.getDate("tgl"),
                    rs.getString("lokasi"),
                    rs.getString("pelatih"),
                    rs.getString("keterangan")
                });
            }

            tabel.setModel(model);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    public DefaultTableModel tampilDetailRiwayat(String idJadwal) {
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("No");
        model.addColumn("Nama Anggota");
        model.addColumn("Kehadiran");

        try {
            String sql = "SELECT a.nama_anggota, al.hadir "
                    + "FROM absensi_latihan al "
                    + "JOIN anggota a ON al.ID_anggota = a.ID_anggota "
                    + "WHERE al.ID_jadwal = ? ";

             ps = koneksi.prepareStatement(sql);
            ps.setString(1, idJadwal);
             rs = ps.executeQuery();

            int no = 1;
            while (rs.next()) {
                String nama = rs.getString("nama_anggota");
                int hadir = rs.getInt("hadir");

                String statusKehadiran = (hadir == 1) ? "HADIR" : "TIDAK HADIR";

                Object baris[] = {no++, nama, statusKehadiran};
                model.addRow(baris);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
            e.printStackTrace();
        }

        return model;
    }
    
     public void aturTable(JTable tData) {
        // Warna lembut untuk header
        tData.getTableHeader().setBackground(new Color(102, 204, 255)); // biru pucat (baby blue)
        tData.getTableHeader().setForeground(Color.BLACK);
        tData.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));

        // Warna sel tabel (hitam putih natural)
        tData.setBackground(Color.WHITE);
        tData.setForeground(Color.BLACK);
        tData.setGridColor(Color.LIGHT_GRAY);
        tData.setSelectionBackground(new Color(220, 240, 255)); // biru muda saat dipilih
        tData.setSelectionForeground(Color.BLACK);

        // === Mengatur rata tengah teks di tabel ===
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);

        // Rata tengah untuk semua kolom
        for (int i = 0; i < tData.getColumnCount(); i++) {
            tData.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        // Rata tengah header kolom juga
        ((DefaultTableCellRenderer) tData.getTableHeader().getDefaultRenderer())
                .setHorizontalAlignment(SwingConstants.CENTER);

        // Mengatur lebar kolom
        tData.getColumnModel().getColumn(0).setPreferredWidth(30);  // No
        tData.getColumnModel().getColumn(1).setPreferredWidth(150); // Nama Anggota
        tData.getColumnModel().getColumn(2).setPreferredWidth(100); // Kehadiran

    }
}
