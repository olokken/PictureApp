package services;
import com.itextpdf.*;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import entities.Album;

import java.io.File;

public class AlbumToPdf {

    //public AlbumToPdf() { }

    public void createPdf(Album album){
        File file = new File(album.getName()+".pdf");
        //Update
        //PdfWriter pdfWriter = new PdfWriter(file);
        //PdfDocument pdfDocument = new PdfDocument(pdfWriter);
    }
}
