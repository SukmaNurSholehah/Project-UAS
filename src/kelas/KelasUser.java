/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package kelas;

import java.awt.Color;
import java.awt.Font;
import java.sql.Connection;
import java.sql.Statement;
import javax.swing.JOptionPane;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author USER
 */
public class KelasUser extends koneksi {

    private String username, full_name, password;
    private PreparedStatement ps;
    private Statement st;
    private ResultSet rs;
    private Connection cn;
    private String query;

    public KelasUser() {
        cn = super.configDB();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public DefaultTableModel loadtable() {
        DefaultTableModel model = new DefaultTableModel(){
        //nonaktif edit tabel
         @Override
         public boolean isCellEditable(int row, int column) {
            return false; // tabel tidak bisa diedit
        }
        };
        model.addColumn("NO");
        model.addColumn("Username");
        model.addColumn("Full Name");

        try {
            query = "SELECT * FROM user";

            st = cn.createStatement();
            rs = st.executeQuery(query);

            int no = 1;
            while (rs.next()) {
                model.addRow(new Object[]{
                    no++,
                    rs.getString("username"),
                    rs.getString("full_name")
                });
            }
            rs.close();
            st.close();
        } catch (SQLException e) {
            System.out.println("error loadtable: " + e.getMessage());
            JOptionPane.showMessageDialog(null, "error memuad data");
            e.printStackTrace();
        }
        return model;
    }

    public void ubahdata() {
        if (this.password.equals("")) {
            query = "UPDATE user SET full_name = ? WHERE username = ?";

            try {
                ps = cn.prepareStatement(query);
                ps.setString(1, full_name);
                ps.setString(2, username);
                ps.executeUpdate();
                ps.close();
                JOptionPane.showMessageDialog(null, "data user berhasil diubah.");
            } catch (SQLException sQLException) {
                JOptionPane.showMessageDialog(null, "error." + sQLException.getMessage());
            }
        } else {
            query = "UPDATE user SET full_name = ?, password = MD5(?) WHERE username = ?";

            try {
                ps = cn.prepareStatement(query);
                ps.setString(1, full_name);
                ps.setString(2, password);
                ps.setString(3, username);
                ps.executeUpdate();
                ps.close();
                JOptionPane.showMessageDialog(null, "data user berhasil diubah.");
            } catch (SQLException sQLException) {
                JOptionPane.showMessageDialog(null, "error." + sQLException.getMessage());

            }
        }
    }

    public void simpandata() {
        query = "INSERT INTO user (username, full_name, password) VALUES (?, ?, MD5(?))";

        try {
            ps = cn.prepareStatement(query);
            ps.setString(1, username);
            ps.setString(2, full_name);
            ps.setString(3, password);
            ps.executeUpdate();
            ps.close();
            JOptionPane.showMessageDialog(null, "data user berhasil ditambah.");
        } catch (SQLException sQLException) {
            JOptionPane.showMessageDialog(null, "data user gagal ditambah. error" + sQLException.getMessage());

        }
    }

    public void hapusdata() {
        query = "DELETE FROM user WHERE username = ?";

        try {
            ps = cn.prepareStatement(query);
            ps.setString(1, username);
            ps.executeUpdate();
            ps.close();
            JOptionPane.showMessageDialog(null, "data user berhasil dihapus.");
        } catch (SQLException sQLException) {
            JOptionPane.showMessageDialog(null, "error saat menghapus data" + sQLException.getMessage());

        }
    }
    
    public boolean login(String username, String password) {
    try {
        String sql = "SELECT * FROM user WHERE username=? AND password=MD5(?)";
        
        koneksi k = new koneksi();
        Connection con = k.configDB(); // ⬅ BENAR

        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, username);
        ps.setString(2, password);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            sesion.setUsername(rs.getString("username"));
            sesion.setFullName(rs.getString("full_name"));
            return true;
        }
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, e.getMessage());
    }
    return false;
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
        tData.getColumnModel().getColumn(1).setPreferredWidth(150); // Username
        tData.getColumnModel().getColumn(2).setPreferredWidth(250); // Full Name
}
}
