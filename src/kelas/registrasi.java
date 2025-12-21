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
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author HP
 */
public class registrasi extends koneksi {

    private String idDetailRegistrasi, namaRegistrasi, tglmulai, tglSelesai, lokasi, idRegitrasi, idAnggota, parentUjian;
    private final Connection con; //penggunaan FINAL membuat variabel koneksi hanya bisa diisi 1x 
    private PreparedStatement ps;
    private Statement st;
    private ResultSet rs;
    private String query;

    public registrasi() {
        con = super.configDB();
    }

    public String getIdDetailRegistrasi() {
        return idDetailRegistrasi;
    }

    public void setIdDetailRegistrasi(String idDetailRegistrasi) {
        this.idDetailRegistrasi = idDetailRegistrasi;
    }

    public String getNamaRegistrasi() {
        return namaRegistrasi;
    }

    public void setNamaRegistrasi(String namaRegistrasi) {
        this.namaRegistrasi = namaRegistrasi;
    }

    public String getTglmulai() {
        return tglmulai;
    }

    public void setTglmulai(String tglmulai) {
        this.tglmulai = tglmulai;
    }

    public String getTglSelesai() {
        return tglSelesai;
    }

    public void setTglSelesai(String tglSelesai) {
        this.tglSelesai = tglSelesai;
    }

    public String getLokasi() {
        return lokasi;
    }

    public void setLokasi(String lokasi) {
        this.lokasi = lokasi;
    }

    public String getIdRegitrasi() {
        return idRegitrasi;
    }

    public void setIdRegitrasi(String idRegitrasi) {
        this.idRegitrasi = idRegitrasi;
    }

    public String getIdAnggota() {
        return idAnggota;
    }

    public void setIdAnggota(String idAnggota) {
        this.idAnggota = idAnggota;
    }

    public String getParentUjian() {
        return parentUjian;
    }

    public void setParentUjian(String parentUjian) {
        this.parentUjian = parentUjian;
    }

    public void tambahRegis() {
        query = "INSERT INTO registrasi VALUES (?,?,?,?,?,?)";
        try {
            ps = con.prepareStatement(query);
            ps.setString(1, idRegitrasi);
            ps.setString(2, namaRegistrasi);
            ps.setString(3, tglmulai);
            ps.setString(4, tglSelesai);
            ps.setString(5, lokasi);
            ps.setString(6, parentUjian);
            ps.executeUpdate();
            ps.close();
            JOptionPane.showMessageDialog(null, "Data Berhasil Ditambahkan ");
        } catch (SQLException e) {
            System.out.println(e);
            JOptionPane.showMessageDialog(null, "Data Gagal Ditambahkan ");
        }
    }

    public void ubahRegis() {
        query = "UPDATE registrasi SET nama_registrasi=?, tgl_mulai=?, tgl_selesai=?, lokasi=?,"
                + " parent_ujian=? WHERE ID_registrasi=?";
        try {
            ps = con.prepareStatement(query);
            ps.setString(1, namaRegistrasi);
            ps.setString(2, tglmulai);
            ps.setString(3, tglSelesai);
            ps.setString(4, lokasi);
            ps.setString(5, parentUjian);
            ps.setString(6, idRegitrasi);
            ps.executeUpdate();
            ps.close();
            JOptionPane.showMessageDialog(null, "Data Berhasil Diubah ");
        } catch (SQLException e) {
            System.out.println(e);
            JOptionPane.showMessageDialog(null, "Data Gagal Diubah ");
        }
    }

    public void hapusRegis() {
        query = "DELETE FROM registrasi WHERE ID_registrasi = ?";
        try {
            ps = con.prepareStatement(query);
            ps.setString(1, idRegitrasi);

            ps.executeUpdate();
            ps.close();
            JOptionPane.showMessageDialog(null, "Data Berhasil Dihapus ");
        } catch (SQLException e) {
            System.out.println(e);
            JOptionPane.showMessageDialog(null, "Data Gagal Dihapus ");
        }
    }

    public boolean simpanDetailRegis() {
        query = "INSERT INTO detail_registrasi VALUES (?,?,?)";
        try {
            ps = con.prepareStatement(query);
            ps.setString(1, idDetailRegistrasi);
            ps.setString(2, idRegitrasi);
            ps.setString(3, idAnggota);

            ps.executeUpdate();
            ps.close();
            return true;
        } catch (SQLException e) {
            System.out.println(e);
            return false;
        }
    }

    @SuppressWarnings("unchecked")
    public void comboUjian(JComboBox cUjian) {
        try {
            query = "SELECT nama_kegiatan FROM kegiatan";
            st = con.createStatement();
            rs = st.executeQuery(query);

            while (rs.next()) {
                cUjian.addItem(rs.getString("nama_kegiatan"));

            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        cUjian.setSelectedItem(null);
    }

    public String konversIDRegis(String namaregis) {
        String idRegis = "";
        try {
            query = "SELECT ID_registrasi FROM registrasi WHERE nama_registrasi=?";
            ps = con.prepareStatement(query);
            ps.setString(1, namaregis);
            rs = ps.executeQuery();
            while (rs.next()) {
                idRegis = rs.getString("ID_registrasi");
            }
            ps.close();
        } catch (SQLException e) {
        }
        return idRegis;
    }

    public DefaultTableModel showRegistrasi() {
       DefaultTableModel model = new DefaultTableModel(){
        //nonaktif edit tabel
         @Override
         public boolean isCellEditable(int row, int column) {
            return false; // tabel tidak bisa diedit
        }
        };
        model.addColumn("No");
        model.addColumn("ID Kegiatan");
        model.addColumn("Nama Kegiatan");
        model.addColumn("Tanggal Mulai");
        model.addColumn("Tanggal Selesai");
        model.addColumn("Lokasi");
        model.addColumn("Induk Ujian");

        try {
            query = "SELECT r.ID_registrasi, r.nama_registrasi, r.tgl_mulai, r.tgl_selesai, "
                    + "r.lokasi, k.nama_kegiatan "
                    + "FROM registrasi r "
                    + "JOIN kegiatan k ON r.parent_ujian = k.ID_kegiatan";
            st = con.createStatement();
            rs = st.executeQuery(query);

            int no = 1;
            while (rs.next()) {
                model.addRow(new Object[]{
                    no++,
                    rs.getString("ID_registrasi"),
                    rs.getString("nama_registrasi"),
                    rs.getString("tgl_mulai"),
                    rs.getString("tgl_selesai"),
                    rs.getString("lokasi"),
                    rs.getString("nama_kegiatan")
                });
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return model;

    }

    //tampildata untuk button detail
    public DefaultTableModel tampilData(String idRegistrasi) {
       DefaultTableModel model = new DefaultTableModel(){
        //nonaktif edit tabel
         @Override
         public boolean isCellEditable(int row, int column) {
            return false; // tabel tidak bisa diedit
        }
        };
        model.addColumn("No");
        model.addColumn("ID Anggota");
        model.addColumn("Nama Anggota");
        model.addColumn("Status");

        try {
            query = "SELECT a.ID_anggota, a.nama_anggota, a.status "
                    + "FROM detail_registrasi d "
                    + "JOIN anggota a ON d.ID_anggota = a.ID_anggota "
                    + "JOIN registrasi r ON d.ID_registrasi = r.ID_registrasi "
                    + "WHERE r.ID_registrasi = ?";
            ps = con.prepareStatement(query);
            ps.setString(1, idRegistrasi);
            rs = ps.executeQuery();
            int no = 1;
            while (rs.next()) {
                String idAnggota = rs.getString("ID_anggota");
                String nama = rs.getString("nama_anggota");
                String status = rs.getString("status");

                model.addRow(new Object[]{no++, idAnggota, nama, status});
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return model;
    }

    //tampildataAkhir menampilkan data peserta ketika sudah melewati tgl
    public DefaultTableModel tampilDataAkhir(String idKegiatan, JLabel lbNamaKegiatan, JLabel lbTglMulai) {
       DefaultTableModel model = new DefaultTableModel(){
        //nonaktif edit tabel
         @Override
         public boolean isCellEditable(int row, int column) {
            return false; // tabel tidak bisa diedit
        }
        };
        model.addColumn("No");
        model.addColumn("ID Anggota");
        model.addColumn("Nama Anggota");
        model.addColumn("Status");

        try {
            query = "SELECT a.ID_anggota, a.nama_anggota, a.status,"
                    + " k.nama_kegiatan, k.tgl_mulai "
                    + "FROM detail_registrasi d "
                    + "JOIN anggota a ON d.ID_anggota = a.ID_anggota "
                    + "JOIN registrasi r ON d.ID_registrasi = r.ID_registrasi "
                    + "JOIN kegiatan k ON r.parent_ujian = k.ID_kegiatan "
                    + "WHERE k.ID_kegiatan = ?";
            ps = con.prepareStatement(query);
            ps.setString(1, idKegiatan);
            rs = ps.executeQuery();
            int no = 1;
            while (rs.next()) {
                String idAnggota = rs.getString("ID_anggota");
                String nama = rs.getString("nama_anggota");
                String status = rs.getString("status");
                String namaKegiatan = rs.getString("nama_kegiatan");
                String tglMulai = rs.getString("tgl_mulai");
                model.addRow(new Object[]{no++, idAnggota, nama, status});
                lbNamaKegiatan.setText(namaKegiatan);
                lbTglMulai.setText(tglMulai);
            }

        } catch (SQLException e) {
            System.out.println(e);
        }
        return model;
    }

    //tampilData untuk  menampilkan nama dan tgl mulai ujianlaporan akhir registrasi
    public void tampilKegiatan(String idRegistrasi, JLabel lblNamaEvent, JLabel lblTglMulai) {
        try {
            query = "SELECT k.nama_kegiatan, k.tgl_mulai FROM kegiatan k "
                    + " JOIN registrasi r ON r.parent_ujian = k.ID_kegiatan "
                    + " WHERE r.ID_registrasi=?";
            ps = con.prepareStatement(query);
            ps.setString(1, idRegistrasi);
            rs = ps.executeQuery();

            if (rs.next()) {
                lblNamaEvent.setText(rs.getString("nama_kegiatan"));
                lblTglMulai.setText(rs.getString("tgl_mulai"));
            } else {
                lblNamaEvent.setText("-");
                lblTglMulai.setText("-");
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    public DefaultTableModel filterTable(Date tglAwal, Date tglAkhir) {
        DefaultTableModel model = new DefaultTableModel(){
        //nonaktif edit tabel
         @Override
         public boolean isCellEditable(int row, int column) {
            return false; // tabel tidak bisa diedit
        }
        };
        model.addColumn("No");
        model.addColumn("ID Kegiatan");
        model.addColumn("Nama Kegiatan");
        model.addColumn("Tanggal Mulai");
        model.addColumn("Tanggal Selesai");
        model.addColumn("Lokasi");
        model.addColumn("Nama Ujian");

        try {
            query = "SELECT r.ID_registrasi, r.nama_registrasi, r.tgl_mulai, r.tgl_selesai, "
                    + "r.lokasi, k.nama_kegiatan "
                    + "FROM registrasi r "
                    + "JOIN kegiatan k ON r.parent_ujian = k.ID_kegiatan "
                    + "WHERE r.tgl_mulai BETWEEN ? AND ? ";

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
                    rs.getString("ID_registrasi"),
                    rs.getString("nama_registrasi"),
                    rs.getString("tgl_mulai"),
                    rs.getString("tgl_selesai"),
                    rs.getString("lokasi"),
                    rs.getString("nama_kegiatan")
                });
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return model;
    }
    
    public DefaultTableModel tabelAnggota() {
       DefaultTableModel model = new DefaultTableModel(){
        //nonaktif edit tabel
         @Override
         public boolean isCellEditable(int row, int column) {
            return false; // tabel tidak bisa diedit
        }
        };
        model.addColumn("No");
        model.addColumn("ID Anggota");
        model.addColumn("Nama Anggota");
        model.addColumn("Status");

        try {
            query = "SELECT * FROM anggota";

           
           Statement st = con.createStatement();
           ResultSet rs = st.executeQuery(query);
           int no = 1;
            while (rs.next()) {
                model.addRow(new Object[]{
                    no++,
                    rs.getString("ID_anggota"),
                    rs.getString("nama_anggota"),
                    rs.getString("status")
                });
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return model;
    }
    
    public DefaultTableModel filterTableAnggota(String namaAnggota) {
       DefaultTableModel model = new DefaultTableModel(){
        //nonaktif edit tabel
         @Override
         public boolean isCellEditable(int row, int column) {
            return false; // tabel tidak bisa diedit
        }
        };
        model.addColumn("No");
        model.addColumn("ID Anggota");
        model.addColumn("Nama Anggota");
        model.addColumn("Status");

        try {
            query = "SELECT * FROM anggota WHERE nama_anggota LIKE ?";

            ps = con.prepareStatement(query);
            ps.setString(1, "%" + namaAnggota + "%");

            rs = ps.executeQuery();

            int no = 1;
            while (rs.next()) {
                model.addRow(new Object[]{
                    no++,
                    rs.getString("ID_anggota"),
                    rs.getString("nama_anggota"),
                    rs.getString("status")
                });
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return model;
    }
    
    public void konversiIDAnggota(String namaAnggota, boolean status) {
        query = "SELECT * FROM anggota WHERE nama_anggota=?";
        try {
            ps = con.prepareStatement(query);
            ps.setString(1, namaAnggota);
            rs = ps.executeQuery();

            if (rs.next()) {
                String idAnggota = rs.getString("ID_anggota");
                setIdAnggota(idAnggota);
                boolean sukses = simpanDetailRegis();

                if (!sukses) {
                    status = false;
                }
            }
        } catch (SQLException e) {
        }
    }

    public void autoID(JTextField t_idregist) {
        try {
            String sql = "SELECT MAX(ID_registrasi) FROM registrasi";
            st = con.createStatement();
            rs = st.executeQuery(sql);

            String id = "REG001"; //default awal
            if (rs.next()) {
                String maxID = rs.getString(1);
                if (maxID != null) {
                    int num = Integer.parseInt(maxID.substring(3)); //ambil angka setelah "REG"
                    num++;
                    id = String.format("REG%03d", num); //format ulang jadi 3 digit
                }
            }

            t_idregist.setText(id);
            t_idregist.setEditable(false);
        } catch (SQLException sQLException) {
            JOptionPane.showMessageDialog(null, "Error saat generate Id Registrasi!");
            System.out.println(sQLException);
        }
    }

    public void autoIDDetail(JLabel lblIDDetail) {
        try {
            String sql = "SELECT MAX(ID_detailRegistrasi) FROM detail_registrasi";
            st = con.createStatement();
            rs = st.executeQuery(sql);

            String id = "DET001"; //default awal
            if (rs.next()) {
                String maxID = rs.getString(1);
                if (maxID != null) {
                    int num = Integer.parseInt(maxID.substring(3)); //ambil angka setelah "DET"
                    num++;
                    id = String.format("DET%03d", num); //format ulang jadi 3 digit
                }
            }

            lblIDDetail.setText(id);
        } catch (SQLException sQLException) {
            lblIDDetail.setText("");
            JOptionPane.showMessageDialog(null, "Error saat generate Id Registrasi!");
            System.out.println(sQLException);
        }
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
        tData.getColumnModel().getColumn(2).setPreferredWidth(150); // Nama Kegiatan
        tData.getColumnModel().getColumn(3).setPreferredWidth(100); // Tanggal Mulai
        tData.getColumnModel().getColumn(4).setPreferredWidth(100); // Tanggal Selesai
        tData.getColumnModel().getColumn(5).setPreferredWidth(100); // Lokasi
        tData.getColumnModel().getColumn(6).setPreferredWidth(250); // Keterangan
    }
    
     public void aturTableDataRegistrasi(JTable tData) {
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
        tData.getColumnModel().getColumn(1).setPreferredWidth(100); // ID Anggota
        tData.getColumnModel().getColumn(2).setPreferredWidth(250); // Nama Anggota
        tData.getColumnModel().getColumn(3).setPreferredWidth(100); // Status
}
     
    public void aturTableDataAnggota(JTable tData) {
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
        tData.getColumnModel().getColumn(1).setPreferredWidth(100); // ID Anggota
        tData.getColumnModel().getColumn(2).setPreferredWidth(250); // Nama Anggota
        tData.getColumnModel().getColumn(3).setPreferredWidth(100); // Status
}  
}
