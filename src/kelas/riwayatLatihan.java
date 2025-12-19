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
import kelas.jadwal;

/**
 *
 * @author Sukma Nur
 */
public class riwayatLatihan extends koneksi {
    
    private Date tglLatihan;
    private String lokasiLatihan, keteranganLatihan;
    
    private final Connection koneksi ;
    private PreparedStatement ps;
    private Statement st;
    private ResultSet rs;
    private String query;
    
    public riwayatLatihan(){
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
     
     public ResultSet getAbsensiTidakHadir(String idJadwal) {
        ResultSet rs = null;
        try {
            String sql =
                "SELECT a.nama_anggota " +
                "FROM absensi_latihan al " +
                "JOIN anggota a ON al.ID_anggota = a.ID_anggota " +
                "WHERE al.hadir = 0 AND al.ID_jadwal = ?";

            PreparedStatement ps = koneksi.prepareStatement(sql);
            ps.setString(1, idJadwal); // ✅ SEKARANG MASUK AKAL
            rs = ps.executeQuery();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
        return rs;
    }
}

