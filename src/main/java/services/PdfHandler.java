package services;


import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.layout.element.AreaBreak;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.property.AreaBreakType;
import entities.Album;

import java.io.*;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import com.itextpdf.kernel.pdf.*;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import entities.Picture;
import idk.AppLogger;

import javax.imageio.ImageIO;
import javax.print.Doc;


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
            PdfDocument pdfDocument = new PdfDocument(pdfWriter);
            return pdfDocument;
        } catch (FileNotFoundException ex) {
            AppLogger.getAppLogger().log(Level.FINE, ex.getMessage());
            AppLogger.closeHandler();
            return null;
        }
    }

    public void createPdfAlbum(ArrayList<Picture> pictures) throws FileNotFoundException {
        try {
            Document document = new Document(createPfdDocument());
            pictures.forEach(x -> {
                try {
                    ImageData imageData = ImageDataFactory.create(x.getFilepath());
                    Image pdfImage = new Image(imageData);
                    document.add(new AreaBreak(new PageSize(pdfImage.getImageWidth(), pdfImage.getImageHeight())));
                    document.add(pdfImage);
                } catch (MalformedURLException ex) {
                    AppLogger.getAppLogger().log(Level.FINE, ex.getMessage());
                    AppLogger.closeHandler();
                }
            });
            document.close();
        } catch (FileNotFoundException ex) {
            AppLogger.getAppLogger().log(Level.FINE, ex.getMessage());
            AppLogger.closeHandler();
        }
    }
}

