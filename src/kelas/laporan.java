/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package kelas;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.JLabel;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.swing.JOptionPane;
/**
 *
 * @author HP
 */
public class laporan {
    public void formatDetailRegistrasi(
            String fileName,
            String titleText,
            List<String> extraHeaderLines, // Baris info tambahan seperti nama event, tanggal, dll.
            String[] tableHeaders,
            float[] columnWidths,
            DefaultTableModel model) {
        try {
            // Lokasi file
            String path = System.getProperty("user.home")
                    + "/OneDrive/Documents/laporan/"
                    + fileName + ".pdf";

            Document document = new Document(PageSize.A4);
            PdfWriter.getInstance(document, new FileOutputStream(path));
            document.open();

            // Font
            Font fontTitle = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
            Font fontSub = new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL);
            Font fontHeader = new Font(Font.FontFamily.HELVETICA, 11, Font.BOLD);
            Font fontCell = new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL);
            Font fontFooter = new Font(Font.FontFamily.HELVETICA, 9, Font.ITALIC);

            // Judul
            Paragraph title = new Paragraph(titleText, fontTitle);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);
            document.add(new Paragraph(" "));
            document.add(new Paragraph(" "));

            // Extra header lines (nama event, tanggal mulai, dll.)
            if (extraHeaderLines != null) {
                for (String line : extraHeaderLines) {
                    document.add(new Paragraph(line, fontSub));
                }
                document.add(new Paragraph(" "));
            }

            // Tabel
            PdfPTable table = new PdfPTable(tableHeaders.length);
            table.setWidthPercentage(100);
            if (columnWidths != null) {
                table.setWidths(columnWidths);
            }

            // Header tabel
            for (String h : tableHeaders) {
                PdfPCell cell = new PdfPCell(new Phrase(h, fontHeader));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);
            }

            // Isi tabel
            for (int i = 0; i < model.getRowCount(); i++) {
                for (int j = 0; j < model.getColumnCount(); j++) {
                    table.addCell(new Phrase(
                            model.getValueAt(i, j).toString(),
                            fontCell
                    ));
                }
            }

            document.add(table);

            // Footer
            document.add(new Paragraph(
                    "\nLaporan dibuat otomatis oleh sistem.",
                    fontFooter
            ));

            document.close();

            JOptionPane.showMessageDialog(null, "PDF berhasil dibuat!");

        } catch (Exception e) {
            System.out.println(e);
        }

    }

    public void formatLaporan(String filName, String titleText,
            String Periode, String[] headers, float[] columnWidths, DefaultTableModel model) {
        try {

            String path = System.getProperty("user.home") + "/OneDrive/Documents/laporan/" + filName + ".pdf";
            // Buat dokumen dan writer
            Document document = new Document(PageSize.A4);
            PdfWriter.getInstance(document, new FileOutputStream(path));
            document.open();
            // Font
            Font fontTitle = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
            Font fontSub = new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL);
            Font fontHeader = new Font(Font.FontFamily.HELVETICA, 11, Font.BOLD);
            Font fontCell = new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL);
            Font fontFooter = new Font(Font.FontFamily.HELVETICA, 9, Font.ITALIC);

            // Judul
            Paragraph title = new Paragraph(titleText, fontTitle);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);

            document.add(new Paragraph(" "));

            if (Periode != null) {
                document.add(new Paragraph(Periode, fontSub));
            }

            document.add(new Paragraph(" "));

            // Tabel
            PdfPTable table = new PdfPTable(headers.length);
            table.setWidthPercentage(100);
            if (columnWidths != null) {
                table.setWidths(columnWidths);
            }

            // Header
            for (String h : headers) {
                PdfPCell cell = new PdfPCell(new Phrase(h, fontHeader));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);
            }

            // Isi tabel
            for (int i = 0; i < model.getRowCount(); i++) {
                for (int j = 0; j < model.getColumnCount(); j++) {
                    table.addCell(new Phrase(
                            model.getValueAt(i, j) != null ? model.getValueAt(i, j).toString() : "",
                            fontCell
                    ));
                }
            }

            document.add(table);

            // Footer
            document.add(new Paragraph(
                    "\nLaporan dibuat otomatis oleh sistem.",
                    fontFooter
            ));

            document.close();
            JOptionPane.showMessageDialog(null, "PDF berhasil dibuat");
//            System.out.println("PDF berhasil dibuat: " + path);

        } catch (Exception e) {
            System.out.println(e);
        }

    }

    public void generateDataRegistrasi(String idKegiatan, JLabel lbNama, JLabel lbTgl) {
        try {
            registrasi regis = new registrasi();
            DefaultTableModel model = regis.tampilDataAkhir(idKegiatan, lbNama, lbTgl);

            // Header kolom tabel
            String[] tableHeaders = {"No", "ID Anggota", "Nama Anggota", "Status"};

            // Lebar kolom
            float[] widths = {0.5f, 2f, 5f, 1f};

            // Nama file (otomatis dengan tanggal)
            String tanggal = new java.text.SimpleDateFormat("yyyy-MM-dd")
                    .format(new java.util.Date());
            String fileName = "Laporan_Data_Anggota_Registrasi_PORSIGAL_" + tanggal;

            // Panggil method generik
            formatDetailRegistrasi(fileName, "Laporan Detail Registrasi",
                    List.of(
                            "Nama Kegiatan : " + lbNama.getText(),
                            "tanggal Mulai : " + lbTgl.getText()), tableHeaders, widths, model
            );

        } catch (Exception e) {
            System.out.println(e);
        }

    }

    public void generateLaporanEvent(Date tglAwal, Date tglAkhir) {
        try {
            ujian uj = new ujian();
            DefaultTableModel model;

            boolean isFiltered = (tglAwal != null && tglAkhir != null);

            // Tentukan model berdasarkan filter
            if (isFiltered) {
                model = uj.filterTable(tglAwal, tglAkhir);
            } else {
                model = uj.showKegiatan(); // tanpa filter
            }

            // Format tanggal hari ini (YYYY-MM-DD)
            String tanggal = new java.text.SimpleDateFormat("yyyy-MM-dd")
                    .format(new java.util.Date());

            // nama file
            String fileName;

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

            String periode;
            // Tentukan periode berdasarkan filter
            if (isFiltered) {
                String tglAwalFormatted = sdf.format(tglAwal);
                String tglAkhirFormatted = sdf.format(tglAkhir);

                periode = "Rentang: " + tglAwalFormatted + " s/d " + tglAkhirFormatted;
                fileName = "Laporan_Ujian_Filter_PORSIGAL_" + tanggal + ".pdf";

            } else {
                periode = "Rentang: - s/d - "; // tanpa filter
                fileName = "Laporan_Ujian_All_PORSIGAL_" + tanggal + ".pdf";
            }
            // Lebar kolom
            float[] widths = {1f, 3f, 5f, 3f, 3f, 3f, 5f};

            String[] headers = {
                "No", "ID Kegiatan", "Nama Kegiatan", "Tanggal Mulai",
                "Tanggal Selesai", "Lokasi", "Penguji"
            };

            formatLaporan(fileName, "Laporan Data Event PORSIGAL", periode, headers, widths, model);
        } catch (Exception e) {
            System.out.println(e);
        }

    }

    public void generateLaporanRegistrasi(Date tglAwal, Date tglAkhir) {

        try {
            registrasi regis = new registrasi();
            DefaultTableModel model = regis.filterTable(tglAwal, tglAkhir);

            boolean isFiltered = (tglAwal != null && tglAkhir != null);

            // Tentukan model berdasarkan filter
            if (isFiltered) {
                model = regis.filterTable(tglAwal, tglAkhir);
            } else {
                model = regis.showRegistrasi(); // tanpa filter
            }
            // Format tanggal hari ini (YYYY-MM-DD)
            String tanggal = new java.text.SimpleDateFormat("yyyy-MM-dd")
                    .format(new java.util.Date());

            // nama file
            String fileName;

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

            String periode;
            // Tentukan periode berdasarkan filter
            if (isFiltered) {
                String tglAwalFormatted = sdf.format(tglAwal);
                String tglAkhirFormatted = sdf.format(tglAkhir);

                periode = "Rentang: " + tglAwalFormatted + " s/d " + tglAkhirFormatted;
                fileName = "Laporan_Registrasi_Filter_PORSIGAL_" + tanggal + ".pdf";

            } else {
                periode = "Rentang: - s/d - "; // tanpa filter
                fileName = "Laporan_Registrasi_All_PORSIGAL_" + tanggal + ".pdf";
            }

            // Lebar kolom
            float[] widths = {1f, 3f, 5f, 3f, 3f, 3f, 5f};

            String[] headers = {
                "No", "ID Kegiatan", "Nama Kegiatan", "Tanggal Mulai",
                "Tanggal Selesai", "Lokasi", "Penguji"
            };
            formatLaporan(fileName, "Laporan Data Registrasi PORSIGAL", periode, headers, widths, model);

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void generateLaporanPelatih(String namaPelatih) {

        try {
            pelatih plt = new pelatih();
            DefaultTableModel model = plt.showPelatih();

            // Format tanggal hari ini (YYYY-MM-DD)
            String tanggal = new java.text.SimpleDateFormat("yyyy-MM-dd")
                    .format(new java.util.Date());

            // nama file
            String fileName = "Laporan_Pelatih_PORSIGAL_" + tanggal;
            String[] headers = {
                "No", "ID Pelatih", "Nama Pelatih",
                "No Handphone", "Sabuk", "Sertifikat"
            };

            float[] widths = {1f, 3f, 4f, 3f, 3f, 4f};
            formatLaporan(fileName, "Laporan Data Pelatih PORSIGAL", tanggal, headers, widths, model);

        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
