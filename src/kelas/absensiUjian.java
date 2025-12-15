/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package kelas;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.SQLException;
import java.sql.ResultSet;
import javax.swing.JOptionPane;
import javax.swing.JLabel;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author HP
 */
public class absensiUjian extends koneksi {

    String id_absensi, id_registrasi, tgl, id_peserta;
    int kehadiran;
    private final Connection con; //penggunaan FINAL membuat variabel koneksi hanya bisa diisi 1x 
    private PreparedStatement ps;
    private Statement st;
    private ResultSet rs;
    private String query;

    public absensiUjian() {
        con = super.configDB();
    }

    public String getId_absensi() {
        return id_absensi;
    }

    public void setId_absensi(String id_absensi) {
        this.id_absensi = id_absensi;
    }

    public String getId_registrasi() {
        return id_registrasi;
    }

    public void setId_registrasi(String id_registrasi) {
        this.id_registrasi = id_registrasi;
    }

    public String getTgl() {
        return tgl;
    }

    public void setTgl(String tgl) {
        this.tgl = tgl;
    }

    public String getId_peserta() {
        return id_peserta;
    }

    public void setId_peserta(String id_peserta) {
        this.id_peserta = id_peserta;
    }

    public int getKehadiran() {
        return kehadiran;
    }

    public void setKehadiran(int kehadiran) {
        this.kehadiran = kehadiran;
    }

    /*method simpanAbsensi bertipe BOOLEAN agar ketika menyimpan data yg banyak (anggotanya di simpan berkali")
    hanya menampilkan 1 kali informasi berhasil disimpan.
     */
    public boolean simpanAbsensi() {
        query = "INSERT INTO absensi_ujian VALUES (?,?,?,?,?)";
        try {
            ps = con.prepareStatement(query);
            ps.setString(1, id_absensi);
            ps.setString(2, id_registrasi);
            ps.setString(3, id_peserta);
            ps.setInt(4, kehadiran);
            ps.setString(5, tgl);

            ps.executeUpdate();
            ps.close();
            return true;
        } catch (SQLException e) {
            System.out.println(e);
            return false;
        }
    }

    public void autoID(JLabel idAbsen) {
        try {
            String sql = "SELECT MAX(ID_absensi_ujian) FROM absensi_ujian";
            st = con.createStatement();
            rs = st.executeQuery(sql);

            String id = "AB001"; //default awal
            if (rs.next()) {
                String maxID = rs.getString(1);
                if (maxID != null) {
                    int num = Integer.parseInt(maxID.substring(3)); //ambil angka setelah "KAT"
                    num++;
                    id = String.format("AB%03d", num); //format ulang jadi 3 digit
                }
            }

            idAbsen.setText(id);
        } catch (SQLException sQLException) {
            JOptionPane.showMessageDialog(null, "Error saat generate Id Absensi Ujian!");
            System.out.println(sQLException);
        }
    }

    public String getIDregistrasi(String idUjian) {
        String idRegis = "";
        try {
            query = "SELECT ID_registrasi FROM registrasi WHERE parent_ujian = ?";
            ps = con.prepareStatement(query);
            ps.setString(1, idUjian);
            rs = ps.executeQuery();

            if (rs.next()) {
                idRegis = rs.getString("ID_registrasi");
            }
            rs.close();
            ps.close();

        } catch (SQLException e) {
            System.out.println(e);
        }
        return idRegis;
    }

    public DefaultTableModel showPeserta(String idRegis) {
        DefaultTableModel model = new DefaultTableModel() {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                // Kolom keempat = checkbox (Boolean)
                if (columnIndex == 3) {
                    return Boolean.class;
                }
                return String.class;
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                // Hanya kolom kehadiran (checkbox) yang bisa diubah
                return column == 3;
            }
        };

        model.addColumn("No");
        model.addColumn("ID Peserta");
        model.addColumn("Nama Peserta");
        model.addColumn("Kehadiran");
        try {
            query = "SELECT a.ID_anggota, a.nama_anggota, a.status FROM detail_registrasi d "
                    + "JOIN anggota a ON d.ID_anggota = a.ID_anggota "
                    + "JOIN registrasi r ON d.ID_registrasi = r.ID_registrasi "
                    + "WHERE d.ID_registrasi= ?";
            ps = con.prepareStatement(query);
            ps.setString(1, idRegis);
            rs = ps.executeQuery();

            int no = 1;
            while (rs.next()) {
                model.addRow(new Object[]{
                    no++,
                    rs.getString("ID_anggota"),
                    rs.getString("nama_anggota"),
                    false
                });
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return model;
    }

    public DefaultTableModel searchNama(String kataKunci) {
        DefaultTableModel model = new DefaultTableModel() {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                // Kolom keempat = checkbox (Boolean)
                if (columnIndex == 3) {
                    return Boolean.class;
                }
                return String.class;
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                // Hanya kolom kehadiran (checkbox) yang bisa diubah
                return column == 3;
            }
        };

        model.addColumn("No");
        model.addColumn("ID Peserta");
        model.addColumn("Nama Peserta");
        model.addColumn("Kehadiran");
        try {
            query = "SELECT ID_anggota, nama_anggota FROM anggota WHERE nama_anggota "
                    + "LIKE ?";
            ps = con.prepareStatement(query);
            ps.setString(1, "%" + kataKunci + "%");
            rs = ps.executeQuery();

            int no = 1;
            while (rs.next()) {
                model.addRow(new Object[]{
                    no++,
                    rs.getString("ID_anggota"),
                    rs.getString("nama_anggota"),
                    false
                });
            }
        } catch (SQLException e) {
        }
        return model;
    }

}
