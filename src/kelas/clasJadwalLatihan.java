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
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author ASUS
 */
public class clasJadwalLatihan extends koneksi {

    String idJadwal, tgl, lokasi, pelatih, keterangan;
    private final Connection cn;
    private PreparedStatement ps;
    private Statement st;
    private ResultSet rs;
    private String query;

    public clasJadwalLatihan() {
        cn = configDB();
    }

    public String getIdJadwal() {
        return idJadwal;
    }

    public void setIdJadwal(String idJadwal) {
        this.idJadwal = idJadwal;
    }

    public String getTgl() {
        return tgl;
    }

    public void setTgl(String tgl) {
        this.tgl = tgl;
    }

    public String getLokasi() {
        return lokasi;
    }

    public void setLokasi(String lokasi) {
        this.lokasi = lokasi;
    }

    public String getPelatih() {
        return pelatih;
    }

    public void setPelatih(String pelatih) {
        this.pelatih = pelatih;
    }

    public String getKeterangan() {
        return keterangan;
    }

    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
    }

    public void tambahJadwal() {
        query = "INSERT INTO jadwal_latihan VALUES (?,?,?,?,?)";
        try {
            ps = cn.prepareStatement(query);
            ps.setString(1, idJadwal);
            ps.setString(2, tgl);
            ps.setString(3, lokasi);
            ps.setString(4, pelatih);
            ps.setString(5, keterangan);
            ps.executeUpdate();
            ps.close();
            JOptionPane.showMessageDialog(null, "Data Berhasil Disimpan");
        } catch (SQLException e) {
            System.out.println(e);
            JOptionPane.showMessageDialog(null, "error");
        }
    }

    public void ubahJadwal() {
        query = "UPDATE jadwal_latihan SET lokasi=?, pelatih=?, keterangan =? "
                + "WHERE ID_jadwal =?";
        try {
            ps = cn.prepareStatement(query);
            ps.setString(1, lokasi);
            ps.setString(2, pelatih);
            ps.setString(3, keterangan);
            ps.setString(4, idJadwal);

            ps.executeUpdate();
            ps.close();
            JOptionPane.showMessageDialog(null, "Data Berhasil Diubah");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Data Gagal Diubah");
            System.out.println(e);
        }
    }

    public void hapusData() {
        try {
            query = "DELETE FROM jadwal_latihan WHERE ID_jadwal = ?";

            ps = cn.prepareStatement(query);
            ps.setString(1, idJadwal);

            ps.executeUpdate();

            JOptionPane.showMessageDialog(null, "Data berhasil dihapus");
        } catch (SQLException e) {
            System.out.println(e);
            JOptionPane.showMessageDialog(null, "Error : ");
        }
    }

    public void autoID(JLabel tID) {
        try {
            query = "SELECT MAX(ID_jadwal) FROM jadwal_latihan";

            st = cn.createStatement();
            rs = st.executeQuery(query);

            String id = "JDL001";
            if (rs.next()) {
                String maxID = rs.getString(1);
                if (maxID != null) {
                    int num = Integer.parseInt(maxID.substring(3));
                    num++;
                    id = String.format("JDL%03d", num);
                }
            }
            tID.setText(id);
        } catch (SQLException e) {
            System.out.println(e);
            JOptionPane.showMessageDialog(null, "Error : ");
        }
    }

    public void tampilDataJadwal(String tgl, JLabel id, JLabel tanggal, JTextField lokasi,
            JComboBox pelatih, JTextArea keterangan) {
        query = "SELECT j.ID_jadwal, j.tgl, j.lokasi, p.nama_pelatih, j.keterangan "
                + "FROM jadwal_latihan j "
                + "JOIN pelatih p ON j.pelatih = p.ID_pelatih "
                + "WHERE j.tgl = ?";
        try {
            ps = cn.prepareStatement(query);
            ps.setString(1, tgl);
            rs = ps.executeQuery();

            if (rs.next()) {
                id.setText(rs.getString("ID_jadwal"));
                tanggal.setText(rs.getString("tgl"));
                lokasi.setText(rs.getString("lokasi"));
                pelatih.setSelectedItem(rs.getString("nama_pelatih"));
                keterangan.setText(rs.getString("keterangan"));
            } else {
                lokasi.setText("");
                pelatih.setSelectedIndex(0);
                keterangan.setText("");
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    public boolean cekDataJadwal(String tgl) {
        query = "SELECT * FROM jadwal_latihan WHERE tgl =?";
        try {
            ps = cn.prepareStatement(query);
            ps.setString(1, tgl);
            rs = ps.executeQuery();
            return rs.next(); // jika ada data -> true
        } catch (SQLException e) {
            System.out.println(e);
        }
        return false;
    }

    public String tampilKeterangan(String tgl) {
        query = "SELECT j.ID_jadwal, j.tgl, j.lokasi, p.nama_pelatih, j.keterangan"
                + " FROM jadwal_latihan j "
                + "JOIN pelatih p ON j.pelatih = p.ID_pelatih WHERE j.tgl=?";
        try {
            ps = cn.prepareStatement(query);
            ps.setString(1, tgl);
            rs = ps.executeQuery();

            if (rs.next()) {
                return "Tanggal  : " + rs.getString("tgl") + "\n"
                        + "Lokasi  : " + rs.getString("lokasi") + "\n"
                        + "Pelatih : " + rs.getString("nama_pelatih") + "\n"
                        + rs.getString("keterangan");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Data Gagal Ditampilkan");
            System.out.println(e);
        }
        return "Tidak ada kegiatan pada tanggal ini.";
    }

    public void editData(String tgl, JTextField id, JTextField lokasi, JComboBox pelatih, JTextField materi) {
        query = "SELECT j.ID_jadwal, j.lokasi, p.nama_pelatih, j.keterangan FROM jadwal_latihan j "
                + "JOIN pelatih p ON j.pelatih = p.ID_pelatih  WHERE j.tgl =?";
        try {
            ps = cn.prepareStatement(query);
            ps.setString(1, tgl);
            rs = ps.executeQuery();
            if (rs.next()) {
                id.setText(rs.getString("ID_jadwal"));
                lokasi.setText(rs.getString("lokasi"));
                pelatih.setSelectedItem(rs.getString("nama_pelatih"));
                materi.setText(rs.getString("keterangan"));
            } else {
                lokasi.setText("");
                pelatih.setSelectedIndex(0);
                materi.setText("");
            }

        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    public DefaultTableModel tampilJadwal() {
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("No.");
        model.addColumn("ID Jadwal");
        model.addColumn("Tanggal");
        model.addColumn("Lokasi");
        model.addColumn("Pelatih");
        model.addColumn("Keterangan");
        try {
            query = "SELECT j.ID_jadwal, j.tgl, j.lokasi, p.nama_pelatih, j.keterangan "
                    + "FROM jadwal_latihan j "
                    + "JOIN pelatih p ON j.pelatih=p.ID_pelatih ";
            st = cn.createStatement();
            rs = st.executeQuery(query);

            int no = 1;
            while (rs.next()) {
                model.addRow(new Object[]{
                    no++,
                    rs.getString("ID_jadwal"),
                    rs.getString("tgl"),
                    rs.getString("lokasi"),
                    rs.getString("nama_pelatih"),
                    rs.getString("keterangan")
                });
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return model;
    }

    public void comboPelatih(JComboBox cPelatih) {
        try {
            query = "SELECT nama_pelatih FROM pelatih";
            st = cn.createStatement();
            rs = st.executeQuery(query);

            while (rs.next()) {
                cPelatih.addItem(rs.getString("nama_pelatih"));

            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        cPelatih.setSelectedIndex(0);
    }

    public String konversIDpelatih(String namaPelatih) {
        String idPelatih = "";
        try {
            query = "SELECT ID_pelatih FROM pelatih WHERE nama_pelatih=?";
            ps = cn.prepareStatement(query);
            ps.setString(1, namaPelatih);
            rs = ps.executeQuery();
            while (rs.next()) {
                idPelatih = rs.getString("ID_pelatih");
            }
            ps.close();
        } catch (SQLException e) {
        }
        return idPelatih;
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
        tData.getColumnModel().getColumn(1).setPreferredWidth(100); // ID Jadwal
        tData.getColumnModel().getColumn(2).setPreferredWidth(100); // Tanggal Mulai
        tData.getColumnModel().getColumn(3).setPreferredWidth(100); // Lokasi
        tData.getColumnModel().getColumn(4).setPreferredWidth(150); //Nama Pelatih
        tData.getColumnModel().getColumn(5).setPreferredWidth(250); // Keterangan

    }

}
