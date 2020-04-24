import Team6.entities.Picture;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testclass testing Picture.
 */
public class PictureTest {
    private static Picture picture;

    @BeforeAll
    static void beforeAll() throws IOException {
        picture = new Picture("./Team6.Controllers.test/garn.jpg");
    }

    @Test
    public void testMetadataExtractor(){
        assertNotNull(picture);
        //assertNotEquals(0, picture.getId());
        assertNotNull(picture.getFilepath());
        assertNotEquals(0, picture.getIso());
        assertNotEquals(0, picture.getShutterSpeed());
        assertNotEquals(0, picture.getExposureTime());
        assertNotEquals(true, picture.isFlashUsed());
        assertNotEquals(0, picture.getLatitude());
        assertNotEquals(0, picture.getLongitude());
        assertNotEquals(0, picture.getFileSize());
        assertNotNull(picture.getFileName());
    }
}
