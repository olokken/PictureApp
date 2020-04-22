package services;


import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Canvas;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.AreaBreak;
import com.itextpdf.layout.element.Image;
import entities.Picture;
import idk.AppLogger;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.logging.Level;


public class PdfHandler {

    public PdfHandler(){

    }
    public PdfDocument createPfdDocument() throws FileNotFoundException {
        try{
            int num = 0;
            String filePath = System.getProperty("user.home") + "/Downloads/images.pdf";
            File file = new File(filePath);
            while (file.exists()){
                num++;
                filePath = System.getProperty("user.home") + "/Downloads/images(" + num + ").pdf";
                file = new File(filePath);
            }
            PdfWriter pdfWriter = new PdfWriter(file);
            return new PdfDocument(pdfWriter);
        } catch (FileNotFoundException ex) {
            AppLogger.getAppLogger().log(Level.FINE, ex.getMessage());
            AppLogger.closeHandler();
            return null;
        }
    }

    public boolean createPdfAlbum(ArrayList<Picture> pictures) throws FileNotFoundException {
        try {
            Document document = new Document(createPfdDocument(), PageSize.A4);
            pictures.forEach(x -> {
                try {
                    ImageData imageData = ImageDataFactory.create(x.getFilepath());
                    Image pdfImage = new Image(imageData);
                    document.add(new AreaBreak(new PageSize(PageSize.A4)));
                    document.add(pdfImage.setAutoScale(true));
                } catch (MalformedURLException ex) {
                    AppLogger.getAppLogger().log(Level.FINE, ex.getMessage());
                    AppLogger.closeHandler();
                }
            });
            document.close();
            return true;
        } catch (FileNotFoundException ex) {
            AppLogger.getAppLogger().log(Level.FINE, ex.getMessage());
            AppLogger.closeHandler();
            return false;
        }
    }
}

