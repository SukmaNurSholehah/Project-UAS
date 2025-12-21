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
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author This PC
 */
public class prestasi extends koneksi{

    String ID_prestasi, peringkat, tingkat, tgl, ID_anggota;
    private final Connection con;
    private PreparedStatement ps;
    private Statement st;
    private ResultSet rs;
    private String query;
    public prestasi(){
    con = super.configDB();
    }
    
    public String getID_prestasi() {
        return ID_prestasi;
    }
    
    public void setID_prestasi(String ID_prestasi) {
        this.ID_prestasi = ID_prestasi;
    }
    
    public String getPeringkat() {
        return peringkat;
    }
    
    public void setPeringkat(String peringkat) {
        this.peringkat = peringkat;
    }
    
    public String getTingkat() {
        return tingkat;
    }
    
    public void setTingkat(String tingkat) {
        this.tingkat = tingkat;
    }
    
    public String getTgl() {
        return tgl;
    }
    
    public void setTgl(String tgl) {
        this.tgl = tgl;
    }
    
    public String getID_anggota() {
        return ID_anggota;
    }
    
    public void setID_anggota(String ID_anggota) {
        this.ID_anggota = ID_anggota;
    }
    
    public void tambah_prestasi() {
        query = " INSERT INTO prestasi VALUES (?,?,?,?,?)";
        try {
            ps = con.prepareStatement(query);
            ps.setString(1, ID_prestasi);
            ps.setString(2, peringkat);
            ps.setString(3, tingkat);
            ps.setString(4, tgl);
            ps.setString(5, ID_anggota);
            
            ps.execute();
            ps.close();
            JOptionPane.showMessageDialog(null, "Data Berhasil Ditambahkan");
        } catch (SQLException e) {
            System.out.println(e);
            JOptionPane.showMessageDialog(null, "Data Gagal Ditambahkan");
            
        }
        
    }

    public void ubah_prestasi() {
        query = "UPDATE prestasi SET peringkat=?, tingkat=?, tgl=?, ID_anggota=? WHERE ID_prestasi=?";
        try {
            ps = con.prepareStatement(query);
            ps.setString(1, peringkat);
            ps.setString(2, tingkat);
            ps.setString(3, tgl);
            ps.setString(4, ID_anggota);
            ps.setString(5, ID_prestasi);
            
            ps.execute();
            ps.close();
            JOptionPane.showMessageDialog(null, "Data Berhasil Diubah");
        } catch (SQLException e) {
            System.out.println(e);
            JOptionPane.showMessageDialog(null, "Data Gagal Diubah");
            
        }
        
    }

    public void hapus_prestasi() {
        query = "DELETE FROM prestasi WHERE ID_prestasi = ?";
        try {
            ps = con.prepareStatement(query);
            ps.setString(1, ID_prestasi);
            
            ps.execute();
            ps.close();
            JOptionPane.showMessageDialog(null, "Data Berhasil Dihapus");
        } catch (SQLException e) {
            System.out.println(e);
            JOptionPane.showMessageDialog(null, "Data Gagal Dihapus");
            
        }
    }
    
    public DefaultTableModel showprestasi() {
        DefaultTableModel model = new DefaultTableModel(){
        //nonaktif edit tabel
         @Override
         public boolean isCellEditable(int row, int column) {
            return false; // tabel tidak bisa diedit
        }
        };
        model.addColumn("No");
        model.addColumn("ID prestasi");
        model.addColumn("Peringkat");
        model.addColumn("Tingkat");
        model.addColumn("Tanggal");
        model.addColumn("Nama Anggota");
        
        try {
            query = "SELECT p.ID_prestasi, p.peringkat, p.tingkat, p.tgl, a.nama_anggota "
                    + " FROM prestasi p "
                    + "JOIN anggota a ON p.ID_anggota = a.ID_anggota ";
            st = con.createStatement();
            rs = st.executeQuery(query);
            
            int no = 1;
            while (rs.next()) {
                model.addRow(new Object[]{
                    no++,
                    rs.getString("ID_prestasi"),
                    rs.getString("peringkat"),
                    rs.getString("tingkat"),
                    rs.getString("tgl"),
                    rs.getString("nama_anggota")
                });
            }
            
        } catch (SQLException e) {
            System.out.println(e);
            JOptionPane.showMessageDialog(null, "Data Gagal Ditampilkan!");
        }
        
        return model;
        
    }
    
    public DefaultTableModel filterTablePrestasi(String namaAnggota) {
        DefaultTableModel model = new DefaultTableModel(){
        //nonaktif edit tabel
         @Override
         public boolean isCellEditable(int row, int column) {
            return false; // tabel tidak bisa diedit
        }
        };
        model.addColumn("No");
        model.addColumn("ID prestasi");
        model.addColumn("Peringkat");
        model.addColumn("Tingkat");
        model.addColumn("Tanggal");
        model.addColumn("Nama Anggota");
        
        try {
            query = "SELECT p.ID_prestasi, p.peringkat, p.tingkat, p.tgl, a.nama_anggota "
                    + " FROM prestasi p "
                    + "JOIN anggota a ON p.ID_anggota = a.ID_anggota "
                    + "WHERE a.nama_anggota LIKE ?";
            ps = con.prepareStatement(query);
            ps.setString(1, "%" + namaAnggota + "%");
            rs = ps.executeQuery();
            
            int no = 1;
            while (rs.next()) {
                model.addRow(new Object[]{
                    no++,
                    rs.getString("ID_prestasi"),
                    rs.getString("peringkat"),
                    rs.getString("tingkat"),
                    rs.getString("tgl"),
                    rs.getString("nama_anggota")
                });
            }
            
        } catch (SQLException e) {
            System.out.println(e);
            JOptionPane.showMessageDialog(null, "Data Gagal Ditampilkan!");
        }
        
        return model;
        
    }
    
    @SuppressWarnings("unchecked")
    public void comboanggota(JComboBox canggota) {
        try {
            query = "SELECT nama_anggota FROM anggota";
            st = con.createStatement();
            rs = st.executeQuery(query);
            
            while (rs.next()) {
                canggota.addItem(rs.getString("nama_anggota"));
            }
        } catch (SQLException e) {
            System.out.println(e);
            e.printStackTrace();
        }
        canggota.setSelectedItem(null);        
    }
    
    public String konversID_anggota(String namaanggota) {
        String idAnggota = "";
        try {
            query = "SELECT ID_anggota FROM anggota WHERE nama_anggota=?";
            ps = con.prepareStatement(query);
            ps.setString(1, namaanggota);
            rs = ps.executeQuery();
            while (rs.next()) {
                idAnggota = rs.getString("ID_anggota");
            }
            ps.close();
        } catch (SQLException e) {
        }
        return idAnggota;
    }
    
    public void autoID(JTextField t_idprestasi) {
        try {
            query = "SELECT MAX(ID_prestasi) FROM prestasi";
             st = con.createStatement();
             rs = st.executeQuery(query);
            
            String id = "PRES001";
            if (rs.next()) {
                String MaxID = rs.getString(1);
                if (MaxID != null) {
                    int num = Integer.parseInt(MaxID.substring(4));
                    num++;
                    id = String.format("PRES%03d", num);
                }
            }
            
            t_idprestasi.setText(id);            
            t_idprestasi.setEditable(false);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error saat generate Id anggota");
            System.out.println(e);
            e.printStackTrace();
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
        tData.getColumnModel().getColumn(1).setPreferredWidth(100); // ID Prestasi
        tData.getColumnModel().getColumn(2).setPreferredWidth(100); // Peringkat
        tData.getColumnModel().getColumn(3).setPreferredWidth(100); // Tingkat
        tData.getColumnModel().getColumn(4).setPreferredWidth(100); // Tanggal 
        tData.getColumnModel().getColumn(5).setPreferredWidth(250); // Nama Anggota

    }
    
    
}
