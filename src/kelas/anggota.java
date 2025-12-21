/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package kelas;

import java.awt.HeadlessException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.swing.JOptionPane;
import java.sql.SQLException;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author naila
 */
public class anggota extends koneksi {

    String id_anggota, nama_anggota, jenis_kelamin, status, tgl_lahir, tgl_gabung, id_sabuk;
    private Connection conn;
    private PreparedStatement ps;
    private Statement st;
    private ResultSet rs;
    private String query;

    public anggota() {
        conn = super.configDB();
    }

    public String getId_anggota() {
        return id_anggota;
    }

    public void setId_anggota(String id_anggota) {
        this.id_anggota = id_anggota;
    }

    public String getNama_anggota() {
        return nama_anggota;
    }

    public void setNama_anggota(String nama_anggota) {
        this.nama_anggota = nama_anggota;
    }

    public String getJenis_kelamin() {
        return jenis_kelamin;
    }

    public void setJenis_kelamin(String jenis_kelamin) {
        this.jenis_kelamin = jenis_kelamin;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTgl_lahir() {
        return tgl_lahir;
    }

    public void setTgl_lahir(String tgl_lahir) {
        this.tgl_lahir = tgl_lahir;
    }

    public String getTgl_gabung() {
        return tgl_gabung;
    }

    public void setTgl_gabung(String tgl_gabung) {
        this.tgl_gabung = tgl_gabung;
    }

    public String getId_sabuk() {
        return id_sabuk;
    }

    public void setId_sabuk(String id_sabuk) {
        this.id_sabuk = id_sabuk;
    }

    public void autoID(JTextField t_idanggota) {
        try {
            query = "SELECT MAX(id_anggota) FROM anggota";
            st = conn.createStatement();
            rs = st.executeQuery(query);

            String id = "AG001";
            if (rs.next()) {
                String maxID = rs.getString(1);
                if (maxID != null) {
                    int no = Integer.parseInt(maxID.substring(3));
                    no++;
                    id = String.format("AG%03d", no);

                }
            }
            t_idanggota.setText(id);
            t_idanggota.setEditable(false);
        } catch (SQLException sQLException) {
            JOptionPane.showMessageDialog(null, "Eror saat generete Id Anggota");
            System.out.println(sQLException);
        }
    }

    public void ubahanggota() {
        query = "UPDATE anggota SET nama_anggota=?,tgl_lahir=?, jenis_kelamin=?, status=?, tgl_gabung=?, sabuk=?  WHERE id_anggota=?";

        try {
            ps = conn.prepareStatement(query);

            ps.setString(1, nama_anggota);
            ps.setString(2, tgl_lahir);
            ps.setString(3, jenis_kelamin);
            ps.setString(4, status);
            ps.setString(5, tgl_gabung);
            ps.setString(6, id_sabuk);
            ps.setString(7, id_anggota);
            ps.executeUpdate();
            JOptionPane.showMessageDialog(null, "Data berhasil diubah");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }

    }

    public void tambahanggota() {
        query = " INSERT INTO anggota (Id_anggota, nama_anggota, jenis_kelamin,tgl_lahir, status, tgl_gabung, sabuk) VALUES (?,?,?,?,?,?,?)";

        try {
            String jk = jenis_kelamin;
            if (jk.equalsIgnoreCase("Laki-laki")) {
                jk = "L";
            } else if (jk.equalsIgnoreCase("Perempuan")) {
                jk = "P";
            }
            ps = conn.prepareStatement(query);
            ps.setString(1, id_anggota);
            ps.setString(2, nama_anggota);
            ps.setString(3, jenis_kelamin);
            ps.setString(4, tgl_lahir);
            ps.setString(5, status);
            ps.setString(6, tgl_gabung);
            ps.setString(7, id_sabuk);
            ps.executeUpdate();
            JOptionPane.showMessageDialog(null, "Data berhasil ditambahkan");

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
            System.out.println(e);
        }
    }

    public void hapusanggota() {
        query = "DELETE FROM anggota WHERE id_anggota=?";

        try {
            ps = conn.prepareStatement(query);
            ps.setString(1, id_anggota);
            ps.executeUpdate();
            JOptionPane.showMessageDialog(null, "Data anggota berhasil dihapus");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());

        }
    }

    public DefaultTableModel showanggota() {
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("NO");
        model.addColumn("ID anggota");
        model.addColumn("Nama anggota");
        model.addColumn("Jenis_kelamin");
        model.addColumn("Tanggal Lahir");
        model.addColumn("Status");
        model.addColumn("Tanggal Gabung");
        model.addColumn("Sabuk");

        try {
            query = "SELECT a.*, s.nama_sabuk FROM anggota a "
                    + "LEFT JOIN sabuk s ON a.sabuk = s.ID_sabuk";

            st = conn.createStatement();
            rs = st.executeQuery(query);

            int no = 1;
            while (rs.next()) {
                Object[] row = {
                    no++,
                    rs.getString("ID_anggota"),
                    rs.getString("nama_anggota"),
                    rs.getString("jenis_kelamin"),
                    rs.getString("tgl_lahir"),
                    rs.getString("status"),
                    rs.getString("tgl_gabung"),
                    rs.getString("nama_sabuk")
                };
                model.addRow(row);
            }
        } catch (SQLException e) {
            System.out.println(e);

        }
        return model;
    }

    public DefaultTableModel filterTable(String nama) {
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("NO");
        model.addColumn("ID anggota");
        model.addColumn("Nama anggota");
        model.addColumn("Jenis_kelamin");
        model.addColumn("Tanggal Lahir");
        model.addColumn("Status");
        model.addColumn("Tanggal Gabung");
        model.addColumn("Sabuk");

        try {
            query = "SELECT a.*, s.nama_sabuk FROM anggota a "
                    + "LEFT JOIN sabuk s ON a.sabuk = s.ID_sabuk "
                    + "WHERE a.nama_anggota LIKE ?";

            ps = conn.prepareStatement(query);
            ps.setString(1, "%" + nama + "%");
            rs = ps.executeQuery();

            int no = 1;
            while (rs.next()) {
                Object[] row = {
                    no++,
                    rs.getString("ID_anggota"),
                    rs.getString("nama_anggota"),
                    rs.getString("jenis_kelamin"),
                    rs.getString("tgl_lahir"),
                    rs.getString("status"),
                    rs.getString("tgl_gabung"),
                    rs.getString("nama_sabuk")
                };
                model.addRow(row);
            }
        } catch (SQLException e) {
            System.out.println(e);

        }
        return model;
    }

}
