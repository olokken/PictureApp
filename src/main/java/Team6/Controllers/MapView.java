package Team6.Controllers;

import Team6.services.AppLogger;
import com.lynden.gmapsfx.GoogleMapView;
import com.lynden.gmapsfx.MapComponentInitializedListener;
import com.lynden.gmapsfx.javascript.event.UIEventType;
import com.lynden.gmapsfx.javascript.object.*;
import Team6.entities.Picture;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.*;
import java.util.logging.Level;

public class MapView extends Base implements MapComponentInitializedListener, Initializable {
    @FXML
    GoogleMapView mapView;

    GoogleMap map;

    List<Picture> pictures = Context.getInstance().currentAlbum().getPictures();

    @Override
    public void mapInitialized() {
        //Set the initial properties of the map.
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
                         switchScene("MapView", "PictureView");
                     } catch (IOException ex) {
                         AppLogger.getAppLogger().log(Level.FINE, ex.getMessage());
                         AppLogger.closeHandler();
                     }
                 });
             }
        });
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        mapView.setKey(getProperties().get("GOOGLE_API_KEY").toString());
        mapView.addMapInializedListener(this);
    }

    @FXML
    private void switchToAlbumView() {
        try{
            switchScene("MapView", "AlbumView");
        } catch (IOException ex){
            AppLogger.getAppLogger().log(Level.FINE, ex.getMessage());
            AppLogger.closeHandler();
        }
    }
    private java.util.Map getProperties() {
        java.util.Map result = new HashMap();
        try (InputStream input = new FileInputStream("config.properties")) {
            Properties prop = new Properties();
            prop.load(input);
            result.put("GOOGLE_API_KEY", prop.getProperty("GOOGLE_API_KEY"));
        } catch (IOException ex) {
            AppLogger.getAppLogger().log(Level.FINE, ex.getMessage());
            AppLogger.closeHandler();
        }
        return result;
    }

}
