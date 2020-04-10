import entities.Picture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testclass testing Picture.
 */
public class PictureTest {
    private Picture picture;

    @BeforeEach
    public void beforeEach() throws IOException {
        this.picture = new Picture("C:\\Users\\sstn\\OneDrive - NTNU\\Programmering\\PictureApp\\test\\garn.jpg");
    }

    @Test
    public void testMetadataExtractor(){
        assertNotNull(picture);
        //assertNotEquals(0, picture.getId());
        assertNotNull(picture.getFilepath());
        assertNotEquals(0, picture.getISO());
        assertNotEquals(0, picture.getShutterSpeed());
        assertNotEquals(0, picture.getExposureTime());
        assertNotEquals(true, picture.isFlashUsed());
        assertNotEquals(0, picture.getLatitude());
        assertNotEquals(0, picture.getLongitude());
        assertNotEquals(0, picture.getFileSize());
        assertNotNull(picture.getFileName());
    }
}
