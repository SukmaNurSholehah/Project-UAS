/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package kelas;
import java.time.LocalDate;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.SQLException;
import java.sql.Date;
import java.sql.ResultSet;
import javax.swing.JOptionPane;

/**
 *
 * @author Sukma Nur
 */
public class dashboard extends koneksi{
    private int jumlahAnggota;
    private Date tglLatihan, tglMulaiUjian, tglSelesaiUjian;
    private String lokasiLatihan, keteranganLatihan, namaUjian;
    
    
    
    private final Connection koneksi ;
    private PreparedStatement ps;
    private Statement st;
    private ResultSet rs;
    private String query;
    
    public dashboard(){
        koneksi = super.configDB();
    }

    public int getJumlahAnggota() {
        return jumlahAnggota;
    }

    public void setJumlahAnggota(int jumlahAnggota) {
        this.jumlahAnggota = jumlahAnggota;
    }

    public Date getTglLatihan() {
        return tglLatihan;
    }

    public void setTglLatihan(Date tglLatihan) {
        this.tglLatihan = tglLatihan;
    }

    public Date getTglMulaiUjian() {
        return tglMulaiUjian;
    }

    public void setTglMulaiUjian(Date tglMulaiUjian) {
        this.tglMulaiUjian = tglMulaiUjian;
    }

    public Date getTglSelesaiUjian() {
        return tglSelesaiUjian;
    }

    public void setTglSelesaiUjian(Date tglSelesaiUjian) {
        this.tglSelesaiUjian = tglSelesaiUjian;
    }

    public String getLokasiLatihan() {
        return lokasiLatihan;
    }

    public void setLokasiLatihan(String lokasiLatihan) {
        this.lokasiLatihan = lokasiLatihan;
    }

    public String getKeteranganLatihan() {
        return keteranganLatihan;
    }

    public void setKeteranganLatihan(String keteranganLatihan) {
        this.keteranganLatihan = keteranganLatihan;
    }

    public String getNamaUjian() {
        return namaUjian;
    }

    public void setNamaUjian(String namaUjian) {
        this.namaUjian = namaUjian;
    }
    
    public void loadJumlahAnggota() {
        try {
            query = "SELECT COUNT(*) FROM anggota";
            ps = koneksi.prepareStatement(query);
            rs = ps.executeQuery();
            if (rs.next()) {
                setJumlahAnggota(rs.getInt(1));
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Gagal memuat jumlah anggota");
        }
    }
    
    public void loadJadwalLatihan() {
        try {
            query = "SELECT tgl, keterangan "
                    + "FROM jadwal_latihan WHERE tgl >= CURDATE() "
                    + "ORDER BY tgl ASC LIMIT 1";

            ps = koneksi.prepareStatement(query);
            rs = ps.executeQuery();

            if (rs.next()) {
                setTglLatihan(rs.getDate("tgl"));
                setKeteranganLatihan(rs.getString("keterangan"));
            } else {
                setTglLatihan(null);
                setKeteranganLatihan("Tidak ada jadwal latihan");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
            e.printStackTrace();
        }
    }
    
    public void loadUjian() {
        try {
            query = "SELECT nama_kegiatan, tgl_mulai, tgl_selesai"
                    + " FROM kegiatan WHERE tgl_selesai >= CURDATE()"
                    + " ORDER BY tgl_mulai ASC LIMIT 1";

            ps = koneksi.prepareStatement(query);
            rs = ps.executeQuery();

            if (rs.next()) {
                setNamaUjian(rs.getString("nama_kegiatan"));
                setTglMulaiUjian(rs.getDate("tgl_mulai"));
                setTglSelesaiUjian(rs.getDate("tgl_selesai"));
                
            } else {
                setNamaUjian("Tidak ada ujian");
                setTglMulaiUjian(null);
                setTglSelesaiUjian(null);
                
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Gagal memuat data ujian");
        }
    
    }
    
    public String getStatusLatihan() {
        if (tglLatihan == null) {
            return "Tidak ada jadwal";
        }

        LocalDate today = LocalDate.now();
        LocalDate tgl = tglLatihan.toLocalDate();

        if (tgl.isAfter(today)) {
            return "Akan Datang";
        } else if (tgl.isEqual(today)) {
            return "Sedang Berlangsung";
        } else {
            return "Selesai";
        }
    }
    
    public String getStatusUjian() {
        if (tglMulaiUjian == null || tglSelesaiUjian == null) {
            return "Tidak ada kegiatan";
        }

        LocalDate today = LocalDate.now();
        LocalDate mulai = tglMulaiUjian.toLocalDate();
        LocalDate selesai = tglSelesaiUjian.toLocalDate();

        if (today.isBefore(mulai)) {
            return "Akan Datang";
        } else if (!today.isAfter(selesai)) {
            return "Sedang Berlangsung";
        } else {
            return "Selesai";
        }
    }


}
