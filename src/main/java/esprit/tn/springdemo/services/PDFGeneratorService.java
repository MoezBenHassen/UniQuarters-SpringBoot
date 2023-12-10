package esprit.tn.springdemo.services;


import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.html.HtmlWriter;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import esprit.tn.springdemo.entities.Bloc;
import esprit.tn.springdemo.repositories.BlocRepo;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

@Component
public class PDFGeneratorService {
    @Autowired
    private BlocRepo blocRepo;

    public void export(HttpServletResponse response) throws DocumentException, IOException {
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, response.getOutputStream());

        document.open();

        // Add Logo
        com.lowagie.text.Image jpg = com.lowagie.text.Image.getInstance("Logo_ESPRIT_Ariana.jpg");
        jpg.scalePercent(5);
        jpg.setAlignment(Image.ALIGN_TOP);
        document.add(jpg);

        // Add Title
        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, Color.PINK);
        Paragraph title = new Paragraph("Liste des blocs", titleFont);
        title.setAlignment(Paragraph.ALIGN_CENTER);
        document.add(title);

        // Add Empty Line
        document.add(Chunk.NEWLINE);

        // Add Table Header
        PdfPTable table = new PdfPTable(3); // Number of columns
        table.setWidthPercentage(100);
        writeTableHeader(table);

        // Add Table Data
        writeTableData(table);

        // Add Table to Document
        document.add(table);

        // Add Signature
        Font signatureFont = FontFactory.getFont(FontFactory.HELVETICA_OBLIQUE, 12);
        Paragraph signature = new Paragraph("Signature", signatureFont);
        signature.setAlignment(Paragraph.ALIGN_RIGHT);
        document.add(signature);

        document.close();
    }

    private void writeTableHeader(PdfPTable table) {
        Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, Color.WHITE);
        PdfPCell cell = new PdfPCell();

        cell.setBackgroundColor(Color.GRAY);
        cell.setPadding(7);

        cell.setPhrase(new Phrase("ID", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Nom", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Capacité", font));
        table.addCell(cell);
    }

    private void writeTableData(PdfPTable table) {
        java.util.List<Bloc> blocs = (List<Bloc>) blocRepo.findAll();
        Font font = FontFactory.getFont(FontFactory.HELVETICA, 12);

        for (Bloc b : blocs) {
            table.addCell(new Phrase(String.valueOf(b.getId()), font));
            table.addCell(new Phrase(String.valueOf(b.getNom()), font));
            table.addCell(new Phrase(String.valueOf(b.getCapacite()), font));
        }
    }
}
