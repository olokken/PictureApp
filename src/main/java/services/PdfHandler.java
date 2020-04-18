package services;


import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.layout.element.AreaBreak;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.property.AreaBreakType;
import entities.Album;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import com.itextpdf.kernel.pdf.*;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import entities.Picture;


public class PdfHandler {

    public PdfHandler(){

    }
    public void createAlbumPdf(Album album){
        try{
            File file = new File(album.getName().trim());
            PdfWriter pdfWriter = new PdfWriter(file);
            PdfDocument pdfDocument = new PdfDocument(pdfWriter);


            ArrayList<Picture> pictures = new ArrayList<>(album.getPictures());
            //Koden under oppretter et objekt av klassen Image som blir brukt til å få riktig størrelse på første side
            Picture picture1 = pictures.get(0);
            ImageData image1Data = ImageDataFactory.create(picture1.getFileName());
            Image image1 = new Image(image1Data);


            Document document = new Document(pdfDocument, new PageSize(image1.getImageWidth(), image1.getImageHeight()));
            Paragraph paragraph = new Paragraph(album.getName());//Nødvendig?
            document.add(paragraph); // kan endre font og størrelse. Egen side?
            document.add(image1);

            for(int i = 1; i <= pictures.size(); i++){
                //Oppretter et objekt av Image-klassen fra ArrayList
                ImageData imageData = ImageDataFactory.create(pictures.get(i).getFileName());
                Image pdfImage = new Image(imageData);

                //Ny side
                document.add(new AreaBreak(new PageSize(pdfImage.getImageWidth(), pdfImage.getImageHeight())));

                //Legger til bildet
                document.add(pdfImage);

                //document.add(new AreaBreak(AreaBreakType.NEXT_PAGE)); //vet ikke om denne fungerer
            }

            document.close();


        }catch (FileNotFoundException e) {
            e.printStackTrace();
        }catch(IOException e) {
            e.printStackTrace();
        }

    }

}
