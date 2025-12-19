/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package kelas;

import java.awt.HeadlessException;
import java.sql.*;
import javax.swing.*;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author naila
 */
public class riwayat extends koneksi {

    Connection conn;
    PreparedStatement ps;
    Statement st;
    ResultSet rs;
    String query;

    public riwayat() {
        conn = super.configDB();
    }

    public String autoId() {
        String newID = "R001";
        String query = "SELECT MAX(id_riwayat) FROM riwayat_latihan";

        try {
            st = conn.createStatement();
            rs = st.executeQuery(query);

            if (rs.next() && rs.getString(1) != null) {
                String lastID = rs.getString(1).substring(1);
                int next = Integer.parseInt(lastID) + 1;
                newID = String.format("R%03d", next);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
        return newID;
    }

    public DefaultTableModel tampilDetail(String idRiwayat) {

        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("No");
        model.addColumn("Nama Anggota");
        model.addColumn("Kehadiran");

        String query
                = "SELECT a.nama_anggota, d.kehadiran "
                + "FROM detail_riwayat_latihan d "
                + "JOIN anggota a ON d.id_anggota = a.ID_anggota "
                + "WHERE d.id_riwayat = ? " 
                + "ORDER BY a.nama_anggota ASC";

        try {
            ps = conn.prepareStatement(query);
            ps.setString(1, idRiwayat);
            rs = ps.executeQuery();

            int no = 1;
            while (rs.next()) {
                model.addRow(new Object[]{
                    no++,
                    rs.getString("nama_anggota"),
                    rs.getString("kehadiran")
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }

        return model;
    }

    public void tambahDetail(String idRiwayat, String idAnggota, String kehadiran) {
        String sql = "INSERT INTO detail_riwayat_latihan "
                + "(id_riwayat, id_anggota, kehadiran) VALUES (?, ?, ?)";

        try {
            ps = conn.prepareStatement(sql);
            ps.setString(1, idRiwayat);
            ps.setString(2, idAnggota);
            ps.setString(3, kehadiran);
            ps.executeUpdate();

            JOptionPane.showMessageDialog(null, "Detail riwayat berhasil disimpan");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    //untuk tampil daata
    public DefaultTableModel tampilData() {
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("ID");
        model.addColumn("Tanggal");
        model.addColumn("Lokasi");
        model.addColumn("Pelatih");
        model.addColumn("Keterangan");

        String query = "SELECT * FROM riwayat_latihan ORDER BY id_riwayat ASC";

        try {
            st = conn.createStatement();
            rs = st.executeQuery(query);

            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getString("id_riwayat"),
                    rs.getString("tanggal"),
                    rs.getString("lokasi"),
                    rs.getString("pelatih"),
                    rs.getString("keterangan")
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
        return model;
    }

    //tambah data 
    public void tambah(String id, String tanggal, String lokasi, String pelatih, String keterangan) {
        query = "INSERT INTO riwayat_latihan (id_riwayat, tanggal, lokasi, pelatih, keterangan) VALUES (?, ?, ?, ?, ?)";

        try {
            ps = conn.prepareStatement(query);
            ps.setString(1, id);
            ps.setString(2, tanggal);
            ps.setString(3, lokasi);
            ps.setString(4, pelatih);
            ps.setString(5, keterangan);
            ps.executeUpdate();

            JOptionPane.showMessageDialog(null, "Data berhasil ditambah");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }

    }

    //ubah data 
    public void ubah(String id, String tanggal, String lokasi, String pelatih, String keterangan) {
        query = "UPDATE riwayat_latihan SET tanggal=?, lokasi=?, pelatih=?, keterangan=? WHERE id_riwayat=?";
        try {
            ps = conn.prepareStatement(query);
            ps.setString(1, tanggal);
            ps.setString(2, lokasi);
            ps.setString(3, pelatih);
            ps.setString(4, keterangan);
            ps.setString(5, id);
            ps.executeUpdate();

            JOptionPane.showMessageDialog(null, "Data berhasil diubah");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
        
        
    }
    
    

}
