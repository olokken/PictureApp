package Team6;

import com.lynden.gmapsfx.GoogleMapView;
import com.lynden.gmapsfx.MapComponentInitializedListener;
import com.lynden.gmapsfx.javascript.event.UIEventType;
import com.lynden.gmapsfx.javascript.object.*;
import com.lynden.gmapsfx.util.MarkerImageFactory;
import entities.Album;
import entities.Picture;
import idk.AppLogger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import netscape.javascript.JSObject;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.*;
import java.util.logging.Level;
import java.util.stream.Collectors;

public class MapController implements MapComponentInitializedListener, Initializable {
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
                 map.addUIEventHandler(marker, UIEventType.click, (JSObject) -> {
                     try {
                         int index = pictures.indexOf(x);
                         Context.getInstance().setIndex(index);
                         Context.getInstance().setLastScene("map");
                         App.setRoot("tertiary");
                     } catch (IOException e) {
                         AppLogger.getAppLogger().log(Level.FINE, e.getMessage());
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
    private void switchToSecondary() throws IOException {
        try{
            Context.getInstance().currentAlbum().setPictures(null);
            App.setRoot("secondary");
        } catch (IOException e){
            AppLogger.getAppLogger().log(Level.FINE, e.getMessage());
            AppLogger.closeHandler();
        }
    }
    private Map getProperties() {
        Map result = new HashMap();
        try (InputStream input = new FileInputStream("config.properties")) {
            Properties prop = new Properties();
            prop.load(input);
            result.put("GOOGLE_API_KEY", prop.getProperty("GOOGLE_API_KEY"));
        } catch (IOException e) {
            AppLogger.getAppLogger().log(Level.FINE, e.getMessage());
            AppLogger.closeHandler();
        }
        return result;
    }

}
