/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package kelas;

import java.sql.*;
import javax.swing.JComboBox;
import javax.swing.table.DefaultTableModel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

/**
 *
 * @author naila
 */
public class Sabuk extends koneksi {

    String idSabuk, namaSabuk, tingkatan;

    private Connection conn;
    private PreparedStatement ps;
    private Statement st;
    private ResultSet rs;
    private String query;

    public String getIdSabuk() {
        return idSabuk;
    }

    public void setIdSabuk(String idSabuk) {
        this.idSabuk = idSabuk;
    }

    public String getNamaSabuk() {
        return namaSabuk;
    }

    public void setNamaSabuk(String namaSabuk) {
        this.namaSabuk = namaSabuk;
    }

    public String getTingkatan() {
        return tingkatan;
    }

    public void setTingkatan(String tingkatan) {
        this.tingkatan = tingkatan;
    }
    
    public Sabuk(){
        conn = super.configDB(); 
    }

    public void autoID(JTextField t_idSabuk) {
       try {
            query = "SELECT MAX(ID_sabuk) FROM sabuk";
             st = conn.createStatement();
             rs = st.executeQuery(query);

            String id = "SB001"; //default awal
            if (rs.next()) {
                String lastID = rs.getString(1);
                if (lastID != null) {
                    int num = Integer.parseInt(lastID.substring(2)); //SB001-> 001
                    num++;
                    id = String.format("SB%03d", num); //format ulang jadi 3 digit
                }
            }
            t_idSabuk.setText(id);
            t_idSabuk.setEditable(false);
        } catch (SQLException sQLException) {
            JOptionPane.showMessageDialog(null, "Error saat generate Id Kegiatan!");
            System.out.println(sQLException);
        }
    }

    
    public void tambah() {
        try {
            query = "INSERT INTO sabuk (ID_sabuk, nama_sabuk, tingkatan_sabuk) VALUES (?,?,?)";
            ps = conn.prepareStatement(query);
            ps.setString(1, idSabuk);
            ps.setString(2, namaSabuk);
            ps.setString(3, tingkatan);
            ps.executeUpdate();
            JOptionPane.showMessageDialog(null, "Data sabuk berhasil ditambahkan");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    
     public void ubah() {
        try {
            query = "UPDATE sabuk SET nama_sabuk=?,tingkatan_sabuk=? WHERE ID_sabuk=?";
            ps = conn.prepareStatement(query);
            ps.setString(1, tingkatan);
            ps.setString(2, namaSabuk);
            ps.setString(3, idSabuk);
            ps.executeUpdate();
            JOptionPane.showMessageDialog(null, "Data sabuk berhasil diubah");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

     
      public void hapus() {
        try {
            query = "DELETE FROM sabuk WHERE id_sabuk=?";
            ps = conn.prepareStatement(query);
            ps.setString(1, idSabuk);
            ps.executeUpdate();
            JOptionPane.showMessageDialog(null, "Data sabuk berhasil dihapus");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }
      
       public DefaultTableModel tampil() {
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("NO");
        model.addColumn("ID Sabuk");
        model.addColumn("Nama Sabuk");
        model.addColumn("Tingkatan Sabuk");
        
        try {
            query = "SELECT * FROM sabuk";
            st = conn.createStatement();
            rs = st.executeQuery(query);
            
            int no = 1;
            while (rs.next()) {
                model.addRow(new Object[]{
                no++,
                rs.getString("ID_sabuk"),
                rs.getString("nama_sabuk"),
                rs.getString("tingkatan_sabuk")
                });
            }
        } catch (SQLException e) {
            System.out.println(e);
            e.printStackTrace();
        }
        return model;
    }
       
    public void comboSabuk(JComboBox cSabuk) {
        try {
            query = "SELECT nama_sabuk FROM sabuk";
            st = conn.createStatement();
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
            ps = conn.prepareStatement(query);
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
    
}
