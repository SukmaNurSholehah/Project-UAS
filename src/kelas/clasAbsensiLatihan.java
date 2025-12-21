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
 * @author ASUS
 */
public class clasAbsensiLatihan extends koneksi{
    String idAbsensiLatihan, idAnggota, idJadwalLatihan, tgl;
    int kehadiran;
    private final Connection con;  
    private PreparedStatement ps;
    private Statement st;
    private ResultSet rs;
    private String query;

     public clasAbsensiLatihan() {
        con = super.configDB();
     }
     
    public String getIdAbsensiLatihan() {
        return idAbsensiLatihan;
    }

    public void setIdAbsensiLatihan(String idAbsensiLatihan) {
        this.idAbsensiLatihan = idAbsensiLatihan;
    }

    public String getIdAnggota() {
        return idAnggota;
    }

    public void setIdAnggota(String idAnggota) {
        this.idAnggota = idAnggota;
    }

    public String getIdJadwalLatihan() {
        return idJadwalLatihan;
    }

    public void setIdJadwalLatihan(String idJadwalLatihan) {
        this.idJadwalLatihan = idJadwalLatihan;
    }

    public Integer getKehadiran() {
        return kehadiran;
    }

    public void setKehadiran(int kehadiran) {
        this.kehadiran = kehadiran;
    }

    public String getTgl() {
        return tgl;
    }

    public void setTgl(String tgl) {
        this.tgl = tgl;
    }
  
    public boolean simpanAbsensi() {
        query = "INSERT INTO absensi_latihan VALUES (?,?,?,?,?)";
        try {
            ps = con.prepareStatement(query);
            ps.setString(1, idAbsensiLatihan);
            ps.setString(2, idAnggota);
            ps.setString(3, idJadwalLatihan);
            ps.setInt(4, kehadiran);
            ps.setString(5, tgl);

            ps.executeUpdate();
            ps.close();
            return  true;
        } catch (SQLException e) {
            System.out.println(e);
            return false;
        }
    }
    
    public void autoID(JLabel idAbsen) {
        try {
            query = "SELECT MAX(ID_absensi_latihan) FROM absensi_latihan";
             
             st = con.createStatement();
             rs = st.executeQuery(query);

            String id = "AL001"; 
            if (rs.next()) {
                String maxID = rs.getString(1);
                if (maxID != null) {
                    int num = Integer.parseInt(maxID.substring(3)); 
                    num++;
                    id = String.format("AL%03d", num); 
                }
            }

            idAbsen.setText(id);
        } catch (SQLException sQLException) {
            JOptionPane.showMessageDialog(null, "Error saat generate Id Absensi Latihan!");
            System.out.println(sQLException);
        }
    }
    
    public String getIDJadwalLatihan(String lokasiLatihan) {
        String idJadwalLatihan = "";
        try {
            query = "SELECT ID_jadwal FROM jadwal_latihan WHERE lokasi = ?";
            ps = con.prepareStatement(query);
            ps.setString(1, lokasiLatihan);
            rs = ps.executeQuery();

            if (rs.next()) {
                idJadwalLatihan = rs.getString("ID_jadwal");
            }
            rs.close();
            ps.close();

        } catch (SQLException e) {
            System.out.println(e);
        }
        return idJadwalLatihan;
    }
    
    public DefaultTableModel showPeserta() {
        DefaultTableModel model = new DefaultTableModel() {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 4) {
                    return Boolean.class; 
                }
                return String.class;
            }

            @Override
        public boolean isCellEditable(int row, int column) {
            return column == 4;
        }
    };

    model.addColumn("No");
    model.addColumn("ID Anggota");
    model.addColumn("Nama Anggota");
    model.addColumn("Status");
    model.addColumn("Kehadiran");

    try {
        query = "SELECT ID_anggota, nama_anggota, status FROM anggota";
        st = con.createStatement();
        rs = st.executeQuery(query);
        int no = 1;
        while (rs.next()) {
            model.addRow(new Object[]{
                no++,
                rs.getString("ID_anggota"),
                rs.getString("nama_anggota"),
                rs.getString("status"), 
                false
            });
        }
    } catch (SQLException e) {
        System.out.println("Error showPeserta : " + e.getMessage());
    }

    return model;
}

    
    public DefaultTableModel searchNama(String kataKunci){
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("No");
        model.addColumn("ID Peserta");
        model.addColumn("Nama Peserta");
        model.addColumn("Status");
        try {
            query = "SELECT ID_anggota, nama_anggota, status FROM anggota WHERE nama_anggota "
                    + "LIKE ?";
            ps = con.prepareStatement(query);
            ps.setString(1, "%"+kataKunci+"%");
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
        }
        return model;
    }
}

    

