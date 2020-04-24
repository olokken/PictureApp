package Team6;

import com.lynden.gmapsfx.GoogleMapView;
import com.lynden.gmapsfx.MapComponentInitializedListener;
import com.lynden.gmapsfx.javascript.event.UIEventType;
import com.lynden.gmapsfx.javascript.object.*;
import entities.Picture;
import idk.AppLogger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.*;
import java.util.logging.Level;

/**
 * Controller map view.
 *
 * @author Team 6
 * @version 2020.04.24
 */
public class MapController extends BaseController implements MapComponentInitializedListener, Initializable {
    @FXML
    GoogleMapView mapView;

    GoogleMap map;

    List<Picture> pictures = Context.getInstance().currentAlbum().getPictures();

    /**
     * Initialize the map.
     */
    @Override
    public void mapInitialized() {
        MapOptions mapOptions = new MapOptions();

        mapOptions.center(new LatLong(63.446827, 10.421906))
                .mapType(MapTypeIdEnum.ROADMAP)
                .overviewMapControl(false)
                .panControl(false)
                .rotateControl(false)
                .scaleControl(false)
                .streetViewControl(false)
                .zoomControl(true)
                .zoom(12);

        map = mapView.createMap(mapOptions);
        createMarkers();
    }

    /**
     * Creates markers for the pictures in the albums.
     * If the latitude and longitude is 0, no marker will be set.
     */
    void createMarkers () {
         pictures.stream().forEach(x -> {
             if(x.getLatitude() != 0 && x.getLongitude() != 0){
                 MarkerOptions markerOptions = new MarkerOptions();
                 markerOptions.position(new LatLong(x.getLatitude(), x.getLongitude()))
                         .visible(true);
                 Marker marker = new Marker(markerOptions);
                 map.addMarker(marker);
                 map.addUIEventHandler(marker, UIEventType.click, JSObject -> {
                     try {
                         int index = pictures.indexOf(x);
                         Context.getInstance().setIndex(index);
                         switchScene("map", "tertiary");
                     } catch (IOException ex) {
                         AppLogger.getAppLogger().log(Level.FINE, ex.getMessage());
                         AppLogger.closeHandler();
                     }
                 });
             }
        });
    }

    /**
     * Initialize the map view and sets google API key.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        mapView.setKey(getProperties().get("GOOGLE_API_KEY").toString());
        mapView.addMapInializedListener(this);
    }

    /**
     * Switches from map to secondary view.
     */
    @FXML
    private void switchToSecondary() {
        try{
            switchScene("map", "secondary");
        } catch (IOException ex){
            AppLogger.getAppLogger().log(Level.FINE, ex.getMessage());
            AppLogger.closeHandler();
        }
    }
    private Map getProperties() {
        Map result = new HashMap();
        try (InputStream input = new FileInputStream("config.properties")) {
            Properties prop = new Properties();
            prop.load(input);
            result.put("GOOGLE_API_KEY", prop.getProperty("GOOGLE_API_KEY"));
            return result;
        } catch (IOException ex) {
            AppLogger.getAppLogger().log(Level.FINE, ex.getMessage());
            AppLogger.closeHandler();
            return null;
        }
    }

}
