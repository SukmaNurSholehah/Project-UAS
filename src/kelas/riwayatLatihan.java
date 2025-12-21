/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package kelas;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import java.awt.Color;
import java.awt.Font;
import java.sql.SQLException;
import java.util.Date;
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

    public DefaultTableModel tampilRiwayatLatihan() {
     DefaultTableModel model = new DefaultTableModel(){
        //nonaktif edit tabel
         @Override
         public boolean isCellEditable(int row, int column) {
            return false; // tabel tidak bisa diedit
        }
        };
        model.addColumn("No.");
        model.addColumn("ID Jadwal");
        model.addColumn("Tanggal");
        model.addColumn("Lokasi");
        model.addColumn("Pelatih");
        model.addColumn("Keterangan");
        model.addColumn("Tidak Hadir");
        try {
            query = "SELECT j.ID_jadwal, j.tgl, j.lokasi, p.nama_pelatih, j.keterangan, "
                    + "SUM(al.hadir = 0) AS tidak_hadir FROM absensi_latihan al "
                    + "LEFT JOIN jadwal_latihan j ON al.ID_jadwal = j.ID_jadwal "
                    + "LEFT JOIN pelatih p ON j.pelatih=p.ID_pelatih "
                    + "GROUP BY j.ID_jadwal, j.tgl, j.lokasi, "
                    + "p.nama_pelatih, j.keterangan";
            st = koneksi.createStatement();
            rs = st.executeQuery(query);

            int no = 1;
            while (rs.next()) {
                model.addRow(new Object[]{
                    no++,
                    rs.getString("ID_jadwal"),
                    rs.getString("tgl"),
                    rs.getString("lokasi"),
                    rs.getString("nama_pelatih"),
                    rs.getString("keterangan"),
                    rs.getString("tidak_hadir")
                });
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return model;
    }

    public DefaultTableModel tampilDetailRiwayat(String idJadwal) {
       DefaultTableModel model = new DefaultTableModel(){
        //nonaktif edit tabel
         @Override
         public boolean isCellEditable(int row, int column) {
            return false; // tabel tidak bisa diedit
        }
        };
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

    public DefaultTableModel filterRiwayatLatihan(Date tglAwal, Date tglAkhir) {
     DefaultTableModel model = new DefaultTableModel(){
        //nonaktif edit tabel
         @Override
         public boolean isCellEditable(int row, int column) {
            return false; // tabel tidak bisa diedit
        }
        };
        model.addColumn("No.");
        model.addColumn("ID Jadwal");
        model.addColumn("Tanggal");
        model.addColumn("Lokasi");
        model.addColumn("Pelatih");
        model.addColumn("Keterangan");
        model.addColumn("Tidak Hadir");

        try {
             query = "SELECT j.ID_jadwal, j.tgl, j.lokasi, p.nama_pelatih, j.keterangan, "
                    + "SUM(al.hadir = 0) AS tidak_hadir "
                    + "FROM absensi_latihan al "
                    + "LEFT JOIN jadwal_latihan j ON al.ID_jadwal = j.ID_jadwal "
                    + "LEFT JOIN pelatih p ON j.pelatih = p.ID_pelatih "
                    + "WHERE j.tgl BETWEEN ? AND ? "
                    + "GROUP BY j.ID_jadwal, j.tgl, j.lokasi, "
                    + "p.nama_pelatih, j.keterangan";

            ps = koneksi.prepareStatement(query);
            ps.setDate(1, new java.sql.Date(tglAwal.getTime()));
            ps.setDate(2, new java.sql.Date(tglAkhir.getTime()));

            rs = ps.executeQuery();

            int no = 1;
            while (rs.next()) {
                model.addRow(new Object[]{
                    no++,
                    rs.getString("ID_jadwal"),
                    rs.getDate("tgl"),
                    rs.getString("lokasi"),
                    rs.getString("nama_pelatih"),
                    rs.getString("keterangan"),
                    rs.getInt("tidak_hadir")
                });
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return model;
    }

    public void aturTableDetail(JTable tData) {
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

    public void aturTableRiwayat(JTable tData) {
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
        tData.getColumnModel().getColumn(1).setPreferredWidth(150); // ID Jadwal
        tData.getColumnModel().getColumn(2).setPreferredWidth(100); // Tanggal
        tData.getColumnModel().getColumn(3).setPreferredWidth(100);  // Lokasi
        tData.getColumnModel().getColumn(4).setPreferredWidth(200); // Nama Pelatih
        tData.getColumnModel().getColumn(5).setPreferredWidth(250); // keterangan
        tData.getColumnModel().getColumn(5).setPreferredWidth(50); // Jumlah Tidak Hadir
    }
}
