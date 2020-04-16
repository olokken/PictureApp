package Team6;

import com.lynden.gmapsfx.GoogleMapView;
import com.lynden.gmapsfx.MapComponentInitializedListener;
import com.lynden.gmapsfx.javascript.object.*;
import entities.Album;
import entities.Picture;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class MapController implements MapComponentInitializedListener, Initializable {
    @FXML
    GoogleMapView mapView;
    @FXML
    GoogleMap map;

    List<Picture> pictures = Context.getInstance().currentAlbum().getPictures();

    //InfoBilde over markers

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
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(new LatLong(x.getLatitude(), x.getLongitude()))
                    .visible(true);
            Marker marker = new Marker(markerOptions);
            map.addMarker(marker);
            InfoWindowOptions infoWindowOptions = new InfoWindowOptions();
            String imageUrl = "<img src=" + x.getFilepath() + ">";
            infoWindowOptions.content(imageUrl);
            InfoWindow infoWindow = new InfoWindow(infoWindowOptions);
            infoWindow.open(map, marker);
        });
    }



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        mapView.addMapInializedListener(this);
    }

    @FXML
    private void switchToSecondary() throws IOException {
        Context.getInstance().currentAlbum().setPictures(null);
        App.setRoot("secondary");
    }

}
