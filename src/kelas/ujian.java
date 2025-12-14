/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package kelas;

import java.awt.Color;
import java.awt.Font;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.SQLException;
import java.sql.ResultSet;
import javax.swing.JOptionPane;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author HP
 */
public class ujian extends koneksi {

    String ID_kegiatan, nama_kegiatan, lokasi, pelatih, tgl_mulai, tgl_selesai;
    private final Connection con; //penggunaan FINAL membuat variabel koneksi hanya bisa diisi 1x 
    private PreparedStatement ps;
    private Statement st;
    private ResultSet rs;
    private String query;

    public ujian() {
        con = super.configDB();
    }

    public String getID_kegiatan() {
        return ID_kegiatan;
    }

    public void setID_kegiatan(String ID_event) {
        this.ID_kegiatan = ID_event;
    }

    public String getNama_kegiatan() {
        return nama_kegiatan;
    }

    public void setNama_kegiatan(String nama_kegiatan) {
        this.nama_kegiatan = nama_kegiatan;
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

    public String getTgl_mulai() {
        return tgl_mulai;
    }

    public void setTgl_mulai(String tgl_mulai) {
        this.tgl_mulai = tgl_mulai;
    }

    public String getTgl_selesai() {
        return tgl_selesai;
    }

    public void setTgl_selesai(String tgl_selesai) {
        this.tgl_selesai = tgl_selesai;
    }

    public void tambah_ujian() {
        query = "INSERT INTO kegiatan VALUES (?,?,?,?,?,?)";
        try {
            ps = con.prepareStatement(query);
            ps.setString(1, ID_kegiatan);
            ps.setString(2, nama_kegiatan);
            ps.setString(3, tgl_mulai);
            ps.setString(4, tgl_selesai);
            ps.setString(5, lokasi);
            ps.setString(6, pelatih);
            ps.executeUpdate();
            ps.close();
            JOptionPane.showMessageDialog(null, "Data Berhasil Ditambahkan ");
        } catch (SQLException e) {
            System.out.println(e);
            JOptionPane.showMessageDialog(null, "Data Gagal Ditambahkan ");
        }
    }

    public void ubah_ujian() {
        query = "UPDATE kegiatan SET nama_kegiatan=?, tgl_mulai=?, tgl_selesai=?, lokasi=?,"
                + " pelatih=? WHERE ID_kegiatan=?";
        try {
            ps = con.prepareStatement(query);
            ps.setString(1, nama_kegiatan);
            ps.setString(2, tgl_mulai);
            ps.setString(3, tgl_selesai);
            ps.setString(4, lokasi);
            ps.setString(5, pelatih);
            ps.setString(6, ID_kegiatan);
            ps.executeUpdate();
            ps.close();
            JOptionPane.showMessageDialog(null, "Data Berhasil Diubah ");
        } catch (SQLException e) {
            System.out.println(e);
            JOptionPane.showMessageDialog(null, "Data Gagal Diubah ");
        }
    }

    public void hapus_ujian() {
        query = "DELETE FROM kegiatan WHERE ID_kegiatan = ?";
        try {
            ps = con.prepareStatement(query);
            ps.setString(1, ID_kegiatan);

            ps.executeUpdate();
            ps.close();
            JOptionPane.showMessageDialog(null, "Data Berhasil Dihapus ");
        } catch (SQLException e) {
            System.out.println(e);
            JOptionPane.showMessageDialog(null, "Data Gagal Dihapus ");
        }
    }

    public DefaultTableModel showKegiatan() {
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("No");
        model.addColumn("ID Kegiatan");
        model.addColumn("Nama Kegiatan");
        model.addColumn("Tanggal Mulai");
        model.addColumn("Tanggal Selesai");
        model.addColumn("Lokasi");
        model.addColumn("Penguji");

        try {
            query = "SELECT e.ID_kegiatan, e.nama_kegiatan, e.tgl_mulai, e.tgl_selesai, "
                    + "e.lokasi, p.nama_pelatih "
                    + "FROM event_ujian e "
                    + "JOIN pelatih p ON e.pelatih = p.ID_pelatih ";
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
                    rs.getString("nama_pelatih")
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
            st = con.createStatement();
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
            ps = con.prepareStatement(query);
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

    public String getIdKegiatanByName(String namaKegiatan) {
        String idEvent = "";
        try {
            query = "SELECT ID_kegiatan FROM kegiatan WHERE nama_kegiatan=?";
            ps = con.prepareStatement(query);
            ps.setString(1, namaKegiatan);
            rs = ps.executeQuery();

            if (rs.next()) {
                idEvent = rs.getString("ID_kegiatan");
            }
            ps.close();
            rs.close();
        } catch (SQLException e) {
            System.out.println(e);
        }
        return idEvent;
    }

    public void autoID(JTextField t_idevent) {
        try {
            query = "SELECT MAX(ID_kegiatan) FROM kegiatan";
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(query);

            String id = "KGT001"; //default awal
            if (rs.next()) {
                String maxID = rs.getString(1);
                if (maxID != null) {
                    int num = Integer.parseInt(maxID.substring(3)); //ambil angka setelah "KAT"
                    num++;
                    id = String.format("KGT%03d", num); //format ulang jadi 3 digit
                }
            }

            t_idevent.setText(id);
            t_idevent.setEditable(false);
        } catch (SQLException sQLException) {
            JOptionPane.showMessageDialog(null, "Error saat generate Id Kegiatan!");
            System.out.println(sQLException);
        }
    }

    public DefaultTableModel filterTable(Date tglAwal, Date tglAkhir) {
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("No");
        model.addColumn("ID Kegiatan");
        model.addColumn("Nama Kegiatan");
        model.addColumn("Tanggal Mulai");
        model.addColumn("Tanggal Selesai");
        model.addColumn("Lokasi");
        model.addColumn("Penguji");

        try {
            query = "SELECT * FROM kegiatan WHERE tgl_mulai BETWEEN ? AND ?";

            ps = con.prepareStatement(query);
            // Convert java.util.Date → java.sql.Date
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String tglAwalFormatted = sdf.format(tglAwal);
            String tglAkhirFormatted = sdf.format(tglAkhir);

            // Kirim parameter
            ps.setString(1, tglAwalFormatted);
            ps.setString(2, tglAkhirFormatted);
            rs = ps.executeQuery();

            int no = 1;
            while (rs.next()) {
                model.addRow(new Object[]{
                    no++,
                    rs.getString("ID_kegiatan"),
                    rs.getString("nama_kegiatan"),
                    rs.getString("tgl_mulai"),
                    rs.getString("tgl_selesai"),
                    rs.getString("lokasi"),
                    rs.getString("pelatih")
                });
            }
        } catch (SQLException e) {
            System.out.println(e);
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
        tData.getColumnModel().getColumn(6).setPreferredWidth(180); // Penguji

    }
}
