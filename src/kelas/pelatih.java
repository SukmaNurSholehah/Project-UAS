/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package kelas;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.io.File;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.ImageIcon;
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
public class pelatih extends koneksi {

    String idPelatih, namaPelatih, noHp, idSabuk, sertifikat;
    private final Connection con; //penggunaan FINAL membuat variabel koneksi hanya bisa diisi 1x 
    private PreparedStatement ps;
    private Statement st;
    private ResultSet rs;
    private String query;

    public pelatih() {
        con = super.configDB();
    }

    public String getIdPelatih() {
        return idPelatih;
    }

    public void setIdPelatih(String idPelatih) {
        this.idPelatih = idPelatih;
    }

    public String getNamaPelatih() {
        return namaPelatih;
    }

    public void setNamaPelatih(String namaPelatih) {
        this.namaPelatih = namaPelatih;
    }

    public String getNoHp() {
        return noHp;
    }

    public void setNoHp(String noHp) {
        this.noHp = noHp;
    }

    public String getIdSabuk() {
        return idSabuk;
    }

    public void setIdSabuk(String idSabuk) {
        this.idSabuk = idSabuk;
    }

    public String getSertifikat() {
        return sertifikat;
    }

    public void setSertifikat(String sertifikat) {
        this.sertifikat = sertifikat;
    }

    public void addPelatih() {
        query = "INSERT INTO pelatih VALUES (?,?,?,?,?)";
        try {
            ps = con.prepareStatement(query);
            ps.setString(1, idPelatih);
            ps.setString(2, namaPelatih);
            ps.setString(3, noHp);
            ps.setString(4, idSabuk);
            ps.setString(5, sertifikat);
            ps.executeUpdate();
            ps.close();
            JOptionPane.showMessageDialog(null, "Data Berhasil Disimpan!");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Data Gagal Disimpan!");
            System.out.println(e);
        }
    }

    public void updatePelatih() {
        query = "UPDATE pelatih SET nama_pelatih=?, no_hp=?, ID_sabuk=?, sertifikat=? "
                + "WHERE ID_pelatih =?";
        try {
            ps = con.prepareStatement(query);
            ps.setString(1, namaPelatih);
            ps.setString(2, noHp);
            ps.setString(3, idSabuk);
            ps.setString(4, sertifikat);
            ps.setString(5, idPelatih);
            ps.executeUpdate();
            ps.close();
            JOptionPane.showMessageDialog(null, "Data Berhasil Diubah!");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Data Gagal Diubah!");
            System.out.println(e);
        }
    }

    public void deletePelatih() {
        query = "DELETE FROM pelatih WHERE ID_pelatih =?";
        try {
            ps = con.prepareStatement(query);
            ps.setString(1, idPelatih);
            ps.executeUpdate();
            ps.close();
            JOptionPane.showMessageDialog(null, "Data Berhasil Dihapus!");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Data Gagal Dihapus!");
            System.out.println(e);
        }
    }

    public DefaultTableModel showPelatih() {
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("No");
        model.addColumn("ID Pelatih");
        model.addColumn("Nama Pelatih");
        model.addColumn("No Handphone");
        model.addColumn("Sabuk");
        model.addColumn("Sertifikat");

        try {
            query = "SELECT p.ID_pelatih, p.nama_pelatih,p.no_hp, s.nama_sabuk, p.sertifikat "
                    + "FROM pelatih p JOIN sabuk s "
                    + "ON p.ID_sabuk = s.ID_sabuk";
            st = con.createStatement();
            rs = st.executeQuery(query);

            int no = 1;
            while (rs.next()) {
                model.addRow(new Object[]{
                    no++,
                    rs.getString("ID_pelatih"),
                    rs.getString("nama_pelatih"),
                    rs.getString("no_hp"),
                    rs.getString("nama_sabuk"),
                    rs.getString("sertifikat")
                });
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return model;
    }

    public void comboSabuk(JComboBox cSabuk) {
        cSabuk.removeAllItems();
        try {
            query = "SELECT nama_sabuk FROM sabuk";
            st = con.createStatement();
            rs = st.executeQuery(query);

            while (rs.next()) {
                
                cSabuk.addItem(rs.getString("nama_sabuk"));

            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        cSabuk.setSelectedIndex(0);
    }

    public String konversIDSabuk(String namaSabuk) {
        String idSabuk = "";
        try {
            query = "SELECT ID_sabuk FROM sabuk WHERE nama_sabuk=?";
            ps = con.prepareStatement(query);
            ps.setString(1, namaSabuk);
            rs = ps.executeQuery();
            while (rs.next()) {
                idSabuk = rs.getString("ID_sabuk");
            }
            ps.close();
        } catch (SQLException e) {
        }
        return idSabuk;
    }

    public void autoID(JTextField tIdPelatih) {
        try {
            query = "SELECT MAX(ID_pelatih) FROM pelatih";
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(query);

            String id = "PLT001"; //default awal
            if (rs.next()) {
                String maxID = rs.getString(1);
                if (maxID != null) {
                    int num = Integer.parseInt(maxID.substring(3)); //ambil angka setelah "KAT"
                    num++;
                    id = String.format("PLT%03d", num); //format ulang jadi 3 digit
                }
            }

            tIdPelatih.setText(id);
            tIdPelatih.setEditable(false);
        } catch (SQLException sQLException) {
            JOptionPane.showMessageDialog(null, "Error saat generate Id Pelatih!");
            System.out.println(sQLException);
        }
    }

    public DefaultTableModel filterTable(String namaPelatih) {
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("No");
        model.addColumn("ID Pelatih");
        model.addColumn("Nama Pelatih");
        model.addColumn("No Handphone");
        model.addColumn("Sabuk");
        model.addColumn("Sertifikat");

        try {
            query = "SELECT p.ID_pelatih, p.nama_pelatih, p.no_hp, s.nama_sabuk, p.sertifikat "
                    + "FROM pelatih p JOIN sabuk s "
                    + "ON p.ID_sabuk = s.ID_sabuk WHERE p.nama_pelatih LIKE?";

            ps = con.prepareStatement(query);

            // Kirim parameter
            ps.setString(1,"%"+ namaPelatih+"%");
            rs = ps.executeQuery();

            int no = 1;
            while (rs.next()) {
                model.addRow(new Object[]{
                    no++,
                    rs.getString("ID_pelatih"),
                    rs.getString("nama_pelatih"),
                    rs.getString("no_hp"),
                    rs.getString("nama_sabuk"),
                    rs.getString("sertifikat")
                });
            }
        } catch (SQLException e) {
            System.out.println(e);
        }

        return model;
    }

    public void showSertif(String idPelatih, JLabel sertif, JLabel pathSertif) {
        try {
            query = "SELECT sertifikat FROM pelatih WHERE ID_pelatih=?";
            ps = con.prepareStatement(query);
            ps.setString(1, idPelatih);
            rs = ps.executeQuery();
            if (rs.next()) {
                // Mengambil sertifikat dari hasil query
                String foto = rs.getString("sertifikat");

                // Jika path foto tidak kosong, tampilkan gambar ke label foto
                if (foto != null && !foto.isEmpty()) {
                    ImageIcon icon = new ImageIcon(foto);
                    Image image = icon.getImage().getScaledInstance(sertif.getWidth(), sertif.getHeight(), Image.SCALE_SMOOTH);
                    pathSertif.setText(foto);
                    sertif.setText(null);
                    sertif.setIcon(new ImageIcon(image));
                } else {
                    // Jika tidak ada foto, set teks "Foto" dan hapus icon
                    sertif.setText("Sertifikat");
                    sertif.setIcon(null);
                }
            }
        } catch (SQLException e) {
            // Menampilkan error ke konsol jika terjadi kesalahan SQL
            System.err.println(e.getMessage());
            System.out.println(e);
        }
    }

    public String editPathSertif(String IDPelatih, String newPath) {
        String sertifAsli = null;

        try {
            String sql = "SELECT sertifikat FROM pelatih WHERE ID_pelatih = ?";
            ps = con.prepareStatement(sql);
            ps.setString(1, IDPelatih);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                sertifAsli = rs.getString("sertifikat");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Gagal mengambil foto asli: " + e.getMessage());
            return sertifAsli;
        }

        // Jika path tidak berubah
        if (sertifAsli != null && sertifAsli.equals(newPath)) {
            return sertifAsli;
        }

        // Jika user tidak memilih file
        File sourceFile = new File(newPath);
        if (!sourceFile.exists()) {
            return sertifAsli;
        }

        try {
            // Buat folder jika belum ada
            new File("src/foto").mkdirs();

            String ext = newPath.substring(newPath.lastIndexOf('.') + 1);
            String timestamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
            String destPath = "src/foto/foto-" + timestamp + "." + ext;

            // Copy file baru
            Files.copy(sourceFile.toPath(), new File(destPath).toPath());

            // Hapus file lama jika ada
            if (sertifAsli != null) {
                File lama = new File(sertifAsli);
                if (lama.exists()) {
                    lama.delete();
                }
            }

            return destPath;

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Gagal upload file: " + e.getMessage());
            return sertifAsli;
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
        tData.getColumnModel().getColumn(1).setPreferredWidth(100); // ID Pelatih
        tData.getColumnModel().getColumn(2).setPreferredWidth(200); // Nama Pelatih
        tData.getColumnModel().getColumn(3).setPreferredWidth(100); // No Hp
        tData.getColumnModel().getColumn(4).setPreferredWidth(100); // Sabuk
        tData.getColumnModel().getColumn(5).setPreferredWidth(100); // Path Sertifikat

    }

}
