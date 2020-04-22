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
    public void createAlbumPdf(ArrayList<Picture> pictures){
        try{
            String filePath = System.getProperty("user.home") + "\\Downloads\\images.pdf";
            File file = new File(filePath);
            PdfWriter pdfWriter = new PdfWriter(file);
            PdfDocument pdfDocument = new PdfDocument(pdfWriter);
            Picture picture1 = pictures.get(0);
            ImageData image1Data = ImageDataFactory.create(picture1.getFilepath());
            Image image1 = new Image(image1Data);
            Document document = new Document(pdfDocument, new PageSize(image1.getImageWidth(), image1.getImageHeight()));
            document.add(image1);
            pictures.forEach(x -> {
                try {
                    if (x != pictures.get(0)) {
                        ImageData imageData = ImageDataFactory.create(x.getFilepath());
                        Image pdfImage = new Image(imageData);
                        document.add(new AreaBreak(new PageSize(pdfImage.getImageWidth(), pdfImage.getImageHeight())));
                        document.add(pdfImage);
                    }
                    } catch(MalformedURLException m){
                        AppLogger.getAppLogger().log(Level.FINE, m.getMessage());
                        AppLogger.closeHandler();
                    }
            });
            document.close();
        } catch (IOException ex) {
            AppLogger.getAppLogger().log(Level.FINE, ex.getMessage());
            AppLogger.closeHandler();
        }
    }




}
