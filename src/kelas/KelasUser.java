/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package kelas;

import java.sql.Connection;
import java.awt.HeadlessException;
import java.sql.Statement;
import javax.swing.JOptionPane;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.swing.JTextField;
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
        DefaultTableModel model = new DefaultTableModel();
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
        query = "INSERT INTO user VALUES (?, ?, ?)";

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
}
