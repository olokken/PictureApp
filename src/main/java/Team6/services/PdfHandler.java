package Team6.services;


import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.AreaBreak;
import com.itextpdf.layout.element.Image;
import Team6.entities.Picture;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.util.List;
import java.util.logging.Level;

/**
 * Holds information about how to create a pdf-file.
 *
 * @author Team 6
 * @version 2020.04.24
 */
public class PdfHandler {

    /**
     * Constructor that creates an instance of the PdfHandler, initialising the instance.
     */
    public PdfHandler(){}

    /**
     * Creates a PdfDocument.
     * If a PdfWriter object can't be made, a
     * {@link FileNotFoundException} will be thrown.
     *
     * @return The PdfDocument.
     */
    public PdfDocument createPfdDocument() {
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

    /**
     * Creates a Pdf album with the given pictures.
     * If the pdf album can't be created, a
     * {@link MalformedURLException} will be thrown.
     *
     * @param pictures List with pictures.
     */
    public boolean createPdfAlbum(List<Picture> pictures) {
        Document document = new Document(createPfdDocument(), PageSize.A4);
        pictures.forEach(x -> {
            try {
                File file = new File(x.getFilepath());
                if (file.exists()) {
                    ImageData imageData = ImageDataFactory.create(x.getFilepath());
                    Image pdfImage = new Image(imageData);
                    document.add(new AreaBreak(new PageSize(PageSize.A4)));
                    document.add(pdfImage.setAutoScale(true));
                }
            } catch (Exception ex) {
                AppLogger.getAppLogger().log(Level.FINE, ex.getMessage());
                AppLogger.closeHandler();
            }
        });
        document.close();
        return true;
    }
}

