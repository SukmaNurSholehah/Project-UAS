/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package kelas;

import java.awt.Color;
import java.awt.Font;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author HP
 */
public class hasilUjian extends koneksi {

    String id_hasil, id_anggota, id_registrasi;
    private final Connection con; //penggunaan FINAL membuat variabel koneksi hanya bisa diisi 1x 
    private PreparedStatement ps;
    private Statement st;
    private ResultSet rs;
    private String query;

    public hasilUjian() {
        con = super.configDB();
    }

    public String getId_hasil() {
        return id_hasil;
    }

    public void setId_hasil(String id_hasil) {
        this.id_hasil = id_hasil;
    }

    public String getId_anggota() {
        return id_anggota;
    }

    public void setId_anggota(String id_anggota) {
        this.id_anggota = id_anggota;
    }

    public String getId_registrasi() {
        return id_registrasi;
    }

    public void setId_registrasi(String id_registrasi) {
        this.id_registrasi = id_registrasi;
    }

    public DefaultTableModel detailHasil(String IDregis) {
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("No");
        model.addColumn("ID Peserta");
        model.addColumn("Nama Peserta");
        model.addColumn("Status");

        try {
            query = "SELECT a.ID_anggota, a.nama_anggota, au.hadir "
                    + "FROM absensi_ujian au "
                    + "JOIN anggota a ON au.ID_anggota = a.ID_anggota "
                    + "JOIN registrasi r ON au.ID_registrasi = r.ID_registrasi "
                    + "JOIN kegiatan k ON r.parent_ujian = k.ID_kegiatan "
                    + "WHERE au.ID_registrasi =?";

            ps = con.prepareStatement(query);
            ps.setString(1, IDregis);
            rs = ps.executeQuery();

            int no = 1;
            while (rs.next()) {
                String id = rs.getString("ID_anggota");
                String nama = rs.getString("nama_anggota");
                int hadir = rs.getInt("hadir");

                String statusKehadiran = (hadir == 1) ? "LULUS" : "TIDAK LULUS";

                Object baris[] = {no++, id, nama, statusKehadiran};
                model.addRow(baris);
            }
        } catch (SQLException e) {
            System.out.println(e);
            
        }
        return model;
    }

    public DefaultTableModel hasilUjian() {
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("No");
        model.addColumn("ID Kegiatan");
        model.addColumn("Nama Kegiatan");
        model.addColumn("Tanggal Mulai");
        model.addColumn("Tanggal Selesai");
        model.addColumn("Lokasi");
        model.addColumn("Peserta");

        try {
            query = "SELECT k.ID_kegiatan, k.nama_kegiatan, k.tgl_mulai, k.tgl_selesai, "
                    + "k.lokasi, SUM(au.hadir = 1) AS jumlah_hadir "
                    + "FROM kegiatan k "
                    + "LEFT JOIN registrasi r ON k.ID_kegiatan = r.parent_ujian "
                    + "LEFT JOIN absensi_ujian au ON r.ID_registrasi = au.ID_registrasi "
                    + "GROUP BY k.ID_kegiatan, k.nama_kegiatan, k.tgl_mulai, "
                    + "k.tgl_selesai, k.lokasi";

            st = con.createStatement();
            rs = st.executeQuery(query);

            int no = 1;
            while (rs.next()) {
                model.addRow(new Object[]{
                    no++,
                    rs.getString("ID_kegiatan"),
                    rs.getString("nama_kegiatan"),
                    rs.getString("tgl_mulai"),
                    rs.getString("tgl_selesai"),
                    rs.getString("lokasi"),
                    rs.getString("jumlah_hadir")
                });
            }
        } catch (SQLException e) {
            System.out.println(e);
        }

        return model;
    }
    
    public DefaultTableModel serchNama(String kataKunci){
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("No");
        model.addColumn("ID Peserta");
        model.addColumn("Nama Peserta");
        model.addColumn("Status");
        try {
             query = "SELECT a.ID_anggota, a.nama_anggota, au.hadir "
                    + "FROM absensi_ujian au "
                    + "JOIN anggota a ON au.ID_anggota = a.ID_anggota "
                    + "JOIN registrasi r ON au.ID_registrasi = r.ID_registrasi "
                    + "JOIN kegiatan k ON r.parent_ujian = k.ID_kegiatan "
                    + "WHERE nama_anggota LIKE ?";
            ps = con.prepareStatement(query);
            ps.setString(1, "%" + kataKunci + "%");
            rs = ps.executeQuery();

            int no = 1;
             while (rs.next()) {
                String id = rs.getString("ID_anggota");
                String nama = rs.getString("nama_anggota");
                int hadir = rs.getInt("hadir");

                String statusKehadiran = (hadir == 1) ? "LULUS" : "TIDAK LULUS";

                Object baris[] = {no++, id, nama, statusKehadiran};
                model.addRow(baris);
             }
        } catch (SQLException e) {
            System.out.println(e);
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
        tData.getColumnModel().getColumn(1).setPreferredWidth(100); // ID Kegiatan
        tData.getColumnModel().getColumn(2).setPreferredWidth(200); // Nama Kegiatan
        tData.getColumnModel().getColumn(3).setPreferredWidth(100); // Tanggal Mulai
        tData.getColumnModel().getColumn(4).setPreferredWidth(100); // Tanggal Selesai
        tData.getColumnModel().getColumn(5).setPreferredWidth(100); // Lokasi
        tData.getColumnModel().getColumn(6).setPreferredWidth(100); // Jumlah Anggota yg LULUS

    }

}
